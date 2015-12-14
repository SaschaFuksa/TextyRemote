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
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
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
 * TODO Implementation für das Aktualisieren und Löschen einer Nachricht,
 * ScrollPanel hinzufügen, Headline wird noch nicht angezeigt. Hashtagbox zum
 * Hinzufügen
 */
public class ChatRoom extends TextyForm {

	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox textBox = new TextBox();
	private Button addMessageButton = new Button("Send");
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Vector<String> messageList = new Vector<String>();
	private TextArea area = new TextArea();

	public ChatRoom(String headline) {
		super(headline);
	}

	protected void run() {

		// FlexCellFormatter cellFormatter =
		// chatFlexTable.getFlexCellFormatter();

		chatFlexTable.setText(0, 0, "Receiver");
		chatFlexTable.setText(0, 1, "You");
		chatFlexTable.setText(0, 2, "Delete");
		chatFlexTable.setText(0, 3, "Edit");
		// chatFlexTable.setText(0, 4, "Test");

		chatFlexTable.setStylePrimaryName("flexTable");
		chatFlexTable.setWidth("32em");
		chatFlexTable.setCellSpacing(7);
		chatFlexTable.setCellPadding(5);

		// Diese Messages symbolisieren bereits abonnierte Messages. Diese
		// werden auf der Startseite Home angezeigt
		Message m1 = new Message("Hallo");
		Message m2 = new Message("Hey, wie gehts dir?");
		Message m3 = new Message("Habe Hunger, #Pizza verbrannt");
		Message m4 = new Message("Oh nein :(");
		Message m5 = new Message("Naja wird schon");
		Message m6 = new Message(
				"Okay, ich muss los, #VfB spielt gleich #naechsteNiederlage");

		messageList.add(m1.getText());
		messageList.add(m2.getText());
		messageList.add(m3.getText());
		messageList.add(m4.getText());
		messageList.add(m5.getText());
		messageList.add(m6.getText());

		addPanel.add(textBox);
		addPanel.add(addMessageButton);
		mainPanel.add(chatFlexTable);
		mainPanel.add(addPanel);

		textBox.setFocus(true);

		addMessageButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addNewMessage();
				textBox.setText("");
			}
		});

		// Listen for keyboard events in the input box.
		textBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					addNewMessage();
					textBox.setText("");
				}
			}
		});

		showConversation();
		RootPanel.get("Details").add(mainPanel);

	}
	
	private void showConversation() {
		for (int i = 0; i < messageList.size(); i++) {
			addExistingMessage(messageList.get(i));
		} 
	}

	private void addExistingMessage(String sText) {
		final String text = sText;
		int row = chatFlexTable.getRowCount();

		chatFlexTable.setText(row, 0, text);
		//chatFlexTable.setText(row, 1, " ");

		// Delete
		Button deleteMessageButton = new Button("x");
		deleteMessageButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Window.alert("Test");
				int removedIndex = messageList.indexOf(text);
				messageList.remove(removedIndex);
				chatFlexTable.removeRow(removedIndex + 1);
			}
		});
		
		//Test
		Label l1 = new Label();
		l1.setText("hallo");
		l1.setTitle("Peter");
		
		// chatFlexTable.setWidget(row, 1, l1);
		chatFlexTable.setWidget(row, 2, deleteMessageButton);
		//chatFlexTable.setWidget(row, 3, editMessageButton);

	}

	// Hier schreibt der Autor selber eine neue Nachricht
	private void addNewMessage() {
		final String textFromTextbox = textBox.getText().trim();
		textBox.setFocus(true);

		if (textFromTextbox.isEmpty()) {
			Window.alert("Please enter a Message!");
			return;
		}

		messageList.add(textFromTextbox);
		int row = chatFlexTable.getRowCount();
		
		// Unterschied zu der Spalte Receiver: Die Nachricht erscheint in der
		// zweiten Spalte
		chatFlexTable.setText(row, 1, textFromTextbox);

		// Delete-Button
		Button deleteMessageButton = new Button("x");
		deleteMessageButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Window.alert(textFromTextbox);

				int removedIndex = messageList.indexOf(textFromTextbox);

				// Integer removedIndex2 = new Integer(removedIndex);
				// String s = removedIndex2.toString();
				// Window.alert(s);
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

		chatFlexTable.setWidget(row, 2, deleteMessageButton);
		chatFlexTable.setWidget(row, 3, editMessageButton);

		if (textFromTextbox.isEmpty()) {
			Window.alert("Please enter a Message!");
			return;
		}

	}

}
