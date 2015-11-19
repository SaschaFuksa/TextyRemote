package hdm.itprojekt.texty.shared;

import hdm.itprojekt.texty.shared.bo.User;
import java.io.Serializable;

public class LoginInfo implements Serializable {

/**
 * Diese Klasse beinhaltet alle Informationen über einen angemeldeten User
 */
	private static final long serialVersionUID = 1L;
	
	private boolean loggedIn = false;
	private String loginUrl;
	private String logoutUrl;
	private User user;
	
	/**
	 * Überprüfung, ob der User am System angemeldet ist.
	 * 
	 * @param loggedIn als Boolean
	 */
	public void setLoggedIn(boolean loggedIn){
		this.loggedIn = loggedIn;
	}
	
	/**
	 * Set-Methode, die den erfolgreichen Login setzt.
	 * 
	 * @return loggedIn als boolean
	 */
	public boolean isLoggedIn(){
		return loggedIn;
	}
	

	/**
	 * Setzt die Login-Url.
	 * 
	 * @param login-Url als String
	 */
	public void setLoginUrl(String loginUrl){
		this.loginUrl = loginUrl;
	}
	
	/**
	 * Gibt die Login-Url zurück.
	 * 
	 * @return Login-Url als String
	 */
	public String getLoginUrl(){
		return loginUrl;
	}
	
	/**
	 * Setzt die Logout-Url
	 * 
	 * @param logoutUrl
	 */
	public void setLogoutUrl(String logoutUrl){
		this.logoutUrl = logoutUrl;
	}
	
	/**
	 * Gibt die Logout-Url zurück.
	 * 
	 * @return logout-URL als String
	 */
	public String getLogoutUrl(){
		return logoutUrl;
	}
	
	/**
	 * Legt den User fest.
	 * 
	 * @param user
	 */
	public void setUser (User user){
		this.user = user;
	}
	
	/**
	 * Gibt den User zurück.
	 * 
	 * @return User als User
	 */
	public User getUser(){
		return user;
	}
}
