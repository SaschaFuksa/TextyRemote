package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SingleConversationViewer extends TextyForm {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());
	private static Vector<Hashtag> allHashtag = new Vector<Hashtag>();

	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private FlexTable chatFlexTable = new FlexTable();
	private MessageForm message = new MessageForm();
	private static Conversation conversation = null;
	private static User currentUser = null;
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public SingleConversationViewer(String headline) {
		super(headline);
	}

	public SingleConversationViewer(String headline, Conversation conversation) {
		super(headline);
		SingleConversationViewer.conversation = conversation;
	}

	@Override
	protected void run() {

		administration.getAllHashtags(new AsyncCallback<Vector<Hashtag>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<Hashtag> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				SingleConversationViewer.allHashtag = result;

			}
		});

		administration.getCurrentUser(new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(User result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				SingleConversationViewer.currentUser = result;
				showAllMessages();
			}
		});
		message.sendButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				administration.addMessageToConversation(conversation,
						message.getText(), message.getHashtag(),
						new AsyncCallback<Conversation>() {
							@Override
							public void onFailure(Throwable caught) {
								LOG.severe("Error: " + caught.getMessage());
							}

							@Override
							public void onSuccess(Conversation result) {
								LOG.info("Success :"
										+ result.getClass().getSimpleName());
								message.clearSelectedHashtag();
								RootPanel.get("Details").clear();
								RootPanel
										.get("Details")
										.add(new SingleConversationViewer(
												"Private Conversation", result));
							}
						});
			}
		});

		scroll.setStylePrimaryName("conversationScroll");
		content.add(chatFlexTable);
		this.add(scroll);
		this.add(message);

	}

	private void showAllMessages() {

		for (int i = 0; i < conversation.getListOfMessage().size(); i++) {

			final Message message = conversation.getListOfMessage().get(i);
			final FlexTable messageTable = new FlexTable();
			final Label text = new Label(message.getText());
			Label author = new Label();
			final TextBox textBox = new TextBox();
			author.setStylePrimaryName("authorPanel");

			textBox.addKeyUpHandler(new KeyUpHandler() {
				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						messageTable.remove(textBox);
						messageTable.setWidget(1, 0, text);
						text.setText(textBox.getText());
					}
				}
			});

			if (message.getAuthor().getId() == currentUser.getId()) {
				HorizontalPanel buttonPanel = new HorizontalPanel();
				author.setText("You");
				text.setStylePrimaryName("senderPanel");
				messageTable.setStylePrimaryName("senderPanel");
				Button deleteButton = new Button("");
				deleteButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						administration.deleteMessage(conversation, message,
								new AsyncCallback<Void>() {
									@Override
									public void onFailure(Throwable caught) {
										LOG.severe("Error: "
												+ caught.getMessage());
									}

									@Override
									public void onSuccess(Void result) {
										LOG.info("Success :"
												+ result.getClass()
														.getSimpleName());
									}
								});
						conversation.removeMessageFromConversation(message);
						RootPanel.get("Details").clear();
						RootPanel.get("Details").add(
								new SingleConversationViewer(
										"Private Conversation", conversation));
					}
				});
				Button editButton = new Button("");
				editButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						messageTable.remove(text);
						messageTable.setWidget(1, 0, textBox);
						textBox.setText(text.getText());
						textBox.setFocus(true);
						MultiWordSuggestOracle oracle = setOracle(
								message.getListOfHashtag(), allHashtag);
						SuggestBox suggestBox = new SuggestBox(oracle);
						messageTable.setWidget(2, 0, suggestBox);
						
					}
				});
				buttonPanel.setStylePrimaryName("buttonPanel");
				deleteButton.getElement().setId("deleteButton");
				editButton.getElement().setId("editButton");
				buttonPanel.add(deleteButton);
				buttonPanel.add(editButton);
				messageTable.setWidget(0, 0, author);
				messageTable.setWidget(0, 1, buttonPanel);
				messageTable.getFlexCellFormatter().setColSpan(1, 0, 2);
				messageTable.setWidget(1, 0, text);

				if (message.getListOfHashtag().size() > 0) {
					Label hashtagLabel = new Label();
					String hashtagList = new String();
					for (Hashtag hashtag : message.getListOfHashtag()) {
						hashtagList = hashtagList + "#" + hashtag.getKeyword()
								+ " ";
					}

					hashtagLabel.setText(hashtagList);
					hashtagLabel.setStylePrimaryName("senderPanel");
					messageTable.getFlexCellFormatter().setColSpan(3, 0, 2);
					messageTable.setWidget(3, 0, hashtagLabel);
				}

				chatFlexTable.setWidget(i, 1, messageTable);

			} else {
				author.setText(conversation.getListOfMessage().get(i)
						.getAuthor().getFirstName());
				text.setStylePrimaryName("receiverPanel");
				messageTable.setStylePrimaryName("receiverPanel");
				messageTable.setWidget(0, 0, author);
				messageTable.setWidget(1, 0, text);

				if (message.getListOfHashtag().size() > 0) {
					Label hashtagLabel = new Label();
					String hashtagList = new String();
					for (Hashtag hashtag : message.getListOfHashtag()) {
						hashtagList = hashtagList + "#" + hashtag.getKeyword()
								+ " ";
					}

					hashtagLabel.setText(hashtagList);
					hashtagLabel.setStylePrimaryName("receiverPanel");
					messageTable.getFlexCellFormatter().setColSpan(3, 0, 2);
					messageTable.setWidget(3, 0, hashtagLabel);
				}

				chatFlexTable.setWidget(i, 0, messageTable);
			}

		}
		scroll.scrollToBottom();
	}

	private MultiWordSuggestOracle setOracle(Vector<Hashtag> selectedHastags,
			Vector<Hashtag> allHashtag) {
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		Vector<Hashtag> editedHashtagsList = allHashtag;
		if(selectedHastags.size() < 1){
			for (Hashtag hashtag : editedHashtagsList){
				oracle.add(hashtag.getKeyword());
			}
		}
		else {
			for (Hashtag hashtag : editedHashtagsList){
				for (Hashtag selectedHashtag : selectedHastags){
					if (selectedHashtag.getId() == hashtag.getId()){
						editedHashtagsList.remove(selectedHashtag);
					}
				}
			}
			for (Hashtag hashtag : editedHashtagsList){
				oracle.add(hashtag.getKeyword());
			}
		}
		return oracle;
	}

}
