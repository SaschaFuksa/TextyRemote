package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.LoginServiceAsync;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class Texty implements EntryPoint {

	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());
	private static LoginInfo loginInfo = null;

	public static LoginInfo getLoginInfo() {
		return loginInfo;
	}

	private Label footerLabel = new Label("About");
	private HorizontalPanel loginPanel = new HorizontalPanel();
	private Label nickname = new Label();
	private Anchor signOutLink = new Anchor("Sign Out");
	private TextyMenu menu = new TextyMenu();
	private HomeForm home = new HomeForm("Home");
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();
	private LoginServiceAsync loginService = ClientsideSettings
			.getLoginService();

	@Override
	public void onModuleLoad() {

		administration.checkUserData(checkUserDataExecute());
		loginService.login(GWT.getHostPageBaseURL(), loginExecute());

		menu.execute("Editor");

		footerLabel.addStyleName("Footer");

		RootPanel.get("Navigator").add(home);
		RootPanel.get("Footer").add(footerLabel);

		footerLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("Details").clear();
				RootPanel.get("Details").add(new Footer());
			}
		});

	}

	private AsyncCallback<Void> checkUserDataExecute() {
		AsyncCallback<Void> asynCallback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Void nothing) {

			}
		};
		return asynCallback;
	}

	private AsyncCallback<LoginInfo> loginExecute() {
		AsyncCallback<LoginInfo> asynCallback = new AsyncCallback<LoginInfo>() {
			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Error: " + caught.getMessage());
			}

			@Override
			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				createLoginPanel();
			}
		};
		return asynCallback;
	}

	private void createLoginPanel() {
		signOutLink.setHref(loginInfo.getLogoutUrl());
		nickname.setText(loginInfo.getEmailAddress());
		loginPanel.add(signOutLink);
		loginPanel.add(nickname);
		RootPanel.get("Banner").add(loginPanel);
	}
}
