package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Die Menübar leitet den User im Client Editor und im Client ReportGenerator in
 * die einzelnen GUI Bereiche weiter.
 */
public class TextyMenu {

	/**
	 * Die Klasse TextyCommand fügt dem Menü die entsprechende Funktionen hinzu,
	 * die ausgeführt werden wenn die einzelnen Menüpunkte angeklickt werden.
	 * 
	 * @see TextyCommand
	 */
	private TextyCommand command = new TextyCommand();

	/**
	 * Die execute() Operation führt die Deklaration, Definition und
	 * Initialisierung des Menüs aus.
	 * 
	 * @param client
	 */
	public void execute(String client) {

		/*
		 * Initialisierung des Menüs und Zuteilung der einzelnen Bereiche.
		 */
		MenuBar menu = new MenuBar();

		/*
		 * Das Menü unterscheidet mittels einer Switch Statement zwischen den
		 * Client Editor und Client ReportGenerator, da je Client verscheidene
		 * Funktionalitäten des Menüs benötigt werden.
		 */
		switch (client) {
		case "Editor":

			/*
			 * Zuteilung der einzelnen Bereiche and das Menü.
			 */
			menu.addItem("Home", command.getCommand("Home"));
			menu.addItem("Conversation", command.getCommand("Conversation"));
			menu.addItem("Subscription", command.getCommand("Subscription"));
			menu.addItem("Profile", command.getCommand("Profile"));

			/*
			 * Zuweisung der Styles an das jeweilige Widget.
			 */
			menu.setStyleName("menubar");

			/*
			 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
			 */
			RootPanel.get("Menu").add(menu);
			break;
		case "Report":

			/*
			 * Initialisierung des Submenüs und Zuteilung der einzelnen
			 * Bereiche.
			 */
			MenuBar subMenu = new MenuBar(true);
			subMenu.addItem("Messages of Hashtag",
					command.getCommand("MessagesOfHashtag"));
			subMenu.addItem("Messages of user",
					command.getCommand("MessagesOfUser"));
			subMenu.addItem("Messages of period",
					command.getCommand("MessagesOfPeriod"));
			subMenu.addItem("Messages of user in period",
					command.getCommand("MessagesOfUserInPeriod"));
			
			MenuBar subMenu2 = new MenuBar(true);
			subMenu2.addItem("Usersubscriptions",
					command.getCommand("UserSubscriptionReport"));
			subMenu2.addItem("Hashtagsubscriptions",
					command.getCommand("HashtagSubscriptionReport"));
			/*
			 * Zuteilung der einzelnen Bereiche and das Menü.
			 */
			menu.addItem("Messagereports", subMenu);
			menu.addItem("Subscriptionreports",subMenu2);

			/*
			 * Zuweisung der Styles an das jeweilige Widget.
			 */
			menu.setStyleName("menubar");

			/*
			 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
			 */
			RootPanel.get("Menu").add(menu);
			break;
		default:
			;
		}
	}

}
