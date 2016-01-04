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
			"Here you can see and select your subscribed Hashtags and User");
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

	// Detailsbereich
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);

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

	// Holt sich alle Messages des Hashtags und speichert sie in der
	// messageListofHashtag
	private void getMessageSelectedHashtag(Hashtag hashtag) {
		
	}

	// Hier entsteht die Detailsanzeige der User
	public void showMessageSelectedUser(User userView) {

		administration.getAllPublicConversationsFromUser(userView,
				new AsyncCallback<Vector<Conversation>>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Vector<Conversation> result) {
						HomeForm.conListofUser = result;


						for (Conversation conversation : conListofUser) {
							final Conversation conversationView = conversation;
							FlexTable chatFlexTable = new FlexTable();
							FocusPanel wrapper = new FocusPanel();
							Vector<Message> messageList = new Vector<Message>();

							String date = DateTimeFormat
									.getFormat("yyyy-MM-dd").format(
											conversationView.getLastMessage()
													.getDateOfCreation());
							Label dateLabel = new Label(date);
							messageList = conversationView.getListOfMessage();
							String text = conversationView.getLastMessage()
									.getText();
							text = "hallo";
							final Label textLabel = new Label(text);

							dateLabel.getElement().setId("conversationDate");
							// textLabel.getElement().setId("conversationBody");

							wrapper.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									
									RootPanel.get("Info").clear();
									RootPanel.get("Details").add(textLabel);
								}
							});

							chatFlexTable.getFlexCellFormatter().setColSpan(1,
									0, 2);
							chatFlexTable.getFlexCellFormatter().setColSpan(2,
									0, 2);

							// chatFlexTable.setWidget(0, 0, authorLabel);
							chatFlexTable.setWidget(0, 1, dateLabel);
							// chatFlexTable.setWidget(1, 0, receiverLabel);
							chatFlexTable.setWidget(2, 0, textLabel);

							wrapper.add(chatFlexTable);

							content.add(wrapper);
							RootPanel.get("Details").add(content);
							
						} // Ende for-Schleife
					}

				});

	}

	// Hier entsteht die Detailsanzeige der Hashtags
	public void showMessageSelectedHashtag(Hashtag hashtag) {
		administration.getAllPublicMessagesFromHashtag(hashtag,
				new AsyncCallback<Vector<Message>>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Vector<Message> result) {
						HomeForm.messageListofHashtag = result;
					}

				});

	}

}
