package hdm.itprojekt.texty.server.db;

import hdm.itprojekt.texty.shared.bo.User;
import hdm.itprojekt.texty.shared.bo.UserSubscription;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class UserSubscriptionMapper {

	private static UserSubscriptionMapper userSubscriptionMapper = null;

	public static UserSubscriptionMapper userSubscriptionMapper() {
		if (userSubscriptionMapper == null) {
			userSubscriptionMapper = new UserSubscriptionMapper();
		}
		return userSubscriptionMapper;
	}

	protected UserSubscriptionMapper() {

	}

	public void delete(UserSubscription usersubscription) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			// HashtagSubscription gets deleted
			stmt.executeUpdate("DELETE FROM textydb.usersubscription "
					+ "WHERE subscriber_userId = "
					+ usersubscription.getSubscriber().getId()
					+ " AND subscribed_userId = "
					+ usersubscription.getSubscribedUser().getId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public UserSubscription insert(UserSubscription userSubscription) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO textydb.usersubscription (subscriber_userId, subscribed_userId)"
					+ "VALUES ("
					+ "'"
					+ userSubscription.getSubscriber().getId()
					+ "'"
					+ ", "
					+ "'" + userSubscription.getSubscribedUser().getId() + "')");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userSubscription;
	}

	// Returns all subscribed Users from a User
	// Will also be used for the Report generator!
	public Vector<User> selectAllSubscribedUsers(User subscriber) {
		Connection con = DBConnection.connection();
		Vector<User> result = new Vector<User>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT userId, givenName, familyName, email, user.dateOfCreation FROM textydb.user INNER JOIN textydb.usersubscription ON user.userId = usersubscription.subscribed_userId "
							+ "WHERE subscriber_userId = " + subscriber.getId());

			// Für jeden Eintrag wird nun ein Usersubscription-Objekt erstellt.

			while (rs.next()) {

				User user = new User();

				user.setId(rs.getInt("userId"));
				user.setFirstName(rs.getString("givenName"));
				user.setLastName(rs.getString("familyName"));
				user.setEmail(rs.getString("email"));
				user.setDateOfCreation(rs.getTime("dateOfCreation"));

				result.addElement(user);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
}
