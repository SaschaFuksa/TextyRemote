package hdm.itprojekt.texty.server.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

import hdm.itprojekt.texty.shared.bo.*;

public class MessageMapper {

	private static MessageMapper messageMapper = null;

	protected MessageMapper() {

	}

	public static MessageMapper messageMapper() {
		if (messageMapper == null) {
			messageMapper = new MessageMapper();
		}
		return messageMapper;
	}

	public Message insert(Message m) {
		// ...
		return m;
	}

	public Message update(Message m) {
		// ...
		return m;
	}

	public void delete(Message m) {
		// ...
	}

	// result ist vom Typ ArrayList
	public Vector<Message> selectAllMessagesFromUser(User u) {
		Vector<Message> result = new Vector<Message>(); 
		// ...

		return result;
	}

	public ArrayList<Message> selectAllMessagesFromUserByDate(User u, Date d) {
		ArrayList result = new ArrayList();
		// ...

		return result;
	}

	public Vector<Message> selectAllMessages() {
		Vector<Message> result = new Vector<Message>();
		// ...

		return result;
	}

}
