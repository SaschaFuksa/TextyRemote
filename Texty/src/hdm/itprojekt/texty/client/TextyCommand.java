package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Hier werden die Commands für die Menübar initalisiert und je nach Tab den
 * jeweiligen Modul angeordnet
 * 
 * 
 */
public class TextyCommand {

	public Command getCommand(String moduleName) {
		final String name = moduleName;
		Command cmd = new Command() {
			@Override
			public void execute() {
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				switch (name) {
				case "Home":
					TextyForm home = new HomeForm("Home");
					RootPanel.get("Navigator").add(home);
					break;
				case "Conversation":
					TextyForm conversation = new ConversationForm(
							"Conversations");
					RootPanel.get("Navigator").add(conversation);
					break;
				case "Community":
					TextyForm community = new CommunityForm("Users");
					TextyForm userSubscription = new UserSubscriptionForm(
							"User Subscriptions");
					RootPanel.get("Navigator").add(community);
					RootPanel.get("Details").add(userSubscription);
					break;
				case "Hashtag":
					TextyForm hashtag = new HashtagForm("Hashtags");
					TextyForm hashtagSubscription = new HashtagSubscriptionForm("Hashtag Subscriptions");
					RootPanel.get("Navigator").add(hashtag);
					RootPanel.get("Details").add(hashtagSubscription);
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
}
