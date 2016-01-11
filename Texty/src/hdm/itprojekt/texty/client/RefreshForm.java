package hdm.itprojekt.texty.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RefreshForm extends TextyForm {

	public RefreshForm(String headline) {
		super(headline);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void run() {

		Button refreshButton = new Button("Refresh");
		refreshButton.getElement().setId("button");
		refreshButton.setWidth("100px");

		refreshButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// reloadAll scheint eine Möglichkeit zu sein, bisher noch kein
				// sinnvolles Beispiel gefunden
			}
		});

		VerticalPanel refreshPanel = new VerticalPanel();
		refreshPanel.add(refreshButton);

		RootPanel.get("Info").clear();
		RootPanel.get("Info").add(refreshPanel);

	}

}
