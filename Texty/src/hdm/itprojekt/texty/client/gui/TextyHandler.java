package hdm.itprojekt.texty.client.gui;

public class TextyHandler {

	// Attribut, welches im Handler festlegen wird, ob der handler bereits
	// hinzugef�gt wurde. Dies wird wichtig in den onLoads um einen handler nur
	// einmal hinzuzuf�gen
	private boolean applicability = false;

	public boolean isApplicability() {
		return applicability;
	}

	public void setApplicability(boolean applicability) {
		this.applicability = applicability;
	}
	
	

}
