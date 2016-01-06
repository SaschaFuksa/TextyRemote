package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SubscriptionForm extends TextyForm {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	private VerticalPanel mainPanel = new VerticalPanel();
	private Vector<Hashtag> allSubscribedHashtag = new Vector<Hashtag>();
	private Vector<User> allSubscribedUser = new Vector<User>();
	private FlexTable subscriptionFormFlexTable = new FlexTable();
	private VerticalPanel contentUserSubscriptions = new VerticalPanel();
	private VerticalPanel contentHashtagSubscriptions = new VerticalPanel();
	private ScrollPanel scrollUserSubscriptions = new ScrollPanel(
			contentUserSubscriptions);
	private ScrollPanel scrollHashtagSubscriptions = new ScrollPanel(
			contentHashtagSubscriptions);
	private InfoBox infoBox = new InfoBox();
	private Label text = new Label(
			"To delete subscriptions, click on the delete button next to your subscription.");
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public SubscriptionForm(String headline) {
		super(headline);
	}

	@Override
	protected void run() {

		getSubscriptions();

		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("subscriptionForm");
		subscriptionFormFlexTable.getElement().setId("fullSize");
		contentUserSubscriptions.getElement().setId("fullWidth");
		contentHashtagSubscriptions.getElement().setId("fullWidth");
		scrollUserSubscriptions.getElement().setId("fullWidth");
		scrollHashtagSubscriptions.getElement().setId("fullWidth");
		text.getElement().setId("blackFont");
		subscriptionFormFlexTable.getColumnFormatter().addStyleName(0,
				"subscriptionFlexTableCell");
		subscriptionFormFlexTable.getColumnFormatter().addStyleName(1,
				"subscriptionFlexTableCell");

		subscriptionFormFlexTable.setText(0, 0, "");
		subscriptionFormFlexTable.setText(0, 1, "");
		subscriptionFormFlexTable.setWidget(1, 0, scrollUserSubscriptions);
		subscriptionFormFlexTable.setWidget(1, 1, scrollHashtagSubscriptions);

		mainPanel.add(getHeadline());
		mainPanel.add(text);
		mainPanel.add(subscriptionFormFlexTable);
		mainPanel.add(infoBox);

		this.add(mainPanel);

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				int height = mainPanel.getOffsetHeight();
				scrollUserSubscriptions.setHeight(height + "px");
				scrollHashtagSubscriptions.setHeight(height + "px");
			}
		});
	}

	private void getSubscriptions() {
		administration.getAllSubscribedUsers(getAllSubscribedUsersExecute());
	}

	private AsyncCallback<Vector<User>> getAllSubscribedUsersExecute() {
		AsyncCallback<Vector<User>> asyncCallback = new AsyncCallback<Vector<User>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<User> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				if (result.size() > 0) {
					subscriptionFormFlexTable.setText(0, 0,
							"User Subscriptions");
				}
				allSubscribedUser = result;
				showUserSubscriptions();
				administration
						.getAllSubscribedHashtags(getAllSubscribedHashtagsExecute());
			}
		};
		return asyncCallback;
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
				if (result.size() > 0) {
					subscriptionFormFlexTable.setText(0, 1,
							"Hashtag Subscriptions");
				}
				allSubscribedHashtag = result;
				if (allSubscribedUser.size() == 0
						&& allSubscribedHashtag.size() == 0) {
					infoBox.setInfoText("You don't have any subscriptions. To subscripe user or hashtags, please use your searchbar on the right and left side!");
				}
				showHashtagSubscriptions();
			}
		};
		return asyncCallback;
	}

	private void showUserSubscriptions() {
		if (RootPanel.get("Navigator").getWidgetCount() == 0) {
			TextyForm userForm = new UserForm("User", allSubscribedUser);
			RootPanel.get("Navigator").add(userForm);
		}
		for (User user : allSubscribedUser) {
			final User selectedUser = user;
			final HorizontalPanel userPanel = new HorizontalPanel();
			final Label nameLabel = new Label(selectedUser.getFirstName());
			final Label removeButton = new Label("x");
			removeButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					deleteUserSubscription(selectedUser);
					contentUserSubscriptions.remove(userPanel);
				}
			});
			userPanel.getElement().setId("selectedObjectLabel");
			removeButton.getElement().setId("removeButton");
			userPanel.add(nameLabel);
			userPanel.add(removeButton);
			contentUserSubscriptions.add(userPanel);
		}
	}

	private void showHashtagSubscriptions() {
		if (RootPanel.get("Info").getWidgetCount() == 0) {
			TextyForm hashtagForm = new HashtagForm("Hashtags",
					allSubscribedHashtag);
			RootPanel.get("Info").add(hashtagForm);
		}
		for (Hashtag hashtag : allSubscribedHashtag) {
			final Hashtag selectedHashtag = hashtag;
			final HorizontalPanel hashtagPanel = new HorizontalPanel();
			final Label keywordLabel = new Label("#"
					+ selectedHashtag.getKeyword());
			final Label removeButton = new Label("x");
			removeButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					deleteHashtagSubscription(selectedHashtag);
					contentHashtagSubscriptions.remove(hashtagPanel);
				}
			});
			hashtagPanel.getElement().setId("selectedObjectLabel");
			removeButton.getElement().setId("removeButton");
			hashtagPanel.add(keywordLabel);
			hashtagPanel.add(removeButton);
			contentHashtagSubscriptions.add(hashtagPanel);
		}
	}

	private void deleteUserSubscription(User user) {
		for (User subscribedUser : allSubscribedUser) {
			if (user.getId() == subscribedUser.getId()) {
				infoBox.clear();
				allSubscribedUser.remove(user);
				if (allSubscribedUser.size() == 0) {
					subscriptionFormFlexTable.setText(0, 0, "");
				}
				if (allSubscribedHashtag.size() == 0
						&& allSubscribedUser.size() == 0) {
					infoBox.setInfoText("You deleted all your subscriptions. To add new subscriptions, select hashtags or user on the right or left side!");
				}
				administration.deleteUserSubscription(subscribedUser,
						deleteUserSubscriptionExecute());
			}
		}

	}

	private AsyncCallback<Void> deleteUserSubscriptionExecute() {
		AsyncCallback<Void> asyncCallback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void nothing) {
				RootPanel.get("Navigator").clear();
				TextyForm userForm = new UserForm("User", allSubscribedUser);
				RootPanel.get("Navigator").add(userForm);
			}
		};
		return asyncCallback;
	}

	private void deleteHashtagSubscription(Hashtag hashtag) {
		for (Hashtag subscribedHashtag : allSubscribedHashtag) {
			if (hashtag.getId() == subscribedHashtag.getId()) {
				infoBox.clear();
				allSubscribedHashtag.remove(subscribedHashtag);
				if (allSubscribedHashtag.size() == 0) {
					subscriptionFormFlexTable.setText(0, 1, "");
				}
				if (allSubscribedHashtag.size() == 0
						&& allSubscribedUser.size() == 0) {
					infoBox.setInfoText("You deleted all your subscriptions. To add new subscriptions, select hashtags or user on the right or left side!");
				}
				administration.deleteHashtagSubscription(subscribedHashtag,
						deleteHashtagSubscriptionExecute());
			}
		}
	}

	private AsyncCallback<Void> deleteHashtagSubscriptionExecute() {
		AsyncCallback<Void> asyncCallback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void nothing) {
				RootPanel.get("Info").clear();
				TextyForm hashtagForm = new HashtagForm("Hashtags",
						allSubscribedHashtag);
				RootPanel.get("Info").add(hashtagForm);
			}
		};
		return asyncCallback;
	}

}
