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

/**
 * EntryPoint Klasse für den Editor Client. Initialisierung des Menüs und
 * Überprüfung mit der DB des eingeloggtern Users, ob dieser bereits in der DB
 * gespeichert ist.
 */
public class Texty implements EntryPoint {

	/**
	 * Der LOG gibt eine mögliche Exception bzw. den Erfolg des asynchronen
	 * Callbacks aus.
	 */
	private static final Logger LOG = Logger
			.getLogger(SingleConversationViewer.class.getSimpleName());

	/**
	 * Das loginInfo dient als Hilfklasse für das Login und stellt erforderliche
	 * Variablen und Operationen bereit.
	 */
	private static LoginInfo loginInfo = null;

	/**
	 * Deklaration, Definition und Initialisierung der Widget.
	 */
	private Label footerLabel = new Label("About");
	private HorizontalPanel loginPanel = new HorizontalPanel();
	private Label nickname = new Label();
	private Anchor signOutLink = new Anchor("Sign Out");
	private TextyMenu menu = new TextyMenu();
	private HomeForm home = new HomeForm("Home");

	/**
	 * Die administration und der loginService ermöglichen die asynchrone
	 * Kommunikation mit der Applikationslogik.
	 */
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();
	private LoginServiceAsync loginService = ClientsideSettings
			.getLoginService();

	/**
	 * Implementierung des Interfaces um der Klasse zu ermöglich, als EntryPoint
	 * des Modules zu laden.
	 */
	@Override
	public void onModuleLoad() {

		/*
		 * Überprüfung des User, ob dieser schon der DB bekannt ist.
		 */
		administration.checkUserData(checkUserDataExecute());

		/*
		 * Der Login wird ausgeführt und der User an die URL des Hosters
		 * weitergeleitet.
		 */
		loginService.login(GWT.getHostPageBaseURL(), loginExecute());

		/*
		 * Das Menü wird für den Client Editor ausgeführt.
		 */
		menu.execute("Editor");

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

		/*
		 * Zuweisung der Styles an das jeweilige Widget.
		 */
		footerLabel.addStyleName("Footer");

		/*
		 * Zuweisung des jeweiligen Child Widget zum Parent Widget.
		 */
		RootPanel.get("Navigator").add(home);
		RootPanel.get("Footer").add(footerLabel);

	}

	/**
	 * AsyncCallback für die Überprüfung der User Daten, ob diese bereits in der
	 * DB bekannt sind. Wenn nicht wird der neue User in der DB angelegt.
	 * 
	 * @return
	 */
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

	/**
	 * AsyncCallback für Login des User. Die Userdaten werden mithilfe des
	 * LoginInfo ausgegeben.
	 * 
	 * @return
	 */
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

	public static LoginInfo getLoginInfo() {
		return loginInfo;
	}
}
