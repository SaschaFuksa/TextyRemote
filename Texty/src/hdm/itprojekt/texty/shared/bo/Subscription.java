package hdm.itprojekt.texty.shared.bo;

public abstract class Subscription extends BusinessObject {

	private static final long serialVersionUID = 1L;
	private User subscriber = null;

	public User getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(User subscriber) {
		this.subscriber = subscriber;
	}

}
