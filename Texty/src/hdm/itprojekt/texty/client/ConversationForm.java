package hdm.itprojekt.texty.client;

import java.util.Vector;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.HashtagSubscription;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConversationForm extends TextyForm {

	public ConversationForm(String headline) {
		super(headline);
	}
	
	private Label intro = new Label("Here you can read and reply to private conversations");
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private static Vector<Conversation> conList = new Vector<Conversation>();
	private TextyAdministrationAsync administration = ClientsideSettings.getTextyAdministration();
	
 void showConversation(){
		for (int i = 0; i < conList.size(); i++) {
			VerticalPanel conversation = new VerticalPanel();
			HorizontalPanel header = new HorizontalPanel();
			Message message = conList.get(i).getLastMessage();
			String receiver = setRecipient(conList.get(i).getLastMessage().getListOfReceivers());
			Label receivers = new Label(receiver);
			Label author = new Label(message.getAuthor().getFirstName());
			Label text = new Label(message.getText());
			
			header.add(author);
			header.add(receivers);
			
			conversation.add(header);
			conversation.add(text);
			
			this.add(conversation);
			
		}
	}
	
	private String setRecipient(Vector<User> receiver) {
		String receivers = "Private conversation with";
		if (receiver.size() < 4) {
			for (int i = 0; i < receiver.size(); i++) {
				receivers = receivers + " '"
						+ receiver.get(i).getFirstName() + "'";
			}

		} else {
			for (int i = 0; i < 3; i++) {
				receivers = receivers + " '"
						+ receiver.get(i).getFirstName() + "'";
			}
			receivers = receivers + " and "
					+ new Integer(receiver.size() - 3).toString()
					+ " more receiver(s).";
		}
		return receivers;
	}

	protected void run() {
		
		administration.getAllConversationsFromUser( new AsyncCallback<Vector<Conversation>>() {
			public void onFailure(Throwable caught) {

			}

			public void onSuccess(Vector<Conversation> result) {
				ConversationForm.conList = result;
				showConversation();
			}
		});
		
		scroll.setSize("300px", "400px");
		
		this.add(intro);
		this.add(scroll);
	}

}
