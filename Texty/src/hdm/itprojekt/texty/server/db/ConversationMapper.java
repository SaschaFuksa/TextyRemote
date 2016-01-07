package hdm.itprojekt.texty.server.db;

import hdm.itprojekt.texty.shared.bo.Conversation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class ConversationMapper {

	private static ConversationMapper conversationMapper = null;

	public static ConversationMapper conversationMapper() {
		if (conversationMapper == null) {
			conversationMapper = new ConversationMapper();
		}
		return conversationMapper;
	}

	protected ConversationMapper() {

	}

	public Conversation insert(Conversation conversation) {
		Connection con = DBConnection.connection();
		int state = 0;
		if (conversation.isPublicly()) {
			state = 1;
		}

		try {
			Statement stmt = con.createStatement();
			// Find highest Primarykey
			ResultSet rs = stmt
					.executeQuery("SELECT MAX(conversationId) AS maxid "
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
						+ state
						+ ")");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conversation;
	}

	public Vector<Conversation> selectAllConversations() {
		Connection con = DBConnection.connection();
		Vector<Conversation> result = new Vector<Conversation>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT conversationId, publicly, dateOfCreation FROM textydb.conversation WHERE publicly = 0");

			// Für jeden Eintrag wird nun ein Conversationobjekt erstellt.

			while (rs.next()) {

				Conversation conversation = new Conversation();

				conversation.setId(rs.getInt("conversationId"));
				conversation.setPublicly(rs.getBoolean("publicly"));
				conversation.setDateOfCreation(rs.getTimestamp("dateOfCreation"));

				result.addElement(conversation);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ergebnisvektor zurückgeben
		return result;
	}
	
	public Vector<Conversation> selectAllPublicConversations() {
		Connection con = DBConnection.connection();
		Vector<Conversation> result = new Vector<Conversation>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT conversationId, publicly, dateOfCreation FROM textydb.conversation WHERE publicly = 1");

			// Für jeden Eintrag wird nun ein Conversationobjekt erstellt.

			while (rs.next()) {

				Conversation conversation = new Conversation();

				conversation.setId(rs.getInt("conversationId"));
				conversation.setPublicly(rs.getBoolean("publicly"));
				conversation.setDateOfCreation(rs.getTimestamp("dateOfCreation"));

				result.addElement(conversation);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ergebnisvektor zurückgeben
		return result;
	}
}
