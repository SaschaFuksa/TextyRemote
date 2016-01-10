package hdm.itprojekt.texty.server.db;

import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * In der Mapper-Klasse MessageMapper werden Nachrichten verwaltet und
 * ausgegeben.
 * 
 * @author David
 */
public class MessageMapper {

	/**
	 * Hier wird die Klasse instanziiert.
	 */
	private static MessageMapper messageMapper = null;

	/**
	 * Die statische Methode kann somit durch MessageMapper.messageMapper()
	 * aufgerufen werden. Dadurch wird sichergestellt, dass nur eine einzelne
	 * Instanz der Mapper existiert.
	 * 
	 * @return MessageMapper Objekt
	 */
	public static MessageMapper messageMapper() {
		if (messageMapper == null) {
			messageMapper = new MessageMapper();
		}
		return messageMapper;
	}

	/**
	 * Mithilfe von diesem Konstrukt wird verhindert, dass eine neue Instanz der
	 * Klasse erzeugt wird.
	 */
	protected MessageMapper() {

	}

	/**
	 * Die Methode findAuthor gibt den Autor einer bestimmten Nachricht ist.
	 * 
	 * @param message
	 *            über die messageId wird bestimmt, von welcher Nachricht der
	 *            Autor bestimmt werden soll.
	 * @return User-Objekt
	 */
	public User findAuthor(Message message) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			ResultSet rs = stmt
					.executeQuery("SELECT user.userId, user.givenName, user.familyName, user.email, user.dateOfCreation FROM textydb.message INNER JOIN textydb.user ON message.author_userId = user.userId "
							+ "WHERE message.messageId = " + message.getId());
			// Es wird nun geprüft ob eine Nachricht mit der entsprechenden Id
			// vorhanden ist
			if (rs.next()) {
				User user = new User();

				user.setId(rs.getInt("user.userId"));
				user.setFirstName(rs.getString("user.givenName"));
				user.setLastName(rs.getString("user.familyName"));
				user.setEmail(rs.getString("user.email"));
				user.setDateOfCreation(rs.getTimestamp("user.dateOfCreation"));
				// User-Objekt zurückgeben
				return user;
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * Die Methode findHahtagsInMessage gibt alle Hashtags zurück, die einer
	 * bestimmten Nachricht angehängt wurden.
	 * 
	 * @param message
	 *            von der die Hashtags ausgegeben werden sollen
	 * @return Hashtag Vektor
	 */
	public Vector<Hashtag> findHashtagsInMessage(Message message) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<Hashtag> result = new Vector<Hashtag>();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			ResultSet rs = stmt
					.executeQuery("SELECT hashtag.hashtagId, hashtag.keyword, hashtag.dateOfCreation, message.messageId FROM textydb.hashtag INNER JOIN textydb.hashtag_in_message ON hashtag.hashtagId = hashtag_in_message.hashtagId INNER JOIN textydb.message ON hashtag_in_message.messageId = message.messageId "
							+ "WHERE message.messageId = " + message.getId());

			// Für jeden Eintrag wird nun ein Message-Objekt erstellt.
			while (rs.next()) {
				Hashtag hashtag = new Hashtag();

				hashtag.setId(rs.getInt("hashtag.hashtagId"));
				hashtag.setKeyword(rs.getString("hashtag.keyword"));
				hashtag.setDateOfCreation(rs
						.getTimestamp("hashtag.dateOfCreation"));
				// Das Objekt wird nun dem Ergebnisvektor hinzugefügt
				result.addElement(hashtag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ergebnisvektor zurückgeben
		return result;
	}

	/**
	 * Die Methode findReceivers gibt alle Empfänger einer bestimmten Nachricht
	 * zurück.
	 * 
	 * @param message
	 *            von der die Empfänger ausgegeben werden sollen
	 * @return User Objekt
	 */
	public Vector<User> findReceivers(Message message) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<User> result = new Vector<User>();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
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
				receiver.setDateOfCreation(rs
						.getTimestamp("user.dateOfCreation"));
				// Das Objekt wird nun dem Ergebnisvektor hinzugefügt
				result.addElement(receiver);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ergebnisvektor zurückgeben
		return result;
	}

	/**
	 * Die Methode insert schreibt ein Message-Objekt in die Datenbank. Ein
	 * Message Objekt besteht aus der Nachricht selbst, die Empfänger und die
	 * angehängten Hashtags.
	 * 
	 * @param message
	 * @return
	 */
	public Message insert(Message message) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Der höchste Primärschlüssel wird in der Datenbank gesucht
			ResultSet rs = stmt.executeQuery("SELECT MAX(messageId) AS maxid "
					+ "FROM textydb.message ");

			if (rs.next()) {
				// Die messageId wird nun anhand der aktuell höchsten messageId
				// um
				// eins erhöht.
				message.setId(rs.getInt("maxid") + 1);

				stmt = con.createStatement();
				// Statement ausfüllen und als Query an die Datenbank schicken
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
			// Neues Statement anlegen
			Statement stmtReceiver = con.createStatement();
			// Für jeden übergebenen Empfänger wird der SQL Befehl augeführt um
			// ihn in die Datenbank zu speichern
			for (int i = 0; i < message.getListOfReceivers().size(); i++) {
				stmtReceiver
						.executeUpdate("INSERT INTO textydb.receiver (receiver_userId, messageId)"
								+ "VALUES ("
								+ message.getListOfReceivers().get(i).getId()
								+ ", " + message.getId() + ")");

			}
			// Neues Statement anlegen
			Statement stmtHashtag = con.createStatement();
			// Für jeden angehangenen Hashtag wird der SQL Befehl ausgeführt um
			// ihn in die Datenbank zu speichern
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
		// Message-Objekt zurückgeben
		return message;
	}

	/**
	 * Die Methode selectAllMessages gibt alle sichtbaren Nachrichten zurück
	 * 
	 * @return
	 */
	public Vector<Message> selectAllMessages() {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<Message> resultMessage = new Vector<Message>();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
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
				// Das Objekt wird nun dem Ergebnisvektor hinzugefügt
				resultMessage.addElement(allmessages);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Message Vektor zurückgeben
		return resultMessage;
	}

	/**
	 * Die Methode selectAllMessagesFromUser gibt alle sichtbaren Nachrichten
	 * eines bestimmten Nutzers zurück, bei denen er Autor und/oder Empfänger
	 * ist.
	 * 
	 * @param currentuser
	 *            der aktulle Nutzer von dem die Nachrichten ausgegeben werden
	 *            sollen
	 * @return Message Vektor
	 */
	public Vector<Message> selectAllMessagesFromUser(User currentuser) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<Message> resultMessage = new Vector<Message>();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
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
				// Das Objekt wird nun dem Ergebnisvektor hinzugefügt
				resultMessage.addElement(allmessages);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Message Vektor zurückgeben
		return resultMessage;
	}

