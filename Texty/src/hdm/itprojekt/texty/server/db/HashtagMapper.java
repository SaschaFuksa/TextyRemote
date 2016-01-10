package hdm.itprojekt.texty.server.db;

import hdm.itprojekt.texty.shared.bo.Hashtag;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * In der Mapper-Klasse HashtagMapper werden Methoden abgebildet, um Hashtags in
 * der Datenbank zu speichern, l�schen und abzurufen.
 * 
 * @author David
 *
 */
public class HashtagMapper {

	/**
	 * Hier wird die Klasse instanziiert.
	 */
	private static HashtagMapper hashtagMapper = null;

	/**
	 * Die statische Methode kann somit durch HashtagMapper.hashtagMapper()
	 * aufgerufen werden. Dadurch wird sichergestellt, dass nur eine einzelne
	 * Instanz der Mapper existiert.
	 * 
	 * @return HashtagMapper Objekt
	 */
	public static HashtagMapper hashtagMapper() {
		if (hashtagMapper == null) {
			hashtagMapper = new HashtagMapper();
		}
		return hashtagMapper;
	}

	/**
	 * Mithilfe von diesem Konstrukt wird verhindert, dass eine neue Instanz der
	 * Klasse erzeugt wird.
	 */
	protected HashtagMapper() {

	}

	/**
	 * Die Methode delete l�scht ein Hashtag-Objekt aus der Datenbank, der
	 * mithilfe seiner Id eindeutig identifiziert wurde.
	 * 
	 * @param hashtag
	 */
	public void delete(Hashtag hashtag) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();

		try {
			// Ergebnisvektor vorbereiten
			Statement stmt = con.createStatement();
			// Statement ausf�llen und als Query an die Datenbank schicken
			stmt.executeUpdate("DELETE FROM hashtag " + "WHERE hashtagId="
					+ hashtag.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Die Methode findAll gibt alle Hashtag-Objekte zur�ck die in der Datenbank
	 * gespeichert sind.
	 * 
	 * @return Hashtag Vektor
	 */
	public Vector<Hashtag> findAll() {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<Hashtag> result = new Vector<Hashtag>();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Statement ausf�llen und als Query an die Datenbank schicken
			ResultSet rs = stmt
					.executeQuery("SELECT hashtagId, keyword, dateOfCreation FROM textydb.hashtag");

			// F�r jeden Eintrag wird nun ein Hashtag-Objekt erstellt.
			while (rs.next()) {
				Hashtag hashtag = new Hashtag();
				hashtag.setId(rs.getInt("hashtagId"));
				hashtag.setKeyword(rs.getString("keyword"));
				hashtag.setDateOfCreation(rs.getTimestamp("dateOfCreation"));

				// Das Objekt wird nun dem Ergebnisvektor hinzugef�gt
				result.addElement(hashtag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ergebnisvektor zur�ckgeben
		return result;
	}

	/**
	 * Die Methode insert speichert ein Hashtag-Objekt in die Datenbank
	 * 
	 * @param hashtag
	 *            das in die Datenbank gespeichert werden soll
	 * @return Hashtag-Objekt
	 */
	public Hashtag insert(Hashtag hashtag) {
		// Datenbankverbindung holen
		Connection con = DBConnection.connection();

		try {
			// Neues Statement anlegen
			Statement stmt = con.createStatement();
			// Der h�chste Prim�rschl�ssel wird in der Datenbank gesucht
			ResultSet rs = stmt.executeQuery("SELECT MAX(hashtagId) AS maxid "
					+ "FROM textydb.hashtag ");

			if (rs.next()) {
				// Die hashtagId wird nun anhand der aktuell h�chsten hashtagId
				// um
				// eins erh�ht.
				hashtag.setId(rs.getInt("maxid") + 1);

				stmt = con.createStatement();
				// Statement ausf�llen und als Query an die Datenbank schicken
				stmt.executeUpdate("INSERT INTO textydb.hashtag (hashtagId, keyword)"
						+ "VALUES ("
						+ hashtag.getId()
						+ ", "
						+ "'"
						+ hashtag.getKeyword() + "')");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Hashtag-Objekt zur�ckgeben
		return hashtag;
	}
}
