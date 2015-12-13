package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class HomeForm extends TextyForm {
	
	public HomeForm(String headline) {
		super(headline);
	}

	Button bn1 = new Button("TEST");

	protected void run() {
		RootPanel.get("Navigator").add(bn1);
		
	}
	
}
