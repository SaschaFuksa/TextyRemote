package hdm.itprojekt.texty.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class AddSelectedUser extends Showcase {

	private HorizontalPanel selectedUserPanel = new HorizontalPanel();
	private Label selectedUserLabel = new Label();
	private Button deleteButton = new Button();

	public void addUser(String text) {
		selectedUserLabel.setText(text);
		
		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("Navigator").remove(selectedUserPanel);
			}
		});

		deleteButton.getElement().setId("deleteButton");

		selectedUserPanel.add(selectedUserLabel);
		selectedUserPanel.add(deleteButton);
		RootPanel.get("Navigator").add(selectedUserPanel);
	}

	@Override
	protected String getHeadlineText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void run() {
		// TODO Auto-generated method stub
		
	}

}
