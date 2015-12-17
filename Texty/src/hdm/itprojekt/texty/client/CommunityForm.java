package hdm.itprojekt.texty.client;

import java.util.Vector;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.User;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
	private static Vector<User> allUser = new Vector<User>();
	private Vector<User> selectedUser = new Vector<User>();
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	private Button addButton = new Button("", new ClickHandler() {
		public void onClick(ClickEvent event) {
			errorLabel.setText("\0");
			if (suggestBox.getText() == "") {
				errorLabel.setText("Please select a user!");
			} else {
				String nickName = getNickName(suggestBox.getText());
				User user = getUserOutOfAllUser(nickName);
				if (user == null) {
					errorLabel.setText("User is unknown!");
				} else {
					boolean alreadySelected = checkUser(user);
					if (alreadySelected) {
						errorLabel.setText("User is already selected!");
					} else {
						addUser(user);
					}
				}
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

	public void addUser(User user) {
		for (int i = 0; i < allUser.size(); i++) {
			if (user.equals(allUser.get(i))) {
				selectedUser.addElement(user);
				allUser.remove(i);
				final HorizontalPanel panel = new HorizontalPanel();
				final Label nameLabel = new Label(user.getFirstName());
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
	}

	private KeyUpHandler suggestBoxHandler = new KeyUpHandler() {
		public void onKeyUp(KeyUpEvent event) {
			errorLabel.setText("\0");
			if (suggestBox.getText() == "") {
				errorLabel.setText("Please select a user!");
			} else {
				String nickName = getNickName(suggestBox.getText());
				User user = getUserOutOfAllUser(nickName);
				if (user == null) {
					errorLabel.setText("User is unknown!");
				} else {
					boolean alreadySelected = checkUser(user);
					if (alreadySelected) {
						errorLabel.setText("User is already selected!");
					} else {
						addUser(user);
					}
				}
			}
		}
	};

	public boolean checkUser(User user) {
		for (int i = 0; i < selectedUser.size(); i++) {
			if (user.equals(selectedUser.get(i))) {
				return true;
			}
		}
		return false;
	}

	public User getUserOutOfAllUser(String nickName) {
		for (int i = 0; i < allUser.size(); i++) {
			if (nickName.equals(setNickName(allUser.get(i).getEmail()))) {
				User user = allUser.get(i);
				return user;
			}
		}
		return null;
	}

	private void deleteUser(String firstname) {
		String name = firstname;
		int indexSelectedUser = 0;
		for (int i = 0; i < selectedUser.size(); i++) {
			if (name.equals(selectedUser.get(i).getFirstName())) {
				indexSelectedUser = i;
			}
		}

		allUser.addElement(selectedUser.get(indexSelectedUser));
		selectedUser.remove(indexSelectedUser);

	}

	private void setOracle() {
		oracle.clear();
		for (int i = 0; i < allUser.size(); i++) {
			oracle.add(getOracleName(allUser.get(i).getFirstName(), allUser
					.get(i).getEmail()));
		}
	}

	private String getOracleName(String firstName, String email) {
		StringBuffer bufferName = new StringBuffer(email);
		bufferName.setLength(bufferName.indexOf("@"));
		String nickName = bufferName.toString();
		String name = new String(firstName + " (" + nickName + ")");
		return name;

	}

	private String getNickName(String text) {
		StringBuffer bufferName = new StringBuffer(text);
		int firstIndex = bufferName.indexOf("(");
		bufferName.delete(0, firstIndex + 1);
		bufferName.deleteCharAt(bufferName.length() - 1);
		String nickName = bufferName.toString();
		return nickName;
	}

	private String setNickName(String email) {
		StringBuffer bufferName = new StringBuffer(email);
		bufferName.setLength(bufferName.indexOf("@"));
		String nickName = bufferName.toString();
		return nickName;
	}

	@SuppressWarnings("deprecation")
	protected void run() {

		suggestBox.addKeyUpHandler(suggestBoxHandler);
		suggestBox.addFocusListener(new FocusListener() {
			public void onFocus(Widget arg1) {
				suggestBox.setText("");
			}

			public void onLostFocus(Widget arg1) {

			}
		});

		suggestBox.setText("Search for user");

		administration.getAllUsers(new AsyncCallback<Vector<User>>() {
			public void onFailure(Throwable caught) {

			}

			public void onSuccess(Vector<User> result) {
				CommunityForm.allUser = result;
				setOracle();

			}
		});

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
