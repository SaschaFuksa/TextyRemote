package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProfileForm extends TextyForm {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	public static User user;
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private TextBox emailTextBox = new TextBox();
	private TextBox firstnameTextBox = new TextBox();
	private TextBox lastnameTextBox = new TextBox();
	private InfoBox infoBox = new InfoBox();
	private Button saveButton = createSaveButton();
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public ProfileForm(String headline) {
		super(headline);
	}

	@Override
	protected void run() {
		administration.getCurrentUser(getCurrentUserExecute());

		// Zeilenbeschriftung
		chatFlexTable.setText(0, 0, "E-Mail");
		chatFlexTable.setText(1, 0, "Vorname");
		chatFlexTable.setText(2, 0, "Nachname");

		// Styles festlegen
		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("fullWidth");
		chatFlexTable.setStyleName("ProfileForm");
		saveButton.getElement().setId("sendButton");
		
		emailTextBox.setEnabled(false);
		firstnameTextBox.addFocusHandler(createFocusHandler());
		lastnameTextBox.addFocusHandler(createFocusHandler());

		// Widgets hinzufügen
		chatFlexTable.setWidget(0, 1, emailTextBox);
		chatFlexTable.setWidget(1, 1, firstnameTextBox);
		chatFlexTable.setWidget(2, 1, lastnameTextBox);
		chatFlexTable.setWidget(3, 1, saveButton);
		
		mainPanel.add(getHeadline());
		mainPanel.add(chatFlexTable);
		mainPanel.add(infoBox);

		this.add(mainPanel);

	}

	private AsyncCallback<User> getCurrentUserExecute() {
		AsyncCallback<User> asyncCallback = new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(User result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				user = result;
				// Textboxen füllen
				emailTextBox.setText(user.getEmail());
				firstnameTextBox.setText(user.getFirstName());
				lastnameTextBox.setText(user.getLastName());
			}
		};
		return asyncCallback;
	}
	
	private AsyncCallback<Void> updateUserDataExecute() {
		AsyncCallback<Void> asyncCallback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
			}
		};
		return asyncCallback;
	}

	private Button createSaveButton() {
		Button saveButton = new Button("Save", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				administration.updateUserData(firstnameTextBox.getText(), lastnameTextBox.getText(), updateUserDataExecute());
				infoBox.setInfoText("Your changes are successful saved!");
			}
		});
		return saveButton;
	}
	
	private FocusHandler createFocusHandler() {
		FocusHandler focusHandler = new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				infoBox.clear();
			}
		};
		return focusHandler;
	}

}
