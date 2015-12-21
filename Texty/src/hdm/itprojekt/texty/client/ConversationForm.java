package hdm.itprojekt.texty.client;

import java.util.Vector;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.User;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConversationForm extends TextyForm {

	public ConversationForm(String headline) {
		super(headline);
	}

	private Label intro = new Label(
			"Here you can read and reply to private conversations");
	private static Vector<Conversation> conList = new Vector<Conversation>();
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public void showConversation() {
		for (int i = 0; i < conList.size(); i++) {
			VerticalPanel conversationPanel = new VerticalPanel();
			HorizontalPanel header = new HorizontalPanel();
			String receiver = setRecipient(conList.get(i).getLastMessage()
					.getListOfReceivers());
			Label receivers = new Label(receiver);
			Label author = new Label(conList.get(i).getLastMessage()
					.getAuthor().getFirstName());
			Label text = new Label(conList.get(i).getLastMessage().getText());

			header.add(author);
			header.add(receivers);

			conversationPanel.add(header);
			conversationPanel.add(text);
			
			this.add(conversationPanel);

		}
	}

	private String setRecipient(Vector<User> receiver) {
		String receivers = "Private conversation with";
		if (receiver.size() < 4) {
			for (int i = 0; i < receiver.size(); i++) {
				Window.alert(" " + receiver.get(i).getFirstName());
				receivers = receivers + " '" + receiver.get(i).getFirstName()
						+ "'";
			}

		} else {
			for (int i = 0; i < 3; i++) {
				Window.alert(" " + receiver.get(i).getFirstName());
				receivers = receivers + " '" + receiver.get(i).getFirstName()
						+ "'";
			}
			receivers = receivers + " and "
					+ new Integer(receiver.size() - 3).toString()
					+ " more receiver(s).";
		}
		return receivers;
	}

	protected void run() {

		administration
				.getAllConversationsFromUser(new AsyncCallback<Vector<Conversation>>() {
					public void onFailure(Throwable caught) {

					}

					public void onSuccess(Vector<Conversation> result) {
						Window.alert("größe con: " + result.size());
						Window.alert("mes größe con1: " + result.get(0).getListOfMessage().size());
						Window.alert("mes größe con2: " + result.get(1).getListOfMessage().size());
						Window.alert("mes größe con3: " + result.get(2).getListOfMessage().size());
						ConversationForm.conList = result;						
						showConversation();
						
					}
				});

		this.add(intro);
	}

}
