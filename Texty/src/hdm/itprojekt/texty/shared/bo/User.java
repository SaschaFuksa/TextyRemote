package hdm.itprojekt.texty.shared.bo;

public class User extends BusinessObject {
	
	private static final long serialVersionUID = 1L;
	private String firstName = "";
	private String lastName = "";
	private String email = "";
	private String googleAccountAPI = "";
	
	public User() {
		
	}
	
	public User(String firstName, String lastName, String email){
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getGoogleAccountAPI() {
		return googleAccountAPI;
	}
	
	public void setGoogleAccountAPI(String googleAccountAPI) {
		this.googleAccountAPI = googleAccountAPI;
	}
	/*
	 * (non-Javadoc)
	 * @see hdm.itprojekt.texty.shared.bo.BusinessObject#toString()
	 * + Vor und Nachname wird ausgegeben. 
	 * TODO Doku 
	 */
	@Override
	public String toString() {
		return super.toString() + " " + this.firstName + " " + this.lastName;
	}

}
