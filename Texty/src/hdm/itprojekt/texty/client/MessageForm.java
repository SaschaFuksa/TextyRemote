package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public class MessageForm extends TextyForm {

	public MessageForm(String headline) {
		super(headline);
	}

	public MessageForm(String headline, Vector<User> recipientList) {
		super(headline);
		this.recipientList = recipientList;
	}

	private HorizontalPanel suggestBoxPanel = new HorizontalPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private HorizontalPanel content = new HorizontalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label recipientLabel = new Label();
	private Label errorLabel = new Label("\0");
	private Label successLabel = new Label("");
	private Label addedHashtagLabel = new Label("\0");
	private TextArea textBox = new TextArea();
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private static Vector<Hashtag> allHashtag = new Vector<Hashtag>();
	private static Vector<Hashtag> selectedHashtag = new Vector<Hashtag>();
	private Vector<User> recipientList = new Vector<User>();
	private String recipient = new String();
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	private Button addButton = new Button("", new ClickHandler() {
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
	
	private Button sendButton = new Button("Send", new ClickHandler() {
		public void onClick(ClickEvent event) {
			administration.createConversation(textBox.getText(), recipientList, selectedHashtag, new AsyncCallback<Conversation>() {
				public void onFailure(Throwable caught) {

				}

				public void onSuccess(Conversation result) {
					selectedHashtag.removeAllElements();
				}
			});
		}
	});

	private KeyUpHandler suggestBoxHandler = new KeyUpHandler() {
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
					suggestBox.setText("Search for hashtags");
				}
			}
		}
	};

	private void setRecipientLabel() {
		if (recipientList.size() < 1) {
			recipientLabel.setText("Public message to all users.");
		} else if (recipientList.size() < 4) {
			for (int i = 0; i < recipientList.size(); i++) {
				recipient = recipient + " '"
						+ recipientList.get(i).getFirstName() + "'";
			}

			recipientLabel.setText("Private message to: " + recipient);
		} else {
			for (int i = 0; i < 3; i++) {
				recipient = recipient + " '"
						+ recipientList.get(i).getFirstName() + "'";
			}
			recipientLabel.setText("Private message to: " + recipient + " and "
					+ new Integer(recipientList.size() - 3).toString()
					+ " more recipient(s).");
		}
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
				public void onFailure(Throwable caught) {

				}

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

	private void setOracle() {
		oracle.clear();
		for (int i = 0; i < allHashtag.size(); i++) {
			String keyword = new String(allHashtag.get(i).getKeyword());
			oracle.add(keyword);
		}
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

	protected void run() {

		administration.getAllHashtags(new AsyncCallback<Vector<Hashtag>>() {
			public void onFailure(Throwable caught) {

			}

			public void onSuccess(Vector<Hashtag> result) {
				MessageForm.allHashtag = result;
				setOracle();

			}
		});

		suggestBox.addKeyUpHandler(suggestBoxHandler);
		suggestBox.addFocusListener(new FocusListener() {
			public void onFocus(Widget arg1) {
				suggestBox.setText("");
				errorLabel.setText("\0");
				successLabel.setText("");
			}

			public void onLostFocus(Widget arg1) {
				
			}
		});

		suggestBox.setText("Search for hashtags");

		addButton.getElement().setId("addButton");
		errorLabel.setStylePrimaryName("errorLabel");
		successLabel.setStylePrimaryName("successLabel");
		sendButton.getElement().setId("sendButton");
		scroll.setSize("400px", "40px");

		setRecipientLabel();
		suggestBoxPanel.add(suggestBox);
		suggestBoxPanel.add(addButton);
		buttonPanel.add(suggestBoxPanel);
		buttonPanel.add(sendButton);
		
		this.add(recipientLabel);
		this.add(textBox);
		this.add(buttonPanel);
		this.add(errorLabel);
		this.add(successLabel);
		this.add(addedHashtagLabel);
		this.add(scroll);

	}

}
