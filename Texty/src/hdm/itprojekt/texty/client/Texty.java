package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.client.gui.About;
import hdm.itprojekt.texty.client.gui.TextyReport;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
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
	
	
	//private VerticalPanel vertPanel = new VerticalPanel();
	//private Button testButton = new Button();
	
	private VerticalPanel trailerPanel = new VerticalPanel();
	private Label aboutLabel = new Label("Impressum");
	
	/*private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";*/

	
	/*private final TextyAdministrationAsync textyService = GWT
			.create(TextyAdministration.class);*/

	public void onModuleLoad() {
		
		aboutLabel.addStyleName("impressum");
		trailerPanel.add(aboutLabel);
		RootPanel.get("Trailer").add(aboutLabel);
		
		aboutLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				RootPanel.get("Details").clear();
				RootPanel.get("Details").add(new About());

			}
		});
		
		// Menübar einfügen
		Command cmd = new Command() {
		      public void execute() {
		        Window.alert("You selected a menu item!");
		      }
		    };
		    
		 Command cmdReport = new Command() {
			      public void execute() {
			    	  //http://127.0.0.1:8888/TextyReport.java;
			    	  TextyReport report= new TextyReport();
			    	  report.onModuleLoad();
			      }
			    };
		    
		    // Make some sub-menus that we will cascade from the top menu.
		    MenuBar subMenu1 = new MenuBar(true);
		    subMenu1.addItem("Public Conversation", cmd);
		    subMenu1.addItem("Private Conversations", cmd);
		    
		    // Make some sub-menus that we will cascade from the top menu.
		    MenuBar fooMenu = new MenuBar(true);
		    fooMenu.addItem("New Conversation", cmd);
		    fooMenu.addItem("Show Conversations", subMenu1);
		   

		 // Make some sub-menus that we will cascade from the top menu.
		    MenuBar subMenu2 = new MenuBar(true);
		    subMenu2.addItem("Create", cmd);
		    subMenu2.addItem("Show", cmd);
		    
		 // Make some sub-menus that we will cascade from the top menu.
		    MenuBar subMenu3 = new MenuBar(true);
		    subMenu3.addItem("Create", cmd);
		    subMenu3.addItem("Show", cmd);
		    
		    MenuBar barMenu = new MenuBar(true);
		    barMenu.addItem("Hashtagsubscription", subMenu2);
		    barMenu.addItem("Usersubscription", subMenu3);
		    

		    // Make a new menu bar, adding a few cascading menus to it.
		    MenuBar menu = new MenuBar();
		    menu.addItem("Conversation", fooMenu);
		    menu.addItem("Subscription", barMenu);
		    menu.addItem("Report",cmdReport);
		    
		    

		    // Add it to the root panel.
		    RootPanel.get("Menue").add(menu);
	}
		

		
		
		
		
		/*//Anlegen der Variable rootPanel
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
*/		
		
	
		
		
	}

