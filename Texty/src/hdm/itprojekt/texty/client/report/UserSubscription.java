package hdm.itprojekt.texty.client.report;

import java.util.Vector;

import hdm.itprojekt.texty.client.ClientsideSettings;
import hdm.itprojekt.texty.client.InfoBox;
import hdm.itprojekt.texty.client.TextyForm;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.User;

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

/**
 * In dieser Klasse wird das UI für die Userabo`s aufgebaut. Die Widgets werden 
 * im Navigatorbereich implementiert.
 *
 */

public class UserSubscription extends TextyForm {
	
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
	private Button Usersubscriptions;
	private Button Hashtagsubscriptions ;
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
	private static Vector<User> allUser = new Vector<User>();
	public static User user;
	
	/**
	 * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular.
	 * 
	 * @see TextyForm
	 * @param headline
	 */
	public UserSubscription(String headline) {
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
		
		// Create UI
		
		/*
		 * Erzeugt einen Button der das Erstellen des Reports für die abonnierten User eines ausgewählten Users ausgibt.
		 */
		
		Usersubscriptions = new Button("Usersubscriptions", new ClickHandler() {
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
				 * Überprüfung ob der Text des Suchfeldes leer ist.
				 */
				if(suggestBox.getText() == ""){
					infoBox.setWarningText("Please select a user!");
				}else{	
				/*
				* Erzeugt aus den Text aus dem Suchfeld den Nicknamen und
				* im Anschluss wird anhand des Nicknamen der User über die
				* Methode getUserOutOfAllUser(nickName) ermittelt.
				*/
				String nickName = getNickName(suggestBox.getText());
				User user = getUserOutOfAllUser(nickName);
				
				/*
				 * Überprüfung ob die Suche nach einem User mit dem
				 * entsprechenden Nickname keinen User zurückgab.
				 */
				if (user == null){
					infoBox.setErrorText("Unknown User!");
				}else{
				
				/*
				 * Lädt alle User, die der ausgewählte User abonniert hat und startet die Methode 
				 * "generateUserSubscriptionReport(result)" der Klasse "HTMLUserSubscriptionReport", 
				 * die den Report aufbaut und im ScrollPanel ausgibt
				 * 
				 * @see HTMLUserSubscriptionReport
				 */ 
				administration.getAllSubscribedUsersFromUser(user, new AsyncCallback<Vector<User>>() {
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
						scrollPanel.add(HTMLUserSubscriptionReport.generateUserSubscriptionReport(result));
					}
				});
				}
				}
			};
		});
		
		/*
		 * Erzeugt einen Button der das Erstellen des Reports für die abonnierten Hashtags eines ausgewählten Users ausgibt.
		 */
		
		Hashtagsubscriptions = new Button("Hashtagsubscriptions", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				infoBox.clear();
				scrollPanel.clear();
				if(suggestBox.getText() == ""){
					infoBox.setWarningText("Please select a user!");
				}else{	
					String nickName = getNickName(suggestBox.getText());
					user = getUserOutOfAllUser(nickName);
				
				if (user == null){
					infoBox.setErrorText("Unknown User!");
				}else{
				/*
				 * Lädt alle Hashtags, die der ausgewählte User abonniert hat.
				 */ 
				administration.getAllSubscribedHashtagsFromUser(user, new AsyncCallback<Vector<Hashtag>>() {
					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Vector<Hashtag> result) {
						RootPanel.get("Details").clear();
						scrollPanel.setSize("100%", "100%");
						RootPanel.get("Details").add(scrollPanel);
						scrollPanel.add(HTMLHashtagSubscriptionReport.generateHashtagSubscriptionReport(result));
					}
				});
				}
				}
			};
		});
		
		/*
		 * Erzeugt einen Button der das Erstellen des Reports für Follower eines ausgewählten User´s ausgibt.
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
				 * Überprüfung ob der Text des Suchfeldes leer ist.
				 */
				if(suggestBox.getText() == ""){
					infoBox.setWarningText("Please select a user!");
				}else{	
				/*
				* Erzeugt aus den Text aus dem Suchfeld den Nicknamen und
				* im Anschluss wird anhand des Nicknamen der User über die
				* Methode getUserOutOfAllUser(nickName) ermittelt.
				*/
				String nickName = getNickName(suggestBox.getText());
				User user = getUserOutOfAllUser(nickName);
				
				/*
				 * Überprüfung ob die Suche nach einem User mit dem
				 * entsprechenden Nickname keinen User zurückgab.
				 */
				if (user == null){
					infoBox.setErrorText("Unknown User!");
				}else{
				
				/*
				 * Lädt alle User, die der ausgewählte User abonniert hat und startet die Methode 
				 * "generateUserSubscriptionReport(result)" der Klasse "HTMLUserSubscriptionReport", 
				 * die den Report aufbaut und im ScrollPanel ausgibt
				 * 
				 * @see HTMLUserSubscriptionReport
				 */ 
				administration.getAllFollowerFromUser(user, new AsyncCallback<Vector<User>>() {
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
						scrollPanel.add(HTMLUserFollowerReport.generateUserFollowerReport(result));
					}
				});
				}
				}
			};
		});
		
		/*
		 * Anlegen einer chatFlexTable zum Anordnen der verschiedenen Widgets im Navigatorbereich
		 */
		// Text
		chatFlexTable.setText(0, 0, "Subscription of:");

		// Textbox
		chatFlexTable.setWidget(0, 1, suggestBox);

		// Show-Button
		chatFlexTable.setWidget(3, 1, Usersubscriptions);
		chatFlexTable.setWidget(4, 1, Hashtagsubscriptions);
		chatFlexTable.setWidget(5, 1, Follower);
		
		//Hinzufügen der infoBox für Fehlermeldungen
		chatFlexTable.setWidget(6, 1, infoBox);
		
		// Hinzufügen der widgets
		mainPanel.add(chatFlexTable);
		mainPanel.add(addPanel);
		RootPanel.get("Navigator").clear();
		RootPanel.get("Navigator").add(mainPanel);
		
		
	/*
	 * Auslesen der registrierten User & Hinzufügen des ausgewählten Users in die SuggestBox
	 */
		administration.getAllUsers(new AsyncCallback<Vector<User>>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Vector<User> result) {
				UserSubscription.allUser = result;
				setOracle();
			}
		});
	}
	
	/*
	 * Setzt die nicht ausgewählten User als Vorschläge im Suchfeld.
	 */
	private void setOracle() {
		oracle.clear();
		for (int i = 0; i < allUser.size(); i++) {
			oracle.add(getOracleName(allUser.get(i).getFirstName(), allUser
					.get(i).getEmail()));
		}
	}
	
	private String getOracleName(String firstName, String email) {
		StringBuffer bufferName = new StringBuffer(email);
		bufferName.setLength(bufferName.indexOf("@"));
		String nickName = bufferName.toString();
		String name = new String(firstName + " (" + nickName + ")");
		return name;

	}
	
	/*
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
	
	public User getUserOutOfAllUser(String nickName) {
		for (int i = 0; i < allUser.size(); i++) {
			if (nickName.equals(setNickName(allUser.get(i).getEmail()))) {
				User user = allUser.get(i);
				return user;
			}
		}
		return null;
	}
	
	/*
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
}
