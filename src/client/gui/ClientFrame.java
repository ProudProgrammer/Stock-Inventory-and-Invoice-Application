package client.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import common.entities.Felhasznalo.RendszergazdaJog;

public class ClientFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final JMenuBar menuBar = new JMenuBar();
	
	private final JMenu keszletnyilvantartoMenu = new JMenu("K�szletnyilv�ntart�");
	private final JMenuItem kijelentkezesMenuItem = new JMenuItem("Kijelentkez�s", new ImageIcon("images/Loading.png"));
	private final JMenuItem kilepesMenuItem = new JMenuItem("Kil�p�s", new ImageIcon("images/Exit.png"));
	
	private final JMenu termekkezelesMenu = new JMenu("Termekkezel�s");
	private final JMenuItem ujTermekMenuItem = new JMenuItem("�j term�k felvitele", new ImageIcon("images/Add.png"));
	private final JMenuItem termekListazasMenuItem = new JMenuItem("Term�k list�z�s/m�dos�t�s/t�rl�s", new ImageIcon("images/Delete.png"));
	
	private final JMenu partnerkezelesMenu = new JMenu("Partnerkezel�s");
	private final JMenuItem ujPartnerMenuItem = new JMenuItem("�j partner felvitele", new ImageIcon("images/Add.png"));
	private final JMenuItem partnerListazasMenuItem = new JMenuItem("Partner list�z�s/m�dos�t�s/t�rl�s", new ImageIcon("images/Delete.png"));
	
	private final JMenu beszerzesekMenu = new JMenu("Beszerz�sek");
	private final JMenuItem ujBeszerzesMenuItem = new JMenuItem("�j beszerz�s felvitele", new ImageIcon("images/Add.png"));
	private final JMenuItem beszerzesListazasMenuItem = new JMenuItem("Beszerz�s list�z�s", new ImageIcon("images/Delete.png"));
	
	private final JMenu szamlakMenu = new JMenu("Szaml�k");
	private final JMenuItem ujSzamlaMenuItem = new JMenuItem("�j sz�mla felvitele", new ImageIcon("images/Add.png"));
	private final JMenuItem szamlaListazasMenuItem = new JMenuItem("Sz�mla list�z�s/sztorn�z�s/nyomtat�s", new ImageIcon("images/Delete.png"));

	private final JMenu karbantartasMenu = new JMenu("Karbantart�s");
	private final JMenu felhasznalokMenu = new JMenu("Felhaszn�l�k kezel�se");
	private final JMenuItem ujFelhasznaloMenuItem = new JMenuItem("�j felhaszn�l� felvitele", new ImageIcon("images/Add.png"));
	private final JMenuItem felhasznaloListazasMenuItem = new JMenuItem("Felhaszn�l� list�z�s/m�dos�t�s/t�rl�s", new ImageIcon("images/Delete.png"));
	private final JMenu uzemeltetoCegMenu = new JMenu("�zemeltet� c�gek kezel�se");
	private final JMenuItem ujUzemeltetoCegMenuItem = new JMenuItem("�j �zemeltet� c�g felvitele", new ImageIcon("images/Add.png"));
	private final JMenuItem uzemeltetoCegListazasMenuItem = new JMenuItem("�zemeltet� c�g list�z�s/m�dos�t�s/t�rl�s", new ImageIcon("images/Delete.png"));

	private JPanel tartalomPanel = new JPanel();
	private JPanel nyitoPanel = new NyitoPanel();
	
	private JPanel ujTermekPanel;
	private JPanel termekListazasPanel;
	private JPanel ujPartnerPanel;
	private JPanel partnerListazasPanel;
	private JPanel ujBeszerzesPanel;
	private JPanel beszerzesListazasPanel;
	private JPanel ujSzamlaPanel;
	private JPanel szamlaListazasPanel;
	private JPanel ujFelhasznaloPanel;
	private JPanel felhasznaloListazasPanel;
	private JPanel ujUzemeltetoCegPanel;
	private JPanel uzemeltetoCegListazasPanel;
	
	private JPanel aktivPanel;
 	
	{
		keszletnyilvantartoMenu.add(kijelentkezesMenuItem);
		keszletnyilvantartoMenu.addSeparator();
		keszletnyilvantartoMenu.add(kilepesMenuItem);
		
		termekkezelesMenu.add(ujTermekMenuItem);
		termekkezelesMenu.add(termekListazasMenuItem);
		
		partnerkezelesMenu.add(ujPartnerMenuItem);
		partnerkezelesMenu.add(partnerListazasMenuItem);
		
		beszerzesekMenu.add(ujBeszerzesMenuItem);
		beszerzesekMenu.add(beszerzesListazasMenuItem);
		
		szamlakMenu.add(ujSzamlaMenuItem);
		szamlakMenu.add(szamlaListazasMenuItem);
		
		felhasznalokMenu.setIcon(new ImageIcon("images/Profile.png"));
		felhasznalokMenu.add(ujFelhasznaloMenuItem);
		felhasznalokMenu.add(felhasznaloListazasMenuItem);
		uzemeltetoCegMenu.setIcon(new ImageIcon("images/Bar Chart.png"));
		uzemeltetoCegMenu.add(ujUzemeltetoCegMenuItem);
		uzemeltetoCegMenu.add(uzemeltetoCegListazasMenuItem);
		karbantartasMenu.add(felhasznalokMenu);
		karbantartasMenu.add(uzemeltetoCegMenu);
		
		menuBar.add(keszletnyilvantartoMenu);
		menuBar.add(termekkezelesMenu);
		menuBar.add(partnerkezelesMenu);
		menuBar.add(beszerzesekMenu);
		menuBar.add(szamlakMenu);
		menuBar.add(karbantartasMenu);
		
		karbantartasMenu.setVisible(false);
		
		tartalomPanel.setLayout(new BorderLayout());
		
		aktivPanel = nyitoPanel;
	}

	public ClientFrame() {
		
		setTitle("K�szletnyilv�ntart�- �s sz�ml�z� alkalmaz�s");
		setBounds(GuiEszkozok.getAlapKepernyoMeret());
		setJMenuBar(menuBar);
		setIconImage(Toolkit.getDefaultToolkit().createImage("images/Pie Chart.png"));
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		setContentPane(tartalomPanel);
		tartalomPanel.add(nyitoPanel, BorderLayout.NORTH);
		
		kijelentkezesMenuItem.addActionListener(this);
		kilepesMenuItem.addActionListener(this);
		ujTermekMenuItem.addActionListener(this);
		termekListazasMenuItem.addActionListener(this);
		ujPartnerMenuItem.addActionListener(this);
		partnerListazasMenuItem.addActionListener(this);
		ujBeszerzesMenuItem.addActionListener(this);
		beszerzesListazasMenuItem.addActionListener(this);
		ujSzamlaMenuItem.addActionListener(this);
		szamlaListazasMenuItem.addActionListener(this);
		ujFelhasznaloMenuItem.addActionListener(this);
		felhasznaloListazasMenuItem.addActionListener(this);
		ujUzemeltetoCegMenuItem.addActionListener(this);
		uzemeltetoCegListazasMenuItem.addActionListener(this);
		
		setVisible(true);
		
		new BejelentkezesDialog(this);
		bejelentkezesUtaniBeallitasok();
		
	}
	
	private void panelCsere(JPanel panel) {
		aktivPanel.setVisible(false);
		tartalomPanel.remove(aktivPanel);
		if(panel == nyitoPanel)
			tartalomPanel.add(panel, BorderLayout.NORTH);
		else
			tartalomPanel.add(panel, BorderLayout.CENTER);
		panel.setVisible(true);
		aktivPanel = panel;
	}

	private void bejelentkezesUtaniBeallitasok() {
		if(GuiEszkozok.getFelhasznalo().getRendszergazdaJog() == RendszergazdaJog.IGEN) {
			karbantartasMenu.setVisible(true);
			setTitle("K�szletnyilv�ntart�- �s sz�ml�z� alkalmaz�s - " + GuiEszkozok.getFelhasznalo().getNev() + " Rendszergazda");
		} else
			setTitle("K�szletnyilv�ntart�- �s sz�ml�z� alkalmaz�s - " + GuiEszkozok.getFelhasznalo().getNev());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == kijelentkezesMenuItem) {
			setTitle("K�szletnyilv�ntart�- �s sz�ml�z� alkalmaz�s");
			karbantartasMenu.setVisible(false);
			if(aktivPanel != nyitoPanel)
				panelCsere(nyitoPanel);
			new BejelentkezesDialog(this);
			bejelentkezesUtaniBeallitasok();
			return;
		}
		if(e.getSource() == kilepesMenuItem) {
			System.exit(0);
		}
		if(e.getSource() == ujTermekMenuItem) {
			if(ujTermekPanel == null)
				ujTermekPanel = new UjTermekPanel();
			if(aktivPanel != ujTermekPanel)
				panelCsere(ujTermekPanel);
			return;
		}
		if(e.getSource() == termekListazasMenuItem) {
			if(termekListazasPanel == null)
				termekListazasPanel = new TermekListazasPanel(GuiEszkozok.panelTipus.LISTAZAS);
			if(aktivPanel != termekListazasPanel)
				panelCsere(termekListazasPanel);
			return;
		}
		if(e.getSource() == ujPartnerMenuItem) {
			if(ujPartnerPanel == null)
				ujPartnerPanel = new UjPartnerPanel();
			if(aktivPanel != ujPartnerPanel)
				panelCsere(ujPartnerPanel);
			return;
		}
		if(e.getSource() == partnerListazasMenuItem) {
			if(partnerListazasPanel == null)
				partnerListazasPanel = new PartnerListazasPanel(GuiEszkozok.panelTipus.LISTAZAS);
			if(aktivPanel != partnerListazasPanel)
				panelCsere(partnerListazasPanel);
			return;
		}
		if(e.getSource() == ujBeszerzesMenuItem) {
			if(ujBeszerzesPanel == null)
				ujBeszerzesPanel = new UjBeszerzesVagySzamlaPanel(GuiEszkozok.panelTipus.BESZERZES);
			if(aktivPanel != ujBeszerzesPanel)
				panelCsere(ujBeszerzesPanel);
			return;
		}
		if(e.getSource() == beszerzesListazasMenuItem) {
			if(beszerzesListazasPanel == null)
				beszerzesListazasPanel = new BeszerzesListazasPanel();
			if(aktivPanel != beszerzesListazasPanel)
				panelCsere(beszerzesListazasPanel);
			return;
		}
		if(e.getSource() == ujSzamlaMenuItem) {
			if(ujSzamlaPanel == null)
				ujSzamlaPanel = new UjBeszerzesVagySzamlaPanel(GuiEszkozok.panelTipus.SZAMLA);
			if(aktivPanel != ujSzamlaPanel)
				panelCsere(ujSzamlaPanel);
			return;
		}
		if(e.getSource() == szamlaListazasMenuItem) {
			if(szamlaListazasPanel == null)
				szamlaListazasPanel = new SzamlaListazasPanel();
			if(aktivPanel != szamlaListazasPanel)
				panelCsere(szamlaListazasPanel);
			return;
		}
		if(e.getSource() == ujFelhasznaloMenuItem) {
			if(ujFelhasznaloPanel == null)
				ujFelhasznaloPanel = new UjFelhasznaloPanel();
			if(aktivPanel != ujFelhasznaloPanel)
				panelCsere(ujFelhasznaloPanel);
			return;
		}
		if(e.getSource() == felhasznaloListazasMenuItem) {
			if(felhasznaloListazasPanel == null)
				felhasznaloListazasPanel = new FelhasznaloListazasPanel();
			if(aktivPanel != felhasznaloListazasPanel)
				panelCsere(felhasznaloListazasPanel);
			return;
		}
		if(e.getSource() == ujUzemeltetoCegMenuItem) {
			if(ujUzemeltetoCegPanel == null)
				ujUzemeltetoCegPanel = new UjUzemeltetoCegPanel();
			if(aktivPanel != ujUzemeltetoCegPanel)
				panelCsere(ujUzemeltetoCegPanel);
			return;
		}
		if(e.getSource() == uzemeltetoCegListazasMenuItem) {
			if(uzemeltetoCegListazasPanel == null)
				uzemeltetoCegListazasPanel = new UzemeltetoCegListazasPanel(GuiEszkozok.panelTipus.LISTAZAS);
			if(aktivPanel != uzemeltetoCegListazasPanel)
				panelCsere(uzemeltetoCegListazasPanel);
			return;
		}
		
	}
}
