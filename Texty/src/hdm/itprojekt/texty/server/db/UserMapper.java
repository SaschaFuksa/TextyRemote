package hdm.itprojekt.texty.server.db;

import hdm.itprojekt.texty.shared.bo.*;

import java.sql.*;

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

	public User insert(User u) {
		return u;
	}

	public User update(User u) {
		return u;
	}

}
