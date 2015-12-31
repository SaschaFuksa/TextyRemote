package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * TextyForm bietet für die Formulare in Texty die zusätzliche Möglichkeit
 * Überschriften festzulegen um Clone-Codes zu vermeiden.
 * 
 * 
 */
public abstract class TextyForm extends VerticalPanel {

	String headline = new String();
	
	public TextyForm(String headline) {
		this.headline = headline;
	}

	@Override
	public void onLoad() {

		super.onLoad();

		this.run();
	}
	
	public HTML getHeadline(){
		HTML headline = new HTML(this.headline);
		headline.getElement().setId("texty-headline");
		return headline;
	}

	protected abstract void run();

}