package hdm.itprojekt.texty.client;

public class HomeForm extends TextyForm {
	
	private ChatRoom chatRoom = new ChatRoom("Chatroom");
	//private ReloadForm reload = new ReloadForm();

	public HomeForm(String headline) {
		super(headline);
	}

	// Button bn1 = new Button("TEST");

	protected void run() {
		
		// RootPanel.get("Navigator").add(bn1);
		
		//Aufruf des Chatrooms für die Anzeige der abonnierten Hashtags und User
		chatRoom.run();
		
		//Aufruf des Refresh-Button im Footer-Bereich
		//reload.run();
		

	} 

} 

