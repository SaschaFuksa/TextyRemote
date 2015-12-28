package hdm.itprojekt.texty.client.report;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Die Menübar leitet den User durch Tabs an die jeweilige Kategorien weiter.
 * 
 * 
 */
public class TextyReportMenu {

	private TextyReportCommand reportCommand = new TextyReportCommand();

	public void execute() {

		// Make some sub-menus that we will cascade from the top menu.
		MenuBar messageReportMenu = new MenuBar(true);
		messageReportMenu.addItem("MessagesOfUser",
				reportCommand.getCommand("MessagesOfUser"));
		messageReportMenu.addItem("MessagesOfPeriod",
				reportCommand.getCommand("MessagesOfPeriod"));
		messageReportMenu.addItem("MessagesOfUserInPeriod",
				reportCommand.getCommand("MessagesOfUserInPeriod"));

		MenuBar menu = new MenuBar();
		menu.addItem("MessageReports", messageReportMenu);
		menu.addItem("SubscriptionReports",
				reportCommand.getCommand("SubscriptionReport"));
		menu.addItem("Editor", reportCommand.getCommand("Editor"));
		menu.setStyleName("menubar");

		RootPanel.get("Menu").add(menu);
	}

}