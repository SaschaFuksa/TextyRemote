package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.FieldVerifier;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;
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
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MessageForm extends VerticalPanel {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	private FlexTable messageFormFlexTable = new FlexTable();
	private Vector<Hashtag> allHashtag = new Vector<Hashtag>();
	private Vector<Hashtag> selectedHashtag = new Vector<Hashtag>();
	private Vector<User> selectedUser = new Vector<User>();
	private Button addButton = createAddButton();
	Button sendButton = new Button("Send");
	private HorizontalPanel suggestBoxPanel = new HorizontalPanel();
	private HorizontalPanel content = new HorizontalPanel();
	private InfoBox infoBox = new InfoBox();

	private ScrollPanel scroll = new ScrollPanel(content);
	private TextArea textBox = new TextArea();
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public MessageForm() {
		run();
	}

	public void run() {
		administration.getAllHashtags(getAllHashtagsExecute());
	
		suggestBox.addKeyUpHandler(createKeyUpHandler());
		suggestBox.getValueBox().addFocusHandler(createFocusHandler());
	
		suggestBox.setText("Search for hashtags");
	
		this.getElement().setId("messageForm");
		addButton.getElement().setId("addButton");
		sendButton.getElement().setId("button");
		suggestBoxPanel.getElement().setId("fullWidth");
		textBox.getElement().setId("messageFormTextarea");
		messageFormFlexTable.getElement().setId("fullSize");
		scroll.getElement().setId("messageFormScroll");
	
		suggestBoxPanel.add(suggestBox);
		suggestBoxPanel.add(addButton);
	
		messageFormFlexTable.setWidget(0, 0, textBox);
		messageFormFlexTable.setWidget(1, 0, suggestBoxPanel);
		messageFormFlexTable.setWidget(2, 0, scroll);
		messageFormFlexTable.setWidget(3, 0, sendButton);
	
		this.add(messageFormFlexTable);
		this.add(infoBox);
	
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setWidth(textBox.getOffsetWidth() + "px");
			}
		});
	}

	private AsyncCallback<Vector<Hashtag>> getAllHashtagsExecute() {
		AsyncCallback<Vector<Hashtag>> asyncCallback = new AsyncCallback<Vector<Hashtag>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}
	
			@Override
			public void onSuccess(Vector<Hashtag> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				allHashtag = result;
				setOracle();
	
			}
		};
		return asyncCallback;
	}

	private boolean checkHashtag(String keyword) {
		for (Hashtag hashtag : selectedHashtag) {
			if (keyword.equals(hashtag.getKeyword())) {
				return true;
			}
		}
		return false;
	}

	private void createHashtagPanel(String keyword) {
		final HorizontalPanel mainPanel = new HorizontalPanel();
		final Label keywordLabel = new Label("#" + keyword);
		final Label deleteLabel = createDeleteLabel(keyword, mainPanel);

		mainPanel.getElement().setId("hashtagPanel");
		keywordLabel.getElement().setId("selectedHashtagLabel");
		deleteLabel.getElement().setId("removeButton");

		mainPanel.add(keywordLabel);
		mainPanel.add(deleteLabel);
		content.add(mainPanel);

		suggestBox.setText("");

	}

	private void deleteHashtag(String keyword) {
		for (Hashtag hashtag : selectedHashtag) {
			if (keyword.equals(hashtag.getKeyword())) {
				allHashtag.addElement(hashtag);
				selectedHashtag.remove(hashtag);
				break;

			}
		}
		setOracle();
	}

	private Label createDeleteLabel(final String keyword,
			final HorizontalPanel mainPanel) {
		Label label = new Label("x");
		label.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				deleteHashtag(keyword);
				content.remove(mainPanel);
			}
	
		});
		return label;
	}

	private boolean getAvailability(String keyword) {
		for (Hashtag hashtag : allHashtag) {
			if (keyword.equals(hashtag.getKeyword())) {
				selectedHashtag.addElement(hashtag);
				allHashtag.remove(hashtag);
				infoBox.clear();
				infoBox.setSuccessText("Hashtag successful added!");
				setOracle();
				return false;
			}
		}
		return true;
	}

	public void addHashtag(String keyword) {
		if (getAvailability(keyword)) {
			administration.createHashtag(keyword, createHashtagExecute());
			infoBox.clear();
			infoBox.setSuccessText("You subscribed a brand new hashtag!");
		} else {
			
		}
		createHashtagPanel(keyword);
		setOracle();
	
	}
	
	private AsyncCallback<Hashtag> createHashtagExecute(){
		AsyncCallback<Hashtag> asyncCallback = new AsyncCallback<Hashtag>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());

			}

			@Override
			public void onSuccess(Hashtag result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				selectedHashtag.add(result);
			}
		};
		return asyncCallback;
	}

	public void addSelectedUser(User user) {
		this.selectedUser.add(user);
	}

	public void clearSelectedHashtag() {
		selectedHashtag.removeAllElements();
	}

	public void removeInfoBox() {
		this.infoBox.removeFromParent();
	}

	public void removeSelectedUser(User user) {
		this.selectedUser.remove(user);
	}

	public Vector<Hashtag> getHashtag() {
		return selectedHashtag;
	}

	public Vector<User> getSelectedUser() {
		return selectedUser;
	}

	public String getText() {
		return textBox.getText();
	}

	public void setSelectedUser(Vector<User> selectedUser) {
		this.selectedUser = selectedUser;
	}

	public void setSendButtonName(String text) {
		sendButton.setText(text);
	}

	public void setText(String text) {
		textBox.setText(text);
	}
	
	private void setOracle() {
		oracle.clear();
		for (Hashtag hashtag : allHashtag) {
			oracle.add(hashtag.getKeyword());
		}
	}

	private Button createAddButton() {
		Button addButton = new Button("", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				infoBox.clear();
	
				String keyword = suggestBox.getText().trim()
						.replaceAll(" ", "").replaceAll("#", "").replaceAll("[^a-zA-Z0-9]+","");
	
				if (!FieldVerifier.isValidHashtag(keyword)) {
					infoBox.setErrorText("Please add a Hashtag with at least three characters!");
					return;
				}
	
				if (keyword == "" || keyword.equals("Search for hashtags")) {
					infoBox.setErrorText("Please select a hashtag!");
				} else if (checkHashtag(keyword)) {
					infoBox.setWarningText("Hashtag is already selected!");
				} else {
					addHashtag(keyword);
					suggestBox.setText("Search for hashtags");
				}
			}
	
		});
		return addButton;
	}

	private KeyUpHandler createKeyUpHandler() {
		KeyUpHandler keyUpHandler = new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				infoBox.clear();
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					infoBox.clear();
					String keyword = suggestBox.getText().trim()
							.replaceAll(" ", "");
	
					if (!FieldVerifier.isValidHashtag(keyword)) {
						infoBox.setErrorText("Please select a Hashtag with at least three characters!");
						return;
					}
					if (keyword == "" || keyword.equals("Search for hashtags")) {
						infoBox.setErrorText("Please select a hashtag!");
					} else if (checkHashtag(keyword)) {
						infoBox.setWarningText("Hashtag is already selected!");
					} else {
						addHashtag(keyword);
					}
				}
			}
		};
		return keyUpHandler;
	}

	private FocusHandler createFocusHandler() {
		FocusHandler focusHandler = new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				suggestBox.setText("");
				infoBox.clear();
			}
		};
		return focusHandler;
	}

}
