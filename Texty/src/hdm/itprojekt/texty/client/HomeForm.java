package hdm.itprojekt.texty.client;

public class HomeForm extends TextyForm {
	
	private FavoriteForm favoriteForm = new FavoriteForm("Chatroom");

	public HomeForm(String headline) {
		super(headline);
	}

	protected void run() {
		
		//Aufruf des Navigatorbereichs f�r die Anzeige der abonnierten Hashtags und User
		favoriteForm.run();
		
	} 

} 

