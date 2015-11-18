package hdm.itprojekt.texty.shared.bo;

import java.io.Serializable;
import java.util.Date;

public abstract class BusinessObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private Date dateofCreation = null;

}
