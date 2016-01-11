package hdm.itprojekt.texty.server;

import hdm.itprojekt.texty.client.LoginInfo;
import hdm.itprojekt.texty.shared.LoginService;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Dies ist die Implementierungsklasse des Interface {@link LoginService}
 */
public class LoginServiceImpl extends RemoteServiceServlet implements
		LoginService {

	private static final long serialVersionUID = 1L;

	/**
	 * Diese Methode führt den Login aus und ruft die Daten von der Google
	 * Accounts API ab.
	 */
	@Override
	public LoginInfo login(String requestUri) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();

		loginInfo.setEmailAddress(user.getEmail());
		loginInfo.setNickname(user.getNickname());
		loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));

		return loginInfo;
	}

}