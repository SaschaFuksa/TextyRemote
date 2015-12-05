package hdm.itprojekt.texty.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ShowPublicConversation extends Showcase {

	// Wird im Details-Bereich realisiert
	private VerticalPanel messagePanel = new VerticalPanel();
	// Wird im Navigator-Bereich realisiert
	private VerticalPanel userPanel = new VerticalPanel();
	private Button testButton = new Button("Show public Conversation");
	
	public void onLoad() {

		testButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Weitere Implementierung erforderlich
				Window.alert("Test!");
			}
		});

		messagePanel.add(testButton);
		RootPanel.get("Details").add(messagePanel);
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
