package hdm.itprojekt.texty.client;

import java.util.Vector;

import hdm.itprojekt.texty.client.gui.TextyHandler;
import hdm.itprojekt.texty.shared.bo.User;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddUserForm extends Showcase {

	private HorizontalPanel selectedUserPanel = new HorizontalPanel();
	private Label selectedUserLabel = new Label();
	private Button deleteButton = new Button();

	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private TextyHandler suggestHandler = new TextyHandler();

	private VerticalPanel navigation = new VerticalPanel();
	private HorizontalPanel addPanel = new HorizontalPanel();

	public void addUser(String text) {
		selectedUserLabel.setText(text);

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

	private KeyUpHandler suggestBoxHandler = new KeyUpHandler() {
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				AddUserForm addSelectedUser = new AddUserForm();
				addSelectedUser.addUser(suggestBox.getText());
			}
		}
	};

	private Button addButton = new Button("", new ClickHandler() {
		public void onClick(ClickEvent event) {
			AddUserForm addSelectedUser = new AddUserForm();
			addSelectedUser.addUser(suggestBox.getText());
		}
	});

	public void checkHandler() {
		if (!suggestHandler.isApplicability()) {
			suggestBox.addKeyUpHandler(suggestBoxHandler);
			suggestHandler.setApplicability(true);
		}

	}

	@Override
	protected String getHeadlineText() {
		// TODO Auto-generated method stub
		return null;
	}

	// Wird automatisch bei der Objekterzeugung ausgeführt
	protected void run() {

		checkHandler();

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

		// Befüllen der Suggestbox mit Inhalt. Am Ende sollen hier die
		// angemeldeten
		// User hinzugefügt werden z.B. mit oracle.addAll(user);
		// Kleines Beispiel:
		for (int i = 0; i < listOfUser.size(); i++) {
			String name = new String(listOfUser.get(i).getNickName());
			oracle.add(name);
		}

		addPanel.add(suggestBox);
		addPanel.add(addButton);
		navigation.add(addPanel);
		RootPanel.get("Navigator").add(navigation);
	}
}
