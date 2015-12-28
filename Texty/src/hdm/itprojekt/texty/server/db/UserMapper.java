package hdm.itprojekt.texty.server.db;

import hdm.itprojekt.texty.shared.bo.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class UserMapper {

	private static UserMapper userMapper = null;

	protected UserMapper() {
	}

	public static UserMapper userMapper() {
		if (userMapper == null) {
			userMapper = new UserMapper();
		}
		return userMapper;
	}

	public User insert(User user) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			// Find highest Primarykey
			ResultSet rs = stmt.executeQuery("SELECT MAX(userId) AS maxid "
					+ "FROM textydb.user ");

			if (rs.next()) {

				user.setId(rs.getInt("maxid") + 1);

				stmt = con.createStatement();

				// TODO: firstname lastname ändern

				// Highest Primarykey has been found and set, now we insert it
				// into the DB
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
		return user;
	}

	public User update(User user) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("UPDATE textydb.user SET givenName = '" + user.getFirstName() + "',"
					+ "familyName='" + user.getLastName()+ "' "
					+ "WHERE userid=" + user.getId());

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}

	public Vector<User> findAll() {
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<User> result = new Vector<User>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT userId, givenName, familyName, email, dateOfCreation FROM textydb.user");

			// Für jeden Eintrag wird nun ein User-Objekt erstellt.
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("userId"));
				user.setFirstName(rs.getString("givenName"));
				user.setLastName(rs.getString("familyName"));
				user.setEmail(rs.getString("email"));
				user.setDateOfCreation(rs.getTime("dateOfCreation"));

				// Hinzufügen des neuen Objekts zum Ergebnisvektor
				result.addElement(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ergebnisvektor zurückgeben
		return result;
	}

	public Vector<User> findById(int userId) {
		Connection con = DBConnection.connection();
		Vector<User> result = new Vector<User>();

		try {
			Statement stmt = con.createStatement();

		     ResultSet rs = stmt.executeQuery("SELECT userId, givenName, familyName, email, dateOfCreation FROM textydb.user " 
		    	      + "WHERE userId = '" + userId + "'");

			// Für jeden Eintrag wird nun ein User-Objekt erstellt.
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("userId"));
				user.setFirstName(rs.getString("givenName"));
				user.setLastName(rs.getString("familyName"));
				user.setEmail(rs.getString("email"));
				user.setDateOfCreation(rs.getTime("dateOfCreation"));

				result.addElement(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public User findByEmail(String email) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

		     ResultSet rs = stmt.executeQuery("SELECT userId, givenName, familyName, email, dateOfCreation FROM textydb.user " 
		    	      + "WHERE email = '" + email + "'");

			if (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("userId"));
				user.setFirstName(rs.getString("givenName"));
				user.setLastName(rs.getString("familyName"));
				user.setEmail(rs.getString("email"));
				user.setDateOfCreation(rs.getTime("dateOfCreation"));
				return user;
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
			return null;
		}

		return null;
	}

	public Vector<User> findByName(String firstName, String lastName) {
		Connection con = DBConnection.connection();
		Vector<User> result = new Vector<User>();

		try {
			Statement stmt = con.createStatement();

		     ResultSet rs = stmt.executeQuery("SELECT userId, givenName, familyName, email, dateOfCreation FROM textydb.user " 
		    	      + "WHERE givenName = '" + firstName + "'" + "AND familyName = '" + lastName + "'");

			// Für jeden Eintrag wird nun ein User-Objekt erstellt.
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("userId"));
				user.setFirstName(rs.getString("givenName"));
				user.setLastName(rs.getString("familyName"));
				user.setEmail(rs.getString("email"));
				user.setDateOfCreation(rs.getTime("dateOfCreation"));

				result.addElement(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
}
