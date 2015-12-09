package hdm.itprojekt.texty.server.db;

import java.sql.*;
import java.util.Vector;

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

	public HashtagSubscription insert(HashtagSubscription hashtagSubscription) {
		Connection con = DBConnection.connection();

		try {
			/* Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT userId, hashtagId "
					+ "FROM hashtagsubscription " + "WHERE userId"
					+ "<> userId=" + hs.getSubscriber() + "AND hashtagId"
					+ "<> hashtagId=" + hs.getSubscribedHashtag());

			if (rs.next()) { */
				Statement stmt = con.createStatement();
				stmt.executeUpdate("INSERT INTO textydb.hashtagsubscription (userId, hashtagId) "
						+ "VALUES ("
						+ hashtagSubscription.getSubscriber()
						+ ", "
						+ hashtagSubscription.getSubscribedHashtag() + ")");
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hashtagSubscription;
	}
	
	public void delete(HashtagSubscription hashtagSubscription) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			//HashtagSubscription gets deleted 
			stmt.executeUpdate("DELETE FROM textydb.hashtagsubscription " + "WHERE userId="
					+ hashtagSubscription.getSubscriber() +"AND hashtagId=" + hashtagSubscription.getSubscribedHashtag());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
