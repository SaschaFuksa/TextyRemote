package hdm.itprojekt.texty.shared.bo;

/**
 * Die Klasse stellt das Grundger�st f�r alle Hashtag-Abonemments dar.
 */

public class HashtagSubscription extends Subscription {

	private static final long serialVersionUID = 1L;
	private Hashtag subscribedHashtag = null;

	/**
	 *  R�ckgabe eines abonnierten Hashtags.
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
