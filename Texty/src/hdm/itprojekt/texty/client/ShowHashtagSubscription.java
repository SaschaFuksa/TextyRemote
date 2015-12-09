package hdm.itprojekt.texty.client;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.List;


//TODO Überprüfen, ob Hashtag bereits in der Datenbank vorhanden ist?
//TODO Messagebox und weitere dazugehörige Widgets + Funktionen in eigene Klasse auslagern
//TODO Optischer Abstand zwischen der Suggestbox und dem CellTree
//TODO Ideensammlung wie die beiden Widgets in Kombination ihren Zweck erfüllen können
//TODO Ideensammlung zur weiteren Modularisierung des Bereichs CreateHashtagSubscription. 

public class ShowHashtagSubscription extends Showcase {

	
	private VerticalPanel navigation = new VerticalPanel();
	/*
	 * MultiWordSuggestOracle oracle = new MultiWordSuggestOracle(); SuggestBox
	 * suggestBox = new SuggestBox(oracle);
	 */
	// private Button addButton = new Button();
	// private Button deleteButton = new Button("Delete Hashtagsubscription");
	private MessageForm messageForm = new MessageForm();
	private AddHashtagForm addHashtagForm = new AddHashtagForm();
	// Erzeugt eine CellList
	CellList<String> cellList = new CellList<String>(new TextCell());
	AddSubscriptionForm AddSubscriptionForm = new AddSubscriptionForm();

	public void run() {

		//messageForm.run();
		
		AddSubscriptionForm.run();
		
		addHashtagForm.run();

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
							MessageForm messageForm = new MessageForm();
							messageForm.onLoad();
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

		/*
		 * deleteButton.addClickHandler(new ClickHandler() { public void
		 * onClick(ClickEvent event) { // Weitere Implementierung erforderlich
		 * Window.alert("Hashtagsubscription deleted!"); } });
		 * 
		 * // Example Users User user1 = new User("Sasa", "sasa@fufu.de"); User
		 * user2 = new User("Daniel", "dada@sese.de"); User user3 = new
		 * User("David", "dada@hehe.de"); User user4 = new User("Matteo",
		 * "mama@brbr.de"); User user5 = new User("Erich", "erer@meme.de"); User
		 * user6 = new User("Fred", "fredchen@schnuschnu.de");
		 * 
		 * user1.setId(1); user2.setId(2); user3.setId(3); user4.setId(4);
		 * user5.setId(5); user6.setId(6);
		 * 
		 * Vector<User> listOfUser = new Vector<User>(); listOfUser.add(user1);
		 * listOfUser.add(user2); listOfUser.add(user3); listOfUser.add(user4);
		 * listOfUser.add(user5); listOfUser.add(user6);
		 * 
		 * addButton.getElement().setId("addButton");
		 */

		/*
		 * suggestBox.addKeyUpHandler(new KeyUpHandler() { public void
		 * onKeyUp(KeyUpEvent event) { if (event.getNativeKeyCode() ==
		 * KeyCodes.KEY_ENTER) { final HorizontalPanel selectedUserPanel = new
		 * HorizontalPanel(); Label selectedUserLabel = new
		 * Label(suggestBox.getText()); Button deleteButton = new Button();
		 * 
		 * int i = 0;
		 * 
		 * User example = new User(); while(i < listOfUser.size()){ if
		 * (suggestBox.getText().equals(listOfUser.get(i+1).getFirstName ())){
		 * example = listOfUser.get(i+1); } else { i++; } }
		 * deleteButton.setTabIndex(example.getId());
		 * 
		 * 
		 * deleteButton.addClickHandler(new ClickHandler() { public void
		 * onClick(ClickEvent event) { RootPanel.get("Navigator")
		 * .remove(selectedUserPanel); } });
		 * 
		 * deleteButton.getElement().setId("deleteButton");
		 */

		/*
		 * selectedUserPanel.add(selectedUserLabel);
		 * selectedUserPanel.add(deleteButton);
		 * RootPanel.get("Navigator").add(selectedUserPanel);
		 * 
		 * } } });
		 * 
		 * addButton.addClickHandler(new ClickHandler() { public void
		 * onClick(ClickEvent event) { final HorizontalPanel selectedUserPanel =
		 * new HorizontalPanel(); Label selectedUserLabel = new
		 * Label(suggestBox.getText()); Button deleteButton = new Button();
		 * 
		 * int i = 0;
		 * 
		 * User example = new User(); while(i < listOfUser.size()){
		 * if(suggestBox .getText().equals(listOfUser.get(i+1).getFirstName())){
		 * example = listOfUser.get(i+1); } else { i++; } }
		 * deleteButton.setTabIndex(example.getId());
		 * 
		 * 
		 * deleteButton.addClickHandler(new ClickHandler() { public void
		 * onClick(ClickEvent event) {
		 * RootPanel.get("Navigator").remove(selectedUserPanel); } });
		 * 
		 * deleteButton.getElement().setId("deleteButton");
		 * 
		 * selectedUserPanel.add(selectedUserLabel);
		 * selectedUserPanel.add(deleteButton);
		 * RootPanel.get("Navigator").add(selectedUserPanel); } });
		 */
		// Befüllen der Suggestbox mit Inhalt. Am Ende sollen hier die
		// angemeldeten
		// User hinzugefügt werden z.B. mit oracle.addAll(user);
		// Kleines Beispiel:
		/*
		 * for (int i = 0; i < listOfUser.size(); i++) { String name = new
		 * String(listOfUser.get(i).getNickName()); oracle.add(name); }
		 */

		// addPanel.add(suggestBox);
		// addPanel.add(addButton);
		// navigation.add(addPanel);

		RootPanel.get("Navigator").add(navigation);

	}// Ende onLoad

	@Override
	protected String getHeadlineText() {
		// TODO Auto-generated method stub
		return null;
	}

}
