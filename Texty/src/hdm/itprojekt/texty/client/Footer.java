package hdm.itprojekt.texty.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Footer extends VerticalPanel {

	private VerticalPanel footerPanel = new VerticalPanel();
	private HTML aboutHtml = new HTML();
	private String about = "IT-Projekt WS 2015/2016<br>"
			+ "Texty Messagesystem<br>"
			+ "Hochschule der Medien Stuttgart<br>"
			+ "Gruppe 07<br>"
			+ "Sascha Fuksa (27945)<br> Erich Meisner (25307)<br> Daniel Seliger (27955)<br> Matteo Benholz (27987)<br> David Hellebrandt (27980)<br> Friedrich Schneider (27632)<br>";

	// Load
	@Override
	public void onLoad() {

		footerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		aboutHtml.setHTML(about);
		aboutHtml.addStyleName("Footer");
		footerPanel.add(aboutHtml);
		footerPanel.addStyleName("Footer");

		RootPanel.get("Details").add(footerPanel);
	}
}
