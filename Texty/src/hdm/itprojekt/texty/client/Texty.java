package hdm.itprojekt.texty.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

//import hdm.itprojekt.texty.shared.*;

public class Texty implements EntryPoint {
	
	private VerticalPanel buttonPanel = new VerticalPanel();
	private Button testButton = new Button();
	
	/*private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";*/

	
	/*private final TextyAdministrationAsync textyService = GWT
			.create(TextyAdministration.class);*/

	public void onModuleLoad() {
		
		testButton.getElement().setId("editButton");
		buttonPanel.add(testButton);
		RootPanel.get("testButtonContainer").add(buttonPanel);
	}
}
