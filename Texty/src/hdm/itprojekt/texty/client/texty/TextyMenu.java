package hdm.itprojekt.texty.client.texty;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Die Menübar leitet den User durch Tabs an die jeweilige Kategorien weiter.
 * 
 * 
 */
public class TextyMenu {

	private TextyCommand command = new TextyCommand();

	public void execute() {

		MenuBar menu = new MenuBar();
		menu.addItem("Home", command.getCommand("Home"));
		menu.addItem("Conversation", command.getCommand("Conversation"));
		menu.addItem("Community", command.getCommand("Community"));
		menu.addItem("Hashtag", command.getCommand("Hashtag"));
		menu.addItem("Profile", command.getCommand("Profile"));
		menu.addItem("ReportGenerator", command.getCommand("Report"));
		menu.setStyleName("menubar");

		RootPanel.get("Menu").add(menu);
	}

}
