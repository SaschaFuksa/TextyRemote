package hdm.itprojekt.texty.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import hdm.itprojekt.texty.shared.TextyAdministrationAsync;
import hdm.itprojekt.texty.shared.bo.Conversation;
import hdm.itprojekt.texty.shared.bo.Hashtag;
import hdm.itprojekt.texty.shared.bo.User;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class HomeForm extends TextyForm {

	private static Vector<User> userList = new Vector<User>();
	private static Vector<Hashtag> hashtagList = new Vector<Hashtag>();
	private static Vector<Conversation> conList = new Vector<Conversation>();
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label intro = new Label(
			"Here you can see and select your subscribed Hashtags and User");
	private VerticalPanel content = new VerticalPanel();
	private ScrollPanel scroll = new ScrollPanel(content);
	private Vector<String> userVector = new Vector<String>();
	private Vector<String> hashtagVector = new Vector<String>();
	private TextCell textCell = new TextCell();
	private CellList<String> cellListUser = new CellList<String>(textCell);
	private CellList<String> cellListHashtag = new CellList<String>(textCell);
	private TextyAdministrationAsync administration = ClientsideSettings
			.getTextyAdministration();

	public HomeForm(String headline) {
		super(headline);
	}

	@Override
	protected void run() {
		
		//Abonnierte User und deren Postings
		administration.getAllSubscribedUsers(new AsyncCallback<Vector<User>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Fail");
			}

			@Override
			public void onSuccess(Vector<User> result) {
				HomeForm.userList = result;
				showSubscribedUser();
			}

		});

		// Abonnierte Hashtags und deren Postings
		administration.getAllSubscribedHashtags(new AsyncCallback<Vector<Hashtag>>() {
					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Vector<Hashtag> result) {
						HomeForm.hashtagList = result;
						showSubscribedHashtag();
					}

				});

		mainPanel.add(getHeadline());
		mainPanel.add(intro);
		mainPanel.add(scroll);

		this.add(mainPanel);

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scroll.setHeight(mainPanel.getOffsetHeight() + "px");
			}
		});

	}// Ende Run-Methode

	private void showSubscribedUser() {
		
		for (User user : userList) {
			final User userView = user;
			userVector.add(userView.getFirstName()+ " " + userView.getLastName());
		}
		
		cellListUser.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		final SingleSelectionModel<String> selectionModelUser = new SingleSelectionModel<String>();
		cellListUser.setSelectionModel(selectionModelUser);
		selectionModelUser.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						String selected = selectionModelUser.getSelectedObject();
						if (selected != null) {
						}
					}
				});
		
		cellListUser.setRowCount(userVector.size(), true);
		cellListUser.setRowData(0, userVector);
		RootPanel.get("Navigator").add(cellListUser);
	}
	
	private void showSubscribedHashtag() {
		
		for (Hashtag hashtag : hashtagList) {
			final Hashtag hashtagView = hashtag;
			hashtagVector.add(hashtagView.getKeyword());
		}
		
		cellListHashtag.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		final SingleSelectionModel<String> selectionModelUser = new SingleSelectionModel<String>();
		cellListHashtag.setSelectionModel(selectionModelUser);
		selectionModelUser.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						String selected = selectionModelUser.getSelectedObject();
						if (selected != null) {
						}
					}
				});
		
		cellListHashtag.setRowCount(hashtagVector.size(), true);
		cellListHashtag.setRowData(0, hashtagVector);
		RootPanel.get("Navigator").add(cellListHashtag);
		
		}

	}


