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


@RemoteServiceRelativePath("administration")
public interface TextyAdministration extends RemoteService {

	 public void init() throws IllegalArgumentException;
	 
	 public Message createInitialMessage(String text, Vector<User> listOfReceiver, Vector<Hashtag> listOfHashtag, int conversationId) 
			 throws IllegalArgumentException;
	 
	 public Hashtag createHashtag (String keyword) throws IllegalArgumentException;
	 
	 public Conversation createConversation(String text,
				Vector<User> listOfReceivers, Vector<Hashtag> listOfHashtag) 
			 throws IllegalArgumentException;
	 
	 public UserSubscription createUserSubscription (User subscribedUser) throws IllegalArgumentException;
	 
	 public HashtagSubscription createHashtagSubscription (Hashtag subscribedHashtag)throws IllegalArgumentException;
	 
	 public User createUser() throws IllegalArgumentException;
	 
	 public Message editMessage(Message message, String newText) throws IllegalArgumentException;
	 	  
	 public Vector<Hashtag> getAllSubscribedHashtags() throws IllegalArgumentException;
	 
	 public Message addMessageToConversation(Conversation c,String text, Vector<Hashtag> listOfHashtag)
			 throws IllegalArgumentException;
	 
	 public void deleteMessage(Conversation conversation, Message message) throws IllegalArgumentException;
	 
	 public void checkUserData()throws IllegalArgumentException;
	 
	 public void updateUserData(String firstName, String lastName) throws IllegalArgumentException;
	 
	 public void deleteUserSubscription(User user) throws IllegalArgumentException;
	 
	 public void deleteHashtagSubscription(Hashtag hashtag) throws IllegalArgumentException;
	 
	 public Vector<Message> getAllMessagesFromUserByDate(User user, Date startDate, Date endDate) throws IllegalArgumentException;
	 
	 public Vector<Message> getAllMessagesByDate(Date startDate, Date endDate) throws IllegalArgumentException;
	 
	 public Vector<Message> getAllMessagesFromUser(User user) throws IllegalArgumentException;
	 
	 public Vector<Conversation> getAllPublicConversationsFromUser (User user) throws IllegalArgumentException;
	 
	 public Vector<User> getAllUsers() throws IllegalArgumentException; 
	 
	 public Vector<User> getAllSubscribedUsers () throws IllegalArgumentException;
	 
	 public User getCurrentUser() throws IllegalArgumentException;
	 
	 public Vector<Hashtag> getAllHashtags() throws IllegalArgumentException;
	 
	 public Vector<Conversation> getAllConversationsFromUser() throws IllegalArgumentException;
	 
}
