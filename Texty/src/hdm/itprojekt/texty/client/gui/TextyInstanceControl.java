package hdm.itprojekt.texty.client.gui;
/**
 * Eine Kontrolle f�r einzelne Widgets oder Objekte, um eine einmalige Instanziierung w�hrend dem run() oder onload() zu gew�hrleisten. 
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
