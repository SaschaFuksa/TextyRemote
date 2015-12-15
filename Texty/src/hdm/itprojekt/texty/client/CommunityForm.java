package hdm.itprojekt.texty.client;

import java.util.Vector;

import hdm.itprojekt.texty.shared.bo.User;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CommunityForm extends TextyForm {

	public CommunityForm(String headline) {
		super(headline);
	}

	private HorizontalPanel suggestBoxPanel = new HorizontalPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label text = new Label("Contact or subscribe new users!");
	private Label errorLabel = new Label("\0");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private Vector<User> allUser = new Vector<User>();
	private Vector<User> selectedUser = new Vector<User>();

	private Button addButton = new Button("", new ClickHandler() {
		public void onClick(ClickEvent event) {
			errorLabel.setText("\0");
			String username = suggestBox.getText();
			boolean alreadySelected = checkUser(username);
			if (username == "") {
				errorLabel.setText("Please select a user!");
			} else if (alreadySelected) {
				errorLabel.setText("User is already selected!");
			} else {
				addUser(username);
			}
		}

	});

	private Button subscribeButton = new Button("Subscribe",
			new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (selectedUser.size() < 1) {
						errorLabel.setText("Please select a user!");
					} else {
						TextyForm userSubscription = new UserSubscriptionForm(
								"User Subscriptions", selectedUser);
						RootPanel.get("Details").clear();
						RootPanel.get("Details").add(userSubscription);
					}
				}
			});

	private Button sendMessageButton = new Button("Send Message",
			new ClickHandler() {
				public void onClick(ClickEvent event) {
					TextyForm message = new MessageForm("New Message",
							selectedUser);
					RootPanel.get("Details").clear();
					RootPanel.get("Details").add(message);

				}

			});

	public void addUser(String username) {
		String name = username;
		for (int i = 0; i < allUser.size(); i++) {
			if (name.equals(allUser.get(i).getNickName())) {
				selectedUser.addElement(allUser.get(i));
				allUser.remove(i);
				final HorizontalPanel panel = new HorizontalPanel();
				final Label nameLabel = new Label(username);
				nameLabel.setStylePrimaryName("selectedObjectLabel");
				final Button deleteButton = new Button("", new ClickHandler() {
					public void onClick(ClickEvent event) {
						deleteUser(nameLabel.getText());
						content.remove(panel);
					}

				});
				deleteButton.getElement().setId("deleteButton");
				panel.add(nameLabel);
				panel.add(deleteButton);
				content.add(panel);
				suggestBox.setText("");
				setOracle();
				return;
			}
		}
		errorLabel.setText("User is unknown!");
	}

	private KeyUpHandler suggestBoxHandler = new KeyUpHandler() {
		public void onKeyUp(KeyUpEvent event) {
			errorLabel.setText("\0");
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				errorLabel.setText("\0");
				String username = suggestBox.getText();
				boolean alreadySelected = checkUser(username);
				if (username == "") {
					errorLabel.setText("Please select a user!");
				} else if (alreadySelected) {
					errorLabel.setText("User is already selected!");
				} else {
					addUser(username);
				}
			}
		}
	};

	public boolean checkUser(String username) {
		String name = username;
		for (int i = 0; i < selectedUser.size(); i++) {
			if (name.equals(selectedUser.get(i).getNickName())) {
				return true;
			}
		}
		return false;
	}

	private void deleteUser(String nickname) {
		String name = nickname;
		int indexSelectedUser = 0;
		for (int i = 0; i < selectedUser.size(); i++) {
			if (name.equals(selectedUser.get(i).getNickName())) {
				indexSelectedUser = i;
			}
		}

		allUser.addElement(selectedUser.get(indexSelectedUser));
		selectedUser.remove(indexSelectedUser);

	}

	private void setOracle() {
		oracle.clear();
		for (int i = 0; i < allUser.size(); i++) {
			String name = new String(allUser.get(i).getNickName());
			oracle.add(name);
		}
	}

	@SuppressWarnings("deprecation")
	protected void run() {

		suggestBox.addKeyUpHandler(suggestBoxHandler);
		suggestBox.addFocusListener(new FocusListener() {
			public void onFocus(Widget arg1) {
				suggestBox.setText("");
			}

			public void onLostFocus(Widget arg1) {
				suggestBox.setText("Search for user");
			}
		});

		suggestBox.setText("Search for user");

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

		allUser.add(user1);
		allUser.add(user2);
		allUser.add(user3);
		allUser.add(user4);
		allUser.add(user5);
		allUser.add(user6);

		setOracle();

		addButton.getElement().setId("addButton");
		sendMessageButton.getElement().setId("button");
		subscribeButton.getElement().setId("button");
		errorLabel.setStylePrimaryName("errorLabel");
		buttonPanel.setStylePrimaryName("buttonLabel");
		scroll.setSize("250px", "110px");

		suggestBoxPanel.add(suggestBox);
		suggestBoxPanel.add(addButton);
		buttonPanel.add(sendMessageButton);
		buttonPanel.add(subscribeButton);

		this.add(text);
		this.add(suggestBoxPanel);
		this.add(errorLabel);
		this.add(scroll);
		this.add(buttonPanel);

	}

}
