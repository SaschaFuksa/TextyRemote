package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;

import java.util.Vector;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * Diese Klasse wird in der Klasse HomeForm aufgerufen und dient der Darstellung
 * aller öffentlichen Messages des im Editor angewählten Users., die den
 * abonnierten Hashtag beinhalten. Weiterhin werden alle weiteren Hashtags, die
 * in der Message enthalten sind, angezeigt
 *
 */
public class PublicConversationViewer extends TextyForm {

	private Vector<Conversation> conversationListOfUser = new Vector<Conversation>();
	private VerticalPanel messagePanel = new VerticalPanel();
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Label text = new Label(
			"Here you can see all public messages from your subscribed user!");

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
		mainPanel.getElement().setId("fullSize");
		scroll.getElement().setId("conversationScroll");
		content.getElement().setId("fullWidth");
		messagePanel.getElement().setId("fullWidth");
		text.getElement().setId("blackFont");

		mainPanel.add(getHeadline());
		mainPanel.add(text);
		mainPanel.add(scroll);

		content.add(messagePanel);

		addConversation();

		this.add(mainPanel);

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				Window.alert("" + mainPanel.getOffsetHeight());
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
			}
		});
	}

	private void addConversation() {
		for (Conversation conversation : conversationListOfUser) {
			final Conversation conversationView = conversation;
			final VerticalPanel conversationPanel = new VerticalPanel();
			Label hashtagLabel = new Label();
			FlexTable chatFlexTable = new FlexTable();
			FocusPanel wrapper = new FocusPanel();
			// Vector<Message> messageList = new Vector<Message>();

			chatFlexTable.getElement().setId("conversation");

			String date = DateTimeFormat.getFormat("yyyy-MM-dd").format(
					conversationView.getListOfMessage().get(0)
							.getDateOfCreation());
			Label dateLabel = new Label(date);
			// messageList = conversationView.getListOfMessage();
			String text = conversationView.getListOfMessage().firstElement()
					.getText();
			final Label textLabel = new Label(text);

			conversationPanel.getElement().setId("conversation");

			wrapper.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.alert("Huhu");
					for (int i = 1; i < conversationView.getListOfMessage()
							.size(); i++) {

						VerticalPanel singleConversationPanel = new VerticalPanel();
						Label messageText = new Label(conversationView
								.getListOfMessage().get(i).getText());
						singleConversationPanel.add(messageText);
						conversationPanel.add(singleConversationPanel);
					}
					// RootPanel.get("Info").clear();
					// RootPanel.get("Details").add(textLabel);
				}
			});

			// chatFlexTable.setWidget(0, 0, authorLabel);
			chatFlexTable.setWidget(0, 0, dateLabel);
			// chatFlexTable.setWidget(1, 0, receiverLabel);
			chatFlexTable.setWidget(1, 0, textLabel);

			chatFlexTable.setWidget(2, 0, hashtagLabel);

			wrapper.add(chatFlexTable);
			conversationPanel.add(wrapper);

			Vector<Message> messageVector = conversationView.getListOfMessage();

			String keywordList = new String();

			for (Hashtag hashtag : conversationView.getLastMessage()
					.getListOfHashtag()) {
				keywordList = keywordList + "#" + hashtag.getKeyword() + " ";
			}

			hashtagLabel.setText(keywordList);

			// for (Message message : conversationView.getListOfMessage()){
			//
			// }
			// for (int i = 0; i < messageVector.size(); i++) {
			// Label hashtagText = new Label("#"
			// +
			// conversationView.getListOfMessage().get(i).getListOfHashtag().get(i).getKeyword());
			// conversationPanel.add(hashtagText);
			// }
			messagePanel.add(chatFlexTable);

			// RootPanel.get("Details").add(contentConversation);

		} // Ende for-Schleife
	}

}
