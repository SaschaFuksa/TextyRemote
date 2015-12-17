package hdm.itprojekt.texty.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * 
 * Hier entsteht die Klasse ReloadForm, die den Reload-Button bereitstellt. 
 * TODO Reload-Funktion implementieren. Window.Location.reload(); lädt die Seite neu
 * und setzt (leider) die eingegebenen Daten zurück. Daher muss ein anderer Ansatz gefunden werden
 *
 */

public class ReloadForm {

	// Hier entsteht der Reload-Button, der auf jeder Seite eingebaut wird, um
	// die tatsächlichen Daten anzuzeigen
	private Button reloadButton = new Button("Refresh");

	public void run() {
		reloadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Window.Location.reload();
			}
		});

		RootPanel.get("Footer").add(reloadButton);
	}

}
