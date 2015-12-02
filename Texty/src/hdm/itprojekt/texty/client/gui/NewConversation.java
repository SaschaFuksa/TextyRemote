package hdm.itprojekt.texty.client.gui;

import hdm.itprojekt.texty.shared.bo.User;

import java.awt.TextField;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TextArea;

//TODO Bei Senden -und Löschenbutton das Verhalten implementieren
//Korrekte und geeignete Widgetsuche bzw. implementierung für TextArea und einer Vorschlagssliste 
//für User
//Löschen Button erst anbieten, wenn Nachricht bereits gesendet!?

public class NewConversation extends VerticalPanel {

	// Wird im Details-Bereich realisiert
	private VerticalPanel messagePanel = new VerticalPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	// Wird im Navigator-Bereich realisiert
	private VerticalPanel navigation = new VerticalPanel();
	private HorizontalPanel addPanel = new HorizontalPanel();

	// private User user;

	// Ein Versuch TextArea zu realisieren, separate Klasse notwendig? Siehe
	// Klasse TextArea und
	// http://www.gwtproject.org/javadoc/latest/com/google/gwt/user/client/ui/TextArea.html
	// private TextArea messageBox = new TextArea();
	// private TextArea hashtagBox = new TextArea();

	// create TextArea element
	private TextArea messageBox = new TextArea();

	// create Textbox element
	private TextBox hashtagBox = new TextBox();

	// Hier entsteht noch eine Anzeige der Message und der Hashtags
	private Label messageLabel = new Label(
			"(Hier wird die Nachricht angezeigt)");
	private Label hashtagLabel = new Label(
			"(Hier werden die ausgewaehlten Hashtags angezeigt)");

	MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	SuggestBox suggestBox = new SuggestBox(oracle);

	private Button addButton = new Button();

	public void onLoad() {

		// Größe der TextArea angeben
		messageBox.setCharacterWidth(80);
		messageBox.setVisibleLines(15);

		// Example Users
		User user1 = new User("Sascha", "Fuksa", "sasa@fufu.de");
		User user2 = new User("Daniel", "Söööligöör", "dada@sese.de");
		User user3 = new User("David", "Lightbrandt", "dada@hehe.de");
		User user4 = new User("Matteo", "Brennholz", "mama@brbr.de");
		User user5 = new User("Erich", "Meisser", "erer@meme.de");
		User user6 = new User("Fredchen", "Schneider", "fredchen@schnuschnu.de");

		user1.setId(1);
		user2.setId(2);
		user3.setId(3);
		user4.setId(4);
		user5.setId(5);
		user6.setId(6);

		Vector<User> listOfUser = new Vector<User>();
		listOfUser.add(user1);
		listOfUser.add(user2);
		listOfUser.add(user3);
		listOfUser.add(user4);
		listOfUser.add(user5);
		listOfUser.add(user6);

		// Anlegen des Senden Button
		final Button sendButton = new Button("Send");

		addButton.getElement().setId("addButton");

		sendButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Weitere Implementierung erforderlich
				Window.alert("Message is sent!");
			}
		});

		suggestBox.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					final HorizontalPanel selectedUserPanel = new HorizontalPanel();
					Label selectedUserLabel = new Label(suggestBox.getText());
					Button deleteButton = new Button();
					/*
					 * int i = 0;
					 * 
					 * User example = new User(); while(i < listOfUser.size()){
					 * if
					 * (suggestBox.getText().equals(listOfUser.get(i+1).getFirstName
					 * ())){ example = listOfUser.get(i+1); } else { i++; } }
					 * deleteButton.setTabIndex(example.getId());
					 */

					deleteButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							RootPanel.get("Navigator")
									.remove(selectedUserPanel);
						}
					});

					deleteButton.getElement().setId("deleteButton");

					selectedUserPanel.add(selectedUserLabel);
					selectedUserPanel.add(deleteButton);
					RootPanel.get("Navigator").add(selectedUserPanel);

				}
			}
		});

		// Anlegen des Löschen-Button

		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final HorizontalPanel selectedUserPanel = new HorizontalPanel();
				Label selectedUserLabel = new Label(suggestBox.getText());
				Button deleteButton = new Button();
				/*
				 * int i = 0;
				 * 
				 * User example = new User(); while(i < listOfUser.size()){
				 * if(suggestBox
				 * .getText().equals(listOfUser.get(i+1).getFirstName())){
				 * example = listOfUser.get(i+1); } else { i++; } }
				 * deleteButton.setTabIndex(example.getId());
				 */

				deleteButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						RootPanel.get("Navigator").remove(selectedUserPanel);
					}
				});

				deleteButton.getElement().setId("deleteButton");

				selectedUserPanel.add(selectedUserLabel);
				selectedUserPanel.add(deleteButton);
				RootPanel.get("Navigator").add(selectedUserPanel);
			}
		});

		messageBox.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					Window.alert("Test!");
				}
			}
		});

		hashtagBox.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					Window.alert("Test!");
				}
			}
		});

		// Befüllen der Suggestbox mit Inhalt. Am Ende sollen hier die
		// angemeldeten
		// User hinzugefügt werden z.B. mit oracle.addAll(user);
		// Kleines Beispiel:
		for (int i = 0; i < listOfUser.size(); i++) {
			String name = new String(listOfUser.get(i).getFirstName());
			oracle.add(name);
		}

		// Hinzufügen der Widgets zu den jeweiligen Bereichen
		messagePanel.add(messageBox);
		messagePanel.add(hashtagBox);
		messagePanel.add(hashtagLabel);
		messagePanel.add(messageLabel);
		messagePanel.add(buttonPanel);
		buttonPanel.add(sendButton);

		addPanel.add(suggestBox);
		addPanel.add(addButton);
		navigation.add(addPanel);

		// Verknüpfung mit der html-Struktur
		RootPanel.get("Details").add(messagePanel);
		RootPanel.get("Navigator").add(navigation);

	}
}
