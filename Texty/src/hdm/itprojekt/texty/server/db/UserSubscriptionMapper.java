package hdm.itprojekt.texty.server.db;

import hdm.itprojekt.texty.shared.bo.*;
import java.sql.*;
import java.util.ArrayList;

public class UserSubscriptionMapper {

	private static UserSubscriptionMapper userSubscriptionMapper = null;

	protected UserSubscriptionMapper() {

	}

	public static UserSubscriptionMapper userSubscriptionMapper() {
		if (userSubscriptionMapper == null) {
			userSubscriptionMapper = new UserSubscriptionMapper();
		}
		return userSubscriptionMapper;
	}

	public ArrayList<UserSubscription> select(User u) {
		ArrayList result = new ArrayList();
		// ...
		return result;
	}

	public void insert(UserSubscription us) {
		// ...
	}

	public void delete(UserSubscription us) {
		// ...
	}

}
