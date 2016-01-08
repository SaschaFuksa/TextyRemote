package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class ReplyMessageForm extends TextyForm {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());
	private MessageForm message = new MessageForm();
	private Conversation conversation = new Conversation();
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public ReplyMessageForm(String headline, Conversation conversation) {
		super(headline);
		this.conversation = conversation;
	}

	@Override
	protected void run() {

		message.sendButton.addClickHandler(createClickHandler());

		this.getElement().setId("fullWidth");

		this.add(getHeadline());
		this.add(message);

	}

	private AsyncCallback<Conversation> addMessageToConversationExecute() {
		AsyncCallback<Conversation> asyncCallback = new AsyncCallback<Conversation>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Conversation result) {
				LOG.info("Success :" + result.getClass().getSimpleName());
				RootPanel.get("Navigator").clear();
				RootPanel.get("Details").clear();
				RootPanel.get("Info").clear();
				RootPanel.get("Navigator").add(new ConversationForm("Conversations", result));
				
			}
		};
		return asyncCallback;
	}

	private ClickHandler createClickHandler() {
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				administration.addMessageToConversation(conversation,
						message.getText(), message.getHashtag(),
						addMessageToConversationExecute());
			}
		};
		return clickHandler;
	}

}
