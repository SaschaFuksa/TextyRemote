package hdm.itprojekt.texty.shared;

import hdm.itprojekt.texty.client.LoginInfo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {

	public LoginInfo login(String requestUri);
}

//TODO LoginServiceImpl