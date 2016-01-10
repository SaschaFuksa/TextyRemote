package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Die InfoBox gibt Warnungen, Errors oder Erfolgsmeldungen aus und kann als
 * Widget in den Formularen eingebunden werden.
 */
public class InfoBox extends VerticalPanel {

	/**
	 * Deklaration, Definition und Initialisierung der Widget.
	 */
	private Label errorLabel = new Label(null);
	private Label infoLabel = new Label(null);
	private Label successLabel = new Label(null);
	private Label warningLabel = new Label(null);

	/**
	 * Setzt bei der Initialisierung die Styles der verschiedenen Labels und
	 * fügt diese der InfoBox hinzu.
	 */
	public InfoBox() {

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		errorLabel.setStylePrimaryName("errorLabel");
		infoLabel.setStylePrimaryName("infoLabel");
		successLabel.setStylePrimaryName("successLabel");
		warningLabel.setStylePrimaryName("warningLabel");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		this.add(errorLabel);
		this.add(infoLabel);
		this.add(successLabel);
		this.add(warningLabel);
	}

	/**
	 * Setzt den Text der Fehlermeldung.
	 * 
	 * @param text
	 */
	public void setErrorText(String text) {
		errorLabel.setText(text);
	}

	/**
	 * Setzt den Text der Erfolgsmeldung.
	 * 
	 * @param text
	 */
	public void setSuccessText(String text) {
		successLabel.setText(text);
	}

	/**
	 * Setzt den Text der Warnung.
	 * 
	 * @param text
	 */
	public void setWarningText(String text) {
		warningLabel.setText(text);
	}

	/**
	 * Setzt den Text der Infomeldung.
	 * 
	 * @param text
	 */
	public void setInfoText(String text) {
		infoLabel.setText(text);
	}

	/**
	 * Leert die Inhalte der Labels in der Infobox.
	 */
	public void clear() {
		errorLabel.setText(null);
		successLabel.setText(null);
		warningLabel.setText(null);
		infoLabel.setText(null);
	}

}
