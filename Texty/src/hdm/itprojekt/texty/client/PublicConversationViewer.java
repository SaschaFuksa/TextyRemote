package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Date;
import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Das PublicConversationViewer Formular sorgt fuer die Anzeige der öffentlichen
 * Nachrichten im Details-Bereich der GUI, nachdem man einen abonnierten User
 * ausgewählt hat
 * 
 *
 */
public class PublicConversationViewer extends TextyForm {

	/**
	 * Der LOG gibt eine mögliche Exception bzw. den Erfolg des asynchronen
	 * Callbacks aus.
	 */
	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	private final static long ONE_MINUTE = 60;
	private final static long ONE_HOURS = 60 * ONE_MINUTE;
	private final static long ONE_DAYS = 24 * ONE_HOURS;
	private final static long ONE_MONTH = 30 * ONE_DAYS;

	/**
	 * Zeitintervall des automatischen Refresh.
	 */
	private static final int REFRESH_INTERVAL = 5000;

	/**
	 * Deklaration, Definition und Initialisierung der Widget.
	 */
	private Vector<Conversation> conversationListOfUser = new Vector<Conversation>();
	private FlexTable conversationTable = new FlexTable();
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label text = new Label(
			"Here you can see all public messages from your subscribed user!");

	/**
	 * Deklaration, Definition und Initialisierung BO.
	 */
	private User currentUser = new User();

	/**
	 * Die administration ermöglicht die asynchrone Kommunikation mit der
	 * Applikationslogik.
	 */
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	/**
	 * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular.
	 * Des weiteren werden alle oeffentlichen Unterhaltungen des Users
	 * uebergeben
	 * 
	 * @param headline
	 * @param conversationListofUser
	 */
	public PublicConversationViewer(String headline,
			Vector<Conversation> conversationListofUser) {
		super(headline);
		this.conversationListOfUser = conversationListofUser;
	}

