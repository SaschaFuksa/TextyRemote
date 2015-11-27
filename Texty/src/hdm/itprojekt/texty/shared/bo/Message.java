package hdm.itprojekt.texty.shared.bo;

import java.util.*;

public class Message extends BusinessObject {

	private static final long serialVersionUID = 1L;
	private String text ="";
	//private ArrayList<Hashtag> listOfHashtag = new ArrayList<Hashtag> ();
	private Vector<Hashtag> listOfHashtag = new Vector<Hashtag>();
	

	private User author = null;
	//private User receiver =null;
	private boolean visible =false;
	
	public Message() {	
	}
	
	
	public Message (String text, User author, User receiver, boolean visible ){
		this.text = text;
		this.author = author;
		//this.receiver = receiver;
		this.visible = visible;
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
	
	
	/*public ArrayList<Hashtag> getListOfHashtag() {
		return listOfHashtag;
	}
	public void setListOfHashtag(ArrayList<Hashtag> listOfHashtag) {
		this.listOfHashtag = listOfHashtag;
	}*/
	/*public User getReceiver() {
		return receiver;
	}
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}*/
	
}
