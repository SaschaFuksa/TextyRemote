package hdm.itprojekt.texty.client.report;

import java.util.Vector;

import hdm.itprojekt.texty.client.ClientsideSettings;
import hdm.itprojekt.texty.client.InfoBox;
import hdm.itprojekt.texty.client.TextyForm;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.User;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * In dieser Klasse wird der Report für die Userabo`s eines ausgewählten Users in HTML 
 * aufgebaut. Die Ausgabe der abonnierten User erfolgt in einer Tabelle mit zwei Spalten, 
 * in denen zum einen der Vorname des abonnierten Users ausgegeben wird und zum anderen 
 * dessen E-Mailadresse.
 *
 */

public class SubscriptionReport extends TextyForm {

	public static User user;
	private ScrollPanel scrollPanel = new ScrollPanel(); 
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private Button Usersubscriptions;
	private Button Hashtagsubscriptions ;
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();
	private static Vector<User> allUser = new Vector<User>();
	private InfoBox infoBox = new InfoBox();

	public SubscriptionReport(String headline) {
		super(headline);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		
		// Create UI
		
		//Hinzufügen des Usersubscription-Buttons zum auslösen des Userabo-Reports 
		Usersubscriptions = new Button("Usersubscriptions", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scrollPanel.clear();
				infoBox.clear();
				if(suggestBox.getText() == ""){
					infoBox.setWarningText("Please select a user!");
				}else{	
				String nickName = getNickName(suggestBox.getText());
				User user = getUserOutOfAllUser(nickName);
				
				if (user == null){
					infoBox.setErrorText("Unknown User!");
				}else{
				
				// 
				administration.getAllSubscribedUsersFromUser(user, new AsyncCallback<Vector<User>>() {
					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Vector<User> result) {
						scrollPanel.setSize("100%", "100%");
						RootPanel.get("Details").add(scrollPanel);
						scrollPanel.add(HTMLUserSubscriptionReport.generateUserSubscriptionReport(result));
					}
				});
				}
				}
			};
		});
		
		//Hinzufügen des Hashtagsubscription-Buttons zum auslösen des Hashtagabo-Reports
		Hashtagsubscriptions = new Button("Hashtagsubscriptions", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				infoBox.clear();
				scrollPanel.clear();
				if(suggestBox.getText() == ""){
					infoBox.setWarningText("Please select a user!");
				}else{	
					String nickName = getNickName(suggestBox.getText());
					User user = getUserOutOfAllUser(nickName);
				
				if (user == null){
					infoBox.setErrorText("Unknown User!");
				}else{
				// 
				administration.getAllSubscribedHashtagsFromUser(user, new AsyncCallback<Vector<Hashtag>>() {
					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Vector<Hashtag> result) {
						RootPanel.get("Details").clear();
						scrollPanel.setSize("100%", "100%");
						RootPanel.get("Details").add(scrollPanel);
						scrollPanel.add(HTMLHashtagSubscriptionReport.generateHashtagSubscriptionReport(result));
					}
				});
				}
				}
			};
		});
		
		//Anlegen einer chatFlexTable zum Anordnen der verschiedenen Widgets im Navigatorbereich
		// Text
		chatFlexTable.setText(0, 0, "Subscription of:");

		// Textboxen
		chatFlexTable.setWidget(0, 1, suggestBox);

		// Save-Button
		chatFlexTable.setWidget(3, 1, Usersubscriptions);
		chatFlexTable.setWidget(4, 1, Hashtagsubscriptions);
		
		//Hinzufügen der infoBox für Fehlermeldungen
		chatFlexTable.setWidget(5, 1, infoBox);
		
		// Hinzufügen der widgets
		mainPanel.add(chatFlexTable);
		mainPanel.add(addPanel);
		RootPanel.get("Navigator").clear();
		RootPanel.get("Navigator").add(mainPanel);
		
		
	/*
	 * Auslesen & Hinzufügen eines registrierten Users aus/in der SuggestBox
	 */
		
		administration.getAllUsers(new AsyncCallback<Vector<User>>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Vector<User> result) {
				SubscriptionReport.allUser = result;
				setOracle();
			}
		});
	}
	
	private void setOracle() {
		oracle.clear();
		for (int i = 0; i < allUser.size(); i++) {
			oracle.add(getOracleName(allUser.get(i).getFirstName(), allUser
					.get(i).getEmail()));
		}
	}
	
	private String getOracleName(String firstName, String email) {
		StringBuffer bufferName = new StringBuffer(email);
		bufferName.setLength(bufferName.indexOf("@"));
		String nickName = bufferName.toString();
		String name = new String(firstName + " (" + nickName + ")");
		return name;

	}
	
	private String getNickName(String text) {
		StringBuffer bufferName = new StringBuffer(text);
		int firstIndex = bufferName.indexOf("(");
		bufferName.delete(0, firstIndex + 1);
		bufferName.deleteCharAt(bufferName.length() - 1);
		String nickName = bufferName.toString();
		return nickName;
	}

	public User getUserOutOfAllUser(String nickName) {
		for (int i = 0; i < allUser.size(); i++) {
			if (nickName.equals(setNickName(allUser.get(i).getEmail()))) {
				User user = allUser.get(i);
				return user;
			}
		}
		return null;
	}
	
	private String setNickName(String email) {
		StringBuffer bufferName = new StringBuffer(email);
		bufferName.setLength(bufferName.indexOf("@"));
		String nickName = bufferName.toString();
		return nickName;
	}
}
