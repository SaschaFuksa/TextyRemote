package hdm.itprojekt.texty.shared.bo;

import java.io.Serializable;
import java.util.Date;

public abstract class BusinessObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id = 0;
	private Date dateOfCreation = null;

	
	/**
	 * Vergleicht ein Objekt mit einem anderen.
	 */
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

	/**
	 * @return dateOfCreation
	 */
	public Date getDateOfCreation() {
		return dateOfCreation;
	}

	/**
	 * @return ID eines Objektes
	 */
	public int getId() {
		return id;
	}

	/**
	 * Generieren eines Hash Wertes zur eindeutigen Indentifizierung von Objekten. 
	 * Dient auch zum Vergleichen von Objekten.
	 */
	@Override
	public int hashCode() {
		final int a = 31;
		int result = 1;
		result = a * result
				+ ((dateOfCreation == null) ? 0 : dateOfCreation.hashCode());
		result = a * result + id;
		return result;
	}

	/**
	 * Setzen des Erstellungsdatums
	 * @param dateOfCreation
	 */
	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	/**
	 * Setzen der Objekt ID
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	@Override
	/**
	 * Rückgabe des Klassennamen und der ID, der jeweiligen Instanz.
	 */
	public String toString() {

		return this.getClass().getName() + "Id: " + this.id;
	}

}
