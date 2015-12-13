package common.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.Normalizer;
import java.util.Date;
import java.util.List;

//A számlát az eladó cég állítja ki

public final class Szamla implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public enum SzamlaAllapot {
		ERVENYES("érvényes"),
		SZTORNOZOTT("sztornózott");
		
		private String irottFormaban;
		
		private SzamlaAllapot(String irottFormaban) {
			this.irottFormaban = irottFormaban;
		}
		
		public String irottFormaban() {
			return irottFormaban;
		}
	}
	
	private long sorszam;
	private Date datum;
	private Ceg vevo;
	private Ceg elado;
	private String megjegyzes;
	private List<Termek> termekek;
	private Felhasznalo keszitette;
	private SzamlaAllapot szamlaAllapot;
	private Felhasznalo allapotatBeallitotta;
	
	private static final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT);

	private Szamla(Builder builder) {
		this.sorszam = builder.sorszam;
		this.datum = builder.datum;
		this.vevo = builder.vevo;
		this.elado = builder.elado;
		this.megjegyzes = builder.megjegyzes;
		this.termekek = builder.termekek;
		this.keszitette = builder.keszitette;
		this.szamlaAllapot = builder.szamlaAllapot;
		this.allapotatBeallitotta = builder.allapotatBeallitotta;
	}
	
	public String toString() {
		StringBuilder szoveg = new StringBuilder(
				(sorszam == 0 ? "" : "Számla sorszáma: " + sorszam + "\n")
				+ "Számla állapota: " + szamlaAllapot.irottFormaban
				+ (allapotatBeallitotta == null ? "" : "\nÁllapotát beállította:\n" + allapotatBeallitotta)
				+ "\nTeljesítés kelte: " + dateFormat.format(datum)
				+ "\nSzámla kelte: " + dateFormat.format(datum)
				+ "\nFizetési határidõ: " + dateFormat.format(datum)
				+ (keszitette == null ? "" : "\nSzámlát készítette:\n" + keszitette)
				+ "\n\nA számlát kiállító cég mint eladó adatai:\n" + elado
				+ "\n\nA cég mint vevõ partner adatai:\n" + vevo
				+ (megjegyzes.equals("") ? "" : "\n\nMegjegyzés: " + megjegyzes)
				+ "\n\nTermékek adatai:\n");
		for(Termek termek : termekek) {
			szoveg.append(termek + "\n\n");
		}
		return szoveg.toString();
	}

	public boolean addTermek(Termek termek) {
		return this.termekek.add(termek);
	}
	
	public boolean addAllTermek(List<Termek> termekek) {
		return this.termekek.addAll(termekek);
	}
	
	public double nettoEladasiAr() {
		double ar = 0;
		for(Termek t : termekek) {
			ar += t.nettoEladasiAr();
		}
		return ar;
	}
	
	public double eladasiAfaErtek() {
		double ertek = 0;
		for(Termek t : termekek) {
			ertek += t.eladasiAfaErtek();
		}
		return ertek;
	}
	
	public double bruttoEladasiAr() {
		double ar = 0;
		for(Termek t : termekek) {
			ar += t.bruttoEladasiAr();
		}
		return ar;
	}
	
	//====================Getters====================
	
	public long getSorszam() {
		return sorszam;
	}

	public Date getDatum() {
		return datum;
	}

	public Ceg getVevo() {
		return vevo;
	}

	public Ceg getElado() {
		return elado;
	}

	public String getMegjegyzes() {
		return megjegyzes;
	}

	public List<Termek> getTermekek() {
		return termekek;
	}
	
	public Felhasznalo getKeszitette() {
		return keszitette;
	}
	
	public SzamlaAllapot getSzamlaAllapot() {
		return szamlaAllapot;
	}
	
	public Felhasznalo getAllapotatBeallitotta() {
		return allapotatBeallitotta;
	}

	//====================Setters====================
	
	public void setSorszam(long sorszam) {
		this.sorszam = sorszam;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public void setVevo(Ceg vevo) {
		this.vevo = vevo;
	}

	public void setElado(Ceg elado) {
		this.elado = elado;
	}

	public void setMegjegyzes(String megjegyzes) {
		this.megjegyzes = megjegyzes;
	}

	public void setTermekek(List<Termek> termekek) {
		this.termekek = termekek;
	}
	
	public void setKeszitette(Felhasznalo keszitette) {
		this.keszitette = keszitette;
	}
	
	public void setSzamlaAllapot(SzamlaAllapot szamlaAllapot) {
		this.szamlaAllapot = szamlaAllapot;
	}
	
	public void setSzamlaAllapot(String szamlaAllapot) {
		if(szamlaAllapot == "") {
			this.szamlaAllapot = SzamlaAllapot.ERVENYES;
		} else {
			szamlaAllapot = szamlaAllapot.toUpperCase();
			szamlaAllapot = Normalizer.normalize(szamlaAllapot, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			this.szamlaAllapot = SzamlaAllapot.valueOf(szamlaAllapot);
		}
	}
	
	public void setAllapotatBeallitotta(Felhasznalo allapotatBeallitotta) {
		this.allapotatBeallitotta = allapotatBeallitotta;
	}

	//====================Builder====================
	
	public static final class Builder {

		// Kötelezõ paraméterek
		private final Ceg vevo;
		private final Ceg elado;
		private final List<Termek> termekek;

		// Tetszõleges paraméterek
		private long sorszam = 0;
		private Date datum = new Date(System.currentTimeMillis());
		private String megjegyzes = "";
		private Felhasznalo keszitette = null;
		private SzamlaAllapot szamlaAllapot = SzamlaAllapot.ERVENYES;
		private Felhasznalo allapotatBeallitotta = null;

		public Builder(Ceg vevo, Ceg elado, List<Termek> termekek) {
			this.vevo = vevo;
			this.elado = elado;
			this.termekek = termekek;
		}

		public Builder sorszam(long sorszam) {
			this.sorszam = sorszam;
			return this;
		}

		public Builder datum(Date datum) {
			this.datum = datum;
			return this;
		}

		public Builder megjegyzes(String megjegyzes) {
			this.megjegyzes = megjegyzes;
			return this;
		}
		
		public Builder keszitette(Felhasznalo keszitette) {
			this.keszitette = keszitette;
			return this;
		}
		
		public Builder szamlaAllapot(SzamlaAllapot szamlaAllapot) {
			this.szamlaAllapot = szamlaAllapot;
			return this;
		}
		
		public Builder szamlaAllapot(String szamlaAllapot) {
			if(szamlaAllapot == "") {
				this.szamlaAllapot = SzamlaAllapot.ERVENYES;
			} else {
				szamlaAllapot = szamlaAllapot.toUpperCase();
				szamlaAllapot = Normalizer.normalize(szamlaAllapot, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				this.szamlaAllapot = SzamlaAllapot.valueOf(szamlaAllapot);
			}
			return this;
		}
		
		public Builder allapotatBeallitotta(Felhasznalo allapotatBeallitotta) {
			this.allapotatBeallitotta = allapotatBeallitotta;
			return this;
		}

		public Szamla build() {
			return new Szamla(this);
		}
	}
}
