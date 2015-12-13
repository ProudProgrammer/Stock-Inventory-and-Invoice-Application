package common.entities;

import java.io.Serializable;

public final class Telepules implements Comparable<Telepules>, Serializable {

	private static final long serialVersionUID = 1L;
	
	private int iranyitoSzam;
	private String nev;
	
	public Telepules(int iranyitoSzam, String nev) {
		this.iranyitoSzam = iranyitoSzam;
		this.nev = nev;
	}
	
	public String toString() {
		return iranyitoSzam + " " + nev;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + iranyitoSzam;
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
		Telepules other = (Telepules) obj;
		if (iranyitoSzam != other.iranyitoSzam)
			return false;
		return true;
	}

	@Override
	public int compareTo(Telepules masik) {
		return this.nev.compareTo(masik.nev);
	}

	//====================Getters====================
	
	public int getIranyitoSzam() {
		return iranyitoSzam;
	}

	public String getNev() {
		return nev;
	}

	//====================Setters====================
	
	public void setIranyitoSzam(int iranyitoSzam) {
		this.iranyitoSzam = iranyitoSzam;
	}

	public void setNev(String nev) {
		this.nev = nev;
	}

}
