package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
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
	private Conversation conversation = null;
	private static User currentUser = null;
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public SingleConversationViewer(String headline) {
		super(headline);
	}

	public SingleConversationViewer(String headline, Conversation conversation) {
		super(headline);
		this.conversation = conversation;
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
						new AsyncCallback<Message>() {
							@Override
							public void onFailure(Throwable caught) {
								LOG.severe("Error: " + caught.getMessage());
							}

							@Override
							public void onSuccess(Message result) {
								LOG.info("Success :"
										+ result.getClass().getSimpleName());
								message.clearSelectedHashtag();
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
				Button editButton = new Button("");
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
				buttonPanel.setStylePrimaryName("buttonPanel");
				deleteButton.getElement().setId("deleteButton");
				editButton.getElement().setId("editButton");
				buttonPanel.add(deleteButton);
				buttonPanel.add(editButton);
				messageTable.setWidget(0, 0, author);
				messageTable.setWidget(0, 1, buttonPanel);
				messageTable.getFlexCellFormatter().setColSpan(1, 0, 2);
				messageTable.setWidget(1, 0, text);
				chatFlexTable.setWidget(i, 1, messageTable);
			} else {
				author.setText(conversation.getListOfMessage().get(i)
						.getAuthor().getFirstName());
				text.setStylePrimaryName("receiverPanel");
				messageTable.setStylePrimaryName("receiverPanel");
				messageTable.setWidget(0, 0, author);
				messageTable.setWidget(1, 0, text);
				chatFlexTable.setWidget(i, 0, messageTable);
			}
			
			// HorizontalPanel header = new HorizontalPanel();
			// VerticalPanel panel = new VerticalPanel();
			// Label text = new
			// Label(conversation.getListOfMessage().get(i).getText());
			// Label author = new Label();
			// author.setStylePrimaryName("authorPanel");
			// header.add(author);
			// panel.add(header);
			// panel.add(text);
			//
			// if (conversation.getListOfMessage().get(i).getAuthor().getId() ==
			// currentUser.getId()){
			// author.setText("You");
			// text.setStylePrimaryName("senderPanel");
			// panel.setStylePrimaryName("senderPanel");
			// chatFlexTable.setWidget(i, 2, panel);
			// Button delete = new Button("x");
			// Button edit = new Button("+");
			// header.add(edit);
			// header.add(delete);
			// }
			// else {
			// author.setText(conversation.getListOfMessage().get(i).getAuthor().getFirstName());
			// text.setStylePrimaryName("receiverPanel");
			// panel.setStylePrimaryName("receiverPanel");
			// chatFlexTable.setWidget(i, 1, panel);
			// }
		}
		scroll.scrollToBottom();
	}

}
