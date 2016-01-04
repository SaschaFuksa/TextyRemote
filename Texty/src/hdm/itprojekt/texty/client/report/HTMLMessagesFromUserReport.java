package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;

public class HTMLMessagesFromUserReport {
	
	private static String generateReportHead() {
		return "<html><head><title></title></head><body>";
	}
	
	private static String generateReportEnd(String currentReport) {
		return currentReport + "</body></html>";
	}
	
	public static HTML generateMessagesOfUserReport(Vector<Message> messages) {
		String report = generateReportHead();
		
		report += "<div>";
		
		Date today = new Date();
	    // A custom date format
	    DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy.MM.dd HH:mm:ss");

		report += "Messagereport generated at " + fmt.format(today) + "<br>";
		
		report += "<table><tr><th>Message</th><th>Receivers</th></tr>";
		
		for(Message message : messages) {
			report += "<tr><td>" + message.getText() + "</td><td>" + message.getListOfReceivers() + "</td></tr>";
		}
		
		report += "</table>";
		
		report += "</div>";
		
		report = generateReportEnd(report);
		return new HTML(report);
	}
	
}
