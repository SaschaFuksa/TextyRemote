package hdm.itprojekt.texty.client;

public class HomeForm extends TextyForm {
	
	private FavoriteForm favoriteForm = new FavoriteForm("Chatroom");
	CellTreeForm treeForm = new CellTreeForm("Tree");

	public HomeForm(String headline) {
		super(headline);
	}

	protected void run() {
		
		treeForm.run();
		//Aufruf des Navigatorbereichs für die Anzeige der abonnierten Hashtags und User
		favoriteForm.run();
		
		
	} 

} 

