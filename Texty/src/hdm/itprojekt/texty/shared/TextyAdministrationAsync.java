package hdm.itprojekt.texty.shared;

import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.HashtagSubscription;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;
import hdm.itprojekt.texty.shared.bo.UserSubscription;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TextyAdministrationAsync {

	void addMessageToConversation(Conversation c, String text,
			Vector<Hashtag> listOfHashtag, AsyncCallback<Conversation> callback);

	void checkUserData(AsyncCallback<Void> callback);

	void createConversation(String text, Vector<User> listOfReceivers,
			Vector<Hashtag> listOfHashtag, AsyncCallback<Conversation> callback);

	void createHashtag(String keyword, AsyncCallback<Hashtag> callback);

	void createHashtagSubscription(Hashtag subscribedHashtag,
			AsyncCallback<HashtagSubscription> callback);

	void createInitialMessage(String text, Vector<User> listOfReceiver,
			Vector<Hashtag> listOfHashtag, int conversationId,
			AsyncCallback<Message> callback);

	void createUser(AsyncCallback<User> callback);

	void createUserSubscription(User subscribedUser,
			AsyncCallback<UserSubscription> callback);

	void deleteHashtagSubscription(Hashtag hashtag, AsyncCallback<Void> callback);

	void deleteMessage(Conversation conversation, Message message,
			AsyncCallback<Void> callback);

	void deleteUserSubscription(User user, AsyncCallback<Void> callback);

	void editMessage(Message message, String newText,
			Vector<Hashtag> listOfHashtag, AsyncCallback<Message> callback);

	void getAllConversationsFromUser(
			AsyncCallback<Vector<Conversation>> callback);

	void getAllHashtags(AsyncCallback<Vector<Hashtag>> callback);

	void getAllMessagesByDate(Date startDate, Date endDate,
			AsyncCallback<Vector<Message>> callback);

	void getAllMessagesFromUser(User user,
			AsyncCallback<Vector<Message>> callback);

	void getAllMessagesFromUserByDate(User user, Date startDate, Date endDate,
			AsyncCallback<Vector<Message>> callback);

	void getAllPublicConversationsFromUser(User user,
			AsyncCallback<Vector<Conversation>> callback);

	void getAllSubscribedHashtags(AsyncCallback<Vector<Hashtag>> callback);

	void getAllSubscribedUsers(AsyncCallback<Vector<User>> callback);

	void getAllUsers(AsyncCallback<Vector<User>> callback);

	void getCurrentUser(AsyncCallback<User> callback);

	void init(AsyncCallback<Void> callback);

	void updateUserData(String firstName, String lastName,
			AsyncCallback<Void> callback);

	void getAllSubscribedUsersFromUser(User selectedUser,
			AsyncCallback<Vector<User>> callback);

	void getAllSubscribedHashtagsFromUser(User selectedUser,
			AsyncCallback<Vector<Hashtag>> callback);

	void getAllPublicMessagesFromHashtag(Hashtag selectedHashtag,
			AsyncCallback<Vector<Message>> callback);

	void getAllMessagesWhereUserIsAuthor(User user,
			AsyncCallback<Vector<Message>> callback);

	void getAllMessagesWhereUserIsAuthorByDate(User user, Date startDate,
			Date endDate, AsyncCallback<Vector<Message>> callback);
	
	//void getAllMesagesFromConversation(Message message, AsyncCallback<Vector<Message>> callback);

}
