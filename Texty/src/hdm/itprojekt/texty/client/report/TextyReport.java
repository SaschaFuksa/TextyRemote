package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.client.Footer;
import hdm.itprojekt.texty.client.LoginInfo;
import hdm.itprojekt.texty.client.TextyMenu;
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

/**
 * EntryPoint Klasse für den Report Client und Initialisierung des Menüs.
 */
public class TextyReport implements EntryPoint {

	/**
	 * Deklaration, Definition und Initialisierung der Widget.
	 */
	private VerticalPanel footerPanel = new VerticalPanel();
	private Label footerLabel = new Label("About");
	private LoginInfo loginInfo = null;
	private HorizontalPanel loginPanel = new HorizontalPanel();
	private Label nickname = new Label();
	private Anchor signOutLink = new Anchor("Sign Out");
	private TextyMenu reportMenu = new TextyMenu();

	/**
	 * Erzeugung des LoginPanels, welches das Logout ermöglichz und die E-Mail
	 * des eingeloggten User anzeigt.
	 */
	private void createLoginPanel() {
		
		/*
		 * Initialisierung der Variablen.
		 */
		signOutLink.setHref(loginInfo.getLogoutUrl());
		nickname.setText(loginInfo.getEmailAddress());
		
		/*
		 * Zuweisung der Child Widget zum Parent Widget.
		 */
		loginPanel.add(signOutLink);
		loginPanel.add(nickname);
		RootPanel.get("Banner").add(loginPanel);
	}


	@Override
	public void onModuleLoad() {
		
		/*
		 * Das Menü wird für den Client Report ausgeführt.
		 */
		reportMenu.execute("Report");

		
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					@Override
					public void onFailure(Throwable error) {
					}

					@Override
					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						createLoginPanel();
					}
				});
		
		
		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		footerLabel.addStyleName("Footer");
		
		/*
		 * Zuweisung der Child Widget zum Parent Widget.
		 */
		footerPanel.add(footerLabel);
		RootPanel.get("Footer").add(footerLabel);


		/*
		 * Zuweisung der Handler an das jeweilige Widget.
		 */
		footerLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("Details").clear();
				RootPanel.get("Details").add(new Footer());

			}
		});
	}

}
