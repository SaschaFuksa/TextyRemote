package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * TextyForm bietet für die Formulare in Texty die Möglichkeit, Überschriften
 * festzulegen und erzwingt den Aufruf der run() Operation, welche ermöglicht
 * das Modul im Browser zu laden.
 */
public abstract class TextyForm extends VerticalPanel {

	/**
	 * Deklaration, Definition und Initialisierung der Überschrift.
	 */
	String headline = new String();

	/**
	 * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular.
	 * 
	 * @param headline
	 */
	public TextyForm(String headline) {
		this.headline = headline;
	}

	/**
	 * Diese Methode wird sofort aufgerufen, sobald ein Formular im Browser
	 * eingebaut wird. Dabei greifen die Formulare als Subklassen auf die
	 * Methode run() zurück.
	 */
	@Override
	public void onLoad() {

		super.onLoad();

		this.run();
	}

	/**
	 * Die Überschrift wird in HTML beim Aufruf der getHeadline() Methode im
	 * Formular im gewünschten Parent Widget eingefügt.
	 * 
	 * @return
	 */
	public HTML getHeadline() {
		HTML headline = new HTML(this.headline);
		headline.getElement().setId("texty-headline");
		return headline;
	}

	/**
	 * Erzwingt die implementierung der run() Operation.
	 */
	protected abstract void run();

}