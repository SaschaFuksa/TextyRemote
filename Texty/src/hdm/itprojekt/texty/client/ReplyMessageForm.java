package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Das ReplyMessageForm Formular setzt erweitert das MessageForm Formular um in
 * bestehendne Unterhaltungen antworten zu können.
 */
public class ReplyMessageForm extends TextyForm {

	/**
	 * Der LOG gibt eine mögliche Exception bzw. den Erfolg des asynchronen
	 * Callbacks aus.
	 */
	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	/**
	 * Deklaration, Definition und Initialisierung der Widget.
	 */
	private MessageForm message = new MessageForm();
	private Label text = new Label(
			"Write down your answer to this conversation!");

	/**
	 * Deklaration, Definition und Initialisierung BO.
	 */
	private boolean isPrivate;
	private Conversation conversation = new Conversation();

	/**
	 * Die administration ermöglicht die asynchrone Kommunikation mit der
	 * Applikationslogik.
	 */
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	/**
	 * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular.
	 * Zudem wird die entsprechende Unterhaltung übergeben, in der die Antwort
	 * erstellt werden soll und ob diese Unterhaltung öffentlich oder privat
	 * ist.
	 * 
	 * @see TextyForm
	 * @param headline
	 * @param conversation
	 * @param isPrivate
	 */
	public ReplyMessageForm(String headline, Conversation conversation,
			boolean isPrivate) {
		super(headline);
		this.conversation = conversation;
		this.isPrivate = isPrivate;
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
		 * Zuweisung der Handler an das jeweilige Widget.
		 */
		message.sendButton.addClickHandler(createClickHandler());

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		this.getElement().setId("fullWidth");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		this.add(getHeadline());
		this.add(text);
		this.add(message);

	}

	/**
	 * AsyncCallback für das hinzufügen einer neuen Nachricht in einer
	 * bestehenden Unterhaltung.
	 * 
	 * @return
	 */
	private AsyncCallback<Conversation> addMessageToConversationExecute() {
		AsyncCallback<Conversation> asyncCallback = new AsyncCallback<Conversation>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Conversation result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				/*
				 * Entfernung der Child Widgets vom jeweiligen Parent Widget.
				 */
				RootPanel.get("Navigator").clear();
				RootPanel.get("Details").clear();
				RootPanel.get("Info").clear();

				/*
				 * Je ob öffentlicher oder privater Unterhaltung wird eine
				 * andere Funktion benötigt, um das Ergebnis dem User anzeigen
				 * zu können.
				 */
				if (isPrivate) {
					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
					RootPanel.get("Navigator").add(
							new ConversationForm("Conversations", result));
				} else {
					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
					RootPanel.get("Navigator").add(new HomeForm("Home"));
				}

			}
		};
		return asyncCallback;
	}

	/**
	 * Erzeugt einen Handler für den SendButton.
	 * 
	 * @return
	 */
	private ClickHandler createClickHandler() {
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*
				 * Fügt die neue Nachricht der bestehenden Unterhaltung hinzu.
				 */
				administration.addMessageToConversation(conversation,
						message.getText(), message.getHashtag(),
						addMessageToConversationExecute());
			}
		};
		return clickHandler;
	}

}
