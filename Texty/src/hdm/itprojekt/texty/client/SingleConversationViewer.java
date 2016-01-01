package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SingleConversationViewer extends TextyForm {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private FlexTable chatFlexTable = new FlexTable();
	private MessageForm messageForm = new MessageForm();
	private Label header = new Label("New message to reply to conversation");
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
		messageForm.sendButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				administration.addMessageToConversation(conversation,
						messageForm.getText(), messageForm.getHashtag(),
						new AsyncCallback<Conversation>() {
							@Override
							public void onFailure(Throwable caught) {
								LOG.severe("Error: " + caught.getMessage());
							}

							@Override
							public void onSuccess(Conversation result) {
								LOG.info("Success :"
										+ result.getClass().getSimpleName());
								messageForm.clearSelectedHashtag();
								RootPanel.get("Details").clear();
								RootPanel
										.get("Details")
										.add(new SingleConversationViewer(
												"Private Conversation", result));
							}
						});
			}
		});

		this.getElement().setId("conversationForm");
		mainPanel.getElement().setId("conversationWrapper");
		scroll.getElement().setId("conversationScroll");
		content.getElement().setId("conversationContent");
		chatFlexTable.getElement().setId("conversationContent");
		
		content.add(chatFlexTable);
		mainPanel.add(getHeadline());
		mainPanel.add(scroll);

		this.add(mainPanel);

		RootPanel.get("Info").clear();
		RootPanel.get("Info").add(header);
		RootPanel.get("Info").add(messageForm);

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
			}
		});

	}

	private void showAllMessages() {

		for (int i = 0; i < conversation.getListOfMessage().size(); i++) {

			final int index = i;
			final Message message = conversation.getListOfMessage().get(index);
			final FlexTable messageTable = new FlexTable();
			final Label text = new Label(message.getText());
			final TextBox textBox = new TextBox();
			final Label hashtagLabel = new Label();
			Label author = new Label();
			String hashtagList = new String();
			author.getElement().setId("authorPanel");

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
				text.getElement().setId("senderPanel");
				messageTable.getElement().setId("senderPanel");
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
					boolean state = true;

					@Override
					public void onClick(ClickEvent event) {

						if (state) {
							final MessageForm editMessage = new MessageForm();
							editMessage.setText(text.getText());
							editMessage.setSendButtonName("Edit");
							editMessage.removeInfoBox();
							if(message.getListOfHashtag().size() > 0){
								for (Hashtag hashtag : message.getListOfHashtag()){
									editMessage.addHashtag(hashtag.getKeyword());
								}
							}
							editMessage.sendButton.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									
									administration.editMessage(message, editMessage.getText(), editMessage.getHashtag(), 
											new AsyncCallback<Message>() {
												@Override
												public void onFailure(Throwable caught) {
													LOG.severe("Error: " + caught.getMessage());
												}

												@Override
												public void onSuccess(Message result) {
													LOG.info("Success :"
															+ result.getClass().getSimpleName());
													String hashtagList = new String();
													editMessage.clearSelectedHashtag();
													text.setText(editMessage.getText());
													if (result.getListOfHashtag().size() > 0) {
														for (Hashtag hashtag : result.getListOfHashtag()) {
															hashtagList = hashtagList + "#" + hashtag.getKeyword()
																	+ " ";
														}

														hashtagLabel.setText(hashtagList);
														hashtagLabel.getElement().setId("senderPanel");
														messageTable.getFlexCellFormatter().setColSpan(3, 0, 2);
														messageTable.setWidget(3, 0, hashtagLabel);
													}
													messageTable.setWidget(2, 0, null);
													messageTable.setWidget(1, 0, text);
													messageTable.setWidget(3, 0, hashtagLabel);
													state = true;
												}
											});
								}
							});
							
							messageTable.setWidget(1, 0, editMessage);
							messageTable.remove(hashtagLabel);
							state = false;
						} else {
							messageTable.setWidget(2, 0, null);
							messageTable.setWidget(1, 0, text);
							messageTable.setWidget(3, 0, hashtagLabel);
							state = true;
						}

					}
				});
				buttonPanel.getElement().setId("buttonPanel");
				deleteButton.getElement().setId("deleteButton");
				editButton.getElement().setId("editButton");
				buttonPanel.add(editButton);
				buttonPanel.add(deleteButton);
				messageTable.setWidget(0, 0, author);
				messageTable.setWidget(0, 1, buttonPanel);
				messageTable.getFlexCellFormatter().setColSpan(1, 0, 2);
				messageTable.setWidget(1, 0, text);

				if (message.getListOfHashtag().size() > 0) {
					for (Hashtag hashtag : message.getListOfHashtag()) {
						hashtagList = hashtagList + "#" + hashtag.getKeyword()
								+ " ";
					}

					hashtagLabel.setText(hashtagList);
					hashtagLabel.getElement().setId("senderPanel");
					messageTable.getFlexCellFormatter().setColSpan(3, 0, 2);
					messageTable.setWidget(3, 0, hashtagLabel);
				}

				chatFlexTable.setWidget(i, 1, messageTable);

			} else {
				author.setText(conversation.getListOfMessage().get(i)
						.getAuthor().getFirstName());
				text.getElement().setId("receiverPanel");
				messageTable.getElement().setId("receiverPanel");
				messageTable.setWidget(0, 0, author);
				messageTable.setWidget(1, 0, text);

				if (message.getListOfHashtag().size() > 0) {
					for (Hashtag hashtag : message.getListOfHashtag()) {
						hashtagList = hashtagList + "#" + hashtag.getKeyword()
								+ " ";
					}

					hashtagLabel.setText(hashtagList);
					hashtagLabel.getElement().setId("receiverPanel");
					messageTable.getFlexCellFormatter().setColSpan(3, 0, 2);
					messageTable.setWidget(3, 0, hashtagLabel);
				}
				
				chatFlexTable.getColumnFormatter().addStyleName(i, "chatFlexTableCell");
				chatFlexTable.setWidget(i, 0, messageTable);
			}

		}
		scroll.scrollToBottom();
	}


}
