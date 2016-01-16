package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.client.report.HashtagSubscriptionReport;
import hdm.itprojekt.texty.client.report.MessagesOfHashtag;
import hdm.itprojekt.texty.client.report.MessagesOfPeriod;
import hdm.itprojekt.texty.client.report.MessagesOfUser;
import hdm.itprojekt.texty.client.report.MessagesOfUserInPeriod;
import hdm.itprojekt.texty.client.report.UserSubscriptionReport;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Hier werden die Commands für die Menübar initalisiert und je nach Modul den
 * jeweiligen Bereich zugeordnet.
 */
public class TextyCommand {

	/*
	 * Je nach Modul wird ein neuer Command zurückgegeben, der die entsprechende
	 * Funktionen in den jeweiligen Menüpunkt beinhaltet.
	 */
	public Command getCommand(final String moduleName) {

		/*
		 * Initialisierung des Command.
		 */
		Command cmd = new Command() {
			@Override
			public void execute() {

				/*
				 * Entfernung der Child Widgets vom jeweiligen Parent Widget.
				 */
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();

				switch (moduleName) {
				case "Home":
					/*
					 * Entfernung der Child Widgets vom Parent Widget Info, da
					 * der Report diesen DIV Bereich nicht enthält.
					 */
					RootPanel.get("Info").clear();

					/*
					 * Instanziierung des jeweiligen Formulars, welches im
					 * entsprechenden Parent Widget angezeigt wird.
					 */
					TextyForm home = new HomeForm("Home");
					
					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
					RootPanel.get("Navigator").add(home);
					break;
				case "Conversation":
					/*
					 * Entfernung der Child Widgets vom Parent Widget Info, da
					 * der Report diesen DIV Bereich nicht enthält.
					 */
					RootPanel.get("Info").clear();
					
					/*
					 * Instanziierung des jeweiligen Formulars, welches im
					 * entsprechenden Parent Widget angezeigt wird.
					 */
					TextyForm conversation = new ConversationForm(
							"Conversations");
					
					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
					RootPanel.get("Navigator").add(conversation);
					break;
				case "Subscription":
					/*
					 * Entfernung der Child Widgets vom Parent Widget Info, da
					 * der Report diesen DIV Bereich nicht enthält.
					 */
					RootPanel.get("Info").clear();
					
					/*
					 * Instanziierung des jeweiligen Formulars, welches im
					 * entsprechenden Parent Widget angezeigt wird.
					 */
					TextyForm subscriptionForm = new SubscriptionForm(
							"Subscriptions");
					
					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
					RootPanel.get("Details").add(subscriptionForm);
					break;
				case "Profile":
					/*
					 * Entfernung der Child Widgets vom Parent Widget Info, da
					 * der Report diesen DIV Bereich nicht enthält.
					 */
					RootPanel.get("Info").clear();
					
					/*
					 * Instanziierung des jeweiligen Formulars, welches im
					 * entsprechenden Parent Widget angezeigt wird.
					 */
					TextyForm profile = new ProfileForm("Profile");
					
					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
					RootPanel.get("Navigator").add(profile);
					break;
				case "MessagesOfHashtag":
					/*
					 * Instanziierung des jeweiligen Formulars, welches im
					 * entsprechenden Parent Widget angezeigt wird.
					 */
					TextyForm messagesOfHashtag = new MessagesOfHashtag(
							"MessagesOfHashtag");
					
					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
					RootPanel.get("Navigator").add(messagesOfHashtag);
					break;
				case "MessagesOfUser":
					/*
					 * Instanziierung des jeweiligen Formulars, welches im
					 * entsprechenden Parent Widget angezeigt wird.
					 */
					TextyForm messagesOfUser = new MessagesOfUser(
							"MessagesOfUser");
					
					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
					RootPanel.get("Navigator").add(messagesOfUser);
					break;
				case "MessagesOfPeriod":
					/*
					 * Instanziierung des jeweiligen Formulars, welches im
					 * entsprechenden Parent Widget angezeigt wird.
					 */
					TextyForm messagesOfPeriod = new MessagesOfPeriod(
							"MessagesOfPeriod");
					
					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
					RootPanel.get("Navigator").add(messagesOfPeriod);
					break;
				case "MessagesOfUserInPeriod":
					/*
					 * Instanziierung des jeweiligen Formulars, welches im
					 * entsprechenden Parent Widget angezeigt wird.
					 */
					TextyForm messagesOfUserInPeriod = new MessagesOfUserInPeriod(
							"MessagesOfUserInPeriod");
					
					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
					RootPanel.get("Navigator").add(messagesOfUserInPeriod);
					break;
				case "UserSubscriptionReport":
					/*
					 * Instanziierung des jeweiligen Formulars, welches im
					 * entsprechenden Parent Widget angezeigt wird.
					 */
					TextyForm userSubscriptionReport = new UserSubscriptionReport(
							"SubscriptionReport");
					
					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
					RootPanel.get("Navigator").add(userSubscriptionReport);
					break;
				case "HashtagSubscriptionReport":
					/*
					 * Instanziierung des jeweiligen Formulars, welches im
					 * entsprechenden Parent Widget angezeigt wird.
					 */
					TextyForm hashtagSubscriptionReport = new HashtagSubscriptionReport(
							"SubscriptionReport");
					
					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
					RootPanel.get("Navigator").add(hashtagSubscriptionReport);
					break;
				default:
					;
				}
			}
		};
		return cmd;
	}
}
