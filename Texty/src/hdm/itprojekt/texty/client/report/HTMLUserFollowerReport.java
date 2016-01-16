package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.shared.bo.User;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;

public class HTMLUserFollowerReport {
	
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
	
	public static HTML generateUserSubscriptionReport(Vector<User> users) {
		String report = generateReportHead();
		
		report += "<div>";
		
		// Hinzufügen des aktuellen Datums mit Uhrzeit für die Überschrift des Reports.
		Date today = new Date();
	    DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");

	    report += "<br>";
		report += "Usersubscriptionreport generated at " + fmt.format(today) + "<br>";
		report += "<br>";
		report += "<table id=\"reporttable\">"
				+ "<tr>"
				+ "<th id=\"author\">Name</th>"
				+ "<th id=\"author\">E-Mail</th>"
				+ "</tr>";
		
		for(User user : users) {
			report += "<tr id=\"spalten\">"
					+ "<td id=\"zelle\">" + user.getFirstName() + " " + ""+ user.getFirstName() +"</td>"
					+ "<td id=\"zelle\">" + user.getEmail() + "</td>"
					+ "</tr>";
		}
		
		report += "</table>";
		
		report += "</div>";
		
		report = generateReportEnd(report);
		return new HTML(report);
	}
}
