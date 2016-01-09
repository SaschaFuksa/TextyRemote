package hdm.itprojekt.texty.shared.bo;


/**
 * Die Klasse stellt das Grundger�st f�r alle Benutzer-Abonemments dar.
 */
public class UserSubscription extends Subscription {

	private static final long serialVersionUID = 1L;
	private User subscribedUser = null;

	/**
	 * R�ckgabe eines abonnierten Benutzers
	 * @return
	 */
	public User getSubscribedUser() {
		return subscribedUser;
	}

	/**
	 * Setzen eines abonnierten Users
	 * @param subscribedUser
	 */
	public void setSubscribedUser(User subscribedUser) {
		this.subscribedUser = subscribedUser;
	}

}
