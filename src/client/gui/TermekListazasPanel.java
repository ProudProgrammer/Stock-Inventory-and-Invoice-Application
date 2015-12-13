package client.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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

import common.entities.Keres;
import common.entities.Termek;
import common.entities.Valasz;
import common.entities.Keres.Operations;

public class TermekListazasPanel extends JPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel felsoPanel = new JPanel();
	private JScrollPane alsoGorgethetoPanel = new JScrollPane();
	
	private JPanel adatokPanel = new JPanel();
	private JPanel dzsokerPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	
	private JTextField termekkodTextField = new JTextField(12);
	private JTextField megnevezesTextField = new JTextField(12);
	private JTextField vtszTextField = new JTextField(12);
	private JTextField afaTextField = new JTextField(12);
	private JTextField beszerzesiArTextField = new JTextField(12);
	private JTextField eladasiArTextField = new JTextField(12);
	private JTextField mennyisegTextField = new JTextField(12);
	
	private JButton listazasButton = new JButton("List�z�s", new ImageIcon("images/Search.png"));
	private JButton modositasButton = new JButton("M�dos�t�s", new ImageIcon("images/Modify.png"));
	private JButton torlesButton = new JButton("T�rl�s", new ImageIcon("images/Delete.png"));
	private JButton mezokTorleseButton = new JButton("Mez�k t�rl�se", new ImageIcon("images/Delete.png"));
	private JButton kivalasztasButton = new JButton("Kiv�laszt�s", new ImageIcon("images/Profile.png"));

	
	private Map<Integer, Termek> termekek = new HashMap<Integer, Termek>();
	
	private Vector<Vector<String>> tablazatSorai = new Vector<Vector<String>>();
	private static final Vector<String> TABLAZAT_MEZO_NEVEK = new Vector<String>();
	private JTable termekekTable = new JTable(null, TABLAZAT_MEZO_NEVEK);
	
	static {
		TABLAZAT_MEZO_NEVEK.add("Term�kk�d");
		TABLAZAT_MEZO_NEVEK.add("Megnevez�s");
		TABLAZAT_MEZO_NEVEK.add("VTSZ");
		TABLAZAT_MEZO_NEVEK.add("Aktu�lis �fa sz�zal�kl�b");
		TABLAZAT_MEZO_NEVEK.add("Aktu�lis nett� beszerz�si �r");
		TABLAZAT_MEZO_NEVEK.add("Aktu�lis nett� elad�si �r");
		TABLAZAT_MEZO_NEVEK.add("Mennyis�g egys�ge");
		TABLAZAT_MEZO_NEVEK.add("Rakt�rozott mennyis�g");
	}
	
	{
		modositasButton.setEnabled(false);
		torlesButton.setEnabled(false);
		kivalasztasButton.setEnabled(false);
		
		adatokPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		adatokPanel.add(new JLabel("Term�kk�d:"));
		adatokPanel.add(termekkodTextField);
		adatokPanel.add(new JLabel("Megnevez�s(%):"));
		adatokPanel.add(megnevezesTextField);
		adatokPanel.add(new JLabel("VTSZ:"));
		adatokPanel.add(vtszTextField);
		adatokPanel.add(new JLabel("Aktu�lis �fa sz�zal�kl�b:"));
		adatokPanel.add(afaTextField);
		adatokPanel.add(new JLabel("Aktu�lis nett� beszerz�si �r:"));
		adatokPanel.add(beszerzesiArTextField);
		adatokPanel.add(new JLabel("Aktu�lis nett� elad�si �r:"));
		adatokPanel.add(eladasiArTextField);
		adatokPanel.add(new JLabel("Rakt�rozott mennyis�g:"));
		adatokPanel.add(mennyisegTextField);
		
		dzsokerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		dzsokerPanel.add(new JLabel("Dzs�ker karakter: % jel. �llhat a sz�veg elej�n, v�g�n vagy mindk�t oldal�n. Haszn�lhat�s�g�t % jel mutatja"));
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(listazasButton);
		buttonPanel.add(modositasButton);
		buttonPanel.add(torlesButton);
		buttonPanel.add(kivalasztasButton);
		buttonPanel.add(mezokTorleseButton);
		
		
		felsoPanel.setLayout(new BorderLayout(10, 10));
		felsoPanel.setBorder(BorderFactory.createEmptyBorder(20, 1, 20, 1));
		felsoPanel.add(dzsokerPanel, BorderLayout.NORTH);
		felsoPanel.add(adatokPanel, BorderLayout.CENTER);
		felsoPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		alsoGorgethetoPanel.setViewportView(termekekTable);
	}

	public TermekListazasPanel(GuiEszkozok.panelTipus panelTipus) {
		if(panelTipus == GuiEszkozok.panelTipus.LISTAZAS) {
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Term�k list�z�s/m�dos�t�s/t�rl�s"));
			modositasButton.setVisible(true);
			torlesButton.setVisible(true);
			kivalasztasButton.setVisible(false);
		} else if(panelTipus == GuiEszkozok.panelTipus.KIVALASZTAS) {
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Term�k kiv�laszt�s"));
			modositasButton.setVisible(false);
			torlesButton.setVisible(false);
			kivalasztasButton.setVisible(true);
		}		
		setLayout(new GridLayout(2, 1));
		add(felsoPanel);
		add(alsoGorgethetoPanel);
		
		listazasButton.addActionListener(this);
		modositasButton.addActionListener(this);
		torlesButton.addActionListener(this);
		kivalasztasButton.addActionListener(this);
		mezokTorleseButton.addActionListener(this);
	}
	
	public JButton getKivalasztasButton() {
		return kivalasztasButton;
	}
	
	@SuppressWarnings("unchecked")
	public void listazas() {
		modositasButton.setEnabled(false);
		torlesButton.setEnabled(false);
		int termekkod = 0;
		try {
			termekkod = Integer.parseInt(termekkodTextField.getText().trim());
		} catch(NumberFormatException e) {
			termekkodTextField.setText("");
		}
		String megnevezes = megnevezesTextField.getText().trim();
		int vtsz = 0;
		try {
			vtsz = Integer.parseInt(vtszTextField.getText().trim());
		} catch(NumberFormatException e) {
			vtszTextField.setText("");
		}
		double afaSzazalekLab = 0;
		try {
			afaSzazalekLab = Double.parseDouble(afaTextField.getText().trim());
		} catch(NumberFormatException e) {
			afaTextField.setText("");
		}
		double egysegNettoBeszerzesiAr = 0;
		try {
			egysegNettoBeszerzesiAr = Double.parseDouble(beszerzesiArTextField.getText().trim());
		} catch(NumberFormatException e) {
			beszerzesiArTextField.setText("");
		}
		double egysegNettoEladasiAr = 0;
		try {
			egysegNettoEladasiAr = Double.parseDouble(eladasiArTextField.getText().trim());
		} catch(NumberFormatException e) {
			eladasiArTextField.setText("");
		}
		int mennyiseg = 0;
		try {
			mennyiseg = Integer.parseInt(mennyisegTextField.getText().trim());
		} catch(NumberFormatException e) {
			mennyisegTextField.setText("");
		}
		Termek termek = new Termek.Builder(megnevezes, afaSzazalekLab).egysegNettoBeszerzesiAr(egysegNettoBeszerzesiAr).egysegNettoEladasiAr(egysegNettoEladasiAr).mennyiseg(mennyiseg).termekkod(termekkod).vtsz(vtsz).build();
		Object parameterek[] = { termek };
		Valasz valasz = null;
		boolean kivetelNemVolt = true;
		if(GuiEszkozok.kapcsolatokNyitasa()) {
			try {
				GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.TERMEKEK_KIVALASZTASA, parameterek, GuiEszkozok.getFelhasznalo()));
				GuiEszkozok.getOutputStream().flush();
				valasz = (Valasz)GuiEszkozok.getInputStream().readObject();
			} catch (IOException e) {
				kivetelNemVolt = false;
				JOptionPane.showMessageDialog(this, "Nem el�rhet� a szerver a data/client.properties f�jl be�ll�t�sai alapj�n\n" +
						"Lehets�ges, hogy nincs is elind�tva az alkalmaz�sszerver", "IOException", JOptionPane.ERROR_MESSAGE);
			} catch (ClassNotFoundException e) {
				kivetelNemVolt = false;
				JOptionPane.showMessageDialog(this, "Az alkalmaz�s egyes r�szei nem el�rhet�k. Telep�tse �jra", "ClassNotFoundException", JOptionPane.ERROR_MESSAGE);
			} finally {
				GuiEszkozok.kapcsolatokZarasa();
			}
			if(valasz != null && kivetelNemVolt) {
				if(valasz.getVoltESQLException()) {
					JOptionPane.showMessageDialog(this, "Az alkalmaz�sszerver �s az adatb�zisszerver k�z�tt hiba l�pett fel\n" +
							"Lehets�ges, hogy a szerveren a data/server.properties f�jl be�ll�t�sai helytelenek\n" +
							"Esetleg nincs is elind�tva az adatb�zisszerver", "SQLException " + valasz.getSQLException().getErrorCode() + " Error Code", JOptionPane.ERROR_MESSAGE);
				} else if (valasz.getAdat() != null ){
					try {
						termekek.clear();
						List<Termek> termekekList = (List<Termek>)valasz.getAdat();
						tablazatSorai.removeAllElements();
						for(Termek t : termekekList) {
							Vector<String> sor = new Vector<String>();
							sor.add(Integer.toString(t.getTermekkod()));
							sor.add(t.getMegnevezes());
							sor.add(Integer.toString(t.getVtsz()));
							sor.add(Double.toString(t.getAfaSzazaleklab()));
							sor.add(Double.toString(t.getEgysegNettoBeszerzesiAr()));
							sor.add(Double.toString(t.getEgysegNettoEladasiAr()));
							sor.add(t.getMennyisegEgysege().irottFormaban());
							sor.add(Integer.toString(t.getMennyiseg()));
							tablazatSorai.add(sor);
							termekek.put(t.getTermekkod(), t);
						}
						termekekTable = new JTable(tablazatSorai, TABLAZAT_MEZO_NEVEK) {

							private static final long serialVersionUID = 1L;
							
							public boolean isCellEditable(int rowIndex, int columnIndex) {
								return false;
							}
						};
						alsoGorgethetoPanel.setViewportView(termekekTable);
						termekekTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						termekekTable.getSelectionModel().addListSelectionListener(this);
					} catch (ClassCastException e) {
						JOptionPane.showMessageDialog(this, "Programhiba. �rtes�tse a rendszergazd�t", "ClassCastException", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else if(valasz == null) {
				JOptionPane.showMessageDialog(this, "Megszakadt a kapcsolat az alkalmaz�sszerverrel", "Nincs v�lasz a szervert�l", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == listazasButton) {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			listazas();
			setCursor(Cursor.getDefaultCursor());
		} else if (event.getSource() == modositasButton) {
			Termek kivalasztottTermek = termekek.get(Integer.parseInt(tablazatSorai.get(termekekTable.getSelectedRow()).get(0)));
			new TermekModositasDialog(this, kivalasztottTermek);
			listazas();
		} else if (event.getSource() == torlesButton) {	
			int termekkod = Integer.parseInt(tablazatSorai.get(termekekTable.getSelectedRow()).get(0));
			int confirm = JOptionPane.showConfirmDialog(this, "Biztos, hogy t�r�lni akarod a " + termekkod + " term�kk�d� term�ket?", "Term�k t�rl�se", JOptionPane.YES_NO_OPTION);
			if(confirm == JOptionPane.NO_OPTION)
				return;
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			Object parameterek[] = { termekkod };
			Valasz valasz = null;
			boolean kivetelNemVolt = true;
			if(GuiEszkozok.kapcsolatokNyitasa()) {
				try {
					GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.TERMEK_TORLESE, parameterek, GuiEszkozok.getFelhasznalo()));
					GuiEszkozok.getOutputStream().flush();
					valasz = (Valasz)GuiEszkozok.getInputStream().readObject();
				} catch (IOException e) {
					kivetelNemVolt = false;
					JOptionPane.showMessageDialog(this, "Nem el�rhet� a szerver a data/client.properties f�jl be�ll�t�sai alapj�n\n" +
							"Lehets�ges, hogy nincs is elind�tva az alkalmaz�sszerver", "IOException", JOptionPane.ERROR_MESSAGE);
				} catch (ClassNotFoundException e) {
					kivetelNemVolt = false;
					JOptionPane.showMessageDialog(this, "Az alkalmaz�s egyes r�szei nem el�rhet�k. Telep�tse �jra", "ClassNotFoundException", JOptionPane.ERROR_MESSAGE);
				} finally {
					GuiEszkozok.kapcsolatokZarasa();
				}
				if(valasz != null && kivetelNemVolt) {
					if(valasz.getVoltESQLException()) {
						int hibakod = valasz.getSQLException().getErrorCode();
						JOptionPane.showMessageDialog(this, GuiEszkozok.getHibauzenetek(hibakod), "SQLException " + hibakod + " Error Code", JOptionPane.ERROR_MESSAGE);
					} else if(!valasz.getSikeresDataBaseOperation()) {
						if(valasz.getEset() == Eset.NINCS_ILYEN_OBJEKTUM)
							JOptionPane.showMessageDialog(this, "Nincs ilyen term�k\n" +
									"Lehets�ges, hogy egy m�sik kezel� �ppen t�r�lte", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
						else
							JOptionPane.showMessageDialog(this, valasz.getEset(), "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, termekkod + " term�kk�d� term�k t�r�lve", "Term�k t�rl�se", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else if(valasz == null) {
					JOptionPane.showMessageDialog(this, "Megszakadt a kapcsolat az alkalmaz�sszerverrel", "Nincs v�lasz a szervert�l", JOptionPane.ERROR_MESSAGE);
				}
			}
			listazas();
			setCursor(Cursor.getDefaultCursor());
		} else if(event.getSource() == kivalasztasButton) {
			Termek kivalasztottTermek = termekek.get(Integer.parseInt(tablazatSorai.get(termekekTable.getSelectedRow()).get(0)));
			GuiEszkozok.setTermek(kivalasztottTermek);
		} else if (event.getSource() == mezokTorleseButton) {
			termekkodTextField.setText("");
			megnevezesTextField.setText("");
			vtszTextField.setText("");
			afaTextField.setText("");
			beszerzesiArTextField.setText("");
			eladasiArTextField.setText("");
			mennyisegTextField.setText("");
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if(event.getValueIsAdjusting())
            return;
		if(termekekTable.getSelectedColumnCount() == 1) {
			modositasButton.setEnabled(true);
			torlesButton.setEnabled(true);
			kivalasztasButton.setEnabled(true);
		} else {
			modositasButton.setEnabled(false);
			torlesButton.setEnabled(false);
			kivalasztasButton.setEnabled(false);
		}
	}

}

