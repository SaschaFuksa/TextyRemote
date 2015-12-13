package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserSubscriptionForm extends TextyForm {

	public UserSubscriptionForm(String headline) {
		super(headline);
	}

	public UserSubscriptionForm(String headline, Vector<User> selectedUser) {
		super(headline);
		this.selectedUser = selectedUser;
	}

	private VerticalPanel selectionPanel = new VerticalPanel();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label intro = new HTML(
			"To add new user, use the searchfield on the left! <br> "
					+ "To delete subscriptions, click on the delete button next to your subscribed user.");
	private Label errorLabel = new Label("\0");
	private Vector<User> selectedUser = new Vector<User>();
	private Vector<User> subscribedUser = new Vector<User>();

	public void addUserSubscriptions() {
//		String result = new String("Folgende User wurden erfolgreich hinzugef�gt:");
//		String warning = new String();
		for (int i = 0; i < selectedUser.size(); i++) {
			if(checkSubscription(selectedUser.get(i).getNickName())){
				final HorizontalPanel panel = new HorizontalPanel();
				final Label nameLabel = new Label(selectedUser.get(i).getNickName());
				nameLabel.setStylePrimaryName("selectedObjectLabel");
				final Button deleteButton = new Button("", new ClickHandler() {
					public void onClick(ClickEvent event) {
						deleteSubscription(nameLabel.getText());
						content.remove(panel);
					}

				});
				;
				deleteButton.getElement().setId("deleteButton");
				panel.add(nameLabel);
				panel.add(deleteButton);
				content.add(panel);
//				result = result + " " + selectedUser.get(i).getNickName();
			}
			else {
				Window.alert(selectedUser.get(i).getNickName() + " bereits abonniert!");
			}
		}

	}
	
	public boolean checkSubscription(String username) {
		String name = username;
		for (int i = 0; i < subscribedUser.size(); i++) {
			if (name.equals(subscribedUser.get(i).getNickName())) {
				return false;
			}
		}
		return true;
	}

	private void deleteSubscription(String nickname) {
		String name = nickname;
		int indexSelectedUser = 0;
		for (int i = 0; i < subscribedUser.size(); i++) {
			if (name.equals(subscribedUser.get(i).getNickName())) {
				indexSelectedUser = i;
			}
		}

		subscribedUser.remove(indexSelectedUser);

	}

	public void showSubscriptions() {
		for (int i = 0; i < subscribedUser.size(); i++) {
			final HorizontalPanel panel = new HorizontalPanel();
			final Label nameLabel = new Label(subscribedUser.get(i)
					.getNickName());
			nameLabel.setStylePrimaryName("selectedObjectLabel");
			final Button deleteButton = new Button("", new ClickHandler() {
				public void onClick(ClickEvent event) {
					deleteSubscription(nameLabel.getText());
					content.remove(panel);
				}

			});
			deleteButton.getElement().setId("deleteButton");
			panel.add(nameLabel);
			panel.add(deleteButton);
			content.add(panel);
		}
	}

	protected void run() {

		User user1 = new User("Matteo", "mama@brbr.de");
		User user2 = new User("Erich", "erer@meme.de");

		user1.setId(4);
		user2.setId(5);

		subscribedUser.add(user1);
		subscribedUser.add(user2);

		showSubscriptions();

		errorLabel.setStylePrimaryName("errorLabel");
		scroll.setSize("250px", "110px");

		this.add(intro);
		this.add(errorLabel);
		this.add(scroll);
		this.add(selectionPanel);
		
		addUserSubscriptions();

	}

}