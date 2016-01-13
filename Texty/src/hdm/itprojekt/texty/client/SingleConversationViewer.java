package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Das SingleConversationViewer Formular zeigt den Nachrichtenverlauf einer
 * Unterhaltung an. Der Nutzer kann in dieser Unterhaltung antworten oder seine
 * eigenen Nachrichten bearbeiten oder löschen.
 */
public class SingleConversationViewer extends TextyForm {

	/**
	 * Der LOG gibt eine mögliche Exception bzw. den Erfolg des asynchronen
	 * Callbacks aus.
	 */
	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	/**
	 * Zeitintervall des automatischen Refresh.
	 */
	private static final int REFRESH_INTERVAL = 5000;

	/**
	 * Deklaration, Definition und Initialisierung der Widget.
	 */
	private Button replyButton = createReplyButton();
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel content = new VerticalPanel();
	private Label text = new Label(
			"Reply to this conversations or edit you messages!");
	private ScrollPanel scroll = new ScrollPanel(content);
	private FlexTable chatFlexTable = new FlexTable();

	/**
	 * Deklaration, Definition und Initialisierung BO.
	 */
	private Conversation conversation = null;
	private User currentUser = null;

	/**
	 * Die administration ermöglicht die asynchrone Kommunikation mit der
	 * Applikationslogik.
	 */
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	/**
	 * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular.
	 * Zudem wird die entsprechende Unterhaltung übergeben, welche angezeigt
	 * werden soll.
	 * 
	 * @see TextyForm
	 * @param headline
	 * @param conversation
	 */
	public SingleConversationViewer(String headline, Conversation conversation) {
		super(headline);
		this.conversation = conversation;
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
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("conversationWrapper");
		scroll.getElement().setId("conversationScroll");
		content.getElement().setId("conversationContent");
		chatFlexTable.getElement().setId("conversationContent");
		text.getElement().setId("blackFont");
		replyButton.getElement().setId("button");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		content.add(chatFlexTable);
		mainPanel.add(getHeadline());
		mainPanel.add(text);
		mainPanel.add(scroll);
		mainPanel.add(replyButton);

		this.add(mainPanel);

		/*
		 * Timer um eine automatische Überprüfung vorzunehmen, ob eine neue
		 * Nachricht der Unterhaltung hinzugefügt wurde.
		 */
		Timer refreshTimer = new Timer() {
			@Override
			public void run() {
				checkNewMessage();
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

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
				showAllMessages();
			}
		};
		return asyncCallback;
	}
	
	/**
	 * AsyncCallback zur Überprüfung der Aktualität der Unterhaltung.
	 */
	private AsyncCallback<Vector<Message>> getRecentMessagesExecute(){
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
				if(result.size() > 0){
					for(Message message : result){
						conversation.addMessageToConversation(message);
						SingleMessageView messageView = new SingleMessageView(message,
								currentUser, conversation);

						/*
						 * Zuweisung der Styles an das jeweilige Widget.
						 */
						chatFlexTable.getColumnFormatter().addStyleName(chatFlexTable.getRowCount(),
								"chatFlexTableCell");

						/*
						 * Fügt die neue Nachricht der Tabelle zu.
						 */
						chatFlexTable.setWidget(chatFlexTable.getRowCount(), 0, messageView);
					}
				}
				
			}
			
		};
		return asyncCallback;
	}

	/**
	 * Zeigt alle Nachrichten der Unterhaltung an.
	 */
	private void showAllMessages() {
		int index = 0;
		/*
		 * Geht jede Nachricht durch und erzeugt ein Objekt der
		 * SingleMessageView. Dieses Objekt enthält die notwendigen Daten der
		 * Nachricht und wird in der FlexTable an Stelle n = index eingefügt.
		 */
		for (Message message : conversation.getListOfMessage()) {

			SingleMessageView messageView = new SingleMessageView(message,
					currentUser, conversation);

			/*
			 * Zuweisung der Styles an das jeweilige Widget.
			 */
			chatFlexTable.getColumnFormatter().addStyleName(index,
					"chatFlexTableCell");

			/*
			 * Wenn der aktuelle User der Autor der Nachricht ist, wird diese in
			 * zweiter Splate eingefügt.
			 */
			if (message.getAuthor().getId() == currentUser.getId()) {
				chatFlexTable.setWidget(index, 1, messageView);
			} else {
				chatFlexTable.setWidget(index, 0, messageView);
			}

			index++;
		}
		scroll.scrollToBottom();
	}
	
	/**
	 * Methode zur Überprüfung der Aktualität der Unterhaltung.
	 */
	private void checkNewMessage(){
		administration.getRecentMessages(conversation.getLastMessage(), getRecentMessagesExecute());
	}

	/**
	 * Button zum Antworten in Unterhaltungen.
	 * 
	 * @return
	 */
	private Button createReplyButton() {
		Button replyButton = new Button("Reply", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*
				 * Entfernung der Child Widgets vom jeweiligen Parent Widget.
				 */
				RootPanel.get("Info").clear();

				/*
				 * Instanziierung einer neuen Antwort.
				 */
				ReplyMessageForm replyMessage = new ReplyMessageForm("Reply to conversation");
				
				/*
				 * Zuweisung der Handler an das jeweilige Widget.
				 */
				replyMessage.message.sendButton
						.addClickHandler(createClickHandler(replyMessage));
				
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
	private ClickHandler createClickHandler(final ReplyMessageForm replyMessage) {
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*
				 * Fügt die neue Nachricht der bestehenden Unterhaltung hinzu.
				 */
				administration.addMessageToConversation(conversation.getLastMessage() ,conversation.getId(),
						replyMessage.message.getText(), replyMessage.message.getHashtag(),
						addMessageToConversationExecute());
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
	private AsyncCallback<Message> addMessageToConversationExecute() {
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
				
				/*
				 * Füge die neue Message der Unterhaltung hinzu.
				 */
				conversation.addMessageToConversation(result);
				
				/*
				 * Erstelle ein Panel für die neue Nachricht.
				 */
				SingleMessageView messageView = new SingleMessageView(
						result, currentUser, conversation);

				/*
				 * Zuweisung der Styles an das jeweilige Widget.
				 */
				chatFlexTable.getColumnFormatter().addStyleName(
						chatFlexTable.getRowCount(),
						"chatFlexTableCell");

				/*
				 * Fügt die neue Nachricht der Tabelle zu.
				 */
				chatFlexTable.setWidget(chatFlexTable.getRowCount(), 1,
						messageView);

				/*
				 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
				 */
				RootPanel.get("Navigator").add(
						new ConversationForm("Conversations"));

			}
		};
		return asyncCallback;
	}
	
}
