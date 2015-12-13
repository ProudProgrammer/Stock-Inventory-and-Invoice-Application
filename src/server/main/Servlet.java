package server.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import server.database.DataBaseOperationException;
import server.database.DataBaseOperations;

import common.entities.Beszerzes;
import common.entities.Ceg;
import common.entities.Cim;
import common.entities.Felhasznalo;
import common.entities.Keres;
import common.entities.Szamla;
import common.entities.Telepules;
import common.entities.Termek;
import common.entities.Keres.Operations;
import common.entities.Valasz;

public final class Servlet implements Runnable {
	
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private Keres keres;
	private boolean sikeresInicializalas = false;
	private Valasz valasz = new Valasz();
	
	public Servlet(Socket socket) {
		this.socket = socket;
		try {
			inputStream = new ObjectInputStream(this.socket.getInputStream());
			outputStream = new ObjectOutputStream(this.socket.getOutputStream());
			keres = (Keres)inputStream.readObject();
			sikeresInicializalas = true;
		} catch(IOException e) {
			// sikeresInicializalas = false;
			valasz.setSikeresAtvitel(false);
		} catch(ClassNotFoundException e) {
			// sikeresInicializalas = false;
			valasz.setSikeresAtvitel(false);
		} catch(ClassCastException e) {
			// sikeresInicializalas = false;
			valasz.setSikeresAtvitel(false);
		} catch(Exception e) {
			// sikeresInicializalas = false;
			valasz.setSikeresAtvitel(false);
		}
	}
	
