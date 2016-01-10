package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
 * eigenen Nachrichten bearbeiten oder l�schen.
 */
public class SingleConversationViewer extends TextyForm {

	/**
	 * Der LOG gibt eine m�gliche Exception bzw. den Erfolg des asynchronen
	 * Callbacks aus.
	 */
	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

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
	 * Die administration erm�glicht die asynchrone Kommunikation mit der
	 * Applikationslogik.
	 */
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	/**
	 * Der Konstruktor erzwingt die Eingabe einer �berschrift f�r das Formular.
	 * Zudem wird die entsprechende Unterhaltung �bergeben, welche angezeigt
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
		administration.getCurrentUser(getCurrentUserExectue());

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
		 * Nachdem das Formular aufgebaut ist, wird die H�he des jeweiligen
		 * Panels ausgelesen und als H�he der Scrollbars gesetzt.
		 */
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
			}
		});

	}

	/**
	 * AsyncCallback f�r das Auslesen des aktuellen User aus der Datenbank.
	 * 
	 * @return
	 */
	private AsyncCallback<User> getCurrentUserExectue() {
		AsyncCallback<User> asyncCallback = new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(User result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				/*
				 * �bergibt das result an currentUser und zeigt im Anschluss
				 * alle Nachrichten der Unterhaltung an.
				 */
				currentUser = result;
				showAllMessages();
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
		 * SingleMessageView. Dieses Objekt enth�lt die notwendigen Daten der
		 * Nachricht und wird in der FlexTable an Stelle n = index eingef�gt.
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
			 * zweiter Splate eingef�gt.
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
				 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
				 */
				RootPanel.get("Info").add(
						new ReplyMessageForm("Reply to this conversation",
								conversation, true));
			}
		});
		return replyButton;
	}
}
