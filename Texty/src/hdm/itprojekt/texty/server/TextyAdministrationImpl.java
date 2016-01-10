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
import java.util.Collections;
import java.util.Date;
import java.util.Vector;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Die {@link TextyAdministrationImpl} ist die Implementierungsklasse des
 * Interface {@link TextyAdministration}. Diese Klasse enthält sämtliche
 * Applikationslogik.
 */
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

	/**
	 * No-Argument Konstruktor.
	 * 
	 * @throws IllegalArgumentException
	 *             Benötigt für RPC-Core
	 */
	public TextyAdministrationImpl() throws IllegalArgumentException {
	}

	/**
	 * Diese Methode wird aufgerifen um bestehenden Unterhaltungen neue
	 * Nachrichten hinzuzüfügen.
	 * 
	 * @param conversation
	 *            Die Unterhaltung der die Nachricht hinzugeügt werden soll
	 * 
	 * @param text
	 *            Der Nachrichtentext
	 * 
	 * @param listOfHastags
	 *            Die Hashtags welche der Nachricht angehängt wurden
	 * 
	 * @return Die geänderte Unterhaltung
	 */
	@Override
	public Conversation addMessageToConversation(Conversation conversation, String text,
			Vector<Hashtag> listOfHashtag) throws IllegalArgumentException {
		// Abfrage des aktuell eingelogten Benutzers
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();
		User currentUser = this.uMapper.findByEmail(user.getEmail());

		// Erstellen des neuen MessageObjekt und zuweisung der Parameter
		Message m = new Message();
		m.setText(text);
		m.setAuthor(currentUser);
		// Nachricht wird auf sichtbar gesetzt.
		m.setVisible(true);

		/*
		 * Abfrage ob der eingelogte Benutzer Author der letzen der Nachricht in
		 * der Unterhaltung ist. Wenn nicht, wird der Author der letzen
		 * Nachricht als Empfänger hinzugefügt. Außerdem werden alle Empfänger
		 * der letzen Nachricht, exklusive des eingeloggten Benutzers, als
		 * Empfänger der Nachricht hinzugefügt.
		 */
		if (conversation.getLastMessage().getAuthor().getId() != currentUser.getId()) {
			m.addReceivers(conversation.getLastMessage().getAuthor());
			for (User receiver : conversation.getLastMessage().getListOfReceivers()) {
				if (receiver.getId() != currentUser.getId()) {
					m.addReceivers(receiver);
				}
			}
			/*
			 * Ist der eingeloggte Benutzer nicht Author der Letzen Nachricht,
			 * werden die Empfänger der letzen Nachricht für die neue Nachricht
			 * übernommen.
			 */
		} else {
			m.setListOfReceivers(conversation.getLastMessage().getListOfReceivers());
		}

		m.setListOfHashtag(listOfHashtag);
		/*
		 * Setzen der UnterhaltungsID innerhalb der Nachricht um die
		 * Zugehörigkeit zur richtigen Unterhaltung festzuhalten
		 */
		m.setConversationID(conversation.getId());
		conversation.addMessageToConversation(this.mMapper.insert(m));

		return conversation;
	}

	/**
	 * Methode überprüft ob der eingeloggte Google Benutzer bereits in der
	 * Datenbank vorhanden ist und initalisiert ggf. das anlegen des Benutzer.
	 */
	@Override
	public void checkUserData() throws IllegalArgumentException {
		// Abfrage des aktuell eingelogten Benutzers
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();

		/*
		 * Überprüfung ob der eingelogte Benutezr bereits in der Datenbank
		 * vorhanden ist, aonsten wird er erzeugt.
		 */
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
		// Erzeugen des neuen Unterhaltungsobjekt
		Conversation c = new Conversation();
		/*
		 * Abfrage ob Empfänger hinzugefügt wurden, um festzulegen ob die
		 * Unterhaltung öffenltich ist oder nicht.
		 */
		if (listOfReceivers.size() == 0) {
			c.setPublicly(true);
		} else {
			c.setPublicly(false);
		}
		Conversation conversation = this.cMapper.insert(c);
		// Erzeugen der Initalnachricht mit welcher die Unterhaltung erstellt
		// wurde
		Message message = createInitialMessage(text, listOfReceivers,
				listOfHashtag, conversation.getId());
		c.addMessageToConversation(message);
		if (message.getListOfReceivers().isEmpty()) {
			c.setPublicly(true);
		} else {
			c.setPublicly(false);
		}

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
		// Hashtag Objekt wird erzeugt und Parameter zugewiesen
		Hashtag hashtag = new Hashtag();
		hashtag.setKeyword(keyword);
		hashtag.setId(1);

		return this.hMapper.insert(hashtag);
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
		// Abfrage des eingelogten User mit Google API
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();

		com.google.appengine.api.users.User user = userService.getCurrentUser();
		HashtagSubscription hashtagSubscription = new HashtagSubscription();

		hashtagSubscription.setSubscribedHashtag(subscribedHashtag);
		// Der Abonierende ist immer der eingelogte Benutzer
		hashtagSubscription.setSubscriber(this.uMapper.findByEmail(user.getEmail()));

		/*
		 * Setzen einer vorläufigen ID, der Insetz Aufruf liefert dann ein
		 * Objekt, dessen ID konsisten mit der Db ist.
		 */
		hashtagSubscription.setId(1);

		return this.hsMapper.insert(hashtagSubscription);

	}

	/**
	 * Diese Methode erzeugt das Objekt der Initalennachricht, welche während
	 * dem erzeugen einer Unterhaltung eingegeben wird.
	 * 
	 * @param text
	 *            Der Text der Initalennachrit der Unterhaltung
	 * 
	 * @param listOfReceivers
	 *            Die Empfänger der Nachricht bzw der Unterhaltung
	 * 
	 * @param listOfHashtags
	 *            Die angefügten Hashtags
	 * 
	 * @param conversationId
	 *            Die ID der Unterhaltung, zu der die Nachricht gehört
	 * 
	 * @return Die erzeugte Nachricht
	 */
	@Override
	public Message createInitialMessage(String text,
			Vector<User> listOfReceivers, Vector<Hashtag> listOfHashtag,
			int conversationId) throws IllegalArgumentException {
		// Abfrage des eingeloggten Benutzers
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();

		com.google.appengine.api.users.User user = userService.getCurrentUser();

		/*
		 * Erzeugen des neuen Message Objekt und setzen der Parameter
		 */
		Message message = new Message();
		message.setText(text);
		message.setAuthor(this.uMapper.findByEmail(user.getEmail()));
		message.setVisible(true);
		message.setListOfHashtag(listOfHashtag);
		message.setListOfReceivers(listOfReceivers);
		message.setId(1);
		message.setConversationID(conversationId);

		return this.mMapper.insert(message);
	}

	/**
	 * Methode zum erzeugen neuer Benutzer. Die E-Mail Adresse wird dabei von
	 * der Google Accounts Api abgerufen.
	 * 
	 * @return Das erzeugte User Objekt
	 */
	@Override
	public User createUser() throws IllegalArgumentException {
		// Abfrage des eingeloggten Benutzers
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();

		com.google.appengine.api.users.User user = userService.getCurrentUser();

		// Erzeugen des neuen User Objekt und setzen der Parameter
		User u = new User();
		u.setEmail(user.getEmail());
		u.setFirstName("");
		u.setLastName("");
		/*
		 * Setzen einer vorläufigen ID, der Insetz Aufruf liefert dann ein
		 * Objekt, dessen ID konsisten mit der Db ist.
		 */
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
		// Abfrage des eingeloggten Google Benutzers
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();

		com.google.appengine.api.users.User user = userService.getCurrentUser();

		// Erzeugen des neuen UserSubscription Objekt und setzen der Parameter
		UserSubscription us = new UserSubscription();

		us.setSubscribedUser(subscribedUser);
		us.setSubscriber(this.uMapper.findByEmail(user.getEmail()));

		/*
		 * Setzen einer vorläufigen ID, der Insetz Aufruf liefert dann ein
		 * Objekt, dessen ID konsisten mit der Db ist.
		 */
		us.setId(1);

		return this.usMapper.insert(us);
	}

	/**
	 * Methode löscht ein bestehendes Hashtagabonement. Der Abonierende ist
	 * immer der eingelogte Nutzer.
	 * 
	 * @param hashtag
	 *            Der Hashtag auf den das zu löschende Abo besteht. *
	 * 
	 */
	@Override
	public void deleteHashtagSubscription(Hashtag hashtag)
			throws IllegalArgumentException {
		/*
		 * Erstellen eines Hashtagssubscribtion Objekt welches dann an die
		 * delete-Methode zum löschen in der Db übergeben werden kann.
		 */
		HashtagSubscription subscription = new HashtagSubscription();
		// Abfrage des eingeloggten Google Benutzers
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();
		// Abgleich des Google Benutzer mit dem User aus der Db
		User subscriber = this.uMapper.findByEmail(user.getEmail());

		subscription.setSubscriber(subscriber);
		subscription.setSubscribedHashtag(hashtag);
		this.hsMapper.delete(subscription);
	}

	/**
	 * Methode wird während des löschen einer Nachricht aufgerufen. Die
	 * Nachricht wird allerdings nicht wirkliich gelöscht, sondern nur
	 * versteckt.
	 * 
	 * @param conversation
	 *            Die Unterhaltung aus welcher die Nachricht gelöscht bzw.
	 *            ausgeblendet werden soll.
	 * 
	 * @param message
	 *            Die zu löschende bzw. auszublendende Nachricht.
	 */
	@Override
	public void deleteMessage(Conversation conversation, Message message)
			throws IllegalArgumentException {
		conversation.removeMessageFromConversation(message);
		// Sichtbarkeit wird aufgehoben
		message.setVisible(false);
		this.mMapper.delete(message);

	}

	/**
	 * Diese Methode wird zum löschen eines bestehenden Benutzerabonements
	 * aufgerufen. Der Abonnierende ist in diesem Fall immer der eingelogte
	 * Benutzer.
	 * 
	 * @param subsribedUser
	 *            Der Benutzer auf den dass zu löschende Abo besteht.
	 */
	@Override
	public void deleteUserSubscription(User subscribedUser)
			throws IllegalArgumentException {
		/*
		 * Erstellen eines Usersubscribtion Objekt welches dann an die
		 * delete-Methode zum löschen in der Db übergeben werden kann.
		 */
		UserSubscription subscription = new UserSubscription();
		// Abfrage des eingeloggten Google Benutzers
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();

		com.google.appengine.api.users.User user = userService.getCurrentUser();
		User subscriber = this.uMapper.findByEmail(user.getEmail());

		subscription.setSubscriber(subscriber);
		subscription.setSubscribedUser(subscribedUser);

		this.usMapper.delete(subscription);
	}

	/**
	 * Methode wird zum bearbeiten einer Nachricht aufgerufen.
	 * 
	 * @param message
	 *            Das zu bearbeitende Message Objekt
	 * 
	 * @param newText
	 *            Der ggf. neue Nachrichtentext
	 * 
	 * @param listOfHashtags
	 *            Die ggf. geänderte Hashtagliste
	 * 
	 * @return Das geändertete Messageobjekt
	 */
	@Override
	public Message editMessage(Message message, String newText,
			Vector<Hashtag> listOfHashtag) throws IllegalArgumentException {
		// Erzeugen des geänderten Message Objekt und setzen der Parameter.
		Message editedMessage = new Message();
		editedMessage.setId(message.getId());
		editedMessage.setText(newText);
		editedMessage.setListOfHashtag(listOfHashtag);
		return this.mMapper.update(message.getListOfHashtag(), editedMessage);
	}

	/**
	 * Diese Methode liefert alle privaten Unterhaltungen zurück in denen der
	 * aktuell eingelogte Benutzer sich befindet.
	 * 
	 * @return Ein Vektor mit allen Unterhaltungen des Benutzers.
	 */
	@Override
	public Vector<Conversation> getAllConversationsFromUser()
			throws IllegalArgumentException {
		// Abfrage des aktuell eingelogten Google Benutzers
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();
		// Abgleich des Google Benutzer mit dem Nutzer aus der Db
		User currentuser = this.uMapper.findByEmail(user.getEmail());

		// Vector mit allen Nachrichten des aktuellen Benutzers wird erstellt.
		Vector<Message> allMesssagesFromUser = this.mMapper
				.selectAllMessagesFromUser(currentuser);
		// Vector mit allen Unterhaltungen wird erstellt.
		Vector<Conversation> allConversations = this.cMapper
				.selectAllConversations();
		// Ergebnisvector wird erstellt.
		Vector<Conversation> result = new Vector<Conversation>();

		/*
		 * Die Nachrichten des Benutzers, werden in der passenden UNterhaltung
		 * in die Nachrichtenliste eingefügt. Unterhaltungen die keine Nachricht
		 * des Benutzer enthalten, werden nicht mit in den Ergebnisvector
		 * genommen.
		 */
		for (Conversation conversation : allConversations) {
			boolean state = true;
			for (Message message : allMesssagesFromUser) {
				if (message.getConversationID() == conversation.getId()) {
					conversation.addMessageToConversation(message);
					// Sortierung der Nachrichtenliste nach Erstellungsdatum
					Collections.sort(conversation.getListOfMessage());
					if (state) {
						result.add(conversation);
						state = false;
					}
				}
			}
		}
		/*
		 * Setzen des Parameters DateOfLastMessage (Erstellungsdatum der letzen
		 * Nachricht) in allen Unterhaltungen des Ergebnisvectors, damit
		 * anschließend nach diesem die Unterhaltungen sortiert werden können.
		 */
		for (int i = 0; i < result.size(); i++) {
			result.get(i).setDateOfLastMessageInCon(
					result.get(i).getLastMessage().getDateOfCreation());
		}
		/*
		 * Sortieren der Unterhaltungen nach Erstellungsdatum der letzen
		 * Nachricht
		 */
		Collections.sort(result);
		return result;

	}

	/**
	 * Diese Methode gibt alle bereits angelegten Hashtag Objekte zurück.
	 * 
	 * @return Vektor mit allen vorhandenen Hashtags
	 */
	@Override
	public Vector<Hashtag> getAllHashtags() throws IllegalArgumentException {
		return this.hMapper.findAll();
	}

	/**
	 * Diese Methode gibt alle geschriebenen Nachrichten, welche im System
	 * vorhanden sind und innerhalb des Zeitraum liegen, welcher über die
	 * Parameter übergeben wird, zurück.
	 * 
	 * @param startDate
	 *            Das Datum ab welchen die Nachrichten zurückgegeben werden
	 *            sollen.
	 * 
	 * @param endDate
	 *            Das Datum bis zu welchen die Nachrichten zurückgegeben werden
	 *            sollen.
	 * 
	 * @return Die Liste der Nachrichten
	 */
	@Override
	public Vector<Message> getAllMessagesByDate(Date startDate, Date endDate)
			throws IllegalArgumentException {
		// Vektor mit allen Nachrichten aus der Db wird erstellt.
		Vector<Message> allMessages = this.mMapper.selectAllMessages();
		// Ergebnisvektor
		Vector<Message> MessagesByDate = new Vector<Message>();
		/*
		 * Alle Nachrichten werden duchlaufen und darauf geprüft ob das
		 * Erstellungsdatum innerhalb des in den Parametern übergebenen Zeitraum
		 * liegt. Trifft das zu werden die Nachrichten den Ergebnisvektor
		 * hinzugefügt.
		 */
		for (int i = 0; i < allMessages.size(); i++) {
			if (allMessages.get(i).getDateOfCreation().after(startDate)
					&& allMessages.get(i).getDateOfCreation().before(endDate)) {
				MessagesByDate.add(allMessages.get(i));
			}
		}
		// Sortieren der Nachrichten nach Datum
		Collections.sort(MessagesByDate);
		return MessagesByDate;
	}

	/**
	 * Diese Methode liefert alle Nachrichten des übergebenen Benutzers.
	 * 
	 * @param user
	 *            Der Benutzer von dem die Nachrichten abgefragt werden sollen.
	 * 
	 * @return Vektor mit allen Nachrichten des Benutzers
	 */
	@Override
	public Vector<Message> getAllMessagesFromUser(User user)
			throws IllegalArgumentException {
		return this.mMapper.selectAllMessagesFromUser(user);
	}

	/**
	 * Diese Methode gibt alle Nachrichten des übergebenen Benutzers zurück, in
	 * denen er der Author der Nachricht ist.
	 * 
	 * @param user
	 *            Der Benutzer von dem die Nachrichten abgefragt werden sollen.
	 * 
	 * @return Liste mit allen Nachrichten des Benutzer in denen er Author ist.
	 */
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
		Collections.sort(allMessagesWhereUserIsAuthor);
		return allMessagesWhereUserIsAuthor;
	}

	public Vector<Message> getAllMessagesWhereUserIsAuthorByDate(User user,
			Date startDate, Date endDate) throws IllegalArgumentException {
		Vector<Message> allMessagesFromUser = this.mMapper
				.selectAllMessagesFromUser(user);
		Vector<Message> allMessagesWhereUserIsAuthor = new Vector<Message>();
		Vector<Message> allMessagesWhereUserIsAuthorByDate = new Vector<Message>();
		for (int i = 0; i < allMessagesFromUser.size(); i++) {
			if (allMessagesFromUser.get(i).getAuthor().getId() == user.getId()) {
				allMessagesWhereUserIsAuthor.add(allMessagesFromUser.get(i));
			}
		}
		for (int i = 0; i < allMessagesWhereUserIsAuthor.size(); i++) {
			if (allMessagesWhereUserIsAuthor.get(i).getDateOfCreation()
					.after(startDate)
					&& allMessagesWhereUserIsAuthor.get(i).getDateOfCreation()
							.before(endDate)) {
				allMessagesWhereUserIsAuthorByDate
						.add(allMessagesWhereUserIsAuthor.get(i));
			}
		}
		Collections.sort(allMessagesWhereUserIsAuthorByDate);
		return allMessagesWhereUserIsAuthorByDate;
	}

	/**
	 * Diese Methode erstellt eine Liste mit allen Nachrichten des übergebenen
	 * Benutzers, innerhalb eines Zeitraums zurück. Ähnlich der Methode
	 * {@link #getAllMessagesByDate(Date, Date)} nur auf einen Benutzer bezogen
	 * und einen Zeitraum.
	 * 
	 * @param user
	 *            Der Benutzer von dem die Nachrichten abgefragt werden sollen.
	 * 
	 * @param startDate
	 */
	@Override
	public Vector<Message> getAllMessagesFromUserByDate(User user,
			Date startDate, Date endDate) throws IllegalArgumentException {
		Vector<Message> allMessages = this.mMapper
				.selectAllMessagesFromUser(user);
		Vector<Message> MessagesByDate = new Vector<Message>();
		for (int i = 0; i < allMessages.size(); i++) {
			if (allMessages.get(i).getDateOfCreation().after(startDate)
					&& allMessages.get(i).getDateOfCreation().before(endDate)) {
				MessagesByDate.add(allMessages.get(i));
			}
		}
		Collections.sort(MessagesByDate);
		return MessagesByDate;
	}

	/**
	 * Methode zum ausgeben aller öffentlichen Conversations (mit Messages)
	 * eines Benutzers.
	 * 
	 * @return Eine Liste mit allen öffentlichen Conversations
	 */
	@Override
	public Vector<Conversation> getAllPublicConversationsFromUser(User user)
			throws IllegalArgumentException {

		Vector<Conversation> allPublicConversations = this.cMapper
				.selectAllPublicConversations();

		Vector<Conversation> result = new Vector<Conversation>();

		for (Conversation conversation : allPublicConversations) {

			conversation.setListOfMessage(this.mMapper
					.selectAllMesagesFromConversation(conversation));
			conversation.setDateOfLastMessageInCon(conversation
					.getLastMessage().getDateOfCreation());
			Collections.sort(conversation.getListOfMessage());

			if (conversation.getFirstMessage().getAuthor().getId() == user
					.getId()) {
				result.add(conversation);

			}
		}
		Collections.sort(result);
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

	/**
	 * Methode gibt eine Liste zurück mit allen Hashtags, die er Benutzer
	 * abonniert hat.
	 * 
	 * @param selectedUser
	 *            Der Benutzer von dem die abonierten Hashtag benötigt werden.
	 * 
	 * @return Vektor mit den Hashtags
	 */
	@Override
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

	/**
	 * Diese Methode liefert alle öffentlichen Nachrichten die den übergebenen
	 * Hashtag angehängt haben.
	 * 
	 * @param selectedHashtag
	 *            Der gewählte Hashtag
	 * 
	 * @return Ein Vektor mit allen Nachrichten mit dem Hashtag
	 */
	@Override
	public Vector<Message> getAllPublicMessagesFromHashtag(
			Hashtag selectedHashtag) throws IllegalArgumentException {
		Vector<Message> result = this.mMapper
				.selectAllPublicMessagesWithHashtag(selectedHashtag);
		Collections.sort(result);
		Collections.reverse(result);
		return result;
	}

	/**
	 * Diese Methode liefert einen Vektor mit allen Benutzern, die der übergeben
	 * Benutezr aboniert hat.
	 * 
	 * @param selectedUser
	 *            Der Benutzer von dem die abonierten Benutzer benötigt werden.
	 * 
	 * @return Vektor mit den Benutzern
	 */
	@Override
	public Vector<User> getAllSubscribedUsersFromUser(User selectedUser)
			throws IllegalArgumentException {
		return this.usMapper.selectAllSubscribedUsers(selectedUser);
	}

	/**
	 * Diese Methode liefert alle Benutzer des Systems.
	 * 
	 * @return Vektor mit den Benutzerobjekten
	 */
	@Override
	public Vector<User> getAllUsers() throws IllegalArgumentException {
		return this.uMapper.findAll();
	}

	/**
	 * Diese Methode gibt das User Objekt des aktuell eingelogten Benutzers
	 * zurück
	 * 
	 * @return Der eingelogte Benutezr
	 */
	@Override
	public User getCurrentUser() throws IllegalArgumentException {
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory
				.getUserService();

		com.google.appengine.api.users.User user = userService.getCurrentUser();
		User us = this.uMapper.findByEmail(user.getEmail());
		return us;
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
