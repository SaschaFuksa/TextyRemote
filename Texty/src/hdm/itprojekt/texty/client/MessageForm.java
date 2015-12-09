package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Diese Klasse definiert ein Modul, welches die Erstellung von Nachrichten
 * ermöglichen wird.
 * 
 *
 */
public class MessageForm extends Showcase {

	private VerticalPanel details = new VerticalPanel();
	private UserForm userForm = new UserForm();
	private AddHashtagForm hashtagForm = new AddHashtagForm();
	private Button sendButton = new Button("Send Message", new ClickHandler() {
		public void onClick(ClickEvent event) {
			String result = new String();
			Vector<User> selectedUser = userForm.getSelectedUser();
			for (int i = 0; i < selectedUser.size(); i++) {
				result = result + new String(selectedUser.get(i).getNickName()) + "\n";
				Window.alert("result");
			}
		}
	});

	private TextArea messageBox = new TextArea();

	protected void run() {
		
		userForm.run();
		messageBox.setPixelSize(400, 300);
		details.add(messageBox);
		details.add(hashtagForm);
		details.add(sendButton);
		RootPanel.get("Details").add(details);

	}

	@Override
	protected String getHeadlineText() {

		return "New Message";
	}
}
