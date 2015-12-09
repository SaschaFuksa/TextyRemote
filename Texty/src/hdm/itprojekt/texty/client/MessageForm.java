package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.client.gui.TextyInstanceControl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * Diese Klasse definiert ein Modul, welches die Erstellung von Nachrichten
 * ermöglichen wird.
 * 
 *
 */

public class MessageForm extends Showcase {

	private HorizontalPanel selectedHashtag = new HorizontalPanel();
	private VerticalPanel details = new VerticalPanel();

	private TextArea messageBox = new TextArea();
	private TextyInstanceControl instanceControl = new TextyInstanceControl();
	Showcase hashtagForm = new AddHashtagForm();

	private Button sendButton = new Button("Send Message", new ClickHandler() {
		public void onClick(ClickEvent event) {
			;
		}
	});

//	private ScrollPanel scrollPanel = new ScrollPanel(messageBox);
//    private DecoratorPanel decoratorPanel = new DecoratorPanel();

	protected void run() {
		
//		scrollPanel.setSize("400px", "300px");
//		decoratorPanel.add(scrollPanel);
		messageBox.setPixelSize(400, 300);
		
		details.add(messageBox);
		details.add(hashtagForm);
		//details.add(sendButton);
		RootPanel.get("Details").add(details);

	}

	@Override
	protected String getHeadlineText() {

		return "New Message";
	}
}
