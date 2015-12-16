package hdm.itprojekt.texty.server.db;

import java.sql.*;
import java.util.Vector;

import hdm.itprojekt.texty.shared.bo.*;

public class HashtagMapper {

	private static HashtagMapper hashtagMapper = null;

	protected HashtagMapper() {

	}

	public static HashtagMapper hashtagMapper() {
		if (hashtagMapper == null) {
			hashtagMapper = new HashtagMapper();
		}
		return hashtagMapper;
	}

	public Hashtag insert(Hashtag hashtag) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			// Find highest Primarykey
			ResultSet rs = stmt.executeQuery("SELECT MAX(hashtagId) AS maxid "
					+ "FROM textydb.hashtag ");

			if (rs.next()) {

				hashtag.setId(rs.getInt("maxid") + 1);

				stmt = con.createStatement();

				// Highest Primarykey has been found and set, now we insert it
				// into the DB
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
		return hashtag;
	}

	public void delete(Hashtag hashtag) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			// Hashtag gets deleted
			stmt.executeUpdate("DELETE FROM hashtag " + "WHERE hashtagId="
					+ hashtag.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	  public Vector<Hashtag> findAll() {
		    Connection con = DBConnection.connection();
		    // Ergebnisvektor vorbereiten
		    Vector<Hashtag> result = new Vector<Hashtag>();

		    try {
		      Statement stmt = con.createStatement();

		      //TODO: Date of Creation
		  	ResultSet rs = stmt.executeQuery("SELECT hashtagId, keyword FROM textydb.hashtag");


		      // Für jeden Eintrag wird nun ein Hashtag-Objekt erstellt.
		      while (rs.next()) {
		    	  Hashtag hashtag = new Hashtag();
		    	hashtag.setId(rs.getInt("hashtagId"));
		    	hashtag.setKeyword(rs.getString("keyword"));
		    	//hashtag.setDateOfCreation(rs.getDate("dateOfCreation"));

		        // Hinzufügen des neuen Objekts zum Ergebnisvektor
		        result.addElement(hashtag);
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }

		    // Ergebnisvektor zurückgeben
		    return result;
		  }
	  
	  public Vector<Hashtag> findById(int hashtagId) {
		    Connection con = DBConnection.connection();
		    Vector<Hashtag> result = new Vector<Hashtag>();

		    try {
		      Statement stmt = con.createStatement();

		      ResultSet rs = stmt.executeQuery("SELECT hashtagId, keyword, dateOfCreation"
		          + "FROM hashtag" + "WHERE hashtagId=" + hashtagId);

		      while (rs.next()) {
		    	  Hashtag hashtag = new Hashtag();
		    	hashtag.setId(rs.getInt("hashtagId"));
		    	hashtag.setKeyword(rs.getString("keyword"));
		    	hashtag.setDateOfCreation(rs.getDate("dateOfCreation"));

		        result.addElement(hashtag);
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		    
		    return result;
		  }
	  
	  public Vector<Hashtag> findByKeyword(String keyword) {
		    Connection con = DBConnection.connection();
		    Vector<Hashtag> result = new Vector<Hashtag>();

		    try {
		      Statement stmt = con.createStatement();

		      ResultSet rs = stmt.executeQuery("SELECT hashtagId, keyword, dateOfCreation"
		          + "FROM hashtag" + "WHERE keyword=" + keyword);

		      while (rs.next()) {
		    	  Hashtag hashtag = new Hashtag();
		    	hashtag.setId(rs.getInt("hashtagId"));
		    	hashtag.setKeyword(rs.getString("keyword"));
		    	hashtag.setDateOfCreation(rs.getDate("dateOfCreation"));

		        result.addElement(hashtag);
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		    
		    return result;
		  }
}

