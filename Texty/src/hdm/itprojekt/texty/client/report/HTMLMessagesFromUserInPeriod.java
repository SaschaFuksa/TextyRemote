package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.shared.bo.Message;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * In dieser Klasse wird der Report f�r die Nachrichten eines ausgew�hlten Users 
 * in einem ausgew�hlten Zeitraum in HTML aufgebaut. Die Ausgabe der Nachrichten
 *  erfolgt in einer Tabelle. Die Tabelle besteht aus drei Spalten in denen die Empf�nger, 
 *  die gesendete Nachricht und das Datum der Erstellung der Nachricht ausgegeben wird. 
 *
 */

public class HTMLMessagesFromUserInPeriod {
	
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

	public static HTML generateMessagesFromUserInPeriodReport(Vector<Message> messages) {
		String report = generateReportHead();
		
		report += "<div>";
		
		// Hinzuf�gen des aktuellen Datums mit Uhrzeit f�r die �berschrift des Reports.
		Date today = new Date();
	    DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");

	    report += "<br>";
		report += "Messagereport generated at " + fmt.format(today) + "<br>";
		report += "<br>";
		report += "<table id=\"reporttable\">"
				+ "<tr>"
					+ "<th id=\"receiver\">Receiver</th>"
					+ "<th id=\"creationdate\">Date of Creation</th>"
					+ "<th id=\"message\">Message</th>"
				+ "</tr>";
		
		for(Message message : messages) {
			report += "<tr id=\"spalten\">"
					+ "<td id=\"zelle\">" + message.getListOfReceivers()+ "</td>"
					+ "<td id=\"zelle\">" + DateTimeFormat.getFormat("dd.MM.yyyy 'at' HH:mm:ss").format(message.getDateOfCreation()) + "</td>"
					+ "<td id=\"zelle\">" + message.getText() + "</td>"
					+ "</tr>";
		}
		
		report += "</table>";
		
		report += "</div>";
		
		report = generateReportEnd(report);
		return new HTML(report);
	}

	}



