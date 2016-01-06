package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.User;

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

public class NewMessage extends TextyForm {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	private Vector<User> allUser = new Vector<User>();
	private Button addButton = createAddButton();
	private FlexTable userFormFlexTable = new FlexTable();
	private HorizontalPanel suggestBoxPanel = new HorizontalPanel();

	private InfoBox infoBox = new InfoBox();
	private HorizontalPanel content = new HorizontalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private MessageForm message = new MessageForm();
	private Label text = new Label("New public message to all user!");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private VerticalPanel mainPanel = new VerticalPanel();
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public NewMessage(String headline) {
		super(headline);
	}

	@Override
	protected void run() {

		administration.getAllUsers(getAllUsersExecute());
		message.sendButton.addClickHandler(createSendHandler());

		suggestBox.addKeyUpHandler(createSuggestBoxHandler());
		suggestBox.getValueBox().addFocusHandler(createFocusHandler());

		suggestBox.setText("Search for users");

		this.getElement().setId("fullSize");
		addButton.getElement().setId("addButton");
		suggestBoxPanel.getElement().setId("fullWidth");
		mainPanel.getElement().setId("subscriptionForm");
		userFormFlexTable.getElement().setId("fullSize");
		scroll.getElement().setId("messageFormScroll");

		suggestBoxPanel.add(suggestBox);
		suggestBoxPanel.add(addButton);

		userFormFlexTable.setWidget(0, 0, text);
		userFormFlexTable.setWidget(1, 0, suggestBoxPanel);
		userFormFlexTable.setWidget(2, 0, scroll);
		userFormFlexTable.setWidget(3, 0, message);
		userFormFlexTable.setWidget(4, 0, infoBox);

		mainPanel.add(getHeadline());
		mainPanel.add(userFormFlexTable);

		this.add(mainPanel);

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setWidth(mainPanel.getOffsetWidth() + "px");
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
				allUser.remove(result);
				setOracle();
			}
		};
		return asyncCallback;
	}

	public void addUser(User selectedUser) {
		for (User user : allUser) {
			if (selectedUser.getId() == user.getId()) {
				if (message.getSelectedUser().size() == 0){
					text.setText("New private message to all selected user!");
				}
				message.addSelectedUser(selectedUser);
				allUser.remove(user);
				createUserPanel(user);
				suggestBox.setText("");
				setOracle();
				return;
			}
		}
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
			}
		});

		userPanel.getElement().setId("selectedObjectLabel");
		removeButton.getElement().setId("removeButton");
		userPanel.add(nameLabel);
		userPanel.add(removeButton);
		content.add(userPanel);
	}

	public boolean checkUser(User user) {
		for (User selectedUser : message.getSelectedUser()) {
			if (user.getId() == selectedUser.getId()) {
				return true;
			}
		}
		return false;
	}

	private void removeUser(User user) {
		allUser.add(user);
		if (message.getSelectedUser().size() == 1){
			text.setText("New public message to all user!");
		}
		message.removeSelectedUser(user);
		setOracle();
	}

	private User getSingleUser(String nickName) {
		for (User user : allUser) {
			if (nickName.equals(setNickName(user.getEmail()))) {
				return user;
			}
		}
		return null;
	}

	private String getNickName(String text) {
		StringBuffer bufferName = new StringBuffer(text);
		int firstIndex = bufferName.indexOf("(");
		bufferName.delete(0, firstIndex + 1);
		bufferName.deleteCharAt(bufferName.length() - 1);
		String nickName = bufferName.toString();
		return nickName;
	}

	private void setOracle() {
		oracle.clear();
		for (User user : allUser) {
			oracle.add(getNickname(user));
		}
	}

	private String setNickName(String email) {
		StringBuffer bufferName = new StringBuffer(email);
		bufferName.setLength(bufferName.indexOf("@"));
		String nickName = bufferName.toString();
		return nickName;
	}

	private String getNickname(User user) {
		StringBuffer bufferName = new StringBuffer(user.getEmail());
		bufferName.setLength(bufferName.indexOf("@"));
		String nickName = bufferName.toString();
		return user.getFirstName() + " (" + nickName + ")";
	}

	private AsyncCallback<Conversation> createConversationExecute() {
		AsyncCallback<Conversation> asyncCallback = new AsyncCallback<Conversation>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Conversation result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				TextyForm conversationForm = new ConversationForm(
						"Conversations", result);
				TextyForm newMessage = new NewMessage(
						"New Conversation");
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				RootPanel.get("Info").clear();
				RootPanel.get("Navigator").add(conversationForm);
				RootPanel.get("Info").add(newMessage);
			}
		};
		return asyncCallback;
	}

	private Button createAddButton() {
		Button addButton = new Button("", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				infoBox.clear();
				if (suggestBox.getText() == "") {
					infoBox.setWarningText("Please select a user!");
				} else {
					String nickname = getNickName(suggestBox.getText());
					User user = getSingleUser(nickname);
					if (user == null) {
						infoBox.setWarningText("User is unknown!");
					} else if (checkUser(user)) {
						infoBox.setWarningText("User is already selected!");
					} else {
						addUser(user);
					}
				}
			}

		});
		return addButton;
	}
	
	private ClickHandler createSendHandler(){
		ClickHandler handler = new ClickHandler() {
			 @Override
			 public void onClick(ClickEvent event) {
			 administration.createConversation(message.getText(), message.getSelectedUser(), message.getHashtag(), createConversationExecute());
			 }
		};
		return handler;		
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
						String nickname = getNickName(suggestBox.getText());
						User user = getSingleUser(nickname);
						if (user == null) {
							infoBox.setWarningText("User is unknown!");
						} else if (checkUser(user)) {
							infoBox.setWarningText("User is already selected!");
						} else {
							addUser(user);
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
