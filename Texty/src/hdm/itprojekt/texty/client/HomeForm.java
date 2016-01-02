package hdm.itprojekt.texty.client;

import java.util.Vector;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.User;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HomeForm extends TextyForm {

	private static Vector<User> userConList = new Vector<User>();
	private static Vector<Hashtag> hashtagConList = new Vector<Hashtag>();
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label intro = new Label(
			"Here you can see and select your subscribed Hashtags and User");
	private FavoriteForm favoriteForm = new FavoriteForm("Chatroom");
	CellTreeForm treeForm = new CellTreeForm("Tree");
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public HomeForm(String headline) {
		super(headline);
	}

	@Override
	protected void run() {

		// Abonnierte User und deren Postings
		administration.getAllSubscribedUsers(new AsyncCallback<Vector<User>>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Vector<User> result) {
				HomeForm.userConList = result;
				showPublicConversation();

			}
		});

		// Abonnierte Hashtags und deren Postings
		administration.getAllSubscribedHashtags(new AsyncCallback<Vector<Hashtag>>() {
					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Vector<Hashtag> result) {
						HomeForm.hashtagConList = result;
						showHashtagSubscriptions();

					}

				});

		mainPanel.add(getHeadline());
		mainPanel.add(intro);

		treeForm.run();
		favoriteForm.run();

	}//Ende Run-Methode
	

	private void showHashtagSubscriptions() {
		for (User userView : userConList) {
			
		}

	}

	private void showPublicConversation() {
		for (Hashtag hashtagView : hashtagConList){
			
		}

	}

}
