package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.User;
import hdm.itprojekt.texty.shared.bo.UserSubscription;

import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserForm extends TextyForm {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	private Vector<User> allUser = new Vector<User>();
	private Vector<User> allSelectedUser = new Vector<User>();
	private Vector<User> allSubscribedUser = new Vector<User>();
	private Button addButton = createAddButton();
	private Button subscribeButton = createSubscribeButton();
	private FlexTable userFormFlexTable = new FlexTable();
	private HorizontalPanel suggestBoxPanel = new HorizontalPanel();

	private InfoBox infoBox = new InfoBox();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label text = new Label("Subscribe to a new user!");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private VerticalPanel mainPanel = new VerticalPanel();
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public UserForm(String headline, Vector<User> allSubscribedUser) {
		super(headline);
		this.allSubscribedUser = allSubscribedUser;
	}

	@Override
	protected void run() {
	
		administration.getAllUsers(getAllUsersExecute());
	
		suggestBox.addKeyUpHandler(createSuggestBoxHandler());
		suggestBox.getValueBox().addFocusHandler(createFocusHandler());
	
		suggestBox.setText("Search for users");
	
		this.getElement().setId("fullSize");
		addButton.getElement().setId("addButton");
		subscribeButton.getElement().setId("button");
		suggestBoxPanel.getElement().setId("fullWidth");
		mainPanel.getElement().setId("subscriptionForm");
		userFormFlexTable.getElement().setId("fullSize");
		content.getElement().setId("fullWidth");
		scroll.getElement().setId("fullWidth");
	
		suggestBoxPanel.add(suggestBox);
		suggestBoxPanel.add(addButton);
	
		userFormFlexTable.setWidget(0, 0, text);
		userFormFlexTable.setWidget(1, 0, suggestBoxPanel);
		userFormFlexTable.setWidget(2, 0, scroll);
		userFormFlexTable.setWidget(3, 0, subscribeButton);
		userFormFlexTable.setWidget(4, 0, infoBox);
	
		subscribeButton.setVisible(false);
	
		mainPanel.add(getHeadline());
		mainPanel.add(userFormFlexTable);
	
		this.add(mainPanel);
	
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
			}
		});
	}

	private AsyncCallback<Vector<User>> getAllUsersExecute() {
		AsyncCallback<Vector<User>> asyncCallback = new AsyncCallback<Vector<User>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}
	
			@Override
			public void onSuccess(Vector<User> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				allUser = result;
				administration.getCurrentUser(getCurrentUserExecuter());
			}
		};
		return asyncCallback;
	}

	private AsyncCallback<User> getCurrentUserExecuter() {
		AsyncCallback<User> asyncCallback = new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
	
			}
	
			@Override
			public void onSuccess(User result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				setAllUser(result);
				setOracle();
			}
		};
		return asyncCallback;
	}

	private void setAllUser(User currentUser) {
		allUser.remove(currentUser);
		for (User user : allUser) {
			for (User subscribedUser : allSubscribedUser) {
				if (user.getId() == subscribedUser.getId()) {
					allUser.remove(user);
				}
			}
		}
	}

	private void addUser(User selectedUser) {
		for (User user : allUser) {
			if (selectedUser.getId() == user.getId()) {
				allSelectedUser.addElement(selectedUser);
				allUser.remove(user);
				createUserPanel(user);
				suggestBox.setText("");
				setOracle();
				return;
			}
		}
	}

	private void addUserSubscriptions() {
		String result = new String("");
		String warning = new String("");
		for (User user : allSelectedUser) {
			if (checkSubscription(user)) {
				allSubscribedUser.add(user);
				administration.createUserSubscription(user,
						createUserSubscriptionExecute());
				result = result + " '" + user.getFirstName() + "'";
			} else {
				warning = warning + " '" + user.getFirstName() + "'";
			}
		}
		if (result != "") {
			infoBox.setSuccessText("User" + result + " successful subscribed!");
		}
		if (warning != "") {
			infoBox.setWarningText("User" + warning
					+ " was already subscribed!");
		}

	}

	private AsyncCallback<UserSubscription> createUserSubscriptionExecute() {
		AsyncCallback<UserSubscription> asyncCallback = new AsyncCallback<UserSubscription>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}
	
			@Override
			public void onSuccess(UserSubscription result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				TextyForm userSubscription = new SubscriptionForm(
						"Hashtag Subscriptions");
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				RootPanel.get("Details").add(userSubscription);
			}
		};
		return asyncCallback;
	}

	private boolean checkSubscription(User user) {
		for (User subscribedUser : allSubscribedUser) {
			if (user.getId() == subscribedUser.getId()) {
				return false;
			}
		}
		return true;
	}

	private void createUserPanel(User user) {
		final User selectedUser = user;
		final HorizontalPanel userPanel = new HorizontalPanel();
		final Label nameLabel = new Label(selectedUser.getFirstName());
		final Label removeButton = new Label("x");
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeUser(selectedUser);
				content.remove(userPanel);
				if (allSelectedUser.size() == 0) {
					subscribeButton.setVisible(false);
				}
			}
		});

		userPanel.getElement().setId("selectedObjectLabel");
		removeButton.getElement().setId("removeButton");
		userPanel.add(nameLabel);
		userPanel.add(removeButton);
		content.add(userPanel);
	}

	private void removeUser(User user) {
		allUser.add(user);
		allSelectedUser.remove(user);
		setOracle();
	}

	private boolean checkUser(User user) {
		for (User selectedUser : allSelectedUser) {
			if (user.getId() == selectedUser.getId()) {
				return true;
			}
		}
		return false;
	}

	private User getSingleUser(String nickName) {
		for (User user : allUser) {
			if (nickName.equals(setNickName(user.getEmail()))) {
				return user;
			}
		}
		return null;
	}

	private String getNickname(String text) {
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
		return bufferName.toString();
	}

	private void setOracle() {
		oracle.clear();
		for (User user : allUser) {
			String oracleName = user.getFirstName()+ " (" + setNickName(user.getEmail()) + ")";
			oracle.add(oracleName);
		}
	}

	private Button createAddButton() {
		Button addButton = new Button("", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				infoBox.clear();
				if (suggestBox.getText() == "") {
					infoBox.setWarningText("Please select a user!");
				} else {
					String nickname = getNickname(suggestBox.getText());
					User user = getSingleUser(nickname);
					if (user == null) {
						infoBox.setWarningText("User is unknown!");
					} else if (checkUser(user)) {
						infoBox.setWarningText("User is already selected!");
					} else {
						addUser(user);
						if (!subscribeButton.isVisible()) {
							subscribeButton.setVisible(true);
						}
					}
				}
			}
	
		});
		return addButton;
	}

	private Button createSubscribeButton() {
		Button subscribeButton = new Button("Subscribe", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (allSelectedUser.size() == 0) {
					infoBox.setWarningText("Please select a user!");
				} else {
					addUserSubscriptions();
				}
			}
		});
		return subscribeButton;
	}

	private KeyUpHandler createSuggestBoxHandler() {
		KeyUpHandler suggestBoxHandler = new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				infoBox.clear();
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					infoBox.clear();
					if (suggestBox.getText() == "") {
						infoBox.setWarningText("Please select a user!");
					} else {
						String nickname = getNickname(suggestBox.getText());
						User user = getSingleUser(nickname);
						if (user == null) {
							infoBox.setWarningText("User is unknown!");
						} else if (checkUser(user)) {
							infoBox.setWarningText("User is already selected!");
						} else {
							addUser(user);
							if (!subscribeButton.isVisible()) {
								subscribeButton.setVisible(true);
							}
						}
					}
				}
			}
	
		};
		return suggestBoxHandler;
	}

	private FocusHandler createFocusHandler() {
		FocusHandler focusHandler = new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				suggestBox.setText("");
			}
		};
		return focusHandler;
	}

}
