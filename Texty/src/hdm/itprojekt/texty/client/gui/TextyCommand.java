package hdm.itprojekt.texty.client.gui;

import hdm.itprojekt.texty.client.Showcase;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootPanel;

public class TextyCommand {
	
	public Command getCommand(Showcase showcase){
		final Showcase loadShowcase = showcase;
		Command cmd = new Command() {
			public void execute() {
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				loadShowcase.onLoad();
			}
		};
		return cmd;
	}
}
