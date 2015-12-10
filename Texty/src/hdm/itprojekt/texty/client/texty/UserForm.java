package hdm.itprojekt.texty.client.texty;

import java.util.Vector;

import hdm.itprojekt.texty.shared.bo.User;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;

public class UserForm extends TextyForm {

	public UserForm(String headline) {
		super(headline);
	}

	private HorizontalPanel suggestBoxPanel = new HorizontalPanel();
	private Label text = new Label("Contact or subscribe new users!");
	private Label errorLabel = new Label();
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private Vector<User> allUser = new Vector<User>();
	private Vector<User> selectedUser = new Vector<User>();

	private Button addButton = new Button("", new ClickHandler() {
		public void onClick(ClickEvent event) {
			errorLabel.setText("");
			String username = suggestBox.getText();
			boolean alreadySelected = checkUser(username);
			if (allUser.isEmpty() || username == "") {
				errorLabel.setText("Please select a user!");
			} else if (alreadySelected) {
				errorLabel.setText("User is already selected!");
			} else {
				addUser(username);
			}
		}

	});;

	public void addUser(String username) {
		String name = username;
		for (int i = 0; i < allUser.size(); i++) {
			if (name.equals(allUser.get(i).getNickName())) {
				final int index = i;
				selectedUser.addElement(allUser.get(i));
				allUser.remove(i);
				final HorizontalPanel panel = new HorizontalPanel();
				Label nameLabel = new Label(username);
				final Button deleteButton = new Button("", new ClickHandler() {
					public void onClick(ClickEvent event) {
						selectedUser.remove(index);
						allUser.addElement(selectedUser.get(index));
						RootPanel.get("Navigator").remove(panel);
					}

				});;
				deleteButton.getElement().setId("deleteButton");
				panel.add(nameLabel);
				panel.add(deleteButton);
				this.add(panel);
				return;
			}
		}
		errorLabel.setText("User is unknown!");
	}

	public boolean checkUser(String username) {
		String name = username;
		for (int i = 0; i < selectedUser.size(); i++) {
			if (name.equals(selectedUser.get(i).getNickName())) {
				return true;
			}
		}
		return false;
	}

	protected void run() {
		
		suggestBox.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {

				}
			}
		});

		User user1 = new User("Sasa", "sasa@fufu.de");
		User user2 = new User("Daniel", "dada@sese.de");
		User user3 = new User("David", "dada@hehe.de");
		User user4 = new User("Matteo", "mama@brbr.de");
		User user5 = new User("Erich", "erer@meme.de");
		User user6 = new User("Fred", "fredchen@schnuschnu.de");

		user1.setId(1);
		user2.setId(2);
		user3.setId(3);
		user4.setId(4);
		user5.setId(5);
		user6.setId(6);

		allUser.add(user1);
		allUser.add(user2);
		allUser.add(user3);
		allUser.add(user4);
		allUser.add(user5);
		allUser.add(user6);

		for (int i = 0; i < allUser.size(); i++) {
			String name = new String(allUser.get(i).getNickName());
			oracle.add(name);
		}
		
		addButton.getElement().setId("addButton");
		errorLabel.setStylePrimaryName("errorLabel");

		suggestBoxPanel.add(suggestBox);
		suggestBoxPanel.add(addButton);

		this.add(text);
		this.add(suggestBoxPanel);
		this.add(errorLabel);
		this.add(errorLabel);
	}

}
