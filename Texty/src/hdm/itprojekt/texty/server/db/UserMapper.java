package hdm.itprojekt.texty.server.db;

import hdm.itprojekt.texty.shared.bo.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * In der Mapper-Klasse UserMapper werden die einzelnen Nutzer verwaltet und
 * ausgegeben.
 * 
 * @author David
 */

public class UserMapper {

	/**
	 * Hier wird die Klasse instanziiert.
	 */
	private static UserMapper userMapper = null;

	/**
	 * Die statische Methode kann somit durch UserMapper.userMapper() aufgerufen
	 * werden. Dadurch wird sichergestellt, dass nur eine einzelne Instanz der
	 * Mapper existiert.
	 * 
	 * @return UserMapper Objekt
	 */
	public static UserMapper userMapper() {
		if (userMapper == null) {
			userMapper = new UserMapper();
		}
		return userMapper;
	}

	/**
	 * Mithilfe von diesem Konstrukt wird verhindert, dass eine neue Instanz der
	 * Klasse erzeugt wird.
	 */
	protected UserMapper() {
	}

	/**
	 * Mit der Methode findAll werden alle Nutzer aus der Datenbank aufgerufen
	 * und in einen Vektor geschrieben.
	 * 
	 * @return Ein User-Vektor mit zugehöriger Id, Vorname, Nachname, Email und
	 *         Erstellungszeitpunkt.
	 */
	public Vector<User> findAll() {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<User> result = new Vector<User>();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			ResultSet rs = stmt
					.executeQuery("SELECT userId, givenName, familyName, email, dateOfCreation FROM textydb.user");

			// Für jeden Eintrag wird nun ein User-Objekt erstellt.
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

		// Ergebnisvektor zurückgeben
		return result;
	}

	/**
	 * Mit der Methode findByEmail wird ein bestimmtes Userobjekt mit der der
	 * bekannten Email zurückgegeben
	 * 
	 * @param email
	 *            Attribut eines Nutzers
	 * @return User-Objekt, dass der Email entspricht
	 */
	public User findByEmail(String email) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			ResultSet rs = stmt
					.executeQuery("SELECT userId, givenName, familyName, email, dateOfCreation FROM textydb.user "
							+ "WHERE email = '" + email + "'");
			// Es wird nun geprüft ob ein Nutzer mit der entsprechenden Email
			// vorhanden ist
			if (rs.next()) {
				// Ergebnis-Tupel wird nun in ein Objekt umgewandlet
				User user = new User();
				user.setId(rs.getInt("userId"));
				user.setFirstName(rs.getString("givenName"));
				user.setLastName(rs.getString("familyName"));
				user.setEmail(rs.getString("email"));
				user.setDateOfCreation(rs.getTimestamp("dateOfCreation"));
				return user;
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
			return null;
		}
		// Findet sich kein Nutzer mit der entsprechenden Email wird nichts
		// zurückgegeben.
		return null;
	}

	/**
	 * Die Methode insert speichert ein neues User-Objekt in die Datenbank.
	 * 
	 * @param user
	 *            der in die Datenbank gespeichert wird
	 * @return Das Objekt mit korriegierten Angaben.
	 */
	public User insert(User user) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Der höchste Primärschlüssel wird in der Datenbank gesucht
			ResultSet rs = stmt.executeQuery("SELECT MAX(userId) AS maxid "
					+ "FROM textydb.user ");

			if (rs.next()) {
				// Die userId wird nun anhand der aktuell höchsten userId um
				// eins erhöht.
				user.setId(rs.getInt("maxid") + 1);
				stmt = con.createStatement();
				// Statement ausfüllen und als Query an die Datenbank schicken
				stmt.executeUpdate("INSERT INTO textydb.user (userId, givenName, familyName, email)"
						+ "VALUES ("
						+ user.getId()
						+ ", "
						+ "'"
						+ user.getFirstName()
						+ "'"
						+ ", "
						+ "'"
						+ user.getLastName()
						+ "'"
						+ ", "
						+ "'"
						+ user.getEmail() + "')");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// User-Objekt zurückgeben
		return user;
	}

	/**
	 * Die Methode update setzt für einen bestimmten User, der durch die userId
	 * bestimmt wird, den Vor- und Nachnamen oder korrigiert den aktuellen Vor-
	 * und Nachnamen.
	 * 
	 * @param user
	 *            von dem wir seine Id erhalten um ihn eindeutig zu
	 *            identifizieren
	 * @return Das korrigierte User-Objekt
	 */
	public User update(User user) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausfüllen und als Query an die Datenbank schicken
			stmt.executeUpdate("UPDATE textydb.user SET givenName = '"
					+ user.getFirstName() + "'," + "familyName='"
					+ user.getLastName() + "' " + "WHERE userid="
					+ user.getId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// User-Objekt zurückgeben
		return user;
	}
}
