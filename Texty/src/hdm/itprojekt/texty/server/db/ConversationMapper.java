package hdm.itprojekt.texty.server.db;

import hdm.itprojekt.texty.shared.bo.Conversation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * In der Mapper-Klasse ConversationMapper werden Methoden abgebildet um neue
 * Unterhaltungen zu erstellen oder diese auszugeben.
 * 
 * @author David
 *
 */
public class ConversationMapper {

	/**
	 * Hier wird die Klasse instanziiert.
	 */
	private static ConversationMapper conversationMapper = null;

	/**
	 * Die statische Methode kann somit durch
	 * ConversationMapper.conversationMapper() aufgerufen werden. Dadurch wird
	 * sichergestellt, dass nur eine einzelne Instanz der Mapper existiert.
	 * 
	 * @return ConversationMapper Objekt
	 */
	public static ConversationMapper conversationMapper() {
		if (conversationMapper == null) {
			conversationMapper = new ConversationMapper();
		}
		return conversationMapper;
	}

	/**
	 * Mithilfe von diesem Konstrukt wird verhindert, dass eine neue Instanz der
	 * Klasse erzeugt wird.
	 */
	protected ConversationMapper() {

	}

	/**
	 * Die Methode insert speichert ein neues Conversation-Objekt in die
	 * Datenbank
	 * 
	 * @param conversation
	 *            das in die Datenbank gespeichert wird
	 * @return Das Objekt mit korrigierten Angaben.
	 */
	public Conversation insert(Conversation conversation) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();
		// Da publicly ein Boolean-Objekt ist, muss dies vorher in einen
		// passenden Datentyp umgewandelt werden um in die Datenbank gespeichert
		// zu werden.
		int state = 0;
		if (conversation.isPublicly()) {
			state = 1;
		}

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Der höchste Primärschlüssel wird in der Datenbank gesucht
			ResultSet rs = stmt
					.executeQuery("SELECT MAX(conversationId) AS maxid "
							+ "FROM textydb.conversation ");

			if (rs.next()) {
				// Die conversationId wird nun anhand der aktuell höchsten
				// conversationId um
				// eins erhöht.
				conversation.setId(rs.getInt("maxid") + 1);
				stmt = con.createStatement();
				// Statement ausfüllen und als Query an die Datenbank schicken
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
		// Conversation-Objekt zurückgeben
		return conversation;
	}

	/**
	 * Die Methode selectAllConversations gibt alle Conversation-Objekt in einem
	 * Vektor zurück, wenn es sich dabei um nicht öffentliche Unterhaltungen
	 * handelt.
	 * 
	 * @return Conversation Vektor
	 */
	public Vector<Conversation> selectAllConversations() {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<Conversation> result = new Vector<Conversation>();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			ResultSet rs = stmt
					.executeQuery("SELECT conversationId, publicly, dateOfCreation FROM textydb.conversation WHERE publicly = 0");

			// Für jeden Eintrag wird nun ein Conversationobjekt erstellt.
			while (rs.next()) {

				Conversation conversation = new Conversation();

				conversation.setId(rs.getInt("conversationId"));
				conversation.setPublicly(rs.getBoolean("publicly"));
				conversation.setDateOfCreation(rs
						.getTimestamp("dateOfCreation"));
				// Das Objekt wird nun dem Ergebnisvektor hinzugefügt
				result.addElement(conversation);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ergebnisvektor zurückgeben
		return result;
	}

	/**
	 * 
	 * Die Methode selectAllPublicConversations gibt alle Conversation-Objekt in
	 * einem Vektor zurück, wenn es sich dabei um öffentliche Unterhaltungen
	 * handelt.
	 * 
	 * @return Conversation Vektor
	 */
	public Vector<Conversation> selectAllPublicConversations() {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<Conversation> result = new Vector<Conversation>();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			ResultSet rs = stmt
					.executeQuery("SELECT conversationId, publicly, dateOfCreation FROM textydb.conversation WHERE publicly = 1");

			// Für jeden Eintrag wird nun ein Conversationobjekt erstellt.
			while (rs.next()) {

				Conversation conversation = new Conversation();

				conversation.setId(rs.getInt("conversationId"));
				conversation.setPublicly(rs.getBoolean("publicly"));
				conversation.setDateOfCreation(rs
						.getTimestamp("dateOfCreation"));
				// Das Objekt wird nun dem Ergebnisvektor hinzugefügt
				result.addElement(conversation);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ergebnisvektor zurückgeben
		return result;
	}
}
