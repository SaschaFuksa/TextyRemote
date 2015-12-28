package hdm.itprojekt.texty.shared.bo;

import java.util.Vector;

public class Conversation extends BusinessObject {

	private static final long serialVersionUID = 1L;
	private boolean publicly = false;
	private Vector<Message> listOfMessage = new Vector<Message>();

	// private Vector<User> listOfParticipant = new Vector<User> ();

	public void addMessageToConversation(Message message) {
		listOfMessage.addElement(message);
	}

	public Message getLastMessage() {
		return this.listOfMessage.elementAt(listOfMessage.size() - 1);
	}

	/*
	 * public Vector<User> getListOfParticipant() { return listOfParticipant; }
	 * 
	 * public void setListOfParticipant(Vector<User> listOfParticipant) {
	 * this.listOfParticipant = listOfParticipant; }
	 */

	public Vector<Message> getListOfMessage() {
		return listOfMessage;
	}

	public boolean isPublicly() {
		return publicly;
	}

	public void removeMessageFromConversation(Message message) {
		listOfMessage.remove(message);
	}

	public void setListOfMessage(Vector<Message> listOfMessage) {
		this.listOfMessage = listOfMessage;
	}

	public void setPublicly(boolean publicly) {
		this.publicly = publicly;
	}

}
