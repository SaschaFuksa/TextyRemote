package hdm.itprojekt.texty.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import hdm.itprojekt.texty.shared.TextyAdministration;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.*;
import hdm.itprojekt.texty.server.db.*;

public class TextyAdministrationImpl extends RemoteServiceServlet implements
		TextyAdministration {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConversationMapper cMapper = null;
	private HashtagMapper hMapper = null;
	private HashtagSubscriptionMapper hsMapper = null;
	private MessageMapper mMapper = null;
	private UserMapper uMapper = null;
	private UserSubscriptionMapper usMapper = null;

	@Override
	public void init() throws IllegalArgumentException {
		this.cMapper = ConversationMapper.conversationMapper();
		this.hMapper = HashtagMapper.hashtagMapper();
		this.hsMapper = HashtagSubscriptionMapper.hashtagSubscriptionMapper();
		this.mMapper = MessageMapper.messageMapper();
		this.uMapper = UserMapper.userMapper();
		this.usMapper = UserSubscriptionMapper.userSubscriptionMapper();

	}

	@Override
	public Message createMessage(String text, User author, Boolean visible)
			throws IllegalArgumentException {
		Message m = new Message();
		m.setText(text);
		m.setAuthor(author);
		m.setVisible(visible);

		/*
		 * Setzen einer vorläufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist. TODO Mit
		 * David besprechen
		 */
		m.setId(1);

		return this.mMapper.insert(m);
	}

	@Override
	public Hashtag createHashtag(String keyword) {
		Hashtag h = new Hashtag();
		h.setKeyword(keyword);

		/*
		 * Setzen einer vorläufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist. TODO Mit
		 * David besprechen
		 */
		h.setId(1);
		return this.hMapper.insert(h);

	}

}
