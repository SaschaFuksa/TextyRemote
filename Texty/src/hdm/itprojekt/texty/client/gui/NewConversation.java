package hdm.itprojekt.texty.client.gui;

import com.google.gwt.user.client.ui.RootPanel;

import hdm.itprojekt.texty.client.AddUserForm;
import hdm.itprojekt.texty.client.AddMessageForm;
import hdm.itprojekt.texty.client.Showcase;

public class NewConversation extends Showcase {

	private Showcase addUserForm = new AddUserForm();
	private Showcase messageForm = new AddMessageForm();

	/*
	 * // Hier entsteht noch eine Anzeige der Message und der Hashtags private
	 * Label messageLabel = new Label( "(Hier wird die Nachricht angezeigt)");
	 * private Label hashtagLabel = new Label(
	 * "(Hier werden die ausgewaehlten Hashtags angezeigt)");
	 */

	/*
	 * int i = 0;
	 * 
	 * User example = new User(); while(i < listOfUser.size()){ if(suggestBox
	 * .getText().equals(listOfUser.get(i+1).getFirstName())){ example =
	 * listOfUser.get(i+1); } else { i++; } }
	 * deleteButton.setTabIndex(example.getId());
	 */

	public void run() {

		// Dieser Aufruf erzeugt die Suggestbox im Navigator-Bereich.
		RootPanel.get("Navigator").add(addUserForm);
		RootPanel.get("Details").add(messageForm);
		// Dieser Aufruf erzeugt eine neue Message im Navigator-Bereich.

		/*
		 * messagePanel.add(hashtagLabel); messagePanel.add(messageLabel);
		 */
	}

	@Override
	protected String getHeadlineText() {
		// TODO Auto-generated method stub
		return null;
	}

}
