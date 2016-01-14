package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.shared.bo.Hashtag;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;

/**
 * In dieser Klasse wird der Report für die Hashtagabo`s eines ausgewählten Users in HTML 
 * aufgebaut. Die Ausgabe der abonnierten Hashtags erfolgt in einer Tabelle . 
 *
 */

public class HTMLHashtagSubscriptionReport{

	//Aufbau der Tabelle im HTML-Format
	private static String generateReportHead() {
		return "<html>"
				+ "<head>"
				+ "<title></title>"
				+ "</head>"
				+ "<body>";
	}
	
	private static String generateReportEnd(String currentReport) {
		return currentReport + "</body></html>";
	}
	
	public static HTML generateHashtagSubscriptionReport(Vector<Hashtag> hashtags) {
		String report = generateReportHead();
		
		report += "<div>";
		
		// Hinzufügen des aktuellen Datums mit Uhrzeit für die Überschrift des Reports.
		Date today = new Date();
	    DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");
	    
	    report += "<br>";
		report += "Hashtagsubscriptionreport generated at " + fmt.format(today) + "<br>";
		report += "<br>";
		report += "<table id=\"reporttable\">"
				+ "<tr>"
				+ "<th id=\"hashtag\">Hashtag</th>"
				+ "</tr>";
		
		for(Hashtag hashtag : hashtags) {
			report += "<tr id=\"spalten\">"
					+ "<td id=\"zelle\">"+ "#" + hashtag.getKeyword() + "</td>"
					+ "</tr>";
		}
		
		report += "</table>";
		
		report += "</div>";
		
		report = generateReportEnd(report);
		return new HTML(report);
	}
	
}
