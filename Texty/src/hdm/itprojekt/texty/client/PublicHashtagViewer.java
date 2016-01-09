package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
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
	
	private final static long ONE_MINUTE = 60;
	private final static long ONE_HOURS = 60 * ONE_MINUTE;
	private final static long ONE_DAYS = 24 * ONE_HOURS;
	private final static long ONE_MONTH = 30 * ONE_DAYS;

	private VerticalPanel mainPanel = new VerticalPanel();
	private Vector<Message> messageListofHashtag = new Vector<Message>();
	private Label text = new Label(
			"Here you can see all public messages with your favourite hashtag!");

	public PublicHashtagViewer(String headline,
			Vector<Message> messageListofHashtag) {
		super(headline);
		this.messageListofHashtag = messageListofHashtag;
	}

	@Override
	protected void run() {

		if (messageListofHashtag.size() == 0) {
			text.setText("Oops! This hashtag doesn't exist in any public conversations!");
		}
		
		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("fullWidth");
		text.getElement().setId("blackFont");

		mainPanel.add(getHeadline());
		mainPanel.add(text);

		addConversation();

		this.add(mainPanel);

	}

	private void addConversation() {
		for (final Message message : messageListofHashtag) {

			FlexTable chatFlexTable = new FlexTable();

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
			
			String keywordList = new String();

			for (Hashtag hashtag : message.getListOfHashtag()) {
				keywordList = keywordList + "#" + hashtag.getKeyword() + " ";
			}
			
			Label authorLabel = new Label(message.getAuthor().getFirstName());
			Label dateLabel = new Label(dateString);
			Label hashtagLabel = new Label(keywordList);

			String text = message.getText();
			final Label textLabel = new Label(text);

			dateLabel.getElement().setId("floatRight");
			chatFlexTable.getElement().setId("conversation");

			chatFlexTable.getFlexCellFormatter().setColSpan(1, 0, 2);
			chatFlexTable.getFlexCellFormatter().setColSpan(2, 0, 2);
			
			chatFlexTable.setWidget(0, 0, authorLabel);
			chatFlexTable.setWidget(0, 1, dateLabel);
			chatFlexTable.setWidget(1, 0, textLabel);
			chatFlexTable.setWidget(2, 0, hashtagLabel);

			mainPanel.add(chatFlexTable);

		} // Ende for-Schleife
	}
}
