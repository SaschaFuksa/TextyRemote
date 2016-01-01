package hdm.itprojekt.texty.client;

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
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MessageForm extends VerticalPanel {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	private FlexTable messageFormFlexTable = new FlexTable();
	private static Vector<Hashtag> allHashtag = new Vector<Hashtag>();
	private static Vector<Hashtag> selectedHashtag = new Vector<Hashtag>();
	private Button addButton = createAddButton();
	Button sendButton = new Button("Send");
	private HorizontalPanel suggestBoxPanel = new HorizontalPanel();
	private HorizontalPanel content = new HorizontalPanel();
	private InfoBox infoBox = new InfoBox();

	private KeyUpHandler suggestBoxHandler = createKeyUp();
	private ScrollPanel scroll = new ScrollPanel(content);
	private TextArea textBox = new TextArea();
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public MessageForm() {

		administration.getAllHashtags(new AsyncCallback<Vector<Hashtag>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<Hashtag> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				MessageForm.allHashtag = result;
				setOracle();

			}
		});

		suggestBox.addKeyUpHandler(suggestBoxHandler);
		suggestBox.getValueBox().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				suggestBox.setText(null);
				infoBox.clear();

			}
		});

		suggestBox.setText("Search for hashtags");

		addButton.getElement().setId("addButton");
		this.getElement().setId("messageForm");

		suggestBoxPanel.add(suggestBox);
		suggestBoxPanel.add(addButton);

		sendButton.getElement().setId("sendButton");
		textBox.getElement().setId("messageFormTextarea");
		messageFormFlexTable.getElement().setId("fullSize");
		scroll.getElement().setId("messageFormScroll");

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

	public boolean getAvailability(String keyword) {
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

	public void createHashtag(String keyword) {
		administration.createHashtag(keyword, new AsyncCallback<Hashtag>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());

			}

			@Override
			public void onSuccess(Hashtag result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				MessageForm.selectedHashtag.add(result);
			}
		});
		infoBox.clear();
		infoBox.setSuccessText("You subscribed a brand new hashtag!");
	}

	public Label createDeleteLabel(final String keyword,
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

	public void createHashtagPanel(String keyword) {
		final HorizontalPanel mainPanel = new HorizontalPanel();
		final Label keywordLabel = new Label(keyword);
		final Label deleteLabel = createDeleteLabel(keyword, mainPanel);

		mainPanel.getElement().setId("hashtagPanel");
		keywordLabel.getElement().setId("selectedHashtagLabel");
		deleteLabel.getElement().setId("removeButton");

		mainPanel.add(keywordLabel);
		mainPanel.add(deleteLabel);
		content.add(mainPanel);

		suggestBox.setText("");

	}

	public void addHashtag(String keyword) {
		if (getAvailability(keyword)) {
			createHashtag(keyword);
		}

		createHashtagPanel(keyword);
	}

	public boolean checkHashtag(String keyword) {
		for (Hashtag hashtag : selectedHashtag) {
			if (keyword.equals(hashtag.getKeyword())) {
				return true;
			}
		}
		return false;
	}

	public void clearSelectedHashtag() {
		selectedHashtag.removeAllElements();
	}

	private Button createAddButton() {
		Button addButton = new Button("", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				infoBox.clear();
				String keyword = suggestBox.getText().trim().replaceAll(" ", ""); 
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

	private KeyUpHandler createKeyUp() {
		return suggestBoxHandler = new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				infoBox.clear();
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					infoBox.clear();
					String keyword = suggestBox.getText();
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

	public Vector<Hashtag> getHashtag() {
		return selectedHashtag;
	}

	public String getText() {
		return textBox.getText();
	}

	public void setText(String text) {
		textBox.setText(text);
	}

	public void setSendButtonName(String text) {
		sendButton.setText(text);
	}

	public void removeInfoBox() {
		this.infoBox.removeFromParent();
	}

	private void setOracle() {
		oracle.clear();
		for (Hashtag hashtag : allHashtag) {
			oracle.add(hashtag.getKeyword());
		}
	}

}
