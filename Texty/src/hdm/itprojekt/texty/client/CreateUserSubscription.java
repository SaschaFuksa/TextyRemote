package hdm.itprojekt.texty.client;

import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class CreateUserSubscription extends Showcase {

	// TODO Idee:Testuserdaten -> isElementOf-Überprüfung und damit Createsub.
	// und Createhash. weiter ausbauen

	// Erzeugt eine CellList
	CellList<String> cellList = new CellList<String>(new TextCell());

	private AddUserForm addUserForm = new AddUserForm();

	public void run() {

		addUserForm.onLoad();

		// Create a list data provider.
		final ListDataProvider<String> dataProvider = new ListDataProvider<String>();

		// Add the cellList to the dataProvider.
		dataProvider.addDataDisplay(cellList);

		// Create a form to add values to the data provider.
		final SuggestBox valueBox = new SuggestBox();
		valueBox.setText("Enter new User!");

		// Anlegen des Add-Button
		Button addButton = new Button("", new ClickHandler() {
			public void onClick(ClickEvent event) {
				String newValue = valueBox.getText();
				List<String> list = dataProvider.getList();
				list.add(newValue);
			}
		});

		// Handler, der beim Auswählen eines abonnierten Hashtags eine Instanz
		// der Klasse ShowHashtagSubscription aufruft
		// und dessen run-Methode aufruft. Dadurch erscheint z.B. eine
		// Messagebox, in der alle Nachrichten mit diesem abonnierten
		// Hashtag erscheinen
		final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
		cellList.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						String selected = selectionModel.getSelectedObject();
						if (selected != null) {
							AddMessageForm messageForm = new AddMessageForm();
							messageForm.onLoad();
						}
					}
				});

		// Grafische Formatierung über den Aufruf der ID im CSS
		addButton.getElement().setId("addButton");

		VerticalPanel vertPanel = new VerticalPanel();
		vertPanel.add(valueBox);
		vertPanel.add(addButton);
		vertPanel.add(cellList);
		RootPanel.get("Navigator").add(vertPanel);

	}

	@Override
	protected String getHeadlineText() {
		// TODO Auto-generated method stub
		return null;
	}

}
