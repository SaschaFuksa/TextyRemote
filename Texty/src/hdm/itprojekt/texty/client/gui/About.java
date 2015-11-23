package hdm.itprojekt.texty.client.gui;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class About extends VerticalPanel{

	private VerticalPanel aboutPanel= new VerticalPanel();
	private HTML aboutHtml = new HTML();
	private String name = "IT-Projekt WS 2015/2016<br>"
			+ "Texty Messagesystem<br>"
			+ "Hochschule der Medien Stuttgart<br>"
			+ "Gruppe 07<br>"
			+ "Sascha Fuksa (27945)<br> Erich Meisner (25307)<br> Daniel Seliger (27955)<br> Matteo Benholz (27987)<br> David Hellebrandt (27980)<br> Friedrich Schneider (27632)<br>";

	// Load
	public void onLoad() {

		aboutPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		aboutHtml.setHTML(name);
		aboutPanel.add(aboutHtml);
		aboutPanel.addStyleName("About");

		RootPanel.get("Details").add(aboutPanel);
}
}
