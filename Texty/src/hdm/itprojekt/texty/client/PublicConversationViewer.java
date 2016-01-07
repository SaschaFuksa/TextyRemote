package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Message;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PublicConversationViewer extends TextyForm {
	
	private Vector<Conversation> conversationListOfUser = new Vector<Conversation>();
	private VerticalPanel mainPanel = new VerticalPanel();
	
	public PublicConversationViewer(String headline, Vector<Conversation> conversationListofUser) {
		super(headline);
		this.conversationListOfUser = conversationListofUser;
	}

	@Override
	protected void run() {
		
		this.getElement().setId("fullSize");
		mainPanel.getElement().setId("fullWidth");
		
		mainPanel.add(getHeadline());
		
		addConversation();
		
		this.add(mainPanel);
		
	}
	
	private void addConversation(){
		for (Conversation conversation : conversationListOfUser) {
			final Conversation conversationView = conversation;
			final VerticalPanel conversationPanel = new VerticalPanel();
			FlexTable chatFlexTable = new FlexTable();
			FocusPanel wrapper = new FocusPanel();
			//Vector<Message> messageList = new Vector<Message>();

			String date = DateTimeFormat
					.getFormat("yyyy-MM-dd").format(
							conversationView.getListOfMessage().get(0)
									.getDateOfCreation());
			Label dateLabel = new Label(date);
			//messageList = conversationView.getListOfMessage();
			String text = conversationView.getListOfMessage().firstElement()
					.getText();
			final Label textLabel = new Label(text);

			conversationPanel.getElement().setId("conversation");

			wrapper.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.alert("Huhu");
					for (int i = 1; i < conversationView.getListOfMessage().size(); i++){
						
						VerticalPanel singleConversationPanel = new VerticalPanel();
						Label messageText = new Label(conversationView.getListOfMessage().get(i).getText());
						singleConversationPanel.add(messageText);
						conversationPanel.add(singleConversationPanel);
					}
//					RootPanel.get("Info").clear();
//					RootPanel.get("Details").add(textLabel);
				}
			});
			
			// chatFlexTable.setWidget(0, 0, authorLabel);
			chatFlexTable.setWidget(0, 0, dateLabel);
			// chatFlexTable.setWidget(1, 0, receiverLabel);
			chatFlexTable.setWidget(1, 0, textLabel);

			wrapper.add(chatFlexTable);
			conversationPanel.add(wrapper);
			mainPanel.add(conversationPanel);
			//RootPanel.get("Details").add(contentConversation);
			
		} // Ende for-Schleife
	}
	
	
	
}
