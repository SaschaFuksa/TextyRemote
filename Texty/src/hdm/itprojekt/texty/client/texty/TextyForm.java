package hdm.itprojekt.texty.client.texty;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * TextyForm bietet für die Formulare in Texty die zusätzliche Möglichkeit
 * Überschriften festzulegen um Clone-Codes zu vermeiden.
 * 
 * 
 */
public abstract class TextyForm extends VerticalPanel {

	public void onLoad() {

		super.onLoad();

		this.run();
	}

	public TextyForm(String headline) {
		HTML content = new HTML(headline);
		content.setStylePrimaryName("texty-headline");
		this.add(content);
	}

	protected abstract void run();

}