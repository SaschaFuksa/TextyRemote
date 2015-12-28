package hdm.itprojekt.texty.shared.bo;

public class HashtagSubscription extends Subscription {

	private static final long serialVersionUID = 1L;
	private Hashtag subscribedHashtag = null;

	public Hashtag getSubscribedHashtag() {
		return subscribedHashtag;
	}

	public void setSubscribedHashtag(Hashtag subscribedHashtag) {
		this.subscribedHashtag = subscribedHashtag;
	}

}
