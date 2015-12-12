package hdm.itprojekt.texty.client.texty;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class TextyCommand {

	public Command getCommand(String form) {
		final String test = form;
		Command cmd = new Command() {
			public void execute() {
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				switch (test) {
				case "Home":
					TextyForm home = new HomeForm("Home");
					RootPanel.get("Navigator").add(home);
					break;
				case "Conversation":
					TextyForm conversation = new ConversationForm("Conversations");
					RootPanel.get("Navigator").add(conversation);
					break;
				case "Community":
					TextyForm community = new CommunityForm("Users");
					RootPanel.get("Navigator").add(community);
					break;
				case "Hashtag":
					TextyForm hashtag = new HashtagForm("Hashtags");
					RootPanel.get("Navigator").add(hashtag);
					break;
				case "Profile":
					TextyForm profile = new ProfileForm("Profile");
					RootPanel.get("Navigator").add(profile);
					break;
				case "Report":
					Window.Location
							.assign("http://127.0.0.1:8888/TextyReports.html");
					break;
				default:
					;
				}
			}
		};
		return cmd;
	}

	public Command getReportCommand() {
		Command cmdReport = new Command() {
			public void execute() {
				Window.Location
						.assign("http://127.0.0.1:8888/TextyReports.html");
			}
		};
		return cmdReport;
	}

	public Command getEditorCommand() {
		Command cmdEditor = new Command() {
			public void execute() {
				Window.Location.assign("http://127.0.0.1:8888/Texty.html");
			}
		};
		return cmdEditor;
	}
}
