package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.FieldVerifier;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;

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

	private static Vector<Hashtag> allHashtag = new Vector<Hashtag>();
	private Button addButton = createAddButton();
	private Button subscribeButton = createSubscribeButton();
	private FlexTable hashtagFormFlexTable = new FlexTable();
	private InfoBox infoBox = new InfoBox();
	private HorizontalPanel suggestBoxPanel = new HorizontalPanel();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label text = new Label("Subscribe existing hashtags!");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private VerticalPanel mainPanel = new VerticalPanel();
	private Vector<Hashtag> selectedHashtag = new Vector<Hashtag>();
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public HashtagForm(String headline) {
		super(headline);
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
		mainPanel.getElement().setId("hashtagForm");
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

		mainPanel.add(getHeadline());
		mainPanel.add(hashtagFormFlexTable);

		this.add(mainPanel);

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
			}
		});

	}

	private void addHashtag(String keyword) {
		String key = keyword;
		for (Hashtag hashtag : allHashtag) {
			if (key.equals(hashtag.getKeyword())) {
				selectedHashtag.addElement(hashtag);
				allHashtag.remove(hashtag);
				createHashtagPanel(key);
				suggestBox.setText("");
				setOracle();
				return;
			}
		}
		infoBox.setWarningText("Hashtag is unknown!");
	}

	private void createHashtagPanel(String keyword) {
		final HorizontalPanel panel = new HorizontalPanel();
		final Label keywordLabel = new Label("#" + keyword);
		final Label removeButton = new Label("x");
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeHashtag(getKeyword(keywordLabel.getText()));
				content.remove(panel);
			}

		});
		panel.getElement().setId("selectedObjectLabel");
		removeButton.getElement().setId("removeButton");
		panel.add(keywordLabel);
		panel.add(removeButton);
		content.add(panel);
	}

	private boolean checkHashtag(String keyword) {
		String key = keyword;
		for (Hashtag hashtag : selectedHashtag) {
			if (key.equals(hashtag.getKeyword())) {
				return true;
			}
		}
		return false;
	}

	private void removeHashtag(String keyword) {
		String key = keyword;
		for (Hashtag hashtag : selectedHashtag) {
			if (key.equals(hashtag.getKeyword())) {
				allHashtag.addElement(hashtag);
				selectedHashtag.remove(hashtag);
				break;
			}
		}
	}

	private String getKeyword(String keyword) {
		String result = keyword.trim().replaceAll("#", "");
		return result;
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
				HashtagForm.allHashtag = result;
				setOracle();
			}
		};
		return asyncCallback;
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
						.replaceAll(" ", "");
				boolean alreadySelected = checkHashtag(keyword);
				if (!FieldVerifier.isValidHashtag(keyword)) {
					infoBox.setWarningText("Please select a Hashtag with at least three characters!");
				} else if (alreadySelected) {
					infoBox.setWarningText("Hashtag is already selected!");
				} else {
					addHashtag(keyword);
				}
			}

		});
		return addButton;
	}

	private Button createSubscribeButton() {
		Button subscribeButton = new Button("Subscribe", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (selectedHashtag.size() < 1) {
					infoBox.setWarningText("Please select a hashtag!");
				} else {
					TextyForm hashtagSubscription = new HashtagSubscriptionForm(
							"Hashtag Subscriptions", selectedHashtag);
					TextyForm hashtagForm = new HashtagForm("Hashtags");
					RootPanel.get("Details").clear();
					RootPanel.get("Details").add(hashtagSubscription);
					RootPanel.get("Navigator").clear();
					RootPanel.get("Navigator").add(hashtagForm);
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
					boolean alreadySelected = checkHashtag(keyword);
					if (!FieldVerifier.isValidHashtag(keyword)) {
						infoBox.setWarningText("Please select a Hashtag with at least three characters!");
					} else if (alreadySelected) {
						infoBox.setWarningText("Hashtag is already selected!");
					} else {
						addHashtag(keyword);
					}
				}
			}
		};
		return suggestBoxHandler;
	}

}