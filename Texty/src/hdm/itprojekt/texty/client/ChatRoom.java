package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * Diese Klasse zeigt die abonnierten Inhalte in einer FlexTable an 
 * 
 * TODO Implementation für das Aktualisieren und Löschen einer Nachricht, ScrollPanel hinzufügen, Headline
 * wird noch nicht angezeigt.
 */
public class ChatRoom extends TextyForm {

	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox textBox = new TextBox();
	private Button addMessageButton = new Button("Send");
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Vector<Message> messageList = new Vector<Message>();
	private TextArea area = new TextArea();
	//private HorizontalSplitPanel split = new HorizontalSplitPanel();

	// private TextArea textArea = new TextArea();

	public ChatRoom(String headline) {
		super(headline);
	}

	// Methode, um eine Nachricht der FlexTable hinzuzufügen

	private void showConversation() {

		for (int i = 0; i < messageList.size(); i++) {
			String text = messageList.get(i).getText();
			// Label textLabel = new Label(text);
			int row = chatFlexTable.getRowCount();
			chatFlexTable.setText(row, 0, text);

			// Delete-Button
			Button deleteMessageButton = new Button("x");
			deleteMessageButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

				}
			});

			// Edit-Button
			Button editMessageButton = new Button("Edit");
			editMessageButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					// Implementation für das Aktualisieren einer Nachricht
				}
			});

			chatFlexTable.setWidget(row, 2, deleteMessageButton);
			chatFlexTable.setWidget(row, 4, editMessageButton);
			// VerticalPanel message = new VerticalPanel();
			// message.add(text);
			// mainPanel.add(message);
		}
	}

	private void addMessage() {
		final String textFromTextbox = textBox.getText().trim();
		textBox.setFocus(true);

		if (textFromTextbox.isEmpty()) {
			Window.alert("Please enter a Message!");
			return;
		}

		int row = chatFlexTable.getRowCount();
		chatFlexTable.setText(row, 0, textFromTextbox);

		// Delete-Button
		Button deleteMessageButton = new Button("x");
		deleteMessageButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int removedIndex = messageList.indexOf(textFromTextbox);
				messageList.remove(removedIndex);
				chatFlexTable.removeRow(removedIndex + 1);
			}
		});

		// Edit-Button
		Button editMessageButton = new Button("Edit");
		editMessageButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Implementation für das Aktualisieren einer Nachricht
			}
		});

		// Nachricht hinzufügen

		chatFlexTable.setWidget(row, 2, deleteMessageButton);
		chatFlexTable.setWidget(row, 4, editMessageButton);

	}

	protected void run() {

		chatFlexTable.setText(0, 0, "Chat");
		chatFlexTable.setText(0, 1, "   ");
		chatFlexTable.setText(0, 2, "Delete");
		chatFlexTable.setText(0, 3, "   ");
		chatFlexTable.setText(0, 4, "Edit");

		// Diese Messages symbolisieren bereits abonnierte Messages. Diese
		// werden auf der Startseite Home angezeigt
		Message m1 = new Message("Hallo");
		Message m2 = new Message("Hey, wie gehts dir?");
		Message m3 = new Message("Habe Hunger, #Pizza verbrannt");
		Message m4 = new Message("Oh nein :(");
		Message m5 = new Message("Naja wird schon");
		Message m6 = new Message("Okay, ich muss los, #VfB spielt gleich #naechsteNiederlage");

		messageList.add(m1);
		messageList.add(m2);
		messageList.add(m3);
		messageList.add(m4);
		messageList.add(m5);
		messageList.add(m6);

		addPanel.add(textBox);
		addPanel.add(addMessageButton);

		mainPanel.add(chatFlexTable);
		mainPanel.add(addPanel);

		// Move cursor focus to the input box.
		textBox.setFocus(true);

		// Listen for mouse events on the Add button.
		addMessageButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addMessage();
				textBox.setText("");
			}
		});

		// Listen for keyboard events in the input box.
		textBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					addMessage();
					textBox.setText("");
				}
			}
		});

		showConversation();
		//mainPanel.add(split);
		RootPanel.get("Details").add(mainPanel);

	}

}
