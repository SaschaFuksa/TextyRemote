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
 * Das PublicHashtagViewer Formular sorgt fuer die Anzeige der öffentlichen
 * Nachrichten im Details-Bereich der GUI, nachdem man einen abonnierten Hashtag
 * ausgewählt hat
 * 
 *
 */
public class PublicHashtagViewer extends TextyForm {

	private final static long ONE_MINUTE = 60;
	private final static long ONE_HOURS = 60 * ONE_MINUTE;
	private final static long ONE_DAYS = 24 * ONE_HOURS;
	private final static long ONE_MONTH = 30 * ONE_DAYS;

	/**
	 * Deklaration, Definition und Initialisierung der Widget.
	 */
	private RefreshForm refreshForm = new RefreshForm("");
	private VerticalPanel mainPanel = new VerticalPanel();
	private Vector<Message> messageListofHashtag = new Vector<Message>();
	private Label text = new Label(
			"Here you can see all public messages with your favourite hashtag!");

	/**
	 * Der Konstruktor erzwingt die Eingabe einer Überschrift für das Formular.
	 * Zudem wird die entsprechende Unterhaltung übergeben,
	 * 
	 * @param headline
	 * @param messageListofHashtag
	 */
	public PublicHashtagViewer(String headline,
			Vector<Message> messageListofHashtag) {
		super(headline);
		this.messageListofHashtag = messageListofHashtag;
	}

	/**
	 * Modifiziert den Standardkonstruktor, um die run() Operation bei der
	 * Initialisierung aufzurufen.
	 * 
	 * @see TextyForm
	 */
	@Override
	protected void run() {
		
		refreshForm.run();

		/*
		 * Falls noch keine oeffentliche Nachricht des beinhalteten Hashtags von
		 * einem beliebigen User gepostet wurde, wird ein entsprechender Hinweis
		 * angezeigt
		 */
		if (messageListofHashtag.size() == 0) {
			text.setText("Oops! This hashtag doesn't exist in any public conversations!");
		}

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("fullWidth");
		text.getElement().setId("blackFont");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		mainPanel.add(getHeadline());
		mainPanel.add(text);

		addConversation();

		this.add(mainPanel);

	}

	private void addConversation() {
		for (final Message message : messageListofHashtag) {

			/*
			 * Erzeugen einer neuen FlexTable
			 */
			FlexTable chatFlexTable = new FlexTable();

			String dateString = DateTimeFormat.getFormat("HH:mm").format(
					message.getDateOfCreation());

			Date afterDate = new Date();
			Date baseDate = message.getDateOfCreation();

			long afterTime = afterDate.getTime() / 1000;
			long baseTime = baseDate.getTime() / 1000;

			long duration = afterTime - baseTime;

			/*
			 * Unterschiedliche Behandlung der Datumanzeige
			 */
			if (duration < ONE_DAYS) {
				String baseDay = DateTimeFormat.getFormat("dd")
						.format(baseDate);
				String afterDay = DateTimeFormat.getFormat("dd").format(
						afterDate);

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

			/*
			 * Geht jeden Hashtag in der Nachricht durch und übergibt diese an
			 * die keywordList
			 */
			for (Hashtag hashtag : message.getListOfHashtag()) {
				keywordList = keywordList + "#" + hashtag.getKeyword() + " ";
			}

			/*
			 * Hinzufügen der Informationen über Author, Datum und Hashtags
			 */
			Label authorLabel = new Label(message.getAuthor().getFirstName());
			Label dateLabel = new Label(dateString);
			Label hashtagLabel = new Label(keywordList);

			String text = message.getText();
			final Label textLabel = new Label(text);

			/*
			 * Zuweisung der Styles an das jeweilige Widget.
			 */
			dateLabel.getElement().setId("floatRight");
			chatFlexTable.getElement().setId("conversation");

			chatFlexTable.getFlexCellFormatter().setColSpan(1, 0, 2);
			chatFlexTable.getFlexCellFormatter().setColSpan(2, 0, 2);

			/*
			 * Hinzufügen der Widgets in die jeweilige Spalte der FlexTable
			 */
			chatFlexTable.setWidget(0, 0, authorLabel);
			chatFlexTable.setWidget(0, 1, dateLabel);
			chatFlexTable.setWidget(1, 0, textLabel);
			chatFlexTable.setWidget(2, 0, hashtagLabel);

			mainPanel.add(chatFlexTable);

		} // Ende for-Schleife
	}
}
