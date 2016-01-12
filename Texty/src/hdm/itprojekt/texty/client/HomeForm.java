package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Collections;
import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

	/**
	 * Der LOG gibt eine mögliche Exception bzw. den Erfolg des asynchronen
	 * Callbacks aus.
	 */
	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	private Vector<User> userList = new Vector<User>();
	private Vector<Hashtag> hashtagList = new Vector<Hashtag>();
	private User currentUser = new User();
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label text = new Label("See your:");
	private Label headerUser = new Label("Subscribed user");
	private Label headerHashtag = new Label("Subscribed hashtags");
	private VerticalPanel contentUser = new VerticalPanel();
	private VerticalPanel contentHashtag = new VerticalPanel();
	private ScrollPanel scrollUser = new ScrollPanel(contentUser);
	private ScrollPanel scrollHashtag = new ScrollPanel(contentHashtag);
	private FlexTable cellListUser = new FlexTable();
	private FlexTable cellListHashtag = new FlexTable();

	/**
	 * administration ermöglicht die asynchrone Kommunikation mit der
	 * Applikationslogik.
	 */
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	/**
	 *
	 * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular.
	 * 
	 * @param headline
	 */
	public HomeForm(String headline) {
		super(headline);
	}

	/**
	 * Modifiziert den Standardkonstruktor, um die run() Operation bei der
	 * Initialisierung aufzurufen.
	 * 
	 * @see TextyForm
	 */
	@Override
	protected void run() {
		

		// Abonnierte User und deren Postings
		administration.getAllSubscribedUsers(getAllSubscribedUsersExecute());

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("conversationWrapper");
		scrollUser.getElement().setId("conversationScroll");
		contentUser.getElement().setId("conversationContent");
		scrollHashtag.getElement().setId("conversationScroll");
		contentHashtag.getElement().setId("conversationContent");

		contentUser.add(cellListUser);
		contentHashtag.add(cellListHashtag);

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		mainPanel.add(getHeadline());
		mainPanel.add(text);
		mainPanel.add(headerUser);
		mainPanel.add(scrollUser);
		mainPanel.add(headerHashtag);
		mainPanel.add(scrollHashtag);

		this.add(mainPanel);

		/*
		 * Nachdem das Formular aufgebaut ist, wird die Höhe des jeweiligen
		 * Panels ausgelesen und als Höhe der Scrollbars gesetzt.
		 */
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scrollUser.setHeight(mainPanel.getOffsetHeight() * 0.45 + "px");
				scrollHashtag.setHeight(mainPanel.getOffsetHeight() * 0.45
						+ "px");
			}
		});

	}// Ende Run-Methode

	/**
	 * Diese Methode holt sich alle abonnierten User
	 * 
	 * @return
	 */
	private AsyncCallback<Vector<User>> getAllSubscribedUsersExecute() {
		AsyncCallback<Vector<User>> asyncCallback = new AsyncCallback<Vector<User>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<User> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				userList = result;
				administration.getCurrentUser(getCurrentUserExecute());
			}

		};
		return asyncCallback;
	}

	/**
	 * Diese Methode holt sich den aktuell eingeloggten User und ruft von der
	 * Applikations-Ebene getAllSubscribedHashtags auf.
	 * 
	 * @return
	 */
	private AsyncCallback<User> getCurrentUserExecute() {
		AsyncCallback<User> asyncCallback = new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());

			}

			@Override
			public void onSuccess(User result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				currentUser = result;
				createUserTable(currentUser, true);
				for (User user : userList) {
					createUserTable(user, false);
				}
				// Abonnierte Hashtags und deren Postings
				administration
						.getAllSubscribedHashtags(getAllSubscribedHashtagsExecute());
			}
		};
		return asyncCallback;
	}

	/**
	 * Diese Methode holt sich alle abonnierten Hashtags und speichert das
	 * result in hashtagList
	 * 
	 * @return
	 */
	private AsyncCallback<Vector<Hashtag>> getAllSubscribedHashtagsExecute() {
		AsyncCallback<Vector<Hashtag>> asyncCallback = new AsyncCallback<Vector<Hashtag>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<Hashtag> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				hashtagList = result;
				showSubscribedHashtag();
				if (result.size() == 0) {
					headerHashtag.setText("");
				}
			}

		};
		return asyncCallback;
	}

	// Navigatorbereich User
	private void createUserTable(final User user, boolean isCurrentUser) {
		FlexTable chatFlexTable = new FlexTable();
		FocusPanel wrapper = new FocusPanel();
		String userName = new String();

		if (isCurrentUser) {
			userName = "Your own public postings";
		} else {
			userName = user.getFirstName() + " " + user.getLastName();
		}

		Label userLabel = new Label(userName);
		userLabel.getElement().setId("conversationHead");

		wrapper.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				administration.getAllPublicConversationsFromUser(user,
						getAllPublicConversationsFromUserExecute(user));

			}
		});

		chatFlexTable.setWidget(0, 0, userLabel);
		chatFlexTable.getElement().setId("conversation");
		wrapper.add(chatFlexTable);
		contentUser.add(wrapper);
	}

	private AsyncCallback<Vector<Conversation>> getAllPublicConversationsFromUserExecute(
			final User user) {
		AsyncCallback<Vector<Conversation>> asyncCallback = new AsyncCallback<Vector<Conversation>>() {

			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<Conversation> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				Collections.reverse(result);
				TextyForm publicConversationViewer = new PublicConversationViewer(
						"Public Postings from " + user.getFirstName(), result);

				RootPanel.get("Details").clear();
				RootPanel.get("Info").clear();
				RootPanel.get("Details").add(publicConversationViewer);

			}

		};
		return asyncCallback;
	}

	/**
	 * Diese Methode wählt in einem Schleifendurchlauf alle beinhalteten
	 * Hashtags an und bezieht dessen Keywords, die auf einem Label platziert
	 * werden.
	 */
	private void showSubscribedHashtag() {

		for (final Hashtag hashtag : hashtagList) {
			FlexTable chatFlexTable = new FlexTable();
			FocusPanel wrapper = new FocusPanel();

			String keyword = "#" + hashtag.getKeyword();
			Label hashtagLabel = new Label(keyword);
			hashtagLabel.getElement().setId("conversationHead");
			wrapper.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					administration.getAllPublicMessagesFromHashtag(hashtag,
							getAllPublicMessagesFromHashtagExecute(hashtag));
				}
			});

			chatFlexTable.setWidget(0, 0, hashtagLabel);
			chatFlexTable.getElement().setId("conversation");
			wrapper.add(chatFlexTable);
			contentHashtag.add(wrapper);

		}

	}

	/**
	 * Diese Methode holt sich alle öffentlichen Nachrichten, die den
	 * abonnierten Hashtag beinhalten
	 * 
	 * @param hashtag
	 * @return
	 */
	// Hier entsteht die Detailsanzeige der Hashtags
	public AsyncCallback<Vector<Message>> getAllPublicMessagesFromHashtagExecute(
			final Hashtag hashtag) {

		AsyncCallback<Vector<Message>> asyncCallback = new AsyncCallback<Vector<Message>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<Message> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				TextyForm publicHashtagViewer = new PublicHashtagViewer(
						"Public Posting with hashtag #" + hashtag.getKeyword(),
						result);

				RootPanel.get("Details").clear();
				RootPanel.get("Info").clear();
				RootPanel.get("Details").add(publicHashtagViewer);
			}

		};
		return asyncCallback;

	}

}
