package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.shared.bo.Message;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;

public class HTMLMessagesOfPeriodReport {
	
	private static String generateReportHead() {
	return "<html><head><title></title></head><body>";
}

private static String generateReportEnd(String currentReport) {
	return currentReport + "</body></html>";
}

public static HTML generateMessagesOfPeriodReport(Vector<Message> messages) {
	String report = generateReportHead();
	
	report += "<div>";
	
	Date today = new Date();
    // A custom date format
    DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");

	report += "Messagereport generated at " + fmt.format(today) + "<br>";
	
	report += "<table><tr><th>Author</th><th>Message</th><th>Date of Creation</th></tr>";
	
	for(Message message : messages) {
		report += "<tr><td>" + message.getAuthor() + "</td><td>" + message.getText() + "</td>"
				+ "<td>" + DateTimeFormat.getFormat("dd.MM.yyyy").format(message.getDateOfCreation()) + "</td></tr>";
	}
	
	report += "</table>";
	
	report += "</div>";
	
	report = generateReportEnd(report);
	return new HTML(report);
}

}