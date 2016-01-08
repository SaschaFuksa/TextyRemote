package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.Command;
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
				RootPanel.get("Info").clear();
				switch (name) {
				case "Home":
					TextyForm home = new HomeForm("Home");
					RootPanel.get("Navigator").add(home);
					break;
				case "Conversation":
					TextyForm conversation = new ConversationForm(
							"Conversations");
					TextyForm newMessage = new NewMessage("New Conversation");
					RootPanel.get("Navigator").add(conversation);
					RootPanel.get("Info").add(newMessage);
					break;
				case "Subscription":
					TextyForm subscriptionForm = new SubscriptionForm(
							"Subscriptions");
					RootPanel.get("Details").add(subscriptionForm);
					break;
				case "Profile":
					TextyForm profile = new ProfileForm("Profile");
					RootPanel.get("Navigator").add(profile);
					break;
				default:
					;
				}
			}
		};
		return cmd;
	}
}
