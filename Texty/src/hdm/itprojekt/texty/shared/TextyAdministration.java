package hdm.itprojekt.texty.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import hdm.itprojekt.texty.shared.*;
import hdm.itprojekt.texty.shared.bo.*;
import hdm.itprojekt.texty.server.db.*;

@RemoteServiceRelativePath("administration")
public interface TextyAdministration extends RemoteService {

	 public void init() throws IllegalArgumentException;
	 
	 public Message createMessage(String text, User Author, Boolean visible) throws IllegalArgumentException;
	 //TODO Message mit Hashtag erzeugen
	 
	 public Hashtag createHashtag (String keyword);
	 
	 

}
