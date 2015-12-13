package client.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import server.database.DataBaseOperationException.Eset;

import common.entities.Beszerzes;
import common.entities.Ceg;
import common.entities.Keres;
import common.entities.Szamla;
import common.entities.Termek;
import common.entities.Valasz;
import common.entities.Keres.Operations;

public class UjBeszerzesVagySzamlaPanel extends JPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	private GuiEszkozok.panelTipus panelTipus;
	
	private JPanel bevitelPanel = new JPanel();
	private JPanel bevitelAlsoPanel = new JPanel();
	private JPanel bevitelFelsoPanel = new JPanel();
	
	//---Termék Panel---
	
	private JPanel termekPanel = new JPanel();
	private JPanel termekFelsoPanel = new JPanel();
	private JPanel termekFelsoElsoSorPanel = new JPanel();
	private JPanel termekFelsoMasodikSorPanel = new JPanel();
	
	private JButton termekHozzaadasaButton = new JButton("Termék hozzáadása", new ImageIcon("images/Add.png"));
	private JButton termekKiveteleButton = new JButton("Hozzáadás törlése", new ImageIcon("images/Delete.png"));
	private JButton termekModositasButton = new JButton("Módosítás", new ImageIcon("images/Modify.png"));
	
	private JTextField mennyisegTextField = new JTextField(5);
	private JTextField beszerzesiArTextField = new JTextField(5);
	private JTextField afaTextField = new JTextField(5);
	
	private JLabel ar = new JLabel();
	
	private Termek termek = null;
	private Map<Integer, Termek> termekek = new HashMap<Integer, Termek>();
	
	private Vector<Vector<String>> termekTablazatSorai = new Vector<Vector<String>>();
	private final Vector<String> TERMEK_TABLAZAT_MEZO_NEVEK = new Vector<String>();
	private JTable termekTable = null;
	
	private JScrollPane termekTablePanel = new JScrollPane();
	
	//---Üzemeltetõ Panel---
	
	private JPanel uzemeltetoPanel = new JPanel();
	private JPanel uzemeltetoFelsoPanel = new JPanel();
	
	private JButton uzemeltetoHozzaadasaButton = new JButton("Üzemeltetõ cég kiválasztása", new ImageIcon("images/Profile.png"));
	private JButton uzemeltetoKiveteleButton = new JButton("Kiválasztás törlése", new ImageIcon("images/Delete.png"));
	
	private Ceg uzemelteto;
	
	private Vector<Vector<String>> uzemeltetoTablazatSorai = new Vector<Vector<String>>();
	private static final Vector<String> UZEMELTETO_TABLAZAT_MEZO_NEVEK = new Vector<String>();
	private JTable uzemeltetoTable = new JTable(null, UZEMELTETO_TABLAZAT_MEZO_NEVEK);
	
	private JScrollPane uzemeltetoTablePanel = new JScrollPane();
	
	//---Partner Panel---
	
	private JPanel partnerPanel = new JPanel();
	private JPanel partnerFelsoPanel = new JPanel();
	
	private JButton partnerHozzaadasaButton = new JButton("Partner kiválasztása", new ImageIcon("images/Profile.png"));
	private JButton partnerKiveteleButton = new JButton("Kiválasztás törlése", new ImageIcon("images/Delete.png"));
	
	private Ceg partner = null;
	
	private Vector<Vector<String>> partnerTablazatSorai = new Vector<Vector<String>>();
	private static final Vector<String> PARTNER_TABLAZAT_MEZO_NEVEK = new Vector<String>();
	private JTable partnerTable = new JTable(null, PARTNER_TABLAZAT_MEZO_NEVEK);
	
	private JScrollPane partnerTablePanel = new JScrollPane();
	
	//---Button Panel---
	
	private JPanel buttonPanel = new JPanel();
	
	private JButton letrehozasButton = new JButton("", new ImageIcon("images/Save.png"));
	private JButton mezokTorleseButton = new JButton("Összes mezõ törlése", new ImageIcon("images/Delete.png"));
	
	static {
		UZEMELTETO_TABLAZAT_MEZO_NEVEK.add("Üzemeltetõkód");
		UZEMELTETO_TABLAZAT_MEZO_NEVEK.add("Név");
		UZEMELTETO_TABLAZAT_MEZO_NEVEK.add("Cím");
		UZEMELTETO_TABLAZAT_MEZO_NEVEK.add("Adószám");
		UZEMELTETO_TABLAZAT_MEZO_NEVEK.add("Telefon");
		UZEMELTETO_TABLAZAT_MEZO_NEVEK.add("Fax");
		UZEMELTETO_TABLAZAT_MEZO_NEVEK.add("Email");
		UZEMELTETO_TABLAZAT_MEZO_NEVEK.add("Web");
		
		PARTNER_TABLAZAT_MEZO_NEVEK.add("Partnerkód");
		PARTNER_TABLAZAT_MEZO_NEVEK.add("Név");
		PARTNER_TABLAZAT_MEZO_NEVEK.add("Cím");
		PARTNER_TABLAZAT_MEZO_NEVEK.add("Adószám");
		PARTNER_TABLAZAT_MEZO_NEVEK.add("Telefon");
		PARTNER_TABLAZAT_MEZO_NEVEK.add("Fax");
		PARTNER_TABLAZAT_MEZO_NEVEK.add("Email");
		PARTNER_TABLAZAT_MEZO_NEVEK.add("Web");
	}
	
	{
		termekPanel.setLayout(new BorderLayout());
		termekFelsoPanel.setLayout(new GridLayout(2, 1));
		termekFelsoElsoSorPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		termekFelsoElsoSorPanel.add(termekHozzaadasaButton);
		termekFelsoElsoSorPanel.add(termekKiveteleButton);
		termekFelsoMasodikSorPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		termekFelsoMasodikSorPanel.add(new JLabel("Áfa százalékláb:"));
		termekFelsoMasodikSorPanel.add(afaTextField);
		termekFelsoMasodikSorPanel.add(ar);
		termekFelsoMasodikSorPanel.add(beszerzesiArTextField);
		termekFelsoMasodikSorPanel.add(new JLabel("Mennyiség:"));
		termekFelsoMasodikSorPanel.add(mennyisegTextField);
		termekFelsoMasodikSorPanel.add(termekModositasButton);
		termekFelsoPanel.add(termekFelsoElsoSorPanel);
		termekFelsoPanel.add(termekFelsoMasodikSorPanel);
		termekPanel.add(termekFelsoPanel, BorderLayout.NORTH);
		termekPanel.add(termekTablePanel, BorderLayout.CENTER);
		
		uzemeltetoPanel.setLayout(new BorderLayout());
		uzemeltetoFelsoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		uzemeltetoFelsoPanel.add(uzemeltetoHozzaadasaButton);
		uzemeltetoFelsoPanel.add(uzemeltetoKiveteleButton);
		uzemeltetoPanel.add(uzemeltetoFelsoPanel, BorderLayout.NORTH);
		uzemeltetoPanel.add(uzemeltetoTablePanel, BorderLayout.CENTER);
		
		partnerPanel.setLayout(new BorderLayout());
		partnerFelsoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		partnerFelsoPanel.add(partnerHozzaadasaButton);
		partnerFelsoPanel.add(partnerKiveteleButton);
		partnerPanel.add(partnerFelsoPanel, BorderLayout.NORTH);
		partnerPanel.add(partnerTablePanel, BorderLayout.CENTER);
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(letrehozasButton);
		buttonPanel.add(mezokTorleseButton);
		
		termekKiveteleButton.setEnabled(false);
		mennyisegTextField.setEnabled(false);
		beszerzesiArTextField.setEnabled(false);
		afaTextField.setEnabled(false);
		termekModositasButton.setEnabled(false);
		uzemeltetoKiveteleButton.setEnabled(false);
		partnerKiveteleButton.setEnabled(false);
		
		bevitelPanel.setLayout(new GridLayout(2, 1));
		bevitelPanel.add(bevitelFelsoPanel);
		bevitelPanel.add(bevitelAlsoPanel);
		
		bevitelFelsoPanel.setLayout(new BorderLayout());
		bevitelFelsoPanel.add(termekPanel);
		
		bevitelAlsoPanel.setLayout(new GridLayout(2, 1));
		bevitelAlsoPanel.add(uzemeltetoPanel);
		bevitelAlsoPanel.add(partnerPanel);
	}

	public UjBeszerzesVagySzamlaPanel(GuiEszkozok.panelTipus panelTipus) {
		this.panelTipus = panelTipus;
		
		if(panelTipus == GuiEszkozok.panelTipus.BESZERZES) {
			letrehozasButton.setText("Beszerzés létrehozása");
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED) , "Új beszerzés felvitele"));
			ar.setText("Beszerzési ár:");
			TERMEK_TABLAZAT_MEZO_NEVEK.add("Termékkód");
			TERMEK_TABLAZAT_MEZO_NEVEK.add("Megnevezés");
			TERMEK_TABLAZAT_MEZO_NEVEK.add("VTSZ");
			TERMEK_TABLAZAT_MEZO_NEVEK.add("Aktuális áfa százalékláb");
			TERMEK_TABLAZAT_MEZO_NEVEK.add("Aktuális nettó beszerzési ár");
			TERMEK_TABLAZAT_MEZO_NEVEK.add("Mennyiség egysége");
			TERMEK_TABLAZAT_MEZO_NEVEK.add("Beszerzési mennyiség");
		} else if(panelTipus == GuiEszkozok.panelTipus.SZAMLA) {
			letrehozasButton.setText("Számla létrehozása");
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED) , "Új számla felvitele"));
			ar.setText("Eladási ár:");
			TERMEK_TABLAZAT_MEZO_NEVEK.add("Termékkód");
			TERMEK_TABLAZAT_MEZO_NEVEK.add("Megnevezés");
			TERMEK_TABLAZAT_MEZO_NEVEK.add("VTSZ");
			TERMEK_TABLAZAT_MEZO_NEVEK.add("Aktuális áfa százalékláb");
			TERMEK_TABLAZAT_MEZO_NEVEK.add("Aktuális nettó eladási ár");
			TERMEK_TABLAZAT_MEZO_NEVEK.add("Mennyiség egysége");
			TERMEK_TABLAZAT_MEZO_NEVEK.add("Eladási mennyiség");
		}
		
		termekTable = new JTable(null, TERMEK_TABLAZAT_MEZO_NEVEK);
		
		termekTablePanel.setViewportView(termekTable);
		uzemeltetoTablePanel.setViewportView(uzemeltetoTable);
		partnerTablePanel.setViewportView(partnerTable);
		
		setLayout(new BorderLayout());
		add(bevitelPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		termekHozzaadasaButton.addActionListener(this);
		termekKiveteleButton.addActionListener(this);
		termekModositasButton.addActionListener(this);
		
		uzemeltetoHozzaadasaButton.addActionListener(this);
		uzemeltetoKiveteleButton.addActionListener(this);
		
		partnerHozzaadasaButton.addActionListener(this);
		partnerKiveteleButton.addActionListener(this);
		
		letrehozasButton.addActionListener(this);
		mezokTorleseButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == termekHozzaadasaButton) {
			GuiEszkozok.setTermek(null);
			new TermekKivalasztasDialog(this);
			if(GuiEszkozok.getTermek() == null)
				return;
			termek = GuiEszkozok.getTermek();
			termek.setMennyiseg(1);
			Vector<String> sor = new Vector<String>();
			if(panelTipus == GuiEszkozok.panelTipus.BESZERZES) {
				sor.add(Integer.toString(termek.getTermekkod()));
				sor.add(termek.getMegnevezes());
				sor.add(Integer.toString(termek.getVtsz()));
				sor.add(Double.toString(termek.getAfaSzazaleklab()));
				sor.add(Double.toString(termek.getEgysegNettoBeszerzesiAr()));
				sor.add(termek.getMennyisegEgysege().irottFormaban());
				sor.add(Integer.toString(1));
				termekTablazatSorai.removeAllElements();
				termekTablazatSorai.add(sor);
				termekTable = new JTable(termekTablazatSorai, TERMEK_TABLAZAT_MEZO_NEVEK) {

					private static final long serialVersionUID = 1L;
				
					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return false;
					}
				};
				termekTablePanel.setViewportView(termekTable);
				termekKiveteleButton.setEnabled(true);
				termekModositasButton.setEnabled(true);
				afaTextField.setEnabled(true);
				beszerzesiArTextField.setEnabled(true);
				mennyisegTextField.setEnabled(true);
				afaTextField.setText(Double.toString(termek.getAfaSzazaleklab()));
				beszerzesiArTextField.setText(Double.toString(termek.getEgysegNettoBeszerzesiAr()));
				mennyisegTextField.setText(Integer.toString(1));
			} else if (panelTipus == GuiEszkozok.panelTipus.SZAMLA) {
				if(termekek.containsKey(GuiEszkozok.getTermek().getTermekkod())) {
					JOptionPane.showMessageDialog(this, "A termék már a listában van", "Termék hozzáadása", JOptionPane.WARNING_MESSAGE);
				} else {
					sor.add(Integer.toString(termek.getTermekkod()));
					sor.add(termek.getMegnevezes());
					sor.add(Integer.toString(termek.getVtsz()));
					sor.add(Double.toString(termek.getAfaSzazaleklab()));
					sor.add(Double.toString(termek.getEgysegNettoEladasiAr()));
					sor.add(termek.getMennyisegEgysege().irottFormaban());
					sor.add(Integer.toString(1));
					termekTablazatSorai.add(sor);
					termekTable = new JTable(termekTablazatSorai, TERMEK_TABLAZAT_MEZO_NEVEK) {
	
						private static final long serialVersionUID = 1L;
					
						public boolean isCellEditable(int rowIndex, int columnIndex) {
							return false;
						}
					};
					termekTablePanel.setViewportView(termekTable);
					termekTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					termekTable.getSelectionModel().addListSelectionListener(this);
					termekek.put(GuiEszkozok.getTermek().getTermekkod(), GuiEszkozok.getTermek());
				}
			}
		} else if(e.getSource() == termekKiveteleButton) {
			if(panelTipus == GuiEszkozok.panelTipus.BESZERZES) {
				GuiEszkozok.setTermek(null);
				termekTablazatSorai.removeAllElements();
				termekTable = new JTable(null, TERMEK_TABLAZAT_MEZO_NEVEK) {

					private static final long serialVersionUID = 1L;
					
					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return false;
					}
				};
				termekTablePanel.setViewportView(termekTable);
			} else if (panelTipus == GuiEszkozok.panelTipus.SZAMLA) {
				termekek.remove(Integer.parseInt(termekTablazatSorai.get(termekTable.getSelectedRow()).get(0)));
				termekTablazatSorai.remove(termekTable.getSelectedRow());
				termekTable = new JTable(termekTablazatSorai, TERMEK_TABLAZAT_MEZO_NEVEK) {

					private static final long serialVersionUID = 1L;
					
					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return false;
					}
				};
				termekTablePanel.setViewportView(termekTable);
				termekTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				termekTable.getSelectionModel().addListSelectionListener(this);
			}
			termekKiveteleButton.setEnabled(false);
			termekModositasButton.setEnabled(false);
			afaTextField.setEnabled(false);
			beszerzesiArTextField.setEnabled(false);
			mennyisegTextField.setEnabled(false);
			afaTextField.setText("");
			beszerzesiArTextField.setText("");
			mennyisegTextField.setText("");
		} else if(e.getSource() == termekModositasButton) {
			double afaSzazalekLab = 0;
			try {
				afaSzazalekLab = Double.parseDouble(afaTextField.getText().trim());
			} catch(NumberFormatException ex) {
				afaTextField.setText(Double.toString(0));
			}
			double ar = 0;
			try {
				ar = Double.parseDouble(beszerzesiArTextField.getText().trim());
			} catch(NumberFormatException ex) {
				beszerzesiArTextField.setText(Double.toString(0));
			}
			int mennyiseg = 1;
			try {
				mennyiseg = Integer.parseInt(mennyisegTextField.getText().trim());
			} catch(NumberFormatException ex) {
				mennyisegTextField.setText(Integer.toString(0));
			}
			if(panelTipus == GuiEszkozok.panelTipus.BESZERZES) {
				termek.setAfaSzazaleklab(afaSzazalekLab);
				termek.setEgysegNettoBeszerzesiAr(ar);
				termek.setMennyiseg(mennyiseg);
				Vector<String> sor = new Vector<String>();
				sor.add(Integer.toString(termek.getTermekkod()));
				sor.add(termek.getMegnevezes());
				sor.add(Integer.toString(termek.getVtsz()));
				sor.add(Double.toString(termek.getAfaSzazaleklab()));
				sor.add(Double.toString(termek.getEgysegNettoBeszerzesiAr()));
				sor.add(termek.getMennyisegEgysege().irottFormaban());
				sor.add(Integer.toString(termek.getMennyiseg()));
				termekTablazatSorai.removeAllElements();
				termekTablazatSorai.add(sor);
				termekTable = new JTable(termekTablazatSorai, TERMEK_TABLAZAT_MEZO_NEVEK) {

					private static final long serialVersionUID = 1L;
				
					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return false;
					}
				};
				termekTablePanel.setViewportView(termekTable);
			} else if (panelTipus == GuiEszkozok.panelTipus.SZAMLA) {
				Termek kivalasztott = termekek.get(Integer.parseInt(termekTablazatSorai.get(termekTable.getSelectedRow()).get(0)));
				kivalasztott.setAfaSzazaleklab(afaSzazalekLab);
				kivalasztott.setEgysegNettoEladasiAr(ar);
				kivalasztott.setMennyiseg(mennyiseg);
				termekTablazatSorai.get(termekTable.getSelectedRow()).set(3, Double.toString(afaSzazalekLab));
				termekTablazatSorai.get(termekTable.getSelectedRow()).set(4, Double.toString(ar));
				termekTablazatSorai.get(termekTable.getSelectedRow()).set(6, Integer.toString(mennyiseg));
				termekTable = new JTable(termekTablazatSorai, TERMEK_TABLAZAT_MEZO_NEVEK) {

					private static final long serialVersionUID = 1L;
				
					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return false;
					}
				};
				termekTablePanel.setViewportView(termekTable);
				termekTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				termekTable.getSelectionModel().addListSelectionListener(this);
				termekKiveteleButton.setEnabled(false);
				termekModositasButton.setEnabled(false);
				afaTextField.setEnabled(false);
				beszerzesiArTextField.setEnabled(false);
				mennyisegTextField.setEnabled(false);
				afaTextField.setText("");
				beszerzesiArTextField.setText("");
				mennyisegTextField.setText("");
			}
		} else if(e.getSource() == uzemeltetoHozzaadasaButton) {
			GuiEszkozok.setUzemeltetoCeg(null);
			new UzemeltetoCegKivalasztasDialog(this);
			if(GuiEszkozok.getUzemeltetoCeg() == null)
				return;
			uzemelteto = GuiEszkozok.getUzemeltetoCeg();
			Vector<String> sor = new Vector<String>();
			sor.add(Integer.toString(uzemelteto.getId()));
			sor.add(uzemelteto.getNev());
			sor.add(uzemelteto.getCim().toString());
			sor.add(Long.toString(uzemelteto.getAdoszam()));
			sor.add(uzemelteto.getTelefon());
			sor.add(uzemelteto.getFax());
			sor.add(uzemelteto.getEmail());
			sor.add(uzemelteto.getWebcim());
			uzemeltetoTablazatSorai.removeAllElements();
			uzemeltetoTablazatSorai.add(sor);
			uzemeltetoTable = new JTable(uzemeltetoTablazatSorai, UZEMELTETO_TABLAZAT_MEZO_NEVEK) {

				private static final long serialVersionUID = 1L;
				
				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return false;
				}
			};
			uzemeltetoTablePanel.setViewportView(uzemeltetoTable);
			uzemeltetoKiveteleButton.setEnabled(true);
				
		} else if(e.getSource() == uzemeltetoKiveteleButton) {
			uzemelteto = null;
			uzemeltetoTablazatSorai.removeAllElements();
			uzemeltetoTable = new JTable(null, UZEMELTETO_TABLAZAT_MEZO_NEVEK) {

				private static final long serialVersionUID = 1L;
				
				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return false;
				}
			};
			uzemeltetoTablePanel.setViewportView(uzemeltetoTable);
			uzemeltetoKiveteleButton.setEnabled(false);
			
		} else if(e.getSource() == partnerHozzaadasaButton) {
			GuiEszkozok.setPartner(null);
			new PartnerKivalasztasDialog(this);
			if(GuiEszkozok.getPartner() == null)
				return;
			partner = GuiEszkozok.getPartner();
			Vector<String> sor = new Vector<String>();
			sor.add(Integer.toString(partner.getId()));
			sor.add(partner.getNev());
			sor.add(partner.getCim().toString());
			sor.add(Long.toString(partner.getAdoszam()));
			sor.add(partner.getTelefon());
			sor.add(partner.getFax());
			sor.add(partner.getEmail());
			sor.add(partner.getWebcim());
			partnerTablazatSorai.removeAllElements();
			partnerTablazatSorai.add(sor);
			partnerTable = new JTable(partnerTablazatSorai, PARTNER_TABLAZAT_MEZO_NEVEK) {

				private static final long serialVersionUID = 1L;
				
				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return false;
				}
			};
			partnerTablePanel.setViewportView(partnerTable);
			partnerKiveteleButton.setEnabled(true);
			
		} else if(e.getSource() == partnerKiveteleButton) {
			partner = null;
			partnerTablazatSorai.removeAllElements();
			partnerTable = new JTable(null, PARTNER_TABLAZAT_MEZO_NEVEK) {

				private static final long serialVersionUID = 1L;
				
				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return false;
				}
			};
			partnerTablePanel.setViewportView(partnerTable);
			partnerKiveteleButton.setEnabled(false);
			
		} else if(e.getSource() == letrehozasButton) {
			if(panelTipus == GuiEszkozok.panelTipus.BESZERZES) {
				if(termek == null || uzemelteto == null || partner == null) {
					JOptionPane.showMessageDialog(this, "A beszerzés létrehozásához meg kell adni egy terméket, egy üzemeltetõ céget és egy partnert", "Beszerzés létrehozása", JOptionPane.WARNING_MESSAGE);
					return;
				}
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				Beszerzes beszerzes = new Beszerzes.Builder(uzemelteto, partner, termek).felhasznalo(GuiEszkozok.getFelhasznalo()).build();
				Object parameterek[] = { beszerzes };
				Valasz valasz = null;
				boolean kivetelNemVolt = true;
				if(GuiEszkozok.kapcsolatokNyitasa()) {
					try {
						GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.BESZERZES_LETREHOZASA, parameterek, GuiEszkozok.getFelhasznalo()));
						GuiEszkozok.getOutputStream().flush();
						valasz = (Valasz)GuiEszkozok.getInputStream().readObject();
					} catch (IOException ex) {
						kivetelNemVolt = false;
						JOptionPane.showMessageDialog(this, "Nem elérhetõ a szerver a data/client.properties fájl beállításai alapján\n" +
								"Lehetséges, hogy nincs is elindítva az alkalmazásszerver", "IOException", JOptionPane.ERROR_MESSAGE);
					} catch (ClassNotFoundException ex) {
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
							if(valasz.getEset() == Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT)
								JOptionPane.showMessageDialog(this, "A " + GuiEszkozok.getFelhasznalo().getNev() + " nevû felhasználó jogosultságát törölték", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
							else
								JOptionPane.showMessageDialog(this, valasz.getEset(), "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(this, "Beszerzés létrehozva", "Beszerzés létrehozása", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					if(valasz == null) {
						JOptionPane.showMessageDialog(this, "Megszakadt a kapcsolat az alkalmazásszerverrel", "Nincs visszaigazolás a szervertõl", JOptionPane.ERROR_MESSAGE);
					}
				}
				setCursor(Cursor.getDefaultCursor());
			} else if (panelTipus == GuiEszkozok.panelTipus.SZAMLA) {
				if(termekek.isEmpty() || uzemelteto == null || partner == null) {
					JOptionPane.showMessageDialog(this, "A számla létrehozásához meg kell adni legalább egy terméket, egy üzemeltetõ céget és egy partnert", "Számla létrehozása", JOptionPane.WARNING_MESSAGE);
					return;
				}
				List<Termek> termeklista = new ArrayList<Termek>(termekek.values());
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				Szamla szamla = new Szamla.Builder(partner, uzemelteto, termeklista).keszitette(GuiEszkozok.getFelhasznalo()).build();
				Object parameterek[] = { szamla };
				Valasz valasz = null;
				boolean kivetelNemVolt = true;
				if(GuiEszkozok.kapcsolatokNyitasa()) {
					try {
						GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.SZAMLA_LETREHOZASA, parameterek, GuiEszkozok.getFelhasznalo()));
						GuiEszkozok.getOutputStream().flush();
						valasz = (Valasz)GuiEszkozok.getInputStream().readObject();
					} catch (IOException ex) {
						kivetelNemVolt = false;
						JOptionPane.showMessageDialog(this, "Nem elérhetõ a szerver a data/client.properties fájl beállításai alapján\n" +
								"Lehetséges, hogy nincs is elindítva az alkalmazásszerver", "IOException", JOptionPane.ERROR_MESSAGE);
					} catch (ClassNotFoundException ex) {
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
							if(valasz.getEset() == Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT)
								JOptionPane.showMessageDialog(this, "A " + GuiEszkozok.getFelhasznalo().getNev() + " nevû felhasználó jogosultságát törölték", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
							else if(valasz.getEset() == Eset.NINCS_ENNYI_RAKTARON)
								JOptionPane.showMessageDialog(this, "Nincs ekkora mennyiség raktáron", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
							else
								JOptionPane.showMessageDialog(this, valasz.getEset(), "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(this, "Számla létrehozva", "Számla létrehozása", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					if(valasz == null) {
						JOptionPane.showMessageDialog(this, "Megszakadt a kapcsolat az alkalmazásszerverrel", "Nincs visszaigazolás a szervertõl", JOptionPane.ERROR_MESSAGE);
					}
				}
				setCursor(Cursor.getDefaultCursor());
			}
			
		} else if(e.getSource() == mezokTorleseButton) {
			GuiEszkozok.setTermek(null);
			GuiEszkozok.setUzemeltetoCeg(null);
			GuiEszkozok.setPartner(null);
			termek = null;
			termekek.clear();
			uzemelteto = null;
			partner = null;
			termekTablazatSorai.clear();
			uzemeltetoTablazatSorai.clear();
			partnerTablazatSorai.clear();
			termekKiveteleButton.setEnabled(false);
			termekModositasButton.setEnabled(false);
			afaTextField.setEnabled(false);
			beszerzesiArTextField.setEnabled(false);
			mennyisegTextField.setEnabled(false);
			afaTextField.setText("");
			beszerzesiArTextField.setText("");
			mennyisegTextField.setText("");
			uzemeltetoKiveteleButton.setEnabled(false);
			partnerKiveteleButton.setEnabled(false);
			JTable termekTable = new JTable(null, TERMEK_TABLAZAT_MEZO_NEVEK);
			termekTablePanel.setViewportView(termekTable);
			JTable uzemeltetoTable = new JTable(null, UZEMELTETO_TABLAZAT_MEZO_NEVEK);
			uzemeltetoTablePanel.setViewportView(uzemeltetoTable);
			JTable partnerTable = new JTable(null, PARTNER_TABLAZAT_MEZO_NEVEK);
			partnerTablePanel.setViewportView(partnerTable);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if(panelTipus == GuiEszkozok.panelTipus.SZAMLA) {
			if(event.getValueIsAdjusting())
				return;
			if(termekTable.getSelectedColumnCount() == 1) {
				termekKiveteleButton.setEnabled(true);
				termekModositasButton.setEnabled(true);
				afaTextField.setEnabled(true);
				beszerzesiArTextField.setEnabled(true);
				mennyisegTextField.setEnabled(true);
				afaTextField.setText(termekTablazatSorai.get(termekTable.getSelectedRow()).get(3));
				beszerzesiArTextField.setText(termekTablazatSorai.get(termekTable.getSelectedRow()).get(4));
				mennyisegTextField.setText(termekTablazatSorai.get(termekTable.getSelectedRow()).get(6));
			} else {
				termekKiveteleButton.setEnabled(false);
				termekModositasButton.setEnabled(false);
				afaTextField.setEnabled(false);
				beszerzesiArTextField.setEnabled(false);
				mennyisegTextField.setEnabled(false);
				afaTextField.setText("");
				beszerzesiArTextField.setText("");
				mennyisegTextField.setText("");
			}
		}
	}
}
