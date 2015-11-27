package hdm.itprojekt.texty.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

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
	public Message createMessage(String text, User author, Boolean visible, Vector<Hashtag> listOfHashtag)
			throws IllegalArgumentException {
		Message m = new Message();
		m.setText(text);
		m.setAuthor(author);
		m.setVisible(visible);
		m.setListOfHashtag(listOfHashtag);
		
		/*
		 * TODO Mit anderen GUI Methoden besprechen  ( Wie wird was übergeben)
		 */

		/*
		 * Setzen einer vorläufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist. 
		 * TODO Mit David besprechen
		 */
		m.setId(1);
		
		Date now = new Date();
		m.setDateOfCreation(now);
		
		

		return this.mMapper.insert(m);
	}

	@Override
	public Hashtag createHashtag(String keyword) throws IllegalArgumentException {
		Hashtag h = new Hashtag();
		h.setKeyword(keyword);

		/*
		 * Setzen einer vorläufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist. TODO Mit
		 * David besprechen
		 */
		h.setId(1);
		Date now = new Date();
		h.setDateOfCreation(now);
		
		return this.hMapper.insert(h);
	}
	@Override
	public Conversation createConversation(Boolean publicly, Vector<Message> listOfMessage, Vector<User> listOfParticipant)
			throws IllegalArgumentException {
		 Conversation c = new Conversation();
		 
		 c.setPublicly(publicly);
		 c.setListOfMessage(listOfMessage);
		 c.setListOfParticipant(listOfParticipant);
		 
		 	/*
			 * Setzen einer vorläufigen Kundennr. Der insert-Aufruf liefert dann ein
			 * Objekt, dessen Nummer mit der Datenbank konsistent ist. TODO Mit
			 * David besprechen
			 */
		 c.setId(1);
		 Date now = new Date();
		 c.setDateOfCreation(now);
		 
		 return this.cMapper.insert(c);
	 }
	
	public UserSubscription createUserSubscription(User subscribedUser, User subscriber){
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
		
		return this.usMapper.insert(us);
		
	}
	
	public HashtagSubscription createHashtagSubscription(Hashtag subscribedHashtag, User subscriber) {
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
		return this.hsMapper.insert(hs);
		
	}
	public User createUser(String firstName, String lastName, String email, String googleAccountAPI)throws IllegalArgumentException {
		User u = new User();
		u.setFirstName(firstName);
		u.setLastName(lastName);
		u.setEmail(email);
		u.setGoogleAccountAPI(googleAccountAPI);
		/*
		 * Setzen einer vorläufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist. TODO Mit
		 * David besprechen
		 */
		u.setId(1);
		Date now = new Date();
		u.setDateOfCreation(now);
		return this.uMapper.insert(u);
			
	}
	

}
