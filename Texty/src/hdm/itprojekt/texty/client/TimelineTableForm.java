package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

//TODO Public posting of (jeweiliger User)

public class TimelineTableForm extends TextyForm {

	public TimelineTableForm(String headline) {
		super(headline);

	}

	Button cancelButton = new Button("Cancel");

	private CellTable<Conversation> conversationTable = new CellTable<Conversation>();
	private Vector<User> userList = new Vector<User>();
	private Vector<Conversation> allPublicConversationList = new Vector<Conversation>();

	@Override
	public void run() {

		/*// Spalte fuer die Conversationliste der jeweiligen ausgewählten User
		Column<Conversation, String> postingColumn = new Column<Conversation, String>(
				new TextCell()) {
			//public String getValue(Conversation conversation) {
				//return conversation.getListOfMessage();
			}
		};
		conversationTable.addColumn(postingColumn, "Favorite User");*/
		

		// Beispieldaten User
		User u1 = new User();
		User u2 = new User();
		User u3 = new User();

		u1.setFirstName("Daniel");
		u2.setFirstName("Lisa");
		u3.setFirstName("Gandalf");

		userList.addElement(u1);
		userList.addElement(u2);
		userList.addElement(u3);

		// Neue CellTable mit Userobjekten wird erzeugt
		CellTable<User> cellTable = new CellTable<User>();

		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		cellTable.setWidth("100%", true);

		addTitleColumn(cellTable);

		// Add a selection model to handle user selection.
		final SingleSelectionModel<User> singleSelectionModel = new SingleSelectionModel<User>();

		cellTable.setSelectionModel(singleSelectionModel);
		singleSelectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						User user = singleSelectionModel.getSelectedObject();
						if (user != null) {
							Window.alert("Selected: " + user.getFirstName());

						}
					}
				});

		// Set the total row count. This isn't strictly necessary, but it
		// affects paging calculations, so its good habit to keep the row count
		// up to
		// date.
		cellTable.setRowCount(userList.size(), true);

		// Push the data into the widget.
		cellTable.setRowData(0, userList);

		RootPanel.get("Details").add(cellTable);
	}

	// Methode zum Festlegen der Spaltenüberschrift
	private void addTitleColumn(CellTable<User> cellTable) {
		TextColumn<User> titleColumn = new TextColumn<User>() {

			@Override
			public String getValue(User object) {
				return object.getFirstName();
			}

		};

		// Zwei Spalten hinzufügen
		cellTable.addColumn(titleColumn, "Public Posting");
		cellTable.addColumn(titleColumn, "Your comment");
	}

	/*
	 * private void addDepartmentColumn(CellTable<User> cellTable) {
	 * TextColumn<User> departmentColumn = new TextColumn<User>() {
	 * 
	 * @Override public String getValue(User object) { return
	 * object.getDepartment(); }
	 * 
	 * };
	 * 
	 * // Add column to table cellTable.addColumn(departmentColumn,
	 * "Department"); }
	 * 
	 * private void addLocationColumn(CellTable<User> cellTable) {
	 * TextColumn<User> locationColumn = new TextColumn<User>() {
	 * 
	 * @Override public String getValue(User object) { return
	 * object.getLocation(); }
	 * 
	 * };
	 */

	// Add column to table
	// cellTable.addColumn(locationColumn, "Location");
}
