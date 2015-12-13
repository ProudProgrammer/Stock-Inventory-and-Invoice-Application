package server.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public final class ServerProperties {

	private static final ServerProperties instance = new ServerProperties();
	private static final File propertiesFile = new File("data/server.properties");
	private static final Properties properties = new Properties();
	private static Writer propertiesFileWriter;
	private static Reader propertiesFileReader;
	private static boolean propertiesLoaded;

	private ServerProperties() {}

	public static ServerProperties getInstance() {
		if(propertiesLoaded)
			return instance;
		try {
			propertiesFileReader = new FileReader(propertiesFile);
			properties.load(propertiesFileReader);
			propertiesLoaded = true;
			propertiesFileReader.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {}
		return instance;
	}
	
	public Properties getProperties() {
		return properties;
	}

	public boolean isPropertiesLoaded() {
		return propertiesLoaded;
	}

	public ServerProperties save() throws FileNotFoundException, IOException {
		propertiesFileWriter = new PrintWriter(propertiesFile);
		properties.store(propertiesFileWriter, "");
		propertiesFileWriter.close();
		return this;
	}

	// ==================DataBaseProperties====================

	// ==================Getters===============================

	public String getDataBaseAddress() {
		String address = properties.getProperty("address");
		if(address == null)
			return "";
		return address;
	}

	public String getDataBaseUser() {
		String user = properties.getProperty("user");
		if(user == null)
			return "";
		return user; 
	}

	public String getDataBasePassword() {
		String password = properties.getProperty("password");
		if(password == null)
			return "";
		return password; 
	}

	// ==================Setters===============================

	public ServerProperties setDataBaseAddress(String address) {
		properties.setProperty("address", address);
		return this;
	}

	public ServerProperties setDataBaseUser(String user) {
		properties.setProperty("user", user);
		return this;
	}

	public ServerProperties setDataBasePassword(String password) {
		properties.setProperty("password", password);
		return this;
	}
	
	// ==================SQL===================================

	// ==================Getters===============================
	
	public String getCreateDatabaseSQL() {
		String createDatabaseSQL = properties.getProperty("sql_create_database");
		if(createDatabaseSQL == null)
			return "";
		return createDatabaseSQL;
	}
	
	public String getCreateTableFelhasznalokSQL() {
		String createTableFelhasznalokSQL = properties.getProperty("sql_create_table_felhasznalok");
		if(createTableFelhasznalokSQL == null)
			return "";
		return createTableFelhasznalokSQL;
	}
	
	public String getCreateTableTelepulesSQL() {
		String createTableTelepulesSQL = properties.getProperty("sql_create_table_telepules");
		if(createTableTelepulesSQL == null)
			return "";
		return createTableTelepulesSQL;
	}
	
	public String getCreateTableCimSQL() {
		String createTableCimSQL = properties.getProperty("sql_create_table_cim");
		if(createTableCimSQL == null)
			return "";
		return createTableCimSQL;
	}
	
	public String getCreateTableUzemeltetoCegekSQL() {
		String createTableCegekSQL = properties.getProperty("sql_create_table_uzemeltetocegek");
		if(createTableCegekSQL == null)
			return "";
		return createTableCegekSQL;
	}
	
	public String getCreateTablePartnerCegekSQL() {
		String createTablePartnerekSQL = properties.getProperty("sql_create_table_partnercegek");
		if(createTablePartnerekSQL == null)
			return "";
		return createTablePartnerekSQL;
	}
	
	public String getCreateTableTermekekSQL() {
		String createTableTermekekSQL = properties.getProperty("sql_create_table_termekek");
		if(createTableTermekekSQL == null)
			return "";
		return createTableTermekekSQL;
	}
	
	public String getCreateTableBeszerzesekSQL() {
		String createTableBeszerzesekSQL = properties.getProperty("sql_create_table_beszerzesek");
		if(createTableBeszerzesekSQL == null)
			return "";
		return createTableBeszerzesekSQL;
	}
	
	public String getCreateTableSzamlakSQL() {
		String createTableSzamlakSQL = properties.getProperty("sql_create_table_szamlak");
		if(createTableSzamlakSQL == null)
			return "";
		return createTableSzamlakSQL;
	}
	
	public String getCreateTableErtekesitesekSQL() {
		String createTableErtekesitesekSQL = properties.getProperty("sql_create_table_ertekesitesek");
		if(createTableErtekesitesekSQL == null)
			return "";
		return createTableErtekesitesekSQL;
	}
	
	// ==================ServerProperties======================
	
	// ==================Getters===============================
	
	public String getServerPort() {
		String port = properties.getProperty("server_port");
		if(port == null)
			return "";
		return port;
	}
	
	// ==================Setters===============================
	
	public ServerProperties setServerPort(String port) {
		properties.setProperty("server_port", port);
		return this;
	}
}