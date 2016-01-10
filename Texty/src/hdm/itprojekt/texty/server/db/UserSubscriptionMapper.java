package hdm.itprojekt.texty.server.db;

import hdm.itprojekt.texty.shared.bo.User;
import hdm.itprojekt.texty.shared.bo.UserSubscription;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * In der Mapper-Klasse UserSubscriptionMapper wird das Abonnement zwischen zwei
 * Nutzerverwaltet und ausgegeben.
 * 
 * @author David
 *
 */
public class UserSubscriptionMapper {

	/**
	 * Hier wird die Klasse instanziiert.
	 */
	private static UserSubscriptionMapper userSubscriptionMapper = null;

	/**
	 * Die statische Methode kann somit durch
	 * UserSubscriptionMapper.userSubscriptionMapper() aufgerufen werden.
	 * Dadurch wird sichergestellt, dass nur eine einzelne Instanz der Mapper
	 * existiert.
	 * 
	 * @return UserSubscriptionMapper Objekt
	 */
	public static UserSubscriptionMapper userSubscriptionMapper() {
		if (userSubscriptionMapper == null) {
			userSubscriptionMapper = new UserSubscriptionMapper();
		}
		return userSubscriptionMapper;
	}

	/**
	 * Mithilfe von diesem Konstrukt wird verhindert, dass eine neue Instanz der
	 * Klasse erzeugt wird.
	 */
	protected UserSubscriptionMapper() {

	}

	/**
	 * Die Methode delete löscht ein Abonnement zu einem anderen Nutzer.
	 * 
	 * @param usersubscription
	 *            das Objekt, dass aus der Datenbank gelöscht werden soll
	 */
	public void delete(UserSubscription usersubscription) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			stmt.executeUpdate("DELETE FROM textydb.usersubscription "
					+ "WHERE subscriber_userId = "
					+ usersubscription.getSubscriber().getId()
					+ " AND subscribed_userId = "
					+ usersubscription.getSubscribedUser().getId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Die Methode insert speichert ein Abonnement von einem Nutzer zu einem
	 * anderen.
	 * 
	 * @param userSubscription
	 *            das Objekt, dass in die Datenbank gespeichert werden soll.
	 * @return UserSubscription-Objekt
	 */
	public UserSubscription insert(UserSubscription userSubscription) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			stmt.executeUpdate("INSERT INTO textydb.usersubscription (subscriber_userId, subscribed_userId)"
					+ "VALUES ("
					+ "'"
					+ userSubscription.getSubscriber().getId()
					+ "'"
					+ ", "
					+ "'" + userSubscription.getSubscribedUser().getId() + "')");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// UserSubscription-Objekt zurückgeben
		return userSubscription;
	}

	/**
	 * Die Methode selectAllSubscribedUsers gibt alle Nutzer eines Abonnierenden
	 * zurück.
	 * 
	 * @param subscriber
	 *            von dem die Abonnements zurückgegeben werden sollen
	 * @return User Vektor
	 */
	public Vector<User> selectAllSubscribedUsers(User subscriber) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<User> result = new Vector<User>();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			ResultSet rs = stmt
					.executeQuery("SELECT userId, givenName, familyName, email, user.dateOfCreation FROM textydb.user INNER JOIN textydb.usersubscription ON user.userId = usersubscription.subscribed_userId "
							+ "WHERE subscriber_userId = " + subscriber.getId());

			// Für jeden Eintrag wird nun ein Usersubscription-Objekt erstellt.
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("userId"));
				user.setFirstName(rs.getString("givenName"));
				user.setLastName(rs.getString("familyName"));
				user.setEmail(rs.getString("email"));
				user.setDateOfCreation(rs.getTimestamp("dateOfCreation"));
				// Das Objekt wird nun dem Ergebnisvektor hinzugefügt
				result.addElement(user);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// User Vektor zurückgeben
		return result;
	}
}
