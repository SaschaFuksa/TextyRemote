package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.shared.bo.Hashtag;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;

public class HTMLHashtagReport {

	private static String generateReportHead() {
		return "<html><head><title></title></head><body>";
	}
	
	private static String generateReportEnd(String currentReport) {
		return currentReport + "</body></html>";
	}
	
	public static HTML generateHashtagSubscriptionReport(Vector<Hashtag> hashtags) {
		String report = generateReportHead();
		
		report += "<div>";
		
		Date today = new Date();
	    // A custom date format
	    DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy.MM.dd HH:mm:ss");

		report += "Hashtagsubscriptionreport generated at " + fmt.format(today) + "<br>";
		
		report += "<table><tr><th>Hashtag</th></tr>";
		
		for(Hashtag hashtag : hashtags) {
			report += "<tr><td>"+ "#" + hashtag.getKeyword() + "</td></tr>";
		}
		
		report += "</table>";
		
		report += "</div>";
		
		report = generateReportEnd(report);
		return new HTML(report);
	}
	
}
