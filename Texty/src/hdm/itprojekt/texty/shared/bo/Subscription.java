package hdm.itprojekt.texty.shared.bo;


/**
 * Superklasse aller Abonnements
 */
public abstract class Subscription extends BusinessObject {

	private static final long serialVersionUID = 1L;
	private User subscriber = null;

	/**
	 * Rückgabe des abonnierenden User ab.
	 * @return
	 */
	public User getSubscriber() {
		return subscriber;
	}

	/**
	 * Setzen des abonnierenden Users
	 * @param subscriber
	 */
	public void setSubscriber(User subscriber) {
		this.subscriber = subscriber;
	}

}
