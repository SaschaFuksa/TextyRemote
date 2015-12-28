package hdm.itprojekt.texty.client;

import java.io.Serializable;

public class LoginInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String logoutUrl;
	private String emailAddress;
	private String nickname;

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public String getNickname() {
		return nickname;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}