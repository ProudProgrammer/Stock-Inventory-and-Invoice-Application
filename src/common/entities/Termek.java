package common.entities;

import java.io.Serializable;
import java.text.Normalizer;
import java.text.NumberFormat;

public final class Termek implements Comparable<Termek>, Serializable {

	private static final long serialVersionUID = 1L;

	public enum MennyisegEgysege {
		DARAB("darab"), 
		CSOMAG("csomag"), 
		KILOGRAMM("kilogramm"), 
		LITER("liter"), 
		METER("méter");
		
		private final String irottFormaban;
		
		private MennyisegEgysege(String irottFormaban) {
			this.irottFormaban = irottFormaban;
		}
		
		public String irottFormaban() {
			return irottFormaban;
		}
	}

	private static final NumberFormat penzFormatum = NumberFormat.getCurrencyInstance();
	private static final NumberFormat szazalekFormatum = NumberFormat.getPercentInstance();
	
	private int termekkod;
	private String megnevezes;
	private int vtsz;
	private double afaSzazaleklabSzazadresze;
	private double egysegNettoBeszerzesiAr;
	private double egysegNettoEladasiAr;
	private MennyisegEgysege mennyisegEgysege;
	private int mennyiseg;
	
	static {
		penzFormatum.setMinimumFractionDigits(2);
		penzFormatum.setMaximumFractionDigits(2);
		szazalekFormatum.setMinimumFractionDigits(2);
		szazalekFormatum.setMaximumFractionDigits(2);
	}

	private Termek(Builder builder) {
		this.termekkod = builder.termekkod;
		this.megnevezes = builder.megnevezes;
		this.vtsz = builder.vtsz;
		this.afaSzazaleklabSzazadresze = builder.afaSzazaleklabSzazadresze;
		this.egysegNettoBeszerzesiAr = builder.egysegNettoBeszerzesiAr;
		this.egysegNettoEladasiAr = builder.egysegNettoEladasiAr;
		this.mennyisegEgysege = builder.mennyisegEgysege;
		this.mennyiseg = builder.mennyiseg;
	}
	
	public String toString() {
		return termekkod + ", " + megnevezes;
	}
	
