package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.shared.bo.User;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * In dieser Klasse wird der Report f�r die Userabo`s eines ausgew�hlten Users in HTML 
 * aufgebaut. Die Ausgabe der abonnierten User erfolgt in einer Tabelle mit zwei Spalten, 
 * in denen zum einen der Vorname des abonnierten Users ausgegeben wird und zum anderen 
 * dessen E-Mailadresse.
 *
 */
public class HTMLUserSubscriptionReport {
	
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
	public static HTML generateUserSubscriptionReport(Vector<User> users, User selectedUser) {
		String report = generateReportHead();
		
		report += "<div>";
		
		// Hinzuf�gen des aktuellen Datums mit Uhrzeit f�r die �berschrift des Reports.
		Date today = new Date();
	    DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");

	    report += "<br>";
		report += "Usersubscriptions of: "+ selectedUser +" generated at " + fmt.format(today) + "<br>";
		report += "<br>";
		report += "<table id=\"reporttable\">"
				+ "<tr>"
				+ "<th id=\"author\">Name</th>"
				+ "<th id=\"author\">E-Mail</th>"
				+ "</tr>";
		
		for(User user : users) {
			report += "<tr id=\"spalten\">"
					+ "<td id=\"zelle\">" + user.getFirstName() + " " + ""+ user.getLastName() +"</td>"
					+ "<td id=\"zelle\">" + user.getEmail() + "</td>"
					+ "</tr>";
		}
		
		report += "</table>";
		
		report += "</div>";
		
		report = generateReportEnd(report);
		return new HTML(report);
	}
}
