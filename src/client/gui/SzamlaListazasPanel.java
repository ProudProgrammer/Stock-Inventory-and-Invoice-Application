package client.gui;

import java.awt.BorderLayout;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.print.NyomtatasiKepPanel;
import client.print.NyomtatasiKepPanel.Tipus;

import server.database.DataBaseOperationException.Eset;

import common.entities.Keres;
import common.entities.Szamla;
import common.entities.Termek;
import common.entities.Valasz;
import common.entities.Keres.Operations;

public class SzamlaListazasPanel extends JPanel implements ActionListener, ListSelectionListener, Printable {

	private static final long serialVersionUID = 1L;
	
	private List<Szamla> szamlak = null;
	private NyomtatasiKepPanel nyomtatasiKepPanel = null;
	
	private JPanel felsoPanel = new JPanel();
	private JScrollPane alsoGorgethetoPanel = new JScrollPane();
	
	private JPanel adatokPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	
	private static final JComboBox<Integer> EV_TOL = new JComboBox<Integer>();
	private static final JComboBox<Integer> HO_TOL = new JComboBox<Integer>();
	private static final JComboBox<Integer> NAP_TOL = new JComboBox<Integer>();
	private static final JComboBox<Integer> EV_IG = new JComboBox<Integer>();
	private static final JComboBox<Integer> HO_IG = new JComboBox<Integer>();
	private static final JComboBox<Integer> NAP_IG = new JComboBox<Integer>();
	
	private JTextField uzemeltetokodTextField = new JTextField(10);
	private JTextField partnerkodTextField = new JTextField(10);
	
	private static final JComboBox<String> SZAMLA_ALLAPOT = new JComboBox<String>();
	
	private JButton listazasButton = new JButton("Listázás", new ImageIcon("images/Search.png"));
	private JButton nyomtatasiKepButton = new JButton("Nyomtatási kép", new ImageIcon("images/Picture.png"));
	private JButton nyomtatasButton = new JButton("Nyomtatás", new ImageIcon("images/Print.png"));
	private JButton sztornozasButton = new JButton("Sztornózás", new ImageIcon("images/Delete.png"));
	private JButton mezokTorleseButton = new JButton("Mezõk törlése", new ImageIcon("images/Delete.png"));
	
	private Vector<Vector<String>> tablazatSorai = new Vector<Vector<String>>();
	private static final Vector<String> TABLAZAT_MEZO_NEVEK = new Vector<String>();
	private JTable szamlakTable = new JTable(null, TABLAZAT_MEZO_NEVEK);
	
	static {
		TABLAZAT_MEZO_NEVEK.add("Számlaszám");
		TABLAZAT_MEZO_NEVEK.add("Dátum");
		TABLAZAT_MEZO_NEVEK.add("Termékek");
		TABLAZAT_MEZO_NEVEK.add("Üzemeltetõ cég");
		TABLAZAT_MEZO_NEVEK.add("Partner");
		TABLAZAT_MEZO_NEVEK.add("Megjegyzés");
		TABLAZAT_MEZO_NEVEK.add("Létrehozta");
		TABLAZAT_MEZO_NEVEK.add("Állapot");
		TABLAZAT_MEZO_NEVEK.add("Állapotát beállította");
		
		for(int i = 2010; i <= 2020; i++) {
			EV_TOL.addItem(new Integer(i));
			EV_IG.addItem(new Integer(i));
		}
		for(int i = 1; i <= 12; i++) {
			HO_TOL.addItem(new Integer(i));
			HO_IG.addItem(new Integer(i));
		}
		for(int i = 1; i <= 31; i++) {
			NAP_TOL.addItem(new Integer(i));
			NAP_IG.addItem(new Integer(i));
		}
		EV_IG.setSelectedIndex(EV_IG.getItemCount()-1);
		HO_IG.setSelectedIndex(HO_IG.getItemCount()-1);
		NAP_IG.setSelectedIndex(NAP_IG.getItemCount()-1);
		
		SZAMLA_ALLAPOT.addItem("Mindegy");
		SZAMLA_ALLAPOT.addItem("Érvényes");
		SZAMLA_ALLAPOT.addItem("Sztornózott");
	}
	
