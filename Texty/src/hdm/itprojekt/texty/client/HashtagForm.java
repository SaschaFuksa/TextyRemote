package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.FieldVerifier;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.HashtagSubscription;

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

public class HashtagForm extends TextyForm {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	private Vector<Hashtag> allHashtag = new Vector<Hashtag>();
	private Vector<Hashtag> allSelectedHashtag = new Vector<Hashtag>();
	private Vector<Hashtag> allSubscribedHashtag = new Vector<Hashtag>();
	private Button addButton = createAddButton();
	private Button subscribeButton = createSubscribeButton();
	private FlexTable hashtagFormFlexTable = new FlexTable();
	private HorizontalPanel suggestBoxPanel = new HorizontalPanel();

	private InfoBox infoBox = new InfoBox();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label text = new Label("Subscribe new hashtags!");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private VerticalPanel mainPanel = new VerticalPanel();
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public HashtagForm(String headline, Vector<Hashtag> allSubscribedHashtag) {
		super(headline);
		this.allSubscribedHashtag = allSubscribedHashtag;
	}

	private void addHashtag(String keyword) {
		for (Hashtag hashtag : allHashtag) {
			if (keyword.equals(hashtag.getKeyword())) {
				allSelectedHashtag.addElement(hashtag);
				allHashtag.remove(hashtag);
				createHashtagPanel(hashtag);
				suggestBox.setText("");
				setOracle();
				return;
			}
		}
		infoBox.setWarningText("Hashtag is unknown!");
	}

	private void addHashtagSubscriptions() {
		String result = new String("");
		String warning = new String("");
		for (Hashtag hashtag : allSelectedHashtag) {
			if (checkSubscription(hashtag)) {
				allSubscribedHashtag.add(hashtag);
				administration.createHashtagSubscription(hashtag,
						createHashtagSubscriptionExecute());
				result = result + " '" + hashtag.getKeyword() + "'";
			} else {
				warning = warning + " '" + hashtag.getKeyword() + "'";
			}
		}
		if (result != "") {
			infoBox.setSuccessText("Hashtag" + result
					+ " successful subscribed!");
		}
		if (warning != "") {
			infoBox.setWarningText("Hashtag" + warning
					+ " was already subscribed!");
		}

	}

	private boolean checkHashtag(String keyword) {
		for (Hashtag hashtag : allSelectedHashtag) {
			if (keyword.equals(hashtag.getKeyword())) {
				return true;
			}
		}
		return false;
	}

	private boolean checkSubscription(Hashtag hashtag) {
		for (Hashtag subscribedHashtag : allSubscribedHashtag) {
			if (hashtag.getId() == subscribedHashtag.getId()) {
				return false;
			}
		}
		return true;
	}

