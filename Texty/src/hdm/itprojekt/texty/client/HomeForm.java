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
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HomeForm extends TextyForm {

	private static Vector<User> userList = new Vector<User>();
	private static Vector<Hashtag> hashtagList = new Vector<Hashtag>();
	private static Vector<Conversation> conListofUser = new Vector<Conversation>();
	private static Vector<Message> messageListofHashtag = new Vector<Message>();
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label intro = new Label(
			"Here you can see and select your subscribed User and Hashtags");
	private VerticalPanel contentUser = new VerticalPanel();
	private VerticalPanel userPanel = new VerticalPanel();
	private ScrollPanel scrollUser = new ScrollPanel(contentUser);
	private VerticalPanel contentHashtag = new VerticalPanel();
	private VerticalPanel hashtagPanel = new VerticalPanel();
	private ScrollPanel scrollHashtag = new ScrollPanel(contentHashtag);
	private Vector<String> userVector = new Vector<String>();
	private Vector<String> hashtagVector = new Vector<String>();
	private TextCell textCell = new TextCell();
	private CellList<String> cellListUser = new CellList<String>(textCell);
	private CellList<String> cellListHashtag = new CellList<String>(textCell);
	private NewMessage messageForm = new NewMessage("New Message");

	// Detailsbereich für Anzeige der Conversation
	private VerticalPanel contentConversation = new VerticalPanel();
	private ScrollPanel scrollConversation = new ScrollPanel(contentConversation);

	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public HomeForm(String headline) {
		super(headline);
	}

	@Override
	protected void run() {

		// Abonnierte User und deren Postings
		administration.getAllSubscribedUsers(new AsyncCallback<Vector<User>>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Vector<User> result) {
				HomeForm.userList = result;
				showSubscribedUser();

				// Abonnierte Hashtags und deren Postings
				administration
						.getAllSubscribedHashtags(new AsyncCallback<Vector<Hashtag>>() {
							@Override
							public void onFailure(Throwable caught) {

							}

							@Override
							public void onSuccess(Vector<Hashtag> result) {
								HomeForm.hashtagList = result;
								showSubscribedHashtag();
							}

						});
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
		mainPanel.add(intro);
		mainPanel.add(userPanel);
		mainPanel.add(hashtagPanel);
		mainPanel.add(contentConversation);

		this.add(mainPanel);

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scrollUser.setHeight(mainPanel.getOffsetHeight() / 3 + "px");
				scrollHashtag.setHeight(mainPanel.getOffsetHeight() / 3 + "px");
			}
		});

	}// Ende Run-Methode

	// Navigatorbereich User
	private void showSubscribedUser() {

		for (User user : userList) {
			final User userView = user;
			FlexTable chatFlexTable = new FlexTable();
			FocusPanel wrapper = new FocusPanel();

			String userName = userView.getFirstName() + " "
					+ userView.getLastName();

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

	}

	// Navigatorbereich Hashtags
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

	// Hier entsteht die Detailsanzeige der User
	public void showMessageSelectedUser(final User user) {
		
		administration.getAllPublicConversationsFromUser(user,
				new AsyncCallback<Vector<Conversation>>() {

					@Override
					public void onFailure(Throwable caught) {
						
					}

					@Override
					public void onSuccess(Vector<Conversation> result) {						
						TextyForm publicConversationViewer = new PublicConversationViewer("Public Postings from " + user.getFirstName(), result);
						
						RootPanel.get("Details").clear();
						RootPanel.get("Details").add(publicConversationViewer);
						
					}

				});

	}

	// Hier entsteht die Detailsanzeige der Hashtags
	public void showMessageSelectedHashtag(final Hashtag hashtag) {
		
		administration.getAllPublicMessagesFromHashtag(hashtag,
				new AsyncCallback<Vector<Message>>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Vector<Message> result) {
						TextyForm publicHashtagViewer = new PublicHashtagViewer("Public Hashtags of " + hashtag.getKeyword(), result);
						
						RootPanel.get("Details").clear();
						RootPanel.get("Details").add(publicHashtagViewer);
					}

				});

	}

}
