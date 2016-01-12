package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Das PublicConversationViewer Formular sorgt fuer die Anzeige der �ffentlichen
 * Nachrichten im Details-Bereich der GUI, nachdem man einen abonnierten User
 * ausgew�hlt hat
 * 
 *
 */
public class PublicConversationViewer extends TextyForm {

	private final static long ONE_MINUTE = 60;
	private final static long ONE_HOURS = 60 * ONE_MINUTE;
	private final static long ONE_DAYS = 24 * ONE_HOURS;
	private final static long ONE_MONTH = 30 * ONE_DAYS;

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
	private User user = new User();

	/**
	 * Der Konstruktor erzwingt die Eingabe einer �berschrift f�r das Formular.
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
		 * Falls der ausgew�hlte User noch keine oeffentliche Nachricht gepostet
		 * hat, wird ein entsprechender Hinweis angezeigt
		 */
		if (conversationListOfUser.size() == 0) {
			text.setText("Oops! This user still doesn't have any public conversations!");
		} else {
			user = conversationListOfUser.firstElement().getFirstMessage()
					.getAuthor();
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
		 * Nachdem das Formular aufgebaut ist, wird die H�he des jeweiligen
		 * Panels ausgelesen und als H�he der Scrollbars gesetzt.
		 */
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
				mainPanel.add(scroll);
			}
		});
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
			 * F�r jeden Hashtag in der ListOfHashtag wird das Keyword
			 * ausgelesen und der keywordList hinzugef�gt
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

			wrapper.addClickHandler(createClickHandler(conversation, chatPanel, messageTable));

			/*
			 * Hinzuf�gen der Widgets in die jeweilige Spalte der FlexTable
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
					VerticalPanel contentMessage = new VerticalPanel();

					ScrollPanel scrollMessage = new ScrollPanel(contentMessage);
					chatPanel.add(scrollMessage);
					contentMessage.getElement().setId("fullWidth");
					scrollMessage.setHeight("200px");

					for (int i = 1; i < conversation.getListOfMessage().size(); i++) {
						SingleMessageView singleMessage = new SingleMessageView(
								conversation.getListOfMessage().get(i), user,
								conversation);
						singleMessage.getElement().setId(
								"singlePublicConversation");
						contentMessage.add(singleMessage);
					}
					Button replyButton = createReplyButton(conversation);
					replyButton.getElement().setId("button");
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
	private Button createReplyButton(final Conversation conversation) {
		Button replyButton = new Button("Reply", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				/*
				 * Entfernung der Child Widgets vom jeweiligen Parent Widget.
				 */
				RootPanel.get("Info").clear();

				/*
				 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
				 */
				RootPanel.get("Info").add(
						new ReplyMessageForm(
								"Reply to this public conversation",
								conversation, false));
			}
		});
		return replyButton;
	}

}
