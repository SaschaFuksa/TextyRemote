package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConversationForm extends TextyForm {

	public ConversationForm(String headline) {
		super(headline);
	}

	private Label intro = new Label(
			"Here you can read and reply to private conversations");
	private static Vector<Conversation> conList = new Vector<Conversation>();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public void showConversation() {
		for (int i = 0; i < conList.size(); i++) {
			final Conversation conversation = conList.get(i);
			VerticalPanel conversationPanel = new VerticalPanel();
			HorizontalPanel headerPanel = new HorizontalPanel();
			HorizontalPanel receiverPanel = new HorizontalPanel();
			FocusPanel wrapper = new FocusPanel();
			String receiver = setRecipient(conList.get(i).getLastMessage()
					.getListOfReceivers());
			String text = setMessageText(conList.get(i).getLastMessage()
					.getText());
			// String date = DateTimeFormat.getFormat("yyyy-MM-dd").format(
			// conList.get(i).getLastMessage().getDateOfCreation());
			Label textLabel = new Label(text);
			Label receiverLabel = new Label(receiver);
			Label authorLabel = new Label("Last message from: "
					+ conList.get(i).getLastMessage().getAuthor()
							.getFirstName());
			// Label dateLabel = new Label(date);

			authorLabel.setStylePrimaryName("conversationHead");
			receiverLabel.setStylePrimaryName("conversationHead");
			// dateLabel.setStylePrimaryName("conversationDate");
			textLabel.setStylePrimaryName("conversationBody");
			conversationPanel.setStylePrimaryName("conversation");

			receiverPanel.add(authorLabel);
			receiverPanel.add(receiverLabel);
			headerPanel.add(receiverPanel);
			// headerPanel.add(dateLabel);

			conversationPanel.add(headerPanel);
			conversationPanel.add(textLabel);

			wrapper.add(conversationPanel);
			wrapper.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					RootPanel.get("Details").clear();
					RootPanel.get("Details").add(new ShowSingleConversation("Private Conversation", conversation));
				}
			});

			content.add(wrapper);

		}
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

	@Override
	protected void run() {

		administration
				.getAllConversationsFromUser(new AsyncCallback<Vector<Conversation>>() {
					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Vector<Conversation> result) {
						ConversationForm.conList = result;
						showConversation();

					}
				});

		scroll.setStylePrimaryName("conversationScroll");

		this.add(intro);
		this.add(scroll);
	}

}
