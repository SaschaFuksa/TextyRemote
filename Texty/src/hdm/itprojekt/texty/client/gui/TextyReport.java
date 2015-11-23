package hdm.itprojekt.texty.client.gui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

public class TextyReport implements EntryPoint {

	public void onModuleLoad() {

		Command cmd = new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		};

		Command cmdEditor = new Command() {
			public void execute() {
				Window.Location.assign("http://127.0.0.1:8888/Texty.html");
			}
		};

		// Make some sub-menus that we will cascade from the top menu.
		MenuBar fooMenu = new MenuBar(true);
		fooMenu.addItem("HashtagReport", cmd);
		fooMenu.addItem("Back", cmdEditor);

		// Make a new menu bar, adding a few cascading menus to it.
		MenuBar menuReport = new MenuBar();
		menuReport.addItem("Report", fooMenu);

		// Add it to the root panel.
		RootPanel.get("Menu").add(menuReport);

	}

}
