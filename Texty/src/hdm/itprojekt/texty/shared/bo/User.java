package hdm.itprojekt.texty.shared.bo;

public class User extends BusinessObject {
	
	private static final long serialVersionUID = 1L;
	private String nickName = "";
	private String firstName = "";	
	private String lastName = "";
	private String email = "";
	
	public User(){}
	public User(String nickName, String email){
		this.nickName = nickName;
		this.email = email;
	}
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
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

	/*
	 * (non-Javadoc)
	 * @see hdm.itprojekt.texty.shared.bo.BusinessObject#toString()
	 * + Vor und Nachname wird ausgegeben. 
	 * TODO Doku 
	 */
	@Override
	public String toString() {
		if(this.firstName == "" && this.lastName == ""){
			return super.toString() + " " + this.email;
		}
		return super.toString() + " " + this.firstName + " " + this.lastName;
	}

}
