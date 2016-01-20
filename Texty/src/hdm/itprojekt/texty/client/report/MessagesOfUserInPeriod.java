package hdm.itprojekt.texty.client.report;

import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

import hdm.itprojekt.texty.client.ClientsideSettings;
import hdm.itprojekt.texty.client.InfoBox;
import hdm.itprojekt.texty.client.TextyForm;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

/**
 * In dieser Klasse wird das UI für die Nachrichten eines ausgewählten User´s in einem 
 * ausgewählten Zeitraum aufgebaut. 
 * Die Widgets werden im Navigatorbereich implementiert.
 *
 */
public class MessagesOfUserInPeriod extends TextyForm{
	
	/**
	 * Deklaration, Definition und Initialisierung der Widgets.
	 */
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private Button MessageReport;
	private ScrollPanel scrollPanel = new ScrollPanel();
	private InfoBox infoBox = new InfoBox();
	
	/**
	 * Die administration ermöglicht die asynchrone Kommunikation mit der
	 * Applikationslogik.
	 */
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();
	
	/**
	 * Deklaration & Definition von Variablen der Klasse.
	 */
	private User selectedUser;
	private static Vector<User> allUser = new Vector<User>();
	private Date date1;
	private Date date2;
	
	/**
	 * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular.
	 * 
	 * @see TextyForm
	 * @param headline
	 */
	public MessagesOfUserInPeriod(String headline) {
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
		
		//Aufbau der GUI
		
		// Hinzufügen eines date pickers für das Startdatum
	    DateBox startDateBox = new DateBox();
	    startDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss")));
	    startDateBox.setFireNullValues(true);

	    // Setzen des Wertes in die TextBox, den der User auswählt
	    startDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
	      public void onValueChange(ValueChangeEvent<Date> event) {
	        date1 = event.getValue();
	      }
	      
	    });

	 // Hinzufügen eines date pickers für das Enddatum
	    DateBox endDateBox = new DateBox();
	    endDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss")));
	    endDateBox.setFireNullValues(true);
	    
	    // Setzen des Wertes in die TextBox, den der User auswählt
	    endDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
	      public void onValueChange(ValueChangeEvent<Date> event) {
	        date2 = event.getValue();
	      }
	    });

		//Hinzufügen des Buttons zum auslösen des MessagesOfUserInPeriod-Reports
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
					selectedUser = getUserOutOfAllUser(nickName);
				/*
				 * Überprüfung ob die Suche nach einem User mit dem
				 * entsprechenden Nickname keinen User zurückgab.
				 */
				if (selectedUser == null){
					infoBox.setErrorText("Unknown User!");
				}else{
					/*
					 * Überprüfung ob eine der Dateboxen den wert "null"
					 * zurückgibt.
					 */
					if (date1 == null || date2 == null){
						/*
						 * Überprüfung ob beide Dateboxen den wert "null" zurückgeben.
						 */
						if (date1 == null && date2 == null)	
							/*
							 * Setzt Warnung, dass Start- und Enddatum ausgewählt werden müssen
							 */
							infoBox.setWarningText("Please select startdate & enddate!");
						/*
						 * Überprüfung ob startDateBox den wert "null" zurückgeben.
						 */
						else if(date1 == null)
							/*
							 * Setzt Warnung, dass Startdatum ausgewählt werden muss.
							 */
							infoBox.setWarningText("Please select startdate!");
						else
							/*
							 * Setzt Warnung, dass Enddatum ausgewählt werden muss.
							 */
							infoBox.setWarningText("Please select enddate!");
					}else 
				
						/*
						* Lädt alle Nachrichten, in denen der ausgewählte User der Author ist und startet die Methode 
						* "generateMessagesOfUserReport(result)" der Klasse "HTMLMessagesFromUserReport", 
						* die den Report aufbaut und im ScrollPanel ausgibt
						* 
						* @see HTMLMessagesFromUserReport
						*/  
				administration.getAllMessagesWhereUserIsAuthorByDate(selectedUser, date1, date2,new AsyncCallback<Vector<Message>>() {
					@Override
					public void onFailure(Throwable caught) {

					}
					@Override
					public void onSuccess(Vector<Message> result) {
						if (result.isEmpty()
								|| result.size() == 0
								|| result == null) {
							Window.alert("No Messages written by the chosen user in chosen period ");
						} else {
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
						scrollPanel.add(HTMLMessagesOfUserInPeriod.generateMessagesFromUserInPeriodReport(result, date1 , date2, selectedUser));
					}
					}
				});
				}
				}
			};
		});
		
							    
					  
		//Anlegen einer chatFlexTable zum Anordnen der verschiedenen Widgets im Navigatorbereich	
		// Text
		chatFlexTable.setText(0, 0, "Please select User and Dateperiod");
		chatFlexTable.setText(1, 0, "Messagereport for:");
		chatFlexTable.setText(3, 0, "in Dateperiod from:");
		chatFlexTable.setText(4, 0, "                to:");
		
		// Textboxe
		chatFlexTable.setWidget(1, 1, suggestBox);
		chatFlexTable.setWidget(3, 1, startDateBox);
		chatFlexTable.setWidget(4, 1, endDateBox);
		
		// Show-Button
		chatFlexTable.setWidget(5, 1, MessageReport);
		
		//Hinzufügen der infoBox für Fehlermeldungen
		chatFlexTable.setWidget(6, 1, infoBox);
				
		// Hinzufügen der widgets
		mainPanel.add(chatFlexTable);
		mainPanel.add(addPanel);
		RootPanel.get("Navigator").clear();
		RootPanel.get("Navigator").add(mainPanel);
		
		//Auswählen eines registrierten Users aus/in der SuggestBox
		administration.getAllUsers(new AsyncCallback<Vector<User>>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Vector<User> result) {
				MessagesOfUserInPeriod.allUser = result;
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
