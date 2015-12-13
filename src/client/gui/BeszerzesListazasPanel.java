package client.gui;

import java.awt.BorderLayout;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import common.entities.Beszerzes;
import common.entities.Keres;
import common.entities.Valasz;
import common.entities.Keres.Operations;

public class BeszerzesListazasPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
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
	
	private JTextField termekkodTextField = new JTextField(10);
	private JTextField uzemeltetokodTextField = new JTextField(10);
	private JTextField partnerkodTextField = new JTextField(10);
	
	private JButton listazasButton = new JButton("Listázás", new ImageIcon("images/Search.png"));
	private JButton mezokTorleseButton = new JButton("Mezõk törlése", new ImageIcon("images/Delete.png"));
	
	private Vector<Vector<String>> tablazatSorai = new Vector<Vector<String>>();
	private static final Vector<String> TABLAZAT_MEZO_NEVEK = new Vector<String>();
	private JTable beszerzesekTable = new JTable(null, TABLAZAT_MEZO_NEVEK);
	
	static {
		TABLAZAT_MEZO_NEVEK.add("Dátum");
		TABLAZAT_MEZO_NEVEK.add("Termék");
		TABLAZAT_MEZO_NEVEK.add("Beszerzett mennyiség");
		TABLAZAT_MEZO_NEVEK.add("Egység nettó beszerzési ára");
		TABLAZAT_MEZO_NEVEK.add("Áfa százalékláb");
		TABLAZAT_MEZO_NEVEK.add("Üzemeltetõ cég");
		TABLAZAT_MEZO_NEVEK.add("Partner");
		TABLAZAT_MEZO_NEVEK.add("Felhasználó");
		
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
	}
	
	{
		adatokPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		adatokPanel.add(new JLabel("Idõpont tartomány:"));
		adatokPanel.add(EV_TOL);
		adatokPanel.add(HO_TOL);
		adatokPanel.add(NAP_TOL);
		adatokPanel.add(new JLabel(" - "));
		adatokPanel.add(EV_IG);
		adatokPanel.add(HO_IG);
		adatokPanel.add(NAP_IG);
		adatokPanel.add(new JLabel("Termékkód:"));
		adatokPanel.add(termekkodTextField);
		adatokPanel.add(new JLabel("Üzemeltetõkód:"));
		adatokPanel.add(uzemeltetokodTextField);
		adatokPanel.add(new JLabel("Partnerkód:"));
		adatokPanel.add(partnerkodTextField);
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(listazasButton);
		buttonPanel.add(mezokTorleseButton);
		
		felsoPanel.setLayout(new BorderLayout(10, 10));
		felsoPanel.setBorder(BorderFactory.createEmptyBorder(20, 1, 20, 1));
		felsoPanel.add(adatokPanel, BorderLayout.CENTER);
		felsoPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		alsoGorgethetoPanel.setViewportView(beszerzesekTable);
	}

	public BeszerzesListazasPanel() {
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Beszerzes listázás"));
		setLayout(new GridLayout(2, 1));
		add(felsoPanel);
		add(alsoGorgethetoPanel);
		
		listazasButton.addActionListener(this);
		mezokTorleseButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == listazasButton) {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			int termekkod = 0;
			try {
				termekkod = Integer.parseInt(termekkodTextField.getText());
			} catch (NumberFormatException e) {
				termekkodTextField.setText("");
			}
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
			Date datumtol = new Date(new GregorianCalendar(EV_TOL.getItemAt(EV_TOL.getSelectedIndex()), HO_TOL.getItemAt(HO_TOL.getSelectedIndex())-1, NAP_TOL.getItemAt(NAP_TOL.getSelectedIndex())).getTimeInMillis());
			Date datumig = new Date(new GregorianCalendar(EV_IG.getItemAt(EV_IG.getSelectedIndex()), HO_IG.getItemAt(HO_IG.getSelectedIndex())-1, NAP_IG.getItemAt(NAP_IG.getSelectedIndex())).getTimeInMillis());
			Object parameterek[] = { datumtol, datumig, uzemeltetokod == 0 ? "" : Integer.toString(uzemeltetokod), partnerkod == 0 ? "" : Integer.toString(partnerkod), termekkod == 0 ? "" : Integer.toString(termekkod) };
			Valasz valasz = null;
			boolean kivetelNemVolt = true;
			if(GuiEszkozok.kapcsolatokNyitasa()) {
				try {
					GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.BESZERZESEK_KIVALASZTASA, parameterek, GuiEszkozok.getFelhasznalo()));
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
							List<Beszerzes> beszerzesek = (List<Beszerzes>)valasz.getAdat();
							tablazatSorai.removeAllElements();
							DateFormat df = DateFormat.getDateInstance();
							for(Beszerzes b : beszerzesek) {
								Vector<String> sor = new Vector<String>();
								sor.add(df.format(b.getDatum()));
								sor.add(b.getTermek().toString());
								sor.add(Integer.toString(b.getTermek().getMennyiseg()));
								sor.add(Double.toString(b.getTermek().getEgysegNettoBeszerzesiAr()));
								sor.add(Double.toString(b.getTermek().getAfaSzazaleklab()));
								sor.add(b.getVevo().toString());
								sor.add(b.getElado().toString());
								sor.add(b.getFelhasznalo().toString());
								tablazatSorai.add(sor);
							}
							beszerzesekTable = new JTable(tablazatSorai, TABLAZAT_MEZO_NEVEK) {

								private static final long serialVersionUID = 1L;
								
								public boolean isCellEditable(int rowIndex, int columnIndex) {
									return false;
								}
							};
							alsoGorgethetoPanel.setViewportView(beszerzesekTable);
						} catch (ClassCastException e) {
							JOptionPane.showMessageDialog(this, "Programhiba. Értesítse a rendszergazdát", "ClassCastException", JOptionPane.ERROR_MESSAGE);
						}
					}
				} else if(valasz == null) {
					JOptionPane.showMessageDialog(this, "Megszakadt a kapcsolat az alkalmazásszerverrel", "Nincs válasz a szervertõl", JOptionPane.ERROR_MESSAGE);
				}
			}
			setCursor(Cursor.getDefaultCursor());
		} else if(event.getSource() == mezokTorleseButton) {
			termekkodTextField.setText("");
			uzemeltetokodTextField.setText("");
			partnerkodTextField.setText("");
			EV_TOL.setSelectedIndex(0);
			HO_TOL.setSelectedIndex(0);
			NAP_TOL.setSelectedIndex(0);
			EV_IG.setSelectedIndex(EV_IG.getItemCount()-1);
			HO_IG.setSelectedIndex(HO_IG.getItemCount()-1);
			NAP_IG.setSelectedIndex(NAP_IG.getItemCount()-1);
		}
	}
}

