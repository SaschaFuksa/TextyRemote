package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * In dieser Klasse wird der Report für die Nachrichten eines ausgewählten Users
 * in einem ausgewählten Zeitraum in HTML aufgebaut. Die Ausgabe der Nachrichten
 * erfolgt in einer Tabelle. Die Tabelle besteht aus drei Spalten in denen die
 * Empfänger, die gesendete Nachricht und das Datum der Erstellung der Nachricht
 * ausgegeben wird.
 *
 */

public class HTMLMessagesOfUserInPeriod {

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
	public static HTML generateMessagesFromUserInPeriodReport(
			Vector<Message> messages, Date date1, Date date2, User selectedUser) {
		String report = generateReportHead();

		report += "<div>";

		// Hinzufügen des aktuellen Datums mit Uhrzeit für die Überschrift des
		// Reports.
		Date today = new Date();
		DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");

		report += "<br>";
		report += "Messagereport of " + selectedUser +" between "+ fmt.format(date1) +" and "+ fmt.format(date2) +" generated at " + fmt.format(today) + "<br>";
		report += "<br>";
		report += "<table id=\"reporttable\">" + 
				"<tr>"
				+ "<th id=\"receiver\">Receiver</th>"
				+ "<th id=\"creationdate\">Date of Creation</th>"
				+ "<th id=\"message\">Message</th>" + "</tr>";

		for (Message message : messages) {
			if (message.getListOfReceivers() == null
					|| message.getListOfReceivers().size() == 0
					|| message.getListOfReceivers().isEmpty()) {
				report += "<tr id=\"spalten\">"
						+ "<td id=\"zelle\">"+ "Public message"+ "</td>"
						+ "<td id=\"zelle\">"+ DateTimeFormat.getFormat("dd.MM.yyyy 'at' HH:mm:ss")
								.format(message.getDateOfCreation()) + "</td>"
						+ "<td id=\"zelle\">" + message.getText() + "</td>"
						+ "</tr>";
			} else
				report += "<tr id=\"spalten\">"
						+ "<td id=\"zelle\">"+ message.getListOfReceivers()+ "</td>"
						+ "<td id=\"zelle\">"+ DateTimeFormat.getFormat("dd.MM.yyyy 'at' HH:mm:ss")
								.format(message.getDateOfCreation()) + "</td>"
						+ "<td id=\"zelle\">" + message.getText() + "</td>"
						+ "</tr>";
		}

		report += "</table>";

		report += "</div>";

		report = generateReportEnd(report);
		return new HTML(report);
	}

}
