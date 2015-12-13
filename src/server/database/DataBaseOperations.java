package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.database.DataBaseOperationException.Eset;
import server.settings.ServerProperties;

import common.entities.Beszerzes;
import common.entities.Ceg;
import common.entities.Cim;
import common.entities.Szamla;
import common.entities.Telepules;
import common.entities.Termek;
import common.entities.Felhasznalo;

/**
 * Minden tranzakció elindítása elõtt ellenõrzésre kerül a felhasználó bejelentkezésének engedélyezettsége.
 * 
 * Keresõ metódusoknál: ha parameter.equals("%"), vagy parameter.equals(""), akkor az összes rekordot adja, ahol parameter tipusa String.
 * A % jel a sztring elején, végén vagy mindkét oldalán lehet ami tetszõleges számú bármilyen karaktert reprezentál.
 * Keresõ metódusoknál: ha parameter == null, ahol a paraméter objektum, akkor az összes rekordot adja. Olyan mint String esetén a ("").
 */
public final class DataBaseOperations {
	
	private static final ThreadLocal<DataBaseOperations> threadLocalDataBaseOperations = new ThreadLocal<DataBaseOperations>();
	
	private Felhasznalo felhasznalo;
	
	private Connection connection;
	private Statement statement;
	
	private PreparedStatement preparedStatementFelhasznaloLetrehozasa;
	private PreparedStatement preparedStatementFelhasznaloModositasa;
	private PreparedStatement preparedStatementFelhasznalokKivalasztasa;
	private PreparedStatement preparedStatementFelhasznaloKivalasztasaIdAlapjan;
	private PreparedStatement preparedStatementFelhasznaloKivalasztasaNevJelszoAlapjan;
	private PreparedStatement preparedStatementFelhasznaloTorlese;
	private PreparedStatement preparedStatementFelhasznaloBejelentkezesEngedelyezettE;
	private PreparedStatement preparedStatementVanEIlyenFelhasznalo;
	
	private PreparedStatement preparedStatementTelepulesLetrehozasa;
	private PreparedStatement preparedStatementTelepulesModositasa;
	private PreparedStatement preparedStatementTelepulesekKivalasztasa;
	private PreparedStatement preparedStatementTelepulesKivalasztasaIdAlapjan;
	private PreparedStatement preparedStatementTelepulesTorlese;
	private PreparedStatement preparedStatementVanEIlyenTelepules;
	
	private PreparedStatement preparedStatementCimLetrehozasa;
	private PreparedStatement preparedStatementCimModositasa;
	private PreparedStatement preparedStatementCimekKivalasztasa;
	private PreparedStatement preparedStatementCimekKivalasztasa2;
	private PreparedStatement preparedStatementCimKivalasztasaIdAlapjan;
	private PreparedStatement preparedStatementCimTorlese;
	private PreparedStatement preparedStatementVanEIlyenCimId;
	
	private PreparedStatement preparedStatementUzemeltetoCegLetrehozasa;
	private PreparedStatement preparedStatementUzemeltetoCegModositasa;
	private PreparedStatement preparedStatementUzemeltetoCegekKivalasztasa;
	private PreparedStatement preparedStatementUzemeltetoCegekKivalasztasa2;
	private PreparedStatement preparedStatementUzemeltetoCegKivalasztasaIdAlapjan;
	private PreparedStatement preparedStatementUzemeltetoCegTorlese;
	private PreparedStatement preparedStatementVanEIlyenUzemeltetokod;
	
	private PreparedStatement preparedStatementPartnerCegLetrehozasa;
	private PreparedStatement preparedStatementPartnerCegModositasa;
	private PreparedStatement preparedStatementPartnerCegekKivalasztasa;
	private PreparedStatement preparedStatementPartnerCegekKivalasztasa2;
	private PreparedStatement preparedStatementPartnerCegKivalasztasaIdAlapjan;
	private PreparedStatement preparedStatementPartnerCegTorlese;
	private PreparedStatement preparedStatementVanEIlyenPartnerkod;
	
	private PreparedStatement preparedStatementTermekLetrehozasa;
	private PreparedStatement preparedStatementTermekModositasa;
	private PreparedStatement preparedStatementTermekekKivalasztasa;
	private PreparedStatement preparedStatementTermekekKivalasztasa2;
	private PreparedStatement preparedStatementTermekKivalasztasaIdAlapjan;
	private PreparedStatement preparedStatementTermekTorlese;
	private PreparedStatement preparedStatementVanEIlyenTermekkod;
	
	private PreparedStatement preparedStatementBeszerzesLetrehozasa;
	private PreparedStatement preparedStatementBeszerzesTermekBeallitasa;
	private PreparedStatement preparedStatementBeszerzesekKivalasztasa;
	
	private PreparedStatement preparedStatementSzamlaLetrehozasa;
	private PreparedStatement preparedStatementSzamlaErtekesitesLetrehozasa;
	private PreparedStatement preparedStatementSzamlaErtekesitesTermekBeallitasa;
	private PreparedStatement preparedStatementSzamlakKivalasztasa;
	private PreparedStatement preparedStatementSzamlakErtekesiteseiKivalasztasa;
	private PreparedStatement preparedStatementSzamlaSztornoEllenorzes;
	private PreparedStatement preparedStatementSzamlaSztornozasa;
	private PreparedStatement preparedStatementSzamlaSztornoTermekBeallitasa;
	
	private DataBaseOperations() throws SQLException {
		String address = ServerProperties.getInstance().getDataBaseAddress();
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		connection = DriverManager.getConnection("jdbc:mysql://" + address, ServerProperties.getInstance().getProperties());
		connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		connection.setAutoCommit(false);
		statement = connection.createStatement();
	}
	
	public static DataBaseOperations getThreadLocalInstance(Felhasznalo felhasznalo) throws SQLException {
		if(threadLocalDataBaseOperations.get() == null) {
			threadLocalDataBaseOperations.set(new DataBaseOperations());
		}
		threadLocalDataBaseOperations.get().felhasznalo = felhasznalo;
		return threadLocalDataBaseOperations.get();
	}
	
	// ==========Adatbázis és táblák létrehozása==============
	
	public void createDataBase() throws SQLException {
		statement.addBatch(ServerProperties.getInstance().getCreateDatabaseSQL());
		statement.addBatch(ServerProperties.getInstance().getCreateTableFelhasznalokSQL());
		statement.addBatch(ServerProperties.getInstance().getCreateTableTelepulesSQL());
		statement.addBatch(ServerProperties.getInstance().getCreateTableCimSQL());
		statement.addBatch(ServerProperties.getInstance().getCreateTableUzemeltetoCegekSQL());
		statement.addBatch(ServerProperties.getInstance().getCreateTablePartnerCegekSQL());
		statement.addBatch(ServerProperties.getInstance().getCreateTableTermekekSQL());
		statement.addBatch(ServerProperties.getInstance().getCreateTableBeszerzesekSQL());
		statement.addBatch(ServerProperties.getInstance().getCreateTableSzamlakSQL());
		statement.addBatch(ServerProperties.getInstance().getCreateTableErtekesitesekSQL());
		statement.executeBatch();
		connection.commit();
	}
	
	// ==================Felhasználó mûveletek======================
	
