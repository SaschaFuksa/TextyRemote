package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.User;
import hdm.itprojekt.texty.server.db.UserMapper;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProfileForm extends TextyForm {

	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox mail = new TextBox();
	private TextBox firstname = new TextBox();
	private TextBox lastname = new TextBox();
	private Button save = new Button("Save");
	private User user;
	private static String email;
	
	public ProfileForm(String headline) {
		super(headline);
	}

	@Override
	protected void run() {
		email = Texty.getLoginInfo().getEmailAddress();
		// TODO: User aus DB laden
		//user = UserMapper.userMapper().findByEmail(email);
		user = new User();
		
		// Create UI
		
		// Text
		chatFlexTable.setText(0, 0, "E-Mail");
		chatFlexTable.setText(1, 0, "Vorname");
		chatFlexTable.setText(2, 0, "Nachname");
		
		// Textboxen
		chatFlexTable.setWidget(0, 1, mail);
		chatFlexTable.setWidget(1, 1, firstname);
		chatFlexTable.setWidget(2, 1, lastname);
		
		// Save-Button
		chatFlexTable.setWidget(3, 1, save);
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Implementation für das Speichern eines Users
				
				if(user == null) {
					user = new User();
					
					user.setFirstName(firstname.getText());
					user.setLastName(lastname.getText());
					user.setEmail(email);
					
					// TODO: User in DB speichern
					//UserMapper.userMapper().insert(user);
				}
			}
		});
		
		// E-Mail einfügen
		mail.setText(email);
		if(user != null) {
			firstname.setText(user.getFirstName());
			lastname.setText(user.getLastName());
		}
		
		mainPanel.add(chatFlexTable);
		mainPanel.add(addPanel);
		RootPanel.get("Details").add(mainPanel);
	}

}
