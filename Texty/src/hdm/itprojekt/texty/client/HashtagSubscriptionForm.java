package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.Hashtag;

import java.util.Vector;

public class HashtagSubscriptionForm extends TextyForm {

	public HashtagSubscriptionForm(String headline) {
		super(headline);
	}

	public HashtagSubscriptionForm(String headline, Vector<Hashtag> selectedUser) {
		super(headline);
		this.selectedUser = selectedUser;
	}

	private Vector<Hashtag> selectedUser = new Vector<Hashtag>();

	protected void run() {
		// TODO Auto-generated method stub
	}

}
