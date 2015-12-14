package hdm.itprojekt.texty.server.db;

import java.sql.*;
import java.util.Vector;

import hdm.itprojekt.texty.shared.bo.*;

public class ConversationMapper {

	private static ConversationMapper conversationMapper = null;

	protected ConversationMapper() {

	}

	public static ConversationMapper conversationMapper() {
		if (conversationMapper == null) {
			conversationMapper = new ConversationMapper();
		}
		return conversationMapper;
	}
	
	public Conversation insert(Conversation conversation) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			// Find highest Primarykey
			ResultSet rs = stmt.executeQuery("SELECT MAX(conversationId) AS maxid "
					+ "FROM textydb.conversation ");

			if (rs.next()) {

				conversation.setId(rs.getInt("maxid") + 1);

				stmt = con.createStatement();

				// Highest Primarykey has been found and set, now we insert it
				// into the DB
				stmt.executeUpdate("INSERT INTO textydb.conversation (conversationId, publicly)"
						+ "VALUES ("
						+ conversation.getId()
						+ ", "
						+ conversation.isPublicly() + ")");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return conversation;
	}

	 public Vector<Conversation> findAllConversations() {
		    Connection con = DBConnection.connection();
		    // Ergebnisvektor vorbereiten
		    Vector<Conversation> result = new Vector<Conversation>();

		    try {
		      Statement stmt = con.createStatement();

		      ResultSet rs = stmt.executeQuery("SELECT conversationId, publicly, dateOfCreation"
		          + "FROM conversation");

		      // Für jeden Eintrag wird nun ein Conversation-Objekt erstellt.
		      while (rs.next()) {
		    	  Conversation conversation = new Conversation();
		    	conversation.setId(rs.getInt("conversationId"));
		    	conversation.setPublicly(rs.getBoolean("publicly"));
		    	conversation.setDateOfCreation(rs.getDate("dateOfCreation"));

		        // Hinzufügen des neuen Objekts zum Ergebnisvektor
		        result.addElement(conversation);
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }

		    // Ergebnisvektor zurückgeben
		    return result;
		  }
}
