package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.FieldVerifier;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Das MessageForm Formular setzt die Widgets zusammen welche benötigt werden,
 * um neue Nachrichten zu verfassen oder Nachrichten zu ändern.
 */
public class MessageForm extends VerticalPanel {

	/**
	 * Der LOG gibt eine mögliche Exception bzw. den Erfolg des asynchronen
	 * Callbacks aus.
	 */
	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	/**
	 * Deklaration, Definition und Initialisierung der Widget.
	 */
	private FlexTable messageFormFlexTable = new FlexTable();
	private Button addButton = createAddButton();
	Button sendButton = new Button("Send");
	private HorizontalPanel suggestBoxPanel = new HorizontalPanel();
	private HorizontalPanel content = new HorizontalPanel();
	private InfoBox infoBox = new InfoBox();
	private ScrollPanel scroll = new ScrollPanel(content);
	private TextArea textBox = new TextArea();
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);

	/**
	 * Deklaration, Definition und Initialisierung BO.
	 */
	private Vector<Hashtag> allHashtag = new Vector<Hashtag>();
	private Vector<Hashtag> selectedHashtag = new Vector<Hashtag>();
	private Vector<User> selectedUser = new Vector<User>();

	/**
	 * Die administration ermöglicht die asynchrone Kommunikation mit der
	 * Applikationslogik.
	 */
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	/**
	 * Modifiziert den Standardkonstruktor, um die run() Operation bei der
	 * Initialisierung aufzurufen.
	 */
	public MessageForm() {
		run();
	}

	/**
	 * Der Konstruktor erzwingt zustäzlich das Übergeben von ausgewählten
	 * Hashtags, falls eine vorhandene Nachricht editiert wird.
	 * 
	 * @param selectedHashtag
	 */
	public MessageForm(Vector<Hashtag> selectedHashtag) {
		run();
		this.selectedHashtag = selectedHashtag;
	}

	/**
	 * Diese Methode wird sofort aufgerufen, sobald ein Formular im Browser
	 * eingebaut wird.
	 */
	public void run() {
		/*
		 * Liest alle Hashtags aus der Datenbank aus.
		 */
		administration.getAllHashtags(getAllHashtagsExecute());

		/*
		 * Zuweisung der Handler an das jeweilige Widget.
		 */
		suggestBox.addKeyUpHandler(createKeyUpHandler());
		suggestBox.getValueBox().addFocusHandler(createFocusHandler());

		/*
		 * Standardtext für das Suchfeld.
		 */
		suggestBox.setText("Search for hashtags");

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		this.getElement().setId("messageForm");
		addButton.getElement().setId("addButton");
		sendButton.getElement().setId("button");
		suggestBoxPanel.getElement().setId("fullWidth");
		textBox.getElement().setId("messageFormTextarea");
		messageFormFlexTable.getElement().setId("fullSize");
		scroll.getElement().setId("messageFormScroll");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		suggestBoxPanel.add(suggestBox);
		suggestBoxPanel.add(addButton);

		messageFormFlexTable.setWidget(0, 0, textBox);
		messageFormFlexTable.setWidget(1, 0, suggestBoxPanel);
		messageFormFlexTable.setWidget(2, 0, scroll);
		messageFormFlexTable.setWidget(3, 0, sendButton);

		this.add(messageFormFlexTable);
		this.add(infoBox);

		/*
		 * Nachdem das Formular aufgebaut ist, wird die Höhe des jeweiligen
		 * Panels ausgelesen und als Höhe der Scrollbars gesetzt.
		 */
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setWidth(textBox.getOffsetWidth() + "px");
			}
		});
	}

	/**
	 * AsyncCallback für das Auslesen aller Hashtag aus der Datenbank.
	 * 
	 * @return
	 */
	private AsyncCallback<Vector<Hashtag>> getAllHashtagsExecute() {
		AsyncCallback<Vector<Hashtag>> asyncCallback = new AsyncCallback<Vector<Hashtag>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<Hashtag> result) {
				LOG.info("Success :" + result.getClass().getSimpleName());

				/*
				 * Übergibt das result an den Vector allHashtag und setzt im
				 * Anschluss die Vorschläge im Suchfeld.
				 */
				allHashtag = result;

				/*
				 * Sofern Hashtags bereits ausgewählt sind, werden diese mit
				 * allen Hashtags auf Gleichheit der IDs überprüft und bei
				 * Übereinstimmung aus allen Hashtags entfernt um das oracle
				 * optimal mit Vorschläge zu füllen.
				 */
				if (selectedHashtag.size() != 0) {
					for (Hashtag sendedHashtag : selectedHashtag) {
						for (Hashtag hashtag : allHashtag) {
							if (sendedHashtag.getId() == hashtag.getId()) {
								allHashtag.remove(hashtag);
								createHashtagPanel(sendedHashtag.getKeyword());
							}
						}
					}
				}

				/*
				 * Setzt die Vorschläge in der Suchleiste.
				 */
				setOracle();

			}
		};
		return asyncCallback;
	}

	/**
	 * AsyncCallback um einen neuen Hashtag anzulegen.
	 * 
	 * @return
	 */
	private AsyncCallback<Hashtag> createHashtagExecute() {
		AsyncCallback<Hashtag> asyncCallback = new AsyncCallback<Hashtag>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());

			}

			@Override
			public void onSuccess(Hashtag result) {
				LOG.info("Success :" + result.getClass().getSimpleName());

				/*
				 * Füge den neuen Hashtag den ausgewählten Hashtags hinzu.
				 */
				selectedHashtag.add(result);
			}
		};
		return asyncCallback;
	}

	/**
	 * Überprüfung ob der Hashtag unter in allHashtag ist, wenn nicht handelt es
	 * sich um einen neuen Hashtag, der in der DB angelegt wird.
	 * 
	 * @param keyword
	 * @return
	 */
	private boolean getAvailability(String keyword) {
		for (Hashtag hashtag : allHashtag) {
			if (keyword.equals(hashtag.getKeyword())) {
				selectedHashtag.addElement(hashtag);
				allHashtag.remove(hashtag);
				infoBox.clear();
				infoBox.setSuccessText("Hashtag successful added!");
				setOracle();
				return false;
			}
		}
		return true;
	}

	/**
	 * Sofern der Hashtag nicht in allHashtag ist, wird dieser in der DB
	 * angelegt und ein entsprechender Hinweis wird angezeigt. Im Anschluss wird
	 * das Panel für den Hashtag erstellt und die Vorschläge neu gesetzt.
	 * 
	 * @param keyword
	 */
	public void addHashtag(String keyword) {
		if (getAvailability(keyword)) {
			administration.createHashtag(keyword, createHashtagExecute());
			infoBox.clear();
			infoBox.setSuccessText("You subscribed a brand new hashtag!");
		}
		createHashtagPanel(keyword);
		setOracle();

	}

	/**
	 * Erstellung eines Panel für den ausgewählten Hashtags in der Scrollbox.
	 * 
	 * @param keyword
	 */
	private void createHashtagPanel(String keyword) {
		/*
		 * Deklaration, Definition und Initialisierung der Widget.
		 */
		final HorizontalPanel mainPanel = new HorizontalPanel();
		final Label keywordLabel = new Label("#" + keyword);
		final Label deleteLabel = createDeleteLabel(keyword, mainPanel);

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		mainPanel.getElement().setId("hashtagPanel");
		keywordLabel.getElement().setId("selectedHashtagLabel");
		deleteLabel.getElement().setId("removeButton");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		mainPanel.add(keywordLabel);
		mainPanel.add(deleteLabel);
		content.add(mainPanel);

		/*
		 * Löscht den Standardtext.
		 */
		suggestBox.setText("");

	}

	/**
	 * Erzeugung des zusätzlichen Labels, welches das löschen eines ausgewählten
	 * Hashtags ermöglicht.
	 * 
	 * @param keyword
	 * @param mainPanel
	 * @return
	 */
	private Label createDeleteLabel(final String keyword,
			final HorizontalPanel mainPanel) {
		Label label = new Label("x");
		label.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				deleteHashtag(keyword);
				content.remove(mainPanel);
			}

		});
		return label;
	}

	/**
	 * Enfernung eines Hashtags aus den ausgewählten Hashtag und fügt diesen
	 * allen nichtausgewählten Hashtags hinzu. Setzt im Anschluss die Vorschläge
	 * des Suchfelds neu.
	 * 
	 * @param keyword
	 */
	private void deleteHashtag(String keyword) {
		for (Hashtag hashtag : selectedHashtag) {
			if (keyword.equals(hashtag.getKeyword())) {
				allHashtag.addElement(hashtag);
				selectedHashtag.remove(hashtag);
				break;

			}
		}
		setOracle();
	}

	/**
	 * Fügt einen ausgewählten User den Empfängern hinzu.
	 * 
	 * @param user
	 */
	public void addSelectedUser(User user) {
		this.selectedUser.add(user);
	}

	/**
	 * Entfernt alle ausgewählte Hahstags.
	 */
	public void clearSelectedHashtag() {
		selectedHashtag.removeAllElements();
	}

	/**
	 * Entfernt die InfoBox.
	 */
	public void removeInfoBox() {
		this.infoBox.removeFromParent();
	}

	/**
	 * Entfernt einen ausgewählten User aus der Empfängerliste.
	 * 
	 * @param user
	 */
	public void removeSelectedUser(User user) {
		this.selectedUser.remove(user);
	}

	/**
	 * Gibt alle ausgewählte Hashtags zurück.
	 * 
	 * @return
	 */
	public Vector<Hashtag> getHashtag() {
		return selectedHashtag;
	}

	/**
	 * Gibt alle ausgewählte User zurück.
	 * 
	 * @return
	 */
	public Vector<User> getSelectedUser() {
		return selectedUser;
	}

	/**
	 * Gibt den Text aus der Textbox zurück.
	 * 
	 * @return
	 */
	public String getText() {
		return textBox.getText();
	}

	/**
	 * Setzt eine komplette Empfängerliste als ausgewählte User.
	 * 
	 * @param selectedUser
	 */
	public void setSelectedUser(Vector<User> selectedUser) {
		this.selectedUser = selectedUser;
	}

	/**
	 * Setzt den Namen des SendButtons z.B. beim Editieren der Nachricht.
	 * 
	 * @param text
	 */
	public void setSendButtonName(String text) {
		sendButton.setText(text);
	}

	/**
	 * Setzt den Text in der TextBox.
	 * 
	 * @param text
	 */
	public void setText(String text) {
		textBox.setText(text);
	}

	/**
	 * Setzt die nicht ausgewählte Hashtags als Vorschläge im Suchfeld.
	 */
	private void setOracle() {
		oracle.clear();
		for (Hashtag hashtag : allHashtag) {
			/*
			 * Dem oracle wird als Vorschlag das keyword des Hashtags
			 * zugewiesen.
			 */
			oracle.add(hashtag.getKeyword());
		}
	}

	/**
	 * Erzeugt einen Button für das hinzufügen des ausgewählten Hashtags aus dem
	 * Suchfeld.
	 * 
	 * @return
	 */
	private Button createAddButton() {
		Button addButton = new Button("", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				infoBox.clear();

				/*
				 * Erzeugt aus den Text aus dem Suchfeld das Keyword und
				 * entfernt ggf. Leerzeichen.
				 */
				String keyword = suggestBox.getText().trim()
						.replaceAll(" ", "");

				/*
				 * Überprüfung ob mindestens drei Zeichen eingegeben wurden.
				 */
				if (!FieldVerifier.isValidHashtag(keyword)) {
					infoBox.setWarningText("Please select a Hashtag with at least three characters!");
				} else {
					/*
					 * Fügt den ausgewählten Hashtag über addHashtag() den
					 * selectedHashtag hinzu.
					 */
					addHashtag(keyword);
				}
			}

		});
		return addButton;
	}

	/**
	 * Erzeugt einen Handler für das Suchfeld.
	 * 
	 * @return
	 */
	private KeyUpHandler createKeyUpHandler() {
		KeyUpHandler suggestBoxHandler = new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {

				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {

					infoBox.clear();

					/*
					 * Erzeugt aus den Text aus dem Suchfeld das Keyword und
					 * entfernt ggf. Leerzeichen.
					 */
					String keyword = suggestBox.getText().trim()
							.replaceAll(" ", "");

					/*
					 * Überprüfung ob mindestens drei Zeichen eingegeben wurden.
					 */
					if (!FieldVerifier.isValidHashtag(keyword)) {
						infoBox.setWarningText("Please select a Hashtag with at least three characters!");
					} else {
						/*
						 * Fügt den ausgewählten Hashtag über addHashtag() den
						 * selectedHashtag hinzu.
						 */
						addHashtag(keyword);

					}

				}
			}
		};
		return suggestBoxHandler;
	}

	/**
	 * Erzeugt einen Handler, der den Standardtext des Suchfeldes zurücksetzt,
	 * sobald diese ausgewählt wird.
	 * 
	 * @return
	 */
	private FocusHandler createFocusHandler() {
		FocusHandler focusHandler = new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				suggestBox.setText("");
				infoBox.clear();
			}
		};
		return focusHandler;
	}

}