	{
		nyomtatasiKepButton.setEnabled(false);
		nyomtatasButton.setEnabled(false);
		sztornozasButton.setEnabled(false);
		
		adatokPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		adatokPanel.add(new JLabel("Idõpont tartomány:"));
		adatokPanel.add(EV_TOL);
		adatokPanel.add(HO_TOL);
		adatokPanel.add(NAP_TOL);
		adatokPanel.add(new JLabel(" - "));
		adatokPanel.add(EV_IG);
		adatokPanel.add(HO_IG);
		adatokPanel.add(NAP_IG);
		adatokPanel.add(new JLabel("Üzemeltetõkód:"));
		adatokPanel.add(uzemeltetokodTextField);
		adatokPanel.add(new JLabel("Partnerkód:"));
		adatokPanel.add(partnerkodTextField);
		adatokPanel.add(new JLabel("Állapot:"));
		adatokPanel.add(SZAMLA_ALLAPOT);
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(listazasButton);
		buttonPanel.add(nyomtatasiKepButton);
		buttonPanel.add(nyomtatasButton);
		buttonPanel.add(sztornozasButton);
		buttonPanel.add(mezokTorleseButton);
		
		felsoPanel.setLayout(new BorderLayout(10, 10));
		felsoPanel.setBorder(BorderFactory.createEmptyBorder(20, 1, 20, 1));
		felsoPanel.add(adatokPanel, BorderLayout.CENTER);
		felsoPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		alsoGorgethetoPanel.setViewportView(szamlakTable);
	}

	public SzamlaListazasPanel() {
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Számla listázás/sztornózás/nyomtatás"));
		setLayout(new GridLayout(2, 1));
		add(felsoPanel);
		add(alsoGorgethetoPanel);
		
		listazasButton.addActionListener(this);
		nyomtatasiKepButton.addActionListener(this);
		nyomtatasButton.addActionListener(this);
		sztornozasButton.addActionListener(this);
		mezokTorleseButton.addActionListener(this);
	}
	
