package hdm.itprojekt.texty.server.report;

import hdm.itprojekt.texty.server.TextyAdministrationImpl;
import hdm.itprojekt.texty.shared.ReportGenerator;
import hdm.itprojekt.texty.shared.TextyAdministration;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ReportGeneratorImpl extends RemoteServiceServlet implements
		ReportGenerator {
	
	private TextyAdministration administration = null;
	
	public void init() throws IllegalArgumentException {
	    TextyAdministrationImpl a = new TextyAdministrationImpl();
	    a.init();
	    this.administration = a;
	  }

}
