package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

public class NewMessage extends TextyForm {

	private Label recipientLabel = new Label("");
	private String recipient = new String();
	private MessageForm message = new MessageForm();
	private Vector<User> recipientList = new Vector<User>();

	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public NewMessage(String headline) {
		super(headline);
	}

	public NewMessage(String headline, Vector<User> recipientList) {
		super(headline);
		this.recipientList = recipientList;
	}

	@Override
	protected void run() {
		message.sendButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				administration.createConversation(message.getText(),
						recipientList, message.getHashtag(),
						new AsyncCallback<Conversation>() {
							@Override
							public void onFailure(Throwable caught) {

							}

							@Override
							public void onSuccess(Conversation result) {
								message.clearSelectedHashtag();
							}
						});
			}
		});
		recipientLabel.getElement().setId("recipientLabel");
		setRecipientLabel();
		this.add(getHeadline());
		this.add(recipientLabel);
		this.add(message);

	}

	private void setRecipientLabel() {
		if (recipientList.size() < 1) {
			recipientLabel.setText("Public message to all users.");
		} else if (recipientList.size() < 4) {
			for (int i = 0; i < recipientList.size(); i++) {
				recipient = recipient + " '"
						+ recipientList.get(i).getFirstName() + "'";
			}

			recipientLabel.setText("Private message to: " + recipient);
		} else {
			for (int i = 0; i < 3; i++) {
				recipient = recipient + " '"
						+ recipientList.get(i).getFirstName() + "'";
			}
			recipientLabel.setText("Private message to: " + recipient + " and "
					+ new Integer(recipientList.size() - 3).toString()
					+ " more recipient(s).");
		}
	}

}
