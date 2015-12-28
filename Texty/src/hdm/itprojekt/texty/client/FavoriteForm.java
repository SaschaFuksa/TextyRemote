package hdm.itprojekt.texty.client;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * 
 * Diese Klasse zeigt die abonnierten Inhalte (User und Hashtags) in einer
 * CellList an TODO
 */
public class FavoriteForm extends TextyForm {

	private static final List<String> user = Arrays.asList("Erich",
			"GandalfDerSchwarze", "Saruman", "Homer", "Eumel",
			"ObamaDerWeisse", "Gundula Gause", "---------- ");

	private static final List<String> hashtags = Arrays.asList("VfB", "BVB",
			"FCB", "Pizza", "SaureKutteln");

	private Timeline timeLine = new Timeline("Timeline");

	public FavoriteForm(String headline) {
		super(headline);
	}

	@Override
	protected void run() {

		// Sorry ;-) Code-Clones müssen natürlich noch behoben werden
		TextCell textCell = new TextCell();

		// Create a CellList that uses the cell.
		CellList<String> cellListUser = new CellList<String>(textCell);
		CellList<String> cellListHashtags = new CellList<String>(textCell);

		cellListUser
				.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		cellListHashtags
				.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		// Add a selection model to handle user selection.
		final SingleSelectionModel<String> selectionModelUser = new SingleSelectionModel<String>();
		cellListUser.setSelectionModel(selectionModelUser);
		selectionModelUser
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						String selected = selectionModelUser
								.getSelectedObject();
						if (selected != null) {
							RootPanel.get("Details").clear();
							timeLine.run();
						}
					}
				});

		// Add a selection model to handle user selection.
		final SingleSelectionModel<String> selectionModelHashtags = new SingleSelectionModel<String>();
		cellListHashtags.setSelectionModel(selectionModelHashtags);
		selectionModelHashtags
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						String selected = selectionModelHashtags
								.getSelectedObject();
						if (selected != null) {
							RootPanel.get("Details").clear();
							timeLine.run();
						}
					}
				});

		// Set the total row count. This isn't strictly necessary, but it
		// affects
		// paging calculations, so its good habit to keep the row count up to
		// date.
		cellListUser.setRowCount(user.size(), true);
		cellListHashtags.setRowCount(hashtags.size(), true);

		// Push the data into the widget.
		cellListUser.setRowData(0, user);
		cellListHashtags.setRowData(0, hashtags);

		// Add it to the root panel.
		VerticalPanel v1 = new VerticalPanel();
		VerticalPanel v2 = new VerticalPanel();

		v1.add(cellListUser);
		v2.add(cellListHashtags);

		RootPanel.get("Navigator").add(v1);
		RootPanel.get("Navigator").add(v2);

	}

}
