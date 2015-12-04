package hdm.itprojekt.texty.shared.bo;

public class User extends BusinessObject {
	
	private static final long serialVersionUID = 1L;
	private String nickName = "";
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

	/*
	 * (non-Javadoc)
	 * @see hdm.itprojekt.texty.shared.bo.BusinessObject#toString()
	 * + Vor und Nachname wird ausgegeben. 
	 * TODO Doku 
	 */
	@Override
	public String toString() {
		return super.toString() + " " + this.nickName;
	}

}
