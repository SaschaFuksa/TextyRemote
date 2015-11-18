package hdm.itprojekt.texty.shared.bo;

import java.io.Serializable;
import java.util.Date;

public abstract class BusinessObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private Date dateofCreation = null;
	/**
	 * @return dateofCreation
	 */
	public Date getDateofCreation() {
		return dateofCreation;
	}
	/**
	 * @param dateofCreation the dateofCreation to set
	 */
	public void setDateofCreation(Date dateofCreation) {
		this.dateofCreation = dateofCreation;
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
		str.append(", dateofCreation=");
		str.append(dateofCreation);
		str.append("]");
		return str.toString();
	}
	
	@Override
	public int hashCode() {
		final int a = 31;
		int result = 1;
		result = a * result
				+ ((dateofCreation == null) ? 0 : dateofCreation.hashCode());
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
		if (dateofCreation == null) {
			if (other.dateofCreation != null)
				return false;
		} else if (!dateofCreation.equals(other.dateofCreation))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	

}
