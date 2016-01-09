package hdm.itprojekt.texty.shared.bo;

import java.util.Vector;


/**
 * Die Klasse Message beinhaltet Methoden zum erstellen, interagieren und bearbeiten von Nachrichten.
 */
public class Message extends BusinessObject implements Comparable<Message> {

	private static final long serialVersionUID = 1L;
	private String text = "";
	private Vector<Hashtag> listOfHashtag = new Vector<Hashtag>();
	private Vector<User> listOfReceivers = new Vector<User>();
	private int conversationID;
	private User author = null;
	private boolean visible = false;

	/**
	 * No Argument Konstruktor
	 */
	public Message() {
	}

	/**
	 * Konstruktor mit übergebenem Text
	 * @param text
	 */
	// Als Test für ChatRoom
	public Message(String text) {
		this.text = text;
	}
	/**
	 * Vollständiger Konstruktor einer Nachricht
	 * @param text
	 * @param author
	 * @param listOfReceivers
	 * @param visible
	 */
	public Message(String text, User author, Vector<User> listOfReceivers,
			boolean visible) {
		this.text = text;
		this.author = author;
		this.listOfReceivers = listOfReceivers;
		this.visible = visible;
	}
	/**
	 * Hinzufügen eines Hashtags
	 * @param hashtag
	 */
	public void addHashtag(Hashtag hashtag) {
		this.listOfHashtag.addElement(hashtag);
		;
	}
	/**
	 * Hinzufügen eines Empfängers
	 * @param receiver
	 */
	public void addReceivers(User receiver) {
		this.listOfReceivers.addElement(receiver);
	}

	/**
	 * Zurückgeben des Autors
	 * @return author
	 */
	public User getAuthor() {
		return author;
	}

	/**
	 * Auslesen der Unterhaltungs ID der die Nachricht zugeordnet ist
	 * @return conversationID
	 */
	public int getConversationID() {
		return conversationID;
	}

	/**
	 * Ausgeben aller Hashtags der Nachricht
	 * @return listOfHashtag
	 */
	public Vector<Hashtag> getListOfHashtag() {
		return listOfHashtag;
	}

	/**
	 * Ausgeben aller Empfänger
	 * @return listOfReceivers
	 */
	public Vector<User> getListOfReceivers() {
		return listOfReceivers;
	}
	
	/**
	 * @return Nachrichtentext
	 */
	public String getText() {
		return text;
	}

	/**
	 * Abfrage ob die Nachricht sichtbar ist. (gelöschte Nachrichten: visible = false) 
	 * @return
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Setzen des Nachrichtenautors
	 * @param author
	 */
	public void setAuthor(User author) {
		this.author = author;
	}
	
	/**
	 * Nachricht wird einer Unterhaltung zugeordnet
	 * @param conversationID
	 */
	public void setConversationID(int conversationID) {
		this.conversationID = conversationID;
	}

	/**
	 * Hinzufügen einer Liste von Hashtags zu einer Nachricht. 
	 * @param listOfHashtag
	 */
	public void setListOfHashtag(Vector<Hashtag> listOfHashtag) {
		this.listOfHashtag = listOfHashtag;
	}

	/**
	 * Setzen der Empfänger einer Nachricht
	 * @param listOfReceivers
	 */
	public void setListOfReceivers(Vector<User> listOfReceivers) {
		this.listOfReceivers = listOfReceivers;
	}

	/**
	 * Text einer Nachricht setzen
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Festlegen der Sichtbarkeit einer Nachricht. (legt fest ob eine Nachricht für den User "gelöscht" ist)
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * Vergleicht eine Nachricht mit der im Parameter übergebenen.
	 */
	public int compareTo(Message o) {
		if (getDateOfCreation() == null || o.getDateOfCreation() == null)
		      return 0;
		    return getDateOfCreation().compareTo(o.getDateOfCreation());
		
	}

}
