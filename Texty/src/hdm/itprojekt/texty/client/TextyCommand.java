package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.client.report.MessagesOfPeriod;
import hdm.itprojekt.texty.client.report.MessagesOfUser;
import hdm.itprojekt.texty.client.report.MessagesOfUserInPeriod;
import hdm.itprojekt.texty.client.report.SubscriptionReport;

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
				RootPanel.get("Info").clear();
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
				case "Subscription":
					TextyForm subscriptionForm = new SubscriptionForm(
							"Subscriptions");
					RootPanel.get("Details").add(subscriptionForm);
					break;
				case "Profile":
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
				case "Editor":
					Window.Location.assign("http://127.0.0.1:8888/Texty.html");
					break;
				default:
					;
				}
			}
		};
		return cmd;
	}
}
