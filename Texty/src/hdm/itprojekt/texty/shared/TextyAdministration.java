package hdm.itprojekt.texty.shared;

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
	 
	 public Message createMessage(String text, User author, Boolean visible, Vector<Hashtag> listOfHashtag) 
			 throws IllegalArgumentException;
	 
	 public Hashtag createHashtag (String keyword) throws IllegalArgumentException;
	 
	 public Conversation createConversation(Boolean publicly, Vector<Message> listOfMessage, Vector<User> listOfParticipant) 
			 throws IllegalArgumentException;
	 
	 public UserSubscription createUserSubscription (User subscripedUser, User subscriber) throws IllegalArgumentException;
	 
	 public HashtagSubscription createHashtagSubscription (Hashtag subscribedHashtag, User subscriber)throws IllegalArgumentException;
	 
	 public User createUser(String firstName, String lastName, String email, String googleAccountAPI) throws IllegalArgumentException;
	 
	 
	 

}
