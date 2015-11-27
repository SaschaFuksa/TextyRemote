package hdm.itprojekt.texty.shared;

import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TextyAdministrationAsync {

	void init(AsyncCallback<Void> callback);

	void createMessage(String text, User Author, Boolean visible,
			AsyncCallback<Message> callback);

	void createHashtag(String keyword, AsyncCallback<Hashtag> callback);

}
