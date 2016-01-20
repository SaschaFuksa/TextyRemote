package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.shared.bo.Message;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * In dieser Klasse wird der Report für die Nachrichten eines ausgewählten
 * Zeitraums in HTML aufgebaut. Die Ausgabe der Nachrichten erfolgt in einer
 * Tabelle. Die Tabelle besteht aus vier Spalten in denen der Author,die Empfänger, 
 * das Datum der Erstellung der Nachrichtd und die gesendete Nachricht ausgegeben
 * wird.
 *
 */

public class HTMLMessagesOfPeriodReport {

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
	public static HTML generateMessagesOfPeriodReport(Vector<Message> messages) {
		String report = generateReportHead();

		report += "<div>";

		// Hinzufügen des aktuellen Datums mit Uhrzeit für die Überschrift des
		// Reports.
		Date today = new Date();
		DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");

		report += "<br>";
		report += "Messagereport generated at " + fmt.format(today) + "<br>";
		report += "<br>";
		report += "<table id=\"reporttable\">" + "<tr>"
				+ "<th id=\"author\">Author</th>"
				+ "<th id=\"receiver\">Receivers</th>"
				+ "<th id=\"creationdate\">Date of creation</th>"
				+ "<th id=\"message\">Message</th>" + "</tr>";

		for (Message message : messages) {
			if (message.getListOfReceivers() == null
					|| message.getListOfReceivers().size() == 0
					|| message.getListOfReceivers().isEmpty()) {
				report += "<tr id=\"spalten\">"
						+ "<td id=\"zelle\">"+ message.getAuthor().toString()+ "</td>"
						+ "<td id=\"zelle\">"+ "Public message"+ "</td>"
						+ "<td id=\"zelle\">"+ DateTimeFormat.getFormat("dd.MM.yyyy 'at' HH:mm:ss")
								.format(message.getDateOfCreation()) + "</td>"
						+ "<td id=\"zelle\">" + message.getText() + "</td>"
						+ "</tr>";
			} else
				report += "<tr id=\"spalten\">"
						+ "<td id=\"zelle\">"
						+ message.getAuthor().toString()
						+ "</td>"
						+ "<td id=\"zelle\">"
						+ message.getListOfReceivers()
						+ "</td>"
						+ "<td id=\"zelle\">"
						+ DateTimeFormat.getFormat("dd.MM.yyyy 'at' HH:mm:ss")
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
