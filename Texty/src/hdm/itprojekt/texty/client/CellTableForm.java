package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;


public class CellTableForm extends TextyForm {

	public CellTableForm(String headline) {
		super(headline);

	}

	private Vector<User> userList = new Vector<User>();
	private Vector<Conversation> allPublicConversationList = new Vector<Conversation>();


	public void run() {
		
		User u1 = new User();

		CellTable<User> cellTable = new CellTable<User>();
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		addTitleColumn(cellTable);
		// addDepartmentColumn(cellTable);
		// addLocationColumn(cellTable);

		// Add a selection model to handle user selection.
		final SingleSelectionModel<User> singleSelectionModel = new SingleSelectionModel<User>();
		
		cellTable.setSelectionModel(singleSelectionModel);
		singleSelectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						User user = singleSelectionModel.getSelectedObject();
						if (user != null) {
							Window.alert("Selected: " + user.getFirstName());
							
						}
					}
				});

		// Set the total row count. This isn't strictly necessary, but it
		// affects paging calculations, so its good habit to keep the row count up to
		// date.
		cellTable.setRowCount(userList.size(), true);

		// Push the data into the widget.
		cellTable.setRowData(0, userList);

		RootPanel.get("Details").add(cellTable);
	}

	private void addTitleColumn(CellTable<User> cellTable) {
		TextColumn<User> titleColumn = new TextColumn<User>() {

			@Override
			public String getValue(User object) {
				return object.getFirstName();
			}

		};

		// Add column to table
		
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
