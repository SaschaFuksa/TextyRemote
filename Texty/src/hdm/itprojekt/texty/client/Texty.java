package hdm.itprojekt.texty.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

public class Texty implements EntryPoint {

	public void onModuleLoad() {

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
		menu.addItem("Report", cmdReport);

		// Add it to the root panel.
		RootPanel.get("Menue").add(menu);
	}
}
