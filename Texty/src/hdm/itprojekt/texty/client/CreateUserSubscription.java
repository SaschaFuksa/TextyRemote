package hdm.itprojekt.texty.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CreateUserSubscription extends Showcase {

	// TODO Idee:Testuserdaten -> isElementOf-Überprüfung und damit Createsub.
	// und Createhash. weiter ausbauen

	// Wird im Details-Bereich realisiert
	private VerticalPanel messagePanel = new VerticalPanel();
	// Wird im Navigator-Bereich realisiert
	private VerticalPanel userPanel = new VerticalPanel();
	private TextBox userBox = new TextBox();
	private Label userLabel = new Label("Abonnierte User");
	private Button likeButton = new Button("Like");

	public void run() {

		userBox.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					userLabel.setText(userBox.getText());
				}
			}
		});

		likeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				userLabel.setText(userBox.getText());

			}
		});

		userPanel.add(userBox);
		messagePanel.add(userPanel);
		messagePanel.add(likeButton);
		userPanel.add(userLabel);
		RootPanel.get("Details").add(messagePanel);

	}

	@Override
	protected String getHeadlineText() {
		// TODO Auto-generated method stub
		return null;
	}

}
