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
			System.out.println("UnknownHostException: localhost lek�rdez�s sikertelen");
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
			System.out.println("InterruptedException: Nem siker�lt a main sz�lat megszak�tani a gui inicializ�l�s�hoz. �jraind�t�s sz�ks�ges");
			JOptionPane.showMessageDialog(null, "Nem siker�lt a main sz�lat megszak�tani a gui inicializ�l�s�hoz\n�jraind�t�s sz�ks�ges", "InterruptedException", JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			port = Integer.parseInt(ServerProperties.getInstance().getServerPort());
		} catch (NumberFormatException e) {
			System.out.println("NumberFormatException: Nem tal�lhat� vagy s�r�lt a data/server.properties f�jl");
			JOptionPane.showMessageDialog(null, "Nem tal�lhat� vagy s�r�lt a data/server.properties f�jl", "NumberFormatException", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("IOException: Nem tal�lhat� vagy s�r�lt a data/server.properties f�jl, vagy foglalt a port");
			JOptionPane.showMessageDialog(null, "Nem tal�lhat� vagy s�r�lt a data/server.properties f�jl, vagy foglalt a port", "IOException", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		if(systemTrayTamogatottE)
			trayIcon.displayMessage("K�szletnyilv�ntart�- �s sz�ml�z� alkalmaz�s", "A szerver elindult a " + port + " sz�m� porton", TrayIcon.MessageType.INFO);
		System.out.println("K�szlenyilv�ntart�- �s sz�ml�z� alkalmaz�s\n" +
				"A szerver elindult a " + port + " sz�m� porton");
		if(voltEUnknownHostException)
			System.out.println("UnknownHostException: localhost lek�rdez�s sikertelen");
		else
			System.out.println("LocalHost: " + serverAddress.getHostName() + ", " + serverAddress.getHostAddress());
		
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		while(true) {
			try {
				socket = serverSocket.accept();
				executorService.execute(new Servlet(socket));
			} catch (IOException e) {
				System.out.println("IOException: Kliens kapcsol�d�s k�zben hiba l�pett fel");
			}
		}

	}
	
	private static void createAndShowGUI() {
        if(!SystemTray.isSupported()) {
            System.out.println("SystemTray nem t�mogatott");
            return;
        } else
        	systemTrayTamogatottE = true;
        
        final PopupMenu popup = new PopupMenu();
    	trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage("images/Pie Chart.png"));
        final SystemTray tray = SystemTray.getSystemTray();
         
        MenuItem initItem = new MenuItem("Adatb�zis inicializ�l�sa");
        MenuItem nevjegyItem = new MenuItem("N�vjegy");
        MenuItem kilepesItem = new MenuItem("Kil�p�s");
         
        popup.add(initItem);
        popup.addSeparator();
        popup.add(nevjegyItem);
        popup.addSeparator();
        popup.add(kilepesItem);
         
        trayIcon.setPopupMenu(popup);
         
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("AWTException: TrayIcon-t nem lehetett hozz�adni");
            return;
        }
        
        initItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		boolean kivetelNemVolt = true;
        		boolean nemVoltKapcsolatiHiba = true;
        		int option = JOptionPane.showConfirmDialog(null, "Az adatb�zis inicializ�l�sa l�trehozza az adatb�zist (ha m�g nem l�tezik)\n" +
        				"�s l�trehoz egy admin rendszergazdajog� felhaszn�l�t (ha m�g nem l�tezik)\n" +
        				"Az inicializ�l�s t�bb percig is eltarthat, a visszaigazol�st meg kell v�rni!\n" +
        				"Biztos, hogy ezt akarod?", "Adatb�zis inicializ�l�sa", JOptionPane.YES_NO_OPTION);
        		if(option == JOptionPane.YES_OPTION) {
        			try {
        				DataBaseOperations.getThreadLocalInstance(null).createDataBase();
        			} catch (SQLException e1) {
        				kivetelNemVolt = false;
        				if(e1.getErrorCode() == 0) {
        					nemVoltKapcsolatiHiba = false;
        					JOptionPane.showMessageDialog(null, "Hiba t�rt�nt az adatb�zis inicializ�l�sa k�zben\n" +
            						"A data/server.properties f�jlban megadott adatb�zis el�r�s helytelen\n" +
            						"Vagy nincs elind�tva az adatb�zisszerver", "SQLException " + e1.getErrorCode() + " Error Code", JOptionPane.ERROR_MESSAGE);
        				}
        				else if(e1.getErrorCode() == 1050) {
        					JOptionPane.showMessageDialog(null, "Hiba t�rt�nt az adatb�zis l�trehoz�sa k�zben\n" +
        							"Az adatb�zis m�r l�tre van hozva", "DataBaseOperationException", JOptionPane.WARNING_MESSAGE);
        				}
        				else if(e1.getErrorCode() == 1045) {
        					nemVoltKapcsolatiHiba = false;
        					JOptionPane.showMessageDialog(null, "Hiba t�rt�nt az adatb�zis inicializ�l�sa k�zben\n" +
            						"A data/server.properties f�jlban megadott felhaszn�l�val nem lehets�ges az adatb�zisba bel�pni\n" +
            						"Vagy nincs el�g jogosults�ga ennek a felhaszn�l�nak", "SQLException " + e1.getErrorCode() + " Error Code", JOptionPane.ERROR_MESSAGE);
        				}
        				
        			} 
        			if(nemVoltKapcsolatiHiba) {
        				try {
        					Felhasznalo felhasznalo = new Felhasznalo.Builder("admin", "admin").rendszergazdaJog(RendszergazdaJog.IGEN).build();
        					DataBaseOperations.getThreadLocalInstance(null).felhasznaloLetrehozasa(felhasznalo);
        				} catch (SQLException e1) {
        					kivetelNemVolt = false;
        					JOptionPane.showMessageDialog(null, "Hiba t�rt�nt az admin rendszergazdajog� felhaszn�l� l�trehoz�sa k�zben\n" +
        							"Nincs kapcsolat az adatb�zissal\n" +
        							"Vagy a data/server.properties f�jl be�ll�t�sai helytelenek\n" +
        							"Vagy nincs elind�tva az adatb�zisszerver", "SQLException " + e1.getErrorCode() + " Error Code", JOptionPane.ERROR_MESSAGE);
        				} catch (DataBaseOperationException e1) {
        					kivetelNemVolt = false;
        					JOptionPane.showMessageDialog(null, "Hiba t�rt�nt az admin rendszergazdajog� felhaszn�l� l�trehoz�sa k�zben\n" +
        							"M�r van admin l�trehozva az adatb�zisban", "DataBaseOperationException", JOptionPane.WARNING_MESSAGE);

        				}
        			}
        			if(kivetelNemVolt) {
        				JOptionPane.showMessageDialog(null, "Az adatb�zis inicializ�l�sa megt�rt�nt", "Adatb�zis inicializ�l�sa", JOptionPane.INFORMATION_MESSAGE);
        			}
        		}
        	}
        });
        
        nevjegyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JOptionPane.showMessageDialog(null, "K�szletnyilv�ntart�- �s sz�ml�z� alkalmaz�s szerver\n" +
            			"G�bor Bal�zs szakdolgozata\nMikolci Egyetem 2012\n\n" +
            			"LocalHost: " + serverAddress.getHostName() + ", " + serverAddress.getHostAddress() + ":" + port, "N�vjegy", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        kilepesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.out.println("A szerver le�llt");
                System.exit(0);
            }
        });
	}
}
