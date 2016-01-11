package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.CommonSettings;
import hdm.itprojekt.texty.shared.LoginService;
import hdm.itprojekt.texty.shared.LoginServiceAsync;
import hdm.itprojekt.texty.shared.TextyAdministration;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;

/**
 * Klasse mit Eigenschaften und Diensten, die für alle Client-seitigen Klassen
 * relevant sind.
 * 
 */
public class ClientsideSettings extends CommonSettings {

	/**
	 * Remote Service Proxy zur Verbindungsaufnahme mit dem Server-seitgen
	 * Dienst namens TextyAdministration.
	 */
	private static TextyAdministrationAsync textyAdministration = null;
	/**
	 * Remote Service Proxy zur Verbindungsaufnahme mit dem Server-seitgen
	 * Dienst namens LoginService.
	 */
	private static LoginServiceAsync loginService = null;
	/**
	 * Name des Client-seitigen Loggers.
	 */
	private static final String LOGGER_NAME = "Texty Web Client";
	/**
	 * Instanz des Client-seitigen Loggers.
	 */
	private static final Logger log = Logger.getLogger(LOGGER_NAME);

	/**
	 * Anlegen und Auslesen der applikationsweit eindeutigen
	 * LoginService. Diese Methode erstellt den LoginService,
	 * sofern sie noch nicht existiert. Bei wiederholtem Aufruf dieser Methode
	 * wird stets das bereits zuvor angelegte Objekt zurückgegeben.
	 * 
	 * @return eindeutige Instanz des Typs {@link LoginServiceAsync}
	 */
	public static LoginServiceAsync getLoginService() {
		if (loginService == null) {
			loginService = GWT.create(LoginService.class);
		}
		return loginService;
	}

	/**
	 * Auslesen des applikationsweiten (Client-seitig!) zentralen Loggers.
	 */
	public static Logger getLogger() {
		return log;
	}

	/**
	 * Anlegen und Auslesen der applikationsweit eindeutigen
	 * TextyAdministration. Diese Methode erstellt die Textyadminiestration,
	 * sofern sie noch nicht existiert. Bei wiederholtem Aufruf dieser Methode
	 * wird stets das bereits zuvor angelegte Objekt zurückgegeben.
	 * 
	 * @return eindeutige Instanz des Typs {@link TextyAdministrationAsync}
	 */
	public static TextyAdministrationAsync getTextyAdministration() {
		if (textyAdministration == null) {
			textyAdministration = GWT.create(TextyAdministration.class);
		}
		return textyAdministration;
	}

}
