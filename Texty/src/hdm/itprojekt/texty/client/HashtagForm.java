package hdm.itprojekt.texty.client;

import java.util.Vector;

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

import hdm.itprojekt.texty.shared.bo.Hashtag;

public class HashtagForm extends TextyForm {

	public HashtagForm(String headline) {
		super(headline);
	}

	private HorizontalPanel suggestBoxPanel = new HorizontalPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label text = new Label("Subscribe new hashtags!");
	private Label errorLabel = new Label("\0");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private Vector<Hashtag> allHashtag = new Vector<Hashtag>();
	private Vector<Hashtag> selectedHashtag = new Vector<Hashtag>();

	private Button addButton = new Button("", new ClickHandler() {
		public void onClick(ClickEvent event) {
			errorLabel.setText("\0");
			String keyword = suggestBox.getText();
			boolean alreadySelected = checkHashtag(keyword);
			if (keyword == "") {
				errorLabel.setText("Please select a hashtag!");
			} else if (alreadySelected) {
				errorLabel.setText("Hashtag is already selected!");
			} else {
				addUser(keyword);
			}
		}

	});

	private Button subscribeButton = new Button("Subscribe",
			new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (selectedHashtag.size() < 1) {
						errorLabel.setText("Please select a hashtag!");
					} else {
						TextyForm hashtagSubscription = new HashtagSubscriptionForm(
								"Hashtag Subscriptions", selectedHashtag);
						RootPanel.get("Details").clear();
						RootPanel.get("Details").add(hashtagSubscription);
					}
				}
			});

	public void addUser(String keyword) {
		String name = keyword;
		for (int i = 0; i < allHashtag.size(); i++) {
			if (name.equals(allHashtag.get(i).getKeyword())) {
				selectedHashtag.addElement(allHashtag.get(i));
				allHashtag.remove(i);
				final HorizontalPanel panel = new HorizontalPanel();
				final Label keywordLabel = new Label("#" + keyword);
				keywordLabel.setStylePrimaryName("selectedObjectLabel");
				final Button deleteButton = new Button("", new ClickHandler() {
					public void onClick(ClickEvent event) {
						deleteHashtag(keywordLabel.getText());
						content.remove(panel);
					}

				});
				deleteButton.getElement().setId("deleteButton");
				panel.add(keywordLabel);
				panel.add(deleteButton);
				content.add(panel);
				suggestBox.setText("");
				setOracle();
				return;
			}
		}
		errorLabel.setText("Hashtag is unknown!");
	}

	private KeyUpHandler suggestBoxHandler = new KeyUpHandler() {
		public void onKeyUp(KeyUpEvent event) {
			errorLabel.setText("\0");
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				errorLabel.setText("\0");
				String keyword = suggestBox.getText();
				boolean alreadySelected = checkHashtag(keyword);
				if (keyword == "") {
					errorLabel.setText("Please select a hashtag!");
				} else if (alreadySelected) {
					errorLabel.setText("Hashtag is already selected!");
				} else {
					addUser(keyword);
				}
			}
		}
	};

	public boolean checkHashtag(String keyword) {
		String name = keyword;
		for (int i = 0; i < selectedHashtag.size(); i++) {
			if (name.equals(selectedHashtag.get(i).getKeyword())) {
				return true;
			}
		}
		return false;
	}

	private void deleteHashtag(String keyword) {
		String name = keyword;
		int indexSelectedHashtag = 0;
		for (int i = 0; i < selectedHashtag.size(); i++) {
			if (name.equals(selectedHashtag.get(i).getKeyword())) {
				indexSelectedHashtag = i;
			}
		}

		allHashtag.addElement(selectedHashtag.get(indexSelectedHashtag));
		selectedHashtag.remove(indexSelectedHashtag);
	}

	private void setOracle() {
		oracle.clear();
		for (int i = 0; i < allHashtag.size(); i++) {
			String keyword = new String(allHashtag.get(i).getKeyword());
			oracle.add(keyword);
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
				suggestBox.setText("Search for hashtags");
			}
		});

		suggestBox.setText("Search for hashtags");

		Hashtag hashtag1 = new Hashtag("VfB Abstieg");
		Hashtag hashtag2 = new Hashtag("Texty");
		Hashtag hashtag3 = new Hashtag("Pizza");
		Hashtag hashtag4 = new Hashtag("Yee boi");
		Hashtag hashtag5 = new Hashtag("Alkohol");
		Hashtag hashtag6 = new Hashtag("Sprechstunde?");

		hashtag1.setId(1);
		hashtag2.setId(2);
		hashtag3.setId(3);
		hashtag4.setId(4);
		hashtag5.setId(5);
		hashtag6.setId(6);

		allHashtag.add(hashtag1);
		allHashtag.add(hashtag2);
		allHashtag.add(hashtag3);
		allHashtag.add(hashtag4);
		allHashtag.add(hashtag5);
		allHashtag.add(hashtag6);

		setOracle();

		addButton.getElement().setId("addButton");
		subscribeButton.getElement().setId("button");
		errorLabel.setStylePrimaryName("errorLabel");
		buttonPanel.setStylePrimaryName("buttonLabel");
		scroll.setSize("250px", "110px");

		suggestBoxPanel.add(suggestBox);
		suggestBoxPanel.add(addButton);
		buttonPanel.add(subscribeButton);

		this.add(text);
		this.add(suggestBoxPanel);
		this.add(errorLabel);
		this.add(scroll);
		this.add(buttonPanel);

	}

}