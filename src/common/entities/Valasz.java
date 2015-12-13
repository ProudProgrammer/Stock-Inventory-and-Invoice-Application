package common.entities;

import java.io.Serializable;
import java.sql.SQLException;

import server.database.DataBaseOperationException;

public class Valasz implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean sikeresAtvitel = true;
	private boolean voltESQLException = false;
	private boolean sikeresDataBaseOperation = true;
	private boolean helytelenKeres = false;
	private DataBaseOperationException.Eset eset = null;
	private SQLException sqlException = null;
	private Object adat = null;
	
	public Valasz() {}
	
	public boolean getSikeresAtvitel() {
		return sikeresAtvitel;
	}
	
	public boolean getVoltESQLException() {
		return voltESQLException;
	}
	
	public boolean getSikeresDataBaseOperation() {
		return sikeresDataBaseOperation;
	}
	
	public SQLException getSQLException() {
		return sqlException;
	}
	
	public boolean getHelytelenKeres() {
		return helytelenKeres;
	}
	
	public DataBaseOperationException.Eset getEset() {
		return eset;
	}
	
	public Object getAdat() {
		return adat;
	}
	
	public void setSikeresAtvitel(boolean sikeresAtvitel) {
		this.sikeresAtvitel = sikeresAtvitel;
	}
	
	public void setVoltESQLException(boolean voltESQLException) {
		this.voltESQLException = voltESQLException;
	}
	
	public void setSikeresDataBaseOperation(boolean sikeresDataBaseOperation) {
		this.sikeresDataBaseOperation = sikeresDataBaseOperation;
	}
	
	public void setHelytelenKeres(boolean helytelenKeres) {
		this.helytelenKeres = helytelenKeres;
	}
	
	public void setEset(DataBaseOperationException.Eset eset) {
		this.eset = eset;
	}
	
	public void setSQLException(SQLException sqlException) {
		this.sqlException = sqlException;
	}
	
	public void setAdat(Object adat) {
		this.adat = adat;
	}
}
