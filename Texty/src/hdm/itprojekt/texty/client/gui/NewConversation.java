package hdm.itprojekt.texty.client.gui;

import hdm.itprojekt.texty.client.AddSelectedUser;
import hdm.itprojekt.texty.client.Showcase;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
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
import com.google.gwt.user.client.ui.TextArea;

//TODO Bei Senden -und Löschenbutton das Verhalten implementieren
//Korrekte und geeignete Widgetsuche bzw. implementierung für TextArea und einer Vorschlagssliste 
//für User
//Löschen Button erst anbieten, wenn Nachricht bereits gesendet!?


public class NewConversation extends Showcase {

	// Wird im Details-Bereich realisiert
	private VerticalPanel messagePanel = new VerticalPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	// Wird im Navigator-Bereich realisiert
	private VerticalPanel navigation = new VerticalPanel();
	private HorizontalPanel addPanel = new HorizontalPanel();
	// create TextArea element
	private TextArea messageBox = new TextArea();
	// create Textbox element
	private TextBox hashtagBox = new TextBox();
	
	// Hier entsteht noch eine Anzeige der Message und der Hashtags
	private Label messageLabel = new Label(
			"(Hier wird die Nachricht angezeigt)");
	private Label hashtagLabel = new Label(
			"(Hier werden die ausgewaehlten Hashtags angezeigt)");
	private TextyHandler suggestHandler = new TextyHandler();
	
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private KeyUpHandler suggestBoxHandler =  new KeyUpHandler() {
	      public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					AddSelectedUser addSelectedUser = new AddSelectedUser();
					addSelectedUser.addUser(suggestBox.getText());
		      }
		    }};

	private Button addButton = new Button("", new ClickHandler() {
	      public void onClick(ClickEvent event) {
	    	  AddSelectedUser addSelectedUser = new AddSelectedUser();
	    	  addSelectedUser.addUser(suggestBox.getText());
		      }
		    });
	private Button sendButton = new Button("Send", new ClickHandler() {
	      public void onClick(ClickEvent event) {
	    	  Window.alert("Message is sent!");
		      }
		    });
	
	public void checkHandler(){
		if (!suggestHandler.isApplicability()){
			suggestBox.addKeyUpHandler(suggestBoxHandler);
			suggestHandler.setApplicability(true);
		}

	}
	
	/* int i = 0;
	 * 
	 * User example = new User(); while(i < listOfUser.size()){
	 * if(suggestBox
	 * .getText().equals(listOfUser.get(i+1).getFirstName())){
	 * example = listOfUser.get(i+1); } else { i++; } }
	 * deleteButton.setTabIndex(example.getId());
	 */
	
	public void run() {
		
		checkHandler();
		
		// Größe der TextArea angeben
		messageBox.setCharacterWidth(80);
		messageBox.setVisibleLines(15);

		// Example Users
		User user1 = new User("Sasa", "sasa@fufu.de");
		User user2 = new User("Daniel", "dada@sese.de");
		User user3 = new User("David", "dada@hehe.de");
		User user4 = new User("Matteo", "mama@brbr.de");
		User user5 = new User("Erich", "erer@meme.de");
		User user6 = new User("Fred", "fredchen@schnuschnu.de");

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

		addButton.getElement().setId("addButton");

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
			String name = new String(listOfUser.get(i).getNickName());
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

	@Override
	protected String getHeadlineText() {
		// TODO Auto-generated method stub
		return null;
	}

	}
