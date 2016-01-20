package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.shared.bo.User;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;

/**
 * In dieser Klasse wird der Report für die Follower eines User`s in HTML 
 * aufgebaut. Die Ausgabe der abonnierten Hashtags erfolgt in einer Tabelle .
 * Die Tabelle besteht aus zwei Spalten für den Namen und die E-Mail Adresse des Followers. 
 *
 */
public class HTMLUserFollowerReport {
	
	//Aufbau der HTML Seite
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
	
	//Aufbau der Tabelle
	public static HTML generateUserFollowerReport(Vector<User> users, User selectedUser) {
		String report = generateReportHead();
		
		report += "<div>";
		
		// Hinzufügen des aktuellen Datums mit Uhrzeit für die Überschrift des Reports.
		Date today = new Date();
	    DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");

	    report += "<br>";
		report += "Followerreport of: "+ selectedUser +" generated at " + fmt.format(today) + "<br>";
		report += "<br>";
		report += "<table id=\"reporttable\">"
				+ "<tr>"
				+ "<th id=\"author\">Name</th>"
				+ "<th id=\"author\">E-Mail</th>"
				+ "</tr>";
		
		for(User user : users) {
			report += "<tr id=\"spalten\">"
					+ "<td id=\"zelle\">" + user.toString()+"</td>"
					+ "<td id=\"zelle\">" + user.getEmail() + "</td>"
					+ "</tr>";
		}
		
		report += "</table>";
		
		report += "</div>";
		
		report = generateReportEnd(report);
		return new HTML(report);
	}
}
