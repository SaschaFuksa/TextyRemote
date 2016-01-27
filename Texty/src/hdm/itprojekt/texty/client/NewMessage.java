package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Die newMessage gibt den User die Möglichkeit, eine neue Unterhaltung durch
 * eine neue Nachricht zu erstellen. Dabei wird die MessageForm mit einem
 * Suchfeld für die Empfänger der Nachricht erweitert.
 */
public class NewMessage extends TextyForm {

	/**
	 * Der LOG gibt eine mögliche Exception bzw. den Erfolg des asynchronen
	 * Callbacks aus.
	 */
	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	/**
	 * Deklaration, Definition und Initialisierung der Widget.
	 */
	private Button addButton = createAddButton();
	private FlexTable userFormFlexTable = new FlexTable();
	private HorizontalPanel suggestBoxPanel = new HorizontalPanel();
	private InfoBox infoBox = new InfoBox();
	private HorizontalPanel content = new HorizontalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private MessageForm message = new MessageForm();
	private Label text = new Label("New public message to all users!");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private VerticalPanel mainPanel = new VerticalPanel();

	/**
	 * Deklaration, Definition und Initialisierung BO.
	 */
	private Vector<User> allUser = new Vector<User>();

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
	public NewMessage(String headline) {
		super(headline);
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
		 * Alle User aus dem System werden aus der DB geladen.
		 */
		administration.getAllUsers(getAllUsersExecute());

		/*
		 * Zuweisung der Handler an das jeweilige Widget.
		 */
		message.sendButton.addClickHandler(createSendHandler());
		suggestBox.addKeyUpHandler(createKeyUpHandler());
		suggestBox.getValueBox().addFocusHandler(createFocusHandler());

		/*
		 * Standardtext für das Suchfeld.
		 */
		suggestBox.setText("Search for users");

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		this.getElement().setId("fullSize");
		addButton.getElement().setId("addButton");
		suggestBoxPanel.getElement().setId("fullWidth");
		mainPanel.getElement().setId("subscriptionForm");
		userFormFlexTable.getElement().setId("fullSize");
		scroll.getElement().setId("messageFormScroll");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		suggestBoxPanel.add(suggestBox);
		suggestBoxPanel.add(addButton);

		userFormFlexTable.setWidget(0, 0, text);
		userFormFlexTable.setWidget(1, 0, suggestBoxPanel);
		userFormFlexTable.setWidget(2, 0, scroll);
		userFormFlexTable.setWidget(3, 0, message);
		userFormFlexTable.setWidget(4, 0, infoBox);

		mainPanel.add(getHeadline());
		mainPanel.add(userFormFlexTable);

		this.add(mainPanel);

		/*
		 * Nachdem das Formular aufgebaut ist, wird die Höhe des jeweiligen
		 * Panels ausgelesen und als Höhe der Scrollbars gesetzt.
		 */
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setWidth(mainPanel.getOffsetWidth() + "px");
			}
		});
	}

	/**
	 * AsyncCallback für das Auslesen aller User aus der Datenbank.
	 * 
	 * @return
	 */
	private AsyncCallback<Vector<User>> getAllUsersExecute() {
		AsyncCallback<Vector<User>> asyncCallback = new AsyncCallback<Vector<User>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<User> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				/*
				 * Übergibt das result an die Operation setAllUser().
				 */
				allUser = result;

				/*
				 * Holt den aktuellen User aus der Datenbank.
				 */
				administration.getCurrentUser(getCurrentUserExecuter());
			}
		};
		return asyncCallback;
	}

	/**
	 * AsyncCallback für das Auslesen des aktuellen User aus der Datenbank.
	 * 
	 * @return
	 */
	private AsyncCallback<User> getCurrentUserExecuter() {
		AsyncCallback<User> asyncCallback = new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());

			}

			@Override
			public void onSuccess(User result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				/*
				 * Entfernt den aktuellen User aus allen Usern.
				 */
				allUser.remove(result);
				setOracle();
			}
		};
		return asyncCallback;
	}

	/**
	 * AsyncCallback zur Erzeugung einer neuen Unterhaltung
	 * 
	 * @return
	 */
	private AsyncCallback<Conversation> createConversationExecute() {
		AsyncCallback<Conversation> asyncCallback = new AsyncCallback<Conversation>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Conversation result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				/*
				 * Erzeugt eine neues Formular zum anzeigen aller Unterhaltungen
				 * inklusive der neuen Unterhaltung.
				 */
				TextyForm conversationForm = new ConversationForm(
						"Conversations", result);

				/*
				 * Entfernung der Child Widgets vom jeweiligen Parent Widget.
				 */
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				RootPanel.get("Info").clear();

				/*
				 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
				 */
				RootPanel.get("Navigator").add(conversationForm);
			}
		};
		return asyncCallback;
	}

	/**
	 * Fügt den ausgewählten User den Teilnehmern hinzu und entfernt ihn aus
	 * allen nicht ausgewählten Usern. Erstellung eines Panels für den
	 * selektierten User und setzt die Vorschläge des Suchfeldes neu.
	 * 
	 * @param selectedUser
	 */
	private void addUser(User selectedUser) {
		for (User user : allUser) {
			if (selectedUser.getId() == user.getId()) {
				/*
				 * Setzt den Anfangstext neu, wenn die Teilnehmerliste bislang
				 * leer war.
				 */
				if (message.getSelectedUser().size() == 0) {
					text.setText("New private message to all selected user!");
				}
				message.addSelectedUser(selectedUser);
				allUser.remove(user);
				createUserPanel(user);
				suggestBox.setText("");
				setOracle();
				return;
			}
		}
	}

	/**
	 * Erstellung eines Panel für den ausgewählten User in der Scrollbox.
	 * 
	 * @param user
	 */
	private void createUserPanel(final User user) {
		/*
		 * Deklaration, Definition und Initialisierung der Widget.
		 */
		final HorizontalPanel userPanel = new HorizontalPanel();
		final Label nameLabel = new Label(user.getFirstName());
		final Label removeButton = new Label("x");

		/*
		 * Zuweisung der Handler an das jeweilige Widget.
		 */
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeUser(user);
				content.remove(userPanel);
			}
		});

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		userPanel.getElement().setId("selectedObjectLabel");
		removeButton.getElement().setId("removeButton");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		userPanel.add(nameLabel);
		userPanel.add(removeButton);
		content.add(userPanel);
	}

	/**
	 * Enfernung eines Users aus der Teilnehmerliste und fügt diesen allen nicht
	 * ausgewählten Usern wieder hinzu. Setzt im Anschluss die Vorschläge des
	 * Suchfelds neu.
	 * 
	 * @param user
	 */
	private void removeUser(User user) {
		allUser.add(user);
		/*
		 * Sollte der letzte Teilnehmer entfernt werden, wird die Unterhaltung
		 * eine öffentliche Unterhaltung und ein entsprechender Hinweis wird
		 * angezeigt.
		 */
		if (message.getSelectedUser().size() == 1) {
			text.setText("New public message to all user!");
		}
		message.removeSelectedUser(user);
		setOracle();
	}

	/**
	 * Vergleich die Nickname aller User mit dem Nickname des ausgewählten Users
	 * um bei Übereinstimmung ein komplettes User Objekt auszugeben.
	 * 
	 * @param nickName
	 * @return
	 */
	private User getSingleUser(String nickName) {
		for (User user : allUser) {
			if (nickName.equals(setNickName(user.getEmail()))) {
				return user;
			}
		}
		return null;
	}

	/**
	 * Wandelt den ausgewählten Namen aus dem Suchfeld in den Nickname um. Dabei
	 * wird der Vorname und die '(' & ')' Klammern entfernt.
	 * 
	 * @param text
	 * @return
	 */
	private String getNickName(String text) {
		StringBuffer bufferName = new StringBuffer(text);
		int firstIndex = bufferName.indexOf("(");
		bufferName.delete(0, firstIndex + 1);
		bufferName.deleteCharAt(bufferName.length() - 1);
		String nickName = bufferName.toString();
		return nickName;
	}

	/**
	 * Setzt die nicht ausgewählten User als Vorschläge im Suchfeld.
	 */
	private void setOracle() {
		oracle.clear();
		for (User user : allUser) {
			/*
			 * Dem oracle wird als Vorschlag der Vorname und der Nickname des
			 * Users mittels setNickName() gegeben, um Nutzer mit selben
			 * Vornamen voneinander unterscheiden zu können.
			 */
			String oracleName = user.getFirstName() + " ("
					+ setNickName(user.getEmail()) + ")";
			oracle.add(oracleName);
		}
	}

	/**
	 * Mittels der E-Mail wird der Nickname des Users erstellt und ausgegeben.
	 * 
	 * @param email
	 * @return
	 */
	private String setNickName(String email) {
		StringBuffer bufferName = new StringBuffer(email);
		bufferName.setLength(bufferName.indexOf("@"));
		String nickName = bufferName.toString();
		return nickName;
	}

	/**
	 * Erzeugt einen Button für das hinzufügen des ausgewählten User aus dem
	 * Suchfeld.
	 * 
	 * @return
	 */
	private Button createAddButton() {
		Button addButton = new Button("", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				infoBox.clear();

				/*
				 * Überprüfung ob der Text des Suchfeldes leer ist.
				 */
				if (suggestBox.getText() == "") {
					infoBox.setWarningText("Please select a user!");
				} else {

					/*
					 * Erzeugt aus den Text aus dem Suchfeld den Nicknamen und
					 * im Anschluss wird anhand des Nicknamen der User über der
					 * Methode getSingleUser() ermittelt.
					 */
					String nickname = getNickName(suggestBox.getText());
					User user = getSingleUser(nickname);

					/*
					 * Überprüfung ob die Suche nach einem User mit dem
					 * entsprechenden Nickname keinen User zurückgab.
					 */
					if (user == null) {
						infoBox.setWarningText("User is unknown!");
					} else {
						/*
						 * Fügt den ausgewählten User über addUser() den
						 * selectedUser hinzu und lässt den Button zum
						 * abonnieren der ausgewählten Usern anzeigen, sofern
						 * noch kein Nutzer ausgewählt wurde.
						 */
						addUser(user);
					}
				}
			}

		});
		return addButton;
	}

	/**
	 * Erzeugt einen Handler für den Send Button in der Message Form
	 * 
	 * @return
	 */
	private ClickHandler createSendHandler() {
		ClickHandler handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*
				 * Erzeugt eine neue Unterhaltung mit der neuen Nachricht als
				 * initial Message.
				 */
				administration.createConversation(message.getText(),
						message.getSelectedUser(), message.getHashtag(),
						createConversationExecute());
			}
		};
		return handler;
	}

	/**
	 * Erzeugt einen Handler für das Suchfeld.
	 * 
	 * @return
	 */
	private KeyUpHandler createKeyUpHandler() {
		KeyUpHandler suggestBoxHandler = new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				infoBox.clear();

				/*
				 * Überprüfung ob der Text des Suchfeldes leer ist.
				 */
				if (suggestBox.getText() == "") {
					infoBox.setWarningText("Please select a user!");
				} else {

					/*
					 * Erzeugt aus den Text aus dem Suchfeld den Nicknamen und
					 * im Anschluss wird anhand des Nicknamen der User über der
					 * Methode getSingleUser() ermittelt.
					 */
					String nickname = getNickName(suggestBox.getText());
					User user = getSingleUser(nickname);

					/*
					 * Überprüfung ob die Suche nach einem User mit dem
					 * entsprechenden Nickname keinen User zurückgab.
					 */
					if (user == null) {
						infoBox.setWarningText("User is unknown!");
					} else {
						/*
						 * Fügt den ausgewählten User über addUser() den
						 * selectedUser hinzu und lässt den Button zum
						 * abonnieren der ausgewählten usern anzeigen, sofern
						 * noch kein Nutzer ausgewählt wurde.
						 */
						addUser(user);

					}
				}
			}

		};
		return suggestBoxHandler;
	}

	/**
	 * Erzeugt einen Handler, der den Standardtext des Suchfeldes zurücksetzt,
	 * sobald diese ausgewählt wird.
	 * 
	 * @return
	 */
	private FocusHandler createFocusHandler() {
		FocusHandler focusHandler = new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				suggestBox.setText("");
			}
		};
		return focusHandler;
	}

}
