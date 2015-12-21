package hdm.itprojekt.texty.client.Report;

import hdm.itprojekt.texty.client.ClientsideSettings;
import hdm.itprojekt.texty.client.TextyForm;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.User;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


public class SubscriptionReport extends TextyForm{
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox mail = new TextBox();
	private Button Usersubscriptions = new Button("Usersubscriptions");
	private Button Hashtagsubscriptions = new Button("Hashtagsubscriptions");
	public static User user;


	 public SubscriptionReport(String headline) {
		super(headline);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		// Create UI
		
				// Text
				chatFlexTable.setText(0, 0, "Subscription of:");
				
				// Textboxen
				chatFlexTable.setWidget(0, 1, mail);				
				
				// Save-Button
				chatFlexTable.setWidget(3, 1, Usersubscriptions);
				chatFlexTable.setWidget(3, 2, Hashtagsubscriptions);
				//show.addClickHandler(new ClickHandler() {
					//public void onClick(ClickEvent event) {
						
				
				mainPanel.add(chatFlexTable);
				mainPanel.add(addPanel);
				RootPanel.get("Details").add(mainPanel);
			}

	
		}



	

