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

/**
 * 
 * Diese Klasse stellt das persönliche Profil des aktuell eingeloggten Users
 * dar. Er hat die Wahl, ob er seinem Nutzerprofil einen Vor- und Nachnamen
 * hinzufügt oder den bereits bestehenden ändert.
 *
 *
 */
public class ProfileForm extends TextyForm {

	/**
	 * Der LOG gibt eine mögliche Exception bzw. den Erfolg des asynchronen
	 * Callbacks aus.
	 */
	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	/**
	 * Deklaration, Definition und Initialisierung der Widget.
	 */
	private User user = new User();
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private TextBox emailTextBox = new TextBox();
	private TextBox firstnameTextBox = new TextBox();
	private TextBox lastnameTextBox = new TextBox();
	private InfoBox infoBox = new InfoBox();
	private Button saveButton = createSaveButton();

	/**
	 * administration ermöglicht die asynchrone Kommunikation mit der
	 * Applikationslogik.
	 */
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	/**
	 * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular.
	 * 
	 * @param headline
	 */
	public ProfileForm(String headline) {
		super(headline);
	}

	/**
	 * Modifiziert den Standardkonstruktor, um die run() Operation bei der
	 * Initialisierung aufzurufen.
	 * 
	 * @see TextyForm
	 */
	@Override
	protected void run() {
		administration.getCurrentUser(getCurrentUserExecute());

		/*
		 * Zeilenbeschriftungen werden hier dargestellt
		 */
		chatFlexTable.setText(0, 0, "E-Mail");
		chatFlexTable.setText(1, 0, "Vorname");
		chatFlexTable.setText(2, 0, "Nachname");

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("fullWidth");
		chatFlexTable.setStyleName("ProfileForm");
		saveButton.getElement().setId("button");

		emailTextBox.setEnabled(false);
		firstnameTextBox.addFocusHandler(createFocusHandler());
		lastnameTextBox.addFocusHandler(createFocusHandler());

		/*
		 * Widgets werden hinzugefügt
		 */
		chatFlexTable.setWidget(0, 1, emailTextBox);
		chatFlexTable.setWidget(1, 1, firstnameTextBox);
		chatFlexTable.setWidget(2, 1, lastnameTextBox);
		chatFlexTable.setWidget(3, 1, saveButton);

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		mainPanel.add(getHeadline());
		mainPanel.add(chatFlexTable);
		mainPanel.add(infoBox);

		this.add(mainPanel);

	}

	/**
	 * AsyncCallback für das Auslesen des aktuellen Users aus der Datenbank.
	 * 
	 * @return
	 */
	private AsyncCallback<User> getCurrentUserExecute() {
		AsyncCallback<User> asyncCallback = new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(User result) {
				LOG.info("Success :" + result.getClass().getSimpleName());

				/*
				 * Übergibt das result an user und befüllt im Anschluss die
				 * Textboxes
				 */
				user = result;
				// Textboxen füllen
				emailTextBox.setText(user.getEmail());
				firstnameTextBox.setText(user.getFirstName());
				lastnameTextBox.setText(user.getLastName());
			}
		};
		return asyncCallback;
	}

	/**
	 * AsyncCallback für das Speichern der eingebenen Profilinformationen des Users
	 * @return
	 */
	private AsyncCallback<Void> updateUserDataExecute() {
		AsyncCallback<Void> asyncCallback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void nothing) {

			}
		};
		return asyncCallback;
	}

	/**
	 * Button zum Speichern der neu eingegebenen Profilinformationen über den
	 * User
	 * 
	 * @return
	 */
	private Button createSaveButton() {
		Button saveButton = new Button("Save", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				administration.updateUserData(firstnameTextBox.getText(),
						lastnameTextBox.getText(), updateUserDataExecute());
				infoBox.setInfoText("Your changes have been successfully saved!");
			}
		});
		return saveButton;
	}

	private FocusHandler createFocusHandler() {
		FocusHandler focusHandler = new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				
				/*
				 * Entfernung der Child Widgets vom jeweiligen Parent Widget.
				 */
				infoBox.clear();
			}
		};
		return focusHandler;
	}

}
