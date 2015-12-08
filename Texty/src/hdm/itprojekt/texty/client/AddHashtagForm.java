package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.client.gui.TextyHandler;
import hdm.itprojekt.texty.shared.bo.Hashtag;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Diese Klasse beinhaltet die Vorlage für eine Vorschlagsbox, die die verfügbaren Hashtags anzeigt. 
 * 
 *
 */
public class AddHashtagForm extends Showcase{

	private HorizontalPanel selectedHashtagPanel = new HorizontalPanel();
	private Label selectedHashtagLabel = new Label();
	private Button deleteButton = new Button();

	//private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	//private SuggestBox suggestBox = new SuggestBox(oracle);
	private TextyHandler suggestHandler = new TextyHandler();

	private VerticalPanel navigation = new VerticalPanel();
	private HorizontalPanel addPanel = new HorizontalPanel();
	
	public void addHashtag(String text) {
		selectedHashtagLabel.setText(text);

		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RootPanel.get("Navigator").remove(selectedHashtagPanel);
			}
		});

		deleteButton.getElement().setId("deleteButton");

		selectedHashtagPanel.add(selectedHashtagLabel);
		selectedHashtagPanel.add(deleteButton);
		RootPanel.get("Navigator").add(selectedHashtagPanel);
	}

	/*private KeyUpHandler suggestBoxHandler = new KeyUpHandler() {
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				AddHashtagForm addSelectedHashtag = new AddHashtagForm();
				addSelectedHashtag.addHashtag(suggestBox.getText());
			}
		}
	};

	private Button addButton = new Button("", new ClickHandler() {
		public void onClick(ClickEvent event) {
			AddHashtagForm addSelectedHashtag = new AddHashtagForm();
			addSelectedHashtag.addHashtag(suggestBox.getText());
		}
	});*/

	/*public void checkHandler() {
		if (!suggestHandler.isApplicability()) {
			suggestBox.addKeyUpHandler(suggestBoxHandler);
			suggestHandler.setApplicability(true);
		}

	}*/

	@Override
	protected String getHeadlineText() {
		// TODO Auto-generated method stub
		return "Add Hashtag";
	}

	// Wird automatisch bei der Objekterzeugung ausgeführt
	protected void run() {

		//checkHandler();

		// Example Hashtags
		Hashtag hashtag1 = new Hashtag("VfB");
		Hashtag hashtag2 = new Hashtag("BVBaeh");
		Hashtag hashtag3 = new Hashtag("FCB");
		Hashtag hashtag4 = new Hashtag("VfBohWeh");
		Hashtag hashtag5 = new Hashtag("FCBaeh");
		Hashtag hashtag6 = new Hashtag("Vereinslos");

		hashtag1.setId(1);
		hashtag2.setId(2);
		hashtag3.setId(3);
		hashtag4.setId(4);
		hashtag5.setId(5);
		hashtag6.setId(6);

		Vector<Hashtag> listOfHashtag = new Vector<Hashtag>();
		listOfHashtag.add(hashtag1);
		listOfHashtag.add(hashtag2);
		listOfHashtag.add(hashtag3);
		listOfHashtag.add(hashtag4);
		listOfHashtag.add(hashtag5);
		listOfHashtag.add(hashtag6);

		//addButton.getElement().setId("addButton");

		// Befüllen der Suggestbox mit Inhalt. Am Ende sollen hier die
		// angemeldeten
		// User hinzugefügt werden z.B. mit oracle.addAll(user);
		// Kleines Beispiel:
		/*for (int i = 0; i < listOfHashtag.size(); i++) {
			String keyword = new String(listOfHashtag.get(i).getKeyword());
			oracle.add(keyword);
		}*/

		//addPanel.add(suggestBox);
		//addPanel.add(addButton);
		navigation.add(addPanel);
		RootPanel.get("Navigator").add(navigation);
		
	}

}
