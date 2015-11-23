package hdm.itprojekt.texty.server.db;

import java.sql.*;
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

	// anders als im Klassendiagramm werden wegen Einheitlichkeit nur Objekte
	// hineingegeben

	public Hashtag insert(Hashtag h) {
		Connection con = DBConnection.connection();
		// ...
		return h;
	}

	public Hashtag select(Hashtag h) {
		Connection con = DBConnection.connection();
		// ...
		return h;
	}

	public Hashtag update(Hashtag h) {
		Connection con = DBConnection.connection();
		// ...
		return h;
	}

	public void delete(Hashtag h) {
		Connection con = DBConnection.connection();
		// ...

	}

}
