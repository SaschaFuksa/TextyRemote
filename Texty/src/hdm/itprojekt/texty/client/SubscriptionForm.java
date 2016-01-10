package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Das SubscriptionForm Formular bietet den Nutzer die M�glichkeit, die
 * abonnierte User und abonnierte Hashtags anzeigen zu lassen und das Abonnement
 * zu den jeweiligen Objekt zu k�ndigen.
 */
public class SubscriptionForm extends TextyForm {

	/**
	 * Der LOG gibt eine m�gliche Exception bzw. den Erfolg des asynchronen
	 * Callbacks aus.
	 */
	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	/**
	 * Deklaration, Definition und Initialisierung der Widget.
	 */
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable subscriptionFormFlexTable = new FlexTable();
	private VerticalPanel contentUserSubscriptions = new VerticalPanel();
	private VerticalPanel contentHashtagSubscriptions = new VerticalPanel();
	private ScrollPanel scrollUserSubscriptions = new ScrollPanel(
			contentUserSubscriptions);
	private ScrollPanel scrollHashtagSubscriptions = new ScrollPanel(
			contentHashtagSubscriptions);
	private InfoBox infoBox = new InfoBox();
	private Label text = new Label(
			"To delete subscriptions, click on the delete button next to your subscription.");

	/**
	 * Deklaration, Definition und Initialisierung BO.
	 */
	private Vector<Hashtag> allSubscribedHashtag = new Vector<Hashtag>();
	private Vector<User> allSubscribedUser = new Vector<User>();

