package hdm.itprojekt.texty.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import hdm.itprojekt.texty.shared.CommonSettings;
import hdm.itprojekt.texty.shared.LoginService;
import hdm.itprojekt.texty.shared.LoginServiceAsync;
import hdm.itprojekt.texty.shared.ReportGenerator;
import hdm.itprojekt.texty.shared.ReportGeneratorAsync;
import hdm.itprojekt.texty.shared.TextyAdministration;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;

public class ClientsideSettings extends CommonSettings {

	private static TextyAdministrationAsync textyAdministration = null;
	private static ReportGeneratorAsync reportGenerator = null;
	private static LoginServiceAsync loginService = null;
	private static final String LOGGER_NAME = "Texty Web Client";
	private static final Logger log = Logger.getLogger(LOGGER_NAME);

	public static Logger getLogger() {
		return log;
	}

	public static TextyAdministrationAsync getTextyAdministration() {
		if (textyAdministration == null) {
			textyAdministration = GWT.create(TextyAdministration.class);
		}
		return textyAdministration;
	}

	public static ReportGeneratorAsync getReportGenerator() {
		if (reportGenerator == null) {
			reportGenerator = GWT.create(ReportGenerator.class);

			final AsyncCallback<Void> initReportGeneratorCallback = new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					ClientsideSettings
							.getLogger()
							.severe("Der ReportGenerator konnte nicht initialisiert werden!");
				}

				@Override
				public void onSuccess(Void result) {
					ClientsideSettings.getLogger().info(
							"Der ReportGenerator wurde initialisiert.");
				}
			};

			reportGenerator.init(initReportGeneratorCallback);
		}
		return reportGenerator;
	}

	public static LoginServiceAsync getBankVerwaltung() {
		if (loginService == null) {
			loginService = GWT.create(LoginService.class);
		}
		return loginService;
	}

}
