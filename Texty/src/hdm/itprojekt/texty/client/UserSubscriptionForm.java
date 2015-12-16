package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label intro = new HTML(
			"To add new user, use the searchfield on the left! <br> "
					+ "To delete subscriptions, click on the delete button next to your subscribed user.");
	private Label errorLabel = new Label("\0");
	private Label warningLabel = new Label("");
	private Label successLabel = new Label("");
	private static User currentUser = new User();
	private Vector<User> selectedUser = new Vector<User>();
	private static Vector<User> subscribedUser = new Vector<User>();
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public void addUserSubscriptions() {
		String result = new String("");
		String warning = new String("");
		for (int i = 0; i < selectedUser.size(); i++) {
			if (checkSubscription(selectedUser.get(i).getNickName())) {
				subscribedUser.add(selectedUser.get(i));
				result = result + " '" + selectedUser.get(i).getNickName() + "'";
			} else {
				warning = warning + " '" + selectedUser.get(i).getNickName() + "'";
			}
		}
		if (result != "") {
			successLabel.setText("User" + result + " successful subscribed!");
		}
		if (warning != "") {
			warningLabel.setText("User" + warning + " is already subscribed!");
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
				successLabel.setText("Subscribed user '" + subscribedUser.get(i).getNickName() + "' sucessful removed!");
				warningLabel.setText("");
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

		administration.getCurrentUser(new AsyncCallback<User>() {
			public void onFailure(Throwable caught) {

			}

			public void onSuccess(User result) {
				UserSubscriptionForm.currentUser = result;
				
				Window.alert("Current User: " + currentUser.getFirstName());
				
				administration.getAllSubscribedUsers(currentUser, new AsyncCallback<Vector<User>>() {
					public void onFailure(Throwable caught) {
						Window.alert("Fail");
					}

					public void onSuccess(Vector<User> result) {
						Window.alert("Anzahl Sub User: " + result.size());
						UserSubscriptionForm.subscribedUser = result;
						
						addUserSubscriptions();

						showSubscriptions();

					}
				});
			}
		});

		warningLabel.setStylePrimaryName("errorLabel");
		successLabel.setStylePrimaryName("successLabel");
		scroll.setSize("250px", "110px");

		this.add(intro);
		this.add(errorLabel);
		this.add(scroll);
		this.add(warningLabel);
		this.add(successLabel);

	}

}
