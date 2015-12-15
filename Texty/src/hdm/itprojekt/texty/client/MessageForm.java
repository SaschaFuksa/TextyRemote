package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;

public class MessageForm extends TextyForm {

	public MessageForm(String headline) {
		super(headline);
	}

	public MessageForm(String headline, Vector<User> recipientList) {
		super(headline);
		this.recipientList = recipientList;
	}

	private Label recipientLabel = new Label();
	private TextArea text = new TextArea();
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private Vector<User> recipientList = new Vector<User>();
	private String recipient = new String();

	private void setRecipientLabel() {
		if (recipientList.size() < 1) {
			recipientLabel.setText("Public message to all users.");
		} else if (recipientList.size() < 4) {
			for (int i = 0; i < recipientList.size(); i++) {
				recipient = recipient + " '"
						+ recipientList.get(i).getNickName() + "'";
			}

			recipientLabel.setText("Private message to: " + recipient);
		} else {
			for (int i = 0; i < 3; i++) {
				recipient = recipient + " '"
						+ recipientList.get(i).getNickName() + "'";
			}
			recipientLabel.setText("Private message to: " + recipient + " and "
					+ new Integer(recipientList.size() - 3).toString()
					+ " more recipient(s).");
		}
	}

	protected void run() {

		setRecipientLabel();
		this.add(recipientLabel);
		this.add(text);

	}

}
