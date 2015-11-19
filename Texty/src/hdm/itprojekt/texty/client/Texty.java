package hdm.itprojekt.texty.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

//import hdm.itprojekt.texty.shared.*;

public class Texty implements EntryPoint {
	
	
	private VerticalPanel vertPanel = new VerticalPanel();
	private Button testButton = new Button();
	
	/*private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";*/

	
	/*private final TextyAdministrationAsync textyService = GWT
			.create(TextyAdministration.class);*/

	public void onModuleLoad() {
		
		//Anlegen der Variable rootPanel
		RootPanel rootPanel = RootPanel.get();
		
		testButton.getElement().setId("editButton");
		vertPanel.add(testButton);
		rootPanel.get("testButtonContainer").add(vertPanel);
		
		//Anlegen des Buttons Unterhaltung
		Button b1 = new Button("Unterhaltung", new ClickHandler() {

			public void onClick(ClickEvent event) {
				
			}
		}); 
		
		//Anlegen des Buttons Abonnement
		Button b2 = new Button("Abonnement", new ClickHandler(){
			
			public void onClick(ClickEvent event) {
				Window.alert("Just a Test");
			}	
		}); 
		
		//Anlegen des Buttons Report
		Button b3 = new Button("Report", new ClickHandler(){


			public void onClick(ClickEvent event) {
				
			}
		});
		
		
		//Anlegen des FlowPenals
		FlowPanel fp = new FlowPanel();
		
		//Hinzufügen der Buttons zum FlowPanel
		fp.add(b1);
		fp.add(b2);
		fp.add(b3);
		
		
		//Hinzufügen des FlowPanels zum RootPanel
		rootPanel.add(fp);
		
		
		//Kann vielleicht noch gebraucht werden
		//MultiWordSuggestOracle suggestBox = new MultiWordSuggestOracle();
		
		
	
		
		
	}
}
