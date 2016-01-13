package hdm.itprojekt.texty.client.report;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
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

public class MessagesOfUserInPeriod extends TextyForm{
	
	public static User user;
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private Button MessageReport;
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();
	private static Vector<User> allUser = new Vector<User>();
	private Date date1;
	private Date date2;
	private ScrollPanel scrollPanel = new ScrollPanel();
	private InfoBox infoBox = new InfoBox();

	public MessagesOfUserInPeriod(String headline) {
		super(headline);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void run() {
		
		//Aufbau der GUI
		
		// Hinzuf�gen eines date pickers f�r das Startdatum
	    DateBox startDateBox = new DateBox();
	    startDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss")));
	    startDateBox.setFireNullValues(true);

	    // Setzen des Wertes in die TextBox, den der User ausw�hlt
	    startDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
	      public void onValueChange(ValueChangeEvent<Date> event) {
	        date1 = event.getValue();
	      }
	      
	    });
//		// Create a date picker
//	    DateBox startDateBox = new DateBox();
//
//	    // Set the value in the text box when the user selects a date
//	    startDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
//	      public void onValueChange(ValueChangeEvent<Date> event) {
//	        date1 = event.getValue();
//	      }
//	    });

	 // Hinzuf�gen eines date pickers f�r das Enddatum
	    DateBox endDateBox = new DateBox();
	    endDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss")));
	    endDateBox.setFireNullValues(true);
	    // Setzen des Wertes in die TextBox, den der User ausw�hlt
	    endDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
	      public void onValueChange(ValueChangeEvent<Date> event) {
	        date2 = event.getValue();
	        date2.setHours(23);
	        date2.setMinutes(59);
	        date2.setSeconds(59);
	      }
	    });
//	 // Create a second date picker
//	    DateBox endDateBox = new DateBox();
//
//	    // Set the value in the text box when the user selects a date
//	    endDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
//	      public void onValueChange(ValueChangeEvent<Date> event) {
//	        date2 = event.getValue();
//	      }
//	    });


		//Hinzuf�gen des Buttons zum ausl�sen des MessagesOfUserInPeriod-Reports
		MessageReport = new Button("Show Messages", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				infoBox.clear();
				scrollPanel.clear();
				
				if(suggestBox.getText() == ""){
					infoBox.setWarningText("Please select a user!");
				}else{	
					String nickName = getNickName(suggestBox.getText());
					User user = getUserOutOfAllUser(nickName);
				
				if (user == null){
					infoBox.setErrorText("Unknown User!");
				}else{
					if (date1 == null || date2 == null){
						if (date1 == null && date2 == null)	
							infoBox.setWarningText("Please select startdate & enddate!");
						else if(date1 == null)
							infoBox.setWarningText("Please select startdate!");
						else
							infoBox.setWarningText("Please select enddate!");
					}else {
//				if (date1 == null || date2 == null || suggestBox.getText() == "")
//				if(suggestBox.getText() == ""){
//					infoBox.setWarningText("Please select a user!");
//				}else{	
//					String nickName = getNickName(suggestBox.getText());
//					User user = getUserOutOfAllUser(nickName);
//				
//				if (user == null){
//					infoBox.setErrorText("Unknown User!");
//				}else{
//					if (date1 == null || date2 == null){
//						if (date1 == null && date2 == null)	
//							infoBox.setWarningText("Please select startdate & enddate!");
//						else if(date1 == null)
//							infoBox.setWarningText("Please select startdate!");
//						else
//							infoBox.setWarningText("Please select enddate!");
//					}else {
//				
				//String nickName = getNickName(suggestBox.getText());
				//User user = getUserOutOfAllUser(nickName);
				
				// 
				administration.getAllMessagesWhereUserIsAuthorByDate(user, date1, date2,new AsyncCallback<Vector<Message>>() {
					@Override
					public void onFailure(Throwable caught) {

					}
					@Override
					public void onSuccess(Vector<Message> result) {
						scrollPanel.setSize("100%", "100%");
						RootPanel.get("Details").add(scrollPanel);
						scrollPanel.add(HTMLMessagesFromUserInPeriod.generateMessagesFromUserInPeriodReport(result));
					}
				});
				}
				}
			};
			}
		});
		
							    
					  
		//Anlegen einer chatFlexTable zum Anordnen der verschiedenen Widgets im Navigatorbereich	
		// Text
		chatFlexTable.setText(0, 0, "Please select User and Dateperiod");
		chatFlexTable.setText(1, 0, "Messagereport for:");
		chatFlexTable.setText(3, 0, "in Dateperiod from:");
		chatFlexTable.setText(4, 0, "                to:");
		
		// Textboxen
		chatFlexTable.setWidget(1, 1, suggestBox);
		chatFlexTable.setWidget(3, 1, startDateBox);
		chatFlexTable.setWidget(4, 1, endDateBox);
		
		// Show-Button
		chatFlexTable.setWidget(5, 1, MessageReport);
		
		//Hinzuf�gen der infoBox f�r Fehlermeldungen
		chatFlexTable.setWidget(6, 1, infoBox);
				
		// Hinzuf�gen der widgets
		mainPanel.add(chatFlexTable);
		mainPanel.add(addPanel);
		RootPanel.get("Navigator").clear();
		RootPanel.get("Navigator").add(mainPanel);
		
		//Ausw�hlen eines registrierten Users aus/in der SuggestBox
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
		
		private String setNickName(String email) {
			StringBuffer bufferName = new StringBuffer(email);
			bufferName.setLength(bufferName.indexOf("@"));
			String nickName = bufferName.toString();
			return nickName;
		}
	
}
