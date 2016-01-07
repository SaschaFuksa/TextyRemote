package hdm.itprojekt.texty.client.report;

import hdm.itprojekt.texty.client.ClientsideSettings;
import hdm.itprojekt.texty.client.TextyForm;
import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Message;
import hdm.itprojekt.texty.shared.bo.User;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MessagesOfUser extends TextyForm {

	public static User user;
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable chatFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox suggestBox = new SuggestBox(oracle);
	private Button MessageReport;
	private final TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();
	private static Vector<User> allUser = new Vector<User>();

	public MessagesOfUser(String headline) {
		super(headline);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		
		// Create UI
		
		//Hinzufügen des Buttons zum auslösen des MessagesOfUser-Report 
		MessageReport = new Button("Show Messages", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String nickName = getNickName(suggestBox.getText());
				User user = getUserOutOfAllUser(nickName);
				
				// 
				administration.getAllMessagesWhereUserIsAuthor(user, new AsyncCallback<Vector<Message>>() {
					@Override
					public void onFailure(Throwable caught) {

					}
					@Override
					public void onSuccess(Vector<Message> result) {
						RootPanel.get("Details").clear();
						RootPanel.get("Details").add(HTMLMessagesFromUserReport.generateMessagesOfUserReport(result));
					}
				});
			};
		});

		//Anlegen einer chatFlexTable zum Anordnen der verschiedenen Widgets im Navigatorbereich
		// Text
		chatFlexTable.setText(0, 0, "Messagereport for:");

		// Textboxen
		chatFlexTable.setWidget(0, 1, suggestBox);

		// Save-Button
		chatFlexTable.setWidget(3, 1, MessageReport);
		
		
		// Hinzufügen der widgets
		mainPanel.add(chatFlexTable);
		mainPanel.add(addPanel);
		RootPanel.get("Navigator").clear();
		RootPanel.get("Navigator").add(mainPanel);
		
		
		//Auswählen eines registrierten Users aus/in der SuggestBox
		administration.getAllUsers(new AsyncCallback<Vector<User>>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Vector<User> result) {
				MessagesOfUser.allUser = result;
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
