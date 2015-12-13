package common.entities;

import java.io.Serializable;

public class Keres implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Operations {
		ADATBAZIS_ES_TABLAK_LETREHOZASA,
		
		FELHASZNALO_LETREHOZASA,
		FELHASZNALO_MODOSITASA,
		FELHASZNALOK_KIVALASZTASA,
		FELHASZNALO_KIVALASZTASA_ID_ALAPJAN,
		FELHASZNALO_KIVALASZTASA_NEV_JELSZO_ALAPJAN,
		FELHASZNALO_TORLESE,
		FELHASZNALO_BEJELENTKEZES_ENGEDELYEZETT_E,
		
		TELEPULES_LETREHOZASA,
		TELEPULES_MODOSITASA,
		TELEPULESEK_KIVALASZTASA,
		TELEPULES_KIVALASZTASA_ID_ALAPJAN,
		TELEPULES_TORLESE,
		
		CIM_LETREHOZASA,
		CIM_MODOSITASA,
		CIMEK_KIVALASZTASA,
		CIM_KIVALASZTASA_ID_ALAPJAN,
		CIM_TORLESE,
		
		UZEMELTETOCEG_LETREHOZASA,
		UZEMELTETOCEG_MODOSITASA,
		UZEMELTETOCEGEK_KIVALASZTASA,
		UZEMELTETOCEG_KIVALASZTASA_ID_ALAPJAN,
		UZEMELTETOCEG_TORLESE,
		
		PARTNERCEG_LETREHOZASA,
		PARTNERCEG_MODOSITASA,
		PARTNERCEGEK_KIVALASZTASA,
		PARTNERCEG_KIVALASZTASA_ID_ALAPJAN,
		PARTNERCEG_TORLESE,
		
		TERMEK_LETREHOZASA,
		TERMEK_MODOSITASA,
		TERMEKEK_KIVALASZTASA,
		TERMEK_KIVALASZTASA_ID_ALAPJAN,
		TERMEK_TORLESE,
		
		BESZERZES_LETREHOZASA,
		BESZERZESEK_KIVALASZTASA,
		
		SZAMLA_LETREHOZASA,
		SZAMLAK_KIVALASZTASA,
		SZAMLA_SZTORNOZASA;
	}
	
	private final Operations operation;
	private final Object[] parameterek;
	private final Felhasznalo felhasznalo;
	
	public Keres(Operations operation, Object[] parameterek, Felhasznalo felhasznalo) {
		this.operation = operation;
		this.parameterek = parameterek;
		this.felhasznalo = felhasznalo;
	}
	
	public Operations getOperation() {
		return operation;
	}
	
	public Object[] getParameterek() {
		return parameterek;
	}
	
	public Felhasznalo getFelhasznalo() {
		return felhasznalo;
	}
	
}
