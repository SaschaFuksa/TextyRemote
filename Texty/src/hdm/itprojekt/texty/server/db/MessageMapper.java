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
}
