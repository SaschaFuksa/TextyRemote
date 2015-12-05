package hdm.itprojekt.texty.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.TextArea;

public class ShowPrivateConversation extends Showcase {

	// Wird im Details-Bereich realisiert
	private VerticalPanel messagePanel = new VerticalPanel();

	/*
	 * //Wird im Navigator-Bereich realisiert private VerticalPanel userPanel =
	 * new VerticalPanel();
	 */

	private TextArea messageBox = new TextArea();
	private TextBox hashtagBox = new TextBox();
	private Button deleteButton = new Button("Delete");
	
	public void run() {

		// Größe der TextArea angeben
		messageBox.setCharacterWidth(80);
		messageBox.setVisibleLines(25);

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

		/*
		 * //Test Button um zu schauen, ob Verweise in den Kommandos richtig
		 * sind final Button testButton= new
		 * Button("Show private Conversation");
		 * 
		 * testButton.addClickHandler(new ClickHandler() { public void
		 * onClick(ClickEvent event) { //Weitere Implementierung erforderlich
		 * Window.alert("Test!"); } });
		 */

		messageBox.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					Window.alert("Test!");
				}
			}
		});

		hashtagBox.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					Window.alert("Test!");
				}
			}
		});

		// Hinzufügen der Widgets zu den jeweiligen Bereichen
		messagePanel.add(messageBox);
		messagePanel.add(hashtagBox);
		messagePanel.add(editButton);
		messagePanel.add(deleteButton);

		RootPanel.get("Details").add(messagePanel);

	}

	@Override
	protected String getHeadlineText() {
		// TODO Auto-generated method stub
		return null;
	}
}
