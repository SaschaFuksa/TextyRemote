package hdm.itprojekt.texty.server.db;

import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class MessageMapper {

	private static MessageMapper messageMapper = null;

	public static MessageMapper messageMapper() {
		if (messageMapper == null) {
			messageMapper = new MessageMapper();
		}
		return messageMapper;
	}

	protected MessageMapper() {

	}

	public User findAuthor(Message message) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT user.userId, user.givenName, user.familyName, user.email, user.dateOfCreation FROM textydb.message INNER JOIN textydb.user ON message.author_userId = user.userId "
							+ "WHERE message.messageId = " + message.getId());

			if (rs.next()) {
				User user = new User();

				user.setId(rs.getInt("user.userId"));
				user.setFirstName(rs.getString("user.givenName"));
				user.setLastName(rs.getString("user.familyName"));
				user.setEmail(rs.getString("user.email"));
				user.setDateOfCreation(rs.getTimestamp("user.dateOfCreation"));

				return user;
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
			return null;
		}

		return null;
	}

	public Vector<Hashtag> findHashtagsInMessage(Message message) {
		Connection con = DBConnection.connection();
		Vector<Hashtag> result = new Vector<Hashtag>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT hashtag.hashtagId, hashtag.keyword, hashtag.dateOfCreation, message.messageId FROM textydb.hashtag INNER JOIN textydb.hashtag_in_message ON hashtag.hashtagId = hashtag_in_message.hashtagId INNER JOIN textydb.message ON hashtag_in_message.messageId = message.messageId "
							+ "WHERE message.messageId = " + message.getId());

			// Für jeden Eintrag wird nun ein Message-Objekt erstellt.
			while (rs.next()) {
				Hashtag hashtag = new Hashtag();

				hashtag.setId(rs.getInt("hashtag.hashtagId"));
				hashtag.setKeyword(rs.getString("hashtag.keyword"));
				hashtag.setDateOfCreation(rs.getTimestamp("hashtag.dateOfCreation"));

				result.addElement(hashtag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ergebnisvektor zurückgeben
		return result;
	}

	public Vector<User> findReceivers(Message message) {
		Connection con = DBConnection.connection();
		Vector<User> result = new Vector<User>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT user.userId, user.givenName, user.familyName, user.email, user.dateOfCreation FROM textydb.message INNER JOIN textydb.receiver ON message.messageId = receiver.messageId INNER JOIN textydb.user ON receiver.receiver_userId = user.userId "
							+ "WHERE message.messageId = " + message.getId());

			// Für jeden Eintrag wird nun ein Message-Objekt erstellt.
			while (rs.next()) {
				User receiver = new User();

				receiver.setId(rs.getInt("user.userId"));
				receiver.setFirstName(rs.getString("user.givenName"));
				receiver.setLastName(rs.getString("user.familyName"));
				receiver.setEmail(rs.getString("user.email"));
				receiver.setDateOfCreation(rs.getTimestamp("user.dateOfCreation"));

				result.addElement(receiver);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ergebnisvektor zurückgeben
		return result;
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
						+ message.getAuthor().getId()
						+ ", "
						+ "'"
						+ message.getConversationID()
						+ "'"
						+ ", "
						+ "'"
						+ message.getText() + "'" + ", " + 1 + ")");
			}

			Statement stmtReceiver = con.createStatement();
			for (int i = 0; i < message.getListOfReceivers().size(); i++) {
				stmtReceiver
						.executeUpdate("INSERT INTO textydb.receiver (receiver_userId, messageId)"
								+ "VALUES ("
								+ message.getListOfReceivers().get(i).getId()
								+ ", " + message.getId() + ")");

			}

			Statement stmtHashtag = con.createStatement();
			for (int i = 0; i < message.getListOfHashtag().size(); i++) {
				stmtHashtag
						.executeUpdate("INSERT INTO textydb.hashtag_in_message (messageId, hashtagId)"
								+ "VALUES ("
								+ message.getId()
								+ ", "
								+ message.getListOfHashtag().get(i).getId()
								+ ")");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return message;
	}

	public Vector<Message> selectAllMessages() {
		Connection con = DBConnection.connection();
		Vector<Message> resultMessage = new Vector<Message>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rsMessages = stmt
					.executeQuery("SELECT message.messageId, message.author_userId, message.messageText, message.conversationId, message.visibility, message.dateOfCreation, user.userId, user.givenName, user.familyName, user.email, user.dateOfCreation FROM textydb.message INNER JOIN textydb.user ON message.author_userId = user.userId "
							+ " AND message.visibility = '1' ");

			// Für jeden Eintrag wird nun ein Message-Objekt erstellt.
			while (rsMessages.next()) {
				Message allmessages = new Message();

				allmessages.setId(rsMessages.getInt("message.messageId"));

				allmessages.setAuthor(findAuthor(allmessages));
				allmessages.setListOfReceivers(findReceivers(allmessages));
				allmessages
						.setListOfHashtag(findHashtagsInMessage(allmessages));

				allmessages
						.setText(rsMessages.getString("message.messageText"));
				allmessages.setConversationID(rsMessages
						.getInt("message.conversationId"));
				allmessages.setVisible(rsMessages
						.getBoolean("message.visibility"));
				allmessages.setDateOfCreation(rsMessages
						.getTimestamp("message.dateOfCreation"));

				resultMessage.addElement(allmessages);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultMessage;
	}

	public Vector<Message> selectAllMessagesFromUser(User currentuser) {
		Connection con = DBConnection.connection();
		Vector<Message> resultMessage = new Vector<Message>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rsMessages = stmt
					.executeQuery("SELECT DISTINCT message.messageId, message.author_userId, message.messageText, message.conversationId, message.visibility, message.dateOfCreation FROM textydb.message INNER JOIN textydb.receiver ON message.messageId = receiver.messageId "
							+ "WHERE author_userId = "
							+ currentuser.getId()
							+ " AND message.visibility = '1' "
							+ " OR receiver_userId = "
							+ currentuser.getId()
							+ " AND message.visibility = '1' ");

			// Für jeden Eintrag wird nun ein Message-Objekt erstellt.
			while (rsMessages.next()) {
				Message allmessages = new Message();

				allmessages.setId(rsMessages.getInt("message.messageId"));

				allmessages.setAuthor(findAuthor(allmessages));
				allmessages.setListOfReceivers(findReceivers(allmessages));
				allmessages
						.setListOfHashtag(findHashtagsInMessage(allmessages));

				allmessages
						.setText(rsMessages.getString("message.messageText"));
				allmessages.setConversationID(rsMessages
						.getInt("message.conversationId"));
				allmessages.setVisible(rsMessages
						.getBoolean("message.visibility"));
				allmessages.setDateOfCreation(rsMessages
						.getTimestamp("message.dateOfCreation"));

				resultMessage.addElement(allmessages);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultMessage;
	}

	// Gives back all public Messages from a specific User
	public Vector<Message> selectAllPublicMessagesFromUser(User user) {
		Connection con = DBConnection.connection();
		Vector<Message> resultMessage = new Vector<Message>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rsMessages = stmt
					.executeQuery("SELECT message.messageId, message.author_userId, message.messageText, message.conversationId, message.visibility, message.dateOfCreation FROM textydb.message inner join textydb.user ON message.author_userId = user.userId inner join textydb.conversation ON message.conversationId = conversation.conversationId "
							+ "WHERE user.userId = "
							+ user.getId()
							+ " AND conversation.publicly = '1'"
							+ " AND message.visibility = '1' ");

			// Für jeden Eintrag wird nun ein Message-Objekt erstellt.
			while (rsMessages.next()) {
				Message allmessages = new Message();

				allmessages.setId(rsMessages.getInt("message.messageId"));

				allmessages.setAuthor(findAuthor(allmessages));
				allmessages.setListOfReceivers(findReceivers(allmessages));
				allmessages
						.setListOfHashtag(findHashtagsInMessage(allmessages));

				allmessages
						.setText(rsMessages.getString("message.messageText"));
				allmessages.setConversationID(rsMessages
						.getInt("message.conversationId"));
				allmessages.setVisible(rsMessages
						.getBoolean("message.visibility"));
				allmessages.setDateOfCreation(rsMessages
						.getTimestamp("message.dateOfCreation"));

				resultMessage.addElement(allmessages);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultMessage;
	}

	// -------- Following Code is for the Home Section -----------
	// | | | |
	// v v v v

	// Gives back all all public Messages from all the subscribed Users from a
	// specific User
	public Vector<Message> selectAllPublicMessagesFromUsersubscription(User user) {
		Connection con = DBConnection.connection();
		Vector<Message> resultMessage = new Vector<Message>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rsMessages = stmt
					.executeQuery("SELECT usersubscription.subscribed_userId, message.messageId, message.author_userId, message.messageText, message.conversationId, message.visibility, message.dateOfCreation FROM textydb.usersubscription INNER JOIN textydb.user ON usersubscription.subscriber_userId = user.userId INNER JOIN textydb.message ON usersubscription.subscribed_userId = message.author_userId INNER JOIN textydb.conversation ON message.conversationId = conversation.conversationId "
							+ "WHERE usersubscription.subscriber_userId = "
							+ user.getId()
							+ " AND conversation.publicly = '1'"
							+ " AND message.visibility = '1' ");

			// Für jeden Eintrag wird nun ein Message-Objekt erstellt.
			while (rsMessages.next()) {
				Message allmessages = new Message();

				allmessages.setId(rsMessages.getInt("message.messageId"));

				allmessages.setAuthor(findAuthor(allmessages));
				allmessages.setListOfReceivers(findReceivers(allmessages));
				allmessages
						.setListOfHashtag(findHashtagsInMessage(allmessages));

				allmessages
						.setText(rsMessages.getString("message.messageText"));
				allmessages.setConversationID(rsMessages
						.getInt("message.conversationId"));
				allmessages.setVisible(rsMessages
						.getBoolean("message.visibility"));
				allmessages.setDateOfCreation(rsMessages
						.getTimestamp("message.dateOfCreation"));

				resultMessage.addElement(allmessages);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultMessage;
	}

	// Gives back all public Messages that contain a certain hashtag
	public Vector<Message> selectAllPublicMessagesWithHashtag(Hashtag hashtag) {
		Connection con = DBConnection.connection();
		Vector<Message> resultMessage = new Vector<Message>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rsMessages = stmt
					.executeQuery("SELECT message.messageId, message.author_userId, message.messageText, message.conversationId, message.visibility, message.dateOfCreation FROM textydb.message inner join textydb.hashtag_in_message ON message.messageId = hashtag_in_message.messageId inner join textydb.conversation ON message.conversationId = conversation.conversationId "
							+ "WHERE hashtag_in_message.hashtagId = "
							+ hashtag.getId()
							+ " AND conversation.publicly = '1'"
							+ " AND message.visibility = '1' ");

			// Für jeden Eintrag wird nun ein Message-Objekt erstellt.
			while (rsMessages.next()) {
				Message allmessages = new Message();

				allmessages.setId(rsMessages.getInt("message.messageId"));

				allmessages.setAuthor(findAuthor(allmessages));
				allmessages.setListOfReceivers(findReceivers(allmessages));
				allmessages
						.setListOfHashtag(findHashtagsInMessage(allmessages));

				allmessages
						.setText(rsMessages.getString("message.messageText"));
				allmessages.setConversationID(rsMessages
						.getInt("message.conversationId"));
				allmessages.setVisible(rsMessages
						.getBoolean("message.visibility"));
				allmessages.setDateOfCreation(rsMessages
						.getTimestamp("message.dateOfCreation"));

				resultMessage.addElement(allmessages);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultMessage;
	}

	public void delete(Message message) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			// The visibilty of the Message will be changed and users can not
			// see it anymore
			// TODO: Boolean Wert umwandeln für die DB? Überprüfen!
			stmt.executeUpdate("UPDATE textydb.message SET message.visibility = "
					+ 0 + " WHERE messageId = " + message.getId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Message update(Vector<Hashtag> listOfHashtag, Message message) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("UPDATE textydb.message SET message.messageText = "
					+ "'"
					+ message.getText()
					+ "'"
					+ "WHERE messageId = "
					+ message.getId());

			Statement stmtDeleteHashtag = con.createStatement();

			stmtDeleteHashtag
					.executeUpdate("DELETE FROM textydb.hashtag_in_message WHERE "
							+ "messageId = " + message.getId());

			Statement stmtHashtag = con.createStatement();

			for (Hashtag hashtag : message.getListOfHashtag()) {
				stmtHashtag
						.executeUpdate("INSERT INTO textydb.hashtag_in_message (messageId, hashtagId)"
								+ "VALUES ("
								+ message.getId()
								+ ", "
								+ hashtag.getId() + ")");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return message;
	}
}