	public void felhasznaloLetrehozasa(Felhasznalo felhasznalo) throws SQLException, DataBaseOperationException {
		if(vanEIlyenFelhasznalo(felhasznalo.getNev()))
			throw new DataBaseOperationException("A felhasználó már szerepel az adatbázisban.\n" + felhasznalo, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		if(preparedStatementFelhasznaloLetrehozasa == null)
			preparedStatementFelhasznaloLetrehozasa = connection.prepareStatement("INSERT INTO " +
					"keszletnyilvantarto.felhasznalok VALUES(?,?,?,?,?)");
		preparedStatementFelhasznaloLetrehozasa.setString(1, felhasznalo.getNev());
		preparedStatementFelhasznaloLetrehozasa.setString(2, felhasznalo.getJelszo());
		preparedStatementFelhasznaloLetrehozasa.setString(3, felhasznalo.getRendszergazdaJog().toString());
		preparedStatementFelhasznaloLetrehozasa.setString(4, felhasznalo.getTeljesNev());
		preparedStatementFelhasznaloLetrehozasa.setString(5, felhasznalo.getTajSzam());
		preparedStatementFelhasznaloLetrehozasa.execute();
		connection.commit();
	}
	
	public void felhasznaloModositasa(Felhasznalo regi, Felhasznalo uj) throws SQLException, DataBaseOperationException {
		if(!vanEIlyenFelhasznalo(regi.getNev()))
			throw new DataBaseOperationException("Nincs ilyen felhasználó az adatbázisban.\n" + regi, Eset.NINCS_ILYEN_OBJEKTUM);
		if(vanEIlyenFelhasznalo(uj.getNev()))
			throw new DataBaseOperationException("A felhasználó már szerepel az adatbázisban.\n" + uj, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		if(preparedStatementFelhasznaloModositasa == null)
			preparedStatementFelhasznaloModositasa = connection.prepareStatement("UPDATE " +
					"keszletnyilvantarto.felhasznalok SET nev = ?, jelszo = ?, rendszergazdajog = ?,  " +
					"teljes_nev = ?, tajszam = ? WHERE nev = ?");
		preparedStatementFelhasznaloModositasa.setString(1, uj.getNev());
		preparedStatementFelhasznaloModositasa.setString(2, uj.getJelszo());
		preparedStatementFelhasznaloModositasa.setString(3, uj.getRendszergazdaJog().toString());
		preparedStatementFelhasznaloModositasa.setString(4, uj.getTeljesNev());
		preparedStatementFelhasznaloModositasa.setString(5, uj.getTajSzam());
		preparedStatementFelhasznaloModositasa.setString(6, regi.getNev());
		preparedStatementFelhasznaloModositasa.executeUpdate();
		connection.commit();
	}
	
	public void felhasznaloModositasa(String nev, Felhasznalo uj) throws SQLException, DataBaseOperationException {
		if(!vanEIlyenFelhasznalo(nev))
			throw new DataBaseOperationException("Nincs ilyen felhasználó az adatbázisban.\n" + nev, Eset.NINCS_ILYEN_OBJEKTUM);
		if(!nev.equals(uj.getNev()))
			if(vanEIlyenFelhasznalo(uj.getNev()))
				throw new DataBaseOperationException("A felhasználó már szerepel az adatbázisban.\n" + uj, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		if(preparedStatementFelhasznaloModositasa == null)
			preparedStatementFelhasznaloModositasa = connection.prepareStatement("UPDATE " +
					"keszletnyilvantarto.felhasznalok SET nev = ?, jelszo = ?, rendszergazdajog = ?,  " +
					"teljes_nev = ?, tajszam = ? WHERE nev = ?");
		preparedStatementFelhasznaloModositasa.setString(1, uj.getNev());
		preparedStatementFelhasznaloModositasa.setString(2, uj.getJelszo());
		preparedStatementFelhasznaloModositasa.setString(3, uj.getRendszergazdaJog().toString());
		preparedStatementFelhasznaloModositasa.setString(4, uj.getTeljesNev());
		preparedStatementFelhasznaloModositasa.setString(5, uj.getTajSzam());
		preparedStatementFelhasznaloModositasa.setString(6, nev);
		preparedStatementFelhasznaloModositasa.executeUpdate();
		connection.commit();
	}
	
	public List<Felhasznalo> felhasznalokKivalasztasa(String nev) throws SQLException {
		if(nev == null || nev.equals(""))
			nev = "%";
		if(preparedStatementFelhasznalokKivalasztasa == null)
			preparedStatementFelhasznalokKivalasztasa = connection.prepareStatement("SELECT * FROM " +
					"keszletnyilvantarto.felhasznalok WHERE nev LIKE ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementFelhasznalokKivalasztasa.setString(1, nev);
		ResultSet resultSet = preparedStatementFelhasznalokKivalasztasa.executeQuery();
		connection.commit();
		List<Felhasznalo> lista = new ArrayList<Felhasznalo>();
		while(resultSet.next()) {
			lista.add(new Felhasznalo.Builder(resultSet.getString("nev"), resultSet.getString("jelszo")).rendszergazdaJog(resultSet.getString("rendszergazdajog")).teljesNev(resultSet.getString("teljes_nev")).tajSzam(resultSet.getString("tajszam")).build());
		}
		return lista;
	}
	
	/**
	 * @return Felhasznalo egyed jelszo paraméter nélkül
	 */
	public Felhasznalo felhasznaloKivalasztasaIdAlapjan(String nev) throws SQLException {
		if(preparedStatementFelhasznaloKivalasztasaIdAlapjan == null)
			preparedStatementFelhasznaloKivalasztasaIdAlapjan = connection.prepareStatement("SELECT * FROM " +
					"keszletnyilvantarto.felhasznalok WHERE nev = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementFelhasznaloKivalasztasaIdAlapjan.setString(1, nev);
		ResultSet resultSet = preparedStatementFelhasznaloKivalasztasaIdAlapjan.executeQuery();
		connection.commit();
		if(resultSet.next())
			return new Felhasznalo.Builder(resultSet.getString("nev"), "").rendszergazdaJog(resultSet.getString("rendszergazdajog")).teljesNev(resultSet.getString("teljes_nev")).tajSzam(resultSet.getString("tajszam")).build();
		return null;
	}
	
	public Felhasznalo felhasznaloKivalasztasaNevJelszoAlapjan(Felhasznalo felhasznalo) throws SQLException {
		if(preparedStatementFelhasznaloKivalasztasaNevJelszoAlapjan == null)
			preparedStatementFelhasznaloKivalasztasaNevJelszoAlapjan = connection.prepareStatement("SELECT * FROM " +
					"keszletnyilvantarto.felhasznalok WHERE nev = ? AND jelszo = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementFelhasznaloKivalasztasaNevJelszoAlapjan.setString(1, felhasznalo.getNev());
		preparedStatementFelhasznaloKivalasztasaNevJelszoAlapjan.setString(2, felhasznalo.getJelszo());
		ResultSet resultSet = preparedStatementFelhasznaloKivalasztasaNevJelszoAlapjan.executeQuery();
		connection.commit();
		if(resultSet.next()) {
			if(felhasznalo.getNev().equals(resultSet.getString("nev")) & 
					felhasznalo.getJelszo().equals(resultSet.getString("jelszo")))
			return new Felhasznalo.Builder(resultSet.getString("nev"), resultSet.getString("jelszo")).rendszergazdaJog(resultSet.getString("rendszergazdajog")).teljesNev(resultSet.getString("teljes_nev")).tajSzam(resultSet.getString("tajszam")).build();
		}
		return null;
	}
	
	public void felhasznaloTorlese(Felhasznalo felhasznalo) throws SQLException, DataBaseOperationException {
		if(!vanEIlyenFelhasznalo(felhasznalo.getNev()))
			throw new DataBaseOperationException("Nincs ilyen felhasználó az adatbázisban.\n" + felhasznalo, Eset.NINCS_ILYEN_OBJEKTUM);
		if(preparedStatementFelhasznaloTorlese == null)
			preparedStatementFelhasznaloTorlese = connection.prepareStatement("DELETE FROM " +
					"keszletnyilvantarto.felhasznalok WHERE nev = ?");
		preparedStatementFelhasznaloTorlese.setString(1, felhasznalo.getNev());
		preparedStatementFelhasznaloTorlese.execute();
		connection.commit();
	}
	
	public void felhasznaloTorlese(String nev) throws SQLException, DataBaseOperationException {
		if(!vanEIlyenFelhasznalo(nev))
			throw new DataBaseOperationException("Nincs ilyen felhasználó az adatbázisban.\n" + nev, Eset.NINCS_ILYEN_OBJEKTUM);
		if(preparedStatementFelhasznaloTorlese == null)
			preparedStatementFelhasznaloTorlese = connection.prepareStatement("DELETE FROM " +
					"keszletnyilvantarto.felhasznalok WHERE nev = ?");
		preparedStatementFelhasznaloTorlese.setString(1, nev);
		preparedStatementFelhasznaloTorlese.execute();
		connection.commit();
	}
	
	public boolean felhasznaloBejelentkezesEngedelyezettE(Felhasznalo felhasznalo) throws SQLException {
		if(preparedStatementFelhasznaloBejelentkezesEngedelyezettE == null)
			preparedStatementFelhasznaloBejelentkezesEngedelyezettE = connection.prepareStatement("SELECT * FROM " +
					"keszletnyilvantarto.felhasznalok WHERE nev = ? AND jelszo = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementFelhasznaloBejelentkezesEngedelyezettE.setString(1, felhasznalo.getNev());
		preparedStatementFelhasznaloBejelentkezesEngedelyezettE.setString(2, felhasznalo.getJelszo());
		ResultSet resultSet = preparedStatementFelhasznaloBejelentkezesEngedelyezettE.executeQuery();
		connection.commit();
		while(resultSet.next()) {
			if(felhasznalo.getNev().equals(resultSet.getString("nev")) & 
					felhasznalo.getJelszo().equals(resultSet.getString("jelszo")))
				return true;
		}
		return false;
	}
	
	private boolean vanEIlyenFelhasznalo(String nev) throws SQLException {
		if(preparedStatementVanEIlyenFelhasznalo == null)
			preparedStatementVanEIlyenFelhasznalo = connection.prepareStatement("SELECT " +
					"count(*) darab FROM keszletnyilvantarto.felhasznalok WHERE nev = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementVanEIlyenFelhasznalo.setString(1, nev);
		ResultSet resultSet = preparedStatementVanEIlyenFelhasznalo.executeQuery();
		connection.commit();
		int darab = 0;
		while(resultSet.next())
			darab = resultSet.getInt("darab");
		if(darab == 0)
			return false;
		return true;
	}
	
	// ==================Település mûveletek======================
	
	public void telepulesLetrehozasa(Telepules telepules) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(vanEIlyenTelepules(telepules.getIranyitoSzam()))	
			throw new DataBaseOperationException("A település már szerepel az adatbázisban.\n" + telepules, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		if(preparedStatementTelepulesLetrehozasa == null)
			preparedStatementTelepulesLetrehozasa = connection.prepareStatement("INSERT INTO " +
					"keszletnyilvantarto.telepules VALUES(?,?)");
		preparedStatementTelepulesLetrehozasa.setInt(1, telepules.getIranyitoSzam());
		preparedStatementTelepulesLetrehozasa.setString(2, telepules.getNev());
		preparedStatementTelepulesLetrehozasa.execute();
		connection.commit();
	}
	
	public void telepulesModositasa(int iranyitoSzam, Telepules uj) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(!vanEIlyenTelepules(iranyitoSzam))
			throw new DataBaseOperationException("Nincs ilyen irányítószámú település az adatbázisban.\n" + iranyitoSzam, Eset.NINCS_ILYEN_OBJEKTUM);
		if(vanEIlyenTelepules(uj.getIranyitoSzam()))
			throw new DataBaseOperationException("A település már szerepel az adatbázisban.\n" + uj, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		if(preparedStatementTelepulesModositasa == null)
			preparedStatementTelepulesModositasa = connection.prepareStatement("UPDATE " +
					"keszletnyilvantarto.telepules SET iranyitoszam = ?, nev = ? WHERE iranyitoszam = ?");
		preparedStatementTelepulesModositasa.setInt(1, uj.getIranyitoSzam());
		preparedStatementTelepulesModositasa.setString(2, uj.getNev());
		preparedStatementTelepulesModositasa.setInt(3, iranyitoSzam);
		preparedStatementTelepulesModositasa.executeUpdate();
		connection.commit();
	}
	
	public List<Telepules> telepulesekKivalasztasa(String iranyitoSzam, String nev) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(iranyitoSzam == null || iranyitoSzam.equals(""))
			iranyitoSzam = "%";
		if(nev == null || nev.equals(""))
			nev = "%";
		if(preparedStatementTelepulesekKivalasztasa == null)
			preparedStatementTelepulesekKivalasztasa = connection.prepareStatement("SELECT * FROM " +
					"keszletnyilvantarto.telepules WHERE iranyitoszam LIKE ? AND nev LIKE ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementTelepulesekKivalasztasa.setString(1, iranyitoSzam);
		preparedStatementTelepulesekKivalasztasa.setString(2, nev);
		ResultSet resultSet = preparedStatementTelepulesekKivalasztasa.executeQuery();
		connection.commit();
		List<Telepules> lista = new ArrayList<Telepules>();
		while(resultSet.next())
			lista.add(new Telepules(resultSet.getInt("iranyitoszam"), resultSet.getString("nev")));
		return lista;
	}
	
	public Telepules telepulesKivalasztasaIdAlapjan(int iranyitoSzam) throws SQLException {
		if(preparedStatementTelepulesKivalasztasaIdAlapjan == null)
			preparedStatementTelepulesKivalasztasaIdAlapjan = connection.prepareStatement("SELECT * FROM " +
					"keszletnyilvantarto.telepules WHERE iranyitoszam = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementTelepulesKivalasztasaIdAlapjan.setInt(1, iranyitoSzam);
		ResultSet resultSet = preparedStatementTelepulesKivalasztasaIdAlapjan.executeQuery();
		connection.commit();
		if(resultSet.next())
			return new Telepules(resultSet.getInt("iranyitoszam"), resultSet.getString("nev"));
		return null;
	}
	
	public void telepulesTorlese(int iranyitoSzam) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(!vanEIlyenTelepules(iranyitoSzam))
			throw new DataBaseOperationException("Nincs ilyen irányítószámú település az adatbázisban.\n" + iranyitoSzam, Eset.NINCS_ILYEN_OBJEKTUM);
		if(preparedStatementTelepulesTorlese == null)
			preparedStatementTelepulesTorlese = connection.prepareStatement("DELETE FROM " +
					"keszletnyilvantarto.telepules WHERE iranyitoszam = ?");
		preparedStatementTelepulesTorlese.setInt(1, iranyitoSzam);
		preparedStatementTelepulesTorlese.execute();
		connection.commit();
	}
	
	private boolean vanEIlyenTelepules(int iranyitoSzam) throws SQLException {
		if(preparedStatementVanEIlyenTelepules == null)
			preparedStatementVanEIlyenTelepules = connection.prepareStatement("SELECT " +
					"count(*) darab FROM keszletnyilvantarto.telepules WHERE iranyitoszam = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementVanEIlyenTelepules.setInt(1, iranyitoSzam);
		ResultSet resultSet = preparedStatementVanEIlyenTelepules.executeQuery();
		connection.commit();
		int darab = 0;
		while(resultSet.next())
			darab = resultSet.getInt("darab");
		if(darab == 0)
			return false;
		return true;
	}
	
	// ==================Cím mûveletek==============================
	
	public void cimLetrehozasa(Cim cim) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		try {
			telepulesLetrehozasa(cim.getTelepules());
		} catch (DataBaseOperationException e) {
			//A település már létezik
		}
		if(ilyenCimekIdSzamai(cim).size() > 0)
			throw new DataBaseOperationException("A cím már szerepel az adatbázisban.\n" + cim, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		if(preparedStatementCimLetrehozasa == null)
			preparedStatementCimLetrehozasa = connection.prepareStatement("INSERT INTO " +
					"keszletnyilvantarto.cim (telepules, kozterulet, kozterulet_jellege, " +
					"hazszam, emelet, ajto) VALUES(?,?,?,?,?,?)");
		preparedStatementCimLetrehozasa.setInt(1, cim.getTelepules().getIranyitoSzam());
		preparedStatementCimLetrehozasa.setString(2, cim.getKozterulet());
		preparedStatementCimLetrehozasa.setString(3, cim.getKozteruletJellege().toString());
		preparedStatementCimLetrehozasa.setInt(4, cim.getHazSzam());
		preparedStatementCimLetrehozasa.setString(5, cim.getEmelet());
		preparedStatementCimLetrehozasa.setString(6, cim.getAjto());
		preparedStatementCimLetrehozasa.execute();
		connection.commit();
	}
	
	public int cimModositasa(Cim regi, Cim uj) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		int modositottRekordokSzama = 0;
		List<Integer> regiCimmelEgyezoCimekIdSzamai = ilyenCimekIdSzamai(regi);
		if(regiCimmelEgyezoCimekIdSzamai.size() == 0)
			throw new DataBaseOperationException("Nincs ilyen cím az adatbázisban.\n" + regi, Eset.NINCS_ILYEN_OBJEKTUM);
		if(ilyenCimekIdSzamai(uj).size() > 0)
			throw new DataBaseOperationException("A cím már szerepel az adatbázisban.\n" + uj, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		try {
			telepulesLetrehozasa(uj.getTelepules());
		} catch (DataBaseOperationException e) {
			//A település már létezik
		}
		if(preparedStatementCimModositasa == null)
			preparedStatementCimModositasa = connection.prepareStatement("UPDATE " +
					"keszletnyilvantarto.cim SET telepules = ?, kozterulet = ?, " +
					"kozterulet_jellege = ?, hazszam = ?, emelet = ?, ajto = ? " +
					"WHERE id = ?");
		for(Integer id : regiCimmelEgyezoCimekIdSzamai) {
			preparedStatementCimModositasa.setInt(1, uj.getTelepules().getIranyitoSzam());
			preparedStatementCimModositasa.setString(2, uj.getKozterulet());
			preparedStatementCimModositasa.setString(3, uj.getKozteruletJellege().toString());
			preparedStatementCimModositasa.setInt(4, uj.getHazSzam());
			preparedStatementCimModositasa.setString(5, uj.getEmelet());
			preparedStatementCimModositasa.setString(6, uj.getAjto());
			preparedStatementCimModositasa.setInt(7, id);
			preparedStatementCimModositasa.executeUpdate();
			connection.commit();
			modositottRekordokSzama++;
		}
		return modositottRekordokSzama;
	}
	
	public void cimModositasa(int id, Cim uj) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(!vanEIlyenCimId(id))
			throw new DataBaseOperationException("Nincs ilyen cím id az adatbázisban: " + id, Eset.NINCS_ILYEN_OBJEKTUM);
		if(ilyenCimekIdSzamai(uj).size() > 0)
			throw new DataBaseOperationException("A cím már szerepel az adatbázisban.\n" + uj, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		try {
			telepulesLetrehozasa(uj.getTelepules());
		} catch (DataBaseOperationException e) {
			//A település már létezik
		}
		if(preparedStatementCimModositasa == null)
			preparedStatementCimModositasa = connection.prepareStatement("UPDATE " +
					"keszletnyilvantarto.cim SET telepules = ?, kozterulet = ?, " +
					"kozterulet_jellege = ?, hazszam = ?, emelet = ?, ajto = ? " +
					"WHERE id = ?");
		preparedStatementCimModositasa.setInt(1, uj.getTelepules().getIranyitoSzam());
		preparedStatementCimModositasa.setString(2, uj.getKozterulet());
		preparedStatementCimModositasa.setString(3, uj.getKozteruletJellege().toString());
		preparedStatementCimModositasa.setInt(4, uj.getHazSzam());
		preparedStatementCimModositasa.setString(5, uj.getEmelet());
		preparedStatementCimModositasa.setString(6, uj.getAjto());
		preparedStatementCimModositasa.setInt(7, id);
		preparedStatementCimModositasa.executeUpdate();
		connection.commit();
	}
	
	public List<Cim> cimekKivalasztasa(String iranyitoSzam, String telepulesNeve, String kozterulet) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(iranyitoSzam == null || iranyitoSzam.equals(""))
			iranyitoSzam = "%";
		if(telepulesNeve == null || telepulesNeve.equals(""))
			telepulesNeve = "%";
		if(kozterulet == null || kozterulet.equals(""))
			kozterulet = "%";
		if(preparedStatementCimekKivalasztasa == null)
			preparedStatementCimekKivalasztasa = connection.prepareStatement("SELECT " +
					"c.id, t.iranyitoszam, t.nev telepules, c.kozterulet, c.kozterulet_jellege, c.hazszam, c.emelet, c.ajto " +
					"FROM keszletnyilvantarto.cim c, keszletnyilvantarto.telepules t WHERE " +
					"c.telepules = t.iranyitoszam AND t.iranyitoszam LIKE ? AND t.nev LIKE ? AND c.kozterulet LIKE ? order by t.nev", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementCimekKivalasztasa.setString(1, iranyitoSzam);
		preparedStatementCimekKivalasztasa.setString(2, telepulesNeve);
		preparedStatementCimekKivalasztasa.setString(3, kozterulet);
		ResultSet resultSet = preparedStatementCimekKivalasztasa.executeQuery();
		connection.commit();
		List<Cim> cimek = new ArrayList<Cim>();
		while(resultSet.next()) {
			Telepules telepules = new Telepules(resultSet.getInt("iranyitoszam"), resultSet.getString("telepules"));
			cimek.add(new Cim.Builder(telepules, resultSet.getString("kozterulet"), resultSet.getInt("hazszam")).kozteruletJellege(resultSet.getString("kozterulet_jellege")).emelet(resultSet.getString("emelet")).ajto(resultSet.getString("ajto")).id(resultSet.getInt("id")).build());
		}
		return cimek;
	}
	
	public Cim cimKivalasztasaIdAlapjan(int id) throws SQLException {
		if(preparedStatementCimKivalasztasaIdAlapjan == null)
			preparedStatementCimKivalasztasaIdAlapjan = connection.prepareStatement("SELECT * FROM " +
					"keszletnyilvantarto.cim WHERE id = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementCimKivalasztasaIdAlapjan.setInt(1, id);
		ResultSet resultSet = preparedStatementCimKivalasztasaIdAlapjan.executeQuery();
		connection.commit();
		if(resultSet.next()) {
			Telepules telepules = telepulesKivalasztasaIdAlapjan(resultSet.getInt("telepules"));
			if(telepules != null)
				return new Cim.Builder(telepules, resultSet.getString("kozterulet"), resultSet.getInt("hazszam")).kozteruletJellege(resultSet.getString("kozterulet_jellege")).emelet(resultSet.getString("emelet")).ajto(resultSet.getString("ajto")).id(id).build();
		}
		return null;
	}
	
	public int cimTorlese(Cim cim) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		int toroltRekordokSzama = 0;
		List<Integer> cimmelEgyezoCimekIdSzamai = ilyenCimekIdSzamai(cim);
		if(cimmelEgyezoCimekIdSzamai.size() == 0)
			throw new DataBaseOperationException("Nincs ilyen cím az adatbázisban.\n" + cim, Eset.NINCS_ILYEN_OBJEKTUM);
		if(preparedStatementCimTorlese == null)
			preparedStatementCimTorlese = connection.prepareStatement("DELETE FROM " +
					"keszletnyilvantarto.cim WHERE id = ?");
		for(Integer id : cimmelEgyezoCimekIdSzamai) {
			preparedStatementCimTorlese.setInt(1, id);
			preparedStatementCimTorlese.execute();
			connection.commit();
			toroltRekordokSzama++;
		}
		return toroltRekordokSzama;
	}
	
	public void cimTorlese(int id) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(!vanEIlyenCimId(id))
			throw new DataBaseOperationException("Nincs ilyen cím id az adatbázisban: " + id, Eset.NINCS_ILYEN_OBJEKTUM);
		if(preparedStatementCimTorlese == null)
			preparedStatementCimTorlese = connection.prepareStatement("DELETE FROM " +
					"keszletnyilvantarto.cim WHERE id = ?");
		preparedStatementCimTorlese.setInt(1, id);
		preparedStatementCimTorlese.execute();
		connection.commit();
	}
	
	private List<Cim> cimekKivalasztasa(Cim cim) throws SQLException {
		if(preparedStatementCimekKivalasztasa2 == null)
			preparedStatementCimekKivalasztasa2 = connection.prepareStatement("SELECT " +
					"c.id, t.iranyitoszam, t.nev telepules, c.kozterulet, c.kozterulet_jellege, c.hazszam, c.emelet, c.ajto " +
					"FROM keszletnyilvantarto.cim c, keszletnyilvantarto.telepules t WHERE " +
					"c.telepules = t.iranyitoszam AND t.iranyitoszam LIKE ? AND c.kozterulet LIKE ? " +
					"AND c.kozterulet_jellege LIKE ? AND c.hazszam LIKE ? AND c.emelet LIKE ? AND c.ajto LIKE ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementCimekKivalasztasa2.setInt(1, cim.getTelepules().getIranyitoSzam());
		preparedStatementCimekKivalasztasa2.setString(2, cim.getKozterulet());
		preparedStatementCimekKivalasztasa2.setString(3, cim.getKozteruletJellege().toString());
		preparedStatementCimekKivalasztasa2.setInt(4, cim.getHazSzam());
		preparedStatementCimekKivalasztasa2.setString(5, cim.getEmelet().equals("") ? "%" : cim.getEmelet());
		preparedStatementCimekKivalasztasa2.setString(6, cim.getAjto().equals("") ? "%" : cim.getAjto());
		ResultSet resultSet = preparedStatementCimekKivalasztasa2.executeQuery();
		connection.commit();
		List<Cim> cimek = new ArrayList<Cim>();
		while(resultSet.next()) {
			Telepules telepules = new Telepules(resultSet.getInt("iranyitoszam"), resultSet.getString("telepules"));
			cimek.add(new Cim.Builder(telepules, resultSet.getString("kozterulet"), resultSet.getInt("hazszam")).kozteruletJellege(resultSet.getString("kozterulet_jellege")).emelet(resultSet.getString("emelet")).ajto(resultSet.getString("ajto")).id(resultSet.getInt("id")).build());
		}
		return cimek;
	}
	
	private boolean vanEIlyenCimId(int id) throws SQLException {
		if(preparedStatementVanEIlyenCimId == null)
			preparedStatementVanEIlyenCimId = connection.prepareStatement("SELECT " +
					"count(*) darab FROM keszletnyilvantarto.cim WHERE id = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementVanEIlyenCimId.setInt(1, id);
		ResultSet resultSet = preparedStatementVanEIlyenCimId.executeQuery();
		connection.commit();
		int darab = 0;
		while(resultSet.next())
			darab = resultSet.getInt("darab");
		if(darab == 0)
			return false;
		return true;
	}
	
	private List<Integer> ilyenCimekIdSzamai(Cim cim) throws SQLException {
		List<Integer> idSzamok = new ArrayList<Integer>();
		for(Cim lekerdezettCim : cimekKivalasztasa(cim))
			if(cim.equals(lekerdezettCim))
				idSzamok.add(lekerdezettCim.getId());
		return idSzamok;
	}
	
	// ==================Üzemeltetõ cég mûveletek===================
	
	public void uzemeltetoCegLetrehozasa(Ceg uzemeltetoCeg) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(ilyenUzemeltetoCegekIdSzamai(uzemeltetoCeg).size() > 0)
			throw new DataBaseOperationException("Az üzemeltetõ cég már szerepel az adatbázisban.\n" + uzemeltetoCeg, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		int cimId = hozdLetreACimetVagyOlvasdKiEsTerjVisszaACimIdval(uzemeltetoCeg.getCim());
		if(preparedStatementUzemeltetoCegLetrehozasa == null)
			preparedStatementUzemeltetoCegLetrehozasa = connection.prepareStatement("INSERT INTO " +
					"keszletnyilvantarto.uzemeltetocegek (nev, cim, adoszam, telefon, fax, email, webcim) " +
					"VALUES (?,?,?,?,?,?,?)");
		preparedStatementUzemeltetoCegLetrehozasa.setString(1, uzemeltetoCeg.getNev());
		preparedStatementUzemeltetoCegLetrehozasa.setInt(2, cimId);
		preparedStatementUzemeltetoCegLetrehozasa.setLong(3, uzemeltetoCeg.getAdoszam());
		preparedStatementUzemeltetoCegLetrehozasa.setString(4, uzemeltetoCeg.getTelefon());
		preparedStatementUzemeltetoCegLetrehozasa.setString(5, uzemeltetoCeg.getFax());
		preparedStatementUzemeltetoCegLetrehozasa.setString(6, uzemeltetoCeg.getEmail());
		preparedStatementUzemeltetoCegLetrehozasa.setString(7, uzemeltetoCeg.getWebcim());
		preparedStatementUzemeltetoCegLetrehozasa.execute();
		connection.commit();
	}

	public int uzemeltetoCegModositasa(Ceg regiUzemeltetoCeg, Ceg ujUzemeltetoCeg) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		int modositottRekordokSzama = 0;
		List<Integer> regiUzemeltetoCeggelEgyezoUzemeltetoCegekIdSzamai = ilyenUzemeltetoCegekIdSzamai(regiUzemeltetoCeg);
		List<Integer> ujUzemeltetoCeggelEgyezoUzemeltetoCegekIdSzamai = ilyenUzemeltetoCegekIdSzamai(ujUzemeltetoCeg);
		if(regiUzemeltetoCeggelEgyezoUzemeltetoCegekIdSzamai.size() == 0)
			throw new DataBaseOperationException("Nincs ilyen üzemeltetõ cég az adatbázisban.\n" + regiUzemeltetoCeg, Eset.NINCS_ILYEN_OBJEKTUM);
		if(ujUzemeltetoCeggelEgyezoUzemeltetoCegekIdSzamai.size() > 0)
			throw new DataBaseOperationException("Az üzemeltetõ cég már szerepel az adatbázisban.\n" + ujUzemeltetoCeg, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		int cimId = hozdLetreACimetVagyOlvasdKiEsTerjVisszaACimIdval(ujUzemeltetoCeg.getCim());
		if(preparedStatementUzemeltetoCegModositasa == null)
			preparedStatementUzemeltetoCegModositasa = connection.prepareStatement("UPDATE " +
					"keszletnyilvantarto.uzemeltetocegek SET nev = ?, cim = ?, adoszam = ?, " +
					"telefon = ?, fax = ?, email = ?, webcim = ? WHERE uzemeltetokod = ?");
		for(Integer uzemeltetokod : regiUzemeltetoCeggelEgyezoUzemeltetoCegekIdSzamai) {
			preparedStatementUzemeltetoCegModositasa.setString(1, ujUzemeltetoCeg.getNev());
			preparedStatementUzemeltetoCegModositasa.setInt(2, cimId);
			preparedStatementUzemeltetoCegModositasa.setLong(3, ujUzemeltetoCeg.getAdoszam());
			preparedStatementUzemeltetoCegModositasa.setString(4, ujUzemeltetoCeg.getTelefon());
			preparedStatementUzemeltetoCegModositasa.setString(5, ujUzemeltetoCeg.getFax());
			preparedStatementUzemeltetoCegModositasa.setString(6, ujUzemeltetoCeg.getEmail());
			preparedStatementUzemeltetoCegModositasa.setString(7, ujUzemeltetoCeg.getWebcim());
			preparedStatementUzemeltetoCegModositasa.setInt(8, uzemeltetokod);
			preparedStatementUzemeltetoCegModositasa.executeUpdate();
			connection.commit();
			modositottRekordokSzama++;
		}
		return modositottRekordokSzama;
	}
	
	public void uzemeltetoCegModositasa(int uzemeltetokod, Ceg ujUzemeltetoCeg) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		List<Integer> ujUzemeltetoCeggelEgyezoUzemeltetoCegekIdSzamai = ilyenUzemeltetoCegekIdSzamai(ujUzemeltetoCeg);
		if(!vanEIlyenUzemeltetokod(uzemeltetokod))
			throw new DataBaseOperationException("Nincs ilyen üzemeltetõkód az adatbázisban: " + uzemeltetokod, Eset.NINCS_ILYEN_OBJEKTUM);
		boolean egyezikEAzUzemeltetokod = false;
		if(ujUzemeltetoCeggelEgyezoUzemeltetoCegekIdSzamai.size() > 0) {
			for(Integer id : ujUzemeltetoCeggelEgyezoUzemeltetoCegekIdSzamai)
				if(uzemeltetokod == id)
					egyezikEAzUzemeltetokod = true;
			if(!egyezikEAzUzemeltetokod)
				throw new DataBaseOperationException("Az üzemeltetõ cég már szerepel az adatbázisban.\n" + ujUzemeltetoCeg, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		}
		int cimId = hozdLetreACimetVagyOlvasdKiEsTerjVisszaACimIdval(ujUzemeltetoCeg.getCim());
		if(preparedStatementUzemeltetoCegModositasa == null)
			preparedStatementUzemeltetoCegModositasa = connection.prepareStatement("UPDATE " +
					"keszletnyilvantarto.uzemeltetocegek SET nev = ?, cim = ?, adoszam = ?, " +
					"telefon = ?, fax = ?, email = ?, webcim = ? WHERE uzemeltetokod = ?");
		preparedStatementUzemeltetoCegModositasa.setString(1, ujUzemeltetoCeg.getNev());
		preparedStatementUzemeltetoCegModositasa.setInt(2, cimId);
		preparedStatementUzemeltetoCegModositasa.setLong(3, ujUzemeltetoCeg.getAdoszam());
		preparedStatementUzemeltetoCegModositasa.setString(4, ujUzemeltetoCeg.getTelefon());
		preparedStatementUzemeltetoCegModositasa.setString(5, ujUzemeltetoCeg.getFax());
		preparedStatementUzemeltetoCegModositasa.setString(6, ujUzemeltetoCeg.getEmail());
		preparedStatementUzemeltetoCegModositasa.setString(7, ujUzemeltetoCeg.getWebcim());
		preparedStatementUzemeltetoCegModositasa.setInt(8, uzemeltetokod);
		preparedStatementUzemeltetoCegModositasa.executeUpdate();
		connection.commit();
	}
	
	public List<Ceg> uzemeltetoCegekKivalasztasa(String nev, Cim cim, String adoszam, String telefon, 
			String fax, String email, String webcim) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(nev == null || nev.equals(""))
			nev = "%";
		if(adoszam == null || adoszam.equals(""))
			adoszam = "%";
		if(telefon == null || telefon.equals(""))
			telefon = "%";
		if(fax == null || fax.equals(""))
			fax = "%";
		if(email == null || email.equals(""))
			email = "%";
		if(webcim == null || webcim.equals(""))
			webcim = "%";
		int cimId = 0;
		if(cim != null)
			for(Cim lekerdezettCim : cimekKivalasztasa(cim))
				if(cim.equals(lekerdezettCim))
					cimId = lekerdezettCim.getId();
		if(preparedStatementUzemeltetoCegekKivalasztasa == null)
			preparedStatementUzemeltetoCegekKivalasztasa = connection.prepareStatement("SELECT " +
					"u.uzemeltetokod, u.nev, c.id cim_id, t.iranyitoszam, t.nev telepules, c.kozterulet, " +
					"c.kozterulet_jellege, c.hazszam, c.emelet, c.ajto, u.adoszam, u.telefon, u.fax, " +
					"u.email, u.webcim FROM keszletnyilvantarto.uzemeltetocegek u, keszletnyilvantarto.cim c, " +
					"keszletnyilvantarto.telepules t WHERE u.cim = c.id AND c.telepules = t.iranyitoszam " +
					"AND u.nev LIKE ? AND c.id LIKE ? AND u.adoszam LIKE ? AND u.telefon LIKE ? AND u.fax LIKE ? " +
					"AND u.email LIKE ? AND u.webcim LIKE ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementUzemeltetoCegekKivalasztasa.setString(1, nev);
		preparedStatementUzemeltetoCegekKivalasztasa.setString(2, cimId == 0 ? "%" : Integer.toString(cimId));
		preparedStatementUzemeltetoCegekKivalasztasa.setString(3, adoszam);
		preparedStatementUzemeltetoCegekKivalasztasa.setString(4, telefon);
		preparedStatementUzemeltetoCegekKivalasztasa.setString(5, fax);
		preparedStatementUzemeltetoCegekKivalasztasa.setString(6, email);
		preparedStatementUzemeltetoCegekKivalasztasa.setString(7, webcim);
		ResultSet resultSet = preparedStatementUzemeltetoCegekKivalasztasa.executeQuery();
		connection.commit();
		List<Ceg> cegek = new ArrayList<Ceg>();
		while(resultSet.next()) {
			Telepules telepules = new Telepules(resultSet.getInt("iranyitoszam"), resultSet.getString("telepules"));
			Cim cim2 = new Cim.Builder(telepules, resultSet.getString("kozterulet"), resultSet.getInt("hazszam")).kozteruletJellege(resultSet.getString("kozterulet_jellege")).emelet(resultSet.getString("emelet")).ajto(resultSet.getString("ajto")).id(resultSet.getInt("cim_id")).build();
			cegek.add(new Ceg.Builder(resultSet.getString("nev"), cim2).adoszam(resultSet.getLong("adoszam")).telefon(resultSet.getString("telefon")).fax(resultSet.getString("fax")).email(resultSet.getString("email")).webcim(resultSet.getString("webcim")).id(resultSet.getInt("uzemeltetokod")).build());
		}
		return cegek;
	}
	
	public Ceg uzemeltetoCegKivalasztasaIdAlapjan(int uzemeltetokod) throws SQLException {
		if(preparedStatementUzemeltetoCegKivalasztasaIdAlapjan == null)
			preparedStatementUzemeltetoCegKivalasztasaIdAlapjan = connection.prepareStatement("SELECT * FROM " +
					"keszletnyilvantarto.uzemeltetocegek WHERE uzemeltetokod = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementUzemeltetoCegKivalasztasaIdAlapjan.setInt(1, uzemeltetokod);
		ResultSet resultSet = preparedStatementUzemeltetoCegKivalasztasaIdAlapjan.executeQuery();
		connection.commit();
		if(resultSet.next()) {
			Cim cim = cimKivalasztasaIdAlapjan(resultSet.getInt("cim"));
			if(cim != null)
				return new Ceg.Builder(resultSet.getString("nev"), cim).adoszam(resultSet.getLong("adoszam")).telefon(resultSet.getString("telefon")).fax(resultSet.getString("fax")).email(resultSet.getString("email")).webcim(resultSet.getString("webcim")).id(uzemeltetokod).build();
		}
		return null;
	}
	
	public int uzemeltetoCegTorlese(Ceg uzemeltetoCeg) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		int toroltRekordokSzama = 0;
		List<Integer> uzemeltetoCeggelEgyezoUzemeltetoCegekIdSzamai = ilyenUzemeltetoCegekIdSzamai(uzemeltetoCeg);
		if(uzemeltetoCeggelEgyezoUzemeltetoCegekIdSzamai.size() == 0)
			throw new DataBaseOperationException("Nincs ilyen üzemeltetõ cég az adatbázisban.\n" + uzemeltetoCeg, Eset.NINCS_ILYEN_OBJEKTUM);
		if(preparedStatementUzemeltetoCegTorlese == null)
			preparedStatementUzemeltetoCegTorlese = connection.prepareStatement("DELETE FROM " +
					"keszletnyilvantarto.uzemeltetocegek WHERE uzemeltetokod = ?");
		for(Integer id : uzemeltetoCeggelEgyezoUzemeltetoCegekIdSzamai) {
			preparedStatementUzemeltetoCegTorlese.setInt(1, id);
			preparedStatementUzemeltetoCegTorlese.execute();
			connection.commit();
			toroltRekordokSzama++;
		}
		return toroltRekordokSzama;
	}
	
	public void uzemeltetoCegTorlese(int uzemeltetokod) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(!vanEIlyenUzemeltetokod(uzemeltetokod))
			throw new DataBaseOperationException("Nincs ilyen üzemeltetõkód az adatbázisban: " + uzemeltetokod, Eset.NINCS_ILYEN_OBJEKTUM);
		if(preparedStatementUzemeltetoCegTorlese == null)
			preparedStatementUzemeltetoCegTorlese = connection.prepareStatement("DELETE FROM " +
					"keszletnyilvantarto.uzemeltetocegek WHERE uzemeltetokod = ?");
		preparedStatementUzemeltetoCegTorlese.setInt(1, uzemeltetokod);
		preparedStatementUzemeltetoCegTorlese.execute();
		connection.commit();
	}
	
	public List<Ceg> uzemeltetoCegekKivalasztasa(Ceg uzemeltetoCeg) throws SQLException {
		if(preparedStatementUzemeltetoCegekKivalasztasa2 == null)
			preparedStatementUzemeltetoCegekKivalasztasa2 = connection.prepareStatement("SELECT " +
					"u.uzemeltetokod, u.nev, c.id cim_id, t.iranyitoszam, t.nev telepules, c.kozterulet, " +
					"c.kozterulet_jellege, c.hazszam, c.emelet, c.ajto, u.adoszam, u.telefon, u.fax, " +
					"u.email, u.webcim FROM keszletnyilvantarto.uzemeltetocegek u, keszletnyilvantarto.cim c, " +
					"keszletnyilvantarto.telepules t WHERE u.cim = c.id AND c.telepules = t.iranyitoszam " +
					"AND u.uzemeltetokod LIKE ? AND u.nev LIKE ? AND t.iranyitoszam LIKE ? AND t.nev LIKE ? " +
					"AND c.kozterulet LIKE ? AND c.hazszam LIKE ? AND c.emelet LIKE ? AND c.ajto " +
					"LIKE ? AND u.adoszam LIKE ? AND u.telefon LIKE ? AND u.fax LIKE ? AND u.email LIKE ? " +
					"AND u.webcim LIKE ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementUzemeltetoCegekKivalasztasa2.setString(1, (uzemeltetoCeg.getId() == 0) ? "%" : Integer.toString(uzemeltetoCeg.getId()));
		preparedStatementUzemeltetoCegekKivalasztasa2.setString(2, uzemeltetoCeg.getNev().equals("") ? "%" : uzemeltetoCeg.getNev());
		preparedStatementUzemeltetoCegekKivalasztasa2.setString(3, (uzemeltetoCeg.getCim().getTelepules().getIranyitoSzam() == 0) ? "%" : Integer.toString(uzemeltetoCeg.getCim().getTelepules().getIranyitoSzam()));
		preparedStatementUzemeltetoCegekKivalasztasa2.setString(4, uzemeltetoCeg.getCim().getTelepules().getNev().equals("") ? "%" : uzemeltetoCeg.getCim().getTelepules().getNev());
		preparedStatementUzemeltetoCegekKivalasztasa2.setString(5, uzemeltetoCeg.getCim().getKozterulet().equals("") ? "%" : uzemeltetoCeg.getCim().getKozterulet());
		preparedStatementUzemeltetoCegekKivalasztasa2.setString(6, (uzemeltetoCeg.getCim().getHazSzam() == 0) ? "%" : Integer.toString(uzemeltetoCeg.getCim().getHazSzam()));
		preparedStatementUzemeltetoCegekKivalasztasa2.setString(7, uzemeltetoCeg.getCim().getEmelet().equals("") ? "%" : uzemeltetoCeg.getCim().getEmelet());
		preparedStatementUzemeltetoCegekKivalasztasa2.setString(8, uzemeltetoCeg.getCim().getAjto().equals("") ? "%" : uzemeltetoCeg.getCim().getAjto());
		preparedStatementUzemeltetoCegekKivalasztasa2.setString(9, (uzemeltetoCeg.getAdoszam() == 0) ? "%" : Long.toString(uzemeltetoCeg.getAdoszam()));
		preparedStatementUzemeltetoCegekKivalasztasa2.setString(10, uzemeltetoCeg.getTelefon().equals("") ? "%" : uzemeltetoCeg.getTelefon());
		preparedStatementUzemeltetoCegekKivalasztasa2.setString(11, uzemeltetoCeg.getFax().equals("") ? "%" : uzemeltetoCeg.getFax());
		preparedStatementUzemeltetoCegekKivalasztasa2.setString(12, uzemeltetoCeg.getEmail().equals("") ? "%" : uzemeltetoCeg.getEmail());
		preparedStatementUzemeltetoCegekKivalasztasa2.setString(13, uzemeltetoCeg.getWebcim().equals("") ? "%" : uzemeltetoCeg.getWebcim());
		ResultSet resultSet = preparedStatementUzemeltetoCegekKivalasztasa2.executeQuery();
		connection.commit();
		List<Ceg> cegek = new ArrayList<Ceg>();
		while(resultSet.next()) {
			Telepules telepules = new Telepules(resultSet.getInt("iranyitoszam"), resultSet.getString("telepules"));
			Cim cim = new Cim.Builder(telepules, resultSet.getString("kozterulet"), resultSet.getInt("hazszam")).kozteruletJellege(resultSet.getString("kozterulet_jellege")).emelet(resultSet.getString("emelet")).ajto(resultSet.getString("ajto")).id(resultSet.getInt("cim_id")).build();
			cegek.add(new Ceg.Builder(resultSet.getString("nev"), cim).adoszam(resultSet.getLong("adoszam")).telefon(resultSet.getString("telefon")).fax(resultSet.getString("fax")).email(resultSet.getString("email")).webcim(resultSet.getString("webcim")).id(resultSet.getInt("uzemeltetokod")).build());
		}
		return cegek;
	}
	
	private int hozdLetreACimetVagyOlvasdKiEsTerjVisszaACimIdval(Cim cim) throws SQLException, DataBaseOperationException {
		try {
			cimLetrehozasa(cim);
		} catch (DataBaseOperationException e) {
			//A cím már létezik
		}
		int cimId = 0;
		for(Cim lekerdezettCim : cimekKivalasztasa(cim))
			if(cim.equals(lekerdezettCim))
				cimId = lekerdezettCim.getId();
		if(cimId == 0)
			throw new DataBaseOperationException("Egy másik kliens törölte a címet. Próbáld újra!", Eset.KONKURENCIA_PROBLEMA);
		return cimId;
	}
	
	private boolean vanEIlyenUzemeltetokod(int uzemeltetokod) throws SQLException {
		if(preparedStatementVanEIlyenUzemeltetokod == null)
			preparedStatementVanEIlyenUzemeltetokod = connection.prepareStatement("SELECT " +
					"count(*) darab FROM keszletnyilvantarto.uzemeltetocegek WHERE uzemeltetokod = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementVanEIlyenUzemeltetokod.setInt(1, uzemeltetokod);
		ResultSet resultSet = preparedStatementVanEIlyenUzemeltetokod.executeQuery();
		connection.commit();
		int darab = 0;
		while(resultSet.next())
			darab = resultSet.getInt("darab");
		if(darab == 0)
			return false;
		return true;
	}
	
	private List<Integer> ilyenUzemeltetoCegekIdSzamai(Ceg uzemeltetoCeg) throws SQLException {
		List<Integer> idSzamok = new ArrayList<Integer>();
		for(Ceg lekerdezettCeg : uzemeltetoCegekKivalasztasa(uzemeltetoCeg))
			if(uzemeltetoCeg.equals(lekerdezettCeg))
				idSzamok.add(lekerdezettCeg.getId());
		return idSzamok;
	}
	
	// ==================Partner cég mûveletek======================
	
	public void partnerCegLetrehozasa(Ceg partnerCeg) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(ilyenPartnerCegekIdSzamai(partnerCeg).size() > 0)
			throw new DataBaseOperationException("A partner cég már szerepel az adatbázisban.\n" + partnerCeg, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		int cimId = hozdLetreACimetVagyOlvasdKiEsTerjVisszaACimIdval(partnerCeg.getCim());
		if(preparedStatementPartnerCegLetrehozasa == null)
			preparedStatementPartnerCegLetrehozasa = connection.prepareStatement("INSERT INTO " +
					"keszletnyilvantarto.partnercegek (nev, cim, adoszam, telefon, fax, email, webcim) " +
					"VALUES (?,?,?,?,?,?,?)");
		preparedStatementPartnerCegLetrehozasa.setString(1, partnerCeg.getNev());
		preparedStatementPartnerCegLetrehozasa.setInt(2, cimId);
		preparedStatementPartnerCegLetrehozasa.setLong(3, partnerCeg.getAdoszam());
		preparedStatementPartnerCegLetrehozasa.setString(4, partnerCeg.getTelefon());
		preparedStatementPartnerCegLetrehozasa.setString(5, partnerCeg.getFax());
		preparedStatementPartnerCegLetrehozasa.setString(6, partnerCeg.getEmail());
		preparedStatementPartnerCegLetrehozasa.setString(7, partnerCeg.getWebcim());
		preparedStatementPartnerCegLetrehozasa.execute();
		connection.commit();
	}
	
	public int partnerCegModositasa(Ceg regiPartnerCeg, Ceg ujPartnerCeg) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		int modositottRekordokSzama = 0;
		List<Integer> regiPartnerCeggelEgyezoPartnerCegekIdSzamai = ilyenPartnerCegekIdSzamai(regiPartnerCeg);
		List<Integer> ujPartnerCeggelEgyezoPartnerCegekIdSzamai = ilyenPartnerCegekIdSzamai(ujPartnerCeg);
		if(regiPartnerCeggelEgyezoPartnerCegekIdSzamai.size() == 0)
			throw new DataBaseOperationException("Nincs ilyen partner cég az adatbázisban.\n" + regiPartnerCeg, Eset.NINCS_ILYEN_OBJEKTUM);
		if(ujPartnerCeggelEgyezoPartnerCegekIdSzamai.size() > 0)
			throw new DataBaseOperationException("A partner cég már szerepel az adatbázisban.\n" + ujPartnerCeg, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		int cimId = hozdLetreACimetVagyOlvasdKiEsTerjVisszaACimIdval(ujPartnerCeg.getCim());
		if(preparedStatementPartnerCegModositasa == null)
			preparedStatementPartnerCegModositasa = connection.prepareStatement("UPDATE " +
					"keszletnyilvantarto.partnercegek SET nev = ?, cim = ?, adoszam = ?, " +
					"telefon = ?, fax = ?, email = ?, webcim = ? WHERE partnerkod = ?");
		for(Integer partnerkod : regiPartnerCeggelEgyezoPartnerCegekIdSzamai) {
			preparedStatementPartnerCegModositasa.setString(1, ujPartnerCeg.getNev());
			preparedStatementPartnerCegModositasa.setInt(2, cimId);
			preparedStatementPartnerCegModositasa.setLong(3, ujPartnerCeg.getAdoszam());
			preparedStatementPartnerCegModositasa.setString(4, ujPartnerCeg.getTelefon());
			preparedStatementPartnerCegModositasa.setString(5, ujPartnerCeg.getFax());
			preparedStatementPartnerCegModositasa.setString(6, ujPartnerCeg.getEmail());
			preparedStatementPartnerCegModositasa.setString(7, ujPartnerCeg.getWebcim());
			preparedStatementPartnerCegModositasa.setInt(8, partnerkod);
			preparedStatementPartnerCegModositasa.executeUpdate();
			connection.commit();
			modositottRekordokSzama++;
		}
		return modositottRekordokSzama;
	}
	
	public void partnerCegModositasa(int partnerkod, Ceg ujPartnerCeg) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		List<Integer> ujPartnerCeggelEgyezoPartnerCegekIdSzamai = ilyenPartnerCegekIdSzamai(ujPartnerCeg);
		if(!vanEIlyenPertnerkod(partnerkod))
			throw new DataBaseOperationException("Nincs ilyen partnerkód az adatbázisban: " + partnerkod, Eset.NINCS_ILYEN_OBJEKTUM);
		boolean egyezikEAPartnerkod = false;
		if(ujPartnerCeggelEgyezoPartnerCegekIdSzamai.size() > 0) {
			for(Integer id : ujPartnerCeggelEgyezoPartnerCegekIdSzamai)
				if(partnerkod == id)
					egyezikEAPartnerkod = true;
			if(!egyezikEAPartnerkod)
				throw new DataBaseOperationException("A partner cég már szerepel az adatbázisban.\n" + ujPartnerCeg, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		}
		int cimId = hozdLetreACimetVagyOlvasdKiEsTerjVisszaACimIdval(ujPartnerCeg.getCim());
		if(preparedStatementPartnerCegModositasa == null)
			preparedStatementPartnerCegModositasa = connection.prepareStatement("UPDATE " +
					"keszletnyilvantarto.partnercegek SET nev = ?, cim = ?, adoszam = ?, " +
					"telefon = ?, fax = ?, email = ?, webcim = ? WHERE partnerkod = ?");
		preparedStatementPartnerCegModositasa.setString(1, ujPartnerCeg.getNev());
		preparedStatementPartnerCegModositasa.setInt(2, cimId);
		preparedStatementPartnerCegModositasa.setLong(3, ujPartnerCeg.getAdoszam());
		preparedStatementPartnerCegModositasa.setString(4, ujPartnerCeg.getTelefon());
		preparedStatementPartnerCegModositasa.setString(5, ujPartnerCeg.getFax());
		preparedStatementPartnerCegModositasa.setString(6, ujPartnerCeg.getEmail());
		preparedStatementPartnerCegModositasa.setString(7, ujPartnerCeg.getWebcim());
		preparedStatementPartnerCegModositasa.setInt(8, partnerkod);
		preparedStatementPartnerCegModositasa.executeUpdate();
		connection.commit();
	}
	
	public List<Ceg> partnerCegekKivalasztasa(String nev, Cim cim, String adoszam, String telefon, 
			String fax, String email, String webcim) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(nev == null || nev.equals(""))
			nev = "%";
		if(adoszam == null || adoszam.equals(""))
			adoszam = "%";
		if(telefon == null || telefon.equals(""))
			telefon = "%";
		if(fax == null || fax.equals(""))
			fax = "%";
		if(email == null || email.equals(""))
			email = "%";
		if(webcim == null || webcim.equals(""))
			webcim = "%";
		int cimId = 0;
		if(cim != null)
			for(Cim lekerdezettCim : cimekKivalasztasa(cim))
				if(cim.equals(lekerdezettCim))
					cimId = lekerdezettCim.getId();
		if(preparedStatementPartnerCegekKivalasztasa == null)
			preparedStatementPartnerCegekKivalasztasa = connection.prepareStatement("SELECT " +
					"p.partnerkod, p.nev, c.id cim_id, t.iranyitoszam, t.nev telepules, c.kozterulet, " +
					"c.kozterulet_jellege, c.hazszam, c.emelet, c.ajto, p.adoszam, p.telefon, p.fax, " +
					"p.email, p.webcim FROM keszletnyilvantarto.partnercegek p, keszletnyilvantarto.cim c, " +
					"keszletnyilvantarto.telepules t WHERE p.cim = c.id AND c.telepules = t.iranyitoszam " +
					"AND p.nev LIKE ? AND c.id LIKE ? AND p.adoszam LIKE ? AND p.telefon LIKE ? AND p.fax LIKE ? " +
					"AND p.email LIKE ? AND p.webcim LIKE ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementPartnerCegekKivalasztasa.setString(1, nev);
		preparedStatementPartnerCegekKivalasztasa.setString(2, cimId == 0 ? "%" : Integer.toString(cimId));
		preparedStatementPartnerCegekKivalasztasa.setString(3, adoszam);
		preparedStatementPartnerCegekKivalasztasa.setString(4, telefon);
		preparedStatementPartnerCegekKivalasztasa.setString(5, fax);
		preparedStatementPartnerCegekKivalasztasa.setString(6, email);
		preparedStatementPartnerCegekKivalasztasa.setString(7, webcim);
		ResultSet resultSet = preparedStatementPartnerCegekKivalasztasa.executeQuery();
		connection.commit();
		List<Ceg> cegek = new ArrayList<Ceg>();
		while(resultSet.next()) {
			Telepules telepules = new Telepules(resultSet.getInt("iranyitoszam"), resultSet.getString("telepules"));
			Cim cim2 = new Cim.Builder(telepules, resultSet.getString("kozterulet"), resultSet.getInt("hazszam")).kozteruletJellege(resultSet.getString("kozterulet_jellege")).emelet(resultSet.getString("emelet")).ajto(resultSet.getString("ajto")).id(resultSet.getInt("cim_id")).build();
			cegek.add(new Ceg.Builder(resultSet.getString("nev"), cim2).adoszam(resultSet.getLong("adoszam")).telefon(resultSet.getString("telefon")).fax(resultSet.getString("fax")).email(resultSet.getString("email")).webcim(resultSet.getString("webcim")).id(resultSet.getInt("partnerkod")).build());
		}
		return cegek;
	}
	
	public Ceg partnerCegKivalasztasaIdAlapjan(int partnerkod) throws SQLException {
		if(preparedStatementPartnerCegKivalasztasaIdAlapjan == null)
			preparedStatementPartnerCegKivalasztasaIdAlapjan = connection.prepareStatement("SELECT * FROM " +
					"keszletnyilvantarto.partnercegek WHERE partnerkod = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementPartnerCegKivalasztasaIdAlapjan.setInt(1, partnerkod);
		ResultSet resultSet = preparedStatementPartnerCegKivalasztasaIdAlapjan.executeQuery();
		connection.commit();
		if(resultSet.next()) {
			Cim cim = cimKivalasztasaIdAlapjan(resultSet.getInt("cim"));
			if(cim != null)
				return new Ceg.Builder(resultSet.getString("nev"), cim).adoszam(resultSet.getLong("adoszam")).telefon(resultSet.getString("telefon")).fax(resultSet.getString("fax")).email(resultSet.getString("email")).webcim(resultSet.getString("webcim")).id(partnerkod).build();
		}
		return null;
	}
	
	public int partnerCegTorlese(Ceg partnerCeg) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		int toroltRekordokSzama = 0;
		List<Integer> partnerCeggelEgyezoPartnerCegekIdSzamai = ilyenPartnerCegekIdSzamai(partnerCeg);
		if(partnerCeggelEgyezoPartnerCegekIdSzamai.size() == 0)
			throw new DataBaseOperationException("Nincs ilyen partner cég az adatbázisban.\n" + partnerCeg, Eset.NINCS_ILYEN_OBJEKTUM);
		if(preparedStatementPartnerCegTorlese == null)
			preparedStatementPartnerCegTorlese = connection.prepareStatement("DELETE FROM " +
					"keszletnyilvantarto.partnercegek WHERE partnerkod = ?");
		for(Integer id : partnerCeggelEgyezoPartnerCegekIdSzamai) {
			preparedStatementPartnerCegTorlese.setInt(1, id);
			preparedStatementPartnerCegTorlese.execute();
			connection.commit();
			toroltRekordokSzama++;
		}
		return toroltRekordokSzama;
	}
	
	public void partnerCegTorlese(int partnerkod) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(!vanEIlyenPertnerkod(partnerkod))
			throw new DataBaseOperationException("Nincs ilyen partnerkód az adatbázisban: " + partnerkod, Eset.NINCS_ILYEN_OBJEKTUM);
		if(preparedStatementPartnerCegTorlese == null)
			preparedStatementPartnerCegTorlese = connection.prepareStatement("DELETE FROM " +
					"keszletnyilvantarto.partnercegek WHERE partnerkod = ?");
		preparedStatementPartnerCegTorlese.setInt(1, partnerkod);
		preparedStatementPartnerCegTorlese.execute();
		connection.commit();
	}
	
	public List<Ceg> partnerCegekKivalasztasa(Ceg partnerCeg) throws SQLException {
		if(preparedStatementPartnerCegekKivalasztasa2 == null)
			preparedStatementPartnerCegekKivalasztasa2 = connection.prepareStatement("SELECT " +
					"p.partnerkod, p.nev, c.id cim_id, t.iranyitoszam, t.nev telepules, c.kozterulet, " +
					"c.kozterulet_jellege, c.hazszam, c.emelet, c.ajto, p.adoszam, p.telefon, p.fax, " +
					"p.email, p.webcim FROM keszletnyilvantarto.partnercegek p, keszletnyilvantarto.cim c, " +
					"keszletnyilvantarto.telepules t WHERE p.cim = c.id AND c.telepules = t.iranyitoszam " +
					"AND p.partnerkod LIKE ? AND p.nev LIKE ? AND t.iranyitoszam LIKE ? AND t.nev LIKE ? " +
					"AND c.kozterulet LIKE ? AND c.hazszam LIKE ? AND c.emelet LIKE ? AND c.ajto " +
					"LIKE ? AND p.adoszam LIKE ? AND p.telefon LIKE ? AND p.fax LIKE ? AND p.email LIKE ? " +
					"AND p.webcim LIKE ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementPartnerCegekKivalasztasa2.setString(1, (partnerCeg.getId() == 0) ? "%" : Integer.toString(partnerCeg.getId()));
		preparedStatementPartnerCegekKivalasztasa2.setString(2, partnerCeg.getNev().equals("") ? "%" : partnerCeg.getNev());
		preparedStatementPartnerCegekKivalasztasa2.setString(3, (partnerCeg.getCim().getTelepules().getIranyitoSzam() == 0) ? "%" : Integer.toString(partnerCeg.getCim().getTelepules().getIranyitoSzam()));
		preparedStatementPartnerCegekKivalasztasa2.setString(4, partnerCeg.getCim().getTelepules().getNev().equals("") ? "%" : partnerCeg.getCim().getTelepules().getNev());
		preparedStatementPartnerCegekKivalasztasa2.setString(5, partnerCeg.getCim().getKozterulet().equals("") ? "%" : partnerCeg.getCim().getKozterulet());
		preparedStatementPartnerCegekKivalasztasa2.setString(6, (partnerCeg.getCim().getHazSzam() == 0) ? "%" : Integer.toString(partnerCeg.getCim().getHazSzam()));
		preparedStatementPartnerCegekKivalasztasa2.setString(7, partnerCeg.getCim().getEmelet().equals("") ? "%" : partnerCeg.getCim().getEmelet());
		preparedStatementPartnerCegekKivalasztasa2.setString(8, partnerCeg.getCim().getAjto().equals("") ? "%" : partnerCeg.getCim().getAjto());
		preparedStatementPartnerCegekKivalasztasa2.setString(9, (partnerCeg.getAdoszam() == 0) ? "%" : Long.toString(partnerCeg.getAdoszam()));
		preparedStatementPartnerCegekKivalasztasa2.setString(10, partnerCeg.getTelefon().equals("") ? "%" : partnerCeg.getTelefon());
		preparedStatementPartnerCegekKivalasztasa2.setString(11, partnerCeg.getFax().equals("") ? "%" : partnerCeg.getFax());
		preparedStatementPartnerCegekKivalasztasa2.setString(12, partnerCeg.getEmail().equals("") ? "%" : partnerCeg.getEmail());
		preparedStatementPartnerCegekKivalasztasa2.setString(13, partnerCeg.getWebcim().equals("") ? "%" : partnerCeg.getWebcim());
		ResultSet resultSet = preparedStatementPartnerCegekKivalasztasa2.executeQuery();
		connection.commit();
		List<Ceg> cegek = new ArrayList<Ceg>();
		while(resultSet.next()) {
			Telepules telepules = new Telepules(resultSet.getInt("iranyitoszam"), resultSet.getString("telepules"));
			Cim cim = new Cim.Builder(telepules, resultSet.getString("kozterulet"), resultSet.getInt("hazszam")).kozteruletJellege(resultSet.getString("kozterulet_jellege")).emelet(resultSet.getString("emelet")).ajto(resultSet.getString("ajto")).id(resultSet.getInt("cim_id")).build();
			cegek.add(new Ceg.Builder(resultSet.getString("nev"), cim).adoszam(resultSet.getLong("adoszam")).telefon(resultSet.getString("telefon")).fax(resultSet.getString("fax")).email(resultSet.getString("email")).webcim(resultSet.getString("webcim")).id(resultSet.getInt("partnerkod")).build());
		}
		return cegek;
	}
	
	private boolean vanEIlyenPertnerkod(int partnerkod) throws SQLException {
		if(preparedStatementVanEIlyenPartnerkod == null)
			preparedStatementVanEIlyenPartnerkod = connection.prepareStatement("SELECT " +
					"count(*) darab FROM keszletnyilvantarto.partnercegek WHERE partnerkod = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementVanEIlyenPartnerkod.setInt(1, partnerkod);
		ResultSet resultSet = preparedStatementVanEIlyenPartnerkod.executeQuery();
		connection.commit();
		int darab = 0;
		while(resultSet.next())
			darab = resultSet.getInt("darab");
		if(darab == 0)
			return false;
		return true;
	}
	
	private List<Integer> ilyenPartnerCegekIdSzamai(Ceg partnerCeg) throws SQLException {
		List<Integer> idSzamok = new ArrayList<Integer>();
		for(Ceg lekerdezettCeg : partnerCegekKivalasztasa(partnerCeg))
			if(partnerCeg.equals(lekerdezettCeg))
				idSzamok.add(lekerdezettCeg.getId());
		return idSzamok;
	}
	
	// ==================Termék mûveletek====================
	
	public void termekLetrehozasa(Termek termek) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(ilyenTermekekIdSzamai(termek).size() > 0)
			throw new DataBaseOperationException("A termék már szerepel az adatbázisban.\n" + termek, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		if(preparedStatementTermekLetrehozasa == null)
			preparedStatementTermekLetrehozasa = connection.prepareStatement("INSERT INTO " +
					"keszletnyilvantarto.termekek (megnevezes, vtsz, aktualis_afa_szazaleklab, " +
					"aktualis_netto_eladasi_ar, mennyiseg_egysege) VALUES (?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
		preparedStatementTermekLetrehozasa.setString(1, termek.getMegnevezes());
		preparedStatementTermekLetrehozasa.setInt(2, termek.getVtsz());
		preparedStatementTermekLetrehozasa.setDouble(3, termek.getAfaSzazaleklab());
		preparedStatementTermekLetrehozasa.setDouble(4, termek.getEgysegNettoEladasiAr());
		preparedStatementTermekLetrehozasa.setString(5, termek.getMennyisegEgysege().toString());
		preparedStatementTermekLetrehozasa.executeUpdate();
		ResultSet rs = preparedStatementTermekLetrehozasa.getGeneratedKeys();
		while(rs.next())
			System.out.println(rs.getInt(1));
		connection.commit();
	}
	
	public void termekModositasa(int termekkod, Termek uj) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		List<Integer> ujTermekkelEgyezoTermekekIdSzamai = ilyenTermekekIdSzamai(uj);
		if(!vanEIlyenTermekkod(termekkod))
			throw new DataBaseOperationException("Nincs ilyen termékkód az adatbázisban: " + termekkod, Eset.NINCS_ILYEN_OBJEKTUM);
		if(ujTermekkelEgyezoTermekekIdSzamai.size() > 0)
			throw new DataBaseOperationException("A termék már szerepel az adatbázisban.\n" + uj, Eset.MAR_VAN_ILYEN_OBJEKTUM);
		if(preparedStatementTermekModositasa == null)
			preparedStatementTermekModositasa = connection.prepareStatement("UPDATE " +
					"keszletnyilvantarto.termekek SET megnevezes = ?, vtsz = ?, " +
					"aktualis_afa_szazaleklab = ?, aktualis_netto_eladasi_ar = ?, " +
					"mennyiseg_egysege = ? WHERE termekkod = ?");
		preparedStatementTermekModositasa.setString(1, uj.getMegnevezes());
		preparedStatementTermekModositasa.setInt(2, uj.getVtsz());
		preparedStatementTermekModositasa.setDouble(3, uj.getAfaSzazaleklab());
		preparedStatementTermekModositasa.setDouble(4, uj.getEgysegNettoEladasiAr());
		preparedStatementTermekModositasa.setString(5, uj.getMennyisegEgysege().toString());
		preparedStatementTermekModositasa.setInt(6, termekkod);
		preparedStatementTermekModositasa.executeUpdate();
		connection.commit();
	}
	
	public List<Termek> termekekKivalasztasa(String megnevezes, String vtsz, String aktualisAfaSzazaleklab, String mennyisegEgysege) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(megnevezes == null || megnevezes.equals(""))
			megnevezes = "%";
		if(vtsz == null || vtsz.equals(""))
			vtsz = "%";
		if(aktualisAfaSzazaleklab == null || aktualisAfaSzazaleklab.equals(""))
			aktualisAfaSzazaleklab = "%";
		if(mennyisegEgysege == null || mennyisegEgysege.equals(""))
			mennyisegEgysege = "%";
		if(preparedStatementTermekekKivalasztasa == null)
			preparedStatementTermekekKivalasztasa = connection.prepareStatement("SELECT * FROM " +
					"keszletnyilvantarto.termekek WHERE megnevezes LIKE ? AND vtsz LIKE ? AND " +
					"aktualis_afa_szazaleklab LIKE ? AND mennyiseg_egysege LIKE ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementTermekekKivalasztasa.setString(1, "%" + megnevezes + "%");
		preparedStatementTermekekKivalasztasa.setString(2, vtsz);
		preparedStatementTermekekKivalasztasa.setString(3, aktualisAfaSzazaleklab);
		preparedStatementTermekekKivalasztasa.setString(4, mennyisegEgysege);
		ResultSet resultSet = preparedStatementTermekekKivalasztasa.executeQuery();
		connection.commit();
		List<Termek> termekek = new ArrayList<Termek>();
		while(resultSet.next())
			termekek.add(new Termek.Builder(resultSet.getString("megnevezes"), resultSet.getDouble("aktualis_afa_szazaleklab")).vtsz(resultSet.getInt("vtsz")).egysegNettoBeszerzesiAr(resultSet.getDouble("aktualis_netto_beszerzesi_ar")).egysegNettoEladasiAr(resultSet.getDouble("aktualis_netto_eladasi_ar")).mennyisegEgysege(resultSet.getString("mennyiseg_egysege")).mennyiseg(resultSet.getInt("raktarozott_mennyiseg")).termekkod(resultSet.getInt("termekkod")).build());
		return termekek;
	}
	
	public List<Termek> termekekKivalasztasa(Termek termek) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(preparedStatementTermekekKivalasztasa2 == null)
			preparedStatementTermekekKivalasztasa2 = connection.prepareStatement("SELECT * FROM " +
					"keszletnyilvantarto.termekek WHERE megnevezes LIKE ? AND vtsz LIKE ? AND " +
					"aktualis_netto_beszerzesi_ar LIKE ? AND aktualis_netto_eladasi_ar LIKE ? " +
					"AND aktualis_afa_szazaleklab LIKE ? AND raktarozott_mennyiseg LIKE ? AND termekkod LIKE ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementTermekekKivalasztasa2.setString(1, termek.getMegnevezes().equals("") ? "%" : termek.getMegnevezes());
		preparedStatementTermekekKivalasztasa2.setString(2, termek.getVtsz() == 0 ? "%" : Integer.toString(termek.getVtsz()));
		preparedStatementTermekekKivalasztasa2.setString(3, termek.getEgysegNettoBeszerzesiAr() == 0 ? "%" : Integer.toString((int)termek.getEgysegNettoBeszerzesiAr()) + "%");
		preparedStatementTermekekKivalasztasa2.setString(4, termek.getEgysegNettoEladasiAr() == 0 ? "%" : Integer.toString((int)termek.getEgysegNettoEladasiAr()) + "%");
		preparedStatementTermekekKivalasztasa2.setString(5, termek.getAfaSzazaleklab() == 0 ? "%" : Integer.toString((int)termek.getAfaSzazaleklab()) + "%");
		preparedStatementTermekekKivalasztasa2.setString(6, termek.getMennyiseg() == 0 ? "%" : Integer.toString(termek.getMennyiseg()));
		preparedStatementTermekekKivalasztasa2.setString(7, termek.getTermekkod() == 0 ? "%" : Integer.toString(termek.getTermekkod()));
		ResultSet resultSet = preparedStatementTermekekKivalasztasa2.executeQuery();
		connection.commit();
		List<Termek> termekek = new ArrayList<Termek>();
		while(resultSet.next())
			termekek.add(new Termek.Builder(resultSet.getString("megnevezes"), resultSet.getDouble("aktualis_afa_szazaleklab")).vtsz(resultSet.getInt("vtsz")).egysegNettoBeszerzesiAr(resultSet.getDouble("aktualis_netto_beszerzesi_ar")).egysegNettoEladasiAr(resultSet.getDouble("aktualis_netto_eladasi_ar")).mennyisegEgysege(resultSet.getString("mennyiseg_egysege")).mennyiseg(resultSet.getInt("raktarozott_mennyiseg")).termekkod(resultSet.getInt("termekkod")).build());
		return termekek;
	}
	
	public Termek termekKivalasztasaIdAlapjan(int termekkod) throws SQLException {
		if(preparedStatementTermekKivalasztasaIdAlapjan == null)
			preparedStatementTermekKivalasztasaIdAlapjan = connection.prepareStatement("SELECT * FROM " +
					"keszletnyilvantarto.termekek WHERE termekkod = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementTermekKivalasztasaIdAlapjan.setInt(1, termekkod);
		ResultSet resultSet = preparedStatementTermekKivalasztasaIdAlapjan.executeQuery();
		connection.commit();
		if(resultSet.next())
			return new Termek.Builder(resultSet.getString("megnevezes"), resultSet.getDouble("aktualis_afa_szazaleklab")).vtsz(resultSet.getInt("vtsz")).egysegNettoBeszerzesiAr(resultSet.getDouble("aktualis_netto_beszerzesi_ar")).egysegNettoEladasiAr(resultSet.getDouble("aktualis_netto_eladasi_ar")).mennyisegEgysege(resultSet.getString("mennyiseg_egysege")).mennyiseg(resultSet.getInt("raktarozott_mennyiseg")).termekkod(termekkod).build();
		return null;
	}
	
	public int termekTorlese(Termek termek) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		int toroltRekordokSzama = 0;
		List<Integer> termekkelEgyezoTermekekIdSzamai = ilyenTermekekIdSzamai(termek);
		if(termekkelEgyezoTermekekIdSzamai.size() == 0)
			throw new DataBaseOperationException("Nincs ilyen termék az adatbázisban.\n" + termek, Eset.NINCS_ILYEN_OBJEKTUM);
		if(preparedStatementTermekTorlese == null)
			preparedStatementTermekTorlese = connection.prepareStatement("DELETE FROM " +
					"keszletnyilvantarto.termekek WHERE termekkod = ?");
		for(Integer id : termekkelEgyezoTermekekIdSzamai) {
			preparedStatementTermekTorlese.setInt(1, id);
			preparedStatementTermekTorlese.execute();
			connection.commit();
			toroltRekordokSzama++;
		}
		return toroltRekordokSzama;
	}
	
	public void termekTorlese(int termekkod) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(!vanEIlyenTermekkod(termekkod))
			throw new DataBaseOperationException("Nincs ilyen termékkód az adatbázisban: " + termekkod, Eset.NINCS_ILYEN_OBJEKTUM);
		if(preparedStatementTermekTorlese == null)
			preparedStatementTermekTorlese = connection.prepareStatement("DELETE FROM " +
					"keszletnyilvantarto.termekek WHERE termekkod = ?");
		preparedStatementTermekTorlese.setInt(1, termekkod);
		preparedStatementTermekTorlese.execute();
		connection.commit();
	}
	
	private boolean vanEIlyenTermekkod(int termekkod) throws SQLException {
		if(preparedStatementVanEIlyenTermekkod == null)
			preparedStatementVanEIlyenTermekkod = connection.prepareStatement("SELECT " +
					"count(*) darab FROM keszletnyilvantarto.termekek WHERE termekkod = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementVanEIlyenTermekkod.setInt(1, termekkod);
		ResultSet resultSet = preparedStatementVanEIlyenTermekkod.executeQuery();
		connection.commit();
		int darab = 0;
		while(resultSet.next())
			darab = resultSet.getInt("darab");
		if(darab == 0)
			return false;
		return true;
	}
	
	private List<Integer> ilyenTermekekIdSzamai(Termek termek) throws SQLException, DataBaseOperationException {
		List<Integer> idSzamok = new ArrayList<Integer>();
		for(Termek lekerdezettTermek : termekekKivalasztasa(termek))
			if(termek.equals(lekerdezettTermek))
				idSzamok.add(lekerdezettTermek.getTermekkod());
		return idSzamok;
	}
	
	// ==================Beszerzés mûveletek=================
	
	public void beszerzesLetrehozasa(Beszerzes beszerzes) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(preparedStatementBeszerzesLetrehozasa == null)
			preparedStatementBeszerzesLetrehozasa = connection.prepareStatement("INSERT INTO " +
					"keszletnyilvantarto.beszerzesek (idopont, termekkod, mennyiseg, egyseg_netto_beszerzesi_ara, " +
					"afa_szazaleklab, uzemeltetokod, partnerkod, felhasznalo_neve) VALUES (?,?,?,?,?,?,?,?)");
		preparedStatementBeszerzesLetrehozasa.setLong(1, beszerzes.getDatum().getTime());
		preparedStatementBeszerzesLetrehozasa.setInt(2, beszerzes.getTermek().getTermekkod());
		preparedStatementBeszerzesLetrehozasa.setInt(3, beszerzes.getTermek().getMennyiseg());
		preparedStatementBeszerzesLetrehozasa.setDouble(4, beszerzes.getTermek().getEgysegNettoBeszerzesiAr());
		preparedStatementBeszerzesLetrehozasa.setDouble(5, beszerzes.getTermek().getAfaSzazaleklab());
		preparedStatementBeszerzesLetrehozasa.setInt(6, beszerzes.getVevo().getId());
		preparedStatementBeszerzesLetrehozasa.setInt(7, beszerzes.getElado().getId());
		preparedStatementBeszerzesLetrehozasa.setString(8, felhasznalo.getNev());
		preparedStatementBeszerzesLetrehozasa.execute();
		if(preparedStatementBeszerzesTermekBeallitasa == null)
			preparedStatementBeszerzesTermekBeallitasa = connection.prepareStatement("UPDATE " +
					"keszletnyilvantarto.termekek SET raktarozott_mennyiseg = raktarozott_mennyiseg + ?, " +
					"aktualis_netto_beszerzesi_ar = ?, aktualis_afa_szazaleklab = ? WHERE termekkod = ?");
		preparedStatementBeszerzesTermekBeallitasa.setInt(1, beszerzes.getTermek().getMennyiseg());
		preparedStatementBeszerzesTermekBeallitasa.setDouble(2, beszerzes.getTermek().getEgysegNettoBeszerzesiAr());
		preparedStatementBeszerzesTermekBeallitasa.setDouble(3, beszerzes.getTermek().getAfaSzazaleklab());
		preparedStatementBeszerzesTermekBeallitasa.setInt(4, beszerzes.getTermek().getTermekkod());
		preparedStatementBeszerzesTermekBeallitasa.executeUpdate();
		connection.commit();
	}
	
	/**
	 * @param datumtol - datumig használata például: az összes 2012. szeptember 8.-án létrehozott beszerzés
	 * @param datumtol Date date = new Date(new GregorianCalendar(2012, Calendar.SEPTEMBER, 8).getTimeInMillis());
	 * @param datumig Date date = new Date(new GregorianCalendar(2012, Calendar.SEPTEMBER, 8).getTimeInMillis());
	 */
	public List<Beszerzes> beszerzesekKivalasztasa(Date datumtol, Date datumig, String uzemeltetokod, String partnerkod, String termekkod) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(uzemeltetokod == null || uzemeltetokod.equals(""))
			uzemeltetokod = "%";
		if(partnerkod == null || partnerkod.equals(""))
			partnerkod = "%";
		if(termekkod == null || termekkod.equals(""))
			termekkod = "%";
		long tol = 0;
		if(datumtol != null)
			tol = datumtol.getTime();
		long ig = 4102441200000l; // 2100. január 1.
		if(datumig != null)
			ig = datumig.getTime() + 86399999; // a nap végéig
		if(preparedStatementBeszerzesekKivalasztasa == null)
			preparedStatementBeszerzesekKivalasztasa = connection.prepareStatement("SELECT * FROM " +
					"keszletnyilvantarto.beszerzesek WHERE idopont > ? AND idopont < ? AND uzemeltetokod LIKE ? " +
					"AND partnerkod LIKE ? AND termekkod LIKE ? ", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementBeszerzesekKivalasztasa.setLong(1, tol);
		preparedStatementBeszerzesekKivalasztasa.setLong(2, ig);
		preparedStatementBeszerzesekKivalasztasa.setString(3, uzemeltetokod);
		preparedStatementBeszerzesekKivalasztasa.setString(4, partnerkod);
		preparedStatementBeszerzesekKivalasztasa.setString(5, termekkod);
		ResultSet resultset = preparedStatementBeszerzesekKivalasztasa.executeQuery();
		connection.commit();
		List<Beszerzes> beszerzesek = new ArrayList<Beszerzes>();
		while(resultset.next()) {
			Termek termek = termekKivalasztasaIdAlapjan(resultset.getInt("termekkod"));
			Ceg uzemeltetoCeg = uzemeltetoCegKivalasztasaIdAlapjan(resultset.getInt("uzemeltetokod"));
			Ceg partnerCeg = partnerCegKivalasztasaIdAlapjan(resultset.getInt("partnerkod"));
			Felhasznalo felhasznalo = felhasznaloKivalasztasaIdAlapjan(resultset.getString("felhasznalo_neve"));
			if(termek != null & uzemeltetoCeg != null & partnerCeg != null & felhasznalo != null) {
				termek.setMennyiseg(resultset.getInt("mennyiseg"));
				termek.setAfaSzazaleklab(resultset.getDouble("afa_szazaleklab"));
				termek.setEgysegNettoBeszerzesiAr(resultset.getDouble("egyseg_netto_beszerzesi_ara"));
				beszerzesek.add(new Beszerzes.Builder(uzemeltetoCeg, partnerCeg, termek).id(resultset.getInt("id")).datum(new Date(resultset.getLong("idopont"))).felhasznalo(felhasznalo).build());
			}
		}
		return beszerzesek;
	}
	
	// ==================Számla mûveletek====================
	
	public void szamlaLetrehozasa(Szamla szamla) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		int szamlaSorszam = 0; 
		if(preparedStatementSzamlaLetrehozasa == null)
			preparedStatementSzamlaLetrehozasa = connection.prepareStatement("INSERT INTO " +
					"keszletnyilvantarto.szamlak (idopont, uzemeltetokod, partnerkod, megjegyzes, " +
					"keszitette, szamla_allapot, allapotat_beallitotta) VALUES (?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
		preparedStatementSzamlaLetrehozasa.setLong(1, szamla.getDatum().getTime());
		preparedStatementSzamlaLetrehozasa.setInt(2, szamla.getElado().getId());
		preparedStatementSzamlaLetrehozasa.setInt(3, szamla.getVevo().getId());
		preparedStatementSzamlaLetrehozasa.setString(4, szamla.getMegjegyzes());
		preparedStatementSzamlaLetrehozasa.setString(5, felhasznalo.getNev());
		preparedStatementSzamlaLetrehozasa.setString(6, szamla.getSzamlaAllapot().toString());
		preparedStatementSzamlaLetrehozasa.setString(7, felhasznalo.getNev());
		preparedStatementSzamlaLetrehozasa.executeUpdate();
		ResultSet resultSet = preparedStatementSzamlaLetrehozasa.getGeneratedKeys();
		if(resultSet.next())
			szamlaSorszam = resultSet.getInt(1);
		if(szamlaSorszam == 0)
			throw new DataBaseOperationException("A számlát nem sikerült létrehozni.", Eset.SZAMLA_LETREHOZAS_HIBA);
		if(preparedStatementSzamlaErtekesitesLetrehozasa == null)
			preparedStatementSzamlaErtekesitesLetrehozasa = connection.prepareStatement("INSERT INTO " +
					"keszletnyilvantarto.ertekesitesek (termekkod, mennyiseg, egyseg_netto_eladasi_ara, " +
					"afa_szazaleklab, szamla_sorszam) VALUES (?,?,?,?,?)");
		for(Termek t : szamla.getTermekek()) {
			preparedStatementSzamlaErtekesitesLetrehozasa.setInt(1, t.getTermekkod());
			preparedStatementSzamlaErtekesitesLetrehozasa.setInt(2, t.getMennyiseg());
			preparedStatementSzamlaErtekesitesLetrehozasa.setDouble(3, t.getEgysegNettoEladasiAr());
			preparedStatementSzamlaErtekesitesLetrehozasa.setDouble(4, t.getAfaSzazaleklab());
			preparedStatementSzamlaErtekesitesLetrehozasa.setInt(5, szamlaSorszam);
			preparedStatementSzamlaErtekesitesLetrehozasa.execute();
		}
		if(preparedStatementSzamlaErtekesitesTermekBeallitasa == null)
			preparedStatementSzamlaErtekesitesTermekBeallitasa = connection.prepareStatement("UPDATE " +
					"keszletnyilvantarto.termekek SET raktarozott_mennyiseg = raktarozott_mennyiseg - ?, " +
					"aktualis_afa_szazaleklab = ?, aktualis_netto_eladasi_ar = ? " +
					"WHERE termekkod = ? AND raktarozott_mennyiseg >= ?");
		for(Termek t : szamla.getTermekek()) {
			preparedStatementSzamlaErtekesitesTermekBeallitasa.setInt(1, t.getMennyiseg());
			preparedStatementSzamlaErtekesitesTermekBeallitasa.setDouble(2, t.getAfaSzazaleklab());
			preparedStatementSzamlaErtekesitesTermekBeallitasa.setDouble(3, t.getEgysegNettoEladasiAr());
			preparedStatementSzamlaErtekesitesTermekBeallitasa.setInt(4, t.getTermekkod());
			preparedStatementSzamlaErtekesitesTermekBeallitasa.setInt(5, t.getMennyiseg());
			preparedStatementSzamlaErtekesitesTermekBeallitasa.execute();
			if(preparedStatementSzamlaErtekesitesTermekBeallitasa.getUpdateCount() == 0) {
				connection.rollback();
				throw new DataBaseOperationException("A számlát nem sikerült létrehozni, mert nincs a megadott mennyiség raktáron", Eset.NINCS_ENNYI_RAKTARON);
			}
		}
		connection.commit();
	}
	
	public List<Szamla> szamlakKivalasztasa(Date datumtol, Date datumig, String uzemeltetokod, String partnerkod, String allapot) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(uzemeltetokod == null || uzemeltetokod.equals(""))
			uzemeltetokod = "%";
		if(partnerkod == null || partnerkod.equals(""))
			partnerkod = "%";
		if(allapot == null || allapot.equals(""))
			allapot = "%";
		long tol = 0;
		if(datumtol != null)
			tol = datumtol.getTime();
		long ig = 4102441200000l; // 2100. január 1.
		if(datumig != null)
			ig = datumig.getTime() + 86399999; // a nap végéig
		if(preparedStatementSzamlakKivalasztasa == null)
			preparedStatementSzamlakKivalasztasa = connection.prepareStatement("SELECT * FROM " +
					"keszletnyilvantarto.szamlak WHERE idopont > ? AND idopont < ? AND uzemeltetokod LIKE ? " +
					"AND partnerkod LIKE ? AND szamla_allapot LIKE ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementSzamlakKivalasztasa.setLong(1, tol);
		preparedStatementSzamlakKivalasztasa.setLong(2, ig);
		preparedStatementSzamlakKivalasztasa.setString(3, uzemeltetokod);
		preparedStatementSzamlakKivalasztasa.setString(4, partnerkod);
		preparedStatementSzamlakKivalasztasa.setString(5, allapot);
		ResultSet szamlakResultSet = preparedStatementSzamlakKivalasztasa.executeQuery();
		connection.commit();
		List<Szamla> szamlak = new ArrayList<Szamla>();
		while(szamlakResultSet.next()) {
			long sorszam = szamlakResultSet.getLong("sorszam");
			Ceg uzemeltetoCeg = uzemeltetoCegKivalasztasaIdAlapjan(szamlakResultSet.getInt("uzemeltetokod"));
			Ceg partnerCeg = partnerCegKivalasztasaIdAlapjan(szamlakResultSet.getInt("partnerkod"));
			Felhasznalo keszitette = felhasznaloKivalasztasaIdAlapjan(szamlakResultSet.getString("keszitette"));
			Felhasznalo allapotatBeallitotta = felhasznaloKivalasztasaIdAlapjan(szamlakResultSet.getString("allapotat_beallitotta"));
			if(preparedStatementSzamlakErtekesiteseiKivalasztasa == null)
				preparedStatementSzamlakErtekesiteseiKivalasztasa = connection.prepareStatement("SELECT * FROM " +
						"keszletnyilvantarto.ertekesitesek WHERE szamla_sorszam = ?");
			preparedStatementSzamlakErtekesiteseiKivalasztasa.setLong(1, sorszam);
			ResultSet ertekesitesekResultSet = preparedStatementSzamlakErtekesiteseiKivalasztasa.executeQuery();
			List<Termek> termekek = new ArrayList<Termek>();
			while(ertekesitesekResultSet.next()) {
				Termek termek = termekKivalasztasaIdAlapjan(ertekesitesekResultSet.getInt("termekkod"));
				if(termek != null) {
					termek.setAfaSzazaleklab(ertekesitesekResultSet.getDouble("afa_szazaleklab"));
					termek.setMennyiseg(ertekesitesekResultSet.getInt("mennyiseg"));
					termek.setEgysegNettoEladasiAr(ertekesitesekResultSet.getDouble("egyseg_netto_eladasi_ara"));
					termekek.add(termek);
				}
			}
			if(uzemeltetoCeg != null & partnerCeg != null & keszitette != null & allapotatBeallitotta != null)
				szamlak.add(new Szamla.Builder(partnerCeg, uzemeltetoCeg, termekek).sorszam(sorszam).datum(new Date(szamlakResultSet.getLong("idopont"))).keszitette(keszitette).szamlaAllapot(szamlakResultSet.getString("szamla_allapot")).megjegyzes(szamlakResultSet.getString("megjegyzes")).allapotatBeallitotta(allapotatBeallitotta).build());
		}
		return szamlak;
	}
	
	public void szamlaSztornozasa(long sorszam) throws SQLException, DataBaseOperationException {
		if(!felhasznaloBejelentkezesEngedelyezettE(felhasznalo))
			throw new DataBaseOperationException("Felhasználó bejelentkezés nem engedélyezett.\n" + felhasznalo, Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT);
		if(preparedStatementSzamlaSztornoEllenorzes == null)
			preparedStatementSzamlaSztornoEllenorzes = connection.prepareStatement("SELECT szamla_allapot " +
					"FROM keszletnyilvantarto.szamlak WHERE sorszam = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		preparedStatementSzamlaSztornoEllenorzes.setLong(1, sorszam);
		ResultSet resultSet = preparedStatementSzamlaSztornoEllenorzes.executeQuery();
		connection.commit();
		if(resultSet.next()) {
			if(resultSet.getString("szamla_allapot").equals("ERVENYES")) {
				if(preparedStatementSzamlaSztornozasa == null)
					preparedStatementSzamlaSztornozasa = connection.prepareStatement("UPDATE " +
							"keszletnyilvantarto.szamlak SET szamla_allapot = 'SZTORNOZOTT', " +
							"allapotat_beallitotta = ? WHERE sorszam = ?");
				preparedStatementSzamlaSztornozasa.setString(1, felhasznalo.getNev());
				preparedStatementSzamlaSztornozasa.setLong(2, sorszam);
				preparedStatementSzamlaSztornozasa.execute();
				if(preparedStatementSzamlakErtekesiteseiKivalasztasa == null)
					preparedStatementSzamlakErtekesiteseiKivalasztasa = connection.prepareStatement("SELECT * FROM " +
							"keszletnyilvantarto.ertekesitesek WHERE szamla_sorszam = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
				preparedStatementSzamlakErtekesiteseiKivalasztasa.setLong(1, sorszam);
				ResultSet ertekesitesekResultSet = preparedStatementSzamlakErtekesiteseiKivalasztasa.executeQuery();
				Map<Integer, Integer> termekkodEsMennyiseg = new HashMap<Integer, Integer>();
				while(ertekesitesekResultSet.next())
					termekkodEsMennyiseg.put(ertekesitesekResultSet.getInt("termekkod"), ertekesitesekResultSet.getInt("mennyiseg"));
				if(preparedStatementSzamlaSztornoTermekBeallitasa == null)
					preparedStatementSzamlaSztornoTermekBeallitasa = connection.prepareStatement("UPDATE " +
							"keszletnyilvantarto.termekek SET raktarozott_mennyiseg = raktarozott_mennyiseg + ? " +
							"WHERE termekkod = ?");
				for(Map.Entry<Integer, Integer> e : termekkodEsMennyiseg.entrySet()) {
					preparedStatementSzamlaSztornoTermekBeallitasa.setInt(1, e.getValue());
					preparedStatementSzamlaSztornoTermekBeallitasa.setInt(2, e.getKey());
					preparedStatementSzamlaSztornoTermekBeallitasa.execute();
				}
			} else {
				throw new DataBaseOperationException("A számla már sztornózott.", Eset.A_SZAMLA_MAR_SZTORNOZOTT);
			}
		}
		connection.commit();
	}
	
}
