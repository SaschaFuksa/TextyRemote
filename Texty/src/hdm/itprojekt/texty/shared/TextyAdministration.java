package hdm.itprojekt.texty.shared;



import java.util.Date;
import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import hdm.itprojekt.texty.shared.*;
import hdm.itprojekt.texty.shared.bo.*;
import hdm.itprojekt.texty.server.db.*;


@RemoteServiceRelativePath("administration")
public interface TextyAdministration extends RemoteService {

	 public void init() throws IllegalArgumentException;
	 
	 public Message createInitialMessage(String text, Vector<User> listOfReceiver, Vector<Hashtag> listOfHashtag) 
			 throws IllegalArgumentException;
	 
	 public Hashtag createHashtag (String keyword) throws IllegalArgumentException;
	 
	 public Conversation createConversation(String text,
				Vector<User> listOfReceivers, Vector<Hashtag> listOfHashtag) 
			 throws IllegalArgumentException;
	 
	 public UserSubscription createUserSubscription (User subscripedUser, User subscriber) throws IllegalArgumentException;
	 
	 public HashtagSubscription createHashtagSubscription (Hashtag subscribedHashtag, User subscriber)throws IllegalArgumentException;
	 
	 public User createUser() throws IllegalArgumentException;
	 
	 public Message editMessage(Message message, String newText) throws IllegalArgumentException;
	 	  
	 public Vector<Hashtag> getAllSubscribedHashtags(User user) throws IllegalArgumentException;
	 
	 public Message addMessageToConversation(Conversation c,String text, Vector<Hashtag> listOfHashtag)
			 throws IllegalArgumentException;
	 
	 public void deleteMessage(Conversation conversation, Message message) throws IllegalArgumentException;
	 
	 public void checkUserData()throws IllegalArgumentException;
	 
	 public void updateUserData(String firstName, String lastName) throws IllegalArgumentException;
	 
	 public void deleteUserSubscription(UserSubscription subscription) throws IllegalArgumentException;
	 
	 public void deleteHashtagSubscription(HashtagSubscription subscription) throws IllegalArgumentException;
	 
	 public Vector<Message> getAllMessagesFromUserByDate(User user, Date startDate, Date endDate) throws IllegalArgumentException;
	 
	 public Vector<Message> getAllMessagesByDate(Date startDate, Date endDate) throws IllegalArgumentException;
	 
	 public Vector<Message> getAllMessagesFromUser(User user) throws IllegalArgumentException;
	 
	 public Vector<Conversation> getAllPublicConversationsFromUser (User user) throws IllegalArgumentException;
	 
	 public Vector<User> getAllUsers() throws IllegalArgumentException; 
	 
	 public Vector<User> getAllSubscribedUsers (User user) throws IllegalArgumentException;
	 
	 public User getCurrentUser() throws IllegalArgumentException;
	 
	 public Vector<Hashtag> getAllHashtags() throws IllegalArgumentException;	 
	 
}
