package hdm.itprojekt.texty.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import hdm.itprojekt.texty.shared.TextyAdministration;
import hdm.itprojekt.texty.shared.bo.*;
import hdm.itprojekt.texty.server.db.*;

import java.util.*;

public class TextyAdministrationImpl extends RemoteServiceServlet implements
		TextyAdministration {

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
	public Message createInitialMessage(String text,
			Vector<User> listOfReceivers, Vector<Hashtag> listOfHashtag)
			throws IllegalArgumentException {
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();

		com.google.appengine.api.users.User user = userService.getCurrentUser();
		
		Message m = new Message();
		m.setText(text);
		m.setAuthor(this.uMapper.findByEmail(user.getEmail()));
		m.setVisible(true);
		m.setListOfHashtag(listOfHashtag);
		m.setListOfReceivers(listOfReceivers);
		m.setId(1);
		m.setConversationID(1);

		return this.mMapper.insert(m);
	}

	/**
	 * Methode erzeugt ein neues Hashtagobjekt und ruft die insert-Methode
	 * {@link hdm.itprojekt.texty.server.db.HashtagMapper#insert(Hashtag)} auf
	 * um den Hashtag in die Datenbank zu schreiben
	 * 
	 * @param keyword
	 *            Das Hashtag dass der Benutzer in GUI eingibt
	 * 
	 * @return Das Erzeugte Hashtag Objekt
	 */
	@Override
	public Hashtag createHashtag(String keyword)
			throws IllegalArgumentException {
		Hashtag h = new Hashtag();
		h.setKeyword(keyword);
		h.setId(1);

		return this.hMapper.insert(h);
	}

	/**
	 * Methode erzeugt ein neues Unterhaltungsobjekt
	 * 
	 * @param Text
	 *            Der initale Nachrichtentext mit dem der Benutzer die
	 *            Unterhaltung erzeugt.
	 * @param author
	 *            Der Ersteller der Unterhaltung bzw. der Initalnachricht.
	 * @param listofReceivers
	 *            Die gewünschten Adressaten der Unterhaltung. Dieser Wert ist
	 *            null, wenn es eine öffentliche Unterhaltung ist.
	 * @param listOfHashtags
	 *            Die Hashtags die der Nachricht angehängt werden sollen.
	 * 
	 * @return Das erzeugt Unterhaltunsobjekt
	 */
	@Override
	public Conversation createConversation(String text,
			Vector<User> listOfReceivers, Vector<Hashtag> listOfHashtag)
			throws IllegalArgumentException {
		Conversation c = new Conversation();
		Message message = createInitialMessage(text, listOfReceivers,
				listOfHashtag);
		c.addMessageToConversation(message);
		if (message.getListOfReceivers() == null) {
			c.setPublicly(true);
		} else {
			c.setPublicly(false);
		}
		c.setId(1);

		return this.cMapper.insert(c);
	}

	/**
	 * Methode erzeugt ein neues Benutzerabo Objekt
	 * 
	 * @param subscribedUser
	 *            Der Benutzer der abonniert werden soll
	 * @param subscriber
	 *            Der Benutzer der abonnieren möchte
	 * 
	 * @return Das erzeugte Objekt
	 */

	public UserSubscription createUserSubscription(User subscribedUser,
			User subscriber) {
		UserSubscription us = new UserSubscription();

		us.setSubscribedUser(subscribedUser);
		us.setSubscriber(subscriber);

		us.setId(1);

		return this.usMapper.insert(us);

	}

	public HashtagSubscription createHashtagSubscription(
			Hashtag subscribedHashtag) {
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();

		com.google.appengine.api.users.User user = userService.getCurrentUser();
		HashtagSubscription hs = new HashtagSubscription();

		hs.setSubscribedHashtag(subscribedHashtag);
		hs.setSubscriber(this.uMapper.findByEmail(user.getEmail()));

		hs.setId(1);

		return this.hsMapper.insert(hs);

	}

	public User createUser() throws IllegalArgumentException {
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();

		com.google.appengine.api.users.User user = userService.getCurrentUser();

		User u = new User();
		// u.setNickName(user.getNickname());
		u.setEmail(user.getEmail());
		u.setFirstName("");
		u.setLastName("");
		u.setId(1);

		return this.uMapper.insert(u);
	}

	public void checkUserData() throws IllegalArgumentException {
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();

		if (this.uMapper.findByEmail(user.getEmail()) == null) {
			createUser();
		}
	}

	public void updateUserData(String firstName, String lastName)
			throws IllegalArgumentException {
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();

		com.google.appengine.api.users.User user = userService.getCurrentUser();
		User us = this.uMapper.findByEmail(user.getEmail());
		if (us != null) {
			us.setFirstName(firstName);
			us.setLastName(lastName);
			this.uMapper.update(us);
		}
	}

	@Override
	public Vector<Hashtag> getAllSubscribedHashtags(User user)
			throws IllegalArgumentException {
		// TODO Neu autogenerated, schauen wo fehler war
		return null;
	}

	public Vector<User> getAllSubscribedUsers(User user)
			throws IllegalArgumentException {
		return this.usMapper.selectAllSubscribedUsers(user);
	}

	public Message editMessage(Message message, String newText)
			throws IllegalArgumentException {
		message.setText(newText);
		return this.mMapper.update(message);
	}

	public Message addMessageToConversation(Conversation c, String text,
			Vector<Hashtag> listOfHashtag)
			throws IllegalArgumentException {
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();
		
		Message m = new Message();
		m.setText(text);
		m.setAuthor(this.uMapper.findByEmail(user.getEmail()));
		m.setListOfReceivers(c.getLastMessage().getListOfReceivers());
		m.setListOfHashtag(listOfHashtag);
		m.setId(1);
		m.setConversationID(1);
		c.addMessageToConversation(m);

		return this.mMapper.insert(m);
	}

	public void deleteMessage(Conversation conversation, Message message)
			throws IllegalArgumentException {
		conversation.removeMessageFromConversation(message);
		message.setVisible(false);
		this.getmMapper().update(message);

	}

	public void deleteUserSubscription(UserSubscription subscription)
			throws IllegalArgumentException {
		this.usMapper.delete(subscription);
	}

	public void deleteHashtagSubscription(HashtagSubscription subscription)
			throws IllegalArgumentException {
		this.hsMapper.delete(subscription);
	}

	public Vector<Message> getAllMessagesFromUserByDate(User user,
			Date startDate, Date endDate) throws IllegalArgumentException {
		Vector<Message> allMessages = this.mMapper
				.selectAllMessagesFromUser(user);
		Vector<Message> MessagesByDate = new Vector<Message>();
		for (int i = allMessages.size(); i > 0; i--) {
			if (allMessages.get(i).getDateOfCreation().after(startDate)
					&& allMessages.get(i).getDateOfCreation().before(endDate)) {
				MessagesByDate.add(allMessages.get(i));
			}
		}
		return MessagesByDate;
	}

	public Vector<Message> getAllMessagesByDate(Date startDate, Date endDate)
			throws IllegalArgumentException {
		Vector<Message> allMessages = this.mMapper.selectAllMessages();
		Vector<Message> MessagesByDate = new Vector<Message>();
		for (int i = allMessages.size(); i > 0; i--) {
			if (allMessages.get(i).getDateOfCreation().after(startDate)
					&& allMessages.get(i).getDateOfCreation().before(endDate)) {
				MessagesByDate.add(allMessages.get(i));
			}
		}
		return MessagesByDate;
	}

	public Vector<Message> getAllMessagesFromUser(User user)
			throws IllegalArgumentException {
		return this.mMapper.selectAllMessagesFromUser(user);
	}

	public Vector<Conversation> getAllPublicConversationsFromUser(User user)
			throws IllegalArgumentException {
		Vector<Conversation> allPublicConversations = new Vector<Conversation>();
		Vector<Conversation> allConversations = this.cMapper
				.findAllConversations();
		for (int i = allConversations.size(); i > 0; i--) {
			if (allConversations.get(i).isPublicly()) {
				allPublicConversations.add(allConversations.get(i));
			}
		}
		return allPublicConversations;
	}

	public Vector<User> getAllUsers() throws IllegalArgumentException {
		return this.uMapper.findAll();
	}
	
	public Vector<Hashtag> getAllHashtags() throws IllegalArgumentException {
		return this.hMapper.findAll();
		}

	public User getCurrentUser() throws IllegalArgumentException {
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();

		com.google.appengine.api.users.User user = userService.getCurrentUser();
		User us = this.uMapper.findByEmail(user.getEmail());
		return us;
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
