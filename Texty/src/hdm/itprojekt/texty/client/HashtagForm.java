package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.client.gui.TextyInstanceControl;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;

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

public class HashtagForm extends Showcase {
	private HorizontalPanel selectedHashtagPanel = new HorizontalPanel();
	private HorizontalPanel hashtagForm = new HorizontalPanel();
	private VerticalPanel details = new VerticalPanel();
	private Label selectedHashtagLabel = new Label();
	private int selectedNameLabelId;
	private Vector<Hashtag> selectedHashtag = new Vector<Hashtag>();

	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private TextyInstanceControl instanceControl = new TextyInstanceControl();

	private Button deleteButton = new Button();
	private Button addButton = new Button("", new ClickHandler() {
		public void onClick(ClickEvent event) {
			UserForm addSelectedUser = new UserForm();
			addSelectedUser.addUser(suggestBox.getText());
		}
	});

	private KeyUpHandler suggestBoxHandler = new KeyUpHandler() {
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				UserForm addSelectedUser = new UserForm();
				addSelectedUser.addUser(suggestBox.getText());
			}
		}
	};

	public void addUser(String keyword) {
		selectedHashtagLabel.setText(keyword);
		Hashtag hashtag = new Hashtag();
		hashtag.setKeyword(keyword);
		selectedHashtag.add(hashtag);
		selectedNameLabelId = selectedHashtag.indexOf(hashtag);
		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				selectedHashtag.remove(selectedNameLabelId);
				RootPanel.get("Navigator").remove(selectedHashtagPanel);
			}

		});
		System.out.println("nach delete");
		deleteButton.getElement().setId("deleteButton");

		selectedHashtagPanel.add(selectedHashtagLabel);
		selectedHashtagPanel.add(deleteButton);
		RootPanel.get("Navigator").add(selectedHashtagPanel);
	}

	public void checkHandler() {
		if (!instanceControl.isApplicability()) {
			suggestBox.addKeyUpHandler(suggestBoxHandler);
			instanceControl.setApplicability(true);
		}
	}

	protected String getHeadlineText() {
		return "Add User";
	}

	public Vector<Hashtag> getSelectedUser() {
		return selectedHashtag;
	}

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

		for (int i = 0; i < listOfUser.size(); i++) {
			String name = new String(listOfUser.get(i).getNickName());
			oracle.add(name);
		}

		
	}
}
