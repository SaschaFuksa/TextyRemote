package hdm.itprojekt.texty.client.texty;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class TextyForm extends VerticalPanel {
	
	public void onLoad() {

		super.onLoad();

		this.run();
	}
	
	public TextyForm(String headline){
		HTML content = new HTML(headline);
		content.setStylePrimaryName("texty-headline");
		this.add(content);
	}
	
	protected abstract void run();

}