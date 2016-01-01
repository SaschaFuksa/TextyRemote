package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.HashtagSubscription;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HashtagSubscriptionForm extends TextyForm {

	private static Vector<Hashtag> subscribedHashtag = new Vector<Hashtag>();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label intro = new HTML(
			"To add new hashtags, use the searchfield on the left! <br> "
					+ "To delete subscriptions, click on the delete button next to your subscribed hashtag.");
	private Label errorLabel = new Label("\0");
	private Label warningLabel = new Label("");
	private Label successLabel = new Label("");
	private Vector<Hashtag> selectedHashtag = new Vector<Hashtag>();
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public HashtagSubscriptionForm(String headline) {
		super(headline);
	}

	public HashtagSubscriptionForm(String headline,
			Vector<Hashtag> selectedHashtag) {
		super(headline);
		this.selectedHashtag = selectedHashtag;
	}

	public void addHashtagSubscriptions() {
		String result = new String("");
		String warning = new String("");
		for (int i = 0; i < selectedHashtag.size(); i++) {
			if (checkSubscription(selectedHashtag.get(i).getKeyword())) {
				subscribedHashtag.add(selectedHashtag.get(i));
				administration.createHashtagSubscription(
						selectedHashtag.get(i),
						new AsyncCallback<HashtagSubscription>() {
							@Override
							public void onFailure(Throwable caught) {

							}

							@Override
							public void onSuccess(HashtagSubscription result) {

							}
						});
				result = result + " '" + selectedHashtag.get(i).getKeyword()
						+ "'";
			} else {
				warning = warning + " '" + selectedHashtag.get(i).getKeyword()
						+ "'";
			}
		}
		if (result != "") {
			successLabel
					.setText("Hashtag" + result + " successful subscribed!");
		}
		if (warning != "") {
			warningLabel.setText("Hashtag" + warning
					+ " is already subscribed!");
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
				successLabel.setText("Subscribed user '#"
						+ subscribedHashtag.get(i).getKeyword()
						+ "' sucessful removed!");
				warningLabel.setText("");
				administration.deleteHashtagSubscription(
						subscribedHashtag.get(i), new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {

							}

							@Override
							public void onSuccess(Void result) {

							}
						});
			}
		}

		subscribedHashtag.remove(indexSelectedHashtag);

	}

	@Override
	protected void run() {

		administration
				.getAllSubscribedHashtags(new AsyncCallback<Vector<Hashtag>>() {
					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Vector<Hashtag> result) {
						HashtagSubscriptionForm.subscribedHashtag = result;
						addHashtagSubscriptions();
						showSubscriptions();

					}
				});

		warningLabel.setStylePrimaryName("errorLabel");
		successLabel.setStylePrimaryName("successLabel");
		scroll.setSize("250px", "110px");

		this.add(getHeadline());
		this.add(intro);
		this.add(errorLabel);
		this.add(scroll);
		this.add(warningLabel);
		this.add(successLabel);

	}

	public void showSubscriptions() {
		for (int i = 0; i < subscribedHashtag.size(); i++) {
			final HorizontalPanel panel = new HorizontalPanel();
			final Label keywordLabel = new Label("#"
					+ subscribedHashtag.get(i).getKeyword());
			keywordLabel.setStylePrimaryName("selectedObjectLabel");
			final Button deleteButton = new Button("", new ClickHandler() {
				@Override
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

}
