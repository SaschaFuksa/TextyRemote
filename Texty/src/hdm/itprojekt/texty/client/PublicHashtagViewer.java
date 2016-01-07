package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;
import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * Diese Klasse wird in der Klasse HomeForm verwendet und dient der Darstellung
 * aller öffentlichen Nachrichten eines Users, die den abonnierten Hashtag beinhalten. Weiterhin
 * werden alle weiteren Hashtags, die in der Message enthalten sind, angezeigt
 *
 */
public class PublicHashtagViewer extends TextyForm {

	private VerticalPanel mainPanel = new VerticalPanel();
	private static Vector<Message> messageListofHashtag = new Vector<Message>();

	public PublicHashtagViewer(String headline,
			Vector<Message> messageListofHashtag) {
		super(headline);
		this.messageListofHashtag = messageListofHashtag;
	}

	@Override
	protected void run() {

		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("fullWidth");

		mainPanel.add(getHeadline());

		addConversation();

		this.add(mainPanel);

	}

	private void addConversation() {
		for (Message message : messageListofHashtag) {
			final Message messageView = message;

			messageView.getListOfHashtag();

			final VerticalPanel hashtagPanel = new VerticalPanel();
			FlexTable chatFlexTable = new FlexTable();
			FocusPanel wrapper = new FocusPanel();
			String date = DateTimeFormat.getFormat("yyyy-MM-dd").format(
					messageView.getDateOfCreation());
			Label dateLabel = new Label(date);

			Vector<Hashtag> hashtagVector = messageView.getListOfHashtag();

			String text = messageView.getText();
			final Label textLabel = new Label(text);

			hashtagPanel.getElement().setId("conversation");

			wrapper.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					// for (int i = 1; i < messageView; i++) {
					/*
					 * VerticalPanel singleHashtagPanel = new VerticalPanel();
					 * Label messageText = new Label(messageView .getText());
					 * singleHashtagPanel.add(messageText);
					 * hashtagPanel.add(singleHashtagPanel);
					 */

					// RootPanel.get("Info").clear();
					// RootPanel.get("Details").add(textLabel);
				}
			});

			// chatFlexTable.setWidget(0, 0, authorLabel);
			chatFlexTable.setWidget(0, 0, dateLabel);
			// chatFlexTable.setWidget(1, 0, receiverLabel);
			chatFlexTable.setWidget(1, 0, textLabel);

			wrapper.add(chatFlexTable);
			hashtagPanel.add(wrapper);

			for (int i = 0; i < hashtagVector.size(); i++) {
				Label hashtagText = new Label("#"
						+ messageView.getListOfHashtag().get(i).getKeyword()
						+ " ");
				hashtagPanel.add(hashtagText);
			}

			mainPanel.add(hashtagPanel);

		} // Ende for-Schleife
	}
}
