package hdm.itprojekt.texty.server.db;

import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.HashtagSubscription;
import hdm.itprojekt.texty.shared.bo.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class HashtagSubscriptionMapper {

	private static HashtagSubscriptionMapper hashtagSubscriptionMapper = null;

	public static HashtagSubscriptionMapper hashtagSubscriptionMapper() {
		if (hashtagSubscriptionMapper == null) {
			hashtagSubscriptionMapper = new HashtagSubscriptionMapper();
		}
		return hashtagSubscriptionMapper;
	}

	protected HashtagSubscriptionMapper() {

	}

	public void delete(HashtagSubscription hashtagSubscription) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			// HashtagSubscription gets deleted
			stmt.executeUpdate("DELETE FROM textydb.hashtagsubscription "
					+ "WHERE userId = "
					+ hashtagSubscription.getSubscriber().getId()
					+ " AND hashtagId = "
					+ hashtagSubscription.getSubscribedHashtag().getId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public HashtagSubscription insert(HashtagSubscription hashtagSubscription) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("INSERT INTO textydb.hashtagsubscription (userId, hashtagId)"
					+ "VALUES ("
					+ "'"
					+ hashtagSubscription.getSubscriber().getId()
					+ "'"
					+ ", "
					+ "'"
					+ hashtagSubscription.getSubscribedHashtag().getId()
					+ "')");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hashtagSubscription;
	}

	// Returns all subscribed Hashtags from a User
	// Will also be used for the Report generator!
	public Vector<Hashtag> selectAllSubscribedHashtags(User subscriber) {
		Connection con = DBConnection.connection();
		Vector<Hashtag> result = new Vector<Hashtag>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT hashtag.hashtagId, hashtag.keyword, hashtag.dateOfCreation FROM textydb.hashtag INNER JOIN textydb.hashtagsubscription ON hashtag.hashtagId = hashtagsubscription.hashtagId "
							+ "WHERE hashtagsubscription.userId = "
							+ subscriber.getId());

			// Für jeden Eintrag wird nun ein Usersubscription-Objekt erstellt.

			while (rs.next()) {
				Hashtag hashtag = new Hashtag();

				hashtag.setId(rs.getInt("hashtagId"));
				hashtag.setKeyword(rs.getString("keyword"));
				hashtag.setDateOfCreation(rs.getTimestamp("dateOfCreation"));

				result.addElement(hashtag);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
}
