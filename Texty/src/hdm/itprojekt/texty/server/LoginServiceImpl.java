package hdm.itprojekt.texty.server;

import hdm.itprojekt.texty.shared.LoginInfo;
import hdm.itprojekt.texty.shared.LoginService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Anmeldung des Users mithilfe der Google Account API
 * 
 * @author Daniel
 *
 */
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	
	
	private static final long serialVersionUID = 1L;

	public LoginInfo login(String requestUri) {
		// TODO Auto-generated method stub
		return null;
	}

}

//TODO gemeinsame Suche nach Ideen zur genauen Implementierung