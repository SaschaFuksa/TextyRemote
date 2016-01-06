package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Die Menübar leitet den User durch Tabs an die jeweilige Kategorien weiter.
 * 
 * 
 */
public class TextyMenu {

	private TextyCommand command = new TextyCommand();

	public void execute(String client) {

		switch (client) {
		case "Editor":

			MenuBar menu = new MenuBar();
			menu.addItem("Home", command.getCommand("Home"));
			menu.addItem("Conversation", command.getCommand("Conversation"));
			menu.addItem("Subscription", command.getCommand("Subscription"));
			menu.addItem("Profile", command.getCommand("Profile"));
			menu.setStyleName("menubar");

			RootPanel.get("Menu").add(menu);
			break;
		case "Report":
			// TODO Reportmenü
			break;
		default:
			;
		}
	}

}
