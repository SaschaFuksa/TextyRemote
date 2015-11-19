package hdm.itprojekt.texty.shared.bo;

import java.io.Serializable;
import java.util.Date;

public abstract class BusinessObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private Date dateOfCreation = null;
	/**
	 * @return dateOfCreation
	 */
	public Date getDateOfCreation() {
		return dateOfCreation;
	}
	/**
	 * @param dateOfCreation the dateOfCreation to set
	 */
	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}
	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("BusinessObject [id=");
		str.append(id);
		str.append(", dateOfCreation=");
		str.append(dateOfCreation);
		str.append("]");
		return str.toString();
	}
	
	@Override
	public int hashCode() {
		final int a = 31;
		int result = 1;
		result = a * result
				+ ((dateOfCreation == null) ? 0 : dateOfCreation.hashCode());
		result = a * result + id;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessObject other = (BusinessObject) obj;
		if (dateOfCreation == null) {
			if (other.dateOfCreation != null)
				return false;
		} else if (!dateOfCreation.equals(other.dateOfCreation))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	

}
