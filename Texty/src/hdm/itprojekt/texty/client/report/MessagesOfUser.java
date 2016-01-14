package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.client.ClientsideSettings;
import hdm.itprojekt.texty.client.InfoBox;
import hdm.itprojekt.texty.client.TextyForm;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Collections;
import java.util.Vector;

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

public class MessagesOfUser extends TextyForm {

	/**
	 * Deklaration, Definition und Initialisierung der Widgets.
	 */
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private Button MessageReport;
	private InfoBox infoBox = new InfoBox();
	private ScrollPanel scrollPanel = new ScrollPanel();
	
	/**
	 * Die administration ermöglicht die asynchrone Kommunikation mit der
	 * Applikationslogik.
	 */
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();
	
	/**
	 * Deklaration & Definition von Variablen der Klasse.
	 */
	public static User user;
	private static Vector<User> allUser = new Vector<User>();
	
	/**
	 * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular.
	 * 
	 * @see TextyForm
	 * @param headline
	 */
	public MessagesOfUser(String headline) {
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
		 * Erzeugt einen Button der das Erstellen des Reports für Nachrichten eines ausgewählten Users ausgibt.
		 */ 
		MessageReport = new Button("Show Messages", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*
				 * Entfernung der evtl. zuvor ausgegebenen Fehlermeldung
				 * in der infoBox
				 */
				infoBox.clear();
				
				/*
				 * Entfernung des evtl. zuvor generierten Reports
				 */
				scrollPanel.clear();
				
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
				* Lädt alle Nachrichten, in denen der ausgewählte User der Author ist und startet die Methode 
				* "generateMessagesOfUserReport(result)" der Klasse "HTMLMessagesFromUserReport", 
				* die den Report aufbaut und im ScrollPanel ausgibt
				* 
				* @see HTMLMessagesFromUserReport
				*/ 
				
				administration.getAllMessagesWhereUserIsAuthor(user, new AsyncCallback<Vector<Message>>() {
					@Override
					public void onFailure(Throwable caught) {

					}
					@Override
					public void onSuccess(Vector<Message> result) {
						/*
						 * Umkehrung der Reihenfolge der Liste.
						 */
						Collections.reverse(result);
						/*
						 * Zuweisung und Anpassung des Widgets.
						 */
						scrollPanel.setSize("100%", "100%");
						RootPanel.get("Details").add(scrollPanel);
						/*
						 * Fügt den generierten Report dem scrollPanel hinzu.
						 */
						scrollPanel.add(HTMLMessagesFromUserReport.generateMessagesOfUserReport(result));
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
		chatFlexTable.setText(0, 0, "Messagereport for:");

		// Textbox
		chatFlexTable.setWidget(0, 1, suggestBox);

		// Show-Button
		chatFlexTable.setWidget(3, 1, MessageReport);
		
		//InfoBox hinzufügen
		chatFlexTable.setWidget(4, 1, infoBox);
		
		
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
				MessagesOfUser.allUser = result;
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
