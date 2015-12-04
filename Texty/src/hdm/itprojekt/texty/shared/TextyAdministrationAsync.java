package hdm.itprojekt.texty.shared;


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

	void createMessage(String text, User Author, Vector<User> listOfReceiver, Vector<Hashtag> listOfHashtag,
			AsyncCallback<Message> callback);

	void createHashtag(String keyword, AsyncCallback<Hashtag> callback);

	void createConversation(Message message,
			 AsyncCallback<Conversation> callback);

	void createUserSubscription(User subscripedUser, User subscriber,
			AsyncCallback<UserSubscription> callback);

	void createHashtagSubscription(Hashtag subscribedHashtag, User subscriber,
			AsyncCallback<HashtagSubscription> callback);

	void createUser(AsyncCallback<User> callback);

	void editMessage(Message message, String newText, AsyncCallback<Message> callback);
	
	void getAllSubscribedHashtags(User user,
			AsyncCallback<Vector<Hashtag>> callback);

	void addMessageToConversation(Conversation c, String text,
			User author, Vector<Hashtag> listOfHashtag,
			AsyncCallback<Message> callback);

	void deleteMessage(Conversation conversation, Message message,
			AsyncCallback<Void> callback);

	void checkUserData(AsyncCallback<Void> callback);

	void updateUserData(User us, AsyncCallback<Void> callback);

	void deleteUserSubscription(UserSubscription subscription,
			AsyncCallback<Void> callback);

	void deleteHashtagSubscription(HashtagSubscription subscription,
			AsyncCallback<Void> callback);

	

	



	

}
