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

import common.entities.Ceg;
import common.entities.Cim;
import common.entities.Keres;
import common.entities.Telepules;
import common.entities.Valasz;
import common.entities.Keres.Operations;

public class UzemeltetoCegListazasPanel extends JPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel felsoPanel = new JPanel();
	private JScrollPane alsoGorgethetoPanel = new JScrollPane();
	
	private JPanel adatokPanel = new JPanel();
	private JPanel dzsokerPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	
	private JTextField uzemeltetokodTextField = new JTextField(10);
	private JTextField nevTextField = new JTextField(10);
	private JTextField iranyitoszamTextField = new JTextField(10);
	private JTextField telepulesTextField = new JTextField(10);
	private JTextField kozteruletTextField = new JTextField(10);
	private JTextField hazszamTextField = new JTextField(10);
	private JTextField emeletTextField = new JTextField(10);
	private JTextField ajtoTextField = new JTextField(10);
	private JTextField adoszamTextField = new JTextField(10);
	private JTextField telefonTextField = new JTextField(10);
	private JTextField faxTextField = new JTextField(10);
	private JTextField emailTextField = new JTextField(10);
	private JTextField webcimTextField = new JTextField(10);
	
	private JButton listazasButton = new JButton("List�z�s", new ImageIcon("images/Search.png"));
	private JButton modositasButton = new JButton("M�dos�t�s", new ImageIcon("images/Modify.png"));
	private JButton torlesButton = new JButton("T�rl�s", new ImageIcon("images/Delete.png"));
	private JButton mezokTorleseButton = new JButton("Mez�k t�rl�se", new ImageIcon("images/Delete.png"));
	private JButton kivalasztasButton = new JButton("Kiv�laszt�s", new ImageIcon("images/Profile.png"));
	
	private Map<Integer, Ceg> uzemeltetoCegek = new HashMap<Integer, Ceg>();
	
	private Vector<Vector<String>> tablazatSorai = new Vector<Vector<String>>();
	private static final Vector<String> TABLAZAT_MEZO_NEVEK = new Vector<String>();
	private JTable uzemeltetoCegekTable = new JTable(null, TABLAZAT_MEZO_NEVEK);
	
	static {
		TABLAZAT_MEZO_NEVEK.add("�zemeltet�k�d");
		TABLAZAT_MEZO_NEVEK.add("N�v");
		TABLAZAT_MEZO_NEVEK.add("C�m");
		TABLAZAT_MEZO_NEVEK.add("Ad�sz�m");
		TABLAZAT_MEZO_NEVEK.add("Telefon");
		TABLAZAT_MEZO_NEVEK.add("Fax");
		TABLAZAT_MEZO_NEVEK.add("Email");
		TABLAZAT_MEZO_NEVEK.add("Web");
	}
	
	{
		modositasButton.setEnabled(false);
		torlesButton.setEnabled(false);
		kivalasztasButton.setEnabled(false);
		
		adatokPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		adatokPanel.add(new JLabel("�zemeltet�k�d:"));
		adatokPanel.add(uzemeltetokodTextField);
		adatokPanel.add(new JLabel("N�v(%):"));
		adatokPanel.add(nevTextField);
		adatokPanel.add(new JLabel("Ir�ny�t�sz�m:"));
		adatokPanel.add(iranyitoszamTextField);
		adatokPanel.add(new JLabel("Telep�l�s(%):"));
		adatokPanel.add(telepulesTextField);
		adatokPanel.add(new JLabel("K�zter�let(%):"));
		adatokPanel.add(kozteruletTextField);
		adatokPanel.add(new JLabel("H�zsz�m:"));
		adatokPanel.add(hazszamTextField);
		adatokPanel.add(new JLabel("Emelet(%):"));
		adatokPanel.add(emeletTextField);
		adatokPanel.add(new JLabel("Ajt�(%):"));
		adatokPanel.add(ajtoTextField);
		adatokPanel.add(new JLabel("Ad�sz�m:"));
		adatokPanel.add(adoszamTextField);
		adatokPanel.add(new JLabel("Telefon(%):"));
		adatokPanel.add(telefonTextField);
		adatokPanel.add(new JLabel("Fax(%):"));
		adatokPanel.add(faxTextField);
		adatokPanel.add(new JLabel("Email(%):"));
		adatokPanel.add(emailTextField);
		adatokPanel.add(new JLabel("Web(%):"));
		adatokPanel.add(webcimTextField);
		
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
		
		alsoGorgethetoPanel.setViewportView(uzemeltetoCegekTable);
	}

	public UzemeltetoCegListazasPanel(GuiEszkozok.panelTipus panelTipus) {
		if(panelTipus == GuiEszkozok.panelTipus.LISTAZAS) {
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "�zemeltet� c�g list�z�s/m�dos�t�s/t�rl�s"));
			modositasButton.setVisible(true);
			torlesButton.setVisible(true);
			kivalasztasButton.setVisible(false);
		} else if(panelTipus == GuiEszkozok.panelTipus.KIVALASZTAS) {
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "�zemeltet� c�g kiv�laszt�s"));
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
		mezokTorleseButton.addActionListener(this);
		kivalasztasButton.addActionListener(this);
	}
	
	public JButton getKivalasztasButton() {
		return kivalasztasButton;
	}
	
	@SuppressWarnings("unchecked")
	public void listazas() {
		modositasButton.setEnabled(false);
		torlesButton.setEnabled(false);
		int uzemeltetokod = 0;
		try {
			uzemeltetokod = Integer.parseInt(uzemeltetokodTextField.getText().trim());
		} catch(NumberFormatException e) {
			uzemeltetokodTextField.setText("");
		}
		String nev = nevTextField.getText().trim();
		int iranyitoszam = 0;
		try {
			iranyitoszam = Integer.parseInt(iranyitoszamTextField.getText().trim());
		} catch(NumberFormatException e) {
			iranyitoszamTextField.setText("");
		}
		String telepules = telepulesTextField.getText().trim();
		String kozterulet = kozteruletTextField.getText().trim();
		int hazSzam = 0;
		try {
			hazSzam = Integer.parseInt(hazszamTextField.getText().trim());
		} catch(NumberFormatException e) {
			hazszamTextField.setText("");
		}
		String emelet = emeletTextField.getText().trim();
		String ajto = ajtoTextField.getText().trim();
		Cim cim = new Cim.Builder(new Telepules(iranyitoszam, telepules), kozterulet, hazSzam).emelet(emelet).ajto(ajto).build();
		long adoszam = 0;
		try {
			adoszam = Long.parseLong(adoszamTextField.getText().trim());
		} catch(NumberFormatException e) {
			adoszamTextField.setText("");
		}
		String telefon = telefonTextField.getText().trim();
		String fax = faxTextField.getText().trim();
		String email = emailTextField.getText().trim();
		String webcim = webcimTextField.getText().trim();
		Ceg uzemeltetoCeg = new Ceg.Builder(nev, cim).adoszam(adoszam).telefon(telefon).fax(fax).email(email).webcim(webcim).id(uzemeltetokod).build();
		Object parameterek[] = { uzemeltetoCeg };
		Valasz valasz = null;
		boolean kivetelNemVolt = true;
		if(GuiEszkozok.kapcsolatokNyitasa()) {
			try {
				GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.UZEMELTETOCEGEK_KIVALASZTASA, parameterek, GuiEszkozok.getFelhasznalo()));
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
				} else if (valasz.getAdat() != null ){
					try {
						uzemeltetoCegek.clear();
						List<Ceg> cegek = (List<Ceg>)valasz.getAdat();
						tablazatSorai.removeAllElements();
						for(Ceg ceg : cegek) {
							Vector<String> sor = new Vector<String>();
							sor.add(Integer.toString(ceg.getId()));
							sor.add(ceg.getNev());
							sor.add(ceg.getCim().toString());
							sor.add(Long.toString(ceg.getAdoszam()));
							sor.add(ceg.getTelefon());
							sor.add(ceg.getFax());
							sor.add(ceg.getEmail());
							sor.add(ceg.getWebcim());
							tablazatSorai.add(sor);
							uzemeltetoCegek.put(ceg.getId(), ceg);
						}
						uzemeltetoCegekTable = new JTable(tablazatSorai, TABLAZAT_MEZO_NEVEK) {

							private static final long serialVersionUID = 1L;
							
							public boolean isCellEditable(int rowIndex, int columnIndex) {
								return false;
							}
						};
						alsoGorgethetoPanel.setViewportView(uzemeltetoCegekTable);
						uzemeltetoCegekTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						uzemeltetoCegekTable.getSelectionModel().addListSelectionListener(this);
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
			Ceg kivalasztottCeg = uzemeltetoCegek.get(Integer.parseInt(tablazatSorai.get(uzemeltetoCegekTable.getSelectedRow()).get(0)));
			new UzemeltetoCegModositasDialog(this, kivalasztottCeg);
			listazas();
		} else if (event.getSource() == torlesButton) {	
			int uzemeltetokod = Integer.parseInt(tablazatSorai.get(uzemeltetoCegekTable.getSelectedRow()).get(0));
			int confirm = JOptionPane.showConfirmDialog(this, "Biztos, hogy t�r�lni akarod a " + uzemeltetokod + " �zemeltet�k�d� �zemeltet� c�get?", "�zemeltet� c�g t�rl�se", JOptionPane.YES_NO_OPTION);
			if(confirm == JOptionPane.NO_OPTION)
				return;
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			Object parameterek[] = { uzemeltetokod };
			Valasz valasz = null;
			boolean kivetelNemVolt = true;
			if(GuiEszkozok.kapcsolatokNyitasa()) {
				try {
					GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.UZEMELTETOCEG_TORLESE, parameterek, GuiEszkozok.getFelhasznalo()));
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
							JOptionPane.showMessageDialog(this, "Nincs ilyen �zemeltet� c�g\n" +
									"Lehets�ges, hogy egy m�sik kezel� �ppen t�r�lte", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
						else
							JOptionPane.showMessageDialog(this, valasz.getEset(), "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, uzemeltetokod + " �zemeltet�k�d� �zemeltet� c�g t�r�lve", "�zemeltet� c�g t�rl�se", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else if(valasz == null) {
					JOptionPane.showMessageDialog(this, "Megszakadt a kapcsolat az alkalmaz�sszerverrel", "Nincs v�lasz a szervert�l", JOptionPane.ERROR_MESSAGE);
				}
			}
			listazas();
			setCursor(Cursor.getDefaultCursor());
		} else if(event.getSource() == kivalasztasButton) {
			Ceg kivalasztottCeg = uzemeltetoCegek.get(Integer.parseInt(tablazatSorai.get(uzemeltetoCegekTable.getSelectedRow()).get(0)));
			GuiEszkozok.setUzemeltetoCeg(kivalasztottCeg);
		} else if (event.getSource() == mezokTorleseButton) {
			uzemeltetokodTextField.setText("");
			nevTextField.setText("");
			iranyitoszamTextField.setText("");
			telepulesTextField.setText("");
			kozteruletTextField.setText("");
			hazszamTextField.setText("");
			emeletTextField.setText("");
			ajtoTextField.setText("");
			adoszamTextField.setText("");
			telefonTextField.setText("");
			faxTextField.setText("");
			emailTextField.setText("");
			webcimTextField.setText("");
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if(event.getValueIsAdjusting())
            return;
		if(uzemeltetoCegekTable.getSelectedColumnCount() == 1) {
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

