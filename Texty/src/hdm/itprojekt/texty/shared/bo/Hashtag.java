package hdm.itprojekt.texty.shared.bo;

public class Hashtag extends BusinessObject {
	private static final long serialVersionUID = 1L;
	private String keyword = "";	

	/**
	 * 
	 * @return keyword
	 */
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}