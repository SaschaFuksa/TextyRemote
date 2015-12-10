package hdm.itprojekt.texty.server.db;

import java.sql.*;
import java.util.Vector;

import hdm.itprojekt.texty.shared.bo.*;

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

	public UserSubscription insert(UserSubscription userSubscription) {
		Connection con = DBConnection.connection();

		try {
			// TODO: überprüfen ob die Subscription schon vorhanden ist; oder
			// schon gui seitig nicht möglich?
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO textydb.usersubscription (subscriber_userId, subscribed_userId) "
					+ "VALUES ("
					+ userSubscription.getSubscriber()
					+ ", "
					+ userSubscription.getSubscribedUser() + ")");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userSubscription;
		// TODO: Timestamp muss mit übergeben werden
	}

	public void delete(UserSubscription userSubscription) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			// UserSubscription gets deleted
			stmt.executeUpdate("DELETE FROM textydb.usersubscription "
					+ "WHERE subscriber_userId="
					+ userSubscription.getSubscriber()
					+ "AND subscribed_userId="
					+ userSubscription.getSubscribedUser());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