	private void listazas() {
		nyomtatasiKepButton.setEnabled(false);
		nyomtatasButton.setEnabled(false);
		sztornozasButton.setEnabled(false);
		int uzemeltetokod = 0;
		try {
			uzemeltetokod = Integer.parseInt(uzemeltetokodTextField.getText());
		} catch (NumberFormatException e) {
			uzemeltetokodTextField.setText("");
		}
		int partnerkod = 0;
		try {
			partnerkod = Integer.parseInt(partnerkodTextField.getText());
		} catch (NumberFormatException e) {
			partnerkodTextField.setText("");
		}
		String szamlaAllapot = "";
		if(SZAMLA_ALLAPOT.getSelectedIndex() == 1)
			szamlaAllapot = "ERVENYES";
		else if(SZAMLA_ALLAPOT.getSelectedIndex() == 2)
			szamlaAllapot = "SZTORNOZOTT";
		Date datumtol = new Date(new GregorianCalendar(EV_TOL.getItemAt(EV_TOL.getSelectedIndex()), HO_TOL.getItemAt(HO_TOL.getSelectedIndex())-1, NAP_TOL.getItemAt(NAP_TOL.getSelectedIndex())).getTimeInMillis());
		Date datumig = new Date(new GregorianCalendar(EV_IG.getItemAt(EV_IG.getSelectedIndex()), HO_IG.getItemAt(HO_IG.getSelectedIndex())-1, NAP_IG.getItemAt(NAP_IG.getSelectedIndex())).getTimeInMillis());
		Object parameterek[] = { datumtol, datumig, uzemeltetokod == 0 ? "" : Integer.toString(uzemeltetokod), partnerkod == 0 ? "" : Integer.toString(partnerkod), szamlaAllapot };
		Valasz valasz = null;
		boolean kivetelNemVolt = true;
		if(GuiEszkozok.kapcsolatokNyitasa()) {
			try {
				GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.SZAMLAK_KIVALASZTASA, parameterek, GuiEszkozok.getFelhasznalo()));
				GuiEszkozok.getOutputStream().flush();
				valasz = (Valasz)GuiEszkozok.getInputStream().readObject();
			} catch (IOException e) {
				kivetelNemVolt = false;
				JOptionPane.showMessageDialog(this, "Nem elérhetõ a szerver a data/client.properties fájl beállításai alapján\n" +
						"Lehetséges, hogy nincs is elindítva az alkalmazásszerver", "IOException", JOptionPane.ERROR_MESSAGE);
			} catch (ClassNotFoundException e) {
				kivetelNemVolt = false;
				JOptionPane.showMessageDialog(this, "Az alkalmazás egyes részei nem elérhetõk. Telepítse újra", "ClassNotFoundException", JOptionPane.ERROR_MESSAGE);
			} finally {
				GuiEszkozok.kapcsolatokZarasa();
			}
			if(valasz != null && kivetelNemVolt) {
				if(valasz.getVoltESQLException()) {
					int hibakod = valasz.getSQLException().getErrorCode();
					JOptionPane.showMessageDialog(this, GuiEszkozok.getHibauzenetek(hibakod), "SQLException " + hibakod + " Error Code", JOptionPane.ERROR_MESSAGE);
				} else if (valasz.getAdat() != null ){
					try {
						@SuppressWarnings("unchecked")
						List<Szamla> lekerdezettSzamlak = (List<Szamla>)valasz.getAdat();
						szamlak = lekerdezettSzamlak;
						tablazatSorai.removeAllElements();
						DateFormat df = DateFormat.getDateInstance();
						for(Szamla sz : lekerdezettSzamlak) {
							String termekek = "";
							for(Termek t : sz.getTermekek()) {
								termekek += termekek.equals("") ? t.toString() : "; " + t.toString();
							}
							Vector<String> sor = new Vector<String>();
							sor.add(Long.toString(sz.getSorszam()));
							sor.add(df.format(sz.getDatum()));
							sor.add(termekek);
							sor.add(sz.getElado().toString());
							sor.add(sz.getVevo().toString());
							sor.add(sz.getMegjegyzes());
							sor.add(sz.getKeszitette().toString());
							sor.add(sz.getSzamlaAllapot().irottFormaban());
							sor.add(sz.getAllapotatBeallitotta().toString());
							tablazatSorai.add(sor);
						}
						szamlakTable = new JTable(tablazatSorai, TABLAZAT_MEZO_NEVEK) {

							private static final long serialVersionUID = 1L;
							
							public boolean isCellEditable(int rowIndex, int columnIndex) {
								return false;
							}
						};
						alsoGorgethetoPanel.setViewportView(szamlakTable);
						szamlakTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						szamlakTable.getSelectionModel().addListSelectionListener(this);
					} catch (ClassCastException e) {
						JOptionPane.showMessageDialog(this, "Programhiba. Értesítse a rendszergazdát", "ClassCastException", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else if(valasz == null) {
				JOptionPane.showMessageDialog(this, "Megszakadt a kapcsolat az alkalmazásszerverrel", "Nincs válasz a szervertõl", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == listazasButton) {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			listazas();
			setCursor(Cursor.getDefaultCursor());
		} else if(event.getSource() == nyomtatasiKepButton) {
			new NyomtatasiKepDialog(this, szamlak.get(szamlakTable.getSelectedRow()));
		} else if(event.getSource() == nyomtatasButton) {
			JFrame keret = new JFrame();
			nyomtatasiKepPanel = new NyomtatasiKepPanel(szamlak.get(szamlakTable.getSelectedRow()), Tipus.NYOMTATAS);
			keret.add(nyomtatasiKepPanel);
			keret.pack();
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintable(this);
			boolean ok = job.printDialog();
			if(ok) {
				try {
					job.print();
				} catch (PrinterException ex) {}
			}
		} else if(event.getSource() == sztornozasButton) {
			long szamlaszam = Long.parseLong(tablazatSorai.get(szamlakTable.getSelectedRow()).get(0));
			int confirm = JOptionPane.showConfirmDialog(this, "Biztos, hogy sztornózni akarod a " + szamlaszam + " számlaszámú számlát?", "Számla sztornózása", JOptionPane.YES_NO_OPTION);
			if(confirm == JOptionPane.NO_OPTION)
				return;
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			nyomtatasiKepButton.setEnabled(false);
			nyomtatasButton.setEnabled(false);
			sztornozasButton.setEnabled(false);
			Object parameterek[] = { szamlaszam };
			Valasz valasz = null;
			boolean kivetelNemVolt = true;
			if(GuiEszkozok.kapcsolatokNyitasa()) {
				try {
					GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.SZAMLA_SZTORNOZASA, parameterek, GuiEszkozok.getFelhasznalo()));
					GuiEszkozok.getOutputStream().flush();
					valasz = (Valasz)GuiEszkozok.getInputStream().readObject();
				} catch (IOException e) {
					kivetelNemVolt = false;
					JOptionPane.showMessageDialog(this, "Nem elérhetõ a szerver a data/client.properties fájl beállításai alapján\n" +
							"Lehetséges, hogy nincs is elindítva az alkalmazásszerver", "IOException", JOptionPane.ERROR_MESSAGE);
				} catch (ClassNotFoundException e) {
					kivetelNemVolt = false;
					JOptionPane.showMessageDialog(this, "Az alkalmazás egyes részei nem elérhetõk. Telepítse újra", "ClassNotFoundException", JOptionPane.ERROR_MESSAGE);
				} finally {
					GuiEszkozok.kapcsolatokZarasa();
				}
				if(valasz != null && kivetelNemVolt) {
					if(valasz.getVoltESQLException()) {
						int hibakod = valasz.getSQLException().getErrorCode();
						JOptionPane.showMessageDialog(this, GuiEszkozok.getHibauzenetek(hibakod), "SQLException " + hibakod + " Error Code", JOptionPane.ERROR_MESSAGE);
					} else if(!valasz.getSikeresDataBaseOperation()) {
						if(valasz.getEset() == Eset.A_SZAMLA_MAR_SZTORNOZOTT)
							JOptionPane.showMessageDialog(this, "A számla már sztornózva van\n" +
									"Lehetséges, hogy egy másik kezelõ éppen sztornózta", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
						else
							JOptionPane.showMessageDialog(this, valasz.getEset(), "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, szamlaszam + " számlaszámú számla sztornózva lett", "Számla sztornózása", JOptionPane.INFORMATION_MESSAGE);
					}
				} else if(valasz == null) {
					JOptionPane.showMessageDialog(this, "Megszakadt a kapcsolat az alkalmazásszerverrel", "Nincs válasz a szervertõl", JOptionPane.ERROR_MESSAGE);
				}
			}
			listazas();
			setCursor(Cursor.getDefaultCursor());
		} else if(event.getSource() == mezokTorleseButton) {
			uzemeltetokodTextField.setText("");
			partnerkodTextField.setText("");
			EV_TOL.setSelectedIndex(0);
			HO_TOL.setSelectedIndex(0);
			NAP_TOL.setSelectedIndex(0);
			EV_IG.setSelectedIndex(EV_IG.getItemCount()-1);
			HO_IG.setSelectedIndex(HO_IG.getItemCount()-1);
			NAP_IG.setSelectedIndex(NAP_IG.getItemCount()-1);
			SZAMLA_ALLAPOT.setSelectedIndex(0);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if(event.getValueIsAdjusting())
            return;
		if(szamlakTable.getSelectedColumnCount() == 1 & szamlakTable.getSelectedRow() > -1) {
			if(tablazatSorai.get(szamlakTable.getSelectedRow()).get(7).equals("sztornózott")) {
				sztornozasButton.setEnabled(false);
				nyomtatasButton.setEnabled(false);
			} else {
				sztornozasButton.setEnabled(true);
				nyomtatasButton.setEnabled(true);
			}
			nyomtatasiKepButton.setEnabled(true);
		} else {
			nyomtatasiKepButton.setEnabled(false);
			nyomtatasButton.setEnabled(false);
			sztornozasButton.setEnabled(false);
		}
	}

	@Override
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		if (page > 0) {
			return NO_SUCH_PAGE;
		}
		Graphics2D g2d = (Graphics2D)g;
		g2d.translate(pf.getImageableX()+30, pf.getImageableY()+30);
		nyomtatasiKepPanel.printAll(g);
		return PAGE_EXISTS;
	}
}


