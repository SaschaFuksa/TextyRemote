package hdm.itprojekt.texty.shared.bo;


/**
 * Diese Klasse stellt das Grundgerüst für alle User Objekte dar. 
 */
public class User extends BusinessObject {

	private static final long serialVersionUID = 1L;
	private String nickName = "";
	private String firstName = "";
	private String lastName = "";
	private String email = "";

	/**
	 * No Argument Konstruktor
	 */
	public User() {
	}

	/**
	 * Vollständiger Konstruktor
	 * @param nickName
	 * @param email
	 */
	public User(String nickName, String email) {
		this.nickName = nickName;
		this.email = email;
	}

	/**
	 * Auslesen der Email
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Auslesen des Vornamen
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Auslesen des Nachnamen
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Auslesen des Spitznamen (von GOOGLE)
	 * @return
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * Setzen der Email
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Setzen des Vornamen
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Setzen des Nachnamen
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Setzen des Spitznamen
	 * @param nickName
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * Vor und Nachname wird ausgegeben.
	 */
	@Override
	public String toString() {
		if (this.firstName == "" && this.lastName == "") {
			return super.toString() + " " + this.email;
		}
		return super.toString() + " " + this.firstName + " " + this.lastName;
	}

}
