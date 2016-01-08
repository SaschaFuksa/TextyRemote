package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.client.report.MessagesOfPeriod;
import hdm.itprojekt.texty.client.report.MessagesOfUser;
import hdm.itprojekt.texty.client.report.MessagesOfUserInPeriod;
import hdm.itprojekt.texty.client.report.SubscriptionReport;

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
				switch (name) {
				case "Home":
					RootPanel.get("Info").clear();
					TextyForm home = new HomeForm("Home");
					RootPanel.get("Navigator").add(home);
					break;
				case "Conversation":
					RootPanel.get("Info").clear();
					TextyForm conversation = new ConversationForm(
							"Conversations");
					RootPanel.get("Navigator").add(conversation);
					break;
				case "Subscription":
					RootPanel.get("Info").clear();
					TextyForm subscriptionForm = new SubscriptionForm(
							"Subscriptions");
					RootPanel.get("Details").add(subscriptionForm);
					break;
				case "Profile":
					RootPanel.get("Info").clear();
					TextyForm profile = new ProfileForm("Profile");
					RootPanel.get("Navigator").add(profile);
					break;
				case "MessagesOfUser":
					TextyForm messagesOfUser = new MessagesOfUser(
							"MessagesOfUser");
					RootPanel.get("Navigator").add(messagesOfUser);
					break;
				case "MessagesOfPeriod":
					TextyForm messagesOfPeriod = new MessagesOfPeriod(
							"MessagesOfPeriod");
					RootPanel.get("Navigator").add(messagesOfPeriod);
					break;
				case "MessagesOfUserInPeriod":
					TextyForm messagesOfUserInPeriod = new MessagesOfUserInPeriod(
							"MessagesOfUserInPeriod");
					RootPanel.get("Navigator").add(messagesOfUserInPeriod);
					break;
				case "SubscriptionReport":
					TextyForm subscriptionReport = new SubscriptionReport(
							"SubscriptionReport");
					RootPanel.get("Navigator").add(subscriptionReport);
					break;
				default:
					;
				}
			}
		};
		return cmd;
	}
}
