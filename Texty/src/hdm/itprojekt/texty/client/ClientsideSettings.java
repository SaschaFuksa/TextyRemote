package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.CommonSettings;
import hdm.itprojekt.texty.shared.LoginService;
import hdm.itprojekt.texty.shared.LoginServiceAsync;
import hdm.itprojekt.texty.shared.TextyAdministration;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;

public class ClientsideSettings extends CommonSettings {

	private static TextyAdministrationAsync textyAdministration = null;
	private static LoginServiceAsync loginService = null;
	private static final String LOGGER_NAME = "Texty Web Client";
	private static final Logger log = Logger.getLogger(LOGGER_NAME);

	public static LoginServiceAsync getLoginService() {
		if (loginService == null) {
			loginService = GWT.create(LoginService.class);
		}
		return loginService;
	}

	public static Logger getLogger() {
		return log;
	}

	public static TextyAdministrationAsync getTextyAdministration() {
		if (textyAdministration == null) {
			textyAdministration = GWT.create(TextyAdministration.class);
		}
		return textyAdministration;
	}

}
