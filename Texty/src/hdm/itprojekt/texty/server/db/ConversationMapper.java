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
					.executeQuery("SELECT conversationId, publicly, dateOfCreation FROM textydb.conversation");

			// Für jeden Eintrag wird nun ein Conversationobjekt erstellt.

			while (rs.next()) {

				Conversation conversation = new Conversation();

				conversation.setId(rs.getInt("conversationId"));
				conversation.setPublicly(rs.getBoolean("publicly"));
				conversation.setDateOfCreation(rs.getTime("dateOfCreation"));

				result.addElement(conversation);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ergebnisvektor zurückgeben
		return result;
	}

	public Vector<Conversation> selectAllConversationsFromUser(User currentuser) {
		Connection con = DBConnection.connection();
		Vector<Conversation> result = new Vector<Conversation>();
		// Vektor für alle Messages des User, in der er Autor oder Empfänger
		// ist. Unabhängig der Unterhaltungen
		Vector<Message> messageResult = new Vector<Message>();
		Vector<Hashtag> allHashtagResult = new Vector<Hashtag>();
		Vector<User> allUserResult = new Vector<User>();

		try {
			Statement stmtHash = con.createStatement();

			ResultSet rsHash = stmtHash
					.executeQuery("SELECT * FROM textydb.hashtag");

			while (rsHash.next()) {

				Hashtag hashtag = new Hashtag();

				hashtag.setId(rsHash.getInt("hashtagId"));
				hashtag.setKeyword(rsHash.getString("keyword"));
				hashtag.setDateOfCreation(rsHash.getTime("dateOfCreation"));

				allHashtagResult.addElement(hashtag);

			}

			Statement stmtUser = con.createStatement();

			ResultSet rsUser = stmtUser
					.executeQuery("SELECT * FROM textydb.user");

			while (rsUser.next()) {

				User user = new User();

				user.setId(rsUser.getInt("userId"));
				user.setFirstName(rsUser.getString("givenName"));
				user.setLastName(rsUser.getString("familyName"));
				user.setEmail(rsUser.getString("email"));
				user.setDateOfCreation(rsUser.getTime("dateOfCreation"));

				allUserResult.addElement(user);

			}

			Statement stmtMessage = con.createStatement();

			ResultSet rsMessage = stmtMessage
					.executeQuery("SELECT * FROM textydb.message");

			while (rsMessage.next()) {

				Message message = new Message();

				message.setId(rsMessage.getInt("messageId"));
				message.setText(rsMessage.getString("messageText"));
				for (int i = 0; i < allUserResult.size(); i++) {
					if (allUserResult.get(i).getId() == rsMessage
							.getInt("author_userId")) {
						message.setAuthor(allUserResult.get(i));
					}
				}
				message.setConversationID(rsMessage.getInt("conversationId"));
				message.setVisible(rsMessage.getBoolean("visibility"));
				message.setDateOfCreation(rsMessage.getDate("dateOfCreation"));

				messageResult.addElement(message);

			}

			Statement stmtReceiver = con.createStatement();

			ResultSet rsReceiver = stmtReceiver
					.executeQuery("SELECT * FROM textydb.receiver");

			while (rsReceiver.next()) {

				for (int i = 0; i < allUserResult.size(); i++) {
					if (allUserResult.get(i).getId() == rsReceiver
							.getInt("receiver_userId")) {
						for (int x = 0; x < messageResult.size(); x++) {
							if (messageResult.get(x).getId() == rsReceiver
									.getInt("messageId")) {
								messageResult.get(x).addReceivers(
										allUserResult.get(i));
							}
						}
					}
				}
			}

			Statement stmtHashInMessage = con.createStatement();

			ResultSet rsHashInMessage = stmtHashInMessage
					.executeQuery("SELECT * FROM textydb.hashtag_in_message");

			while (rsHashInMessage.next()) {

				for (int i = 0; i < allHashtagResult.size(); i++) {
					if (allHashtagResult.get(i).getId() == rsHashInMessage
							.getInt("hashtagId")) {
						for (int x = 0; x < messageResult.size(); x++) {
							if (messageResult.get(x).getId() == rsHashInMessage
									.getInt("messageId")) {
								messageResult.get(x).addHashtag(
										allHashtagResult.get(i));
							}
						}
					}
				}
			}

			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT * FROM textydb.conversation Where publicly = 0");

			while (rs.next()) {

				Conversation conversation = new Conversation();
				Vector<Message> message = new Vector<Message>();

				conversation.setId(rs.getInt("conversationId"));
				conversation.setPublicly(rs.getBoolean("publicly"));
				conversation.setDateOfCreation(rs.getTime("dateOfCreation"));

				for (int i = 0; i < messageResult.size(); i++) {
					if (messageResult.get(i).getConversationID() == conversation.getId()) {
						message.addElement(messageResult
								.get(i));
					}
				}

				conversation.setListOfMessage(message);
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
