package hdm.itprojekt.texty.client.gui;

import hdm.itprojekt.texty.client.AddUserForm;
import hdm.itprojekt.texty.client.Showcase;
import hdm.itprojekt.texty.shared.bo.User;
import java.util.Vector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.TextArea;

//TODO Bei Senden -und Löschenbutton das Verhalten implementieren
//Korrekte und geeignete Widgetsuche bzw. implementierung für TextArea und einer Vorschlagssliste 
//für User
//Löschen Button erst anbieten, wenn Nachricht bereits gesendet!?


public class NewConversation extends Showcase {

	// Wird im Details-Bereich realisiert
	private VerticalPanel messagePanel = new VerticalPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();

	// create TextArea element
	private TextArea messageBox = new TextArea();
	// create Textbox element
	private TextBox hashtagBox = new TextBox();
	
	private AddUserForm addUserForm = new AddUserForm();
	
	// Hier entsteht noch eine Anzeige der Message und der Hashtags
	private Label messageLabel = new Label(
			"(Hier wird die Nachricht angezeigt)");
	private Label hashtagLabel = new Label(
			"(Hier werden die ausgewaehlten Hashtags angezeigt)");
	
	private Button sendButton = new Button("Send", new ClickHandler() {
	      public void onClick(ClickEvent event) {
	    	  Window.alert("Message is sent!");
		      }
		    });
	
	
	
	/* int i = 0;
	 * 
	 * User example = new User(); while(i < listOfUser.size()){
	 * if(suggestBox
	 * .getText().equals(listOfUser.get(i+1).getFirstName())){
	 * example = listOfUser.get(i+1); } else { i++; } }
	 * deleteButton.setTabIndex(example.getId());
	 */
	
	public void run() {
		
		addUserForm.onLoad();
		
		// Größe der TextArea angeben
		messageBox.setCharacterWidth(80);
		messageBox.setVisibleLines(15);

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
		messagePanel.add(hashtagLabel);
		messagePanel.add(messageLabel);
		messagePanel.add(buttonPanel);
		buttonPanel.add(sendButton);


		// Verknüpfung mit der html-Struktur
		RootPanel.get("Details").add(messagePanel);

	}

	@Override
	protected String getHeadlineText() {
		// TODO Auto-generated method stub
		return null;
	}

}
