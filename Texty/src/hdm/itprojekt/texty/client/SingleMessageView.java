package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SingleMessageView extends VerticalPanel {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private Button deleteButton = createDeleteButton();
	private Button editButton = createEditButton();
	private Label text = new Label();
	private Label author = new Label();
	private Label dateLabel = new Label();
	private Label hashtagLabel = new Label();
	private Message message = new Message();
	private User user = new User();
	private Conversation conversation = new Conversation();
	private FlexTable messageTable = new FlexTable();
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	SingleMessageView(Message message, User user, Conversation conversation) {
		this.message = message;
		this.user = user;
		this.conversation = conversation;
		run();
	}

	public void run() {

		String date = DateTimeFormat.getFormat("yyyy.MM.dd 'at' HH:mm:ss")
				.format(message.getDateOfCreation());
		dateLabel.setText(date);
		text.setText(message.getText());

		if (message.getListOfHashtag().size() > 0) {
			String keyword = new String();
			for (Hashtag hashtag : message.getListOfHashtag()) {
				keyword = keyword + "#" + hashtag.getKeyword() + " ";
			}
			hashtagLabel.setText(keyword);
		}
		
		if (user.getId() == message.getAuthor().getId()) {
			author.setText("You");
			messageTable.setWidget(0, 2, buttonPanel);	
			
			this.getElement().setId("senderPanel");
			text.getElement().setId("senderPanel");
			hashtagLabel.getElement().setId("senderPanel");
			messageTable.getElement().setId("senderPanel");
			getElement().setId("senderPanel");
		} else {
			author.setText(message.getAuthor().getFirstName());
			messageTable.getElement().setId("receiverPanel");
		}
		
		buttonPanel.getElement().setId("buttonPanel");
		deleteButton.getElement().setId("deleteButton");
		editButton.getElement().setId("editButton");
		author.getElement().setId("authorPanel");
		dateLabel.getElement().setId("conversationDate");
		messageTable.getFlexCellFormatter().setColSpan(1, 0, 3);
		messageTable.getFlexCellFormatter().setColSpan(2, 0, 3);
		messageTable.getFlexCellFormatter().setColSpan(3, 0, 3);

		messageTable.setWidget(0, 0, author);
		messageTable.setWidget(0, 1, dateLabel);
		messageTable.setWidget(1, 0, text);
		messageTable.setWidget(3, 0, hashtagLabel);
		
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);
		
		this.add(messageTable);
	}

	private AsyncCallback<Void> deleteMessageExecute() {
		AsyncCallback<Void> asyncCallback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void nothing) {

			}
		};
		return asyncCallback;
	}

	private AsyncCallback<Message> editMessageExecute() {
		AsyncCallback<Message> asyncCallback = new AsyncCallback<Message>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Message result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				text.setText(result.getText());
				if (message.getListOfHashtag().size() > 0) {
					String keyword = new String();
					for (Hashtag hashtag : message.getListOfHashtag()) {
						keyword = keyword + "#" + hashtag.getKeyword() + " ";
					}
					hashtagLabel.setText(keyword);
				} else {
					hashtagLabel.setText("");
				}
				
				messageTable.setWidget(2, 0, null);
				messageTable.setWidget(1, 0, text);
				messageTable.setWidget(3, 0, hashtagLabel);
			}
		};
		return asyncCallback;
	}

	private Button createDeleteButton() {
		Button deleteButton = new Button("", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				administration.deleteMessage(conversation, message,
						deleteMessageExecute());
				conversation.removeMessageFromConversation(message);
				RootPanel.get("Details").clear();
				if (conversation.getListOfMessage().size() == 0){
					RootPanel.get("Navigator").clear();
					RootPanel.get("Navigator").add(
							new ConversationForm("Conversations"));
				} else {
					RootPanel.get("Details").add(
							new SingleConversationViewer("Private Conversation",
									conversation));
				}
			}
		});
		return deleteButton;
	}

	private Button createEditButton() {
		Button editButton = new Button("", new ClickHandler() {
			boolean state = true;			
			@Override
			public void onClick(ClickEvent event) {
				if (state) {
					
					MessageForm editMessage = new MessageForm();
					
					editMessage.setText(text.getText());
					editMessage.setSendButtonName("Edit");
					editMessage.removeInfoBox();
					if (message.getListOfHashtag().size() != 0) {
						for (Hashtag hashtag : message.getListOfHashtag()) {
							editMessage.addHashtag(hashtag.getKeyword());
						}
					}
					editMessage.sendButton.addClickHandler(createClickHandler(editMessage));
					
					messageTable.setWidget(2, 0, editMessage);
					messageTable.setWidget(1, 0, null);
					messageTable.setWidget(3, 0, null);
					state = false;
				} else {
					messageTable.setWidget(2, 0, null);
					messageTable.setWidget(1, 0, text);
					messageTable.setWidget(3, 0, hashtagLabel);
					state = true;
				}

			}
		});
		return editButton;
	}

	private ClickHandler createClickHandler(final MessageForm editMessage) {
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				administration.editMessage(message, editMessage.getText(),
						editMessage.getHashtag(), editMessageExecute());
			}
		};
		return clickHandler;
	}
	
}
