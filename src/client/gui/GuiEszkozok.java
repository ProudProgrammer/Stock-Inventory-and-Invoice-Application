package client.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import client.settings.ClientProperties;

import common.entities.Ceg;
import common.entities.Felhasznalo;
import common.entities.Szamla;
import common.entities.Termek;

public final class GuiEszkozok {
	
	enum panelTipus {
		LISTAZAS, KIVALASZTAS, BESZERZES, SZAMLA
	}
	
	private static final Map<Integer, String> HIBAUZENETEK = new HashMap<Integer, String>();
	
	private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();
	private static final Dimension SCREEN_SIZE = TOOLKIT.getScreenSize();
	private static final Rectangle ALAP_KEPERNYO_MERET = alapKepernyoMeret();
	
	private static Felhasznalo felhasznalo = null;
	
	private static String host = ClientProperties.getInstance().getServerHost();
	private static int port = 0;
	private static Socket socket = null;
	private static ObjectOutputStream outputStream = null;
	private static ObjectInputStream inputStream = null;
	
	private static Ceg uzemelteto = null;
	private static Ceg partner = null;
	private static Termek termek = null;
	
	static {
		try {
			port = Integer.parseInt(ClientProperties.getInstance().getServerPort());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Nem található vagy sérült a data/client.properties fájl", "NumberFormatException", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		HIBAUZENETEK.put(0, "Nem elérhetõ az adatbázisszerver");
		HIBAUZENETEK.put(1406, "Valamelyik mezõ túl hosszú adatot tartalmaz");
		HIBAUZENETEK.put(1451, "Nem törölhetõ mert egy másik táblában hivatkozás található az elemre");
	}
	
	private GuiEszkozok() {}

	private static Rectangle alapKepernyoMeret() {
		int width = SCREEN_SIZE.width - SCREEN_SIZE.width/3;
		int height = SCREEN_SIZE.height - SCREEN_SIZE.height/3;
		int x = (SCREEN_SIZE.width - width)/2;
		int y = (SCREEN_SIZE.height - height)/2;
		return new Rectangle(x, y, width, height);
	}
	
	public static String getHibauzenetek(int hibakod) {
		if(HIBAUZENETEK.containsKey(hibakod))
			return HIBAUZENETEK.get(hibakod);
		return "A hibakódot közölje a rendszergazdával és kérje segítségét";
	}
	
	public static Rectangle getAlapKepernyoMeret() {
		return ALAP_KEPERNYO_MERET;
	}
	
	public static Felhasznalo getFelhasznalo() {
		return felhasznalo;
	}
	
	public static void setFelhasznalo(Felhasznalo belepoFelhasznalo) {
		felhasznalo = belepoFelhasznalo;
	}
	
	public static Ceg getUzemeltetoCeg() {
		return uzemelteto;
	}
	
	public static void setUzemeltetoCeg(Ceg beallitott) {
		uzemelteto = beallitott;
	}
	
	public static Ceg getPartner() {
		return partner;
	}
	
	public static void setPartner(Ceg beallitott) {
		partner = beallitott;
	}
	
	public static Termek getTermek() {
		return termek;
	}
	
	public static void setTermek(Termek beallitott) {
		termek = beallitott;
	}
	
	public static JTextArea getSzamlaTextArea(Szamla sz) {
		JTextArea ta = new JTextArea();
		ta.setEditable(false);
		ta.setFont(new Font(Font.DIALOG, Font.PLAIN, 10));
		ta.append("Eladó neve: " + sz.getElado().getNev() + "\tVevõ neve: " + sz.getVevo().getNev());
		ta.append("\nEladó adószáma: " + sz.getElado().getAdoszam() + "\tVevõ adószáma: " + sz.getVevo().getAdoszam());
		ta.append("\nEladó címe: " + sz.getElado().getCim() + "\tVevõ címe: " + sz.getVevo().getCim());
		return ta;
	}
	
	public static ObjectOutputStream getOutputStream() {
		return outputStream;
	}
	
	public static ObjectInputStream getInputStream() {
		return inputStream;
	}
	
	public static boolean kapcsolatokNyitasa() {
		boolean siker = false;
		try {
			socket = new Socket(host, port);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
			siker = true;
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Nem elérhetõ a szerver a data/client.properties fájl beállításai alapján\n" +
					"Lehetséges, hogy nincs is elindítva az alkalmazásszerver", "UnknownHostException", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Nem elérhetõ a szerver a data/client.properties fájl beállításai alapján\n" +
					"Lehetséges, hogy nincs is elindítva az alkalmazásszerver", "IOException", JOptionPane.ERROR_MESSAGE);
		}
		return siker;
	}
	
	public static void kapcsolatokZarasa() {
		try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