	// -------- Following Code is for the Home Section -----------

	/**
	 * Die Methode selectAllPublicMessagesFromUser gibt allen öffentlichen und
	 * sichtbare Nachrichten eines bestimmten Nutzers zurück, die von ihm
	 * verfasst wurden.
	 * 
	 * @param user
	 *            von dem die öffentlichen Nachrichten ausgegeben werden sollen
	 * @return
	 */
	public Vector<Message> selectAllPublicMessagesFromUser(User user) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<Message> resultMessage = new Vector<Message>();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
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
				// Das Objekt wird nun dem Ergebnisvektor hinzugefügt
				resultMessage.addElement(allmessages);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Message Vektor zurückgeben
		return resultMessage;
	}

	/**
	 * Die Methode selectAllPublicMessagesFromUsersubscription gibt alle
	 * öffentlichen Nachrichten der von Ihm abonnierten Nutzer zurück.
	 * 
	 * @param user
	 *            der als abonnierender identifiziert wird
	 * @return Message Vektor
	 */
	public Vector<Message> selectAllPublicMessagesFromUsersubscription(User user) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<Message> resultMessage = new Vector<Message>();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
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
				// Das Objekt wird nun dem Ergebnisvektor hinzugefügt
				resultMessage.addElement(allmessages);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Message Vektor zurückgeben
		return resultMessage;
	}

	/**
	 * Die Methode selectAllMesagesFromConversation gibt alle sichtbaren
	 * Nachrichten einer Unterhaltung aus.
	 * 
	 * @param message
	 *            um die conversationId herauszufinden.
	 * @return
	 */
	public Vector<Message> selectAllMesagesFromConversation(Message message) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<Message> resultMessage = new Vector<Message>();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			ResultSet rsMessages = stmt
					.executeQuery("SELECT message.messageId, message.author_userId, message.messageText, message.conversationId, message.visibility, message.dateOfCreation FROM textydb.message inner join textydb.conversation ON message.conversationId = conversation.conversationId "
							+ "WHERE message.conversationId = "
							+ message.getConversationID()
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
				// Das Objekt wird nun dem Ergebnisvektor hinzugefügt
				resultMessage.addElement(allmessages);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Message Vektor zurückgeben
		return resultMessage;
	}

	// Gives back all public Messages that contain a certain hashtag
	/**
	 * Die Methode selectAllPublicMessagesWithHashtag gibt alle öffentlichen
	 * Nachrichten aus, die einen bestimmten Hashtag enthalten.
	 * 
	 * @param hashtag
	 *            der in einer Nachricht verlinkt wurde
	 * @return
	 */
	public Vector<Message> selectAllPublicMessagesWithHashtag(Hashtag hashtag) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<Message> resultMessage = new Vector<Message>();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
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
				// Das Objekt wird nun dem Ergebnisvektor hinzugefügt
				resultMessage.addElement(allmessages);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Message Vektor zurückgeben
		return resultMessage;
	}

	/**
	 * Die methode delete setzt die visibility einer bestimmten Nachricht auf 0.
	 * Nachrichten mit der visibility 0 werden Nutzern nicht mehr angezeigt.
	 * 
	 * @param message
	 *            um zu bestimmen welche Nachricht "gelöscht" werden soll.
	 */
	public void delete(Message message) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			stmt.executeUpdate("UPDATE textydb.message SET message.visibility = "
					+ 0 + " WHERE messageId = " + message.getId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Die Methode update ändert den Nachrichtentext oder die angehängten
	 * Hashtags einer Nachricht.
	 * 
	 * @param listOfHashtag
	 *            gibt alle Hashtags einer Nachricht zurück
	 * @param message
	 *            um zu bestimmen an welcher Nachricht etwas geändert werden
	 *            soll.
	 * @return Message-Objekt
	 */
	public Message update(Vector<Hashtag> listOfHashtag, Message message) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			stmt.executeUpdate("UPDATE textydb.message SET message.messageText = "
					+ "'"
					+ message.getText()
					+ "'"
					+ "WHERE messageId = "
					+ message.getId());

			// Neues Statement anlegen
			Statement stmtDeleteHashtag = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			stmtDeleteHashtag
					.executeUpdate("DELETE FROM textydb.hashtag_in_message WHERE "
							+ "messageId = " + message.getId());

			// Neues Statement anlegen
			Statement stmtHashtag = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
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
		// Message Objekt zurückgeben
		return message;
	}
}
