package hdm.itprojekt.texty.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ShowPublicConversation {
	
		//Wird im Details-Bereich realisiert
		private VerticalPanel messagePanel = new VerticalPanel();
		//Wird im Navigator-Bereich realisiert
		private VerticalPanel userPanel = new VerticalPanel();

	public void onLoad(){
		
				//Test Button um zu schauen, ob Verweise in den Kommandos richtig sind
				final Button testButton= new Button("Show public Conversation");
				
				testButton.addClickHandler(new ClickHandler() {
				      public void onClick(ClickEvent event) {
				    	//Weitere Implementierung erforderlich
				        Window.alert("Test!");
				      }
				    });
				
				messagePanel.add(testButton);
				RootPanel.get("Details").add(messagePanel);
		
	}
}
