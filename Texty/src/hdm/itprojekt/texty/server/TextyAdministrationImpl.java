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
	public Message createMessage(String text, User author, Boolean visible,
			Vector<Hashtag> listOfHashtag) throws IllegalArgumentException {
		Message m = new Message();
		m.setText(text);
		m.setAuthor(author);
		m.setVisible(visible);
		m.setListOfHashtag(listOfHashtag);

		/*
		 * TODO Mit anderen GUI Methoden besprechen ( Wie wird was �bergeben)
		 */

		/*
		 * Setzen einer vorl�ufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist. TODO Mit
		 * David besprechen
		 */
		m.setId(1);

		Date now = new Date();
		m.setDateOfCreation(now);

		return this.mMapper.insert(m);
	}

	@Override
	public Hashtag createHashtag(String keyword)
			throws IllegalArgumentException {
		Hashtag h = new Hashtag();
		h.setKeyword(keyword);

		/*
		 * Setzen einer vorl�ufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist. TODO Mit
		 * David besprechen
		 */
		h.setId(1);
		Date now = new Date();
		h.setDateOfCreation(now);

		return this.hMapper.insert(h);
	}

	@Override
	public Conversation createConversation(Boolean publicly,
			Vector<Message> listOfMessage, Vector<User> listOfParticipant)
			throws IllegalArgumentException {
		Conversation c = new Conversation();

		c.setPublicly(publicly);
		c.setListOfMessage(listOfMessage);
		c.setListOfParticipant(listOfParticipant);

		/*
		 * Setzen einer vorl�ufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist. TODO Mit
		 * David besprechen
		 */
		c.setId(1);
		Date now = new Date();
		c.setDateOfCreation(now);

		return this.cMapper.insert(c);
	}

	public UserSubscription createUserSubscription(User subscribedUser,
			User subscriber) {
		UserSubscription us = new UserSubscription();

		us.setSubscribedUser(subscribedUser);
		us.setSubscriber(subscriber);

		/*
		 * Setzen einer vorl�ufigen Kundennr. Der insert-Aufruf liefert dann ein
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
		 * Setzen einer vorl�ufigen Kundennr. Der insert-Aufruf liefert dann ein
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

	// TODO N�chste Woche durchsprechen ob wir diese Methode �berhaupt
	// ben�tigen.
	public User createUser(String firstName, String lastName, String email,
			String googleAccountAPI) throws IllegalArgumentException {
		User u = new User();
		u.setFirstName(firstName);
		u.setLastName(lastName);
		u.setEmail(email);
		u.setGoogleAccountAPI(googleAccountAPI);
		/*
		 * Setzen einer vorl�ufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist. TODO Mit
		 * David besprechen
		 */
		u.setId(1);
		Date now = new Date();
		u.setDateOfCreation(now);
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

	public Conversation editConversation(Conversation conversation, User user)
			throws IllegalArgumentException, SQLException {
		conversation.removeParticipant(user);
		/*
		 * TODO Methode Fehlt in DB Connection M�ssen wir jedes mal
		 * closeConnection machen? DBConnection.closeConnection();
		 */
		return this.cMapper.update(conversation);
	}

	public Vector<Hashtag> getAllSubscribedHashtags(User user)
			throws IllegalArgumentException {
		/*
		 * TODO Methode fehllt in den Mappern @David
		 */
		// return this.uMapper.findHashtags(user);
		return null;
	}

	public Message addMessageToConversation(Conversation c, Message m,
			String text, User author, Boolean visible,
			Vector<Hashtag> listOfHashtag) throws IllegalArgumentException {
		Message message = createMessage(text, author, visible, listOfHashtag);
		c.addMessageToVector(message);
		// TODO Mit den anderen besprechen, woher wei� db welche Message zu
		// welcher conversation geh�rt.
		this.mMapper.insert(message);
		return null;
	}

	public void deleteMessage(Conversation conversation, Message message)
			throws IllegalArgumentException {
		conversation.removeMessageFromConversation(message);
		this.getmMapper().delete(message);
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
