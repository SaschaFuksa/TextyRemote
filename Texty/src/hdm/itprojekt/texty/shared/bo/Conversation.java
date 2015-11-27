package hdm.itprojekt.texty.shared.bo;

import java.util.*;

public class Conversation extends BusinessObject {

	private static final long serialVersionUID = 1L;
	private boolean publicly = false;
	private Vector<Message> listOfMessage = new Vector<Message> ();
	private Vector<User> listOfParticipant = new Vector<User> ();
	
	
	public Vector<Message> getListOfMessage() {
		return listOfMessage;
	}
	
	public void setListOfMessage(Vector<Message> listOfMessage) {
		this.listOfMessage = listOfMessage;
	}
	
	public Vector<User> getListOfParticipant() {
		return listOfParticipant;
	}
	
	public void setListOfParticipant(Vector<User> listOfParticipant) {
		this.listOfParticipant = listOfParticipant;
	}
	
	public boolean isPublicly() {
		return publicly;
	}
	
	public void setPublicly(boolean publicly) {
		this.publicly = publicly;
	}
	

}
