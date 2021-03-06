package hdm.itprojekt.texty.shared;

import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.HashtagSubscription;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;
import hdm.itprojekt.texty.shared.bo.UserSubscription;
import java.util.Date;
import java.util.Vector;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Sychrones Interface f�r RPC F�hige Klasse zu verwalten des Messengers F�r
 * eine detailierte Dokumentation der einzelnen Methoden sie
 * {@link hdm.itprojekt.texty.server.TextyAdministrationImpl} .
 */
@RemoteServiceRelativePath("administration")
public interface TextyAdministration extends RemoteService {

	public Message addMessageToConversation(Message lastMessage,
			int conversationId, String text, Vector<Hashtag> listOfHashtag)
			throws IllegalArgumentException;

	public void checkUserData() throws IllegalArgumentException;

	public Conversation createConversation(String text,
			Vector<User> listOfReceivers, Vector<Hashtag> listOfHashtag)
			throws IllegalArgumentException;

	public Hashtag createHashtag(String keyword)
			throws IllegalArgumentException;

	public HashtagSubscription createHashtagSubscription(
			Hashtag subscribedHashtag) throws IllegalArgumentException;

	public Message createInitialMessage(String text,
			Vector<User> listOfReceiver, Vector<Hashtag> listOfHashtag,
			int conversationId) throws IllegalArgumentException;

	public User createUser() throws IllegalArgumentException;

	public UserSubscription createUserSubscription(User subscribedUser)
			throws IllegalArgumentException;

	public void deleteHashtagSubscription(Hashtag hashtag)
			throws IllegalArgumentException;

	public void deleteMessage(Conversation conversation, Message message)
			throws IllegalArgumentException;

	public void deleteUserSubscription(User user)
			throws IllegalArgumentException;

	public Message editMessage(Message message, String newText,
			Vector<Hashtag> listOfHashtag) throws IllegalArgumentException;

	public Vector<Conversation> getAllConversationsFromUser()
			throws IllegalArgumentException;

	public Vector<User> getAllFollowersFromHashtag(Hashtag selectedHashtag)
			throws IllegalArgumentException;

	public Vector<User> getAllFollowerFromUser(User selectedUser)
			throws IllegalArgumentException;

	public Vector<Hashtag> getAllHashtags() throws IllegalArgumentException;

	public Vector<Message> getAllMessagesByDate(Date startDate, Date endDate)
			throws IllegalArgumentException;

	public Vector<Message> getAllMessagesFromUser(User user)
			throws IllegalArgumentException;

	public Vector<Message> getAllMessagesWhereUserIsAuthor(User user)
			throws IllegalArgumentException;

	public Vector<Message> getAllMessagesWhereUserIsAuthorByDate(User user,
			Date startDate, Date endDate) throws IllegalArgumentException;

	public Vector<Message> getAllMessagesFromUserByDate(User user,
			Date startDate, Date endDate) throws IllegalArgumentException;

	public Vector<Message> getAllMessagesFromHashtag(Hashtag selectedHashtag)
			throws IllegalArgumentException;

	public Vector<Conversation> getAllPublicConversationsFromUser(User user)
			throws IllegalArgumentException;

	public Vector<Hashtag> getAllSubscribedHashtags()
			throws IllegalArgumentException;

	public Vector<Hashtag> getAllSubscribedHashtagsFromUser(User selectedUser)
			throws IllegalArgumentException;

	public Vector<User> getAllSubscribedUsers() throws IllegalArgumentException;

	public Vector<User> getAllSubscribedUsersFromUser(User selectedUser)
			throws IllegalArgumentException;

	public Vector<Message> getAllPublicMessagesFromHashtag(
			Hashtag selectedHashtag) throws IllegalArgumentException;

	public Vector<User> getAllUsers() throws IllegalArgumentException;

	public User getCurrentUser() throws IllegalArgumentException;

	public void init() throws IllegalArgumentException;

	public void updateUserData(String firstName, String lastName)
			throws IllegalArgumentException;

	public Vector<Message> getRecentMessages(Message message)
			throws IllegalArgumentException;

}
