package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * TextyForm bietet f�r die Formulare in Texty die zus�tzliche M�glichkeit
 * �berschriften festzulegen um Clone-Codes zu vermeiden.
 * 
 * 
 */
public abstract class TextyForm extends VerticalPanel {

	public TextyForm(String headline) {
		HTML content = new HTML(headline);
		content.setStylePrimaryName("texty-headline");
		this.add(content);
	}

	@Override
	public void onLoad() {

		super.onLoad();

		this.run();
	}

	protected abstract void run();

}