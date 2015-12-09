package hdm.itprojekt.texty.client.gui;

import hdm.itprojekt.texty.client.ShowHashtagSubscription;
import hdm.itprojekt.texty.client.ShowPrivateConversation;
import hdm.itprojekt.texty.client.ShowPublicConversation;
import hdm.itprojekt.texty.client.ShowUserSubscription;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

public class TextyMenu {

	private TextyCommand command = new TextyCommand();

	public void execute() {

		MenuBar subMenuConversation = new MenuBar(true);
		subMenuConversation.addItem("Public Conversation",
				command.getCommand(new ShowPublicConversation()));
		subMenuConversation.addItem("Private Conversations",
				command.getCommand(new ShowPrivateConversation()));
		subMenuConversation.setStyleName("menubar");

		MenuBar mainMenuConversation = new MenuBar(true);
		mainMenuConversation.addItem("New Conversation",
				command.getCommand(new NewConversation()));
		mainMenuConversation.addItem("Show Conversations", subMenuConversation);
		mainMenuConversation.setStyleName("menubar");

		MenuBar subMenuHashtagSubscription = new MenuBar(true);	
		subMenuHashtagSubscription.addItem("Show",
				command.getCommand(new ShowHashtagSubscription()));
		subMenuHashtagSubscription.setStyleName("menubar");

		MenuBar subMenuUserSubscription = new MenuBar(true);
		subMenuUserSubscription.addItem("Show",
				command.getCommand(new ShowUserSubscription()));
		subMenuUserSubscription.setStyleName("menubar");

		MenuBar mainMenuSubscription = new MenuBar(true);
		mainMenuSubscription.addItem("Hashtagsubscription",
				subMenuHashtagSubscription);
		mainMenuSubscription.addItem("Usersubscription",
				subMenuUserSubscription);
		mainMenuSubscription.setStyleName("menubar");

		MenuBar menu = new MenuBar();
		menu.addItem("Conversation", mainMenuConversation);
		menu.addItem("Subscription", mainMenuSubscription);
		menu.addItem("ReportGenerator", command.getReportCommand());
		menu.setStyleName("menubar");

		RootPanel.get("Menu").add(menu);
	}

}
