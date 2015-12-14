package hdm.itprojekt.texty.server.db;

import java.sql.*;
import java.util.Vector;
import hdm.itprojekt.texty.shared.bo.*;

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

				//TODO: firstname lastname ändern
				
				// Highest Primarykey has been found and set, now we insert it
				// into the DB
				stmt.executeUpdate("INSERT INTO textydb.user (userId, givenName, email)"
						+ "VALUES ("
						+ user.getId()
						+ ", "
						+ user.getNickName()
						+ ", " 
						+ user.getEmail() + ")");
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

		      stmt.executeUpdate("UPDATE customers " + "SET givenName=\""
		          + user.getFirstName() + "\", " + "familyName=\"" + user.getLastName() + "\" "
		          + "WHERE id=" + user.getId());

		    }
		    catch (SQLException e) {
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

		      ResultSet rs = stmt.executeQuery("SELECT userId, givenName, familyName, dateOfCreation"
		          + "FROM user");

		      // Für jeden Eintrag wird nun ein User-Objekt erstellt.
		      while (rs.next()) {
		    	  User user = new User();
		    	user.setId(rs.getInt("userId"));
		    	user.setFirstName(rs.getString("givenName"));
		    	user.setLastName(rs.getString("familyName")); 
		    	user.setDateOfCreation(rs.getDate("dateOfCreation"));

		        // Hinzufügen des neuen Objekts zum Ergebnisvektor
		        result.addElement(user);
		      }
		    }
		    catch (SQLException e) {
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

		      ResultSet rs = stmt.executeQuery("SELECT userId, givenName, familyName, dateOfCreation"
		          + "FROM user" + "WHERE userId=" + userId);

		      // Für jeden Eintrag wird nun ein User-Objekt erstellt.
		      while (rs.next()) {
		    	  User user = new User();
		    	user.setId(rs.getInt("userId"));
		    	user.setFirstName(rs.getString("givenName"));
		    	user.setLastName(rs.getString("familyName")); 
		    	user.setDateOfCreation(rs.getDate("dateOfCreation"));

		        result.addElement(user);
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		    
		    return result;
		  }
	
	  public User findByEmail(String email) {
		  Connection con = DBConnection.connection();

		    try {
				Statement stmt = con.createStatement();

		      ResultSet rs = stmt.executeQuery("SELECT userId, givenName, familyName, dateOfCreation"
			          + "FROM user" + "WHERE email=" + email);

		      if (rs.next()) {
		        User user = new User();
		    	user.setId(rs.getInt("userId"));
		    	user.setFirstName(rs.getString("givenName"));
		    	user.setLastName(rs.getString("familyName")); 
		    	user.setDateOfCreation(rs.getDate("dateOfCreation"));
		        return user;
		      }
		    }
		    catch (SQLException e2) {
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

		      ResultSet rs = stmt.executeQuery("SELECT userId, givenName, familyName, dateOfCreation"
		          + "FROM user" + "WHERE givenName = " + firstName + " AND familyName = " + lastName +"");

		      // Für jeden Eintrag wird nun ein User-Objekt erstellt.
		      while (rs.next()) {
		    	  User user = new User();
		    	user.setId(rs.getInt("userId"));
		    	user.setFirstName(rs.getString("givenName"));
		    	user.setLastName(rs.getString("familyName")); 
		    	user.setDateOfCreation(rs.getDate("dateOfCreation"));

		        result.addElement(user);
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		    
		    return result;
		  }
	}
