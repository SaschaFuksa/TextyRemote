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
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO textydb.usersubscription (subscriber_userId, subscribed_userId)"
					+ "VALUES ("
					+"'"
					+ userSubscription.getSubscriber().getId()
					+"'"
					+ ", "
					+ "'"
					+ userSubscription.getSubscribedUser().getId() + "')");
	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userSubscription;
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
	
	  public Vector<UserSubscription> findBySubscriber(int subscriber) {
		    Connection con = DBConnection.connection();
		    Vector<UserSubscription> result = new Vector<UserSubscription>();

		    try {
		      Statement stmt = con.createStatement();

		      ResultSet rs = stmt.executeQuery("SELECT subscriber_userId, subscribed_userId, dateOfCreation"
		          + "FROM usersubscription" + "WHERE subscriber_userId=" + subscriber);

		      // Für jeden Eintrag wird nun ein Usersubscription-Objekt erstellt.
		      while (rs.next()) {
		    	  UserSubscription userSubscription = new UserSubscription();
		        /*
		         * TODO: setter ändern
		         
		    	userSubscription.setSubscriber(rs.getInt("susriber_userId"));
		        userSubscription.setSubscribedUser(rs.getInt("subscribed_userId"));
		        userSubscription.setDateOfCreation(rs.getDate("dateOfCreation"));
		         */
		    	  
		        result.addElement(userSubscription);
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		    
		    return result;
		  }

	public Vector<User> selectAllSubscribedUsers(User user) {
		// TODO Auto-generated method stub
		return null;
	}
}
