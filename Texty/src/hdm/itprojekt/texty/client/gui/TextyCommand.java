package hdm.itprojekt.texty.client.gui;

import hdm.itprojekt.texty.client.Showcase;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class TextyCommand {
	
	private Showcase showcase = null;
	
	public Command getCommand(Showcase showcase){
		this.showcase = showcase;
		final Showcase loadShowcase = this.showcase;
		Command cmd = new Command() {
			public void execute() {
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				loadShowcase.onLoad();
			}
		};
		return cmd;
	}
	
	public Command getReportCommand(){
		Command cmdReport = new Command() {
			public void execute() {
				Window.Location
						.assign("http://127.0.0.1:8888/TextyReports.html");
			}
		};
		return cmdReport;
	}
	
	public Command getEditorCommand(){
		Command cmdEditor = new Command() {
			public void execute() {
				Window.Location
						.assign("http://127.0.0.1:8888/Texty.html");
			}
		};
		return cmdEditor;
	}
}
