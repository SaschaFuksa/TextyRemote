package hdm.itprojekt.texty.client.gui;
/**
 * Eine Kontrolle für einzelne Widgets oder Objekte, um eine einmalige Instanziierung während dem run() oder onload() zu gewährleisten. 
 * 
 * 
 */
public class TextyInstanceControl {

	private boolean applicability = false;

	public boolean isApplicability() {
		return applicability;
	}

	public void setApplicability(boolean applicability) {
		this.applicability = applicability;
	}
	
	

}
