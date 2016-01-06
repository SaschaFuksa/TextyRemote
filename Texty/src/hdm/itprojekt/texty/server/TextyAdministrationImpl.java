package hdm.itprojekt.texty.server;

import hdm.itprojekt.texty.server.db.ConversationMapper;
import hdm.itprojekt.texty.server.db.HashtagMapper;
import hdm.itprojekt.texty.server.db.HashtagSubscriptionMapper;
import hdm.itprojekt.texty.server.db.MessageMapper;
import hdm.itprojekt.texty.server.db.UserMapper;
import hdm.itprojekt.texty.server.db.UserSubscriptionMapper;
import hdm.itprojekt.texty.shared.TextyAdministration;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.HashtagSubscription;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;
import hdm.itprojekt.texty.shared.bo.UserSubscription;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TextyAdministrationImpl extends RemoteServiceServlet implements
		TextyAdministration {

	private static final long serialVersionUID = 1L;
	/**
	 * Referenz auf den DatenbankMapper, welcher Unterhaltungsobjekte mit der
	 * Datenbank abgleicht.
	 */
	private ConversationMapper cMapper = null;
	/**
	 * Referenz auf den DatenbankMapper, welcher Hashtagobjekte mit der
	 * Datenbank abgleicht.
	 */
	private HashtagMapper hMapper = null;
	/**
	 * Referenz auf den DatenbankMapper, welcher Hashtagsaboobjekte mit der
	 * Datenbank abgleicht.
	 */
	private HashtagSubscriptionMapper hsMapper = null;
	/**
	 * Referenz auf den DatenbankMapper, welcher Nachrichtenobjekte mit der
	 * Datenbank abgleicht.
	 */
	private MessageMapper mMapper = null;
	/**
	 * Referenz auf den DatenbankMapper, welcher Benutzerobjekte mit der
	 * Datenbank abgleicht.
	 */
	private UserMapper uMapper = null;
	/**
	 * Referenz auf den DatenbankMapper, welcher Benutzeraboobjekte mit der
	 * Datenbank abgleicht.
	 */
	private UserSubscriptionMapper usMapper = null;

	/*
	 * ************************************************************************
	 * ABSCHNITT, Beginn: Initialisierung
	 * **************************************** *******************************
	 */

	/**
	 * No-Argument Konstruktor.
	 * 
	 * @throws IllegalArgumentException
	 *             Benötigt für RPC-Core
	 */
	public TextyAdministrationImpl() throws IllegalArgumentException {
	}

	@Override
	public Conversation addMessageToConversation(Conversation c, String text,
			Vector<Hashtag> listOfHashtag) throws IllegalArgumentException {
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();
		User currentUser = this.uMapper.findByEmail(user.getEmail());

		Message m = new Message();
		m.setText(text);
		m.setAuthor(currentUser);
		m.setVisible(true);

		if (c.getLastMessage().getAuthor().getId() != currentUser.getId()) {
			m.addReceivers(c.getLastMessage().getAuthor());
			for (User receiver : c.getLastMessage().getListOfReceivers()) {
				if (receiver.getId() != currentUser.getId()) {
					m.addReceivers(receiver);
				}
			}
		} else {
			m.setListOfReceivers(c.getLastMessage().getListOfReceivers());
		}

		m.setListOfHashtag(listOfHashtag);
		m.setConversationID(c.getId());
		c.addMessageToConversation(this.mMapper.insert(m));

		return c;
	}

	/*
	 * **********************************************************************
	 * ABSCHNITT, Ende: Initialisierung
	 * **********************************************************************
	 */

	/*
	 * ************************************************************************
	 * ABSCHNITT, Beginn: Methoden zum erzeugen von allen Business Objekten
	 * **************************************** *******************************
	 */

	/**
	 * Methode überprüft ob der eingeloggte Google Benutzer bereits in der
	 * Datenbank vorhanden ist und initalisiert ggf. das anlegen des Benutzer.
	 */
	@Override
	public void checkUserData() throws IllegalArgumentException {
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();

		if (this.uMapper.findByEmail(user.getEmail()) == null) {
			createUser();
		}
	}

	/**
	 * Methode erzeugt ein neues Unterhaltungsobjekt
	 * 
	 * @param Text
	 *            Der initale Nachrichtentext mit dem der Benutzer die
	 *            Unterhaltung erzeugt.
	 * @param listofReceivers
	 *            Die gewünschten Adressaten der Unterhaltung. Dieser Wert ist
	 *            null, wenn es eine öffentliche Unterhaltung ist.
	 * @param listOfHashtags
	 *            Die Hashtags die der Nachricht angehängt werden sollen.
	 * 
	 * @return Das erzeugte Unterhaltunsobjekt
	 */
	@Override
	public Conversation createConversation(String text,
			Vector<User> listOfReceivers, Vector<Hashtag> listOfHashtag)
			throws IllegalArgumentException {
		Conversation c = new Conversation();
		if (listOfReceivers.size() == 0) {
			c.setPublicly(true);
		} else {
			c.setPublicly(false);
		}
		Conversation conversation = this.cMapper.insert(c);
		Message message = createInitialMessage(text, listOfReceivers,
				listOfHashtag, conversation.getId());
		c.addMessageToConversation(message);
		if (message.getListOfReceivers().isEmpty()) {
			c.setPublicly(true);
		} else {
			c.setPublicly(false);
		}
		c.setId(1);

		return conversation;
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
	 * Methode zum erzeugen eines neuen Hashtagabonnements. Der abonnierende ist
	 * immer der aktuell eingeloggte Benutzer.
	 * 
	 * @param subscribedHashtags
	 *            Das Hashtag das erzeugt werden soll.
	 * 
	 * @return Das erzeugte Objekt vom Typ HashtagSubscribtion
	 */
	@Override
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

	@Override
	public Message createInitialMessage(String text,
			Vector<User> listOfReceivers, Vector<Hashtag> listOfHashtag,
			int conversationId) throws IllegalArgumentException {
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
		m.setConversationID(conversationId);

		return this.mMapper.insert(m);
	}

	/**
	 * Methode zum erzeugen neuer Benutzer. Die E-Mail Adresse wird dabei von
	 * der Google Accounts Api abgerufen.
	 * 
	 * @return Das erzeugte User Objekt
	 */

	@Override
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

	/**
	 * Methode erzeugt ein neues Benutzerabo Objekt der abonnierende ist immer
	 * der aktuell eingeloggte User.
	 * 
	 * @param subscribedUser
	 *            Der Benutzer der abonniert werden soll
	 * 
	 * @return Das erzeugte Objekt
	 */
	@Override
	public UserSubscription createUserSubscription(User subscribedUser) {
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();

		com.google.appengine.api.users.User user = userService.getCurrentUser();

		UserSubscription us = new UserSubscription();

		us.setSubscribedUser(subscribedUser);
		us.setSubscriber(this.uMapper.findByEmail(user.getEmail()));

		us.setId(1);

		return this.usMapper.insert(us);

	}

	@Override
	public void deleteHashtagSubscription(Hashtag hashtag)
			throws IllegalArgumentException {
		HashtagSubscription subscription = new HashtagSubscription();
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();
		User subscriber = this.uMapper.findByEmail(user.getEmail());

		subscription.setSubscriber(subscriber);
		subscription.setSubscribedHashtag(hashtag);
		this.hsMapper.delete(subscription);
	}

	@Override
	public void deleteMessage(Conversation conversation, Message message)
			throws IllegalArgumentException {
		conversation.removeMessageFromConversation(message);
		message.setVisible(false);
		this.getmMapper().delete(message);

	}

	@Override
	public void deleteUserSubscription(User subscribedUser)
			throws IllegalArgumentException {
		UserSubscription subscription = new UserSubscription();

		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();

		com.google.appengine.api.users.User user = userService.getCurrentUser();
		User subscriber = this.uMapper.findByEmail(user.getEmail());

		subscription.setSubscriber(subscriber);
		subscription.setSubscribedUser(subscribedUser);

		this.usMapper.delete(subscription);
	}

	@Override
	public Message editMessage(Message message, String newText,
			Vector<Hashtag> listOfHashtag) throws IllegalArgumentException {
		Message editedMessage = new Message();
		editedMessage.setId(message.getId());
		editedMessage.setText(newText);
		editedMessage.setListOfHashtag(listOfHashtag);
		return this.mMapper.update(message.getListOfHashtag(), editedMessage);
	}

	@Override
	public Vector<Conversation> getAllConversationsFromUser()
			throws IllegalArgumentException {

		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();
		User currentuser = this.uMapper.findByEmail(user.getEmail());

		Vector<Message> allMesssagesFromUser = this.mMapper
				.selectAllMessagesFromUser(currentuser);
		Vector<Conversation> allConversations = this.cMapper
				.selectAllConversations();
		Vector<Conversation> result = new Vector<Conversation>();

		for (Conversation conversation : allConversations) {
			boolean state = true;
			for (Message message : allMesssagesFromUser) {
				if (message.getConversationID() == conversation.getId()) {
					conversation.addMessageToConversation(message);
					if (state) {
						result.add(conversation);
						state = false;
					}
				}
			}
		}
		return result;

	}

	@Override
	public Vector<Hashtag> getAllHashtags() throws IllegalArgumentException {
		return this.hMapper.findAll();
	}

	@Override
	public Vector<Message> getAllMessagesByDate(Date startDate, Date endDate)
			throws IllegalArgumentException {		
		
		Vector<Message> allMessages = this.mMapper.selectAllMessages();
		Vector<Message> MessagesByDate = new Vector<Message>();
		for (int i = 0; i < allMessages.size(); i++) {
			if (allMessages.get(i).getDateOfCreation().after(startDate)
					&& allMessages.get(i).getDateOfCreation().before(endDate)) {
				MessagesByDate.add(allMessages.get(i));
			}
		}
		return MessagesByDate;
	}

	@Override
	public Vector<Message> getAllMessagesFromUser(User user)
			throws IllegalArgumentException {
		return this.mMapper.selectAllMessagesFromUser(user);
	}

	@Override
	public Vector<Message> getAllMessagesWhereUserIsAuthor(User user)
			throws IllegalArgumentException {
		Vector<Message> allMessagesFromUser = this.mMapper
				.selectAllMessagesFromUser(user);
		Vector<Message> allMessagesWhereUserIsAuthor = new Vector<Message>();
		for (int i = 0; i < allMessagesFromUser.size(); i++) {
			if (allMessagesFromUser.get(i).getAuthor().getId() == user.getId()) {
				allMessagesWhereUserIsAuthor.add(allMessagesFromUser.get(i));
			}
		}
		return allMessagesWhereUserIsAuthor;
	}

	@Override
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

	
	/**
	 * Methode zum ausgeben aller öffentlichen Conversations (mit Messages) eines Benutzers.
	 * 
	 * @return Eine Liste mit allen öffentlichen Conversations
	 */
	@Override
	
	public Vector<Conversation> getAllPublicConversationsFromUser(User user)
			throws IllegalArgumentException {
		
		Vector<Message> allMessagesFromUser = this.mMapper
				.selectAllMessagesFromUser(user);
		
		Vector<Conversation> allConversations = this.cMapper
				.selectAllConversations();
		
		Vector<Conversation> result = new Vector<Conversation>();

		
		for (Conversation conversation : allConversations) {
			boolean state = true;
			for (Message message : allMessagesFromUser){
				if (message.getConversationID() == conversation.getId() && conversation.isPublicly()){
					conversation.addMessageToConversation(message);
					if (state){
						result.add(conversation);
						state = false;
					}
				}
			}
		} 
		return result;
	}
					

	/**
	 * Methode zum auslesen aller Hashtagabonnements des eingeloggten Benutzers
	 * 
	 * @return Eine Liste mit den abonnierten Hashtags
	 */
	@Override
	public Vector<Hashtag> getAllSubscribedHashtags()
			throws IllegalArgumentException {
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();

		User us = this.uMapper.findByEmail(user.getEmail());
		return this.hsMapper.selectAllSubscribedHashtags(us);
	}

	public Vector<Hashtag> getAllSubscribedHashtagsFromUser(User selectedUser)
			throws IllegalArgumentException {
		return this.hsMapper.selectAllSubscribedHashtags(selectedUser);
	}

	/**
	 * Methode zum auslesen aller Nutzerabonnements des eingeloggten Benutzers
	 * 
	 * @return Einen User-Vector mit allen abonnierten Benutzern
	 */
	@Override
	public Vector<User> getAllSubscribedUsers() throws IllegalArgumentException {
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();
		User us = this.uMapper.findByEmail(user.getEmail());
		return this.usMapper.selectAllSubscribedUsers(us);
	}

	@Override
	public Vector<Message> getAllPublicMessagesFromHashtag(
			Hashtag selectedHashtag) throws IllegalArgumentException {
		return this.mMapper.selectAllPublicMessagesWithHashtag(selectedHashtag);
	}

	@Override
	public Vector<User> getAllSubscribedUsersFromUser(User selectedUser)
			throws IllegalArgumentException {
		return this.usMapper.selectAllSubscribedUsers(selectedUser);
	}

	@Override
	public Vector<User> getAllUsers() throws IllegalArgumentException {
		return this.uMapper.findAll();
	}

	/**
	 * @return der ConversationMapper
	 */
	public ConversationMapper getcMapper() {
		return cMapper;
	}

	@Override
	public User getCurrentUser() throws IllegalArgumentException {
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();

		com.google.appengine.api.users.User user = userService.getCurrentUser();
		User us = this.uMapper.findByEmail(user.getEmail());
		return us;
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

	@Override
	public void init() throws IllegalArgumentException {
		this.cMapper = ConversationMapper.conversationMapper();
		this.hMapper = HashtagMapper.hashtagMapper();
		this.hsMapper = HashtagSubscriptionMapper.hashtagSubscriptionMapper();
		this.mMapper = MessageMapper.messageMapper();
		this.uMapper = UserMapper.userMapper();
		this.usMapper = UserSubscriptionMapper.userSubscriptionMapper();

	}

	/*
	 * ************************************************************************
	 * ABSCHNITT, Ende: Methoden zum erzeugen von allen Business Objekten
	 * **************************************** *******************************
	 */
	/**
	 * Methode zum aktualiesieren oder setzen des Vor- und Nachnamen des
	 * eingeloggten Benutzers. Vor- und Nachname kann ein Nutzer in der GUI per
	 * eingabe selber festlegen.
	 * 
	 * @param firstName
	 *            Der eingebebene Vorname des Benutzers
	 * 
	 * @param lastName
	 *            der eingebene Nachname des Benutzers
	 *
	 */
	@Override
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

}
