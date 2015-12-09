package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.client.gui.TextyInstanceControl;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class Showcase extends VerticalPanel {
	
	private TextyInstanceControl instanceControl = new TextyInstanceControl();
	
	public void onLoad() {

		super.onLoad();

		this.checkHeadline();

		this.run();
	}
	
	protected void checkHeadline(){
		if (!instanceControl.isApplicability()) {
			this.add(this.createHeadline(this.getHeadlineText()));
			instanceControl.setApplicability(true);
		}
	}
	
	protected HTML createHeadline(String text) {
		HTML content = new HTML(text);
		content.setStylePrimaryName("texty-headline");
		return content;
	}

	protected abstract String getHeadlineText();

	protected abstract void run();
}
