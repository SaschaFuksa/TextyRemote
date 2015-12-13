package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserSubscriptionForm extends TextyForm {

	public UserSubscriptionForm(String headline) {
		super(headline);
	}

	public UserSubscriptionForm(String headline, Vector<User> selectedUser) {
		super(headline);
		this.selectedUser = selectedUser;
	}

	// private HorizontalPanel suggestBoxPanel = new HorizontalPanel();
	// private HorizontalPanel buttonPanel = new HorizontalPanel();
	private VerticalPanel selectionPanel = new VerticalPanel();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label text = new HTML(
			"To add new user, use the searchfield on the left! <br> "
					+ "To delete subscriptions, click on the delete button next to your subscribed user.");
	private Label errorLabel = new Label("\0");
	private Vector<User> selectedUser = new Vector<User>();
	private Vector<User> subscribedUser = new Vector<User>();

	// private Button addButton = new Button("", new ClickHandler() {
	// public void onClick(ClickEvent event) {
	// // errorLabel.setText("\0");
	// // String username = suggestBox.getText();
	// // boolean alreadySelected = checkUser(username);
	// // if (username == "") {
	// // errorLabel.setText("Please select a user!");
	// // } else if (alreadySelected) {
	// // errorLabel.setText("User is already selected!");
	// // } else {
	// // addUser(username);
	// // }
	// }
	//
	// });

	// private Button subscribeButton = new Button("Subscribe",
	// new ClickHandler() {
	// public void onClick(ClickEvent event) {
	//
	// }
	//
	// });
	//
	// private Button sendMessageButton = new Button("Send Message",
	// new ClickHandler() {
	// public void onClick(ClickEvent event) {
	//
	// }
	//
	// });

	public void addUser(String username) {
		String name = username;
		for (int i = 0; i < subscribedUser.size(); i++) {
			if (name.equals(subscribedUser.get(i).getNickName())) {
				selectedUser.addElement(subscribedUser.get(i));
				subscribedUser.remove(i);
				final HorizontalPanel panel = new HorizontalPanel();
				final Label nameLabel = new Label(username);
				nameLabel.setStylePrimaryName("selectedObjectLabel");
				final Button deleteButton = new Button("", new ClickHandler() {
					public void onClick(ClickEvent event) {
						deleteUser(nameLabel.getText());
						content.remove(panel);
					}

				});
				;
				deleteButton.getElement().setId("deleteButton");
				panel.add(nameLabel);
				panel.add(deleteButton);
				selectionPanel.add(panel);
				content.add(panel);
				// suggestBox.setText("");
				// setOracle();
				return;
			}
		}
		errorLabel.setText("User is unknown!");
	}

	// private KeyUpHandler suggestBoxHandler = new KeyUpHandler() {
	// public void onKeyUp(KeyUpEvent event) {
	// // errorLabel.setText("\0");
	// // if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
	// // errorLabel.setText("\0");
	// // String username = suggestBox.getText();
	// // boolean alreadySelected = checkUser(username);
	// // if (username == "") {
	// // errorLabel.setText("Please select a user!");
	// // } else if (alreadySelected) {
	// // errorLabel.setText("User is already selected!");
	// // } else {
	// // addUser(username);
	// // }
	// // }
	// }
	// };

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

		subscribedUser.addElement(selectedUser.get(indexSelectedUser));
		selectedUser.remove(indexSelectedUser);

	}

	public void showSubscriptions() {
		for (int i = 0; i < subscribedUser.size(); i++) {
			final HorizontalPanel panel = new HorizontalPanel();
			final Label nameLabel = new Label(subscribedUser.get(i)
					.getNickName());
			nameLabel.setStylePrimaryName("selectedObjectLabel");
			final Button deleteButton = new Button("", new ClickHandler() {
				public void onClick(ClickEvent event) {
					deleteUser(nameLabel.getText());
					content.remove(panel);
				}

			});
			;
			deleteButton.getElement().setId("deleteButton");
			panel.add(nameLabel);
			panel.add(deleteButton);
//			selectionPanel.add(panel);
			content.add(panel);
			// suggestBox.setText("");
			// setOracle();
		}
	}

	// private void setOracle() {
	// // oracle.clear();
	// // for (int i = 0; i < allUser.size(); i++) {
	// // String name = new String(allUser.get(i).getNickName());
	// // oracle.add(name);
	// // }
	// }

	protected void run() {

		// suggestBox.addKeyUpHandler(suggestBoxHandler);
		//
		// suggestBox.setText("Search for user");

		User user1 = new User("Matteo", "mama@brbr.de");
		User user2 = new User("Erich", "erer@meme.de");

		user1.setId(4);
		user2.setId(5);

		subscribedUser.add(user1);
		subscribedUser.add(user2);

		showSubscriptions();

		// setOracle();

		// addButton.getElement().setId("addButton");
		// sendMessageButton.getElement().setId("button");
		// subscribeButton.getElement().setId("button");
		errorLabel.setStylePrimaryName("errorLabel");
		// buttonPanel.setStylePrimaryName("buttonLabel");
		scroll.setSize("250px", "110px");

		// suggestBoxPanel.add(suggestBox);
		// suggestBoxPanel.add(addButton);
		// buttonPanel.add(sendMessageButton);
		// buttonPanel.add(subscribeButton);

		this.add(text);
		// this.add(suggestBoxPanel);
		this.add(errorLabel);
		this.add(scroll);
		this.add(selectionPanel);
		// this.add(buttonPanel);

	}

}
