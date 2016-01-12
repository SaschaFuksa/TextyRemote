package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Date;
import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Der SingleMessageView erstellt für jede einzelne Nachricht einer Unterhaltung
 * das entsprechende Layout und ordnet die Widgets entsprechend an. Zudem
 * ermöglich SingleMessageView das bearbeiten und löschen der aktuellen
 * Nachricht.
 */
public class SingleMessageView extends VerticalPanel {

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
	private FlexTable messageTable = new FlexTable();
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private Button deleteButton = createDeleteButton();
	private Button editButton = createEditButton();
	private Label text = new Label();
	private Label author = new Label();
	private Label dateLabel = new Label();
	private Label hashtagLabel = new Label();

	/**
	 * Deklaration, Definition und Initialisierung BO.
	 */
	private Message message = new Message();
	private User user = new User();
	private Conversation conversation = new Conversation();

	/**
	 * Die administration ermöglicht die asynchrone Kommunikation mit der
	 * Applikationslogik.
	 */
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	/**
	 * Der Konstruktor erzwingt dsd Übergeben der entsprechenden Unterhaltung,
	 * des aktuellen Nutzers und die dazugehörende Nachricht.
	 * 
	 * @param message
	 * @param user
	 * @param conversation
	 */
	SingleMessageView(Message message, User user, Conversation conversation) {
		this.message = message;
		this.user = user;
		this.conversation = conversation;
		run();
	}

	/**
	 * Diese Methode wird sofort aufgerufen, sobald ein Formular im Browser
	 * eingebaut wird.
	 */
	public void run() {
		/*
		 * Bestimmung des Abstand des Datums zwischen den aktuellen Zeitpunkt
		 * und den Zeitpunkt der zuletzt geschriebenen Nachricht.
		 */
		String dateString = DateTimeFormat.getFormat("HH:mm").format(
				message.getDateOfCreation());

		Date afterDate = new Date();
		Date baseDate = message.getDateOfCreation();

		long afterTime = afterDate.getTime() / 1000;
		long baseTime = baseDate.getTime() / 1000;

		long duration = afterTime - baseTime;

		if (duration < ONE_DAYS) {
			String baseDay = DateTimeFormat.getFormat("dd").format(baseDate);
			String afterDay = DateTimeFormat.getFormat("dd").format(afterDate);

			if (!baseDay.equals(afterDay)) {
				dateString = "yesterday at " + dateString;
			}

		} else if (duration > ONE_DAYS && duration < ONE_MONTH) {
			dateString = DateTimeFormat.getFormat("dd.MM").format(baseDate)
					+ " at " + DateTimeFormat.getFormat("HH").format(baseDate)
					+ "h";
		}

		else if (duration > ONE_MONTH) {
			dateString = DateTimeFormat.getFormat("MM.yyyy").format(baseDate)
					+ " at " + DateTimeFormat.getFormat("dd").format(baseDate)
					+ ", " + DateTimeFormat.getFormat("HH").format(baseDate)
					+ "h";
		}

		/*
		 * Deklaration, Definition und Initialisierung der Widget.
		 */
		dateLabel.setText(dateString);
		text.setText(message.getText());

		/*
		 * Füllt das hashtagLabel mit allen vorhandenen Hahstags.
		 */
		if (message.getListOfHashtag().size() > 0) {
			String keyword = new String();
			for (Hashtag hashtag : message.getListOfHashtag()) {
				keyword = keyword + "#" + hashtag.getKeyword() + " ";
			}
			hashtagLabel.setText(keyword);
		}

		/*
		 * Überprüfung ob der aktuelle Nutzer Autor der Nachricht ist.
		 */
		if (user.getId() == message.getAuthor().getId()) {
			/*
			 * Deklaration, Definition und Initialisierung der Widget.
			 */
			author.setText("You");
			messageTable.setWidget(0, 2, buttonPanel);

			/*
			 * Zuweisung der Styles an das jeweilige Widget.
			 */
			this.getElement().setId("senderPanel");
			text.getElement().setId("senderPanel");
			hashtagLabel.getElement().setId("senderPanel");
			messageTable.getElement().setId("senderPanel");
			getElement().setId("senderPanel");
		} else {
			/*
			 * Deklaration, Definition und Initialisierung der Widget. Zuweisung
			 * der Styles an das jeweilige Widget.
			 */
			author.setText(message.getAuthor().getFirstName());
			messageTable.getElement().setId("receiverPanel");
		}

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		buttonPanel.getElement().setId("buttonPanel");
		deleteButton.getElement().setId("deleteButton");
		editButton.getElement().setId("editButton");
		author.getElement().setId("authorPanel");
		dateLabel.getElement().setId("conversationDate");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		messageTable.getFlexCellFormatter().setColSpan(1, 0, 3);
		messageTable.getFlexCellFormatter().setColSpan(2, 0, 3);
		messageTable.getFlexCellFormatter().setColSpan(3, 0, 3);
		messageTable.getColumnFormatter().setWidth(1, "100%");

		messageTable.setWidget(0, 0, author);
		messageTable.setWidget(0, 1, dateLabel);
		messageTable.setWidget(1, 0, text);
		messageTable.setWidget(3, 0, hashtagLabel);

		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);

