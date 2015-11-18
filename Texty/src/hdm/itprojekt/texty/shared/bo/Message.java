package hdm.itprojekt.texty.shared.bo;

import java.util.*;

public class Message extends BusinessObject {

	private static final long serialVersionUID = 1L;
	private String text ="";
	private ArrayList<Hashtag> listOfHashtag = new ArrayList<Hashtag> ();
	private User author = null;
	private boolean visible =false;
	
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
	
	public ArrayList<Hashtag> getListOfHashtag() {
		return listOfHashtag;
	}
	
}
