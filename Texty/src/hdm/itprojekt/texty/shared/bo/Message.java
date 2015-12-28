package hdm.itprojekt.texty.shared.bo;

import java.util.Vector;

public class Message extends BusinessObject {

	private static final long serialVersionUID = 1L;
	private String text = "";	
	private Vector<Hashtag> listOfHashtag = new Vector<Hashtag>();
	private Vector<User> listOfReceivers = new Vector<User>();
	private int conversationID;
	private User author = null;
	private boolean visible = false;
	
	public Message() {	
	}
	
	
	public Message (String text, User author, Vector<User> listOfReceivers, boolean visible ){
		this.text = text;
		this.author = author;
		this.listOfReceivers = listOfReceivers;
		this.visible = visible;
	}
	
	//Als Test für ChatRoom
	public Message(String text) {
		this.text = text;
	}


	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public User getAuthor() {
		return author;
	}
	
	public void setAuthor(User author) {
		this.author = author;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}


	public Vector<Hashtag> getListOfHashtag() {
		return listOfHashtag;
	}
	
	public void setListOfHashtag(Vector<Hashtag> listOfHashtag) {
		this.listOfHashtag = listOfHashtag;
	}
	
	public void addHashtag(Hashtag hashtag) {
		this.listOfHashtag.addElement(hashtag);;
	}
	/**
	 * @return the listOfReceivers
	 */
	public Vector<User> getListOfReceivers() {
		return listOfReceivers;
	}


	/**
	 * @param listOfReceivers the listOfReceivers to set
	 */
	public void setListOfReceivers(Vector<User> listOfReceivers) {
		this.listOfReceivers = listOfReceivers;
	}
	
	public void addReceivers(User receiver) {
		this.listOfReceivers.addElement(receiver);
	}
	
	public int getConversationID() {
		return conversationID;
	}


	public void setConversationID(int conversationID) {
		this.conversationID = conversationID;
	}

	
		
}
