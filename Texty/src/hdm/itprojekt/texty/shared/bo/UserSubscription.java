package hdm.itprojekt.texty.shared.bo;


/**
 * Die Klasse stellt das Grundgerüst für alle Benutzer-Abonemments dar.
 */
public class UserSubscription extends Subscription {

	private static final long serialVersionUID = 1L;
	private User subscribedUser = null;

	/**
	 * Rückgabe eines abonnierten Benutzers
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
