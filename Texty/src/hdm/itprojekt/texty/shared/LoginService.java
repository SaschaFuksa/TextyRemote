package hdm.itprojekt.texty.shared;

import hdm.itprojekt.texty.client.LoginInfo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
/**
 * Sieh {@link hdm.itprojekt.texty.server.LoginServiceImpl}
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {

	public LoginInfo login(String requestUri);
}