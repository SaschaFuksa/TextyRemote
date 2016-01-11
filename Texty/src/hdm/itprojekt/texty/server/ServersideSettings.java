package hdm.itprojekt.texty.server;

import hdm.itprojekt.texty.shared.CommonSettings;

import java.util.logging.Logger;

/**
 * 
 * Klasse mit Eigenschaften und Diensten, die f�r alle Server-seitigen Klassen
 * relevant sind.
 * 
 * In ihrem aktuellen Entwicklungsstand bietet die Klasse eine rudiment�re
 * Unterst�tzung der Logging-Funkionalit�t unter Java. Es wird ein
 * applikationszentraler Logger realisiert, der mittels
 * ServerSideSettings.getLogger()genutzt werden kann.
 * 
 */
public class ServersideSettings extends CommonSettings {
	private static final String LOGGER_NAME = "Texty Server";
	private static final Logger log = Logger.getLogger(LOGGER_NAME);

	public static Logger getLogger() {
		return log;
	}

}
