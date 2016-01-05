package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.client.ClientsideSettings;
import hdm.itprojekt.texty.client.TextyForm;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;


public class MessagesOfPeriod extends TextyForm {

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

	public MessagesOfPeriod(String headline) {
		super(headline);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		
		// Create a date picker
	    DateBox dateBox = new DateBox();
	    final Label text = new Label();

	    // Set the value in the text box when the user selects a date
	    dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
	      public void onValueChange(ValueChangeEvent<Date> event) {
	        Date date = event.getValue();
	        String dateString = DateTimeFormat.getFormat("dd.MM.yyyy").format(date);
	        text.setText(dateString);
	      }
	    });

	    // Set the default value
	    dateBox.setValue(new Date(), false);
	    
	 // Create a second date picker
	    DateBox dateBox2 = new DateBox();
	    final Label text2 = new Label();

	    // Set the value in the text box when the user selects a date
	    dateBox2.addValueChangeHandler(new ValueChangeHandler<Date>() {
	      public void onValueChange(ValueChangeEvent<Date> event) {
	        Date date = event.getValue();
	        String dateString = DateTimeFormat.getFormat("dd.MM.yyyy").format(date);
	        text2.setText(dateString);
	      }
	    });

	    // Set the default value
	    dateBox2.setValue(new Date(), false);
	    
	    // Add the widgets to the page
	    //RootPanel.get("Navigator").add(text);
	    //RootPanel.get("Navigator").add(datePicker);
		
		
		// Create UI
		
		MessageReport = new Button("Show Messages");
		//TODO Funktionalität des Buttons

		// Text
		chatFlexTable.setText(0, 0, "Messagereport in Period of:");
		chatFlexTable.setText(1, 0, "Start:");
		chatFlexTable.setText(1, 1, "End:");
		
		// Textboxen
		chatFlexTable.setWidget(2, 0, dateBox);
		chatFlexTable.setWidget(2, 1, dateBox2);
		// Save-Button
		chatFlexTable.setWidget(4, 0, MessageReport);
		
		
		// show.addClickHandler(new ClickHandler() {
		// public void onClick(ClickEvent event) {

		mainPanel.add(chatFlexTable);
		mainPanel.add(addPanel);
		RootPanel.get("Navigator").clear();
		RootPanel.get("Navigator").add(mainPanel);
		
	}
	
}
