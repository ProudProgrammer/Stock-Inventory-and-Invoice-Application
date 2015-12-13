package client.gui;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
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

import common.entities.Felhasznalo;
import common.entities.Keres;
import common.entities.Valasz;
import common.entities.Keres.Operations;

public class FelhasznaloListazasPanel extends JPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel felsoPanel = new JPanel();
	private JScrollPane alsoGorgethetoPanel = new JScrollPane();
	
	private JPanel nevPanel = new JPanel();
	private JPanel dzsokerPanel = new JPanel();
	private JPanel listazasPanel = new JPanel();
	
	private JTextField nevTextField = new JTextField(15);
	private JButton listazasButton = new JButton("List�z�s", new ImageIcon("images/Search.png"));
	private JButton modositasButton = new JButton("M�dos�t�s", new ImageIcon("images/Modify.png"));
	private JButton torlesButton = new JButton("T�rl�s", new ImageIcon("images/Delete.png"));
	
	private Vector<Vector<String>> tablazatSorai = new Vector<Vector<String>>();
	private static final Vector<String> TABLAZAT_MEZO_NEVEK = new Vector<String>();
	private JTable felhasznalokTable = new JTable(null, TABLAZAT_MEZO_NEVEK);
	
	static {
		TABLAZAT_MEZO_NEVEK.add("N�v");
		TABLAZAT_MEZO_NEVEK.add("Jelsz�");
		TABLAZAT_MEZO_NEVEK.add("Rendszergazdajog");
		TABLAZAT_MEZO_NEVEK.add("Teljes n�v");
		TABLAZAT_MEZO_NEVEK.add("TAJ sz�m");
	}
	
	{
		modositasButton.setEnabled(false);
		torlesButton.setEnabled(false);
		
		nevPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		nevPanel.add(new JLabel("N�v(%):"));
		nevPanel.add(nevTextField);
		
		dzsokerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		dzsokerPanel.add(new JLabel("Dzs�ker karakter: % jel. �llhat a sz�veg elej�n, v�g�n vagy mindk�t oldal�n. Haszn�lhat�s�g�t % jel mutatja"));
		
		listazasPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		listazasPanel.add(listazasButton);
		listazasPanel.add(modositasButton);
		listazasPanel.add(torlesButton);
		
		felsoPanel.setLayout(new GridLayout(3, 1));
		felsoPanel.add(nevPanel);
		felsoPanel.add(dzsokerPanel);
		felsoPanel.add(listazasPanel);
		
		alsoGorgethetoPanel.setViewportView(felhasznalokTable);
	}

	public FelhasznaloListazasPanel() {
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED) , "Felhaszn�l� list�z�s/m�dos�t�s/t�rl�s"));
		setLayout(new GridLayout(2, 1));
		add(felsoPanel);
		add(alsoGorgethetoPanel);
		
		listazasButton.addActionListener(this);
		modositasButton.addActionListener(this);
		torlesButton.addActionListener(this);
	}
	
	@SuppressWarnings("unchecked")
	public void listazas() {
		modositasButton.setEnabled(false);
		torlesButton.setEnabled(false);
		Object parameterek[] = { nevTextField.getText().trim() };
		Valasz valasz = null;
		boolean kivetelNemVolt = true;
		if(GuiEszkozok.kapcsolatokNyitasa()) {
			try {
				GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.FELHASZNALOK_KIVALASZTASA, parameterek, GuiEszkozok.getFelhasznalo()));
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
				} else {
					try {
						List<Felhasznalo> felhasznalok = (List<Felhasznalo>)valasz.getAdat();
						tablazatSorai.removeAllElements();
						for(Felhasznalo felhasznalo : felhasznalok) {
							Vector<String> sor = new Vector<String>();
							sor.add(felhasznalo.getNev());
							sor.add(felhasznalo.getJelszo());
							sor.add(felhasznalo.getRendszergazdaJog().irottFormaban());
							sor.add(felhasznalo.getTeljesNev());
							sor.add(felhasznalo.getTajSzam());
							tablazatSorai.add(sor);
						}
						felhasznalokTable = new JTable(tablazatSorai, TABLAZAT_MEZO_NEVEK) {

							private static final long serialVersionUID = 1L;
							
							public boolean isCellEditable(int rowIndex, int columnIndex) {
								return false;
							}
						};
						alsoGorgethetoPanel.setViewportView(felhasznalokTable);
						felhasznalokTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						felhasznalokTable.getSelectionModel().addListSelectionListener(this);
					} catch (ClassCastException e) {
						JOptionPane.showMessageDialog(this, "Programhiba. �rtes�tse a rendszergazd�t", "ClassCastException", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			else if(valasz == null) {
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
			Felhasznalo felhasznalo = new Felhasznalo.Builder(tablazatSorai.get(felhasznalokTable.getSelectedRow()).get(0), tablazatSorai.get(felhasznalokTable.getSelectedRow()).get(1))
			.rendszergazdaJog(tablazatSorai.get(felhasznalokTable.getSelectedRow()).get(2)).teljesNev(tablazatSorai.get(felhasznalokTable.getSelectedRow()).get(3)).tajSzam(tablazatSorai.get(felhasznalokTable.getSelectedRow()).get(4)).build();
			new FelhasznaloModositasDialog(this, felhasznalo);
			listazas();
		} else if (event.getSource() == torlesButton) {
			String nev = tablazatSorai.get(felhasznalokTable.getSelectedRow()).get(0);
			if(nev.equals("admin")) {
				JOptionPane.showMessageDialog(this, "Az admin rendszergazda nem t�r�lhet�", "Felhaszn�l� t�rl�se", JOptionPane.WARNING_MESSAGE);
				return;
			}
			int confirm = JOptionPane.showConfirmDialog(this, "Biztos, hogy t�r�lni akarod a " + nev + " nev� felhaszn�l�t?", "Felhaszn�l� t�rl�se", JOptionPane.YES_NO_OPTION);
			if(confirm == JOptionPane.NO_OPTION)
				return;
			if(felhasznalokTable.getSelectedColumnCount() != 1) {
				JOptionPane.showMessageDialog(this, "Egyn�l t�bb vagy kevesebb elem van kijel�lve", "T�rl�s hiba", JOptionPane.ERROR_MESSAGE);
			} else {
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				Object parameterek[] = { nev };
				Valasz valasz = null;
				boolean kivetelNemVolt = true;
				if(GuiEszkozok.kapcsolatokNyitasa()) {
					try {
						GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.FELHASZNALO_TORLESE, parameterek, GuiEszkozok.getFelhasznalo()));
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
								JOptionPane.showMessageDialog(this, "Nincs ilyen felhaszn�l�\n" +
										"Lehets�ges, hogy egy m�sik kezel� �ppen t�r�lte", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
							else
								JOptionPane.showMessageDialog(this, valasz.getEset(), "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(this, nev + " felhaszn�l� t�r�lve", "Felhaszn�l� t�rl�se", JOptionPane.INFORMATION_MESSAGE);
						}
					} else if(valasz == null) {
						JOptionPane.showMessageDialog(this, "Megszakadt a kapcsolat az alkalmaz�sszerverrel", "Nincs v�lasz a szervert�l", JOptionPane.ERROR_MESSAGE);
					}
				}
				listazas();
				setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if(event.getValueIsAdjusting())
            return;
		if(felhasznalokTable.getSelectedColumnCount() == 1) {
			if(tablazatSorai.get(felhasznalokTable.getSelectedRow()).get(0).equals("admin")) {
				modositasButton.setEnabled(true);
				torlesButton.setEnabled(false);
			} else {
				modositasButton.setEnabled(true);
				torlesButton.setEnabled(true);
			}
		} else {
			modositasButton.setEnabled(false);
			torlesButton.setEnabled(false);
		}
	}

}
