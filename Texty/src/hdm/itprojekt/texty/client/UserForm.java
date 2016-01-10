package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.User;
import hdm.itprojekt.texty.shared.bo.UserSubscription;

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
 * Das UserForm Formular bietet den Nutzer die M�glichkeit, �ber ein Suchfeld
 * nicht-abonnierte User des Systems auszuw�hlen und diese durch einen Klick auf
 * den entsprechenden Button zu abonnieren.
 */
public class UserForm extends TextyForm {

	/**
	 * Der LOG gibt eine m�gliche Exception bzw. den Erfolg des asynchronen
	 * Callbacks aus.
	 */
	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	/**
	 * Deklaration, Definition und Initialisierung der Widget.
	 */
	private Button addButton = createAddButton();
	private Button subscribeButton = createSubscribeButton();
	private FlexTable userFormFlexTable = new FlexTable();
	private HorizontalPanel suggestBoxPanel = new HorizontalPanel();
	private InfoBox infoBox = new InfoBox();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label text = new Label("Subscribe to a new user!");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private VerticalPanel mainPanel = new VerticalPanel();

	/**
	 * Deklaration, Definition und Initialisierung BO.
	 */
	private Vector<User> allUser = new Vector<User>();
	private Vector<User> allSelectedUser = new Vector<User>();
	private Vector<User> allSubscribedUser = new Vector<User>();

