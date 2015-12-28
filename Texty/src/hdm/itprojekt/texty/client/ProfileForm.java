package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.User;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProfileForm extends TextyForm {

	public static User user;
	private static String email;
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox mail = new TextBox();
	private TextBox firstname = new TextBox();
	private TextBox lastname = new TextBox();
	private Button save = new Button("Save");

	public ProfileForm(String headline) {
		super(headline);
	}

	@Override
	protected void run() {
		email = Texty.getLoginInfo().getEmailAddress();
		TextyAdministrationAsync administration = ClientsideSettings
				.getTextyAdministration();
		class getCurrentUserCallback implements AsyncCallback<User> {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(User user) {
				ProfileForm.user = user;
				firstname.setText(user.getFirstName());
				lastname.setText(user.getLastName());
			}
		}
		administration.getCurrentUser(new getCurrentUserCallback());

		// Create UI

		// Text
		chatFlexTable.setText(0, 0, "E-Mail");
		chatFlexTable.setText(1, 0, "Vorname");
		chatFlexTable.setText(2, 0, "Nachname");

		// Textboxen
		chatFlexTable.setWidget(0, 1, mail);
		mail.setEnabled(false);
		chatFlexTable.setWidget(1, 1, firstname);
		chatFlexTable.setWidget(2, 1, lastname);

		// Textboxen füllen
		// E-Mail einfügen
		mail.setText(email);

		// Save-Button
		chatFlexTable.setWidget(3, 1, save);
		save.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Implementation für das Speichern eines Users

				TextyAdministrationAsync administration = ClientsideSettings
						.getTextyAdministration();

				class checkUserCallback implements AsyncCallback<Void> {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Void nothing) {
					}
				}
				class updateUserCallback implements AsyncCallback<Void> {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Void nothing) {
					}
				}

				administration.checkUserData(new checkUserCallback());
				administration.updateUserData(firstname.getText(),
						lastname.getText(), new updateUserCallback());
			}
		});
		
		chatFlexTable.setStyleName("flexTable");
		save.getElement().setId("sendButton");
		
		mainPanel.add(chatFlexTable);
		mainPanel.add(addPanel);
		RootPanel.get("Details").add(mainPanel);
	}

}
