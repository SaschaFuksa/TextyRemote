package hdm.itprojekt.texty.server.db;

import java.sql.*;

import hdm.itprojekt.texty.shared.bo.*;

public class ConversationMapper {

	private static ConversationMapper conversationMapper = null;

	protected ConversationMapper() {

	}

	public static ConversationMapper conversationMapper() {
		if (conversationMapper == null) {
			conversationMapper = new ConversationMapper();
		}
		return conversationMapper;
	}

	public Conversation insert(Conversation c) {
		Connection con = DBConnection.connection();
		// ...
		return c;
	}

	public Conversation select(Conversation c) {
		Connection con = DBConnection.connection();
		// ...
		return c;
	}

	public Conversation update(Conversation c) {
		Connection con = DBConnection.connection();
		// ...
		return c;
	}

	public void delete(Conversation c) {
		Connection con = DBConnection.connection();
		// ...

	}

}
