package hdm.itprojekt.texty.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import com.google.appengine.api.utils.SystemProperty;

public class DBConnection {

	private static Connection con = null;

	private static String googleUrl = "jdbc:google:mysql://texty-project:textydb?user=root&password=root";
	private static String localUrl = "jdbc:mysql://173.194.107.165:3306/?user=root&password=root";

	public static Connection connection() {
		if (con == null) {
			String url = null;
			try {
				if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
					// Connecting from App Engine.
					// Load the class that provides the "jdbc:google:mysql://"
					// prefix.
					Class.forName("com.mysql.jdbc.GoogleDriver");
					url = googleUrl;
				} else {
					// Connecting from an external network.
					Class.forName("com.mysql.jdbc.Driver");
					url = localUrl;
				}

				con = DriverManager.getConnection(url);
			} catch (Exception e) {
				con = null;
				e.printStackTrace();
			}
		}

		// Connection gets sent back
		return con;
	}

}
