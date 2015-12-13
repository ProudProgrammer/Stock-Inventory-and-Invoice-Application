package server.main;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import common.entities.Felhasznalo;
import common.entities.Felhasznalo.RendszergazdaJog;

import server.database.DataBaseOperationException;
import server.database.DataBaseOperations;
import server.settings.ServerProperties;

public class Server {
	
	private static boolean systemTrayTamogatottE = false;
	private static TrayIcon trayIcon;
	
	private static int port = 0;
	
	private static InetAddress serverAddress;
	private static boolean voltEUnknownHostException = false;
	static {
		try {
			serverAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException ex) {
			voltEUnknownHostException = true;
			System.out.println("UnknownHostException: localhost lekérdezés sikertelen");
		}
	}
	
	public static void main(String[] args) {
		
		final CountDownLatch stop = new CountDownLatch(1);
		
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
                stop.countDown();
            }
        });
		
		try {
			stop.await();
		} catch (InterruptedException e1) {
			System.out.println("InterruptedException: Nem sikerült a main szálat megszakítani a gui inicializálásához. Újraindítás szükséges");
			JOptionPane.showMessageDialog(null, "Nem sikerült a main szálat megszakítani a gui inicializálásához\nÚjraindítás szükséges", "InterruptedException", JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			port = Integer.parseInt(ServerProperties.getInstance().getServerPort());
		} catch (NumberFormatException e) {
			System.out.println("NumberFormatException: Nem található vagy sérült a data/server.properties fájl");
			JOptionPane.showMessageDialog(null, "Nem található vagy sérült a data/server.properties fájl", "NumberFormatException", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("IOException: Nem található vagy sérült a data/server.properties fájl, vagy foglalt a port");
			JOptionPane.showMessageDialog(null, "Nem található vagy sérült a data/server.properties fájl, vagy foglalt a port", "IOException", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		if(systemTrayTamogatottE)
			trayIcon.displayMessage("Készletnyilvántartó- és számlázó alkalmazás", "A szerver elindult a " + port + " számú porton", TrayIcon.MessageType.INFO);
		System.out.println("Készlenyilvántartó- és számlázó alkalmazás\n" +
				"A szerver elindult a " + port + " számú porton");
		if(voltEUnknownHostException)
			System.out.println("UnknownHostException: localhost lekérdezés sikertelen");
		else
			System.out.println("LocalHost: " + serverAddress.getHostName() + ", " + serverAddress.getHostAddress());
		
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		while(true) {
			try {
				socket = serverSocket.accept();
				executorService.execute(new Servlet(socket));
			} catch (IOException e) {
				System.out.println("IOException: Kliens kapcsolódás közben hiba lépett fel");
			}
		}

	}
	
	private static void createAndShowGUI() {
        if(!SystemTray.isSupported()) {
            System.out.println("SystemTray nem támogatott");
            return;
        } else
        	systemTrayTamogatottE = true;
        
        final PopupMenu popup = new PopupMenu();
    	trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage("images/Pie Chart.png"));
        final SystemTray tray = SystemTray.getSystemTray();
         
        MenuItem initItem = new MenuItem("Adatbázis inicializálása");
        MenuItem nevjegyItem = new MenuItem("Névjegy");
        MenuItem kilepesItem = new MenuItem("Kilépés");
         
        popup.add(initItem);
        popup.addSeparator();
        popup.add(nevjegyItem);
        popup.addSeparator();
        popup.add(kilepesItem);
         
        trayIcon.setPopupMenu(popup);
         
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("AWTException: TrayIcon-t nem lehetett hozzáadni");
            return;
        }
        
        initItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		boolean kivetelNemVolt = true;
        		boolean nemVoltKapcsolatiHiba = true;
        		int option = JOptionPane.showConfirmDialog(null, "Az adatbázis inicializálása létrehozza az adatbázist (ha még nem létezik)\n" +
        				"És létrehoz egy admin rendszergazdajogú felhasználót (ha még nem létezik)\n" +
        				"Az inicializálás több percig is eltarthat, a visszaigazolást meg kell várni!\n" +
        				"Biztos, hogy ezt akarod?", "Adatbázis inicializálása", JOptionPane.YES_NO_OPTION);
        		if(option == JOptionPane.YES_OPTION) {
        			try {
        				DataBaseOperations.getThreadLocalInstance(null).createDataBase();
        			} catch (SQLException e1) {
        				kivetelNemVolt = false;
        				if(e1.getErrorCode() == 0) {
        					nemVoltKapcsolatiHiba = false;
        					JOptionPane.showMessageDialog(null, "Hiba történt az adatbázis inicializálása közben\n" +
            						"A data/server.properties fájlban megadott adatbázis elérés helytelen\n" +
            						"Vagy nincs elindítva az adatbázisszerver", "SQLException " + e1.getErrorCode() + " Error Code", JOptionPane.ERROR_MESSAGE);
        				}
        				else if(e1.getErrorCode() == 1050) {
        					JOptionPane.showMessageDialog(null, "Hiba történt az adatbázis létrehozása közben\n" +
        							"Az adatbázis már létre van hozva", "DataBaseOperationException", JOptionPane.WARNING_MESSAGE);
        				}
        				else if(e1.getErrorCode() == 1045) {
        					nemVoltKapcsolatiHiba = false;
        					JOptionPane.showMessageDialog(null, "Hiba történt az adatbázis inicializálása közben\n" +
            						"A data/server.properties fájlban megadott felhasználóval nem lehetséges az adatbázisba belépni\n" +
            						"Vagy nincs elég jogosultsága ennek a felhasználónak", "SQLException " + e1.getErrorCode() + " Error Code", JOptionPane.ERROR_MESSAGE);
        				}
        				
        			} 
        			if(nemVoltKapcsolatiHiba) {
        				try {
        					Felhasznalo felhasznalo = new Felhasznalo.Builder("admin", "admin").rendszergazdaJog(RendszergazdaJog.IGEN).build();
        					DataBaseOperations.getThreadLocalInstance(null).felhasznaloLetrehozasa(felhasznalo);
        				} catch (SQLException e1) {
        					kivetelNemVolt = false;
        					JOptionPane.showMessageDialog(null, "Hiba történt az admin rendszergazdajogú felhasználó létrehozása közben\n" +
        							"Nincs kapcsolat az adatbázissal\n" +
        							"Vagy a data/server.properties fájl beállításai helytelenek\n" +
        							"Vagy nincs elindítva az adatbázisszerver", "SQLException " + e1.getErrorCode() + " Error Code", JOptionPane.ERROR_MESSAGE);
        				} catch (DataBaseOperationException e1) {
        					kivetelNemVolt = false;
        					JOptionPane.showMessageDialog(null, "Hiba történt az admin rendszergazdajogú felhasználó létrehozása közben\n" +
        							"Már van admin létrehozva az adatbázisban", "DataBaseOperationException", JOptionPane.WARNING_MESSAGE);

        				}
        			}
        			if(kivetelNemVolt) {
        				JOptionPane.showMessageDialog(null, "Az adatbázis inicializálása megtörtént", "Adatbázis inicializálása", JOptionPane.INFORMATION_MESSAGE);
        			}
        		}
        	}
        });
        
        nevjegyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JOptionPane.showMessageDialog(null, "Készletnyilvántartó- és számlázó alkalmazás szerver\n" +
            			"Gábor Balázs szakdolgozata\nMikolci Egyetem 2012\n\n" +
            			"LocalHost: " + serverAddress.getHostName() + ", " + serverAddress.getHostAddress() + ":" + port, "Névjegy", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        kilepesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.out.println("A szerver leállt");
                System.exit(0);
            }
        });
	}
}
