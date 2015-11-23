package hdm.itprojekt.texty.server.db;

import java.sql.*;
import java.util.ArrayList;
import hdm.itprojekt.texty.shared.bo.*;

public class HashtagSubscriptionMapper {

	private static HashtagSubscriptionMapper hashtagSubscriptionMapper = null;

	protected HashtagSubscriptionMapper() {

	}

	public static HashtagSubscriptionMapper hashtagSubscriptionMapper() {
		if (hashtagSubscriptionMapper == null) {
			hashtagSubscriptionMapper = new HashtagSubscriptionMapper();
		}
		return hashtagSubscriptionMapper;
	}

	public ArrayList<HashtagSubscription> select(User u) {
		ArrayList result = new ArrayList();
		// ...
		return result;
	}

	public void insert(HashtagSubscription hs) {
		// ...
	}

	public void delete(HashtagSubscription hs) {
		// ...
	}

}
