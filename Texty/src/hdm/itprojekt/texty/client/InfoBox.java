package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InfoBox extends VerticalPanel {

	private Label errorLabel = new Label(null);
	private Label infoLabel = new Label(null);
	private Label successLabel = new Label(null);
	private Label warningLabel = new Label(null);

	public InfoBox() {

		errorLabel.setStylePrimaryName("errorLabel");
		infoLabel.setStylePrimaryName("infoLabel");
		successLabel.setStylePrimaryName("successLabel");
		warningLabel.setStylePrimaryName("warningLabel");

		this.add(errorLabel);
		this.add(infoLabel);
		this.add(successLabel);
		this.add(warningLabel);
	}

	public void setErrorText(String text) {
		errorLabel.setText(text);
	}

	public void setSuccessText(String text) {
		successLabel.setText(text);
	}

	public void setWarningText(String text) {
		warningLabel.setText(text);
	}
	
	public void setInfoText(String text) {
		infoLabel.setText(text);
	}

	public void clear() {
		errorLabel.setText(null);
		successLabel.setText(null);
		warningLabel.setText(null);
		infoLabel.setText(null);
	}

}
