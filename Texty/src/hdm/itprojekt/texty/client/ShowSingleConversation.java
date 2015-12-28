package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.Conversation;

import com.google.gwt.user.client.ui.Label;

public class ShowSingleConversation extends TextyForm {

	public ShowSingleConversation(String headline) {
		super(headline);
	}
	
	public ShowSingleConversation(String headline, Conversation conversation) {
		super(headline);
		this.conversation = conversation;
	}

	private Conversation conversation = null;
	
	private void showAllMessages(){
		for (int i = 0; i < conversation.getListOfMessage().size(); i++) {
			Label label = new Label(conversation.getListOfMessage().get(i).getText());
			this.add(label);
		}
	}
	
	@Override
	protected void run() {
		
		showAllMessages();
		
	}

}
