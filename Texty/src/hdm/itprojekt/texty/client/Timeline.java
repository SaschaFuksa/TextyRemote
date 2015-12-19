package hdm.itprojekt.texty.client;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import hdm.itprojekt.texty.shared.bo.Message;

public class Timeline extends TextyForm {

	private VerticalPanel detailsPanel = new VerticalPanel();
	private FlexTable publicChatFlexTable = new FlexTable();
	private Vector<String> messageList = new Vector<String>();

	// Aufbau Inhalt
	private VerticalPanel container = new VerticalPanel();
	private VerticalPanel container2 = new VerticalPanel();
	
	private HorizontalPanel userCarrier = new HorizontalPanel();
	private HorizontalPanel userCarrier2 = new HorizontalPanel();
	
	private HorizontalPanel timeCarrier = new HorizontalPanel();
	private HorizontalPanel timeCarrier2 = new HorizontalPanel();
	
	private HorizontalPanel messageCarrier = new HorizontalPanel();
	private HorizontalPanel messageCarrier2 = new HorizontalPanel();
	
	private Label userLabel = new Label();
	private Label userLabel2 = new Label();
	
	private Label timeLabel = new Label();
	private Label timeLabel2 = new Label();
	
	private Label messageLabel = new Label();
    private Label messageLabel2 = new Label();
    
   // private FavoriteForm form = new FavoriteForm("");
    
    private CellTableForm cellTable = new CellTableForm("");
    
	public Timeline(String headline) {
		super(headline);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void run() {
		
		cellTable.run();
		
		/*//Post 1
		timeLabel.setText("13:15");
		userLabel.setText("Autor");
		messageLabel.setText("Ich mag #Pizza");
		
		messageLabel.addDoubleClickHandler(new DoubleClickHandler(){
			public void onDoubleClick(DoubleClickEvent event) {
				messageLabel.setText("Edited");
			}
		});
		
		
		Anchor anchor = new Anchor("Click me!"); // At this point clicking it won't do a thing
		anchor.addClickHandler(new ClickHandler() {
		    @Override
		    public void onClick (ClickEvent event){
		        Window.open("http://www.example.com/", "_blank", ""); // Or open a PopupPanel
		                                                              // or sth similar
		    }
		});
		
		Message m1 = new Message("#Hallo");
		String s = m1.getText();
		
		if (	s.startsWith("#")){
			Window.alert("HU");
		}
				
		timeCarrier.add(timeLabel);
		userCarrier.add(userLabel);
		messageCarrier.add(messageLabel);
		container.add(timeCarrier);
		container.add(userCarrier);
		container.add(messageCarrier);
		
		//Post 2
		timeLabel2.setText("13:17");
		userLabel2.setText("Autor");
		messageLabel2.setText("Ich mag doch keine #Pizza");
		timeCarrier2.add(timeLabel2);
		userCarrier2.add(userLabel2);
		messageCarrier2.add(messageLabel2);
		container2.add(timeCarrier2);
		container2.add(userCarrier2);
		container2.add(messageCarrier2);
		
		publicChatFlexTable.setText(0, 0, "public Post");
		publicChatFlexTable.setText(0, 1, "You");
		publicChatFlexTable.setText(0, 2, "Test");
		
		int row = publicChatFlexTable.getRowCount();
		publicChatFlexTable.setWidget(1, 0, container);
		publicChatFlexTable.setWidget(2, 0, container2);
		
		publicChatFlexTable.setStylePrimaryName("flexTable");
		publicChatFlexTable.setWidth("32em");
		publicChatFlexTable.setCellSpacing(7);
		publicChatFlexTable.setCellPadding(5);

		detailsPanel.add(publicChatFlexTable);
		detailsPanel.add(anchor);
		
		//int row = publicChatFlexTable.getRowCount();
		publicChatFlexTable.setWidget(row, 0, container);
		publicChatFlexTable.setWidget(row, 1, container);
		
		detailsPanel.add(publicChatFlexTable);
		
		RootPanel.get("Details").add(detailsPanel);

		
		// chatFlexTable.setWidget(row, 2, deleteMessageButton);
		// chatFlexTable.setWidget(row, 3, editMessageButton)
		// chatFlexTable.setText(row, 1, textFromTextbox);
		
		// Diese Messages symbolisieren bereits abonnierte Messages. Diese //
		// werden auf der Startseite Home angezeigt Message m1 = new
		//Message m1 = new Message("Hallo");
		Message m2 = new Message("Hey, wie gehts dir?");
		Message m3 = new Message("Habe Hunger, #Pizza verbrannt");
		Message m4 = new Message("Oh nein :(");
		Message m5 = new Message("Naja wird schon");
		Message m6 = new Message(
				"Okay, ich muss los, #VfB spielt gleich #naechsteNiederlage");*/

	}
}
