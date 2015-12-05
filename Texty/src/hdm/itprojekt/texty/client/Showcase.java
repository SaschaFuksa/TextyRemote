package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class Showcase extends VerticalPanel {

	public void onLoad() {

		super.onLoad();

		this.add(this.createHeadline(this.getHeadlineText()));

		this.run();
	}

	protected HTML createHeadline(String text) {
		HTML content = new HTML(text);
		content.setStylePrimaryName("texty-headline");
		return content;
	}

	protected abstract String getHeadlineText();

	protected abstract void run();
}
