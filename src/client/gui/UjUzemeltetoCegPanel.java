package client.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import server.database.DataBaseOperationException.Eset;

import common.entities.Ceg;
import common.entities.Cim;
import common.entities.Keres;
import common.entities.Telepules;
import common.entities.Valasz;
import common.entities.Keres.Operations;

public class UjUzemeltetoCegPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel elsoSorPanel = new JPanel();
	private JPanel masodikSorPanel = new JPanel();
	private JPanel harmadikSorPanel = new JPanel();
	private JPanel negyedikSorPanel = new JPanel();
	private JPanel otodikSorPanel = new JPanel();
	private JPanel hatodikSorPanel = new JPanel();
	private JPanel hetedikSorPanel = new JPanel();
	private JPanel nyolcadikSorPanel = new JPanel();
	
	private JTextField nevTextField = new JTextField(12);
	private JTextField iranyitoszamTextField = new JTextField(12);
	private JTextField telepulesTextField = new JTextField(12);
	private JTextField kozteruletTextField = new JTextField(12);
	private JTextField hazszamTextField = new JTextField(12);
	private JTextField emeletTextField = new JTextField(12);
	private JTextField ajtoTextField = new JTextField(12);
	private JTextField adoszamTextField = new JTextField(12);
	private JTextField telefonTextField = new JTextField(12);
	private JTextField faxTextField = new JTextField(12);
	private JTextField emailTextField = new JTextField(12);
	private JTextField webcimTextField = new JTextField(12);
	
	private JComboBox<String> kozteruletJellegeComboBox = new JComboBox<String>();
	
	private JLabel nevHibasLabel = new JLabel("Hiányzó adat!");
	private JLabel adoszamHibasLabel = new JLabel("Hibás adat!");
	private JLabel iranyitoszamHibasLabel = new JLabel("Hiányzó vagy hibás adat!");
	private JLabel telepulesHibasLabel = new JLabel("Hiányzó adat!");
	private JLabel kozteruletHibasLabel = new JLabel("Hiányzó adat!");
	private JLabel hazszamHibasLabel = new JLabel("Hiányzó vagy hibás adat!");
	
	private JButton felhasznaloLetrehozasaButton = new JButton("Üzemeltetõ cég létrehozása", new ImageIcon("images/Save.png"));
	private JButton mezokTorleseButton = new JButton("Mezõk törlése", new ImageIcon("images/Delete.png"));
	
	{
		elsoSorPanel.setLayout(new GridLayout(1, 6));
		JPanel elsoSorElsoMezoPanel = new JPanel();
		elsoSorElsoMezoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel elsoSorMasodikMezoPanel = new JPanel();
		elsoSorMasodikMezoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel elsoSorHarmadikMezoPanel = new JPanel();
		elsoSorHarmadikMezoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel elsoSorNegyedikMezoPanel = new JPanel();
		elsoSorNegyedikMezoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel elsoSorOtodikMezoPanel = new JPanel();
		elsoSorOtodikMezoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel elsoSorHatodikMezoPanel = new JPanel();
		elsoSorHatodikMezoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		elsoSorPanel.add(elsoSorElsoMezoPanel);
		elsoSorPanel.add(elsoSorMasodikMezoPanel);
		elsoSorPanel.add(elsoSorHarmadikMezoPanel);
		elsoSorPanel.add(elsoSorNegyedikMezoPanel);
		elsoSorPanel.add(elsoSorOtodikMezoPanel);
		elsoSorPanel.add(elsoSorHatodikMezoPanel);
		elsoSorElsoMezoPanel.add(new JLabel("Név:"));
		elsoSorMasodikMezoPanel.add(nevTextField);
		elsoSorHarmadikMezoPanel.add(nevHibasLabel);
		elsoSorNegyedikMezoPanel.add(new JLabel("Irányítószám:"));
		elsoSorOtodikMezoPanel.add(iranyitoszamTextField);
		elsoSorHatodikMezoPanel.add(iranyitoszamHibasLabel);
		
		masodikSorPanel.setLayout(new GridLayout(1, 6));
		JPanel masodikSorElsoMezoPanel = new JPanel();
		masodikSorElsoMezoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel masodikSorMasodikMezoPanel = new JPanel();
		masodikSorMasodikMezoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel masodikSorHarmadikMezoPanel = new JPanel();
		masodikSorHarmadikMezoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel masodikSorNegyedikMezoPanel = new JPanel();
		masodikSorNegyedikMezoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel masodikSorOtodikMezoPanel = new JPanel();
		masodikSorOtodikMezoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel masodikSorHatodikMezoPanel = new JPanel();
		masodikSorHatodikMezoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		masodikSorPanel.add(masodikSorElsoMezoPanel);
		masodikSorPanel.add(masodikSorMasodikMezoPanel);
		masodikSorPanel.add(masodikSorHarmadikMezoPanel);
		masodikSorPanel.add(masodikSorNegyedikMezoPanel);
		masodikSorPanel.add(masodikSorOtodikMezoPanel);
		masodikSorPanel.add(masodikSorHatodikMezoPanel);
		masodikSorElsoMezoPanel.add(new JLabel("Adószám:"));
		masodikSorMasodikMezoPanel.add(adoszamTextField);
		masodikSorHarmadikMezoPanel.add(adoszamHibasLabel);
		masodikSorNegyedikMezoPanel.add(new JLabel("Település:"));
		masodikSorOtodikMezoPanel.add(telepulesTextField);
		masodikSorHatodikMezoPanel.add(telepulesHibasLabel);
		
		harmadikSorPanel.setLayout(new GridLayout(1, 6));
		JPanel harmadikSorElsoMezoPanel = new JPanel();
		harmadikSorElsoMezoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel harmadikSorMasodikMezoPanel = new JPanel();
		harmadikSorMasodikMezoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel harmadikSorHarmadikMezoPanel = new JPanel();
		harmadikSorHarmadikMezoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel harmadikSorNegyedikMezoPanel = new JPanel();
		harmadikSorNegyedikMezoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel harmadikSorOtodikMezoPanel = new JPanel();
		harmadikSorOtodikMezoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel harmadikSorHatodikMezoPanel = new JPanel();
		harmadikSorHatodikMezoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		harmadikSorPanel.add(harmadikSorElsoMezoPanel);
		harmadikSorPanel.add(harmadikSorMasodikMezoPanel);
		harmadikSorPanel.add(harmadikSorHarmadikMezoPanel);
		harmadikSorPanel.add(harmadikSorNegyedikMezoPanel);
		harmadikSorPanel.add(harmadikSorOtodikMezoPanel);
		harmadikSorPanel.add(harmadikSorHatodikMezoPanel);
		harmadikSorElsoMezoPanel.add(new JLabel("Telefonszám:"));
		harmadikSorMasodikMezoPanel.add(telefonTextField);
		harmadikSorHarmadikMezoPanel.add(new JLabel());
		harmadikSorNegyedikMezoPanel.add(new JLabel("Közterület:"));
		harmadikSorOtodikMezoPanel.add(kozteruletTextField);
		harmadikSorHatodikMezoPanel.add(kozteruletHibasLabel);
		
		negyedikSorPanel.setLayout(new GridLayout(1, 6));
		JPanel negyedikSorElsoMezoPanel = new JPanel();
		negyedikSorElsoMezoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel negyedikSorMasodikMezoPanel = new JPanel();
		negyedikSorMasodikMezoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel negyedikSorHarmadikMezoPanel = new JPanel();
		negyedikSorHarmadikMezoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel negyedikSorNegyedikMezoPanel = new JPanel();
		negyedikSorNegyedikMezoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel negyedikSorOtodikMezoPanel = new JPanel();
		negyedikSorOtodikMezoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel negyedikSorHatodikMezoPanel = new JPanel();
		negyedikSorHatodikMezoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		negyedikSorPanel.add(negyedikSorElsoMezoPanel);
		negyedikSorPanel.add(negyedikSorMasodikMezoPanel);
		negyedikSorPanel.add(negyedikSorHarmadikMezoPanel);
		negyedikSorPanel.add(negyedikSorNegyedikMezoPanel);
		negyedikSorPanel.add(negyedikSorOtodikMezoPanel);
		negyedikSorPanel.add(negyedikSorHatodikMezoPanel);
		negyedikSorElsoMezoPanel.add(new JLabel("Faxszám:"));
		negyedikSorMasodikMezoPanel.add(faxTextField);
		negyedikSorHarmadikMezoPanel.add(new JLabel());
		negyedikSorNegyedikMezoPanel.add(new JLabel("Közterület jellege:"));
		negyedikSorOtodikMezoPanel.add(kozteruletJellegeComboBox);
		negyedikSorHatodikMezoPanel.add(new JLabel());
		
		otodikSorPanel.setLayout(new GridLayout(1, 6));
		JPanel otodikSorElsoMezoPanel = new JPanel();
		otodikSorElsoMezoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel otodikSorMasodikMezoPanel = new JPanel();
		otodikSorMasodikMezoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel otodikSorHarmadikMezoPanel = new JPanel();
		otodikSorHarmadikMezoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel otodikSorNegyedikMezoPanel = new JPanel();
		otodikSorNegyedikMezoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel otodikSorOtodikMezoPanel = new JPanel();
		otodikSorOtodikMezoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel otodikSorHatodikMezoPanel = new JPanel();
		otodikSorHatodikMezoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		otodikSorPanel.add(otodikSorElsoMezoPanel);
		otodikSorPanel.add(otodikSorMasodikMezoPanel);
		otodikSorPanel.add(otodikSorHarmadikMezoPanel);
		otodikSorPanel.add(otodikSorNegyedikMezoPanel);
		otodikSorPanel.add(otodikSorOtodikMezoPanel);
		otodikSorPanel.add(otodikSorHatodikMezoPanel);
		otodikSorElsoMezoPanel.add(new JLabel("E-mail cím:"));
		otodikSorMasodikMezoPanel.add(emailTextField);
		otodikSorHarmadikMezoPanel.add(new JLabel());
		otodikSorNegyedikMezoPanel.add(new JLabel("Házszám:"));
		otodikSorOtodikMezoPanel.add(hazszamTextField);
		otodikSorHatodikMezoPanel.add(hazszamHibasLabel);
		
		hatodikSorPanel.setLayout(new GridLayout(1, 6));
		JPanel hatodikSorElsoMezoPanel = new JPanel();
		hatodikSorElsoMezoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel hatodikSorMasodikMezoPanel = new JPanel();
		hatodikSorMasodikMezoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel hatodikSorHarmadikMezoPanel = new JPanel();
		hatodikSorHarmadikMezoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel hatodikSorNegyedikMezoPanel = new JPanel();
		hatodikSorNegyedikMezoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel hatodikSorOtodikMezoPanel = new JPanel();
		hatodikSorOtodikMezoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel hatodikSorHatodikMezoPanel = new JPanel();
		hatodikSorHatodikMezoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		hatodikSorPanel.add(hatodikSorElsoMezoPanel);
		hatodikSorPanel.add(hatodikSorMasodikMezoPanel);
		hatodikSorPanel.add(hatodikSorHarmadikMezoPanel);
		hatodikSorPanel.add(hatodikSorNegyedikMezoPanel);
		hatodikSorPanel.add(hatodikSorOtodikMezoPanel);
		hatodikSorPanel.add(hatodikSorHatodikMezoPanel);
		hatodikSorElsoMezoPanel.add(new JLabel("Webcím:"));
		hatodikSorMasodikMezoPanel.add(webcimTextField);
		hatodikSorHarmadikMezoPanel.add(new JLabel());
		hatodikSorNegyedikMezoPanel.add(new JLabel("Emelet:"));
		hatodikSorOtodikMezoPanel.add(emeletTextField);
		hatodikSorHatodikMezoPanel.add(new JLabel());
		
		hetedikSorPanel.setLayout(new GridLayout(1, 6));
		JPanel hetedikSorElsoMezoPanel = new JPanel();
		hetedikSorElsoMezoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel hetedikSorMasodikMezoPanel = new JPanel();
		hetedikSorMasodikMezoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel hetedikSorHarmadikMezoPanel = new JPanel();
		hetedikSorHarmadikMezoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel hetedikSorNegyedikMezoPanel = new JPanel();
		hetedikSorNegyedikMezoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel hetedikSorOtodikMezoPanel = new JPanel();
		hetedikSorOtodikMezoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel hetedikSorHatodikMezoPanel = new JPanel();
		hetedikSorHatodikMezoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		hetedikSorPanel.add(hetedikSorElsoMezoPanel);
		hetedikSorPanel.add(hetedikSorMasodikMezoPanel);
		hetedikSorPanel.add(hetedikSorHarmadikMezoPanel);
		hetedikSorPanel.add(hetedikSorNegyedikMezoPanel);
		hetedikSorPanel.add(hetedikSorOtodikMezoPanel);
		hetedikSorPanel.add(hetedikSorHatodikMezoPanel);
		hetedikSorElsoMezoPanel.add(new JLabel());
		hetedikSorMasodikMezoPanel.add(new JLabel());
		hetedikSorHarmadikMezoPanel.add(new JLabel());
		hetedikSorNegyedikMezoPanel.add(new JLabel("Ajtó:"));
		hetedikSorOtodikMezoPanel.add(ajtoTextField);
		hetedikSorHatodikMezoPanel.add(new JLabel());
		
		nyolcadikSorPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		nyolcadikSorPanel.add(felhasznaloLetrehozasaButton);
		nyolcadikSorPanel.add(mezokTorleseButton);
		
		for(Cim.KozteruletJellege kj : Cim.KozteruletJellege.values()) {
			kozteruletJellegeComboBox.addItem(kj.irottFormaban());
		}
		kozteruletJellegeComboBox.setSelectedItem(Cim.KozteruletJellege.UT.irottFormaban());
		
		nevHibasLabel.setForeground(Color.RED);
		adoszamHibasLabel.setForeground(Color.RED);
		iranyitoszamHibasLabel.setForeground(Color.RED);
		telepulesHibasLabel.setForeground(Color.RED);
		kozteruletHibasLabel.setForeground(Color.RED);
		hazszamHibasLabel.setForeground(Color.RED);
		
		nevHibasLabel.setVisible(false);
		adoszamHibasLabel.setVisible(false);
		iranyitoszamHibasLabel.setVisible(false);
		telepulesHibasLabel.setVisible(false);
		kozteruletHibasLabel.setVisible(false);
		hazszamHibasLabel.setVisible(false);
	}

	public UjUzemeltetoCegPanel() {
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED) , "Új üzemeltetõ cég létrehozása"));
		setLayout(new GridLayout(8, 1));
		add(elsoSorPanel);
		add(masodikSorPanel);
		add(harmadikSorPanel);
		add(negyedikSorPanel);
		add(otodikSorPanel);
		add(hatodikSorPanel);
		add(hetedikSorPanel);
		add(nyolcadikSorPanel);
		
		felhasznaloLetrehozasaButton.addActionListener(this);
		mezokTorleseButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if(actionEvent.getSource() == felhasznaloLetrehozasaButton) {
			nevHibasLabel.setVisible(false);
			adoszamHibasLabel.setVisible(false);
			iranyitoszamHibasLabel.setVisible(false);
			telepulesHibasLabel.setVisible(false);
			kozteruletHibasLabel.setVisible(false);
			hazszamHibasLabel.setVisible(false);
			boolean hibasAdat = false;
			long adoszam = 0;
			int iranyitoszam = 0;
			int hazszam = 0;
			if(nevTextField.getText().trim().equals("")) {
				nevHibasLabel.setVisible(true);
				hibasAdat = true;
			}
			if(!adoszamTextField.getText().trim().equals("")) {
				try {
					adoszam = Long.parseLong(adoszamTextField.getText().trim());
				} catch (NumberFormatException e) {
					adoszamHibasLabel.setVisible(true);
					hibasAdat = true;
				}
			}
			if(iranyitoszamTextField.getText().trim().equals("")) {
				iranyitoszamHibasLabel.setVisible(true);
				hibasAdat = true;
			} else {
				try {
					iranyitoszam = Integer.parseInt(iranyitoszamTextField.getText().trim());
				} catch (NumberFormatException e) {
					iranyitoszamHibasLabel.setVisible(true);
					hibasAdat = true;
				}
			}
			if(telepulesTextField.getText().trim().equals("")) {
				telepulesHibasLabel.setVisible(true);
				hibasAdat = true;
			}
			if(kozteruletTextField.getText().trim().equals("")) {
				kozteruletHibasLabel.setVisible(true);
				hibasAdat = true;
			}
			if(hazszamTextField.getText().trim().equals("")) {
				hazszamHibasLabel.setVisible(true);
				hibasAdat = true;
			} else {
				try {
					hazszam = Integer.parseInt(hazszamTextField.getText().trim());
				} catch (NumberFormatException e) {
					hazszamHibasLabel.setVisible(true);
					hibasAdat = true;
				}
			}
			if(hibasAdat)
				return; 
			else {
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				Telepules telepules = new Telepules(iranyitoszam, telepulesTextField.getText().trim());
				Cim cim = new Cim.Builder(telepules, kozteruletTextField.getText().trim(), hazszam).kozteruletJellege(kozteruletJellegeComboBox.getItemAt(kozteruletJellegeComboBox.getSelectedIndex())).emelet(emeletTextField.getText().trim()).ajto(ajtoTextField.getText().trim()).build();
				Ceg uzemeltetoCeg = new Ceg.Builder(nevTextField.getText().trim(), cim).adoszam(adoszam).telefon(telefonTextField.getText().trim()).fax(faxTextField.getText().trim()).email(emailTextField.getText().trim()).webcim(webcimTextField.getText().trim()).build();
				Object parameterek[] = { uzemeltetoCeg };
				Valasz valasz = null;
				boolean kivetelNemVolt = true;
				if(GuiEszkozok.kapcsolatokNyitasa()) {
					try {
						GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.UZEMELTETOCEG_LETREHOZASA, parameterek, GuiEszkozok.getFelhasznalo()));
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
							if(valasz.getEset() == Eset.MAR_VAN_ILYEN_OBJEKTUM)
								JOptionPane.showMessageDialog(this, "Már van ilyen üzemeltetõ cég", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
							else
								JOptionPane.showMessageDialog(this, valasz.getEset(), "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(this, "Üzemeltetõ cég létrehozva " + uzemeltetoCeg.getNev() + " néven", "Üzemeltetõ cég létrehozása", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					if(valasz == null) {
						JOptionPane.showMessageDialog(this, "Megszakadt a kapcsolat az alkalmazásszerverrel", "Nincs visszaigazolás a szervertõl", JOptionPane.ERROR_MESSAGE);
					}
				}
				setCursor(Cursor.getDefaultCursor());
			}
		}
		else if(actionEvent.getSource() == mezokTorleseButton) {
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
}