	public String toStringReszletes() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
				(termekkod == 0 ? "" : "Termékkód: " + termekkod + "\n")
				+ (vtsz == 0 ? "" : "Vámtarifaszám: " + vtsz + "\n")
				+ "Termék megnevezése: " + megnevezes
				+ "\nÁfa: " + szazalekFormatum.format(afaSzazaleklabSzazadresze)
				+ (mennyiseg == 0 ? "" : "\nMennyisége: " + mennyiseg + mennyisegEgysege.irottFormaban));
		if(egysegNettoBeszerzesiAr != 0)
			stringBuilder.append(
				"\nEgység nettó beszerzési ára: " + penzFormatum.format(egysegNettoBeszerzesiAr)
				+ "\nÖsszes nettó beszerzési ára: " + penzFormatum.format(nettoBeszerzesiAr())
				+ "\nBeszerzés áfa értéke: " + penzFormatum.format(beszerzesiAfaErtek())
				+ "\nÖsszes bruttó beszerzési ára: " + penzFormatum.format(bruttoBeszerzesiAr()));
		if(egysegNettoEladasiAr != 0)
			stringBuilder.append(
				"\nEgység nettó eladasi ára: " + penzFormatum.format(egysegNettoEladasiAr)
				+ "\nÖsszes nettó eladasi ára: " + penzFormatum.format(nettoEladasiAr())
				+ "\nEladás áfa értéke: " + penzFormatum.format(eladasiAfaErtek())
				+ "\nÖsszes bruttó eladasi ára: " + penzFormatum.format(bruttoEladasiAr()));
		return stringBuilder.toString();
				
	}
	
	private double egysegBruttoBeszerzesiAr() {
		return egysegNettoBeszerzesiAr + egysegNettoBeszerzesiAr * afaSzazaleklabSzazadresze;
	}
	
	public double nettoBeszerzesiAr() {
		return egysegNettoBeszerzesiAr * mennyiseg;
	}
	
	public double bruttoBeszerzesiAr() {
		return egysegBruttoBeszerzesiAr() * mennyiseg;
	}
	
	public double beszerzesiAfaErtek() {
		return bruttoBeszerzesiAr() - nettoBeszerzesiAr();
	}
	
	private double egysegBruttoEladasiAr() {
		return egysegNettoEladasiAr + egysegNettoEladasiAr * afaSzazaleklabSzazadresze;
	}
	
	public double nettoEladasiAr() {
		return egysegNettoEladasiAr * mennyiseg;
	}
	
	public double bruttoEladasiAr() {
		return egysegBruttoEladasiAr() * mennyiseg;
	}
	
	public double eladasiAfaErtek() {
		return bruttoEladasiAr() - nettoEladasiAr();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(afaSzazaleklabSzazadresze);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(egysegNettoEladasiAr);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((megnevezes == null) ? 0 : megnevezes.hashCode());
		result = prime
				* result
				+ ((mennyisegEgysege == null) ? 0 : mennyisegEgysege.hashCode());
		result = prime * result + vtsz;
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
		Termek other = (Termek) obj;
		if (Double.doubleToLongBits(afaSzazaleklabSzazadresze) != Double
				.doubleToLongBits(other.afaSzazaleklabSzazadresze))
			return false;
		if (Double.doubleToLongBits(egysegNettoEladasiAr) != Double
				.doubleToLongBits(other.egysegNettoEladasiAr))
			return false;
		if (megnevezes == null) {
			if (other.megnevezes != null)
				return false;
		} else if (!megnevezes.equals(other.megnevezes))
			return false;
		if (mennyisegEgysege != other.mennyisegEgysege)
			return false;
		if (vtsz != other.vtsz)
			return false;
		return true;
	}

	@Override
	public int compareTo(Termek masik) {
		return megnevezes.compareTo(masik.megnevezes);
	}
	
	//====================Getters====================

	public int getTermekkod() {
		return termekkod;
	}

	public String getMegnevezes() {
		return megnevezes;
	}

	public int getVtsz() {
		return vtsz;
	}

	public double getAfaSzazaleklab() {
		return (afaSzazaleklabSzazadresze*100);
	}

	public double getEgysegNettoBeszerzesiAr() {
		return egysegNettoBeszerzesiAr;
	}
	
	public double getEgysegNettoEladasiAr() {
		return egysegNettoEladasiAr;
	}

	public MennyisegEgysege getMennyisegEgysege() {
		return mennyisegEgysege;
	}

	public int getMennyiseg() {
		return mennyiseg;
	}

	//====================Setters====================
	
	public void setTermekkod(int termekkod) {
		this.termekkod = termekkod;
	}

	public void setMegnevezes(String megnevezes) {
		this.megnevezes = megnevezes;
	}

	public void setVtsz(int vtsz) {
		this.vtsz = vtsz;
	}

	public void setAfaSzazaleklab(double afaSzazaleklab) {
		this.afaSzazaleklabSzazadresze = afaSzazaleklab/100;
	}

	public void setEgysegNettoBeszerzesiAr(double egysegNettoBeszerzesiAr) {
		this.egysegNettoBeszerzesiAr = egysegNettoBeszerzesiAr;
	}
	
	public void setEgysegNettoEladasiAr(double egysegNettoEladasiAr) {
		this.egysegNettoEladasiAr = egysegNettoEladasiAr;
	}

	public void setMennyisegEgysege(MennyisegEgysege mennyisegEgysege) {
		this.mennyisegEgysege = mennyisegEgysege;
	}
	
	public void setMennyisegEgysege(String mennyisegEgysege) {
		if(mennyisegEgysege == "") {
			this.mennyisegEgysege = MennyisegEgysege.DARAB;
		} else {
			mennyisegEgysege = mennyisegEgysege.toUpperCase();
			mennyisegEgysege = Normalizer.normalize(mennyisegEgysege, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			this.mennyisegEgysege = MennyisegEgysege.valueOf(mennyisegEgysege);
		}
	}

	public void setMennyiseg(int mennyiseg) {
		this.mennyiseg = mennyiseg;
	}

	//====================Builder====================
	
	public static final class Builder {

		// Kötelezõ paraméterek
		private final String megnevezes;
		private final double afaSzazaleklabSzazadresze;
		
		// Tetszõleges paraméterek
		private int termekkod = 0;
		private int vtsz = 0;
		private double egysegNettoBeszerzesiAr = 0.0;
		private double egysegNettoEladasiAr = 0.0;
		private MennyisegEgysege mennyisegEgysege = MennyisegEgysege.DARAB;
		private int mennyiseg = 0;
		
		public Builder(String megnevezes, double afaSzazaleklab) {
			this.megnevezes = megnevezes;
			this.afaSzazaleklabSzazadresze = afaSzazaleklab/100;
		}
		
		public Builder termekkod(int termekkod) {
			this.termekkod = termekkod;
			return this;
		}
		
		public Builder vtsz(int vtsz) {
			this.vtsz = vtsz;
			return this;
		}
		
		public Builder egysegNettoBeszerzesiAr(double egysegNettoBeszerzesiAr) {
			this.egysegNettoBeszerzesiAr = egysegNettoBeszerzesiAr;
			return this;
		}
		
		public Builder egysegNettoEladasiAr(double egysegNettoEladasiAr) {
			this.egysegNettoEladasiAr = egysegNettoEladasiAr;
			return this;
		}
		
		public Builder mennyisegEgysege(MennyisegEgysege mennyisegEgysege) {
			this.mennyisegEgysege = mennyisegEgysege;
			return this;
		}
		
		public Builder mennyisegEgysege(String mennyisegEgysege) {
			if(mennyisegEgysege == "") {
				this.mennyisegEgysege = MennyisegEgysege.DARAB;
			} else {
				mennyisegEgysege = mennyisegEgysege.toUpperCase();
				mennyisegEgysege = Normalizer.normalize(mennyisegEgysege, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				this.mennyisegEgysege = MennyisegEgysege.valueOf(mennyisegEgysege);
			}
			return this;
		}
		
		public Builder mennyiseg(int mennyiseg) {
			this.mennyiseg = mennyiseg;
			return this;
		}
		
		public Termek build() {
			return new Termek(this);
		}
	}

}
