package hdm.itprojekt.texty.server.db;

import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.HashtagSubscription;
import hdm.itprojekt.texty.shared.bo.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * In der Mapper-Klasse HashtagSubscriptionMapper wird das Abonnement zwischen
 * einen Nutzer und eines Hashtags verwaltet und ausgegeben.
 * 
 * @author David
 *
 */
public class HashtagSubscriptionMapper {

	/**
	 * Hier wird die Klasse instanziiert.
	 */
	private static HashtagSubscriptionMapper hashtagSubscriptionMapper = null;

	/**
	 * Die statische Methode kann somit durch
	 * HashtagSubscriptionMapper.hashtagSubscriptionMapper() aufgerufen werden.
	 * Dadurch wird sichergestellt, dass nur eine einzelne Instanz der Mapper
	 * existiert.
	 * 
	 * @return HashtagSubscriptionMapper Objekt
	 */
	public static HashtagSubscriptionMapper hashtagSubscriptionMapper() {
		if (hashtagSubscriptionMapper == null) {
			hashtagSubscriptionMapper = new HashtagSubscriptionMapper();
		}
		return hashtagSubscriptionMapper;
	}

	/**
	 * Mithilfe von diesem Konstrukt wird verhindert, dass eine neue Instanz der
	 * Klasse erzeugt wird.
	 */
	protected HashtagSubscriptionMapper() {

	}

	/**
	 * Die Methode delete löscht ein Abonnement zwischen einen Nutzer und einem
	 * Hashtag aus der Datenbank.
	 * 
	 * @param hashtagSubscription
	 *            das gelöscht werden soll
	 */
	public void delete(HashtagSubscription hashtagSubscription) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			stmt.executeUpdate("DELETE FROM textydb.hashtagsubscription "
					+ "WHERE userId = "
					+ hashtagSubscription.getSubscriber().getId()
					+ " AND hashtagId = "
					+ hashtagSubscription.getSubscribedHashtag().getId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Die Methode HashtagSubscription speichert ein Abonnement zwischen Nutzer
	 * und Hashtag in die Datenbank.
	 * 
	 * @param hashtagSubscription
	 *            die in die Datenbank gespeichert werden soll
	 * @return HashtagSubscription-Objekt
	 */
	public HashtagSubscription insert(HashtagSubscription hashtagSubscription) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			stmt.executeUpdate("INSERT INTO textydb.hashtagsubscription (userId, hashtagId)"
					+ "VALUES ("
					+ "'"
					+ hashtagSubscription.getSubscriber().getId()
					+ "'"
					+ ", "
					+ "'"
					+ hashtagSubscription.getSubscribedHashtag().getId()
					+ "')");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// HashtagSubscription-Objekt zurückgeben
		return hashtagSubscription;
	}

	/**
	 * Die Methode selectAllSubscribedHashtags ruft alle abonnierten Hashtags
	 * eines bestimmten Nutzers auf.
	 * 
	 * @param subscriber
	 *            der Abonnement von dem wir wissen wollen, welche Hashtags er
	 *            abonniert hat
	 * @return Hahtag Vektor
	 */
	public Vector<Hashtag> selectAllSubscribedHashtags(User subscriber) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();
		Vector<Hashtag> result = new Vector<Hashtag>();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			ResultSet rs = stmt
					.executeQuery("SELECT hashtag.hashtagId, hashtag.keyword, hashtag.dateOfCreation FROM textydb.hashtag INNER JOIN textydb.hashtagsubscription ON hashtag.hashtagId = hashtagsubscription.hashtagId "
							+ "WHERE hashtagsubscription.userId = "
							+ subscriber.getId());

			// Für jeden Eintrag wird nun ein Usersubscription-Objekt erstellt.
			while (rs.next()) {
				Hashtag hashtag = new Hashtag();

				hashtag.setId(rs.getInt("hashtagId"));
				hashtag.setKeyword(rs.getString("keyword"));
				hashtag.setDateOfCreation(rs.getTimestamp("dateOfCreation"));
				// Das Objekt wird nun dem Ergebnisvektor hinzugefügt
				result.addElement(hashtag);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Hashtag Vektor zurückgeben
		return result;
	}
}