	@Override
	public void run() {
		if (sikeresInicializalas) {
			try {
				
				// ==================Adatbázis és táblák létrehozása============
				
				if(keres.getOperation() == Operations.ADATBAZIS_ES_TABLAK_LETREHOZASA) {
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).createDataBase();
				}
				
				// ==================Felhasználó mûveletek======================
				
				else if(keres.getOperation() == Operations.FELHASZNALO_LETREHOZASA) {
					Felhasznalo felhasznalo = (Felhasznalo)keres.getParameterek()[0];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).felhasznaloLetrehozasa(felhasznalo);
				}
				else if(keres.getOperation() == Operations.FELHASZNALO_MODOSITASA) {
					String nev = (String)keres.getParameterek()[0];
					Felhasznalo uj = (Felhasznalo)keres.getParameterek()[1];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).felhasznaloModositasa(nev, uj);
				}
				else if(keres.getOperation() == Operations.FELHASZNALOK_KIVALASZTASA) {
					String nev = (String)keres.getParameterek()[0];
					List<Felhasznalo> felhasznalok = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).felhasznalokKivalasztasa(nev);
					valasz.setAdat(felhasznalok);
				}
				else if(keres.getOperation() == Operations.FELHASZNALO_KIVALASZTASA_ID_ALAPJAN) {
					String nev = (String)keres.getParameterek()[0];
					Felhasznalo felhasznalo = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).felhasznaloKivalasztasaIdAlapjan(nev);
					valasz.setAdat(felhasznalo);
				}
				else if(keres.getOperation() == Operations.FELHASZNALO_KIVALASZTASA_NEV_JELSZO_ALAPJAN) {
					Felhasznalo felhasznalo = (Felhasznalo)keres.getParameterek()[0];
					Felhasznalo felhasznaloTeljes = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).felhasznaloKivalasztasaNevJelszoAlapjan(felhasznalo);
					valasz.setAdat(felhasznaloTeljes);
				}
				else if(keres.getOperation() == Operations.FELHASZNALO_TORLESE) {
					String nev = (String)keres.getParameterek()[0];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).felhasznaloTorlese(nev);
				}
				else if(keres.getOperation() == Operations.FELHASZNALO_BEJELENTKEZES_ENGEDELYEZETT_E) {
					Felhasznalo felhasznalo = (Felhasznalo)keres.getParameterek()[0];
					Boolean engedelyezettE = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).felhasznaloBejelentkezesEngedelyezettE(felhasznalo);
					valasz.setAdat(engedelyezettE);
				}
				
				// ==================Település mûveletek======================
				
				else if(keres.getOperation() == Operations.TELEPULES_LETREHOZASA) {
					Telepules telepules = (Telepules)keres.getParameterek()[0];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).telepulesLetrehozasa(telepules);
				}
				else if(keres.getOperation() == Operations.TELEPULES_MODOSITASA) {
					Integer iranyitoSzam = (Integer)keres.getParameterek()[0];
					Telepules uj = (Telepules)keres.getParameterek()[1];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).telepulesModositasa(iranyitoSzam, uj);
				}
				else if(keres.getOperation() == Operations.TELEPULESEK_KIVALASZTASA) {
					String iranyitoSzam = (String)keres.getParameterek()[0];
					String nev = (String)keres.getParameterek()[0];
					List<Telepules> telepulesek = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).telepulesekKivalasztasa(iranyitoSzam, nev);
					valasz.setAdat(telepulesek);
				}
				else if(keres.getOperation() == Operations.TELEPULES_KIVALASZTASA_ID_ALAPJAN) {
					Integer iranyitoSzam = (Integer)keres.getParameterek()[0];
					Telepules telepules = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).telepulesKivalasztasaIdAlapjan(iranyitoSzam);
					valasz.setAdat(telepules);
				}
				else if(keres.getOperation() == Operations.TELEPULES_TORLESE) {
					Integer iranyitoSzam = (Integer)keres.getParameterek()[0];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).telepulesTorlese(iranyitoSzam);
				}
				
				// ==================Cím mûveletek==============================
				
				else if(keres.getOperation() == Operations.CIM_LETREHOZASA) {
					Cim cim = (Cim)keres.getParameterek()[0];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).cimLetrehozasa(cim);
				}
				else if(keres.getOperation() == Operations.CIM_MODOSITASA) {
					Integer id = (Integer)keres.getParameterek()[0];
					Cim uj = (Cim)keres.getParameterek()[1];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).cimModositasa(id, uj);
				}
				else if(keres.getOperation() == Operations.CIMEK_KIVALASZTASA) {
					String iranyitoSzam = (String)keres.getParameterek()[0];
					String telepulesNeve = (String)keres.getParameterek()[1];
					String kozterulet = (String)keres.getParameterek()[2];
					List<Cim> cimek = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).cimekKivalasztasa(iranyitoSzam, telepulesNeve, kozterulet);
					valasz.setAdat(cimek);
				}
				else if(keres.getOperation() == Operations.CIM_KIVALASZTASA_ID_ALAPJAN) {
					Integer id = (Integer)keres.getParameterek()[0];
					Cim cim = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).cimKivalasztasaIdAlapjan(id);
					valasz.setAdat(cim);
				}
				else if(keres.getOperation() == Operations.CIM_TORLESE) {
					Integer id = (Integer)keres.getParameterek()[0];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).cimTorlese(id);
				}
				
				// ==================Üzemeltetõ cég mûveletek===================
				
				else if(keres.getOperation() == Operations.UZEMELTETOCEG_LETREHOZASA) {
					Ceg uzemeltetoCeg = (Ceg)keres.getParameterek()[0];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).uzemeltetoCegLetrehozasa(uzemeltetoCeg);
				}
				else if(keres.getOperation() == Operations.UZEMELTETOCEG_MODOSITASA) {
					Integer uzemeltetokod = (Integer)keres.getParameterek()[0];
					Ceg ujUzemeltetoCeg = (Ceg)keres.getParameterek()[1];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).uzemeltetoCegModositasa(uzemeltetokod, ujUzemeltetoCeg);
				}
				else if(keres.getOperation() == Operations.UZEMELTETOCEGEK_KIVALASZTASA) {
					Ceg ceg = (Ceg)keres.getParameterek()[0];
					List<Ceg> uzemeltetoCegek = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).uzemeltetoCegekKivalasztasa(ceg);
					valasz.setAdat(uzemeltetoCegek);
				}
				else if(keres.getOperation() == Operations.UZEMELTETOCEG_KIVALASZTASA_ID_ALAPJAN) {
					Integer uzemeltetokod = (Integer)keres.getParameterek()[0];
					Ceg uzemeltetoCeg = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).uzemeltetoCegKivalasztasaIdAlapjan(uzemeltetokod);
					valasz.setAdat(uzemeltetoCeg);
				}
				else if(keres.getOperation() == Operations.UZEMELTETOCEG_TORLESE) {
					Integer uzemeltetokod = (Integer)keres.getParameterek()[0];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).uzemeltetoCegTorlese(uzemeltetokod);
				}
				
				// ==================Partner cég mûveletek======================
				
				else if(keres.getOperation() == Operations.PARTNERCEG_LETREHOZASA) {
					Ceg partnerCeg = (Ceg)keres.getParameterek()[0];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).partnerCegLetrehozasa(partnerCeg);
				}
				else if(keres.getOperation() == Operations.PARTNERCEG_MODOSITASA) {
					Integer partnerkod = (Integer)keres.getParameterek()[0];
					Ceg ujPartnerCeg = (Ceg)keres.getParameterek()[1];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).partnerCegModositasa(partnerkod, ujPartnerCeg);
				}
				else if(keres.getOperation() == Operations.PARTNERCEGEK_KIVALASZTASA) {
					Ceg ceg = (Ceg)keres.getParameterek()[0];
					List<Ceg> partnerCegek = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).partnerCegekKivalasztasa(ceg);
					valasz.setAdat(partnerCegek);
				}
				else if(keres.getOperation() == Operations.PARTNERCEG_KIVALASZTASA_ID_ALAPJAN) {
					Integer partnerkod = (Integer)keres.getParameterek()[0];
					Ceg partnerCeg = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).partnerCegKivalasztasaIdAlapjan(partnerkod);
					valasz.setAdat(partnerCeg);
				}
				else if(keres.getOperation() == Operations.PARTNERCEG_TORLESE) {
					Integer partnerkod = (Integer)keres.getParameterek()[0];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).partnerCegTorlese(partnerkod);
				}
				
				// ==================Termék mûveletek====================
				
				else if(keres.getOperation() == Operations.TERMEK_LETREHOZASA) {
					Termek termek = (Termek)keres.getParameterek()[0];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).termekLetrehozasa(termek);
				}
				else if(keres.getOperation() == Operations.TERMEK_MODOSITASA) {
					Integer termekkod = (Integer)keres.getParameterek()[0];
					Termek uj = (Termek)keres.getParameterek()[1];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).termekModositasa(termekkod, uj);
				}
				else if(keres.getOperation() == Operations.TERMEKEK_KIVALASZTASA) {
					Termek termek = (Termek)keres.getParameterek()[0];
					List<Termek> termekek = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).termekekKivalasztasa(termek);
					valasz.setAdat(termekek);
				}
				else if(keres.getOperation() == Operations.TERMEK_KIVALASZTASA_ID_ALAPJAN) {
					Integer termekkod = (Integer)keres.getParameterek()[0];
					Termek termek = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).termekKivalasztasaIdAlapjan(termekkod);
					valasz.setAdat(termek);
				}
				else if(keres.getOperation() == Operations.TERMEK_TORLESE) {
					Integer termekkod = (Integer)keres.getParameterek()[0];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).termekTorlese(termekkod);
				}
				
				// ==================Beszerzés mûveletek=================
				
				else if(keres.getOperation() == Operations.BESZERZES_LETREHOZASA) {
					Beszerzes beszerzes = (Beszerzes)keres.getParameterek()[0];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).beszerzesLetrehozasa(beszerzes);
				}
				else if(keres.getOperation() == Operations.BESZERZESEK_KIVALASZTASA) {
					Date datumtol = (Date)keres.getParameterek()[0];
					Date datumig = (Date)keres.getParameterek()[1];
					String uzemeltetokod = (String)keres.getParameterek()[2];
					String partnerkod = (String)keres.getParameterek()[3];
					String termekkod = (String)keres.getParameterek()[4];
					List<Beszerzes> beszerzesek = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).beszerzesekKivalasztasa(datumtol, datumig, uzemeltetokod, partnerkod, termekkod);
					valasz.setAdat(beszerzesek);
				}
				
				// ==================Számla mûveletek====================
				
				else if(keres.getOperation() == Operations.SZAMLA_LETREHOZASA) {
					Szamla szamla = (Szamla)keres.getParameterek()[0];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).szamlaLetrehozasa(szamla);
				}
				else if(keres.getOperation() == Operations.SZAMLAK_KIVALASZTASA) {
					Date datumtol = (Date)keres.getParameterek()[0];
					Date datumig = (Date)keres.getParameterek()[1];
					String uzemeltetokod = (String)keres.getParameterek()[2];
					String partnerkod = (String)keres.getParameterek()[3];
					String allapot = (String)keres.getParameterek()[4];
					List<Szamla> szamlak = DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).szamlakKivalasztasa(datumtol, datumig, uzemeltetokod, partnerkod, allapot);
					valasz.setAdat(szamlak);
				}
				else if(keres.getOperation() == Operations.SZAMLA_SZTORNOZASA) {
					Long sorszam = (Long)keres.getParameterek()[0];
					DataBaseOperations.getThreadLocalInstance(keres.getFelhasznalo()).szamlaSztornozasa(sorszam);
				}
			} catch(ClassCastException e) {
				valasz.setSikeresDataBaseOperation(false);
				valasz.setHelytelenKeres(true);
			} catch(SQLException e) {
				valasz.setSQLException(e);
				valasz.setSikeresDataBaseOperation(false);
				valasz.setVoltESQLException(true);
			} catch(DataBaseOperationException e) {
				valasz.setSikeresDataBaseOperation(false);
				valasz.setEset(e.getEset());
			} finally {
				try {
					outputStream.writeObject(valasz);
					outputStream.flush();
				} catch (IOException e) {}
				try {
					inputStream.close();
				} catch (IOException e) {}
				try {
					outputStream.close();
				} catch (IOException e) {}
				try {
					socket.close();
				} catch(IOException e) {}
			}
		}
	}
}
