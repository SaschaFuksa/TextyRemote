package hdm.itprojekt.texty.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

//TODO Überlegung: Das Erzeugen des Textboxwidgets geht einher mit der Erstellung einer neuen Conversation 
//TODO Vorschlag: Einheitlichere Benennung der Panels z.B. detailsPanel, um den Ort zu zeigen. Dadurch eventuell leichter verständlich für 
//TODO Gruppenmitglieder aus den anderen Aufgabenbereichen
//TODO hashtagBox in SuggestBox umbauen
/*
 * Zu einer New MessageForm gehört auf jeden Fall immer die Textbox, in der die Messages erscheinen 
 * sowie der Send-Button. 
 */
public class MessageForm extends Showcase{
	
		private VerticalPanel detailsPanel = new VerticalPanel();
		
		private TextArea messageBox = new TextArea();
		private TextBox hashtagBox = new TextBox();
		private Button sendButton = new Button("Send");
	

		protected void run() {
			
			// Größe der TextArea angeben
			messageBox.setCharacterWidth(80);
			messageBox.setVisibleLines(15);
			
			sendButton.addClickHandler(new ClickHandler(){
				 public void onClick(ClickEvent event) {
			    	  Window.alert("Message is sent!");
				      }
				    });
			
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
			
			detailsPanel.add(messageBox);
			detailsPanel.add(hashtagBox);
			detailsPanel.add(sendButton);

			// Verknüpfung mit der html-Struktur
			RootPanel.get("Details").add(detailsPanel);
			
		}
		
		@Override
		protected String getHeadlineText() {
			
			return null;
		}
}
