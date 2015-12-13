package client.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public final class ClientProperties {

	private static final ClientProperties instance = new ClientProperties();
	private static final File propertiesFile = new File("data/client.properties");
	private static final Properties properties = new Properties();
	private static Writer propertiesFileWriter;
	private static Reader propertiesFileReader;
	private static boolean propertiesLoaded;
	
	private ClientProperties() {}
	
	public static ClientProperties getInstance() {
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
	
	public boolean isPropertiesLoaded() {
		return propertiesLoaded;
	}
	
	public ClientProperties save() throws FileNotFoundException, IOException {
		propertiesFileWriter = new PrintWriter(propertiesFile);
		properties.store(propertiesFileWriter, "");
		propertiesFileWriter.close();
		return this;
	}
	
	// ==================ServerProperties====================
	
	// ==================Getters===============================
	
	public String getServerPort() {
		String port = properties.getProperty("server_port");
		if(port == null)
			return "";
		return port;
	}
	
	public String getServerHost() {
		String host = properties.getProperty("server_host");
		if(host == null)
			return "";
		return host;
	}
	
	// ==================Setters===============================
	
	public ClientProperties setServerPort(String port) {
		properties.setProperty("server_port", port);
		return this;
	}
	
	public ClientProperties setServerHost(String host) {
		properties.setProperty("server_host", host);
		return this;
	}
}
