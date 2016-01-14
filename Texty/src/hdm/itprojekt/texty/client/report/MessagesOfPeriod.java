package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.client.ClientsideSettings;
import hdm.itprojekt.texty.client.InfoBox;
import hdm.itprojekt.texty.client.TextyForm;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;


public class MessagesOfPeriod extends TextyForm {

	/**
	 * Deklaration, Definition und Initialisierung der Widgets.
	 */
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private ScrollPanel scrollPanel = new ScrollPanel();
	private InfoBox infoBox = new InfoBox();
	private Button MessageReport;
	
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
	private Date date1;
	private Date date2;
	
	/**
	 * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular.
	 * 
	 * @see TextyForm
	 * @param headline
	 */
	public MessagesOfPeriod(String headline) {
		super(headline);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Diese Methode wird sofort aufgerufen, sobald ein Formular im Browser
	 * eingebaut wird.
	 * 
	 * @see TextyForm
	 */
	@Override
	public void run() {
				
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
	        date2.setHours(23);
	        date2.setMinutes(59);
	        date2.setSeconds(59);
	      }
	    });

	   
		
		// Create UI
	    //Hinzufügen des Buttons zum auslösen des MessagesOfPeriod-Report
	    MessageReport = new Button("Show Messages", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				infoBox.clear();
				scrollPanel.clear();
				if (date1 == null || date2 == null){
					if (date1 == null && date2 == null)	
						infoBox.setWarningText("Please select startdate & enddate!");
					else if(date1 == null)
						infoBox.setWarningText("Please select startdate!");
					else
						infoBox.setWarningText("Please select enddate!");
				}else  {
					
				administration.getAllMessagesByDate(date1, date2, new AsyncCallback<Vector<Message>>() {
					@Override
					public void onFailure(Throwable caught) {

					}
					@Override
					public void onSuccess(Vector<Message> result) {
						Collections.reverse(result);
						scrollPanel.setSize("100%", "100%");
						RootPanel.get("Details").add(scrollPanel);
						scrollPanel.add(HTMLMessagesOfPeriodReport.generateMessagesOfPeriodReport(result));
					}
				});
				}
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
		
		//InfoBox hinzufügen
		chatFlexTable.setWidget(5, 0, infoBox);
		
		// Hinzufügen der widgets
		mainPanel.add(chatFlexTable);
		mainPanel.add(addPanel);
		RootPanel.get("Navigator").clear();
		RootPanel.get("Navigator").add(mainPanel);
		
	}
	
}
