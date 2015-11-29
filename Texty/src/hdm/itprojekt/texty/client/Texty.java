package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.client.gui.CreateHashtagSubscription;
import hdm.itprojekt.texty.client.gui.CreateUserSubscription;
import hdm.itprojekt.texty.client.gui.Footer;
import hdm.itprojekt.texty.client.gui.NewConversation;
import hdm.itprojekt.texty.client.gui.ShowHashtagSubscription;
import hdm.itprojekt.texty.client.gui.ShowPrivateConversation;
import hdm.itprojekt.texty.client.gui.ShowPublicConversation;
import hdm.itprojekt.texty.client.gui.ShowUserSubscription;
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

		Command cmdCreateHashSub = new Command() {
			public void execute() {
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				// Erzeugen einer Instanz der Klasse CreateHashtagSubscription
				CreateHashtagSubscription createHashSub = new CreateHashtagSubscription();
				// Diese Instanz ruft nun ihre Methode onLoad auf
				createHashSub.onLoad();
			}
		};

		Command cmdShowHashSub = new Command() {
			public void execute() {
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				// Erzeugen einer Instanz der Klasse ShowHashtagSubscription
				ShowHashtagSubscription showHashSub = new ShowHashtagSubscription();
				// Diese Instanz ruft nun ihre Methode onLoad auf
				showHashSub.onLoad();
			}
		};

		Command cmdCreateUserSub = new Command() {
			public void execute() {
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				// Erzeugen einer Instanz der Klasse CreateUserSubscription
				CreateUserSubscription createUserSub = new CreateUserSubscription();
				// Diese Instanz ruft nun ihre Methode onLoad auf
				createUserSub.onLoad();
			}
		};

		Command cmdShowUserSub = new Command() {
			public void execute() {
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				// Erzeugen einer Instanz der Klasse ShowUserSubscription
				ShowUserSubscription showUserSub = new ShowUserSubscription();
				// Diese Instanz ruft nun ihre Methode onLoad auf
				showUserSub.onLoad();
			}
		};

		Command cmdShowPublicConv = new Command() {
			public void execute() {
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				// Erzeugen einer Instanz der Klasse ShowPublicConversation
				ShowPublicConversation showPublicConv = new ShowPublicConversation();
				// Diese Instanz ruft nun ihre Methode onLoad auf
				showPublicConv.onLoad();
			}
		};

		Command cmdShowPrivateConv = new Command() {
			public void execute() {
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				// Erzeugen einer Instanz der Klasse ShowPrivateConversation
				ShowPrivateConversation showPrivateConv = new ShowPrivateConversation();
				// Diese Instanz ruft nun ihre Methode onLoad auf
				showPrivateConv.onLoad();
			}
		};

		Command cmdNewConv = new Command() {
			public void execute() {
				RootPanel.get("Details").clear();
				RootPanel.get("Navigator").clear();
				// Erzeugen einer Instanz der Klasse NewConversation
				NewConversation newConv = new NewConversation();
				// Diese Instanz ruft nun ihre Methode onLoad auf
				newConv.onLoad();
			}
		};

		Command cmdReport = new Command() {
			public void execute() {
				Window.Location
						.assign("http://127.0.0.1:8888/TextyReports.html");
			}
		};

		// Submenu fuer Conversation
		MenuBar subMenu1 = new MenuBar(true);
		subMenu1.addItem("Public Conversation", cmdShowPublicConv);
		subMenu1.addItem("Private Conversations", cmdShowPrivateConv);
		subMenu1.setStyleName("menubar");

		// Menu für Conversation
		MenuBar fooMenu = new MenuBar(true);
		fooMenu.addItem("New Conversation", cmdNewConv);
		fooMenu.addItem("Show Conversations", subMenu1);
		fooMenu.setStyleName("menubar");

		// Submenu fuer Hashtagsubscription
		MenuBar subMenu2 = new MenuBar(true);
		subMenu2.addItem("Create", cmdCreateHashSub);
		subMenu2.addItem("Show", cmdShowHashSub);
		subMenu2.setStyleName("menubar");

		// Submenu fuer Usersubscription
		MenuBar subMenu3 = new MenuBar(true);
		subMenu3.addItem("Create", cmdCreateUserSub);
		subMenu3.addItem("Show", cmdShowUserSub);
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
