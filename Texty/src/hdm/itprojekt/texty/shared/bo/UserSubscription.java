package hdm.itprojekt.texty.shared.bo;

public class UserSubscription extends Subscription {

	private static final long serialVersionUID = 1L;
	private User subscribedUser = null;

	public User getSubscribedUser() {
		return subscribedUser;
	}

	public void setSubscribedUser(User subscribedUser) {
		this.subscribedUser = subscribedUser;
	}

}