		this.add(messageTable);
	}

	/**
	 * AsyncCallback um eine eigene Nachricht zu löschen.
	 * 
	 * @return
	 */
	private AsyncCallback<Void> deleteMessageExecute() {
		AsyncCallback<Void> asyncCallback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void nothing) {
				conversation.removeMessageFromConversation(message);

				messageTable.removeFromParent();

				/*
				 * Überprüfung ob die letzte Nachricht der Unterhaltung gelöscht
				 * wurde.
				 */
				if (conversation.getListOfMessage().size() == 0) {
					/*
					 * Entfernung der Child Widgets vom jeweiligen Parent
					 * Widget.
					 */
					RootPanel.get("Navigator").clear();

					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
					if (conversation.isPublicly()) {
						RootPanel.get("Navigator").add(new HomeForm("Home"));
					} else {
						RootPanel.get("Navigator").add(
								new ConversationForm("Conversations"));
					}
				}
				
			}
		};
		return asyncCallback;
	}

	/**
	 * AsyncCallback um eine eigene Nachricht zu bearbeiten.
	 * 
	 * @param allSelectedHashtag
	 * @return
	 */
	private AsyncCallback<Message> editMessageExecute(
			final Vector<Hashtag> allSelectedHashtag) {
		AsyncCallback<Message> asyncCallback = new AsyncCallback<Message>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Message result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				/*
				 * Übergibt die Inhalte der bearbeiteten Nachricht als result an
				 * die einzelnen Widgets.
				 */
				text.setText(result.getText());

				/*
				 * Setzt die neuen Hashtags der bearbeiteten Nachricht.
				 */
				if (allSelectedHashtag.size() > 0) {
					String keyword = new String();
					for (Hashtag hashtag : message.getListOfHashtag()) {
						keyword = keyword + "#" + hashtag.getKeyword() + " ";
					}
					hashtagLabel.setText(keyword);
				} else {
					hashtagLabel.setText("");
				}

				/*
				 * Ist die letzte Nachricht bearbeitet worden, wird die Ansicht
				 * der Unterhaltungen neu geladen.
				 */
				if (message.getId() == conversation.getLastMessage().getId()) {
					RootPanel.get("Navigator").clear();
					if (conversation.isPublicly()) {
						RootPanel.get("Navigator").add(new HomeForm("Home"));
					} else {
						RootPanel.get("Navigator").add(
								new ConversationForm("Conversations"));
					}

				}

				/*
				 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
				 */
				messageTable.setWidget(2, 0, null);
				messageTable.setWidget(1, 0, text);
				messageTable.setWidget(3, 0, hashtagLabel);
			}
		};
		return asyncCallback;
	}

	/**
	 * Erzeugt einen Button für das löschen einer eigenen Nachricht aus einer
	 * Unterhaltung.
	 * 
	 * @return
	 */
	private Button createDeleteButton() {
		Button deleteButton = new Button("", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*
				 * Entfernt die Nachricht aus der Unterhaltung.
				 */
				administration.deleteMessage(conversation, message,
						deleteMessageExecute());
			}
		});
		return deleteButton;
	}

	/**
	 * Erzeugt einen Button um eigene Nachrichten bearbeiten zu können.
	 * 
	 * @return
	 */
	private Button createEditButton() {
		Button editButton = new Button("", new ClickHandler() {
			/*
			 * Der state ermöglicht das auf und zuklappen der Nachricht um diese
			 * zu bearbeiten.
			 */
			boolean state = true;

			@Override
			public void onClick(ClickEvent event) {
				if (state) {
					/*
					 * Neue MessageForm um eine Nachricht bearbeiten zu können.
					 */
					MessageForm editMessage = new MessageForm(message
							.getListOfHashtag());

					/*
					 * Übergibt den aktuellen Text in die TextBox, benennt den
					 * SendButton in "Edit" und entfernt die InfoBox.
					 */
					editMessage.setText(text.getText());
					editMessage.setSendButtonName("Edit");
					editMessage.removeInfoBox();

					/*
					 * Zuweisung der Handler an das jeweilige Widget.
					 */
					editMessage.sendButton
							.addClickHandler(createClickHandler(editMessage));

					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
					messageTable.setWidget(2, 0, editMessage);
					messageTable.setWidget(1, 0, null);
					messageTable.setWidget(3, 0, null);

					/*
					 * Wechselt den State, so dass beim nächsten Klick die
					 * Bearbeitung abgebrochen wird.
					 */
					state = false;
				} else {
					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
					messageTable.setWidget(2, 0, null);
					messageTable.setWidget(1, 0, text);
					messageTable.setWidget(3, 0, hashtagLabel);

					/*
					 * Wechselt den State, so dass beim nächsten Klick die
					 * Message wieder bearbeitet werden kann.
					 */
					state = true;
				}

			}
		});
		return editButton;
	}

	/**
	 * Erzeugt einen Handler für den SendButton der MessageForm, welche beim
	 * bearbeiten der Nachricht erzeugt wird..
	 * 
	 * @param editMessage
	 * @return
	 */
	private ClickHandler createClickHandler(final MessageForm editMessage) {
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*
				 * Überschreibt die aktuellen Inhalte der jeweiligen Nachricht
				 * mit neuen Inhalten.
				 */
				administration.editMessage(message, editMessage.getText(),
						editMessage.getHashtag(),
						editMessageExecute(editMessage.getHashtag()));
			}
		};
		return clickHandler;
	}

}
