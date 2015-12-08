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

	public Hashtag insert(Hashtag h) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			// Find highest Primarykey
			ResultSet rs = stmt.executeQuery("SELECT MAX(hashtagId) AS maxid "
					+ "FROM hashtag ");

			if (rs.next()) {

				h.setId(rs.getInt("maxid") + 1);

				stmt = con.createStatement();

				// Highest Primarykey has been found and set, now we insert it
				// into the DB
				stmt.executeUpdate("INSERT INTO hashtag (hashtagId, keyword)"
						+ "VALUES (" + h.getId() + ", '" + h.getKeyword()
						+ "')");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return h;
	}

	public void delete(Hashtag h) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			//Hashtag gets deleted 
			stmt.executeUpdate("DELETE FROM hashtag " + "WHERE hashtagId="
					+ h.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