	/**
	 * Die administration erm�glicht die asynchrone Kommunikation mit der
	 * Applikationslogik.
	 */
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	/**
	 * Der Konstruktor erzwingt die Eingabe einer �berschrift f�r das Formular
	 * und die �bergabe aller bereits abonnierten User.
	 * 
	 * @see TextyForm
	 * @param headline
	 * @param allSubscribedUser
	 */
	public UserForm(String headline, Vector<User> allSubscribedUser) {
		super(headline);
		this.allSubscribedUser = allSubscribedUser;
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
		suggestBox.addKeyUpHandler(createKeyUpHandler());
		suggestBox.getValueBox().addFocusHandler(createFocusHandler());

		/*
		 * Standardtext f�r das Suchfeld.
		 */
		suggestBox.setText("Search for users");

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		this.getElement().setId("fullSize");
		addButton.getElement().setId("addButton");
		subscribeButton.getElement().setId("button");
		suggestBoxPanel.getElement().setId("fullWidth");
		mainPanel.getElement().setId("subscriptionForm");
		userFormFlexTable.getElement().setId("fullSize");
		content.getElement().setId("fullWidth");
		scroll.getElement().setId("fullWidth");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		suggestBoxPanel.add(suggestBox);
		suggestBoxPanel.add(addButton);

		userFormFlexTable.setWidget(0, 0, text);
		userFormFlexTable.setWidget(1, 0, suggestBoxPanel);
		userFormFlexTable.setWidget(2, 0, scroll);
		userFormFlexTable.setWidget(3, 0, subscribeButton);
		userFormFlexTable.setWidget(4, 0, infoBox);

		subscribeButton.setVisible(false);

		mainPanel.add(getHeadline());
		mainPanel.add(userFormFlexTable);

		this.add(mainPanel);

		/*
		 * Nachdem das Formular aufgebaut ist, wird die H�he des jeweiligen
		 * Panels ausgelesen und als H�he der Scrollbar gesetzt.
		 */
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
			}
		});
	}

	/**
	 * AsyncCallback f�r das Auslesen aller User aus der Datenbank.
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
				 * �bergibt das result an den Vector allUser und sucht im
				 * Anschluss den aktuellen Nutzer.
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
	 * AsyncCallback f�r das Auslesen des aktuellen User aus der Datenbank.
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
				 * �bergibt das result an die Operation setAllUser() und setzt
				 * im Anschluss die Vorschl�ge im Suchfeld.
				 */
				setAllUser(result);
				setOracle();
			}
		};
		return asyncCallback;
	}

	/**
	 * Erzeugt ein neues Abonnement zwischen den aktuellen Nutzer und des
	 * ausgew�hlten Users.
	 * 
	 * @return
	 */
	private AsyncCallback<UserSubscription> createUserSubscriptionExecute() {
		AsyncCallback<UserSubscription> asyncCallback = new AsyncCallback<UserSubscription>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(UserSubscription result) {
				LOG.info("Success :" + result.getClass().getSimpleName());

				/*
				 * Erzeugt eine neues Formular f�r die Abonnements um sofort den
				 * neu hinzuf�gten User in der Liste anzeigen zu k�nnen.
				 */
				TextyForm subscriptionForm = new SubscriptionForm(
						"Subscriptions");

				/*
				 * Entfernung der Child Widgets vom jeweiligen Parent Widget.
				 */
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();

				/*
				 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
				 */
				RootPanel.get("Details").add(subscriptionForm);
			}
		};
		return asyncCallback;
	}

	/**
	 * �ber die Methode setAllUser() wird der aktuelle Nutzer und alle bisher
	 * abonnierte Nutzer aus dem Vector allUser entfernt. Dadurch werden in setOralce()
	 * nur die User den Vorschl�gen hinzugef�gt, welche noch nicht abonniert
	 * sind sowie nicht der aktuelle User ist.
	 * 
	 * @param currentUser
	 */
	private void setAllUser(User currentUser) {
		/*
		 * Entfernung des aktuellen User.
		 */
		allUser.remove(currentUser);

		/*
		 * Suche nach einer ausgew�hlten �bereinstimmung der einzelnen
		 * abonnierten User mit den einzelnen User des Systems.
		 */
		for (User user : allUser) {
			for (User subscribedUser : allSubscribedUser) {

				/*
				 * Stimmen die IDs �berein, wird der abonnierte User aus dem
				 * Vector allUser entfernt.
				 */
				if (user.getId() == subscribedUser.getId()) {
					allUser.remove(user);
				}
			}
		}
	}

	/**
	 * F�gt den ausgew�hlten User den selektierten Usern hinzu und entfernt ihn
	 * aus allen nicht abonnierent Usern. Erstellung eines Panels f�r den
	 * selektierten User und setzt die Vorschl�ge des Suchfeldes neu.
	 * 
	 * @param selectedUser
	 */
	private void addUser(User selectedUser) {
		for (User user : allUser) {
			if (selectedUser.getId() == user.getId()) {
				allSelectedUser.addElement(selectedUser);
				allUser.remove(user);
				createUserPanel(user);
				suggestBox.setText("");
				setOracle();
				return;
			}
		}
	}

	/**
	 * F�r alle ausgew�hlte Nutzer wird ein neues Abonnement erstellt.
	 */
	private void addUserSubscriptions() {
		for (User user : allSelectedUser) {
			allSubscribedUser.add(user);
			administration.createUserSubscription(user,
					createUserSubscriptionExecute());
		}
	}

	/**
	 * Erstellung eines Panel f�r den ausgew�hlten User in der Scrollbox.
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
				
				/*
				 * Setzt den Abonnieren-Button unsichtbar wenn keine User
				 * ausgew�hlt sind.
				 */
				if (allSelectedUser.size() == 0) {
					subscribeButton.setVisible(false);
				}
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
	 * Enfernung eines Users aus den ausgew�hlten User und f�gt diesen allen
	 * nichtabonnierten Usern wieder hinzu. Setzt im Anschluss die Vorschl�ge
	 * des Suchfelds neu.
	 * 
	 * @param user
	 */
	private void removeUser(User user) {
		allUser.add(user);
		allSelectedUser.remove(user);
		setOracle();
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
		return bufferName.toString();
	}

	/**
	 * Setzt die nicht abonnierte User als Vorschl�ge im Suchfeld.
	 */
	private void setOracle() {
		oracle.clear();
		for (User user : allUser) {
			/*
			 * Dem oracle wird als Vorschlag der Vorname und der Nickname des
			 * Users mittels setNickName() gegeben, um Nutzer mit selben
			 * Vornamen voneinander unterscheiden zu k�nnen.
			 */
			String oracleName = user.getFirstName() + " ("
					+ setNickName(user.getEmail()) + ")";
			oracle.add(oracleName);
		}
	}

	/**
	 * Vergleich die Nickname aller User mit dem Nickname des ausgew�hlten Users
	 * um bei �bereinstimmung ein komplettes User Objekt auszugeben.
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
	 * Wandelt den ausgew�hlten Namen aus dem Suchfeld in den Nickname um. Dabei
	 * wird der Vorname und die '(' & ')' Klammern entfernt.
	 * 
	 * @param text
	 * @return
	 */
	private String getNickname(String text) {
		StringBuffer bufferName = new StringBuffer(text);
		int firstIndex = bufferName.indexOf("(");
		bufferName.delete(0, firstIndex + 1);
		bufferName.deleteCharAt(bufferName.length() - 1);
		String nickName = bufferName.toString();
		return nickName;
	}

	/**
	 * Erzeugt einen Button f�r das hinzuf�gen des ausgew�hlten User aus dem
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
				 * �berpr�fung ob der Text des Suchfeldes leer ist.
				 */
				if (suggestBox.getText() == "") {
					infoBox.setWarningText("Please select a user!");
				} else {

					/*
					 * Erzeugt aus den Text aus dem Suchfeld den Nicknamen und
					 * im Anschluss wird anhand des Nicknamen der User �ber der
					 * Methode getSingleUser() ermittelt.
					 */
					String nickname = getNickname(suggestBox.getText());
					User user = getSingleUser(nickname);

					/*
					 * �berpr�fung ob die Suche nach einem User mit dem
					 * entsprechenden Nickname keinen User zur�ckgab.
					 */
					if (user == null) {
						infoBox.setWarningText("User is unknown!");
					} else {
						/*
						 * F�gt den ausgew�hlten User �ber addUser() den
						 * selectedUser hinzu und l�sst den Button zum
						 * abonnieren der ausgew�hlten Usern anzeigen, sofern
						 * noch kein Nutzer ausgew�hlt wurde.
						 */
						addUser(user);
						if (!subscribeButton.isVisible()) {
							subscribeButton.setVisible(true);
						}
					}
				}
			}

		});
		return addButton;
	}

	/**
	 * Erzeugt einen Button f�r das abonnieren ausgew�hlter User.
	 * 
	 * @return
	 */
	private Button createSubscribeButton() {
		Button subscribeButton = new Button("Subscribe", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*
				 * Sofern kein User ausgew�hlt ist, erscheint eine Fehlermeldung.
				 */
				if (allSelectedUser.size() == 0) {
					infoBox.setWarningText("Please select a user!");
				} else {
					/*
					 * ist mindestens ein User ausgew�hlt, wird dieser abonniert.
					 */
					addUserSubscriptions();
				}
			}
		});
		return subscribeButton;
	}

	/**
	 * Erzeugt einen Handler f�r das Suchfeld.
	 * 
	 * @return
	 */
	private KeyUpHandler createKeyUpHandler() {
		KeyUpHandler suggestBoxHandler = new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				infoBox.clear();

				/*
				 * �berpr�fung ob der Text des Suchfeldes leer ist.
				 */
				if (suggestBox.getText() == "") {
					infoBox.setWarningText("Please select a user!");
				} else {

					/*
					 * Erzeugt aus den Text aus dem Suchfeld den Nicknamen und
					 * im Anschluss wird anhand des Nicknamen der User �ber der
					 * Methode getSingleUser() ermittelt.
					 */
					String nickname = getNickname(suggestBox.getText());
					User user = getSingleUser(nickname);

					/*
					 * �berpr�fung ob die Suche nach einem User mit dem
					 * entsprechenden Nickname keinen User zur�ckgab.
					 */
					if (user == null) {
						infoBox.setWarningText("User is unknown!");
					} else {
						/*
						 * F�gt den ausgew�hlten User �ber addUser() den
						 * selectedUser hinzu und l�sst den Button zum
						 * abonnieren der ausgew�hlten usern anzeigen, sofern
						 * noch kein Nutzer ausgew�hlt wurde.
						 */
						addUser(user);
						if (!subscribeButton.isVisible()) {
							subscribeButton.setVisible(true);
						}
					}
				}
			}

		};
		return suggestBoxHandler;
	}

	/**
	 * Erzeugt einen Handler, der den Standardtext des Suchfeldes zur�cksetzt,
	 * sobald diese ausgew�hlt wird.
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
