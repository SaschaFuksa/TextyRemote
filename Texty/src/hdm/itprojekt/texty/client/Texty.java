package hdm.itprojekt.texty.client;


import hdm.itprojekt.texty.client.gui.Footer;
import hdm.itprojekt.texty.client.gui.TextyReport;

import java.util.ArrayList;





import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


//import hdm.itprojekt.texty.shared.*;


public class Texty implements EntryPoint {
	
	 
	//private VerticalPanel vertPanel = new VerticalPanel();
	//private Button testButton = new Button();
	
	private VerticalPanel footerPanel = new VerticalPanel();
	private Label footerLabel = new Label("About");
	
	/*private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";*/

	
	/*private final TextyAdministrationAsync textyService = GWT
			.create(TextyAdministration.class);*/


	public void onModuleLoad() {

		
		footerLabel.addStyleName("Footer");
		footerPanel.add(footerLabel);
		RootPanel.get("Footer").add(footerLabel);
		
		footerLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				RootPanel.get("Details").clear();
				RootPanel.get("Details").add(new Footer());

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

				Window.Location
						.assign("http://127.0.0.1:8888/TextyReports.html");
			}
		};

		// Make some sub-menus that we will cascade from the top menu.
		MenuBar subMenu1 = new MenuBar(true);
		subMenu1.addItem("Public Conversation", cmd);
		subMenu1.addItem("Private Conversations", cmd);
		subMenu1.setStyleName("menubar");
		
		// Make some sub-menus that we will cascade from the top menu.
		MenuBar fooMenu = new MenuBar(true);
		fooMenu.addItem("New Conversation", cmd);
		fooMenu.addItem("Show Conversations", subMenu1);
		fooMenu.setStyleName("menubar");

		// Make some sub-menus that we will cascade from the top menu.
		MenuBar subMenu2 = new MenuBar(true);
		subMenu2.addItem("Create", cmd);
		subMenu2.addItem("Show", cmd);
		subMenu2.setStyleName("menubar");

		// Make some sub-menus that we will cascade from the top menu.
		MenuBar subMenu3 = new MenuBar(true);
		subMenu3.addItem("Create", cmd);
		subMenu3.addItem("Show", cmd);
		subMenu3.setStyleName("menubar");

		MenuBar barMenu = new MenuBar(true);
		barMenu.addItem("Hashtagsubscription", subMenu2);
		barMenu.addItem("Usersubscription", subMenu3);
		barMenu.setStyleName("menubar");

		// Make a new menu bar, adding a few cascading menus to it.
		MenuBar menu = new MenuBar();
		menu.addItem("Conversation", fooMenu);
		menu.addItem("Subscription", barMenu);
		menu.addItem("ReportGenerator", cmdReport);
		
		menu.setStyleName("menubar");
		 
		// Add it to the root panel.
		RootPanel.get("Menu").add(menu);
	}
}
