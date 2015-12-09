package hdm.itprojekt.texty.client.gui;

import com.google.gwt.user.client.ui.RootPanel;

import hdm.itprojekt.texty.client.MessageForm;
import hdm.itprojekt.texty.client.Showcase;
import hdm.itprojekt.texty.client.UserLayer;

/**
 * Über New Conversation werden die Module zum erstellen einer Unterhaltung der
 * GUI geladen.
 * 
 * 
 */
public class NewConversation extends Showcase {

	private Showcase userLayer = new UserLayer();
	private Showcase messageForm = new MessageForm();

	public void run() {

		RootPanel.get("Navigator").add(userLayer);
		RootPanel.get("Details").add(messageForm);

	}

	protected String getHeadlineText() {
		return null;
	}

}
