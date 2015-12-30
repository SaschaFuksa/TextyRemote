package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SingleConversationViewer extends TextyForm {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

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
								RootPanel.get("Details").add(
										new SingleConversationViewer(
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
			Label text = new Label(message.getText());
			Label author = new Label();
			author.setStylePrimaryName("authorPanel");

			if (message.getAuthor().getId() == currentUser
					.getId()) {
				HorizontalPanel buttonPanel = new HorizontalPanel();
				author.setText("You");
				text.setStylePrimaryName("senderPanel");
				messageTable.setStylePrimaryName("senderPanel");
				Button deleteButton = new Button("");
				deleteButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						administration.deleteMessage(conversation, message, new AsyncCallback<Void>() {
									@Override
									public void onFailure(Throwable caught) {
										LOG.severe("Error: " + caught.getMessage());
									}

									@Override
									public void onSuccess(Void result) {
										LOG.info("Success :"
												+ result.getClass().getSimpleName());
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
				buttonPanel.setStylePrimaryName("buttonPanel");
				deleteButton.getElement().setId("deleteButton");
				editButton.getElement().setId("editButton");
				buttonPanel.add(deleteButton);
				buttonPanel.add(editButton);
				messageTable.setWidget(0, 0, author);
				messageTable.setWidget(0, 1, buttonPanel);
				messageTable.getFlexCellFormatter().setColSpan(1, 0, 2);
				messageTable.setWidget(1, 0, text);
				
				if (message.getListOfHashtag().size() > 0){
					Label hashtagLabel = new Label();
					String hashtagList = new String();
					for (Hashtag hashtag : message.getListOfHashtag()){
						hashtagList = hashtagList + "#" + hashtag.getKeyword() + " ";
					}
					
					hashtagLabel.setText(hashtagList);
					hashtagLabel.setStylePrimaryName("senderPanel");
					messageTable.getFlexCellFormatter().setColSpan(2, 0, 2);
					messageTable.setWidget(2, 0, hashtagLabel);
				}
				
				chatFlexTable.setWidget(i, 1, messageTable);
				
			} 
			else {
				author.setText(conversation.getListOfMessage().get(i)
						.getAuthor().getFirstName());
				text.setStylePrimaryName("receiverPanel");
				messageTable.setStylePrimaryName("receiverPanel");
				messageTable.setWidget(0, 0, author);
				messageTable.setWidget(1, 0, text);
				
				if (message.getListOfHashtag().size() > 0){
					Label hashtagLabel = new Label();
					String hashtagList = new String();
					for (Hashtag hashtag : message.getListOfHashtag()){
						hashtagList = hashtagList + "#" + hashtag.getKeyword() + " ";
					}
					
					hashtagLabel.setText(hashtagList);
					hashtagLabel.setStylePrimaryName("receiverPanel");
					messageTable.getFlexCellFormatter().setColSpan(2, 0, 2);
					messageTable.setWidget(2, 0, hashtagLabel);
				}
				
				chatFlexTable.setWidget(i, 0, messageTable);
			}

		}
		scroll.scrollToBottom();
	}

}
