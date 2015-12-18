package hdm.itprojekt.texty.client;

import java.util.Vector;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import hdm.itprojekt.texty.shared.bo.Message;

public class Timeline extends TextyForm {

	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Vector<String> messageList = new Vector<String>();

	// Aufbau Inhalt
	private VerticalPanel container = new VerticalPanel();
	private VerticalPanel carrier = new VerticalPanel();
	private Label userLabel = new Label();


	public Timeline(String headline) {
		super(headline);
		// TODO Auto-generated constructor stub
	}

	// Diese Messages symbolisieren bereits abonnierte Messages. Diese //
	// werden auf der Startseite Home angezeigt Message m1 = new
	Message m1 = new Message("Hallo");
	Message m2 = new Message("Hey, wie gehts dir?");
	Message m3 = new Message("Habe Hunger, #Pizza verbrannt");
	Message m4 = new Message("Oh nein :(");
	Message m5 = new Message("Naja wird schon");
	Message m6 = new Message(
			"Okay, ich muss los, #VfB spielt gleich #naechsteNiederlage");

	@Override
	protected void run() {
		

		userLabel.setText("Daniel Seliger");
		
		carrier.add(userLabel);
		
		container.add(carrier);

		chatFlexTable.setText(0, 0, "Favorite");
		chatFlexTable.setText(0, 1, "You");
		chatFlexTable.setText(0, 2, "Delete");
		chatFlexTable.setText(0, 3, "Edit");
		
		chatFlexTable.setWidget(0, 1, container);

		chatFlexTable.setStylePrimaryName("flexTable");
		chatFlexTable.setWidth("32em");
		chatFlexTable.setCellSpacing(7);
		chatFlexTable.setCellPadding(5);

		messageList.add(m1.getText());
		messageList.add(m2.getText());
		messageList.add(m3.getText());
		messageList.add(m4.getText());
		messageList.add(m5.getText());
		messageList.add(m6.getText());

		
		mainPanel.add(chatFlexTable);
	

		int row = chatFlexTable.getRowCount();
		chatFlexTable.setWidget(row, 0, container);

		RootPanel.get("Details").add(mainPanel);

		
		// chatFlexTable.setWidget(row, 2, deleteMessageButton);
		// chatFlexTable.setWidget(row, 3, editMessageButton)
		// chatFlexTable.setText(row, 1, textFromTextbox);
	}
}