	/**
	 * Modifiziert den Standardkonstruktor, um die run() Operation bei der
	 * Initialisierung aufzurufen.
	 * 
	 * @see TextyForm
	 */
	@Override
	protected void run() {

		/*
		 * Holt den aktuellen User aus der Datenbank.
		 */
		administration.getCurrentUser(getCurrentUserExecute());

		/*
		 * Falls der ausgewählte User noch keine oeffentliche Nachricht gepostet
		 * hat, wird ein entsprechender Hinweis angezeigt
		 */
		if (conversationListOfUser.size() == 0) {
			text.setText("Oops! This user still doesn't have any public conversations!");
		}

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("conversationWrapper");
		scroll.getElement().setId("conversationScroll");
		content.getElement().setId("fullWidth");
		conversationTable.getElement().setId("fullWidth");
		text.getElement().setId("blackFont");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		mainPanel.add(getHeadline());
		mainPanel.add(text);

		content.add(conversationTable);

		showConversation();

		this.add(mainPanel);

		/*
		 * Nachdem das Formular aufgebaut ist, wird die Höhe des jeweiligen
		 * Panels ausgelesen und als Höhe der Scrollbars gesetzt.
		 */
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
				mainPanel.add(scroll);
			}
		});
	}

	/**
	 * AsyncCallback für das Auslesen des aktuellen User aus der Datenbank.
	 * 
	 * @return
	 */
	private AsyncCallback<User> getCurrentUserExecute() {
		AsyncCallback<User> asyncCallback = new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(User result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				/*
				 * Übergibt das result an currentUser und zeigt im Anschluss
				 * alle Nachrichten der Unterhaltung an.
				 */
				currentUser = result;
			}
		};
		return asyncCallback;
	}

	/**
	 * AsyncCallback zur Überprüfung der Aktualität der Unterhaltung.
	 */
	private AsyncCallback<Vector<Message>> getRecentMessagesExecute(
			final Conversation conversation,
			final VerticalPanel contentMessage, final Button replyButton) {
		AsyncCallback<Vector<Message>> asyncCallback = new AsyncCallback<Vector<Message>>() {

			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());

			}

			@Override
			public void onSuccess(Vector<Message> result) {
				/*
				 * Überprüfung ob neue Nachrichten in der Unterhaltung sind.
				 */
				if (result.size() > 0) {

					replyButton.removeFromParent();

					for (Message message : result) {
						conversation.addMessageToConversation(message);
						SingleMessageView messageView = new SingleMessageView(
								message, currentUser, conversation);

						messageView.getElement().setId(
								"singlePublicConversation");

						/*
						 * Fügt die neue Nachricht dem Panel zu.
						 */
						contentMessage.add(messageView);
					}

					contentMessage.add(replyButton);
				}

			}

		};
		return asyncCallback;
	}

	/**
	 * Methode zur Überprüfung der Aktualität der Unterhaltung.
	 */
	private void checkNewMessage(Conversation conversation,
			VerticalPanel contentMessage, Button replyButton) {
		administration.getRecentMessages(
				conversation.getLastMessage(),
				getRecentMessagesExecute(conversation, contentMessage,
						replyButton));
	}

	/**
	 * Diese Methode zeigt die Unterhaltung an
	 */
	private void showConversation() {

		int index = 0;
		for (final Conversation conversation : conversationListOfUser) {
			VerticalPanel chatPanel = new VerticalPanel();
			FlexTable messageTable = new FlexTable();
			FocusPanel wrapper = new FocusPanel();

			/*
			 * Zuweisung der Styles an das jeweilige Widget.
			 */
			chatPanel.getElement().setId("fullWidth");
			messageTable.getElement().setId("conversation");

			String keywordList = new String();

			/*
			 * Für jeden Hashtag in der ListOfHashtag wird das Keyword
			 * ausgelesen und der keywordList hinzugefügt
			 */
			for (Hashtag hashtag : conversation.getListOfMessage()
					.firstElement().getListOfHashtag()) {
				keywordList = keywordList + "#" + hashtag.getKeyword() + " ";
			}

			/*
			 * Holt sich den Zeitpunkt der Erstellung
			 */
			String dateString = DateTimeFormat.getFormat("HH:mm").format(
					conversation.getListOfMessage().firstElement()
							.getDateOfCreation());

			Date afterDate = new Date();
			Date baseDate = conversation.getListOfMessage().firstElement()
					.getDateOfCreation();

			long afterTime = afterDate.getTime() / 1000;
			long baseTime = baseDate.getTime() / 1000;

			long duration = afterTime - baseTime;

			/*
			 * Unterschiedliche Behandlung der Datumanzeige
			 */
			if (duration < ONE_DAYS) {
				String baseDay = DateTimeFormat.getFormat("dd")
						.format(baseDate);
				String afterDay = DateTimeFormat.getFormat("dd").format(
						afterDate);

				if (!baseDay.equals(afterDay)) {
					dateString = "yesterday at " + dateString;
				}

			} else if (duration > ONE_DAYS && duration < ONE_MONTH) {
				dateString = DateTimeFormat.getFormat("dd.MM").format(baseDate)
						+ " at "
						+ DateTimeFormat.getFormat("HH:mm").format(baseDate);
			}

			else if (duration > ONE_MONTH) {
				dateString = DateTimeFormat.getFormat("MM.yyyy").format(
						baseDate)
						+ " at "
						+ DateTimeFormat.getFormat("dd").format(baseDate)
						+ ", "
						+ DateTimeFormat.getFormat("HH").format(baseDate) + "h";
			}

			String text = conversation.getListOfMessage().firstElement()
					.getText();

			Label hashtagLabel = new Label(keywordList);
			Label dateLabel = new Label(dateString);
			Label textLabel = new Label(text);

			wrapper.addClickHandler(createClickHandler(conversation, chatPanel,
					messageTable));

			/*
			 * Hinzufügen der Widgets in die jeweilige Spalte der FlexTable
			 */
			messageTable.setWidget(0, 0, dateLabel);
			messageTable.setWidget(1, 0, textLabel);
			messageTable.setWidget(2, 0, hashtagLabel);

			wrapper.add(messageTable);

			conversationTable.setWidget(index, 0, wrapper);
			conversationTable.setWidget(index + 1, 0, chatPanel);
			index = index + 2;
		}

	} // Ende for-Schleife

	private ClickHandler createClickHandler(final Conversation conversation,
			final VerticalPanel chatPanel, final FlexTable messageTable) {
		ClickHandler clickHandler = new ClickHandler() {
			boolean state = true;

			@Override
			public void onClick(ClickEvent event) {

				if (state) {

					final VerticalPanel contentMessage = new VerticalPanel();
					final Button replyButton = createReplyButton(conversation,
							contentMessage);
					replyButton.getElement().setId("button");

					ScrollPanel scrollMessage = new ScrollPanel(contentMessage);
					chatPanel.add(scrollMessage);
					contentMessage.getElement().setId("fullWidth");
					scrollMessage.setHeight("200px");

					/*
					 * Timer um eine automatische Überprüfung vorzunehmen, ob
					 * eine neue Nachricht der Unterhaltung hinzugefügt wurde.
					 */
					Timer refreshTimer = new Timer() {
						@Override
						public void run() {
							checkNewMessage(conversation, contentMessage,
									replyButton);
						}
					};
					refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

					for (int i = 1; i < conversation.getListOfMessage().size(); i++) {
						SingleMessageView singleMessage = new SingleMessageView(
								conversation.getListOfMessage().get(i),
								currentUser, conversation);
						singleMessage.getElement().setId(
								"singlePublicConversation");
						contentMessage.add(singleMessage);
					}
					contentMessage.add(replyButton);
					scrollMessage.scrollToBottom();
					chatPanel.getElement().setId("publicMessageBottom");
					messageTable.getElement().setId("publicMessageHead");
					state = false;
				} else {
					chatPanel.clear();
					chatPanel.getElement().setId("fullWidth");
					messageTable.getElement().setId("conversation");
					state = true;
				}

			}
		};
		return clickHandler;
	}

	/**
	 * Button zum Antworten in Unterhaltungen.
	 * 
	 * @return
	 */
	private Button createReplyButton(final Conversation conversation,
			final VerticalPanel contentMessage) {
		final Button replyButton = new Button("Reply");
		replyButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				/*
				 * Entfernung der Child Widgets vom jeweiligen Parent Widget.
				 */
				RootPanel.get("Info").clear();

				/*
				 * Instanziierung einer neuen Antwort.
				 */
				ReplyMessageForm replyMessage = new ReplyMessageForm(
						"Reply to conversation");

				/*
				 * Zuweisung der Handler an das jeweilige Widget.
				 */
				replyMessage.message.sendButton
						.addClickHandler(createClickHandler(conversation,
								contentMessage, replyMessage, replyButton));

				/*
				 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
				 */
				RootPanel.get("Info").add(replyMessage);
			}
		});
		return replyButton;
	}

	/**
	 * Erzeugt einen Handler für den SendButton.
	 * 
	 * @return
	 */
	private ClickHandler createClickHandler(final Conversation conversation,
			final VerticalPanel contentMessage,
			final ReplyMessageForm replyMessage, final Button replyButton) {
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*
				 * Fügt die neue Nachricht der bestehenden Unterhaltung hinzu.
				 */
				administration.addMessageToConversation(
						conversation.getLastMessage(),
						conversation.getId(),
						replyMessage.message.getText(),
						replyMessage.message.getHashtag(),
						addMessageToConversationExecute(conversation,
								contentMessage, replyButton));
			}
		};
		return clickHandler;
	}

	/**
	 * AsyncCallback für das hinzufügen einer neuen Nachricht in einer
	 * bestehenden Unterhaltung.
	 * 
	 * @return
	 */
	private AsyncCallback<Message> addMessageToConversationExecute(
			final Conversation conversation, final VerticalPanel contentMessage, final Button replyButton) {
		AsyncCallback<Message> asyncCallback = new AsyncCallback<Message>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Message result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				/*
				 * Entfernung der Child Widgets vom jeweiligen Parent Widget.
				 */
				RootPanel.get("Navigator").clear();
				RootPanel.get("Info").clear();
				replyButton.removeFromParent();

				/*
				 * Füge die neue Message der Unterhaltung hinzu.
				 */
				conversation.addMessageToConversation(result);

				/*
				 * Erstelle ein Panel für die neue Nachricht.
				 */
				SingleMessageView messageView = new SingleMessageView(result,
						currentUser, conversation);

				/*
				 * Zuweisung der Styles an das jeweilige Widget.
				 */
				messageView.getElement().setId("singlePublicConversation");

				/*
				 * Fügt die neue Nachricht dem Panel zu.
				 */
				contentMessage.add(messageView);

				contentMessage.add(replyButton);

				/*
				 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
				 */
				RootPanel.get("Navigator").add(
						new HomeForm("Home"));
			}

		};
		return asyncCallback;
	}

}
