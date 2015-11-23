package hdm.itprojekt.texty.server.db;

import java.sql.*;
import com.google.appengine.api.rdbms.AppEngineDriver;

public class DBConnection {

	private static Connection con = null;

	private static String url = "";

	public static Connection connection() {
		if (con == null) {
			try {
				DriverManager.registerDriver(new AppEngineDriver());

				con = DriverManager.getConnection(url);
			} catch (SQLException e1) {
				con = null;
				e1.printStackTrace();
			}
		}
		return con;
	}
}
