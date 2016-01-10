package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * TextyForm bietet f�r die Formulare in Texty die M�glichkeit, �berschriften
 * festzulegen und erzwingt den Aufruf der run() Operation, welche erm�glicht
 * das Modul im Browser zu laden.
 */
public abstract class TextyForm extends VerticalPanel {

	/**
	 * Deklaration, Definition und Initialisierung der �berschrift.
	 */
	String headline = new String();

	/**
	 * Der Konstruktor erzwingt die Eingabe einer �berschrift f�r das Formular.
	 * 
	 * @param headline
	 */
	public TextyForm(String headline) {
		this.headline = headline;
	}

	/**
	 * Diese Methode wird sofort aufgerufen, sobald ein Formular im Browser
	 * eingebaut wird. Dabei greifen die Formulare als Subklassen auf die
	 * Methode run() zur�ck.
	 */
	@Override
	public void onLoad() {

		super.onLoad();

		this.run();
	}

	/**
	 * Die �berschrift wird in HTML beim Aufruf der getHeadline() Methode im
	 * Formular im gew�nschten Parent Widget eingef�gt.
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