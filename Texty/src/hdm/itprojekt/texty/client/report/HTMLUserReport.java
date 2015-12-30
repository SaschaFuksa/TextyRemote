package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.shared.bo.User;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.i18n.client.DateTimeFormat;


public class HTMLUserReport {
	
	private static String generateReportHead() {
		return "<html><head><title></title></head><body>";
	}
	
	private static String generateReportEnd(String currentReport) {
		return currentReport + "</body></html>";
	}
	
	public static HTML generateUserSubscriptionReport(Vector<User> users) {
		String report = generateReportHead();
		
		report += "<div>";
		
		Date today = new Date();
	    // A custom date format
	    DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy.MM.dd HH:mm:ss");

		report += "Usersubscriptionreport generated at " + fmt.format(today) + "<br>";
		
		report += "<table><tr><th>Vorname</th><th>E-Mail</th></tr>";
		
		for(User user : users) {
			report += "<tr><td>" + user.getFirstName() + "</td><td>" + user.getEmail() + "</td></tr>";
		}
		
		report += "</table>";
		
		report += "</div>";
		
		report = generateReportEnd(report);
		return new HTML(report);
	}
}
