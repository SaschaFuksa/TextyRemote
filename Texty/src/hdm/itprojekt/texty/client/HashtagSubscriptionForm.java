package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.HashtagSubscription;

import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HashtagSubscriptionForm extends TextyForm {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	private VerticalPanel mainPanel = new VerticalPanel();
	private Vector<Hashtag> allSubscribedHashtag = new Vector<Hashtag>();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private InfoBox infoBox = new InfoBox();
	private Label text = new HTML(
			"To add new hashtags, use the searchfield on the left! <br> "
					+ "To delete subscriptions, click on the delete button next to your subscribed hashtag.");
	private Vector<Hashtag> allSelectedHashtag = new Vector<Hashtag>();
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public HashtagSubscriptionForm(String headline) {
		super(headline);
	}

	public HashtagSubscriptionForm(String headline,
			Vector<Hashtag> selectedHashtag) {
		super(headline);
		this.allSelectedHashtag = selectedHashtag;
	}

	@Override
	protected void run() {

		administration
				.getAllSubscribedHashtags(getAllSubscribedHashtagsExecute());

		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("hashtagForm");
		content.getElement().setId("fullWidth");
		scroll.getElement().setId("fullWidth");

		mainPanel.add(getHeadline());
		mainPanel.add(text);
		mainPanel.add(scroll);
		mainPanel.add(infoBox);
		
		this.add(mainPanel);
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
			}
		});

	}

	private AsyncCallback<Vector<Hashtag>> getAllSubscribedHashtagsExecute() {
		AsyncCallback<Vector<Hashtag>> asyncCallback = new AsyncCallback<Vector<Hashtag>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<Hashtag> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				allSubscribedHashtag = result;
				addHashtagSubscriptions();
				showSubscriptions();
			}
		};
		return asyncCallback;
	}

	private void addHashtagSubscriptions() {
		String result = new String("");
		String warning = new String("");
		for (Hashtag hashtag : allSelectedHashtag) {
			if (checkSubscription(hashtag)) {
				allSubscribedHashtag.add(hashtag);
				administration.createHashtagSubscription(hashtag,
						createHashtagSubscriptionExecute());
				result = result + " '" + hashtag.getKeyword()
						+ "'";
			} else {
				warning = warning + " '" + hashtag.getKeyword()
						+ "'";
			}
		}
		if (result != "") {
			infoBox.setSuccessText("Hashtag" + result
					+ " successful subscribed!");
		}
		if (warning != "") {
			infoBox.setWarningText("Hashtag" + warning
					+ " was already subscribed!");
		}

	}

	private AsyncCallback<HashtagSubscription> createHashtagSubscriptionExecute() {
		AsyncCallback<HashtagSubscription> asyncCallback = new AsyncCallback<HashtagSubscription>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(HashtagSubscription result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
			}
		};
		return asyncCallback;
	}

	private void showSubscriptions() {
		for (Hashtag hashtag : allSubscribedHashtag) {
			final Hashtag selectedHashtag = hashtag;
			final HorizontalPanel hashtagPanel = new HorizontalPanel();
			final Label keywordLabel = new Label("#" + hashtag.getKeyword());
			final Label removeButton = new Label("x");
			removeButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					deleteSubscription(selectedHashtag);
					content.remove(hashtagPanel);
				}
			});
			hashtagPanel.getElement().setId("selectedObjectLabel");
			removeButton.getElement().setId("removeButton");
			hashtagPanel.add(keywordLabel);
			hashtagPanel.add(removeButton);
			content.add(hashtagPanel);
		}
	}

	private void deleteSubscription(Hashtag hashtag) {
		for (Hashtag subscribedHashtag : allSubscribedHashtag) {
			if (hashtag.getId() == subscribedHashtag.getId()) {
				infoBox.clear();
				infoBox.setSuccessText("Subscribed hahstag '#"
						+ subscribedHashtag.getKeyword()
						+ "' sucessful removed!");
				allSubscribedHashtag.remove(subscribedHashtag);
				administration.deleteHashtagSubscription(
						subscribedHashtag, deleteHashtagSubscriptionExecute());
			}
		}
	}
	
	private AsyncCallback<Void> deleteHashtagSubscriptionExecute(){
		AsyncCallback<Void> asyncCallback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
			}
		};
		return asyncCallback;
	}

	private boolean checkSubscription(Hashtag hashtag) {
		for (Hashtag subscribedHashtag : allSubscribedHashtag) {
			if (hashtag.getId() == subscribedHashtag.getId()) {
				return false;
			}
		}
		return true;
	}

}
