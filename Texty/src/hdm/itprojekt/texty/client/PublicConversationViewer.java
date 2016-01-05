package hdm.itprojekt.texty.client;

import hdm.itprojekt.texty.shared.bo.Conversation;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;

public class PublicConversationViewer extends TextyForm {
	
	private Vector<Conversation> conversationListOfUser = new Vector<Conversation>();
	
	public PublicConversationViewer(String headline, Vector<Conversation> conversationListofUser) {
		super(headline);
		this.conversationListOfUser = conversationListofUser;
	}

	@Override
	protected void run() {

		for (Conversation conversation : conversationListOfUser) {
			final Conversation conversationView = conversation;
			FlexTable chatFlexTable = new FlexTable();
			FocusPanel wrapper = new FocusPanel();
			//Vector<Message> messageList = new Vector<Message>();

			String date = DateTimeFormat
					.getFormat("yyyy-MM-dd").format(
							conversationView.getListOfMessage().get(0)
									.getDateOfCreation());
			Label dateLabel = new Label("sgnjlsngljksgnmljk");
			//messageList = conversationView.getListOfMessage();
			String text = conversationView.getListOfMessage().get(0)
					.getText();
			final Label textLabel = new Label("bsvdhkjbhjkgnvjkldsnlk");

			dateLabel.getElement().setId("conversationDate");
			// textLabel.getElement().setId("conversationBody");

			wrapper.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.alert("Success");
//					RootPanel.get("Info").clear();
//					RootPanel.get("Details").add(textLabel);
				}
			});

			// chatFlexTable.setWidget(0, 0, authorLabel);
			chatFlexTable.setWidget(0, 0, dateLabel);
			// chatFlexTable.setWidget(1, 0, receiverLabel);
			chatFlexTable.setWidget(1, 0, textLabel);

			wrapper.add(chatFlexTable);
			this.add(wrapper);
			//RootPanel.get("Details").add(contentConversation);
			
		} // Ende for-Schleife
		
	}
	
	
	
}
