package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.client.gui.Footer;
import hdm.itprojekt.texty.client.gui.TextyMenu;
import hdm.itprojekt.texty.shared.LoginService;
import hdm.itprojekt.texty.shared.LoginServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Texty implements EntryPoint {

	private VerticalPanel footerPanel = new VerticalPanel();
	private Label footerLabel = new Label("About");
	private LoginInfo loginInfo = null;
	private HorizontalPanel loginPanel = new HorizontalPanel();
	private Label nickname = new Label();
	private Anchor signOutLink = new Anchor("Sign Out");
	private TextyMenu menu = new TextyMenu();

	public void onModuleLoad() {

		menu.execute();

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

	}

	private void createLoginPanel() {
		signOutLink.setHref(loginInfo.getLogoutUrl());
		nickname.setText(loginInfo.getEmailAddress());
		loginPanel.add(signOutLink);
		loginPanel.add(nickname);
		RootPanel.get("Banner").add(loginPanel);
	}
}
