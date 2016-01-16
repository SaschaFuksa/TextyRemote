package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.Message;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;

public class HTMLMessagesOfHashtagReport {
	
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

public static HTML generateMessagesOfHashtagReport(Vector<Message> messages) {
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
	
	for(Message message : messages) {
		report += "<tr id=\"spalten\">"
				+ "<td id=\"zelle\">"+ "#" + message.getText() + "</td>"
				+ "</tr>";
	}
	
	report += "</table>";
	
	report += "</div>";
	
	report = generateReportEnd(report);
	return new HTML(report);
}

}
