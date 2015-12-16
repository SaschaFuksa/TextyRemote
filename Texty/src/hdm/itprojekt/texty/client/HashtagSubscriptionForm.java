package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.HashtagSubscription;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HashtagSubscriptionForm extends TextyForm {

	public HashtagSubscriptionForm(String headline) {
		super(headline);
	}
	
	public HashtagSubscriptionForm(String headline, Vector<Hashtag> selectedHashtag) {
		super(headline);
		this.selectedHashtag = selectedHashtag;
	}

	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label intro = new HTML(
			"To add new hashtags, use the searchfield on the left! <br> "
					+ "To delete subscriptions, click on the delete button next to your subscribed hashtag.");
	private Label errorLabel = new Label("\0");
	private Label warningLabel = new Label("");
	private Label successLabel = new Label("");
	private Vector<Hashtag> selectedHashtag = new Vector<Hashtag>();
	private Vector<Hashtag> subscribedHashtag = new Vector<Hashtag>();
	private TextyAdministrationAsync administration = ClientsideSettings.getTextyAdministration();

	public void addHashtagSubscriptions() {
		String result = new String("");
		String warning = new String("");
		for (int i = 0; i < selectedHashtag.size(); i++) {
			if (checkSubscription(selectedHashtag.get(i).getKeyword())) {
				subscribedHashtag.add(selectedHashtag.get(i));
				administration.createHashtagSubscription(selectedHashtag.get(i), new createHashtagSubscriptionCallback());
				result = result + " '" + selectedHashtag.get(i).getKeyword() + "'";
			} else {
				warning = warning + " '" + selectedHashtag.get(i).getKeyword() + "'";
			}
		}
		if (result != "") {
			successLabel.setText("Hashtag" + result + " successful subscribed!");
		}
		if (warning != "") {
			warningLabel.setText("Hashtag" + warning + " is already subscribed!");
		}

	}

	public boolean checkSubscription(String keyword) {
		String word = keyword;
		for (int i = 0; i < subscribedHashtag.size(); i++) {
			if (word.equals(subscribedHashtag.get(i).getKeyword())) {
				return false;
			}
		}
		return true;
	}

	private void deleteSubscription(String keyword) {
		StringBuffer word = new StringBuffer(keyword);
		word.deleteCharAt(0);
		int indexSelectedHashtag = 0;
		for (int i = 0; i < subscribedHashtag.size(); i++) {
			if (word.toString().equals(subscribedHashtag.get(i).getKeyword())) {
				indexSelectedHashtag = i;
				successLabel.setText("Subscribed user '#" + subscribedHashtag.get(i).getKeyword() + "' sucessful removed!");
				warningLabel.setText("");
			}
		}

		subscribedHashtag.remove(indexSelectedHashtag);

	}

	public void showSubscriptions() {
		for (int i = 0; i < subscribedHashtag.size(); i++) {
			final HorizontalPanel panel = new HorizontalPanel();
			final Label keywordLabel = new Label("#" + subscribedHashtag.get(i)
					.getKeyword());
			keywordLabel.setStylePrimaryName("selectedObjectLabel");
			final Button deleteButton = new Button("", new ClickHandler() {
				public void onClick(ClickEvent event) {
					deleteSubscription(keywordLabel.getText());
					content.remove(panel);
				}

			});
			deleteButton.getElement().setId("deleteButton");
			panel.add(keywordLabel);
			panel.add(deleteButton);
			content.add(panel);
		}
	}

	protected void run() {
		
		Hashtag hashtag1 = new Hashtag("VfB Abstieg");
		Hashtag hashtag2 = new Hashtag("Texty");

		hashtag1.setId(1);
		hashtag2.setId(2);

		subscribedHashtag.add(hashtag1);
		subscribedHashtag.add(hashtag2);

		addHashtagSubscriptions();

		showSubscriptions();

		warningLabel.setStylePrimaryName("errorLabel");
		successLabel.setStylePrimaryName("successLabel");
		scroll.setSize("250px", "110px");

		this.add(intro);
		this.add(errorLabel);
		this.add(scroll);
		this.add(warningLabel);
		this.add(successLabel);

	}

}

class createHashtagSubscriptionCallback implements AsyncCallback<HashtagSubscription> {

	public void onFailure(Throwable caught) {
		Window.alert("FAILURE");
	}

	public void onSuccess(HashtagSubscription subscription) {
		
	}
}
