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

import common.entities.Keres;
import common.entities.Termek;
import common.entities.Valasz;
import common.entities.Keres.Operations;

public class UjTermekPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel megnevezesPanel = new JPanel();
	private JPanel vtszPanel = new JPanel();
	private JPanel aktualisAfaSzazaleklabPanel = new JPanel();
	private JPanel mennyisegEgysegePanel = new JPanel();
	private JPanel termekLetrehozasaPanel = new JPanel();
	
	private JTextField megnevezesTextField = new JTextField(15);
	private JTextField vtszTextField = new JTextField(15);
	private JTextField aktualisAfaSzazaleklabTextField = new JTextField(15);
	
	private JComboBox<String> mennyisegEgysegeComboBox = new JComboBox<String>();
	
	private JLabel megnevezesHibasLabel = new JLabel("Hiányzó adat!");
	private JLabel vtszHibasLabel = new JLabel("Hibás adat!");
	private JLabel aktualisAfaSzazaleklabHibasLabel = new JLabel("Hiányzó vagy hibás adat!");
	
	private JButton termekLetrehozasaButton = new JButton("Termék létrehozása", new ImageIcon("images/Save.png"));
	private JButton mezokTorleseButton = new JButton("Mezõk törlése", new ImageIcon("images/Delete.png"));
	
	{
		megnevezesPanel.setLayout(new GridLayout(1, 3));
		JPanel nevPanelBal = new JPanel();
		nevPanelBal.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel nevPanelKozep = new JPanel();
		nevPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel nevPanelJobb = new JPanel();
		nevPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		megnevezesPanel.add(nevPanelBal);
		megnevezesPanel.add(nevPanelKozep);
		megnevezesPanel.add(nevPanelJobb);
		nevPanelBal.add(new JLabel("Megnevezés:"));
		nevPanelKozep.add(megnevezesTextField);
		nevPanelJobb.add(megnevezesHibasLabel);
		
		vtszPanel.setLayout(new GridLayout(1, 3));
		JPanel jelszoPanelBal = new JPanel();
		jelszoPanelBal.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel jelszoPanelKozep = new JPanel();
		jelszoPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel jelszoPanelJobb = new JPanel();
		jelszoPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		vtszPanel.add(jelszoPanelBal);
		vtszPanel.add(jelszoPanelKozep);
		vtszPanel.add(jelszoPanelJobb);
		jelszoPanelBal.add(new JLabel("VTSZ:"));
		jelszoPanelKozep.add(vtszTextField);
		jelszoPanelJobb.add(vtszHibasLabel);
		
		aktualisAfaSzazaleklabPanel.setLayout(new GridLayout(1, 3));
		JPanel teljesNevPanelBal = new JPanel();
		teljesNevPanelBal.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel teljesNevPanelKozep = new JPanel();
		teljesNevPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel teljesNevPanelJobb = new JPanel();
		teljesNevPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		aktualisAfaSzazaleklabPanel.add(teljesNevPanelBal);
		aktualisAfaSzazaleklabPanel.add(teljesNevPanelKozep);
		aktualisAfaSzazaleklabPanel.add(teljesNevPanelJobb);
		teljesNevPanelBal.add(new JLabel("Aktuális ÁFA százalékláb:"));
		teljesNevPanelKozep.add(aktualisAfaSzazaleklabTextField);
		teljesNevPanelJobb.add(aktualisAfaSzazaleklabHibasLabel);
		
		mennyisegEgysegePanel.setLayout(new GridLayout(1, 3));
		JPanel tajSzamPanelBal = new JPanel();
		tajSzamPanelBal.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel tajSzamPanelKozep = new JPanel();
		tajSzamPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel tajSzamPanelJobb = new JPanel();
		tajSzamPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		mennyisegEgysegePanel.add(tajSzamPanelBal);
		mennyisegEgysegePanel.add(tajSzamPanelKozep);
		mennyisegEgysegePanel.add(tajSzamPanelJobb);
		tajSzamPanelBal.add(new JLabel("Mennyiség egysége:"));
		tajSzamPanelKozep.add(mennyisegEgysegeComboBox);
		tajSzamPanelJobb.add(new JLabel());
		
		for(Termek.MennyisegEgysege me : Termek.MennyisegEgysege.values()) {
			mennyisegEgysegeComboBox.addItem(me.irottFormaban());
		}
		mennyisegEgysegeComboBox.setSelectedItem(Termek.MennyisegEgysege.DARAB.irottFormaban());
		
		termekLetrehozasaPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		termekLetrehozasaPanel.add(termekLetrehozasaButton);
		termekLetrehozasaPanel.add(mezokTorleseButton);
		
		megnevezesHibasLabel.setForeground(Color.RED);
		vtszHibasLabel.setForeground(Color.RED);
		aktualisAfaSzazaleklabHibasLabel.setForeground(Color.RED);
		
		megnevezesHibasLabel.setVisible(false);
		vtszHibasLabel.setVisible(false);
		aktualisAfaSzazaleklabHibasLabel.setVisible(false);
	}

	public UjTermekPanel() {
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED) , "Új termék létrehozása"));
		setLayout(new GridLayout(5, 1));
		add(megnevezesPanel);
		add(vtszPanel);
		add(aktualisAfaSzazaleklabPanel);
		add(mennyisegEgysegePanel);
		add(termekLetrehozasaPanel);
		
		termekLetrehozasaButton.addActionListener(this);
		mezokTorleseButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if(actionEvent.getSource() == termekLetrehozasaButton) {
			megnevezesHibasLabel.setVisible(false);
			vtszHibasLabel.setVisible(false);
			aktualisAfaSzazaleklabHibasLabel.setVisible(false);
			boolean hibasAdat = false;
			int vtsz = 0;
			double afaSzazaleklab = 0;
			if(megnevezesTextField.getText().trim().equals("")) {
				megnevezesHibasLabel.setVisible(true);
				hibasAdat = true;
			}
			if(!vtszTextField.getText().trim().equals("")) {
				try {
					vtsz = Integer.parseInt(vtszTextField.getText().trim());
				} catch (NumberFormatException e) {
					vtszHibasLabel.setVisible(true);
					hibasAdat = true;
				}
			}
			if(aktualisAfaSzazaleklabTextField.getText().trim().equals("")) {
				aktualisAfaSzazaleklabHibasLabel.setVisible(true);
				hibasAdat = true;
			} else {
				try {
					afaSzazaleklab = Double.parseDouble(aktualisAfaSzazaleklabTextField.getText().trim());
				} catch (NumberFormatException e) {
					aktualisAfaSzazaleklabHibasLabel.setVisible(true);
					hibasAdat = true;
				}
			}
			if(hibasAdat)
				return;
			else {
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				Termek termek = new Termek.Builder(megnevezesTextField.getText().trim(), afaSzazaleklab).vtsz(vtsz).mennyisegEgysege(mennyisegEgysegeComboBox.getItemAt(mennyisegEgysegeComboBox.getSelectedIndex())).build();
				Object parameterek[] = { termek };
				Valasz valasz = null;
				boolean kivetelNemVolt = true;
				if(GuiEszkozok.kapcsolatokNyitasa()) {
					try {
						GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.TERMEK_LETREHOZASA, parameterek, GuiEszkozok.getFelhasznalo()));
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
								JOptionPane.showMessageDialog(this, "Már van ilyen termék", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
							else if(valasz.getEset() == Eset.BEJELENTKEZES_NEM_ENGEDELYEZETT)
								JOptionPane.showMessageDialog(this, "A " + GuiEszkozok.getFelhasznalo().getNev() + " nevû felhasználó jogosultságát törölték", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(this, "Termék létrehozva " + termek.getMegnevezes() + " megnevezéssel", "Felhasználó létrehozása", JOptionPane.INFORMATION_MESSAGE);
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
			megnevezesTextField.setText("");
			vtszTextField.setText("");
			aktualisAfaSzazaleklabTextField.setText("");
		}
	}
}

