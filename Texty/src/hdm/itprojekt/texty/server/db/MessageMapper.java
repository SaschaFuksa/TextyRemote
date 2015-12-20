package hdm.itprojekt.texty.server.db;

import java.sql.*;
import java.util.Vector;

import hdm.itprojekt.texty.shared.bo.*;

public class MessageMapper {

	private static MessageMapper messageMapper = null;

	protected MessageMapper() {

	}

	public static MessageMapper messageMapper() {
		if (messageMapper == null) {
			messageMapper = new MessageMapper();
		}
		return messageMapper;
	}

	public Message insert(Message message) {
		Connection con = DBConnection.connection();
		int state = 0;
		if (message.isVisible()){
			state = 1;
		}
		
		try {
			Statement stmt = con.createStatement();
			// Find highest Primarykey
			ResultSet rs = stmt.executeQuery("SELECT MAX(messageId) AS maxid "
					+ "FROM textydb.message ");

			if (rs.next()) {

				message.setId(rs.getInt("maxid") + 1);
				

				stmt = con.createStatement();

				// Highest Primarykey has been found and set, now we insert it
				// into the DB
				stmt.executeUpdate("INSERT INTO textydb.message (messageId, author_userId, conversationId, messageText, visibility)"
						+ "VALUES ("
						+ message.getId()
						+ ", "
						+ message.getAuthor().getId()
						+ ", "
						+ "'"
						+ message.getConversationID()
						+ "'"
						+ ", "
						+ "'"
						+ message.getText()
						+ "'"
						+ ", "
						+ state + ")");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return message;
	}

	public void delete(Message message) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			// The visibilty of the Message will be changed and users can not
			// see it anymore
			// TODO: Boolean Wert umwandeln für die DB? Überprüfen!
			stmt.executeUpdate("UPDATE textydb.message SET message.visibility = "
					+ message.isVisible() + "WHERE messageId = " + message.getId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	   public Message update(Message message) {
		    Connection con = DBConnection.connection();

		    try {
		      Statement stmt = con.createStatement();

				stmt.executeUpdate("UPDATE textydb.message SET message.messageText = "
						+ "'" + message.getText() + "'" + "WHERE messageId = " + message.getId());

		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }

		    return message;
		  }

	public Vector<Message> selectAllMessagesFromUser(User currentuser) {
		    Connection con = DBConnection.connection();
		    Vector<Message> resultMessage = new Vector<Message>();

		    try {
		      Statement stmt = con.createStatement();

		      ResultSet rsMessages = stmt.executeQuery("SELECT message.messageId, message.author_userId, message.messageText, message.conversationId, message.visibility, message.dateOfCreation, user.userId, user.givenName, user.familyName, user.email, user.dateOfCreation FROM textydb.message INNER JOIN textydb.user ON message.author_userId = user.userId "
		    		  + "WHERE user.userId = " + currentuser.getId() + "AND message.visibility = 1" );

		      // Für jeden Eintrag wird nun ein Message-Objekt erstellt.
		      while (rsMessages.next()) {
		    	 Message allmessages = new Message();
		    	 
		    	 allmessages.setId(rsMessages.getInt("message.messageId"));
		    	 
		    	 allmessages.setAuthor(findAuthor(allmessages));
		    	 allmessages.setListOfReceivers(findReceivers(allmessages));
		    	 allmessages.setListOfHashtag(findHashtagsInMessage(allmessages));
		    	 
		    	 allmessages.setText(rsMessages.getString("message.messageText"));
		    	 allmessages.setConversationID(rsMessages.getInt("message.conversationId"));
		    	 allmessages.setVisible(rsMessages.getBoolean("message.visibility"));
		    	 allmessages.setDateOfCreation(rsMessages.getTime("message.dateOfCreation"));  	 

		        resultMessage.addElement(allmessages);
		      }
		     
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		    
		    return resultMessage;
		  }
	
	public User findAuthor (Message message) {
	    Connection con = DBConnection.connection();

	        try {
	          Statement stmt = con.createStatement();

		      ResultSet rs = stmt.executeQuery("SELECT user.userId, user.givenName, user.familyName, user.email, user.dateOfCreation FROM textydb.message INNER JOIN textydb.user ON message.author_userId = user.userId "
		    		  + "WHERE message.messageId = " + message.getId() + "AND message.visibility = 1");

	          if (rs.next()) {
	            User user = new User();
	            
	            user.setId(rs.getInt("user.userId"));
	            user.setFirstName(rs.getString("user.givenName"));
	            user.setLastName(rs.getString("user.familyName"));
	            user.setEmail(rs.getString("user.email"));
	            user.setDateOfCreation(rs.getTime("user.dateOfCreation"));
	            
	            return user;
	          }
	        }
	        catch (SQLException e2) {
	          e2.printStackTrace();
	          return null;
	        }

	        return null;
	        }
	
	public Vector<User> findReceivers(Message message) {
	    Connection con = DBConnection.connection();
	    Vector<User> result = new Vector<User>();
	    
	    try {
		      Statement stmt = con.createStatement();
		      
		      ResultSet rs = stmt.executeQuery("SELECT user.userId, user.givenName, user.familyName, user.email, user.dateOfCreation FROM textydb.message INNER JOIN textydb.receiver ON message.messageId = receiver.messageId INNER JOIN textydb.user ON receiver.receiver_userId = user.userId "
		    		  + "WHERE message.messageId = " + message.getId() + "AND message.visibility = 1");

		      // Für jeden Eintrag wird nun ein Message-Objekt erstellt.
		      while (rs.next()) {
		    	 User receiver = new User();
		    	 
		    	  receiver.setId(rs.getInt("user.userId"));
		    	  receiver.setFirstName(rs.getString("user.givenName"));
		    	  receiver.setLastName(rs.getString("user.familyName"));
		    	  receiver.setEmail(rs.getString("user.email"));
		    	  receiver.setDateOfCreation(rs.getTime("user.dateOfCreation"));  	 

		        result.addElement(receiver);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Ergebnisvektor zurückgeben
			return result;
		}
	
	public Vector<Hashtag> findHashtagsInMessage(Message message) {
	    Connection con = DBConnection.connection();
	    Vector<Hashtag> result = new Vector<Hashtag>();
	    
	    try {
		      Statement stmt = con.createStatement();
		      
		      ResultSet rs = stmt.executeQuery("SELECT hashtag.hashtagId, hashtag.keyword, hashtag.dateOfCreation, message.messageId FROM textydb.hashtag INNER JOIN textydb.hashtag_in_message ON hashtag.hashtagId = hashtag_in_message.hashtagId INNER JOIN textydb.message ON hashtag_in_message.messageId = message.messageId "
		    		  + "WHERE message.messageId = " + message.getId() + "AND message.visibility = 1");

		      // Für jeden Eintrag wird nun ein Message-Objekt erstellt.
		      while (rs.next()) {
		    	 Hashtag hashtag = new Hashtag();
		    	 
			    	hashtag.setId(rs.getInt("hashtag.hashtagId"));
			    	hashtag.setKeyword(rs.getString("hashtag.keyword"));
			    	hashtag.setDateOfCreation(rs.getTime("hashtag.dateOfCreation"));

		        result.addElement(hashtag);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Ergebnisvektor zurückgeben
			return result;
		}

	public Vector<Message> selectAllMessages() {
	    Connection con = DBConnection.connection();
	    Vector<Message> resultMessage = new Vector<Message>();

	    try {
	      Statement stmt = con.createStatement();

	      ResultSet rsMessages = stmt.executeQuery("SELECT message.messageId, message.author_userId, message.messageText, message.conversationId, message.visibility, message.dateOfCreation, user.userId, user.givenName, user.familyName, user.email, user.dateOfCreation FROM textydb.message INNER JOIN textydb.user ON message.author_userId = user.userId "
	    		  + "WHERE message.visibility = 1");

	      // Für jeden Eintrag wird nun ein Message-Objekt erstellt.
	      while (rsMessages.next()) {
	    	 Message allmessages = new Message();
	    	 
	    	 allmessages.setId(rsMessages.getInt("message.messageId"));
	    	 
	    	 allmessages.setAuthor(findAuthor(allmessages));
	    	 allmessages.setListOfReceivers(findReceivers(allmessages));
	    	 allmessages.setListOfHashtag(findHashtagsInMessage(allmessages));
	    	 
	    	 allmessages.setText(rsMessages.getString("message.messageText"));
	    	 allmessages.setConversationID(rsMessages.getInt("message.conversationId"));
	    	 allmessages.setVisible(rsMessages.getBoolean("message.visibility"));
	    	 allmessages.setDateOfCreation(rsMessages.getTime("message.dateOfCreation"));  	 

	        resultMessage.addElement(allmessages);
	      }
	     
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
	    
	    return resultMessage;
	  }
}
