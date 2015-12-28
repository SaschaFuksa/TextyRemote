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
				Statement stmt = con.createStatement();
				
				stmt.executeUpdate("INSERT INTO textydb.hashtagsubscription (userId, hashtagId)"
						+ "VALUES ("
						+"'"
						+ hashtagSubscription.getSubscriber().getId()
						+"'"
						+ ", "
						+ "'"
						+ hashtagSubscription.getSubscribedHashtag().getId() + "')");
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hashtagSubscription;
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
	  
	  // Returns all subscribed Hashtags from a User
	  // Will also be used for the Report generator!
		public Vector<Hashtag> selectAllSubscribedHashtags(User subscriber) {
			Connection con = DBConnection.connection();
			Vector<Hashtag> result = new Vector<Hashtag>();

			try {
				Statement stmt = con.createStatement();

				
				ResultSet rs = stmt.executeQuery("SELECT hashtag.hashtagId, hashtag.keyword, hashtag.dateOfCreation FROM textydb.hashtag INNER JOIN textydb.hashtagsubscription ON hashtag.hashtagId = hashtagsubscription.hashtagId "
						+ "WHERE hashtagsubscription.userId = " + subscriber.getId() );
				
				// Für jeden Eintrag wird nun ein Usersubscription-Objekt erstellt.
				
				while (rs.next()) {
					Hashtag hashtag = new Hashtag();
					
					hashtag.setId(rs.getInt("hashtagId"));
					hashtag.setKeyword(rs.getString("keyword"));
					hashtag.setDateOfCreation(rs.getTime("dateOfCreation"));

					result.addElement(hashtag);

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return result;
		}
		
		public void delete(HashtagSubscription hashtagSubscription) {
			Connection con = DBConnection.connection();

			try {
				Statement stmt = con.createStatement();
				//HashtagSubscription gets deleted 
				stmt.executeUpdate("DELETE FROM textydb.hashtagsubscription " + "WHERE userId = " 
				+ hashtagSubscription.getSubscriber().getId() + " AND hashtagId = " + hashtagSubscription.getSubscribedHashtag().getId());
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
}
