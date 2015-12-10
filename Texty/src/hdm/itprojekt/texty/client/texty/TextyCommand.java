package hdm.itprojekt.texty.client.texty;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class TextyCommand {
	
	public Command getCommand(TextyForm form) {
		final TextyForm textyForm = form;
		Command cmd = new Command() {
			public void execute() {
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				RootPanel.get("Navigator").add(textyForm);
			}
		};
		return cmd;
	}

	public Command getReportCommand() {
		Command cmdReport = new Command() {
			public void execute() {
				Window.Location
						.assign("http://127.0.0.1:8888/TextyReports.html");
			}
		};
		return cmdReport;
	}

	public Command getEditorCommand() {
		Command cmdEditor = new Command() {
			public void execute() {
				Window.Location.assign("http://127.0.0.1:8888/Texty.html");
			}
		};
		return cmdEditor;
	}
}
