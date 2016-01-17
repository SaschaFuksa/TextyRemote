package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.client.ClientsideSettings;
import hdm.itprojekt.texty.client.InfoBox;
import hdm.itprojekt.texty.client.SingleConversationViewer;
import hdm.itprojekt.texty.client.TextyForm;
import hdm.itprojekt.texty.shared.FieldVerifier;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HashtagSubscriptionReport extends TextyForm {
	
	/**
	 * Der LOG gibt eine mögliche Exception bzw. den Erfolg des asynchronen
	 * Callbacks aus.
	 */
	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());
	
	/**
	 * Deklaration, Definition und Initialisierung der Widgets.
	 */
	private ScrollPanel scrollPanel = new ScrollPanel();
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private InfoBox infoBox = new InfoBox();
	private Button Follower;
	
	/**
	 * Die administration ermöglicht die asynchrone Kommunikation mit der
	 * Applikationslogik.
	 */
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();
	
	/**
	 * Deklaration & Definition von Variablen der Klasse.
	 */
	private Vector<Hashtag> allHashtag = new Vector<Hashtag>();
//	private static Vector<User> allUser = new Vector<User>();
	public static User user;
	private Hashtag hashtagSelection = null;
	
	/**
	 * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular.
	 * 
	 * @see TextyForm
	 * @param headline
	 */
	public HashtagSubscriptionReport(String headline) {
		super(headline);
	}

	/**
	 * Diese Methode wird sofort aufgerufen, sobald ein Formular im Browser
	 * eingebaut wird.
	 * 
	 * @see TextyForm
	 */
	@Override
	public void run() {
		
		/*
		 * Alle Hashtags aus dem System werden aus der DB geladen.
		 */
		administration.getAllHashtags(getAllHashtagExecute());
		
		// Create UI
		
		/*
		 * Erzeugt einen Button der das Erstellen des Reports für Nachrichten eines ausgewählten Users ausgibt.
		 */ 
		Follower = new Button("Show Follower", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				/*
				 * Entfernung des evtl. zuvor generierten Reports
				 */
				scrollPanel.clear();
				
				/*
				 * Entfernung der evtl. zuvor ausgegebenen Fehlermeldung
				 * in der infoBox
				 */
					infoBox.clear();

					/*
					 * Erzeugt aus den Text aus dem Suchfeld das Keyword und
					 * entfernt ggf. Leerzeichen.
					 */
					String keyword = suggestBox.getText().trim()
							.replaceAll(" ", "");
					for (Hashtag hashtag : allHashtag) {
						if (hashtag.getKeyword() == keyword) {
							hashtagSelection = hashtag;
						}
					}

					/*
					 * Überprüfung ob mindestens drei Zeichen eingegeben wurden.
					 */
					if (!FieldVerifier.isValidHashtag(keyword)) {
						infoBox.setWarningText("Please select a Hashtag with at least three characters!");
					} else {
				
				/*
				 * Lädt alle User, die der ausgewählte User abonniert hat und startet die Methode 
				 * "generateUserSubscriptionReport(result)" der Klasse "HTMLUserSubscriptionReport", 
				 * die den Report aufbaut und im ScrollPanel ausgibt
				 * 
				 * @see HTMLUserSubscriptionReport
				 */ 
				administration.getAllFollowersFromHashtag(hashtagSelection, new AsyncCallback<Vector<User>>() {
					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Vector<User> result) {
						/*
						 * Zuweisung und Anpassung des Widgets.
						 */
						scrollPanel.setSize("100%", "100%");
						RootPanel.get("Details").add(scrollPanel);
						
						/*
						 * Fügt den generierten Report dem scrollPanel hinzu.
						 */
						scrollPanel.add(HTMLHashtagFollowerReport.generateHashtagFollowerReport(result));
					}
				});
				}
			};
		});
		
		/*
		 * Anlegen einer chatFlexTable zum Anordnen der verschiedenen Widgets im Navigatorbereich
		 */
		// Text
		chatFlexTable.setText(0, 0, "Follower of: #");

		// Textbox
		chatFlexTable.setWidget(0, 1, suggestBox);

		// Show-Button
		chatFlexTable.setWidget(3, 1, Follower);
		
		//Hinzufügen der infoBox für Fehlermeldungen
		chatFlexTable.setWidget(4, 1, infoBox);
		
		// Hinzufügen der widgets
		mainPanel.add(chatFlexTable);
		mainPanel.add(addPanel);
		RootPanel.get("Navigator").clear();
		RootPanel.get("Navigator").add(mainPanel);
		
	}
	
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
					setOracle();
				}
			};
			return asyncCallback;
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
		
//		private void addHashtag(String keyword) {
//			for (Hashtag hashtag : allHashtag) {
//				if (keyword.equals(hashtag.getKeyword())) {
//					suggestBox.setText("");
//					setOracle();
//					return;
//				}
//			}
//			infoBox.setWarningText("Hashtag is unknown!");
//		}
}
