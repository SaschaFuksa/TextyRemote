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
			// TODO: überprüfen ob die Subscription schon vorhanden ist; oder
			// schon gui seitig nicht möglich?
			/* Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT userId, hashtagId "
					+ "FROM hashtagsubscription " + "WHERE userId"
					+ "<> userId=" + hs.getSubscriber() + "AND hashtagId"
					+ "<> hashtagId=" + hs.getSubscribedHashtag());

			if (rs.next()) { */
				Statement stmt = con.createStatement();
				
				stmt.executeUpdate("INSERT INTO textydb.hashtagsubscription (userId, hashtagId)"
						+ "VALUES ("
						+ hashtagSubscription.getSubscriber()
						+ ", "
						+ "'"
						+ hashtagSubscription.getSubscribedHashtag() + "')");
		
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
			stmt.executeUpdate("DELETE * FROM textydb.hashtagsubscription " + "WHERE userId = '" 
			+ hashtagSubscription.getSubscriber() + "'" +"AND hashtagId= '" + hashtagSubscription.getSubscribedHashtag() + "'");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	  public Vector<HashtagSubscription> findAll() {
		    Connection con = DBConnection.connection();
		    // Ergebnisvektor vorbereiten
		    Vector<HashtagSubscription> result = new Vector<HashtagSubscription>();

		    try {
		      Statement stmt = con.createStatement();
		      //TODO: Date of Creation fehlt
				ResultSet rs = stmt.executeQuery("SELECT userId, hashtagId, FROM textydb.user");


		      // Für jeden Eintrag wird nun ein HashtagSubscription-Objekt erstellt.
		      while (rs.next()) {
		    	HashtagSubscription hashtagSubscription = new HashtagSubscription();
		    	//TODO: Add setter
		    	//hashtagSubscription.setSubscriberId(rs.getInt("userId"));
		    	//hashtagSubscription.setHashtagId(rs.getInt("hashtagId"));
		    	hashtagSubscription.setDateOfCreation(rs.getDate("dateOfCreation"));

		        // Hinzufügen des neuen Objekts zum Ergebnisvektor
		        result.addElement(hashtagSubscription);
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }

		    // Ergebnisvektor zurückgeben
		    return result;
		  }
	  
	  public Vector<HashtagSubscription> findByUser(int userId) {
		    Connection con = DBConnection.connection();
		    Vector<HashtagSubscription> result = new Vector<HashtagSubscription>();

		    try {
		      Statement stmt = con.createStatement();

		      ResultSet rs = stmt.executeQuery("SELECT userId, hashtagId, dateOfCreation"
		          + "FROM hashtagsubscription " + "WHERE userId=" + userId);

		      while (rs.next()) {
		    	HashtagSubscription hashtagSubscription = new HashtagSubscription();
		    	//TODO: Add setter
		    	//hashtagSubscription.setSubscriberId(rs.getInt("userId"));
		    	//hashtagSubscription.setHashtagId(rs.getInt("hashtagId"));
		    	hashtagSubscription.setDateOfCreation(rs.getDate("dateOfCreation"));

		        result.addElement(hashtagSubscription);
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		    
		    return result;
		  }
}
