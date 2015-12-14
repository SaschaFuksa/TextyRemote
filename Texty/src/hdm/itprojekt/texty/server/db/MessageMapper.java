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
						+ message.getAuthor()
						+ ", "
						// TODO: add getConversationId + message.getConversationId
						+ ", "
						+ message.getText()
						+ ", "
						+ message.isVisible()
						+ ")");
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
			stmt.executeUpdate("UPDATE textydb.message SET (visibility)"
					+ "VALUES(" + message.isVisible() + "WHERE messageId="
					+ message.getId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	   public Message update(Message message) {
		    Connection con = DBConnection.connection();

		    try {
		      Statement stmt = con.createStatement();

		      stmt.executeUpdate("UPDATE message " + "SET messageText=\""
		          + message.getText() + "WHERE messageId=" + message.getId());

		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }

		    return message;
		  }

	public Vector<Message> selectAllMessagesFromUser(User user) {
		    Connection con = DBConnection.connection();
		    Vector<Message> result = new Vector<Message>();

		    try {
		      Statement stmt = con.createStatement();

		      ResultSet rs = stmt.executeQuery("SELECT messageId, messageText, dateOfCreation"
		          + "FROM message" + "WHERE author_user=" + user);

		      // Für jeden Eintrag wird nun ein User-Objekt erstellt.
		      while (rs.next()) {
		    	 Message message = new Message();
		    	message.setId(rs.getInt("messageId"));
		    	message.setText(rs.getString("messageText"));
		    	message.setDateOfCreation(rs.getDate("dateOfCreation"));

		        result.addElement(message);
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		    
		    return result;
		  }
	
	  public Vector<Message> selectAllMessages() {
		    Connection con = DBConnection.connection();
		    Vector<Message> result = new Vector<Message>();

		    try {
		      Statement stmt = con.createStatement();

		      ResultSet rs = stmt.executeQuery("SELECT messageId, messageText, author_userId, conversationId, visibility dateOfCreation"
		          + "FROM message");

		      // Für jeden Eintrag wird nun ein Message-Objekt erstellt.
		      while (rs.next()) {
			    	 Message message = new Message();
				   message.setId(rs.getInt("messageId"));
				   message.setText(rs.getString("messageText"));
				   //TODO: fix error
				   //message.setAuthor(rs.getInt("author_userId"));
				   // TODO: add conversation id Setter
				   //message.setConversationId(rs.getInt("conversationId"))
				   message.setVisible(rs.getBoolean("visibility"));
				   message.setDateOfCreation(rs.getDate("dateOfCreation"));

		        // Hinzufügen des neuen Objekts zum Ergebnisvektor
		        result.addElement(message);
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }

		    // Ergebnisvektor zurückgeben
		    return result;
		  }
}
