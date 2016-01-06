package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConversationForm extends TextyForm {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	private static Vector<Conversation> conList = new Vector<Conversation>();
	private VerticalPanel mainPanel = new VerticalPanel();
	private TextyForm newMessage = new NewMessage("New Conversation");
	private Label intro = new Label(
			"Here you can read and reply to private conversations");
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
				.getAllConversationsFromUser(new AsyncCallback<Vector<Conversation>>() {
					@Override
					public void onFailure(Throwable caught) {
						LOG.severe("Error: " + caught.getMessage());
					}

					@Override
					public void onSuccess(Vector<Conversation> result) {
						LOG.info("Success :"
								+ result.getClass().getSimpleName());

						ConversationForm.conList = result;
						showConversation();

						if (state) {
							for (Conversation singleConversation : result) {
								if (conversation.getId() == singleConversation
										.getId()) {
									TextyForm singleConversationViewer = new SingleConversationViewer(
											"Private Conversation",
											singleConversation);
									RootPanel.get("Details").add(
											singleConversationViewer);
								}
							}
							state = false;
						}
					}
				});

		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("conversationWrapper");
		scroll.getElement().setId("conversationScroll");
		content.getElement().setId("conversationContent");

		mainPanel.add(getHeadline());
		mainPanel.add(intro);
		mainPanel.add(scroll);
		
		if (RootPanel.get("Info").getWidgetCount() == 0) {
			RootPanel.get("Info").add(newMessage);
		}

		this.add(mainPanel);

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
			}
		});

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

	public void showConversation() {
		for (Conversation conversation : conList) {
			final Conversation conversationView = conversation;
			FlexTable chatFlexTable = new FlexTable();
			FocusPanel wrapper = new FocusPanel();

			String receiver = setRecipient(conversationView.getLastMessage()
					.getListOfReceivers());
			String text = setMessageText(conversationView.getLastMessage()
					.getText());
			String date = DateTimeFormat.getFormat("yyyy-MM-dd").format(
					conversationView.getLastMessage().getDateOfCreation());
			Label textLabel = new Label(text);
			Label receiverLabel = new Label(receiver);
			Label authorLabel = new Label("Last message from: "
					+ conversationView.getLastMessage().getAuthor()
							.getFirstName());
			Label dateLabel = new Label(date);

			authorLabel.getElement().setId("conversationHead");
			receiverLabel.getElement().setId("conversationHead");
			dateLabel.getElement().setId("conversationDate");
			textLabel.getElement().setId("conversationBody");

			wrapper.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					RootPanel.get("Details").clear();
					RootPanel.get("Details").add(
							new SingleConversationViewer(
									"Private Conversation", conversationView));
				}
			});

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

}
