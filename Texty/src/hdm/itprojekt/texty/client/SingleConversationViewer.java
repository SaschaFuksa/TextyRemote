package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Message;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

public class SingleConversationViewer extends TextyForm {

	private MessageForm message = new MessageForm();
	private Conversation conversation = null;
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

		showAllMessages();
		message.sendButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				administration.addMessageToConversation(conversation, message.getText(), message.getHashtag(), new AsyncCallback<Message>() {
							@Override
							public void onFailure(Throwable caught) {

							}

							@Override
							public void onSuccess(Message result) {
								message.clearSelectedHashtag();
							}
						});
			}
		});
		this.add(message);

	}

	private void showAllMessages() {
		for (int i = 0; i < conversation.getListOfMessage().size(); i++) {
			Label label = new Label(conversation.getListOfMessage().get(i)
					.getText());
			this.add(label);
		}
	}

}
