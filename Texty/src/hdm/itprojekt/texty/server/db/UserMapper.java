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
}