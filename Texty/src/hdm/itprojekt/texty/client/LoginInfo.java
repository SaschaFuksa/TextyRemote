package hdm.itprojekt.texty.client;

import java.io.Serializable;

/**
 * Diese Klasse ist Bestandteil des Login-Vorgangs, um die Applikation nutzen zu
 * können
 * 
 *
 */
public class LoginInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String logoutUrl;
	private String emailAddress;
	private String nickname;

	/**
	 * Getter, der die e-Mail Adresse des Users zurückliefert
	 * 
	 * @return
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Getter, der die Google Logout-URL zurückliefert
	 * 
	 * @return
	 */
	public String getLogoutUrl() {
		return logoutUrl;
	}

	/**
	 * Getter, der den Nickname des Users zurückliefert
	 * 
	 * @return
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Legt die e-Mail Adresse des Users fest
	 * 
	 * @param emailAddress
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * Legt die Google Logout-URL fest
	 * 
	 * @param logoutUrl
	 */
	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	/**
	 * Legt einen Nickname fuer den User fest
	 * 
	 * @param nickname
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}