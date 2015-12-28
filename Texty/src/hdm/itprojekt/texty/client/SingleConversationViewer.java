package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.Conversation;

import com.google.gwt.user.client.ui.Label;

public class SingleConversationViewer extends TextyForm {

	private MessageForm message = new MessageForm();

	private Conversation conversation = null;

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
