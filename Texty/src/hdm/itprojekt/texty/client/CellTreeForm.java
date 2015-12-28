package hdm.itprojekt.texty.client;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

public class CellTreeForm extends TextyForm {

	/**
	 * The model that defines the nodes in the tree.
	 */
	private static class CustomTreeModel implements TreeViewModel {

		/**
		 * Get the {@link NodeInfo} that provides the children of the specified
		 * value.
		 */
		@Override
		public <T> NodeInfo<?> getNodeInfo(T value) {
			/*
			 * Create some data in a data provider. Use the parent value as a
			 * prefix for the next level.
			 */
			// Befüllen des DataProvider mit den Conversations
			ListDataProvider<String> dataProvider = new ListDataProvider<String>();
			for (int i = 0; i < 5; i++) {
				dataProvider.getList().add(value + "." + String.valueOf(i));
			}

			// Return a node info that pairs the data with a cell.
			return new DefaultNodeInfo<String>(dataProvider, new TextCell());
		}

		/**
		 * Check if the specified value represents a leaf node. Leaf nodes
		 * cannot be opened.
		 */
		@Override
		public boolean isLeaf(Object value) {
			// The maximum length of a value is ten characters.
			return value.toString().length() > 10;
		}
	}

	public CellTreeForm(String headline) {
		super(headline);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void run() {

		// Create a model for the tree.
		TreeViewModel model = new CustomTreeModel();

		/*
		 * Create the tree using the model. We specify the default value of the
		 * hidden root node as "Item 1".
		 */
		CellTree tree = new CellTree(model, "Item 1");

		// Add the tree to the root layout panel.
		RootPanel.get("Details").add(tree);
	}

}
