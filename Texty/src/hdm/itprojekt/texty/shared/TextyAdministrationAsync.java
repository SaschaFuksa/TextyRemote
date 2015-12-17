package hdm.itprojekt.texty.shared;


import java.util.Date;
import java.util.Vector;

import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.HashtagSubscription;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;
import hdm.itprojekt.texty.shared.bo.UserSubscription;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TextyAdministrationAsync {

	void init(AsyncCallback<Void> callback);

	void createInitialMessage(String text, Vector<User> listOfReceiver, Vector<Hashtag> listOfHashtag,
			AsyncCallback<Message> callback);

	void createHashtag(String keyword, AsyncCallback<Hashtag> callback);

	void createConversation(String text,
			Vector<User> listOfReceivers, Vector<Hashtag> listOfHashtag,
			AsyncCallback<Conversation> callback);

	void createUserSubscription(User subscribedUser, 
			AsyncCallback<UserSubscription> callback);

	void createHashtagSubscription(Hashtag subscribedHashtag,
			AsyncCallback<HashtagSubscription> callback);

	void createUser(AsyncCallback<User> callback);

	void editMessage(Message message, String newText, AsyncCallback<Message> callback);
	
	void getAllSubscribedHashtags(User user,
			AsyncCallback<Vector<Hashtag>> callback);

	void addMessageToConversation(Conversation c, String text,
			Vector<Hashtag> listOfHashtag,
			AsyncCallback<Message> callback);

	void deleteMessage(Conversation conversation, Message message,
			AsyncCallback<Void> callback);

	void checkUserData(AsyncCallback<Void> callback);

	void updateUserData(String firstName, String lastName,
			AsyncCallback<Void> callback);

	void deleteUserSubscription(User user,
			AsyncCallback<Void> callback);

	void deleteHashtagSubscription(Hashtag hashtag,
			AsyncCallback<Void> callback);

	void getAllMessagesFromUserByDate(User user, Date startDate, Date endDate,
			AsyncCallback<Vector<Message>> callback);

	void getAllMessagesByDate(Date startDate, Date endDate,
			AsyncCallback<Vector<Message>> callback);

	void getAllMessagesFromUser(User user,
			AsyncCallback<Vector<Message>> callback);

	void getAllPublicConversationsFromUser(User user,
			AsyncCallback<Vector<Conversation>> callback);

	void getAllUsers(AsyncCallback<Vector<User>> callback);

	void getCurrentUser(AsyncCallback<User> callback);

	void getAllSubscribedUsers(AsyncCallback<Vector<User>> callback);

	void getAllHashtags(AsyncCallback<Vector<Hashtag>> callback);

	

	



	

}
