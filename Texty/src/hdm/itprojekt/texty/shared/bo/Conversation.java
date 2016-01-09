package hdm.itprojekt.texty.shared.bo;
import java.util.Date;
import java.util.Vector;

/**
 * Die Klasse Conversation beinhaltet Methoden zum erstellen, und bearbeiten von Unterhaltungen.
 */
public class Conversation extends BusinessObject implements
		Comparable<Conversation> {

	private static final long serialVersionUID = 1L;
	private boolean publicly = false;
	private Date DateOfLastMessageInCon = null;
	
	private Vector<Message> listOfMessage = new Vector<Message>();

	// private Vector<User> listOfParticipant = new Vector<User> ();

	
	/**
	 * Hinzuf�gen einer Nachricht zu einer Unterhaltung
	 * @param message
	 */
	public void addMessageToConversation(Message message) {
		listOfMessage.addElement(message);
	}

	/**
	 * Gibt die letzte Nachricht einer Unterhaltung zur�ck.
	 */
	public Message getLastMessage() {
		return this.listOfMessage.elementAt(listOfMessage.size() - 1);
	}

	/*
	 * public Vector<User> getListOfParticipant() { return listOfParticipant; }
	 * 
	 * public void setListOfParticipant(Vector<User> listOfParticipant) {
	 * this.listOfParticipant = listOfParticipant; }
	 */

	/**
	 * R�ckgabe eines Vektors mit allen Message Objekten einer Unterhaltung
	 * @return listOfMessages
	 */
	public Vector<Message> getListOfMessage() {
		return listOfMessage;
	}

	/**
	 * Pr�ft ob die Unterhaltung �ffentlich ist.
	 */
	public boolean isPublicly() {
		return publicly;
	}

	/**
	 * Entfernen einer Nachticht aus der Unterhaltung
	 * @param message
	 */
	public void removeMessageFromConversation(Message message) {
		listOfMessage.remove(message);
	}

	/**
	 * Hinzuf�gen einer Liste von Nachrichten zu einer Unterhaltung
	 * @param listOfMessage
	 */
	public void setListOfMessage(Vector<Message> listOfMessage) {
		this.listOfMessage = listOfMessage;
	}

	/**
	 * Bestimmen der Sichtbarkeit einer Unterhaltung.
	 * @param publicly
	 */
	public void setPublicly(boolean publicly) {
		this.publicly = publicly;
	}

	/**
	 * Vergleichen einer Unterhaltung mit der im Parameter �bergebenen.
	 */
	public int compareTo(Conversation o) {
		if (getDateOfLastMessageInCon() == null || o.getDateOfLastMessageInCon() == null)
			return 0;
		return getDateOfLastMessageInCon().compareTo(o.getDateOfLastMessageInCon());
	}
	
	/**
	 * Setzten des Datums der letzten Nachricht einer Unterhaltung.
	 * @param dateOfLastMessageInCon
	 */
	public void setDateOfLastMessageInCon(Date dateOfLastMessageInCon){
		this.DateOfLastMessageInCon=dateOfLastMessageInCon;
	}
	
	/**
	 * Auslesen des Datums der letzten Nachricht einer Unterhaltung.
	 * @return
	 */
	public Date getDateOfLastMessageInCon() {
		return DateOfLastMessageInCon;
	}

}
