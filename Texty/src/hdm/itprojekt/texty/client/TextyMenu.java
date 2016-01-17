package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Die Men�bar leitet den User im Client Editor und im Client ReportGenerator in
 * die einzelnen GUI Bereiche weiter.
 */
public class TextyMenu {

	/**
	 * Die Klasse TextyCommand f�gt dem Men� die entsprechende Funktionen hinzu,
	 * die ausgef�hrt werden wenn die einzelnen Men�punkte angeklickt werden.
	 * 
	 * @see TextyCommand
	 */
	private TextyCommand command = new TextyCommand();

	/**
	 * Die execute() Operation f�hrt die Deklaration, Definition und
	 * Initialisierung des Men�s aus.
	 * 
	 * @param client
	 */
	public void execute(String client) {

		/*
		 * Initialisierung des Men�s und Zuteilung der einzelnen Bereiche.
		 */
		MenuBar menu = new MenuBar();

		/*
		 * Das Men� unterscheidet mittels einer Switch Statement zwischen den
		 * Client Editor und Client ReportGenerator, da je Client verscheidene
		 * Funktionalit�ten des Men�s ben�tigt werden.
		 */
		switch (client) {
		case "Editor":

			/*
			 * Zuteilung der einzelnen Bereiche and das Men�.
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
			 * Initialisierung des Submen�s und Zuteilung der einzelnen
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
			 * Zuteilung der einzelnen Bereiche and das Men�.
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
