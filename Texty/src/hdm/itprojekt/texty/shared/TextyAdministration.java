package hdm.itprojekt.texty.shared;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import hdm.itprojekt.texty.shared.*;
import hdm.itprojekt.texty.shared.bo.*;
import hdm.itprojekt.texty.server.db.*;


@RemoteServiceRelativePath("administration")
public interface TextyAdministration extends RemoteService {

	 public void init() throws IllegalArgumentException;
	 
	 public Message createMessage(String text, User author, Vector<User> listOfReceiver, Vector<Hashtag> listOfHashtag) 
			 throws IllegalArgumentException;
	 
	 public Hashtag createHashtag (String keyword) throws IllegalArgumentException;
	 
	 public Conversation createConversation(Message message) 
			 throws IllegalArgumentException;
	 
	 public UserSubscription createUserSubscription (User subscripedUser, User subscriber) throws IllegalArgumentException;
	 
	 public HashtagSubscription createHashtagSubscription (Hashtag subscribedHashtag, User subscriber)throws IllegalArgumentException;
	 
	 public User createUser() throws IllegalArgumentException;
	 
	 public Message editMessage(Message message, String newText) throws IllegalArgumentException, SQLException;
	 	  
	 public Vector<Hashtag> getAllSubscribedHashtags(User user) throws IllegalArgumentException;
	 
	 public Message addMessageToConversation(Conversation c,String text, User author, Vector<Hashtag> listOfHashtag)
			 throws IllegalArgumentException;
	 
	 public void deleteMessage(Conversation conversation, Message message) throws IllegalArgumentException, SQLException;
	 
	 public void checkUserData()throws IllegalArgumentException;
	 
	 public void updateUserData(User us) throws IllegalArgumentException;
	 
	 public void deleteUserSubscription(UserSubscription subscription) throws IllegalArgumentException;
	 
	 public void deleteHashtagSubscription(HashtagSubscription subscription) throws IllegalArgumentException;
	 
	 
	 

}
