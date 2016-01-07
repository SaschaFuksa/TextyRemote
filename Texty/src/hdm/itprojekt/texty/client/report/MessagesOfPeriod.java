package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.client.ClientsideSettings;
import hdm.itprojekt.texty.client.TextyForm;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;


public class MessagesOfPeriod extends TextyForm {

	public static User user;
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();

	private Button MessageReport;
	private Date date1;
	private Date date2;
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();


	public MessagesOfPeriod(String headline) {
		super(headline);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		
		// Hinzufügen eines date pickers für das Startdatum
	    DateBox startDateBox = new DateBox();

	    // Setzen des Wertes in die TextBox, den der User auswählt
	    startDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
	      public void onValueChange(ValueChangeEvent<Date> event) {
	        date1 = event.getValue();
	      }
	    });

	    
	    // Hinzufügen eines date pickers für das Enddatum
	    DateBox endDateBox = new DateBox();
	   
	    // Setzen des Wertes in die TextBox, den der User auswählt
	    endDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
	      public void onValueChange(ValueChangeEvent<Date> event) {
	        date2 = event.getValue();
	      }
	    });

	   
		
		// Create UI
	    //Hinzufügen des Buttons zum auslösen des MessagesOfPeriod-Report
	    MessageReport = new Button("Show Messages", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				administration.getAllMessagesByDate(date1, date2, new AsyncCallback<Vector<Message>>() {
					@Override
					public void onFailure(Throwable caught) {

					}
					@Override
					public void onSuccess(Vector<Message> result) {
						RootPanel.get("Details").clear();
						RootPanel.get("Details").add(HTMLMessagesOfPeriodReport.generateMessagesOfPeriodReport(result));
					}
				});
			};
		});
	    
		//Anlegen einer chatFlexTable zum Anordnen der verschiedenen Widgets im Navigatorbereich
		// Text
		chatFlexTable.setText(0, 0, "Messagereport in Period of:");
		chatFlexTable.setText(1, 0, "Start:");
		chatFlexTable.setText(1, 1, "End:");
		
		// Textboxen
		chatFlexTable.setWidget(2, 0, startDateBox);
		chatFlexTable.setWidget(2, 1, endDateBox);
		// Save-Button
		chatFlexTable.setWidget(4, 0, MessageReport);
		
		// Hinzufügen der widgets
		mainPanel.add(chatFlexTable);
		mainPanel.add(addPanel);
		RootPanel.get("Navigator").clear();
		RootPanel.get("Navigator").add(mainPanel);
		
	}
	
}
