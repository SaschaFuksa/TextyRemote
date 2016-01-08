package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Collections;
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

public class ConversationForm extends TextyForm {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	private Button newMessageButton = createNewMessageButton();
	private Vector<Conversation> conversationList = new Vector<Conversation>();
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label text = new Label(
			"Write a new message or read your private conversations!");
	private VerticalPanel content = new VerticalPanel();
	private Conversation conversation = new Conversation();
	private ScrollPanel scroll = new ScrollPanel(content);
	private boolean state = false;
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public ConversationForm(String headline) {
		super(headline);
	}

	public ConversationForm(String headline, Conversation conversation) {
		super(headline);
		this.conversation = conversation;
		this.state = true;
	}

	@Override
	protected void run() {

		administration
				.getAllConversationsFromUser(getAllConversationsFromUserExecute());

		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("conversationWrapper");
		scroll.getElement().setId("conversationScroll");
		content.getElement().setId("conversationContent");
		newMessageButton.getElement().setId("button");

		mainPanel.add(getHeadline());
		mainPanel.add(text);
		mainPanel.add(scroll);
		mainPanel.add(newMessageButton);

		this.add(mainPanel);

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
			}
		});

	}

	private AsyncCallback<Vector<Conversation>> getAllConversationsFromUserExecute() {
		AsyncCallback<Vector<Conversation>> asyncCallback = new AsyncCallback<Vector<Conversation>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<Conversation> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());

				conversationList = result;
				showConversation();

				if (state) {
					for (Conversation singleConversation : result) {
						if (conversation.getId() == singleConversation.getId()) {
							TextyForm singleConversationViewer = new SingleConversationViewer(
									"Private Conversation", singleConversation);
							RootPanel.get("Details").add(
									singleConversationViewer);
						}
					}
				}
			}
		};
		return asyncCallback;
	}

	private void showConversation() {
		Collections.reverse(conversationList);
		for (Conversation conversation : conversationList) {

			FlexTable chatFlexTable = new FlexTable();
			FocusPanel wrapper = new FocusPanel();

			String text = setMessageText(conversation.getLastMessage()
					.getText());
			String receiver = setRecipient(conversation.getLastMessage()
					.getListOfReceivers());
			String date = DateTimeFormat.getFormat("yyyy-MM-dd").format(
					conversation.getLastMessage().getDateOfCreation());

			Label textLabel = new Label(text);
			Label receiverLabel = new Label(receiver);
			Label authorLabel = new Label("Last message from: "
					+ conversation.getLastMessage().getAuthor().getFirstName());
			Label dateLabel = new Label(date);

			authorLabel.getElement().setId("conversationHead");
			receiverLabel.getElement().setId("conversationHead");
			dateLabel.getElement().setId("conversationDate");
			textLabel.getElement().setId("conversationBody");

			wrapper.addClickHandler(createClickHandler(conversation));

			chatFlexTable.getElement().setId("conversation");

			chatFlexTable.getFlexCellFormatter().setColSpan(1, 0, 2);
			chatFlexTable.getFlexCellFormatter().setColSpan(2, 0, 2);

			chatFlexTable.setWidget(0, 0, authorLabel);
			chatFlexTable.setWidget(0, 1, dateLabel);
			chatFlexTable.setWidget(1, 0, receiverLabel);
			chatFlexTable.setWidget(2, 0, textLabel);

			wrapper.add(chatFlexTable);

			content.add(wrapper);

		}
	}

	private String setMessageText(String text) {
		String messageText = null;
		if (text.length() < 30) {
			messageText = text;

		} else {

			StringBuffer bufferName = new StringBuffer(text);
			bufferName.setLength(30);
			bufferName.insert(bufferName.length(), "...");
			messageText = bufferName.toString();
		}
		return messageText;
	}

	private String setRecipient(Vector<User> receiver) {
		String receivers = " to";
		if (receiver.size() < 3) {
			for (int i = 0; i < receiver.size(); i++) {
				receivers = receivers + " '" + receiver.get(i).getFirstName()
						+ "'";
			}

		} else {
			for (int i = 0; i < 2; i++) {
				receivers = receivers + " '" + receiver.get(i).getFirstName()
						+ "'";
			}
			receivers = receivers + " and "
					+ new Integer(receiver.size() - 2).toString()
					+ " more receiver(s).";
		}
		return receivers;
	}

	private Button createNewMessageButton() {
		Button newMessageButton = new Button("New Conversation",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						TextyForm newMessage = new NewMessage(
								"New Conversation");
						RootPanel.get("Info").clear();
						RootPanel.get("Info").add(newMessage);
					}
				});
		return newMessageButton;
	}

	private ClickHandler createClickHandler(final Conversation conversation) {
		ClickHandler clickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("Details").clear();
				RootPanel.get("Details").add(
						new SingleConversationViewer("Private Conversation",
								conversation));
			}
		};
		return clickHandler;
	}

}
