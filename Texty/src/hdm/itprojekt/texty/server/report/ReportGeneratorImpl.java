package hdm.itprojekt.texty.server.report;

import java.util.Vector;

import hdm.itprojekt.texty.server.TextyAdministrationImpl;
import hdm.itprojekt.texty.shared.ReportGenerator;
import hdm.itprojekt.texty.shared.TextyAdministration;
import hdm.itprojekt.texty.shared.bo.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ReportGeneratorImpl extends RemoteServiceServlet implements
		ReportGenerator {
	
	private TextyAdministration administration = null;
	
	public void init() throws IllegalArgumentException {
	    TextyAdministrationImpl a = new TextyAdministrationImpl();
	    a.init();
	    this.administration = a;
	  }
	
//	public Vector<User> getAllSubscribedUsers(User user)
//			throws IllegalArgumentException {
//		
//		User subscriber = user;
//		return this.usMapper.selectAllSubscribedUsers(subscriber.getId());
//	}

}
