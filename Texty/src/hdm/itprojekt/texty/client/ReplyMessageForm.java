package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.ui.Label;

/**
 * Das ReplyMessageForm Formular setzt erweitert das MessageForm Formular um in
 * bestehendne Unterhaltungen antworten zu können.
 */
public class ReplyMessageForm extends TextyForm {

	/**
	 * Deklaration, Definition und Initialisierung der Widget.
	 */
	MessageForm message = new MessageForm();
	private Label text = new Label(
			"Write down your answer to this conversation!");

	/**
	 * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular.
	 * 
	 * @see TextyForm
	 * @param headline
	 */
	public ReplyMessageForm(String headline) {
		super(headline);
	}

	/**
	 * Modifiziert den Standardkonstruktor, um die run() Operation bei der
	 * Initialisierung aufzurufen.
	 * 
	 * @see TextyForm
	 */
	@Override
	protected void run() {

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		this.getElement().setId("fullWidth");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		this.add(getHeadline());
		this.add(text);
		this.add(message);

	}
}
