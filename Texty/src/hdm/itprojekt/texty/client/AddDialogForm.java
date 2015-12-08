package hdm.itprojekt.texty.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootPanel;

/*
 * Diese Klasse ist ein erster Testansatz, um dem Nutzer Feedback-Möglichkeiten zu geben. Führt er etwas aus, sollte der Nutzer 
 * benachrichtigt werden z.B. wenn etwas schiefgeht (Nichterreichbarkeit des Servers, etc.) Über die genaue Implementierung ist noch zu sprechen.
 * Quelle: http://www.gwtproject.org/javadoc/latest/com/google/gwt/user/client/ui/DialogBox.html
 * 
 * Alternative Window.alert? 
 */
public class AddDialogForm implements EntryPoint, ClickHandler {

	  private static class MyDialog extends DialogBox {

	    public MyDialog() {
	      // Set the dialog box's caption.
	      setText("My First Dialog");

	      // Enable animation.
	      setAnimationEnabled(true);

	      // Enable glass background.
	      setGlassEnabled(true);

	      // DialogBox is a SimplePanel, so you have to set its widget property to
	      // whatever you want its contents to be.
	      Button ok = new Button("OK");
	      ok.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	          MyDialog.this.hide();
	        }
	      });
	      setWidget(ok);
	    }
	  }

	  public void onModuleLoad() {
	    Button b = new Button("Click me");
	    b.addClickHandler(this);

	    RootPanel.get("Details").add(b);
	  }

	  public void onClick(ClickEvent event) {
	    // Instantiate the dialog box and show it.
	    new MyDialog().show();
	  }
	}