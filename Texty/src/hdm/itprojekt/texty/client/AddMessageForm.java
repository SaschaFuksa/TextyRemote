package hdm.itprojekt.texty.client;

import java.awt.TextField;

import hdm.itprojekt.texty.shared.bo.Message;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

//TODO Überlegung: Das Erzeugen des Textboxwidgets geht einher mit der Erstellung einer neuen Conversation 
//TODO Vorschlag: Einheitlichere Benennung der Panels z.B. detailsPanel, um den Ort zu zeigen. Dadurch eventuell leichter verständlich für 
//TODO Gruppenmitglieder aus den anderen Aufgabenbereichen
//TODO hashtagBox in SuggestBox umbauen
//TODO Simuliertes Chatfenster aufbauen, welches Widget stellt die Chatbox am besten dar? 
/*
 * Zu einer New MessageForm gehört auf jeden Fall die Textbox, in der die Messages erscheinen,
 * sowie der Send-Button. 
 */
public class AddMessageForm extends Showcase{
	
		private VerticalPanel detailsPanel = new VerticalPanel();
		
		private TextArea messageBox = new TextArea();
		private TextBox hashtagBox = new TextBox();
		private Button sendButton = new Button("Send");
		
		
		//Label das die Nachricht trägt
		private Label messageLabel = new Label();
		private Label messageLabel2 = new Label();
		//Panel, das die Labels mit den Messages trägt
		private VerticalPanel chatLabel = new VerticalPanel();
		
		private Message message1 = new Message();
		private Message message2 = new Message();
		private Message message3 = new Message();
		private Message message4 = new Message();
		
		protected void run() {
			
			//Hier entsteht ein Beispielchatfenster.
			message1.setText("Hey du");
			message2.setText("Hi, wie gehts dir?");
			message3.setText("IT-Projekt..");
			message4.setText("So gehts mir auch!");
			
			String s = "Hallo";
			String s2 = "Hey";
			
			messageLabel.setText(s);
			messageLabel2.setText(s2);
			
			chatLabel.add(messageLabel);
			chatLabel.add(messageLabel2);
			
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
			
			detailsPanel.add(chatLabel);
			detailsPanel.add(messageBox);
			detailsPanel.add(hashtagBox);
			detailsPanel.add(sendButton);

			// Verknüpfung mit der html-Struktur
			RootPanel.get("Details").add(detailsPanel);
			
		}
		
		@Override
		protected String getHeadlineText() {
			
			return "New Message";
		}
}
