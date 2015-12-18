package hdm.itprojekt.texty.client;

import java.util.Vector;

import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConversationForm extends TextyForm {

	public ConversationForm(String headline) {
		super(headline);
	}
	
	private Label intro = new Label("Here you can read and reply to private conversations");
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Vector<Conversation> conList = new Vector<Conversation>();

	protected void run() {
		
		Conversation con1 = new Conversation();
		Conversation con2 = new Conversation();
		Conversation con3 = new Conversation();
		Conversation con4 = new Conversation();
		
		con1.setId(1);
		con2.setId(2);
		con3.setId(3);
		con4.setId(4);
		
		conList.addElement(con1);
		conList.addElement(con2);
		conList.addElement(con3);
		conList.addElement(con4);
		
		//Message msg1 = new Message("Hallo", new User().setFirstName("Jürgen"), null, true);
		
		scroll.setSize("300px", "400px");
		
		this.add(intro);
		this.add(scroll);
	}

}
