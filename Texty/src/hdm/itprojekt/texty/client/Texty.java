package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.client.gui.CreateHashtagSubscription;
import hdm.itprojekt.texty.client.gui.CreateUserSubscription;
import hdm.itprojekt.texty.client.gui.Footer;
import hdm.itprojekt.texty.client.gui.NewConversation;
import hdm.itprojekt.texty.client.gui.ShowHashtagSubscription;
import hdm.itprojekt.texty.client.gui.ShowPrivateConversation;
import hdm.itprojekt.texty.client.gui.ShowPublicConversation;
import hdm.itprojekt.texty.client.gui.ShowUserSubscription;
import hdm.itprojekt.texty.client.gui.TextyCommand;
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

public class Texty implements EntryPoint {
	
	private VerticalPanel footerPanel = new VerticalPanel();
	private Label footerLabel = new Label("About");

	private LoginInfo loginInfo = null;
	private HorizontalPanel loginPanel = new HorizontalPanel();
	private Label nickname = new Label();
	private Anchor signOutLink = new Anchor("Sign Out");
	
	private TextyCommand command = new TextyCommand();

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

		Command cmdReport = new Command() {
			public void execute() {
				Window.Location
						.assign("http://127.0.0.1:8888/TextyReports.html");
			}
		};
		
		
		// Submenu fuer Conversation
		MenuBar subMenu1 = new MenuBar(true);
		subMenu1.addItem("Public Conversation", command.getCommand(new ShowPublicConversation()));
		subMenu1.addItem("Private Conversations", command.getCommand(new ShowPrivateConversation()));
		subMenu1.setStyleName("menubar");
		
		NewConversation newConv = new NewConversation();
		// Menu für Conversation
		MenuBar fooMenu = new MenuBar(true);
		fooMenu.addItem("New Conversation", command.getCommand(new NewConversation()));
		fooMenu.addItem("Show Conversations", subMenu1);
		fooMenu.setStyleName("menubar");

		
		// Submenu fuer Hashtagsubscription
		MenuBar subMenu2 = new MenuBar(true);
		subMenu2.addItem("Create",  command.getCommand(new CreateHashtagSubscription()));
		subMenu2.addItem("Show", command.getCommand(new ShowHashtagSubscription()));
		subMenu2.setStyleName("menubar");

		// Submenu fuer Usersubscription
		MenuBar subMenu3 = new MenuBar(true);
		subMenu3.addItem("Create", command.getCommand(new CreateUserSubscription()));
		subMenu3.addItem("Show", command.getCommand(new ShowUserSubscription()));
		subMenu3.setStyleName("menubar");

		// Menu für Subscription
		MenuBar barMenu = new MenuBar(true);
		barMenu.addItem("Hashtagsubscription", subMenu2);
		barMenu.addItem("Usersubscription", subMenu3);
		barMenu.setStyleName("menubar");

		// Komplette Menübar
		MenuBar menu = new MenuBar();
		menu.addItem("Conversation", fooMenu);
		menu.addItem("Subscription", barMenu);
		menu.addItem("ReportGenerator", cmdReport);

		menu.setStyleName("menubar");

		RootPanel.get("Menu").add(menu);
	}

	private void createLoginPanel() {
		signOutLink.setHref(loginInfo.getLogoutUrl());
		nickname.setText(loginInfo.getEmailAddress());
		loginPanel.add(signOutLink);
		loginPanel.add(nickname);
		RootPanel.get("Banner").add(loginPanel);
	}
}
