package hdm.itprojekt.texty.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import hdm.itprojekt.texty.server.db.DBConnection;

import java.sql.SQLException;

import hdm.itprojekt.texty.shared.TextyAdministration;
import hdm.itprojekt.texty.shared.bo.*;
import hdm.itprojekt.texty.server.db.*;

import java.util.*;

public class TextyAdministrationImpl extends RemoteServiceServlet implements
		TextyAdministration {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConversationMapper cMapper = null;
	private HashtagMapper hMapper = null;
	private HashtagSubscriptionMapper hsMapper = null;
	private MessageMapper mMapper = null;
	private UserMapper uMapper = null;
	private UserSubscriptionMapper usMapper = null;

	@Override
	public void init() throws IllegalArgumentException {
		this.cMapper = ConversationMapper.conversationMapper();
		this.hMapper = HashtagMapper.hashtagMapper();
		this.hsMapper = HashtagSubscriptionMapper.hashtagSubscriptionMapper();
		this.mMapper = MessageMapper.messageMapper();
		this.uMapper = UserMapper.userMapper();
		this.usMapper = UserSubscriptionMapper.userSubscriptionMapper();

	}

	@Override
	public Message createMessage(String text, User author, Vector<User> listOfReceivers,
			Vector<Hashtag> listOfHashtag) throws IllegalArgumentException {
		Message m = new Message();
		m.setText(text);
		m.setAuthor(author);
		m.setVisible(true);
		m.setListOfHashtag(listOfHashtag);
		m.setListOfReceivers(listOfReceivers);

		/*
		 * Setzen einer vorläufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist. TODO Mit
		 * David besprechen
		 */
		m.setId(1);

		
		return this.mMapper.insert(m);
	}

	@Override
	public Hashtag createHashtag(String keyword)
			throws IllegalArgumentException {
		Hashtag h = new Hashtag();
		h.setKeyword(keyword);

		/*
		 * Setzen einer vorläufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist. TODO Mit
		 * David besprechen
		 */
		h.setId(1);		

		return this.hMapper.insert(h);
	}

	@Override
	public Conversation createConversation(Message message)
			throws IllegalArgumentException {
		Conversation c = new Conversation();
		
		c.addMessageToConversation(message);
		if(message.getAuthor() == null) {
			c.setPublicly(true);
		}else {
			c.setPublicly(false);
		}		
		/*
		 * Setzen einer vorläufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist. TODO Mit
		 * David besprechen
		 */
		c.setId(1);		

		return this.cMapper.insert(c);
	}

	public UserSubscription createUserSubscription(User subscribedUser,
			User subscriber) {
		UserSubscription us = new UserSubscription();

		us.setSubscribedUser(subscribedUser);
		us.setSubscriber(subscriber);

		/*
		 * Setzen einer vorläufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist. TODO Mit
		 * David besprechen
		 */
		us.setId(1);
		Date now = new Date();
		us.setDateOfCreation(now);

		/*
		 * TODO Methode muss noch im Mapper erstellt werden
		 */

		// return this.usMapper.insert(us);
		return null;

	}

	public HashtagSubscription createHashtagSubscription(
			Hashtag subscribedHashtag, User subscriber) {
		HashtagSubscription hs = new HashtagSubscription();

		hs.setSubscribedHashtag(subscribedHashtag);
		hs.setSubscriber(subscriber);

		/*
		 * Setzen einer vorläufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist. TODO Mit
		 * David besprechen
		 */
		hs.setId(1);
		Date now = new Date();
		hs.setDateOfCreation(now);
		/*
		 * TODO Methode muss noch im Mapper erstellt werden
		 */
		// return this.hsMapper.insert(hs);
		return null;

	}

	// TODO Nächste Woche durchsprechen ob wir diese Methode überhaupt
	// benötigen.
	public User createUser(String firstName, String lastName, String email) throws IllegalArgumentException {
		User u = new User();
		u.setFirstName(firstName);
		u.setLastName(lastName);
		u.setEmail(email);
		
		/*
		 * Setzen einer vorläufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist. TODO Mit
		 * David besprechen
		 */
		u.setId(1);
		
		return this.uMapper.insert(u);

	}

	public Message editMessage(Message message) throws IllegalArgumentException {
		Message res = this.getmMapper().update(message);
		/*
		 * TODO Muss das hier gemacht werden? Methode Fehlt
		 * DBConnection.closeConnection();
		 */
		return res;

	}	

	public Vector<Hashtag> getAllSubscribedHashtags(User user)
			throws IllegalArgumentException {
		/*
		 * TODO Methode fehllt in den Mappern @David
		 */
		// return this.uMapper.findHashtags(user);
		return null;
	}

	public Message addMessageToConversation(Conversation c, String text, User author,
			Vector<Hashtag> listOfHashtag) throws IllegalArgumentException {
		Message m = new Message();
		m.setText(text);
		m.setAuthor(author);
		m.setListOfReceivers(c.getLastMessage().getListOfReceivers());
		m.setListOfHashtag(listOfHashtag);
		c.addMessageToConversation(m);
		
		
		// TODO Mit den anderen besprechen, woher weiß db welche Message zu
		// welcher conversation gehört.
		return this.mMapper.insert(m);		
	} 

	public void deleteMessage(Conversation conversation, Message message)
			throws IllegalArgumentException {
		conversation.removeMessageFromConversation(message);
		message.setVisible(false);
		this.getmMapper().update(message);
		// TODO DBConnection.closeConnection();

	}

	/**
	 * @return der ConversationMapper
	 */
	public ConversationMapper getcMapper() {
		return cMapper;
	}

	/**
	 * @return der HashtagMapper
	 */
	public HashtagMapper gethMapper() {
		return hMapper;
	}

	/**
	 * @return der HashtagsubscriptionMapper
	 */
	public HashtagSubscriptionMapper getHsMapper() {
		return hsMapper;
	}

	/**
	 * @return Der Messagemapper
	 */
	public MessageMapper getmMapper() {
		return mMapper;
	}

	/**
	 * @return Der UserMapper
	 */
	public UserMapper getuMapper() {
		return uMapper;
	}

	/**
	 * @return Der UserSubscriptionMapper
	 */
	public UserSubscriptionMapper getUsMapper() {
		return usMapper;
	}

}
