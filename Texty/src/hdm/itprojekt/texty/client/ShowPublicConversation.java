package hdm.itprojekt.texty.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;

//TODO User lassen sich im Navigatorbereich gut in einer CellList darstellen

public class ShowPublicConversation extends Showcase {

	private MessageForm messageForm = new MessageForm();

	public void onLoad() {

		messageForm.onLoad();

		// Anlegen des Bearbeiten Button
		final Button editButton = new Button("Edit");

		editButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Weitere Implementierung erforderlich
				Window.alert("Test");
			}
		});

		editButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Weitere Implementierung erforderlich
				Window.alert("Test");
			}
		});
	}

	@Override
	protected String getHeadlineText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void run() {
		// TODO Auto-generated method stub

	}
}
