package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MessageForm extends VerticalPanel {

	private static Vector<Hashtag> allHashtag = new Vector<Hashtag>();
	private static Vector<Hashtag> selectedHashtag = new Vector<Hashtag>();
	private Button addButton = createAddButton();
	Button sendButton = new Button("Send");
	private HorizontalPanel suggestBoxPanel = new HorizontalPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private HorizontalPanel content = new HorizontalPanel();

	private KeyUpHandler suggestBoxHandler = createKeyUp();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label errorLabel = new Label("\0");
	private Label successLabel = new Label("");
	private Label addedHashtagLabel = new Label("\0");
	private TextArea textBox = new TextArea();
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public MessageForm() {
		administration.getAllHashtags(new AsyncCallback<Vector<Hashtag>>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Vector<Hashtag> result) {
				MessageForm.allHashtag = result;
				setOracle();

			}
		});

		suggestBox.addKeyUpHandler(suggestBoxHandler);
		suggestBox.getValueBox().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				suggestBox.setText("");
				errorLabel.setText("\0");
				successLabel.setText("");

			}
		});

		suggestBox.setText("Search for hashtags");

		addButton.getElement().setId("addButton");
		errorLabel.setStylePrimaryName("errorLabel");
		successLabel.setStylePrimaryName("successLabel");
		sendButton.getElement().setId("sendButton");
		scroll.setSize("400px", "60px");

		suggestBoxPanel.add(suggestBox);
		suggestBoxPanel.add(addButton);
		buttonPanel.add(suggestBoxPanel);
		buttonPanel.add(sendButton);

		this.add(textBox);
		this.add(buttonPanel);
		this.add(errorLabel);
		this.add(successLabel);
		this.add(addedHashtagLabel);
		this.add(scroll);
	}

	public void addHashtag(String keyword) {
		String word = keyword;
		boolean availability = true;
		for (int i = 0; i < allHashtag.size(); i++) {
			if (word.equals(allHashtag.get(i).getKeyword())) {
				selectedHashtag.addElement(allHashtag.get(i));
				allHashtag.remove(i);
				errorLabel.setText("");
				successLabel.setText("Hashtag successful added!");
				setOracle();
				availability = false;
			}
		}
		if (availability) {
			administration.createHashtag(word, new AsyncCallback<Hashtag>() {
				@Override
				public void onFailure(Throwable caught) {

				}

				@Override
				public void onSuccess(Hashtag result) {

					MessageForm.selectedHashtag.add(result);
				}
			});
			availability = true;
			errorLabel.setText("");
			successLabel.setText("You subscribed a brand new hashtag!");
		}

		final HorizontalPanel panel = new HorizontalPanel();
		final Label keywordLabel = new Label(keyword);
		panel.setStylePrimaryName("selectedHashtagLabel");
		final Button deleteButton = new Button("", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				deleteHashtag(keywordLabel.getText());
				content.remove(panel);
			}

		});
		deleteButton.getElement().setId("deleteButton");
		panel.add(keywordLabel);
		panel.add(deleteButton);
		content.add(panel);
		keywordLabel.setStylePrimaryName("hashtagPanel");
		panel.setStylePrimaryName("hashtagPanel");
		suggestBox.setText("");
		if (addedHashtagLabel.getText() == "\0") {
			addedHashtagLabel.setText("Added hashtags:");
		}
		setOracle();

	}

	public boolean checkHashtag(String keyword) {
		String word = keyword;
		for (int i = 0; i < selectedHashtag.size(); i++) {
			if (word.equals(selectedHashtag.get(i).getKeyword())) {
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
				errorLabel.setText("\0");
				String keyword = suggestBox.getText();
				boolean alreadySelected = checkHashtag(keyword);
				if (keyword == "" || keyword.equals("Search for hashtags")) {
					errorLabel.setText("Please select a hashtag!");
				} else if (alreadySelected) {
					errorLabel.setText("Hashtag is already selected!");
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
				errorLabel.setText("\0");
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					errorLabel.setText("\0");
					String keyword = suggestBox.getText();
					boolean alreadySelected = checkHashtag(keyword);
					if (keyword == "" || keyword.equals("Search for hashtags")) {
						errorLabel.setText("Please select a hashtag!");
					} else if (alreadySelected) {
						errorLabel.setText("Hashtag is already selected!");
					} else {
						addHashtag(keyword);
					}
				}
			}
		};
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

		oracle.add(keyword);

		if (selectedHashtag.size() < 1) {
			addedHashtagLabel.setText("\0");
		}
	}

	public Vector<Hashtag> getHashtag() {
		return selectedHashtag;
	}

	public String getText() {
		return textBox.getText();
	}

	private void setOracle() {
		oracle.clear();
		for (int i = 0; i < allHashtag.size(); i++) {
			String keyword = new String(allHashtag.get(i).getKeyword());
			oracle.add(keyword);
		}
	}

}
