package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.client.TextyForm;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Hier werden die Commands für die Menübar initalisiert und je nach Tab den
 * jeweiligen Modul angeordnet
 * 
 * 
 */
public class TextyReportCommand {

	public Command getCommand(String moduleName) {
		final String name = moduleName;
		Command cmd = new Command() {
			@Override
			public void execute() {
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				switch (name) {
				case "MessagesOfUser":
					TextyForm messagesOfUser = new MessageReport(
							"MessagesOfUser");
					RootPanel.get("Navigator").add(messagesOfUser);
					break;
				case "MMessagesOfPeriod":
					TextyForm messagesOfPeriod = new MessageReport(
							"MessagesOfPeriod");
					RootPanel.get("Navigator").add(messagesOfPeriod);
					break;
				case "MessagesOfUserInPeriod":
					TextyForm messagesOfUserInPeriod = new MessageReport(
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
