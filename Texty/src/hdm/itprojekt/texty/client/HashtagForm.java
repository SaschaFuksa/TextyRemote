package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.FieldVerifier;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.HashtagSubscription;

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
 * Das HashtagForm Formular bietet den Nutzer die Möglichkeit, über ein Suchfeld
 * nicht-abonnierte Hahstags des Systems auszuwählen und diese durch einen Klick
 * auf den entsprechenden Button zu abonnieren.
 */
public class HashtagForm extends TextyForm {

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
	private Button subscribeButton = createSubscribeButton();
	private FlexTable hashtagFormFlexTable = new FlexTable();
	private HorizontalPanel suggestBoxPanel = new HorizontalPanel();
	private InfoBox infoBox = new InfoBox();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label text = new Label("Subscribe new hashtags!");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private VerticalPanel mainPanel = new VerticalPanel();

	/**
	 * Deklaration, Definition und Initialisierung BO.
	 */
	private Vector<Hashtag> allHashtag = new Vector<Hashtag>();
	private Vector<Hashtag> allSelectedHashtag = new Vector<Hashtag>();
	private Vector<Hashtag> allSubscribedHashtag = new Vector<Hashtag>();

	/**
	 * Die administration ermöglicht die asynchrone Kommunikation mit der
	 * Applikationslogik.
	 */
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	/**
	 * * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular
	 * und die Übergabe aller bereits abonnierten User.
	 * 
	 * @see TextyForm
	 * @param headline
	 * @param allSubscribedHashtag
	 */
	public HashtagForm(String headline, Vector<Hashtag> allSubscribedHashtag) {
		super(headline);
		this.allSubscribedHashtag = allSubscribedHashtag;
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
		 * Alle Hashtags aus dem System werden aus der DB geladen.
		 */
		administration.getAllHashtags(getAllHashtagExecute());

		/*
		 * Zuweisung der Handler an das jeweilige Widget.
		 */
		suggestBox.addKeyUpHandler(createKeyUpHandler());
		suggestBox.getValueBox().addFocusHandler(createFocusHandler());

		/*
		 * Standardtext für das Suchfeld.
		 */
		suggestBox.setText("Search for hashtags");

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		this.getElement().setId("fullSize");
		addButton.getElement().setId("addButton");
		subscribeButton.getElement().setId("button");
		suggestBoxPanel.getElement().setId("fullWidth");
		mainPanel.getElement().setId("subscriptionForm");
		hashtagFormFlexTable.getElement().setId("fullSize");
		content.getElement().setId("fullWidth");
		scroll.getElement().setId("fullWidth");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		suggestBoxPanel.add(suggestBox);
		suggestBoxPanel.add(addButton);

		hashtagFormFlexTable.setWidget(0, 0, text);
		hashtagFormFlexTable.setWidget(1, 0, suggestBoxPanel);
		hashtagFormFlexTable.setWidget(2, 0, scroll);
		hashtagFormFlexTable.setWidget(3, 0, subscribeButton);
		hashtagFormFlexTable.setWidget(4, 0, infoBox);

		subscribeButton.setVisible(false);

		mainPanel.add(getHeadline());
		mainPanel.add(hashtagFormFlexTable);

		this.add(mainPanel);

		/*
		 * Nachdem das Formular aufgebaut ist, wird die Höhe des jeweiligen
		 * Panels ausgelesen und als Höhe der Scrollbar gesetzt.
		 */
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
			}
		});

	}

	/**
	 * AsyncCallback für das Auslesen aller Hashtag aus der Datenbank.
	 * 
	 * @return
	 */
	private AsyncCallback<Vector<Hashtag>> getAllHashtagExecute() {
		AsyncCallback<Vector<Hashtag>> asyncCallback = new AsyncCallback<Vector<Hashtag>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<Hashtag> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				/*
				 * Übergibt das result an den Vector allHashtag und setzt im
				 * Anschluss die Vorschläge im Suchfeld.
				 */
				allHashtag = result;
				setAllHashtag();
				setOracle();
			}
		};
		return asyncCallback;
	}

	/**
	 * Erzeugt ein neues Abonnement zwischen den aktuellen Nutzer und des
	 * ausgewählten Hashtag.
	 * 
	 * @return
	 */
	private AsyncCallback<HashtagSubscription> createHashtagSubscriptionExecute() {
		AsyncCallback<HashtagSubscription> asyncCallback = new AsyncCallback<HashtagSubscription>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(HashtagSubscription result) {
				LOG.info("Success :" + result.getClass().getSimpleName());

				/*
				 * Erzeugt eine neues Formular für die Abonnements um sofort den
				 * neu hinzufügten Hashtag in der Liste anzeigen zu können.
				 */
				TextyForm hashtagSubscription = new SubscriptionForm(
						"Hashtag Subscriptions");

				/*
				 * Entfernung der Child Widgets vom jeweiligen Parent Widget.
				 */
				RootPanel.get("Details").clear();
				RootPanel.get("Info").clear();

				/*
				 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
				 */
				RootPanel.get("Details").add(hashtagSubscription);
			}
		};
		return asyncCallback;
	}

	/**
	 * Über die Methode setAllHashtag() werden alle bisher abonnierte Hashtags
	 * aus dem Vector allHashtag entfernt. Dadurch werden in setOralce() nur die
	 * Hashtag den Vorschlägen hinzugefügt, welche noch nicht abonniert sind.
	 * 
	 * @param allHashtag
	 */
	private void setAllHashtag() {
		/*
		 * Suche nach einer ausgewählten Übereinstimmung der einzelnen
		 * abonnierten Hashtags mit den einzelnen Hashtag des Systems.
		 */
		for (Hashtag hashtag : allHashtag) {
			for (Hashtag subscribedHashtag : allSubscribedHashtag) {

				/*
				 * Stimmen die IDs überein, wird der abonnierte Hashtag aus dem
				 * Vector allHashtag entfernt.
				 */
				if (hashtag.getId() == subscribedHashtag.getId()) {
					allHashtag.remove(hashtag);
				}
			}
		}
	}

	/**
	 * Fügt den ausgewählten Hashtag den selektierten Hashtags hinzu und
	 * entfernt ihn aus allen nicht abonnierent Hashtags. Erstellung eines
	 * Panels für den selektierten Hashtag und setzt die Vorschläge des
	 * Suchfeldes neu.
	 * 
	 * @param keyword
	 */
	private void addHashtag(String keyword) {
		for (Hashtag hashtag : allHashtag) {
			if (keyword.equals(hashtag.getKeyword())) {
				allSelectedHashtag.addElement(hashtag);
				allHashtag.remove(hashtag);
				createHashtagPanel(hashtag);
				suggestBox.setText("");
				setOracle();
				return;
			}
		}
		infoBox.setWarningText("Hashtag is unknown!");
	}

	/**
	 * Für alle ausgewählte Hashtags wird ein neues Abonnement erstellt.
	 */
	private void addHashtagSubscriptions() {
		for (Hashtag hashtag : allSelectedHashtag) {
			allSubscribedHashtag.add(hashtag);
			administration.createHashtagSubscription(hashtag,
					createHashtagSubscriptionExecute());

		}
	}

	/**
	 * Erstellung eines Panel für den ausgewählten Hashtag in der Scrollbox.
	 * 
	 * @param hashtag
	 */
	private void createHashtagPanel(final Hashtag hashtag) {
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
				removeHashtag(hashtag);
				content.remove(hashtagPanel);

				/*
				 * Setzt den Abonnieren-Button unsichtbar wenn keine Hashtag
				 * ausgewählt sind.
				 */
				if (allSelectedHashtag.size() == 0) {
					subscribeButton.setVisible(false);
				}
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
		content.add(hashtagPanel);
	}

	/**
	 * Enfernung eines Hashtags aus den ausgewählten Hashtag und fügt diesen
	 * allen nichtabonnierten Hashtags wieder hinzu. Setzt im Anschluss die
	 * Vorschläge des Suchfelds neu.
	 * 
	 * @param hashtag
	 */
	private void removeHashtag(Hashtag hashtag) {
		allHashtag.add(hashtag);
		allSelectedHashtag.remove(hashtag);
		setOracle();
	}

	/**
	 * Setzt die nicht abonnierte Hashtags als Vorschläge im Suchfeld.
	 */
	private void setOracle() {
		oracle.clear();
		for (Hashtag hashtag : allHashtag) {
			/*
			 * Dem oracle wird als Vorschlag das keyword des Hashtags
			 * zugewiesen.
			 */
			oracle.add(hashtag.getKeyword());
		}
	}

	/**
	 * Erzeugt einen Button für das hinzufügen des ausgewählten Hashtags aus dem
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
				 * Erzeugt aus den Text aus dem Suchfeld das Keyword und
				 * entfernt ggf. Leerzeichen.
				 */
				String keyword = suggestBox.getText().trim()
						.replaceAll(" ", "");

				/*
				 * Überprüfung ob mindestens drei Zeichen eingegeben wurden.
				 */
				if (!FieldVerifier.isValidHashtag(keyword)) {
					infoBox.setWarningText("Please select a Hashtag with at least three characters!");
				} else {
					/*
					 * Fügt den ausgewählten Hashtag über addHashtag() den
					 * selectedHashtag hinzu und lässt den Button zum abonnieren
					 * der ausgewählten Hashtags anzeigen, sofern noch kein
					 * Hashtag ausgewählt wurde.
					 */
					addHashtag(keyword);
					if (!subscribeButton.isVisible()) {
						subscribeButton.setVisible(true);
					}
				}
			}

		});
		return addButton;
	}

	/**
	 * Erzeugt einen Button für das abonnieren ausgewählter Hashtags.
	 * 
	 * @return
	 */
	private Button createSubscribeButton() {
		Button subscribeButton = new Button("Subscribe", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*
				 * Sofern kein Hashtag ausgewählt ist, erscheint eine
				 * Fehlermeldung.
				 */
				if (allSelectedHashtag.size() == 0) {
					infoBox.setWarningText("Please select a hashtag!");
				} else {
					/*
					 * ist mindestens ein Hashtag ausgewählt, wird dieser
					 * abonniert.
					 */
					addHashtagSubscriptions();
				}
			}
		});
		return subscribeButton;
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
				 * Erzeugt aus den Text aus dem Suchfeld das Keyword und
				 * entfernt ggf. Leerzeichen.
				 */
				String keyword = suggestBox.getText().trim()
						.replaceAll(" ", "");

				/*
				 * Überprüfung ob mindestens drei Zeichen eingegeben wurden.
				 */
				if (!FieldVerifier.isValidHashtag(keyword)) {
					infoBox.setWarningText("Please select a Hashtag with at least three characters!");
				} else {
					/*
					 * Fügt den ausgewählten Hashtag über addHashtag() den
					 * selectedHashtag hinzu und lässt den Button zum abonnieren
					 * der ausgewählten Hashtags anzeigen, sofern noch kein
					 * Hashtag ausgewählt wurde.
					 */
					addHashtag(keyword);
					if (!subscribeButton.isVisible()) {
						subscribeButton.setVisible(true);
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