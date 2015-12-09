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
					+ "FROM hashtag ");

			if (rs.next()) {

				hashtag.setId(rs.getInt("maxid") + 1);

				stmt = con.createStatement();

				// Highest Primarykey has been found and set, now we insert it
				// into the DB
				stmt.executeUpdate("INSERT INTO hashtag (hashtagId, keyword)"
						+ "VALUES (" + hashtag.getId() + ", '" + hashtag.getKeyword()
						+ "')");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return hashtag;
	}

	public void delete(Hashtag hashtag) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			//Hashtag gets deleted 
			stmt.executeUpdate("DELETE FROM hashtag " + "WHERE hashtagId="
					+ hashtag.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
