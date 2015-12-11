package hdm.itprojekt.texty.client.texty;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

public class TextyMenu {

	private TextyCommand command = new TextyCommand();

	public void execute() {
		
		MenuBar menu = new MenuBar();
		menu.addItem("Home",
				command.getCommand(new HomeForm("Home")));
		menu.addItem("Conversation",
				command.getCommand(new ConversationForm("Conversations")));
		menu.addItem("Community",
				command.getCommand(new UserForm("Users")));
		menu.addItem("Hashtag",
				command.getCommand(new HashtagForm("Hashtags")));
		menu.addItem("Profile",
				command.getCommand(new ProfileForm("Profile")));
		menu.addItem("ReportGenerator", command.getReportCommand());
		menu.setStyleName("menubar");

		RootPanel.get("Menu").add(menu);
	}

}

