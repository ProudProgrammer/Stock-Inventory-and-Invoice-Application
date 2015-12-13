package common.entities;

import java.io.Serializable;
import java.text.Normalizer;

public final class Cim implements Comparable<Cim>, Serializable {

	private static final long serialVersionUID = 1L;

	public enum KozteruletJellege {
		DULO("dûlõ"), 
		FASOR("fasor"), 
		KORUT("körút"), 
		UT("út"), 
		UTCA("utca"), 
		KAPU("kapu"), 
		SETANY("sétány"), 
		SOR("sor"), 
		SUGARUT("sugárút"), 
		TER("tér");

		private final String irottFormaban;

		private KozteruletJellege(String irottFormaban) {
			this.irottFormaban = irottFormaban;
		}

		public String irottFormaban() {
			return irottFormaban;
		}
	}

	private Telepules telepules;
	private String kozterulet;
	private KozteruletJellege kozteruletJellege;
	private int hazSzam;
	private String emelet;
	private String ajto;
	private int id;

	private Cim(Builder builder) {
		this.telepules = builder.telepules;
		this.kozterulet = builder.kozterulet;
		this.kozteruletJellege = builder.kozteruletJellege;
		this.hazSzam = builder.hazSzam;
		this.emelet = builder.emelet;
		this.ajto = builder.ajto;
		this.id = builder.id;
	}

	public String toString() {
		return telepules + ", " + kozterulet + " " + kozteruletJellege.irottFormaban() + " "
				+ hazSzam + ". " + (emelet.equals("") ? "" : emelet + ".em. ")
				+ (ajto.equals("") ? "" : ajto + ".ajtó ");
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ajto == null) ? 0 : ajto.hashCode());
		result = prime * result + ((emelet == null) ? 0 : emelet.hashCode());
		result = prime * result + hazSzam;
		result = prime * result
				+ ((kozterulet == null) ? 0 : kozterulet.hashCode());
		result = prime
				* result
				+ ((kozteruletJellege == null) ? 0 : kozteruletJellege
						.hashCode());
		result = prime * result
				+ ((telepules == null) ? 0 : telepules.hashCode());
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
		Cim other = (Cim) obj;
		if (ajto == null) {
			if (other.ajto != null)
				return false;
		} else if (!ajto.equals(other.ajto))
			return false;
		if (emelet == null) {
			if (other.emelet != null)
				return false;
		} else if (!emelet.equals(other.emelet))
			return false;
		if (hazSzam != other.hazSzam)
			return false;
		if (kozterulet == null) {
			if (other.kozterulet != null)
				return false;
		} else if (!kozterulet.equals(other.kozterulet))
			return false;
		if (kozteruletJellege != other.kozteruletJellege)
			return false;
		if (telepules == null) {
			if (other.telepules != null)
				return false;
		} else if (!telepules.equals(other.telepules))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Cim masik) {
		int telepulesCompareTo = telepules.compareTo(masik.telepules);
		if(telepulesCompareTo == 0) {
			int kozteruletCompareTo = kozterulet.compareTo(masik.kozterulet);
			if(kozteruletCompareTo == 0) {
				int kozteruletJellegeCompareTo = kozteruletJellege.compareTo(masik.kozteruletJellege);
				if(kozteruletJellegeCompareTo == 0) {
					int hazSzamCompareTo = hazSzam - masik.hazSzam;
					if(hazSzamCompareTo == 0) {
						int emeletCompareTo = emelet.compareTo(masik.emelet);
						if(emeletCompareTo == 0) {
							return ajto.compareTo(masik.ajto);
						}
						return emeletCompareTo;
					}
					return hazSzamCompareTo;
				}
				return kozteruletJellegeCompareTo;
			}
			return kozteruletCompareTo;
		}
		return telepulesCompareTo;
	}

	//====================Getters====================

	public Telepules getTelepules() {
		return telepules;
	}

	public String getKozterulet() {
		return kozterulet;
	}

	public KozteruletJellege getKozteruletJellege() {
		return kozteruletJellege;
	}

	public int getHazSzam() {
		return hazSzam;
	}

	public String getEmelet() {
		return emelet;
	}

	public String getAjto() {
		return ajto;
	}
	
	public int getId() {
		return id;
	}

	//====================Setters====================
	
	public void setTelepules(Telepules telepules) {
		this.telepules = telepules;
	}

	public void setKozterulet(String kozterulet) {
		this.kozterulet = kozterulet;
	}

	public void setKozteruletJellege(KozteruletJellege kozteruletJellege) {
		this.kozteruletJellege = kozteruletJellege;
	}
	
	public void setKozteruletJellege(String kozteruletJellege) {
		if(kozteruletJellege.equals("")) {
			this.kozteruletJellege = KozteruletJellege.UT;
		} else {
			kozteruletJellege = kozteruletJellege.toUpperCase();
			kozteruletJellege = Normalizer.normalize(kozteruletJellege,Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			this.kozteruletJellege = KozteruletJellege.valueOf(kozteruletJellege);
		}
	}

	public void setHazSzam(int hazSzam) {
		this.hazSzam = hazSzam;
	}

	public void setEmelet(String emelet) {
		this.emelet = emelet;
	}

	public void setAjto(String ajto) {
		this.ajto = ajto;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	//====================Builder====================
	
	public static final class Builder {

		// Kötelezõ paraméterek
		private final Telepules telepules;
		private final String kozterulet;
		private final int hazSzam;

		// Tetszõleges paraméterek
		private KozteruletJellege kozteruletJellege = KozteruletJellege.UT;
		private String emelet = "";
		private String ajto = "";
		private int id = 0;

		public Builder(Telepules telepules, String kozterulet, int hazSzam) {
			this.telepules = telepules;
			this.kozterulet = kozterulet;
			this.hazSzam = hazSzam;
		}

		public Builder kozteruletJellege(KozteruletJellege kozteruletJellege) {
			this.kozteruletJellege = kozteruletJellege;
			return this;
		}
		
		public Builder kozteruletJellege(String kozteruletJellege) {
			if(kozteruletJellege.equals("")) {
				this.kozteruletJellege = KozteruletJellege.UT;
			} else {
				kozteruletJellege = kozteruletJellege.toUpperCase();
				kozteruletJellege = Normalizer.normalize(kozteruletJellege, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				this.kozteruletJellege = KozteruletJellege.valueOf(kozteruletJellege);
			}
			return this;
		}

		public Builder emelet(String emelet) {
			this.emelet = emelet;
			return this;
		}

		public Builder ajto(String ajto) {
			this.ajto = ajto;
			return this;
		}
		
		public Builder id(int id) {
			this.id = id;
			return this;
		}

		public Cim build() {
			return new Cim(this);
		}
	}
	
}
