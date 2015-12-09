package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * Diese Klasse beinhaltet die Widgets zum Anzeigen der abonnierten Inhalte (Hashtags und User). 
 *
 */
public class AddDisplayForm extends Showcase{
	
	//Dieses Widget zeigt den Inhalt des jeweiligen Abonnements 
	private TextArea displayBox = new TextArea();
	private VerticalPanel vertPanel = new VerticalPanel();

	@Override
	protected String getHeadlineText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void run() {
		vertPanel.add(displayBox);
		RootPanel.get("Details").add(vertPanel);
		
	}

}
