package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.client.report.MessagesOfPeriod;
import hdm.itprojekt.texty.client.report.MessagesOfUser;
import hdm.itprojekt.texty.client.report.MessagesOfUserInPeriod;
import hdm.itprojekt.texty.client.report.SubscriptionReport;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Hier werden die Commands f�r die Men�bar initalisiert und je nach Modul den
 * jeweiligen Bereich zugeordnet.
 */
public class TextyCommand {

	/*
	 * Je nach Modul wird ein neuer Command zur�ckgegeben, der die entsprechende
	 * Funktionen in den jeweiligen Men�punkt beinhaltet.
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
					 * der Report diesen DIV Bereich nicht enth�lt.
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
					 * der Report diesen DIV Bereich nicht enth�lt.
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
					 * der Report diesen DIV Bereich nicht enth�lt.
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
					 * der Report diesen DIV Bereich nicht enth�lt.
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
				case "SubscriptionReport":
					/*
					 * Instanziierung des jeweiligen Formulars, welches im
					 * entsprechenden Parent Widget angezeigt wird.
					 */
					TextyForm subscriptionReport = new SubscriptionReport(
							"SubscriptionReport");
					
					/*
					 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
					 */
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
