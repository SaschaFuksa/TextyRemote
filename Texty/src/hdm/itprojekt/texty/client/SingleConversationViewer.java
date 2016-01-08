package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SingleConversationViewer extends TextyForm {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	private Button replyButton = createReplyButton();
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private FlexTable chatFlexTable = new FlexTable();
	private Conversation conversation = null;
	private User currentUser = null;
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public SingleConversationViewer(String headline, Conversation conversation) {
		super(headline);
		this.conversation = conversation;
	}

	protected void run() {

		administration.getCurrentUser(getCurrentUserExectue());

		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("conversationWrapper");
		scroll.getElement().setId("conversationScroll");
		content.getElement().setId("conversationContent");
		chatFlexTable.getElement().setId("conversationContent");
		replyButton.getElement().setId("button");

		content.add(chatFlexTable);
		mainPanel.add(getHeadline());
		mainPanel.add(scroll);
		mainPanel.add(replyButton);

		this.add(mainPanel);

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
			}
		});

	}

	private AsyncCallback<User> getCurrentUserExectue() {
		AsyncCallback<User> asyncCallback = new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(User result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				currentUser = result;
				showAllMessages();
			}
		};
		return asyncCallback;
	}

	private void showAllMessages() {
		int index = 0;
		for (Message message : conversation.getListOfMessage()) {
			
			SingleMessageView messageView = new SingleMessageView(message, currentUser, conversation);

			chatFlexTable.getColumnFormatter().addStyleName(index,
					"chatFlexTableCell");

			if (message.getAuthor().getId() == currentUser.getId()) {
				chatFlexTable.setWidget(index, 1, messageView);
			} else {
				chatFlexTable.setWidget(index, 0, messageView);
			}

			index++;
		}
		scroll.scrollToBottom();
	}

	private Button createReplyButton() {
		Button replyButton = new Button("Reply", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("Info").clear();
				RootPanel.get("Info").add(new ReplyMessageForm("Reply to this conversation", conversation));
			}
		});
		return replyButton;
	}
}
