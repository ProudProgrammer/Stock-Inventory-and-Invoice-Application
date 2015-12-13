package common.entities;

import java.io.Serializable;

public final class Ceg implements Comparable<Ceg>, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String nev;
	private Cim cim;
	private long adoszam;
	private String telefon;
	private String fax;
	private String email;
	private String webcim;

	private Ceg(Builder builder) {
		this.id = builder.id;
		this.nev = builder.nev;
		this.cim = builder.cim;
		this.adoszam = builder.adoszam;
		this.telefon = builder.telefon;
		this.fax = builder.fax;
		this.email = builder.email;
		this.webcim = builder.webcim;
	}

	public String toString() {
		return id + ", " + nev;
	}
	
	public String toStringReszletes() {
		return (id == 0 ? "" : "Cég id: " + id + "\n")
				+ "Cég neve: " + nev
				+ "\nCég címe: " + cim
				+ (adoszam == 0 ? "" : "\nCég adószáma: " + adoszam)
				+ (telefon.equals("") ? "" : "\nCég telefonszáma: " + telefon)
				+ (fax.equals("") ? "" : "\nCég faxszáma: " + fax)
				+ (email.equals("") ? "" : "\nCég e-mail címe: " + email)
				+ (webcim.equals("") ? "" : "\nCég webcíme: " + webcim);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (adoszam ^ (adoszam >>> 32));
		result = prime * result + ((cim == null) ? 0 : cim.hashCode());
		result = prime * result + ((nev == null) ? 0 : nev.hashCode());
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
		Ceg other = (Ceg) obj;
		if (adoszam != other.adoszam)
			return false;
		if (cim == null) {
			if (other.cim != null)
				return false;
		} else if (!cim.equals(other.cim))
			return false;
		if (nev == null) {
			if (other.nev != null)
				return false;
		} else if (!nev.equals(other.nev))
			return false;
		return true;
	}

	@Override
	public int compareTo(Ceg masik) {
		return nev.compareTo(masik.nev);
	}

	//====================Getters====================
	
	public int getId() {
		return id;
	}

	public String getNev() {
		return nev;
	}

	public Cim getCim() {
		return cim;
	}

	public long getAdoszam() {
		return adoszam;
	}

	public String getTelefon() {
		return telefon;
	}

	public String getFax() {
		return fax;
	}

	public String getEmail() {
		return email;
	}

	public String getWebcim() {
		return webcim;
	}

	//====================Setters====================
	
	public void setId(int id) {
		this.id = id;
	}

	public void setNev(String nev) {
		this.nev = nev;
	}

	public void setCim(Cim cim) {
		this.cim = cim;
	}

	public void setAdoszam(long adoszam) {
		this.adoszam = adoszam;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setWebcim(String webcim) {
		this.webcim = webcim;
	}

	//====================Builder====================
	
	public static final class Builder {

		// Kötelezõ paraméterek
		private final String nev;
		private final Cim cim;

		// Tetszõleges paraméterek
		private int id = 0;
		private long adoszam = 0;
		private String telefon = "";
		private String fax = "";
		private String email = "";
		private String webcim = "";

		public Builder(String nev, Cim cim) {
			this.nev = nev;
			this.cim = cim;
		}

		public Builder id(int id) {
			this.id = id;
			return this;
		}

		public Builder adoszam(long adoszam) {
			this.adoszam = adoszam;
			return this;
		}

		public Builder telefon(String telefon) {
			this.telefon = telefon;
			return this;
		}

		public Builder fax(String fax) {
			this.fax = fax;
			return this;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public Builder webcim(String webcim) {
			this.webcim = webcim;
			return this;
		}

		public Ceg build() {
			return new Ceg(this);
		}
	}

}
