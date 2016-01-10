package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;

import java.util.Date;
import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PublicConversationViewer extends TextyForm {

	private final static long ONE_MINUTE = 60;
	private final static long ONE_HOURS = 60 * ONE_MINUTE;
	private final static long ONE_DAYS = 24 * ONE_HOURS;
	private final static long ONE_MONTH = 30 * ONE_DAYS;

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	private Vector<Conversation> conversationListOfUser = new Vector<Conversation>();
	private FlexTable conversationTable = new FlexTable();
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label text = new Label(
			"Here you can see all public messages from your subscribed user!");
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public PublicConversationViewer(String headline,
			Vector<Conversation> conversationListofUser) {
		super(headline);
		this.conversationListOfUser = conversationListofUser;
	}

	@Override
	protected void run() {

		if (conversationListOfUser.size() == 0) {
			text.setText("Oops! This user still doesn't have any public conversations!");
		}

		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("conversationWrapper");
		scroll.getElement().setId("conversationScroll");
		content.getElement().setId("fullWidth");
		conversationTable.getElement().setId("fullWidth");
		text.getElement().setId("blackFont");

		mainPanel.add(getHeadline());
		mainPanel.add(text);

		content.add(conversationTable);

		showConversation();

		this.add(mainPanel);

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
				mainPanel.add(scroll);
			}
		});
	}

	private void showConversation() {
		int index = 0;
		for (final Conversation conversation : conversationListOfUser) {
			VerticalPanel chatPanel = new VerticalPanel();
			FlexTable messageTable = new FlexTable();
			FocusPanel wrapper = new FocusPanel();

			chatPanel.getElement().setId("fullWidth");
			messageTable.getElement().setId("conversation");

			String keywordList = new String();

			for (Hashtag hashtag : conversation.getListOfMessage()
					.firstElement().getListOfHashtag()) {
				keywordList = keywordList + "#" + hashtag.getKeyword() + " ";
			}

			String dateString = DateTimeFormat.getFormat("HH:mm").format(
					conversation.getListOfMessage()
					.firstElement().getDateOfCreation());

			Date afterDate = new Date();
			Date baseDate = conversation.getListOfMessage()
					.firstElement().getDateOfCreation();

			long afterTime = afterDate.getTime() / 1000;
			long baseTime = baseDate.getTime() / 1000;

			long duration = afterTime - baseTime;

			if (duration < ONE_DAYS) {
				String baseDay = DateTimeFormat.getFormat("dd").format(baseDate);
				String afterDay = DateTimeFormat.getFormat("dd").format(afterDate);

				if (!baseDay.equals(afterDay)) {
					dateString = "yesterday at " + dateString;
				}

			} else if (duration > ONE_DAYS && duration < ONE_MONTH) {
				dateString = DateTimeFormat.getFormat("dd.MM").format(baseDate)
						+ " at "
						+ DateTimeFormat.getFormat("HH").format(baseDate) + "h";
			}

			else if (duration > ONE_MONTH) {
				dateString = DateTimeFormat.getFormat("MM.yyyy").format(
						baseDate)
						+ " at "
						+ DateTimeFormat.getFormat("dd").format(baseDate)
						+ ", "
						+ DateTimeFormat.getFormat("HH").format(baseDate) + "h";
			}

			String text = conversation.getListOfMessage().firstElement()
					.getText();

			Label hashtagLabel = new Label(keywordList);
			Label dateLabel = new Label(dateString);
			Label textLabel = new Label(text);

			wrapper.addClickHandler(createClickHandler(conversation, chatPanel));

			messageTable.setWidget(0, 0, dateLabel);
			messageTable.setWidget(1, 0, textLabel);
			messageTable.setWidget(2, 0, hashtagLabel);

			wrapper.add(messageTable);

			conversationTable.setWidget(index, 0, wrapper);
			conversationTable.setWidget(index + 1, 0, chatPanel);
			index = index + 2;
		}

	} // Ende for-Schleife

	private ClickHandler createClickHandler(final Conversation conversation,
			final VerticalPanel chatPanel) {
		ClickHandler clickHandler = new ClickHandler() {
			boolean state = true;

			@Override
			public void onClick(ClickEvent event) {

				if (state) {
				/*	administration.getAllMesagesFromConversation(
							conversation.getListOfMessage().firstElement(),
							getAllMesagesFromConversationExecute(conversation,
									chatPanel));*/
					state = false;
				} else {
					chatPanel.clear();
					state = true;
				}

			}
		};
		return clickHandler;
	}

	private FlexTable createSingleMessage(Message message) {
		FlexTable messageTable = new FlexTable();
		messageTable.getElement().setId("publicConversation");

		String author = message.getAuthor().getFirstName();
		String keywordList = new String();

		for (Hashtag hashtag : message.getListOfHashtag()) {
			keywordList = keywordList + "#" + hashtag.getKeyword() + " ";
		}

		String dateString = DateTimeFormat.getFormat("HH:mm").format(
				message.getDateOfCreation());

		Date afterDate = new Date();
		Date baseDate = message.getDateOfCreation();

		long afterTime = afterDate.getTime() / 1000;
		long baseTime = baseDate.getTime() / 1000;

		long duration = afterTime - baseTime;

		if (duration < ONE_DAYS) {
			String baseDay = DateTimeFormat.getFormat("dd").format(baseDate);
			String afterDay = DateTimeFormat.getFormat("dd").format(afterDate);

			if (!baseDay.equals(afterDay)) {
				dateString = "yesterday at " + dateString;
			}

		} else if (duration > ONE_DAYS && duration < ONE_MONTH) {
			dateString = DateTimeFormat.getFormat("dd.MM").format(baseDate)
					+ " at "
					+ DateTimeFormat.getFormat("HH").format(baseDate) + "h";
		}

		else if (duration > ONE_MONTH) {
			dateString = DateTimeFormat.getFormat("MM.yyyy").format(
					baseDate)
					+ " at "
					+ DateTimeFormat.getFormat("dd").format(baseDate)
					+ ", "
					+ DateTimeFormat.getFormat("HH").format(baseDate) + "h";
		}

		String text = message.getText();

		Label authorLabel = new Label(author);
		Label hashtagLabel = new Label(keywordList);
		Label dateLabel = new Label(dateString);
		Label textLabel = new Label(text);

		dateLabel.getElement().setId("floatRight");
		messageTable.getFlexCellFormatter().setColSpan(1, 0, 3);
		messageTable.getFlexCellFormatter().setColSpan(2, 0, 3);

		messageTable.setWidget(0, 0, authorLabel);
		messageTable.setWidget(0, 1, dateLabel);
		messageTable.setWidget(1, 0, textLabel);
		messageTable.setWidget(2, 0, hashtagLabel);

		return messageTable;
	}

	private AsyncCallback<Vector<Message>> getAllMesagesFromConversationExecute(
			final Conversation conversation, final VerticalPanel chatPanel) {
		AsyncCallback<Vector<Message>> asyncCallback = new AsyncCallback<Vector<Message>>() {

			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());

			}

			@Override
			public void onSuccess(Vector<Message> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				conversation.setListOfMessage(result);
				VerticalPanel contentMessage = new VerticalPanel();
				ScrollPanel scrollMessage = new ScrollPanel(contentMessage);
				chatPanel.add(scrollMessage);
				contentMessage.getElement().setId("fullWidth");
				scrollMessage.setHeight("200px");

				for (int i = 1; i < conversation.getListOfMessage().size(); i++) {
					FlexTable messageTable = createSingleMessage(conversation
							.getListOfMessage().get(i));
					contentMessage.add(messageTable);
				}
				Button replyButton = createReplyButton(conversation);
				replyButton.getElement().setId("button");
				contentMessage.add(replyButton);
				scrollMessage.scrollToBottom();

			}

		};
		return asyncCallback;
	}

	private Button createReplyButton(final Conversation conversation) {
		Button replyButton = new Button("Reply", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("Info").clear();
				RootPanel.get("Info").add(
						new ReplyMessageForm(
								"Reply to this public conversation",
								conversation, false));
			}
		});
		return replyButton;
	}

}
