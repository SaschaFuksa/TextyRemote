package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Collections;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Das ConversationForm Formular zeigt alle privaten Unterhaltungen an, in denen
 * der aktuelle Nutzer Teilnehmer ist.
 */
public class ConversationForm extends TextyForm {

	/**
	 * Der LOG gibt eine mögliche Exception bzw. den Erfolg des asynchronen
	 * Callbacks aus.
	 */
	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	/**
	 * Festlegung einzelner Zeitintervalle
	 */
	private final static long ONE_MINUTE = 60;
	private final static long ONE_HOURS = 60 * ONE_MINUTE;
	private final static long ONE_DAYS = 24 * ONE_HOURS;
	private final static long ONE_MONTH = 30 * ONE_DAYS;

	/**
	 * Deklaration, Definition und Initialisierung der Widget.
	 */
	private Button newMessageButton = createNewMessageButton();
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label text = new Label(
			"Write a new message or read your private conversations!");
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);

	/**
	 * Deklaration, Definition und Initialisierung BO. isReplyed gibt an, ob das
	 * Formular dadurch geladen wird, weil in einer Unterhaltung geantwortet
	 * wurde und das Formular aktualisiert werden muss.
	 */
	private Conversation conversation = new Conversation();
	private Vector<Conversation> conversationList = new Vector<Conversation>();
	private boolean isReplyed = false;

	/**
	 * Die administration ermöglicht die asynchrone Kommunikation mit der
	 * Applikationslogik.
	 */
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	/**
	 * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular.
	 * 
	 * @see TextyForm
	 * @param headline
	 */
	public ConversationForm(String headline) {
		super(headline);
	}

	/**
	 * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular
	 * und übergibt die veränderte Unterhaltung. Der Konstruktor wird nur
	 * angewandt, wenn auf einer Unterhaltung geantwortet wurde, dadurch ändert
	 * sich zudem isReplyed zu true. Formular.
	 * 
	 * @see TextyForm
	 * @param headline
	 * @param conversation
	 */
	public ConversationForm(String headline, Conversation conversation) {
		super(headline);
		this.conversation = conversation;
		this.isReplyed = true;
	}

	/**
	 * Diese Methode wird sofort aufgerufen, sobald ein Formular im Browser
	 * eingebaut wird.
	 * 
	 * @see TextyForm
	 */
	@Override
	protected void run() {

		/*
		 * Lädt alle Unterhaltungen in welchen der aktuelel User teilnehmer ist.
		 */
		administration
				.getAllConversationsFromUser(getAllConversationsFromUserExecute());

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("conversationWrapper");
		scroll.getElement().setId("conversationScroll");
		content.getElement().setId("conversationContent");
		newMessageButton.getElement().setId("button");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		mainPanel.add(getHeadline());
		mainPanel.add(text);
		mainPanel.add(scroll);
		mainPanel.add(newMessageButton);

		this.add(mainPanel);

		/*
		 * Nachdem das Formular aufgebaut ist, wird die Höhe des jeweiligen
		 * Panels ausgelesen und als Höhe der Scrollbars gesetzt.
		 */
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
			}
		});

	}

	/**
	 * AsyncCallback für das Auslesen aller Unterhaltungen in welchen der
	 * aktuelel User teilnehmer ist.
	 * 
	 * @return
	 */
	private AsyncCallback<Vector<Conversation>> getAllConversationsFromUserExecute() {
		AsyncCallback<Vector<Conversation>> asyncCallback = new AsyncCallback<Vector<Conversation>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<Conversation> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());

				/*
				 * Übergibt das result an den Vector conversationList und zeigt
				 * alle Unterhaltungen mittels showConversation() an.
				 */
				conversationList = result;
				showConversation();

				/*
				 * Sofern auf einer Unterhaltung geantwortet wurde, wird das
				 * Formular der entsprechenden Unterhaltung erstellt.
				 */
				if (isReplyed) {
					/*
					 * Überprüfung in welcher Unterhaltung geantwortet wurde.
					 */
					for (Conversation singleConversation : result) {
						if (conversation.getId() == singleConversation.getId()) {

							/*
							 * Erzeugt eine neues Formular für die Unterhaltung,
							 * in der geantwortet wurde.
							 */
							TextyForm singleConversationViewer = new SingleConversationViewer(
									"Private Conversation", singleConversation);

							/*
							 * Zuweisung des jeweiligen Child Widget zum Parent
							 * Widget.
							 */
							RootPanel.get("Details").add(
									singleConversationViewer);
						}
					}
				}
			}
		};
		return asyncCallback;
	}

	/**
	 * Zeigt die einzelnen Unterhaltungen in der Scrollbox an.
	 */
	private void showConversation() {
		/*
		 * Ändert die Reihenfolge des Verctors, so dass die aktuellste
		 * Unterhaltungen den niedrigsten Index haben.
		 */
		Collections.reverse(conversationList);
		for (Conversation conversation : conversationList) {
			/*
			 * Initialisierung der textuellen Inhalte.
			 */
			String text = setMessageText(conversation.getLastMessage()
					.getText());
			String receiver = setRecipient(conversation.getLastMessage()
					.getListOfReceivers());
			String dateString = new String();

			/*
			 * Bestimmung des Abstand des Datums zwischen den aktuellen
			 * Zeitpunkt und den Zeitpunkt der zuletzt geschriebenen Nachricht.
			 */
			Date afterDate = new Date();
			Date baseDate = conversation.getLastMessage().getDateOfCreation();

			final long baseTime = baseDate.getTime() / 1000;
			final long afterTime = afterDate.getTime() / 1000;

			long duration = afterTime - baseTime;

			if (duration < ONE_MINUTE) {
				dateString = "Just now";
			}

			else if (duration < ONE_HOURS) {
				Date durationDate = new Date(duration * 1000);
				dateString = DateTimeFormat.getFormat("mm").format(durationDate)
						+ " minutes ago";
				
				if(ONE_DAYS - ONE_HOURS < duration && duration < ONE_DAYS){
					dateString = "23:" + DateTimeFormat.getFormat("mm").format(
							conversation.getLastMessage().getDateOfCreation()) + " ago";
				}
			}
			
			else if(duration >= ONE_HOURS && duration <= ONE_HOURS*2) {
				Date durationDate = new Date(duration * 1000);
				dateString = DateTimeFormat.getFormat("HH").format(durationDate)
						+ " hour ago";
			}

			else if (duration < ONE_DAYS) {
				Date durationDate = new Date(duration * 1000);
				dateString = DateTimeFormat.getFormat("HH").format(durationDate)
						+ " hours ago";
			}

			else if (duration < ONE_MONTH) {
				Date durationDate = new Date(duration * 1000);
				dateString = DateTimeFormat.getFormat("dd").format(durationDate)
						+ " days ago";
			}

			else if (duration > ONE_MONTH) {
				Date durationDate = new Date(duration * 1000);
				dateString = "No activity since "
						+ DateTimeFormat.getFormat("MM:yyyy").format(
								durationDate);
			}

			/*
			 * Deklaration, Definition und Initialisierung der Widget.
			 */
			FlexTable chatFlexTable = new FlexTable();
			FocusPanel wrapper = new FocusPanel();
			Label textLabel = new Label(text);
			Label receiverLabel = new Label(receiver);
			Label authorLabel = new Label("Last message from: "
					+ conversation.getLastMessage().getAuthor().getFirstName());
			Label dateLabel = new Label(dateString);

			/*
			 * Zuweisung der Handler an das jeweilige Widget.
			 */
			wrapper.addClickHandler(createClickHandler(conversation));

			/*
			 * Zuweisung der Styles an das jeweilige Widget.
			 */
			authorLabel.getElement().setId("conversationHead");
			receiverLabel.getElement().setId("conversationHead");
			dateLabel.getElement().setId("conversationDate");
			textLabel.getElement().setId("conversationBody");
			chatFlexTable.getElement().setId("conversation");

			/*
			 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
			 */
			chatFlexTable.getFlexCellFormatter().setColSpan(1, 0, 2);
			chatFlexTable.getFlexCellFormatter().setColSpan(2, 0, 2);

			chatFlexTable.setWidget(0, 0, authorLabel);
			chatFlexTable.setWidget(0, 1, dateLabel);
			chatFlexTable.setWidget(1, 0, receiverLabel);
			chatFlexTable.setWidget(2, 0, textLabel);

			wrapper.add(chatFlexTable);

			content.add(wrapper);

		}
	}

	/**
	 * Sofern der Text der einzelnen Nachricht >= 30 Zeichen ist, wird der
	 * angezeigte Text auf 30 Zeichen verkürzt und ein "..." hinzugefügt
	 * 
	 * @param text
	 * @return
	 */
	private String setMessageText(String text) {
		String messageText = null;
		if (text.length() < 30) {
			messageText = text;

		} else {

			StringBuffer bufferName = new StringBuffer(text);
			bufferName.setLength(30);
			bufferName.insert(bufferName.length(), "...");
			messageText = bufferName.toString();
		}
		return messageText;
	}

	/**
	 * Setzt die Anzeige der Namen aller Teilnehmer fest. Sind mehr als drei
	 * User Teilnehmer, so werden nur der Autor und zwei Teilnehmer angezeigt
	 * und für die restliche Anzahl der Teilnehmer ein "and n more" gesetzt.
	 * 
	 * @param receiver
	 * @return
	 */
	private String setRecipient(Vector<User> receiver) {
		String receivers = " to";
		/*
		 * Falls weniger als drei Teilnehmer in der Unterhaltung sind.
		 */
		if (receiver.size() < 3) {
			for (int i = 0; i < receiver.size(); i++) {
				receivers = receivers + " '" + receiver.get(i).getFirstName()
						+ "'";
			}

		}
		/*
		 * Falls mehr als drei Teilnehmer in der Unterhaltung sind.
		 */
		else {
			for (int i = 0; i < 2; i++) {
				receivers = receivers + " '" + receiver.get(i).getFirstName()
						+ "'";
			}
			receivers = receivers + " and "
					+ new Integer(receiver.size() - 2).toString()
					+ " more receiver(s).";
		}
		return receivers;
	}

	/**
	 * Erzeugt einen Button der das Erstellen einer neuen Unterhaltung durch
	 * einer neuen Nachricht ermöglicht.
	 * 
	 * @return
	 */
	private Button createNewMessageButton() {
		Button newMessageButton = new Button("New Conversation",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {

						/*
						 * Erzeugt eine neues Formular um eine neue Unterhaltung
						 * durch eine neue Nachricht erstellen zu können.
						 */
						TextyForm newMessage = new NewMessage(
								"New Conversation");

						/*
						 * Entfernung der Child Widgets vom jeweiligen Parent
						 * Widget.
						 */
						RootPanel.get("Info").clear();

						/*
						 * Zuweisung des jeweiligen Child Widget zum Parent
						 * Widget.
						 */
						RootPanel.get("Info").add(newMessage);
					}
				});
		return newMessageButton;
	}

	/**
	 * Erzeugt einen Handler, der beim Klick auf einer Unterhaltung den
	 * Chatverlauf öffnet.
	 * 
	 * @param conversation
	 * @return
	 */
	private ClickHandler createClickHandler(final Conversation conversation) {
		ClickHandler clickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				/*
				 * Entfernung der Child Widgets vom jeweiligen Parent
				 * Widget.
				 */
				RootPanel.get("Details").clear();
				
				/*
				 * Zuweisung des jeweiligen Child Widget zum Parent
				 * Widget.
				 */
				RootPanel.get("Details").add(
						new SingleConversationViewer("Private Conversation",
								conversation));
			}
		};
		return clickHandler;
	}

}
