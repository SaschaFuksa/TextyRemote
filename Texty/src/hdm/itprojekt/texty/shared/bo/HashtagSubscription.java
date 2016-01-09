package hdm.itprojekt.texty.shared.bo;

/**
 * Die Klasse stellt das Grundgerüst für alle Hashtag-Abonemments dar.
 */

public class HashtagSubscription extends Subscription {

	private static final long serialVersionUID = 1L;
	private Hashtag subscribedHashtag = null;

	/**
	 *  Rückgabe eines abonnierten Hashtags.
	 * @return abonnierter Hashtag
	 */
	public Hashtag getSubscribedHashtag() {
		return subscribedHashtag;
	}
	/**
	 * Setzen eines abonnierten Hashtags.
	 * @param subscribedHashtag
	 */
	public void setSubscribedHashtag(Hashtag subscribedHashtag) {
		this.subscribedHashtag = subscribedHashtag;
	}

}
