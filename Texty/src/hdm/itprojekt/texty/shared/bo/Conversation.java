package hdm.itprojekt.texty.shared.bo;

import java.util.*;

public class Conversation extends BusinessObject {

	private static final long serialVersionUID = 1L;
	private boolean publicly = false;
	private ArrayList<Message> listOfMessage = new ArrayList<Message> ();
	private ArrayList<User> listOfParticipant = new ArrayList<User> ();
	
	public ArrayList<Message> getListOfMessage() {
		return listOfMessage;
	}
	
	public void setListOfMessage(ArrayList<Message> listOfMessage) {
		this.listOfMessage = listOfMessage;
	}
	
	public ArrayList<User> getListOfParticipant() {
		return listOfParticipant;
	}
	
	public boolean isPublicly() {
		return publicly;
	}
	
	public void setPublicly(boolean publicly) {
		this.publicly = publicly;
	}
	

}
