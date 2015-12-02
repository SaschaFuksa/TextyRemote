package hdm.itprojekt.texty.shared;

import java.util.ArrayList;
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

	void createMessage(String text, User Author, Boolean visible, Vector<Hashtag> listOfHashtag,
			AsyncCallback<Message> callback);

	void createHashtag(String keyword, AsyncCallback<Hashtag> callback);

	void createConversation(Boolean publicly, Vector<Message> listOfMessage,
			Vector<User> listOfParticipant, AsyncCallback<Conversation> callback);

	void createUserSubscription(User subscripedUser, User subscriber,
			AsyncCallback<UserSubscription> callback);

	void createHashtagSubscription(Hashtag subscribedHashtag, User subscriber,
			AsyncCallback<HashtagSubscription> callback);

	void createUser(String firstName, String lastName, String email,
			String googleAccountAPI, AsyncCallback<User> callback);

	void editMessage(Message message, AsyncCallback<Message> callback);

	void editConversation(Conversation conversation, User user,
			AsyncCallback<Conversation> callback);

	void getAllSubscribedHashtags(User user,
			AsyncCallback<Vector<Hashtag>> callback);

	void addMessageToConversation(Conversation c, Message m, String text,
			User author, Boolean visible, Vector<Hashtag> listOfHashtag,
			AsyncCallback<Message> callback);

	void deleteMessage(Conversation conversation, Message message,
			AsyncCallback<Void> callback);

	

	



	

}
