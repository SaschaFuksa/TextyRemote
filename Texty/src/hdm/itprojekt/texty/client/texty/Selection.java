package hdm.itprojekt.texty.client.texty;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class Selection extends HorizontalPanel {
	
	private String name = new String();
	private int id;
	private HorizontalPanel panel = new HorizontalPanel();
	private Label nameLabel = new Label(name);
	private Button deleteButton = new Button("", new ClickHandler() {
		public void onClick(ClickEvent event) {

		}

	});;
	
	public Selection(String name, int id){
		this.name = name;
		this.id = id;
	}
	
	public void onload() {
		deleteButton.getElement().setId("deleteButton");
		panel.add(nameLabel);
		panel.add(deleteButton);
		this.add(panel);
	}
}
