package common.entities;

import java.io.Serializable;
import java.text.Normalizer;

public final class Felhasznalo implements Comparable<Felhasznalo>, Serializable {
	
	private static final long serialVersionUID = 1L;

	public enum RendszergazdaJog {
		IGEN("igen"), 
		NEM("nem");
		
		private final String irottFormaban;
		
		private RendszergazdaJog(String irottFormaban) {
			this.irottFormaban = irottFormaban;
		}
		
		public String irottFormaban() {
			return irottFormaban;
		}
	}
	
	// Nev és jelszo csak kisbetûs és ékezet nélküli karakterbõl állhat
	private String nev;
	private String jelszo;
	private RendszergazdaJog rendszergazdaJog;
	private String teljesNev = "";
	private String tajSzam = "";
	
	private Felhasznalo(Builder builder) {
		this.nev = builder.nev;
		this.jelszo = builder.jelszo;
		this.rendszergazdaJog = builder.rendszergazdaJog;
		this.teljesNev = builder.teljesNev;
		this.tajSzam = builder.tajSzam;
	}
	
	public String toString() {
		return nev;
	}
	
	public String toStringReszletes() {
		return "Felhasználónév: " + nev 
				+ (teljesNev.equals("") ? "" : "\nTeljes név: " + teljesNev)
				+ (tajSzam.equals("") ? "" : "\nTaj szám: " + tajSzam);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Felhasznalo other = (Felhasznalo) obj;
		if (nev == null) {
			if (other.nev != null)
				return false;
		} else if (!nev.equals(other.nev))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Felhasznalo masik) {
		return this.nev.compareTo(masik.nev);
	}

	//====================Getters====================

	public String getNev() {
		return nev;
	}

	public String getJelszo() {
		return jelszo;
	}

	public RendszergazdaJog getRendszergazdaJog() {
		return rendszergazdaJog;
	}
	
	public String getTeljesNev() {
		return teljesNev;
	}
	
	public String getTajSzam() {
		return tajSzam;
	}

	//====================Setters====================
	
	public void setNev(String nev) {
		nev = nev.toLowerCase();
		nev = Normalizer.normalize(nev, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		this.nev = nev;
	}

	public void setJelszo(String jelszo) {
		jelszo = jelszo.toLowerCase();
		jelszo = Normalizer.normalize(jelszo, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		this.jelszo = jelszo;
	}

	public void setRendszergazdaJog(RendszergazdaJog rendszergazdaJog) {
		this.rendszergazdaJog = rendszergazdaJog;
	}
	
	public void setRendszergazdaJog(String rendszergazdaJog) {
		if(rendszergazdaJog.equals("")) {
			this.rendszergazdaJog = RendszergazdaJog.NEM;
		} else {
			rendszergazdaJog = rendszergazdaJog.toUpperCase();
			rendszergazdaJog = Normalizer.normalize(rendszergazdaJog, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			this.rendszergazdaJog = RendszergazdaJog.valueOf(rendszergazdaJog);
		}
	}
	
	public void setTeljesNev(String teljesNev) {
		this.teljesNev = teljesNev;
	}
	
	public void setTajSzam(String tajSzam) {
		this.tajSzam = tajSzam;
	}
	
	//====================Builder====================
	
	public static class Builder {
		
		// Kötelezõ paraméterek
		// Nev és jelszo csak kisbetûs és ékezet nélküli karakterbõl állhat
		private final String nev;
		private final String jelszo;
		
		// Tetszõleges paraméterek
		private RendszergazdaJog rendszergazdaJog = RendszergazdaJog.NEM;
		private String teljesNev = "";
		private String tajSzam = "";
		
		public Builder(String nev, String jelszo) {
			nev = nev.toLowerCase();
			nev = Normalizer.normalize(nev, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			jelszo = jelszo.toLowerCase();
			jelszo = Normalizer.normalize(jelszo, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			this.nev = nev;
			this.jelszo = jelszo;
		}
		
		public Builder rendszergazdaJog(RendszergazdaJog rendszergazdaJog) {
			this.rendszergazdaJog = rendszergazdaJog;
			return this;
		}
		
		public Builder rendszergazdaJog(String rendszergazdaJog) {
			if(rendszergazdaJog.equals("")) {
				this.rendszergazdaJog = RendszergazdaJog.NEM;
			} else {
				rendszergazdaJog = rendszergazdaJog.toUpperCase();
				rendszergazdaJog = Normalizer.normalize(rendszergazdaJog, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				this.rendszergazdaJog = RendszergazdaJog.valueOf(rendszergazdaJog);
			}
			return this;
		}
		
		public Builder teljesNev(String teljesNev) {
			this.teljesNev = teljesNev;
			return this;
		}
		
		public Builder tajSzam(String tajSzam) {
			this.tajSzam = tajSzam;
			return this;
		}
			
		public Felhasznalo build() {
			return new Felhasznalo(this);
		}
	}
	
}
