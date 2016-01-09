package hdm.itprojekt.texty.client;

import java.util.Vector;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * Diese Klasse stellt die abonnierten Hashtags und User in zwei verschiedenen
 * ScrollBars im Navigatorbereich dar. 
 *
 */
public class HomeForm extends TextyForm {

	private static Vector<User> userList = new Vector<User>();
	private static Vector<Hashtag> hashtagList = new Vector<Hashtag>();
	private static Vector<Conversation> conListofUser = new Vector<Conversation>();
	private static Vector<Message> messageListofHashtag = new Vector<Message>();
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label text = new Label("See your:");
	private Label headerUser = new Label("Subscribed user");
	private Label headerHashtag = new Label("Subscribed hashtags");
	private VerticalPanel contentUser = new VerticalPanel();
	private VerticalPanel userPanel = new VerticalPanel();
	private ScrollPanel scrollUser = new ScrollPanel(contentUser);
	private VerticalPanel contentHashtag = new VerticalPanel();
	private VerticalPanel hashtagPanel = new VerticalPanel();
	private ScrollPanel scrollHashtag = new ScrollPanel(contentHashtag);
	private Vector<String> userVector = new Vector<String>();
	private Vector<String> hashtagVector = new Vector<String>();
	private User currentUser = new User();
	private TextCell textCell = new TextCell();
	private CellList<String> cellListUser = new CellList<String>(textCell);
	private CellList<String> cellListHashtag = new CellList<String>(textCell);

	// Detailsbereich für Anzeige der Conversation
	private VerticalPanel contentConversation = new VerticalPanel();
	private ScrollPanel scrollConversation = new ScrollPanel(
			contentConversation);

	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public HomeForm(String headline) {
		super(headline);
	}

	/**
	 * Diese Methode holt sich alle abonnierten User und Hashtags und speichert
	 * sie in den Vectoren userList und hashtagList
	 */
	@Override
	protected void run() {

		// Abonnierte User
		administration.getAllSubscribedUsers(new AsyncCallback<Vector<User>>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Vector<User> result) {
				userList = result;
				showSubscribedUser();

				// Abonnierte Hashtags 
				administration
						.getAllSubscribedHashtags(new AsyncCallback<Vector<Hashtag>>() {
							@Override
							public void onFailure(Throwable caught) {

							}

							@Override
							public void onSuccess(Vector<Hashtag> result) {
								hashtagList = result;
								showSubscribedHashtag();
								if (result.size() == 0) {
									headerHashtag.setText("");
								}
							}

						});
			}

		});

		/**
		 * Diese Methode gibt den aktuell eingeloggten User zurück
		 */
		administration.getCurrentUser(new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(User result) {
				currentUser = result;
			}

		});

		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("fullSize");
		userPanel.getElement().setId("publicConversation");
		scrollUser.getElement().setId("conversationScroll");
		contentUser.getElement().setId("conversationContent");
		hashtagPanel.getElement().setId("publicConversation");
		scrollHashtag.getElement().setId("conversationScroll");
		contentHashtag.getElement().setId("conversationContent");

		contentUser.add(cellListUser);
		contentHashtag.add(cellListHashtag);
		userPanel.add(scrollUser);
		hashtagPanel.add(scrollHashtag);

		mainPanel.add(getHeadline());
		mainPanel.add(text);
		mainPanel.add(headerUser);
		mainPanel.add(userPanel);
		mainPanel.add(headerHashtag);
		mainPanel.add(hashtagPanel);

		this.add(mainPanel);

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scrollUser.setHeight(mainPanel.getOffsetHeight() * 5 / 16
						+ "px");
				scrollHashtag.setHeight(mainPanel.getOffsetHeight() * 5 / 16
						+ "px");
			}
		});

	}// Ende Run-Methode

	/**
	 * Diese Methode wählt in einem Schleifendurchlauf alle beinhalteten User an
	 * und bezieht dessen Vor -und Nachname, nachdem jeder User an die Methode createUserTable übergeben wurden. 
	 * Des weiteren kann man auch seine persönliche Chronik aller öffentlichen Messages einsehen.
	 */
	private void showSubscribedUser() {

		createUserTable(currentUser, true);

		for (User user : userList) {
			createUserTable(user, false);
		}

	}

	private void createUserTable(User user, boolean isCurrentUser) {
		final User userView = user;
		FlexTable chatFlexTable = new FlexTable();
		FocusPanel wrapper = new FocusPanel();
		String userName = new String();

		if (isCurrentUser) {
			userName = "Your own public postings";
		} else {
			userName = userView.getFirstName() + " " + userView.getLastName();
		}

		Label userLabel = new Label(userName);
		userLabel.getElement().setId("conversationHead");

		wrapper.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				showMessageSelectedUser(userView);

			}
		});

		chatFlexTable.setWidget(0, 0, userLabel);
		chatFlexTable.getElement().setId("conversation");
		wrapper.add(chatFlexTable);
		contentUser.add(wrapper);
	}

	/**
	 * Diese Methode wählt in einem Schleifendurchlauf alle beinhalteten
	 * Hashtags an und bezieht dessen Keywords, die auf einem Label platziert
	 * werden
	 */
	private void showSubscribedHashtag() {

		for (Hashtag hashtag : hashtagList) {
			final Hashtag hashtagView = hashtag;
			FlexTable chatFlexTable = new FlexTable();
			FocusPanel wrapper = new FocusPanel();

			String keyword = hashtagView.getKeyword();
			Label hashtagLabel = new Label(keyword);
			hashtagLabel.getElement().setId("conversationHead");
			wrapper.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					showMessageSelectedHashtag(hashtagView);
				}
			});

			chatFlexTable.setWidget(0, 0, hashtagLabel);
			chatFlexTable.getElement().setId("conversation");
			wrapper.add(chatFlexTable);
			contentHashtag.add(wrapper);
		}

	}

	/**
	 * Hier entsteht die Anzeige der User im Detailsbereich. Der angewählte User
	 * wird, nachdem mir von der Applikations-Ebene dessen Conversations
	 * geliefert wurden, an die Klasse PublicConversationViewer übergeben
	 * 
	 */
	public void showMessageSelectedUser(final User user) {

		administration.getAllPublicConversationsFromUser(user,
				new AsyncCallback<Vector<Conversation>>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Vector<Conversation> result) {
						TextyForm publicConversationViewer = new PublicConversationViewer(
								"Public Postings from " + user.getFirstName(),
								result);

						RootPanel.get("Details").clear();
						RootPanel.get("Details").add(publicConversationViewer);

					}

				});

	}

	/**
	 * Hier entsteht die Anzeige der Hashtags im Detailsbereich. Der angewählte
	 * Hashtag wird an die Klasse PublicHashtagViewer übergeben.
	 * 
	 */
	// Hier entsteht die Detailsanzeige der Hashtags
	public void showMessageSelectedHashtag(final Hashtag hashtag) {

		administration.getAllPublicMessagesFromHashtag(hashtag,
				new AsyncCallback<Vector<Message>>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Vector<Message> result) {
						TextyForm publicHashtagViewer = new PublicHashtagViewer(
								"Public Hashtags of " + hashtag.getKeyword(),
								result);

						RootPanel.get("Details").clear();
						RootPanel.get("Details").add(publicHashtagViewer);
					}

				});

	}

}
