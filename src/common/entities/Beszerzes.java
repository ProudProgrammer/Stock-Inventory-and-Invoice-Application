package common.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

//A beszerz�st a vev� c�g kezeli

public class Beszerzes implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date datum;
	private Ceg vevo;
	private Ceg elado;
	private Termek termek;
	private Felhasznalo felhasznalo;
	private long id;
	
	private static final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT);
	
	private Beszerzes(Builder builder) {
		this.datum = builder.datum;
		this.vevo = builder.vevo;
		this.elado = builder.elado;
		this.termek = builder.termek;
		this.felhasznalo = builder.felhasznalo;
		this.id = builder.id;
	}
	
	public String toString() {
		return "Beszerz�s kelte: " + dateFormat.format(datum)
				+ (felhasznalo == null ? "" : "\nBeszerz�st k�sz�tette:\n" + felhasznalo)
				+ "\n\nC�g mint vev� adatai:\n" + vevo
				+ "\n\nC�g mint elad� partner adatai:\n" + elado
				+ "\n\nTerm�k adatai:\n" + termek;
		
	}
	
	//====================Getters====================

	public Date getDatum() {
		return datum;
	}

	public Ceg getVevo() {
		return vevo;
	}

	public Ceg getElado() {
		return elado;
	}

	public Termek getTermek() {
		return termek;
	}
	
	public Felhasznalo getFelhasznalo() {
		return felhasznalo;
	}
	
	public long getId() {
		return id;
	}

	//====================Setters====================

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public void setVevo(Ceg vevo) {
		this.vevo = vevo;
	}

	public void setElado(Ceg elado) {
		this.elado = elado;
	}

	public void setTermek(Termek termek) {
		this.termek = termek;
	}
	
	public void setFelhasznalo(Felhasznalo felhasznalo) {
		this.felhasznalo = felhasznalo;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	//====================Builder====================
	
	public static final class Builder {

		// K�telez� param�terek
		private final Ceg vevo;
		private final Ceg elado;
		private final Termek termek;

		// Tetsz�leges param�terek
		private Date datum = new Date(System.currentTimeMillis());
		private Felhasznalo felhasznalo = null;
		private long id = 0;

		public Builder(Ceg vevo, Ceg elado, Termek termek) {
			this.vevo = vevo;
			this.elado = elado;
			this.termek = termek;
		}

		public Builder datum(Date datum) {
			this.datum = datum;
			return this;
		}

		public Builder felhasznalo(Felhasznalo felhasznalo) {
			this.felhasznalo = felhasznalo;
			return this;
		}
		
		public Builder id(long id) {
			this.id = id;
			return this;
		}

		public Beszerzes build() {
			return new Beszerzes(this);
		}
	}
}
