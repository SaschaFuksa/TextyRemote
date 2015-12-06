package hdm.itprojekt.texty.client;

//Nicht verwendete Imports nicht rauslöschen, werden eventuell noch gebraucht. 
import hdm.itprojekt.texty.shared.bo.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class CreateHashtagSubscription extends Showcase {

	//TODO Überprüfen, ob Hashtag bereits in der Datenbank vorhanden ist?
	//TODO Messagebox und weitere dazugehörige Widgets + Funktionen in eigene Klasse auslagern
	//TODO Optischer Abstand zwischen der Suggestbox und dem CellTree
	//TODO Ideensammlung wie die beiden Widgets in Kombination ihren Zweck erfüllen können
	//TODO Ideensammlung zur weiteren Modularisierung des Bereichs CreateHashtagSubscription. 

	private AddUserForm addUserForm = new AddUserForm();

	// Erzeugt eine CellList
	CellList<String> cellList = new CellList<String>(new TextCell());

	public void run() {
		
		//Aufruf der Suggestbox
		addUserForm.run();

		// Create a list data provider.
		final ListDataProvider<String> dataProvider = new ListDataProvider<String>();

		// Add the cellList to the dataProvider.
		dataProvider.addDataDisplay(cellList);

		// Create a form to add values to the data provider.
		final SuggestBox valueBox = new SuggestBox();
		valueBox.setText("Enter new Hashtag!");

		//Anlegen des Add-Button 
		Button addButton = new Button("", new ClickHandler() {
			public void onClick(ClickEvent event) {
				String newValue = valueBox.getText();
				List<String> list = dataProvider.getList();
				list.add("#" + newValue);
			}
		});

		// Handler, der beim Auswählen eines abonnierten Hashtags eine Instanz der Klasse ShowHashtagSubscription aufruft
		//und dessen run-Methode aufruft. Dadurch erscheint z.B. eine Messagebox, in der alle Nachrichten mit diesem abonnierten 
		//Hashtag erscheinen
		final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
		cellList.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						String selected = selectionModel.getSelectedObject();
						if (selected != null) {
							ShowHashtagSubscription test = new ShowHashtagSubscription();
							test.run();
						}
					}
				});
		
		//Grafische Formatierung über den Aufruf der ID im CSS
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