	/**
	 * Die administration erm�glicht die asynchrone Kommunikation mit der
	 * Applikationslogik.
	 */
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	/**
	 * Der Konstruktor erzwingt die Eingabe einer �berschrift f�r das Formular.
	 * 
	 * @see TextyForm
	 * @param headline
	 */
	public SubscriptionForm(String headline) {
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
		 * Alle abonnierte User werden aus der DB geladen.
		 */
		administration.getAllSubscribedUsers(getAllSubscribedUsersExecute());

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("subscriptionForm");
		subscriptionFormFlexTable.getElement().setId("fullSize");
		contentUserSubscriptions.getElement().setId("fullWidth");
		contentHashtagSubscriptions.getElement().setId("fullWidth");
		scrollUserSubscriptions.getElement().setId("fullWidth");
		scrollHashtagSubscriptions.getElement().setId("fullWidth");
		text.getElement().setId("blackFont");
		subscriptionFormFlexTable.getColumnFormatter().addStyleName(0,
				"subscriptionFlexTableCell");
		subscriptionFormFlexTable.getColumnFormatter().addStyleName(1,
				"subscriptionFlexTableCell");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		subscriptionFormFlexTable.setText(0, 0, "");
		subscriptionFormFlexTable.setText(0, 1, "");
		subscriptionFormFlexTable.setWidget(1, 0, scrollUserSubscriptions);
		subscriptionFormFlexTable.setWidget(1, 1, scrollHashtagSubscriptions);

		mainPanel.add(getHeadline());
		mainPanel.add(text);
		mainPanel.add(subscriptionFormFlexTable);
		mainPanel.add(infoBox);

		this.add(mainPanel);

		/*
		 * Nachdem das Formular aufgebaut ist, wird die H�he des jeweiligen
		 * Panels ausgelesen und als H�he der Scrollbars gesetzt.
		 */
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				int height = mainPanel.getOffsetHeight() * 2;
				scrollUserSubscriptions.setHeight(height + "px");
				scrollHashtagSubscriptions.setHeight(height + "px");
			}
		});
	}

	/**
	 * AsyncCallback f�r das Auslesen aller abonnierten User aus der Datenbank.
	 * 
	 * @return
	 */
	private AsyncCallback<Vector<User>> getAllSubscribedUsersExecute() {
		AsyncCallback<Vector<User>> asyncCallback = new AsyncCallback<Vector<User>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<User> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				/*
				 * Wenn User abonniert sind, wird die �berschrift der Spalte f�r
				 * die abonnierten User beschriftet.
				 */
				if (result.size() > 0) {
					subscriptionFormFlexTable.setText(0, 0,
							"User Subscriptions");
				}

				/*
				 * �bergibt das result an den Vector allSubscribedUser und zeigt
				 * alle abonnierte User mittels showUserSubscriptions() an.
				 */
				allSubscribedUser = result;
				showUserSubscriptions();

				/*
				 * Alle abonnierte Hashtags werden aus der DB geladen.
				 */
				administration
						.getAllSubscribedHashtags(getAllSubscribedHashtagsExecute());
			}
		};
		return asyncCallback;
	}

	/**
	 * AsyncCallback f�r das Auslesen aller abonnierten Hashtags aus der
	 * Datenbank.
	 * 
	 * @return
	 */
	private AsyncCallback<Vector<Hashtag>> getAllSubscribedHashtagsExecute() {
		AsyncCallback<Vector<Hashtag>> asyncCallback = new AsyncCallback<Vector<Hashtag>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<Hashtag> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				/*
				 * Wenn Hashtags abonniert sind, wird die �berschrift der Spalte
				 * f�r die abonnierten Hashtags beschriftet.
				 */
				if (result.size() > 0) {
					subscriptionFormFlexTable.setText(0, 1,
							"Hashtag Subscriptions");
				}

				/*
				 * �bergibt das result an den Vector allSubscribedUser und zeigt
				 * alle abonnierte User mittels showUserSubscriptions() an.
				 */
				allSubscribedHashtag = result;
				showHashtagSubscriptions();

				/*
				 * Sollten weder Hashtags noch User bereits abonniert sein,
				 * kommt ein entsprechender Hinweis.
				 */
				if (allSubscribedUser.size() == 0
						&& allSubscribedHashtag.size() == 0) {
					infoBox.setInfoText("You don't have any subscriptions. To subscripe user or hashtags, please use your searchbar on the right and left side!");
				}
			}
		};
		return asyncCallback;
	}

	/**
	 * AsyncCallback f�r das k�ndigen einzelner User Abonnements.
	 * 
	 * @return
	 */
	private AsyncCallback<Void> deleteUserSubscriptionExecute() {
		AsyncCallback<Void> asyncCallback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void nothing) {

				/*
				 * Erzeugt eine neues Formular f�r das abonnieren von User um
				 * das Suchfeld den gek�ndigten User hinzuf�gen zu k�nnen.
				 */
				TextyForm userForm = new UserForm("User", allSubscribedUser);

				/*
				 * Entfernung der Child Widgets vom jeweiligen Parent Widget.
				 */
				RootPanel.get("Navigator").clear();

				/*
				 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
				 */
				RootPanel.get("Navigator").add(userForm);
			}
		};
		return asyncCallback;
	}

	/**
	 * AsyncCallback f�r das k�ndigen einzelner Hashtag Abonnements.
	 * 
	 * @return
	 */
	private AsyncCallback<Void> deleteHashtagSubscriptionExecute() {
		AsyncCallback<Void> asyncCallback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void nothing) {

				/*
				 * Erzeugt eine neues Formular f�r das abonnieren von Hashtags
				 * um das Suchfeld den gek�ndigten Hashtag hinzuf�gen zu k�nnen.
				 */
				TextyForm hashtagForm = new HashtagForm("Hashtags",
						allSubscribedHashtag);

				/*
				 * Entfernung der Child Widgets vom jeweiligen Parent Widget.
				 */
				RootPanel.get("Info").clear();

				/*
				 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
				 */
				RootPanel.get("Info").add(hashtagForm);
			}
		};
		return asyncCallback;
	}

	/**
	 * Zeigt auf der Spalte f�r abonnierte User die abonnierten User an.
	 */
	private void showUserSubscriptions() {
		/*
		 * F�ge ein neues Formular in der GUI hinzu, �ber welches neue User
		 * abonniert werden k�nnen. �bergibt alle abonnierte User um das
		 * Suchfeld optimal anzupassen.
		 */
		if (RootPanel.get("Navigator").getWidgetCount() == 0) {
			TextyForm userForm = new UserForm("User", allSubscribedUser);
			RootPanel.get("Navigator").add(userForm);
		}

		/*
		 * Erstellung eines Panel f�r jeden abonnierten User.
		 */
		for (final User user : allSubscribedUser) {
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
					deleteUserSubscription(user);
					contentUserSubscriptions.remove(userPanel);
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
			contentUserSubscriptions.add(userPanel);
		}
	}

	/**
	 * Diese Operation k�ndigt das Abonnement vom �bergebenen User.
	 * 
	 * @param user
	 */
	private void deleteUserSubscription(User user) {

		/*
		 * Gehe alle abonnierte User durch.
		 */
		for (User subscribedUser : allSubscribedUser) {

			/*
			 * �berpr�fe die Gleichheit der ID. Stimmen die IDs �berein, wird
			 * das Abonnement gek�ndigt und der User aus allSubscribedUser
			 * entfernt.
			 */
			if (user.getId() == subscribedUser.getId()) {
				infoBox.clear();
				allSubscribedUser.remove(user);

				/*
				 * Entferne die �berschrift der Spalte wenn keine User mehr
				 * abonniert sind.
				 */
				if (allSubscribedUser.size() == 0) {
					subscriptionFormFlexTable.setText(0, 0, "");
				}
				
				/*
				 * Erzeugt einen Hinweis wenn weder User noch Hashtags abonniert sind
				 */
				if (allSubscribedHashtag.size() == 0
						&& allSubscribedUser.size() == 0) {
					infoBox.setInfoText("You deleted all your subscriptions. To add new subscriptions, select hashtags or user on the right or left side!");
				}
				administration.deleteUserSubscription(subscribedUser,
						deleteUserSubscriptionExecute());
			}
		}

	}

	/**
	 * Zeigt auf der Spalte f�r abonnierte Hashtag die abonnierten Hashtags an.
	 */
	private void showHashtagSubscriptions() {
		/*
		 * F�ge ein neues Formular in der GUI hinzu, �ber welches neue Hahstags
		 * abonniert werden k�nnen. �bergibt alle abonnierte Hashtags um das
		 * Suchfeld optimal anzupassen.
		 */
		if (RootPanel.get("Info").getWidgetCount() == 0) {
			TextyForm hashtagForm = new HashtagForm("Hashtags",
					allSubscribedHashtag);
			RootPanel.get("Info").add(hashtagForm);
		}

		/*
		 * Erstellung eines Panel f�r jeden abonnierten Hashtag.
		 */
		for (final Hashtag hashtag : allSubscribedHashtag) {
			/*
			 * Deklaration, Definition und Initialisierung der Widget.
			 */
			final HorizontalPanel hashtagPanel = new HorizontalPanel();
			final Label keywordLabel = new Label("#" + hashtag.getKeyword());
			final Label removeButton = new Label("x");

			/*
			 * Zuweisung der Handler an das jeweilige Widget.
			 */
			removeButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					deleteHashtagSubscription(hashtag);
					contentHashtagSubscriptions.remove(hashtagPanel);
				}
			});

			/*
			 * Zuweisung der Styles an das jeweilige Widget.
			 */
			hashtagPanel.getElement().setId("selectedObjectLabel");
			removeButton.getElement().setId("removeButton");

			/*
			 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
			 */
			hashtagPanel.add(keywordLabel);
			hashtagPanel.add(removeButton);
			contentHashtagSubscriptions.add(hashtagPanel);
		}
	}

	/**
	 * Diese Operation k�ndigt das Abonnement vom �bergebenen Hashtag.
	 * 
	 * @param hashtag
	 */
	private void deleteHashtagSubscription(Hashtag hashtag) {
		
		/*
		 * Gehe alle abonnierte Hashtags durch.
		 */
		for (Hashtag subscribedHashtag : allSubscribedHashtag) {
			
			/*
			 * �berpr�fe die Gleichheit der ID. Stimmen die IDs �berein, wird
			 * das Abonnement gek�ndigt und der Hashtag aus allSubscribedHashtag
			 * entfernt.
			 */
			if (hashtag.getId() == subscribedHashtag.getId()) {
				infoBox.clear();
				allSubscribedHashtag.remove(subscribedHashtag);
				
				/*
				 * Entferne die �berschrift der Spalte wenn keine Hashtags mehr
				 * abonniert sind.
				 */
				if (allSubscribedHashtag.size() == 0) {
					subscriptionFormFlexTable.setText(0, 1, "");
				}
				
				/*
				 * Erzeugt einen Hinweis wenn weder User noch Hashtags abonniert sind
				 */
				if (allSubscribedHashtag.size() == 0
						&& allSubscribedUser.size() == 0) {
					infoBox.setInfoText("You deleted all your subscriptions. To add new subscriptions, select hashtags or user on the right or left side!");
				}
				administration.deleteHashtagSubscription(subscribedHashtag,
						deleteHashtagSubscriptionExecute());
			}
		}
	}

}
