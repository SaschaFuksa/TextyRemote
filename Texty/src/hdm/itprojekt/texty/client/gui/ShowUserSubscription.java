package hdm.itprojekt.texty.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

import hdm.itprojekt.texty.shared.bo.User;

import java.awt.TextField;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.Vector;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ShowUserSubscription extends VerticalPanel{
		
	
		// Wird im Details-Bereich realisiert
		private VerticalPanel messagePanel = new VerticalPanel();
		private HorizontalPanel buttonPanel = new HorizontalPanel();
		// Wird im Navigator-Bereich realisiert
		private VerticalPanel navigation = new VerticalPanel();
		private HorizontalPanel addPanel = new HorizontalPanel();

		private TextBox userBox = new TextBox();
		private TextArea messageBox = new TextArea();
		
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		SuggestBox suggestBox = new SuggestBox(oracle);
		
		private Button addButton = new Button();
		
	public void onLoad(){
	
				// Größe der TextArea angeben
				messageBox.setCharacterWidth(80);
				messageBox.setVisibleLines(15);
				
				//Alternativer Delete-Button, der das User-Abonnement kündigt
				final Button deleteButton= new Button("Delete Usersubscription");
				
				deleteButton.addClickHandler(new ClickHandler() {
				      public void onClick(ClickEvent event) {
				    	//Weitere Implementierung erforderlich
				        Window.alert("Usersubscription deleted!");
				      }
				    });
				
				// Example Users
				User user1 = new User("Sasa", "sasa@fufu.de");
				User user2 = new User("Daniel", "dada@sese.de");
				User user3 = new User("David", "dada@hehe.de");
				User user4 = new User("Matteo", "mama@brbr.de");
				User user5 = new User("Erich", "erer@meme.de");
				User user6 = new User("Fred", "fredchen@schnuschnu.de");
				
				user1.setId(1);
				user2.setId(2);
				user3.setId(3);
				user4.setId(4);
				user5.setId(5);
				user6.setId(6);

				Vector<User> listOfUser = new Vector<User>();
				listOfUser.add(user1);
				listOfUser.add(user2);
				listOfUser.add(user3);
				listOfUser.add(user4);
				listOfUser.add(user5);
				listOfUser.add(user6);
				
				addButton.getElement().setId("addButton");
				
				suggestBox.addKeyUpHandler(new KeyUpHandler() {
					public void onKeyUp(KeyUpEvent event) {
						if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
							final HorizontalPanel selectedUserPanel = new HorizontalPanel();
							Label selectedUserLabel = new Label(suggestBox.getText());
							Button deleteButton = new Button();
							/*
							 * int i = 0;
							 * 
							 * User example = new User(); while(i < listOfUser.size()){
							 * if
							 * (suggestBox.getText().equals(listOfUser.get(i+1).getFirstName
							 * ())){ example = listOfUser.get(i+1); } else { i++; } }
							 * deleteButton.setTabIndex(example.getId());
							 */

							deleteButton.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									RootPanel.get("Navigator")
											.remove(selectedUserPanel);
								}
							});

							deleteButton.getElement().setId("deleteButton");

							selectedUserPanel.add(selectedUserLabel);
							selectedUserPanel.add(deleteButton);
							RootPanel.get("Navigator").add(selectedUserPanel);

						}
					}
				});
				
				addButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						final HorizontalPanel selectedUserPanel = new HorizontalPanel();
						Label selectedUserLabel = new Label(suggestBox.getText());
						Button deleteButton = new Button();
						/*
						 * int i = 0;
						 * 
						 * User example = new User(); while(i < listOfUser.size()){
						 * if(suggestBox
						 * .getText().equals(listOfUser.get(i+1).getFirstName())){
						 * example = listOfUser.get(i+1); } else { i++; } }
						 * deleteButton.setTabIndex(example.getId());
						 */

						deleteButton.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								RootPanel.get("Navigator").remove(selectedUserPanel);
							}
						});

						deleteButton.getElement().setId("deleteButton");

						selectedUserPanel.add(selectedUserLabel);
						selectedUserPanel.add(deleteButton);
						RootPanel.get("Navigator").add(selectedUserPanel);
					}
				});
				
				// Befüllen der Suggestbox mit Inhalt. Am Ende sollen hier die
				// angemeldeten
				// User hinzugefügt werden z.B. mit oracle.addAll(user);
				// Kleines Beispiel:
				for (int i = 0; i < listOfUser.size(); i++) {
					String name = new String(listOfUser.get(i).getNickName());
					oracle.add(name);
				}
				
				// Hinzufügen der Widgets zu den jeweiligen Bereichen
				messagePanel.add(messageBox);
				messagePanel.add(buttonPanel);
				messagePanel.add(deleteButton);
				
				addPanel.add(suggestBox);
				addPanel.add(addButton);
				navigation.add(addPanel);

				// Verknüpfung mit der html-Struktur
				RootPanel.get("Details").add(messagePanel);
				RootPanel.get("Navigator").add(navigation);

				
				
	} //Ende onLoad
} //Ende Klasse
