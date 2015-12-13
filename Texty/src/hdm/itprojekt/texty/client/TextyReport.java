package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.LoginService;
import hdm.itprojekt.texty.shared.LoginServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TextyReport implements EntryPoint {
	
	private VerticalPanel footerPanel = new VerticalPanel();
	private Label footerLabel = new Label("About");

	private LoginInfo loginInfo = null;
	private HorizontalPanel loginPanel = new HorizontalPanel();
	private Label nickname = new Label();
	private Anchor signOutLink = new Anchor("Sign Out");
	
	public void onModuleLoad() {
		
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						createLoginPanel();
					}
				});

		footerLabel.addStyleName("Footer");
		footerPanel.add(footerLabel);
		RootPanel.get("Footer").add(footerLabel);

		footerLabel.addStyleName("Footer");
		footerPanel.add(footerLabel);
		RootPanel.get("Footer").add(footerLabel);

		footerLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				RootPanel.get("Details").clear();
				RootPanel.get("Details").add(new Footer());

			}
		});
		
		Command cmd = new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		};

		Command cmdEditor = new Command() {
			public void execute() {
				Window.Location.assign("http://127.0.0.1:8888/Texty.html");
			}
		};

		// Make some sub-menus that we will cascade from the top menu.
		MenuBar fooMenu = new MenuBar(true);
		fooMenu.addItem("HashtagReport", cmd);
		fooMenu.addItem("Back", cmdEditor);

		// Make a new menu bar, adding a few cascading menus to it.
		MenuBar menuReport = new MenuBar();
		menuReport.addItem("Report", fooMenu);
		menuReport.setStyleName("menubar");

		// Add it to the root panel.
		RootPanel.get("Menu").add(menuReport);

	}
	
	private void createLoginPanel() {
		signOutLink.setHref(loginInfo.getLogoutUrl());
		nickname.setText(loginInfo.getEmailAddress());
		loginPanel.add(signOutLink);
		loginPanel.add(nickname);
		RootPanel.get("Banner").add(loginPanel);
	}

}