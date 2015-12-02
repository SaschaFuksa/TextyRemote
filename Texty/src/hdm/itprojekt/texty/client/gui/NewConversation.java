package hdm.itprojekt.texty.client.gui;

import hdm.itprojekt.texty.shared.bo.User;

import java.awt.TextField;
import java.awt.event.KeyListener;
import java.util.Collection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TextArea;

//TODO Bei Senden -und Löschenbutton das Verhalten implementieren
	//Korrekte und geeignete Widgetsuche bzw. implementierung für TextArea und einer Vorschlagssliste 
	//für User
	//Löschen Button erst anbieten, wenn Nachricht bereits gesendet!?

public class NewConversation extends VerticalPanel {
	
	//Wird im Details-Bereich realisiert
	private VerticalPanel messagePanel = new VerticalPanel();
	//Wird im Navigator-Bereich realisiert
	private VerticalPanel userPanel = new VerticalPanel();
	
	//private User user;
	
	//Ein Versuch TextArea zu realisieren, separate Klasse notwendig? Siehe Klasse TextArea und 
	// http://www.gwtproject.org/javadoc/latest/com/google/gwt/user/client/ui/TextArea.html
	//private TextArea messageBox = new TextArea();
	//private TextArea hashtagBox = new TextArea();
	
	//create TextArea element
	private TextArea messageBox = new TextArea();
   
	//create Textbox element
	private TextBox hashtagBox = new TextBox();
	
	//Hier entsteht noch eine Anzeige der Message und der Hashtags 
	private Label messageLabel = new Label("(Hier wird die Nachricht angezeigt)");
	private Label hashtagLabel = new Label("(Hier werden die ausgewaehlten Hashtags angezeigt)");
		
	MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	SuggestBox suggestBox = new SuggestBox(oracle);
	
	
	public void onLoad(){
		
		// Größe der TextArea angeben
		messageBox.setCharacterWidth(80);
		messageBox.setVisibleLines(15);
		
		//Anlegen des Senden Button
		final Button sendButton= new Button("Send");
		final Button deleteButton= new Button("Delete");
		
		sendButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		    	//Weitere Implementierung erforderlich
		        Window.alert("Message is sent!");
		      }
		    });
		
		//Anlegen des Löschen-Button
		deleteButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		    	//Weitere Implementierung erforderlich
		        Window.alert("Message is deleted!");
		      }
		    });
		
		
		messageBox.addKeyUpHandler(new KeyUpHandler() {
			   public void onKeyUp(KeyUpEvent event) {
			    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) 
			     {
			    	Window.alert("Test!");
			     }
			    }
			  });
		
		hashtagBox.addKeyUpHandler(new KeyUpHandler() {
			   public void onKeyUp(KeyUpEvent event) {
			    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) 
			     {
			    	Window.alert("Test!");
			     }
			    }
			  });
		
		
		//Befüllen der Suggestbox mit Inhalt. Am Ende sollen hier die angemeldeten
		//User hinzugefügt werden z.B. mit oracle.addAll(user);
		//Kleines Beispiel:
		oracle.add("Sascha");
		oracle.add("Erich");
		oracle.add("David");
		oracle.add("Matteo");
		oracle.add("Fred");
		oracle.add("Daniel");
		
		
		//Hinzufügen der Widgets zu den jeweiligen Bereichen
		messagePanel.add(messageBox);
		messagePanel.add(hashtagBox);
		messagePanel.add(hashtagLabel);
		messagePanel.add(messageLabel);
		messagePanel.add(sendButton);
		messagePanel.add(deleteButton);
		
		userPanel.add(suggestBox);
		
		//Verknüpfung mit der html-Struktur
		RootPanel.get("Details").add(messagePanel);
		RootPanel.get("Navigator").add(userPanel);
		
	}
}
