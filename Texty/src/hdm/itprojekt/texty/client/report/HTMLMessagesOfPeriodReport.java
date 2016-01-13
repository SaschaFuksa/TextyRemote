package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.shared.bo.Message;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * In dieser Klasse wird der Report für die Nachrichten eines ausgewählten Zeitraums in HTML 
 * aufgebaut. Die Ausgabe der Nachrichten erfolgt in einer Tabelle. Die Tabelle besteht aus drei Spalten in denen der Author, 
 * die gesendete Nachricht und das Datum der Erstellung der Nachricht ausgegeben wird. 
 *
 */

public class HTMLMessagesOfPeriodReport {
	
	//Aufbau der Tabelle im HTML-Format
	private static String generateReportHead() {
		return 	"<html>"
				+ "<head>"
				+ "<title></title>"
				+ "</head>"
				+ "<body>";
}

private static String generateReportEnd(String currentReport) {
	return currentReport + "</body></html>";
}

public static HTML generateMessagesOfPeriodReport(Vector<Message> messages) {
	String report = generateReportHead();
	
	report += "<div>";
	
	// Hinzufügen des aktuellen Datums mit Uhrzeit für die Überschrift des Reports.
	Date today = new Date();
    DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");

    report += "<br>";
	report += "Messagereport generated at " + fmt.format(today) + "<br>";
	report += "<br>";
	report += "<table id=\"reporttable\">"
			+ "<tr>"
				+ "<th id=\"spaltenueberschrift\">Author</th>"
				+ "<th id=\"spaltenueberschrift\">Message</th>"
				+ "<th id=\"spaltenueberschrift\">Date of Creation</th>"
			+ "</tr>";
	
	for(Message message : messages) {
		report += "<tr id=\"spalten\">"
				+ "<td id=\"zellen\">" + message.getAuthor() + "</td>"
				+ "<td id=\"zellen\">" + message.getText() + "</td>"
				+ "<td id=\"zellen\">" + DateTimeFormat.getFormat("dd.MM.yyyy 'at' HH:mm:ss").format(message.getDateOfCreation()) + "</td>"
				+ "</tr>";
	}
	
	report += "</table>";
	
	report += "</div>";
	
	report = generateReportEnd(report);
	return new HTML(report);
}

}