	private Button createAddButton() {
		Button addButton = new Button("", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				infoBox.clear();
				String keyword = suggestBox.getText().trim()
						.replaceAll(" ", "");
				if (!FieldVerifier.isValidHashtag(keyword)) {
					infoBox.setWarningText("Please select a Hashtag with at least three characters!");
				} else if (checkHashtag(keyword)) {
					infoBox.setWarningText("Hashtag is already selected!");
				} else {
					addHashtag(keyword);
					if (!subscribeButton.isVisible()) {
						subscribeButton.setVisible(true);
					}
				}
			}
	
		});
		return addButton;
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

	private void createHashtagPanel(Hashtag hashtag) {
		final Hashtag selectedHashtag = hashtag;
		final HorizontalPanel hashtagPanel = new HorizontalPanel();
		final Label keywordLabel = new Label("#" + selectedHashtag.getKeyword());
		final Label removeButton = new Label("x");
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeHashtag(selectedHashtag);
				content.remove(hashtagPanel);
				if (allSelectedHashtag.size() == 0) {
					subscribeButton.setVisible(false);
				}
			}
		});
		hashtagPanel.getElement().setId("selectedObjectLabel");
		removeButton.getElement().setId("removeButton");
		hashtagPanel.add(keywordLabel);
		hashtagPanel.add(removeButton);
		content.add(hashtagPanel);
	}

	private AsyncCallback<HashtagSubscription> createHashtagSubscriptionExecute() {
		AsyncCallback<HashtagSubscription> asyncCallback = new AsyncCallback<HashtagSubscription>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}
	
			@Override
			public void onSuccess(HashtagSubscription result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				TextyForm hashtagSubscription = new SubscriptionForm(
						"Hashtag Subscriptions");
				RootPanel.get("Details").clear();
				RootPanel.get("Info").clear();
				RootPanel.get("Details").add(hashtagSubscription);
			}
		};
		return asyncCallback;
	}

	private Button createSubscribeButton() {
		Button subscribeButton = new Button("Subscribe", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (allSelectedHashtag.size() == 0) {
					infoBox.setWarningText("Please select a hashtag!");
				} else {
					addHashtagSubscriptions();
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
					String keyword = suggestBox.getText();
					if (!FieldVerifier.isValidHashtag(keyword)) {
						infoBox.setWarningText("Please select a Hashtag with at least three characters!");
					} else if (checkHashtag(keyword)) {
						infoBox.setWarningText("Hashtag is already selected!");
					} else {
						addHashtag(keyword);
						if (!subscribeButton.isVisible()) {
							subscribeButton.setVisible(true);
						}
					}
				}
			}
		};
		return suggestBoxHandler;
	}

	private AsyncCallback<Vector<Hashtag>> getAllHashtagExecute() {
		AsyncCallback<Vector<Hashtag>> asyncCallback = new AsyncCallback<Vector<Hashtag>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}
	
			@Override
			public void onSuccess(Vector<Hashtag> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				setAllHashtag(result);
				setOracle();
			}
		};
		return asyncCallback;
	}

	private void removeHashtag(Hashtag hashtag) {
		allHashtag.add(hashtag);
		allSelectedHashtag.remove(hashtag);
		setOracle();
	}

	@Override
	protected void run() {
	
		administration.getAllHashtags(getAllHashtagExecute());
	
		suggestBox.addKeyUpHandler(createSuggestBoxHandler());
		suggestBox.getValueBox().addFocusHandler(createFocusHandler());
	
		suggestBox.setText("Search for hashtags");
	
		this.getElement().setId("fullSize");
		addButton.getElement().setId("addButton");
		subscribeButton.getElement().setId("button");
		suggestBoxPanel.getElement().setId("fullWidth");
		mainPanel.getElement().setId("subscriptionForm");
		hashtagFormFlexTable.getElement().setId("fullSize");
		content.getElement().setId("fullWidth");
		scroll.getElement().setId("fullWidth");
	
		suggestBoxPanel.add(suggestBox);
		suggestBoxPanel.add(addButton);
	
		hashtagFormFlexTable.setWidget(0, 0, text);
		hashtagFormFlexTable.setWidget(1, 0, suggestBoxPanel);
		hashtagFormFlexTable.setWidget(2, 0, scroll);
		hashtagFormFlexTable.setWidget(3, 0, subscribeButton);
		hashtagFormFlexTable.setWidget(4, 0, infoBox);
	
		subscribeButton.setVisible(false);
	
		mainPanel.add(getHeadline());
		mainPanel.add(hashtagFormFlexTable);
	
		this.add(mainPanel);
	
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
			}
		});
	
	}

	private void setAllHashtag(Vector<Hashtag> allHashtag) {
		for (Hashtag hashtag : allHashtag) {
			for (Hashtag subscribedHashtag : allSubscribedHashtag) {
				if (hashtag.getId() == subscribedHashtag.getId()) {
					allHashtag.remove(hashtag);
				}
			}
		}
		this.allHashtag = allHashtag;
	}

	private void setOracle() {
		oracle.clear();
		for (Hashtag hashtag : allHashtag) {
			oracle.add(hashtag.getKeyword());
		}
	}

}