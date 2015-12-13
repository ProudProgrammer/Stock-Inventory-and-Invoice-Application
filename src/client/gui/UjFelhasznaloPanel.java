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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import server.database.DataBaseOperationException.Eset;

import common.entities.Felhasznalo;
import common.entities.Keres;
import common.entities.Valasz;
import common.entities.Keres.Operations;

public class UjFelhasznaloPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel nevPanel = new JPanel();
	private JPanel jelszoPanel = new JPanel();
	private JPanel teljesNevPanel = new JPanel();
	private JPanel tajSzamPanel = new JPanel();
	private JPanel felhasznaloLetrehozasaPanel = new JPanel();
	
	private JTextField nevTextField = new JTextField(15);
	private JTextField jelszoTextField = new JTextField(15);
	private JTextField teljesNevTextField = new JTextField(15);
	private JTextField tajSzamTextField = new JTextField(15);
	
	private JLabel nevHibasLabel = new JLabel("Hiányzó adat!");
	private JLabel jelszoHibasLabel = new JLabel("Hiányzó adat!");
	private JLabel teljesNevHibasLabel = new JLabel("Hiányzó adat!");
	private JLabel tajSzamHibasLabel = new JLabel("Hiányzó adat!");
	
	private JButton felhasznaloLetrehozasaButton = new JButton("Felhasználó létrehozása", new ImageIcon("images/Save.png"));
	private JButton mezokTorleseButton = new JButton("Mezõk törlése", new ImageIcon("images/Delete.png"));
	
	{
		nevPanel.setLayout(new GridLayout(1, 3));
		JPanel nevPanelBal = new JPanel();
		nevPanelBal.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel nevPanelKozep = new JPanel();
		nevPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel nevPanelJobb = new JPanel();
		nevPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		nevPanel.add(nevPanelBal);
		nevPanel.add(nevPanelKozep);
		nevPanel.add(nevPanelJobb);
		nevPanelBal.add(new JLabel("Név:"));
		nevPanelKozep.add(nevTextField);
		nevPanelJobb.add(nevHibasLabel);
		
		jelszoPanel.setLayout(new GridLayout(1, 3));
		JPanel jelszoPanelBal = new JPanel();
		jelszoPanelBal.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel jelszoPanelKozep = new JPanel();
		jelszoPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel jelszoPanelJobb = new JPanel();
		jelszoPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		jelszoPanel.add(jelszoPanelBal);
		jelszoPanel.add(jelszoPanelKozep);
		jelszoPanel.add(jelszoPanelJobb);
		jelszoPanelBal.add(new JLabel("Jelszó:"));
		jelszoPanelKozep.add(jelszoTextField);
		jelszoPanelJobb.add(jelszoHibasLabel);
		
		teljesNevPanel.setLayout(new GridLayout(1, 3));
		JPanel teljesNevPanelBal = new JPanel();
		teljesNevPanelBal.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel teljesNevPanelKozep = new JPanel();
		teljesNevPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel teljesNevPanelJobb = new JPanel();
		teljesNevPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		teljesNevPanel.add(teljesNevPanelBal);
		teljesNevPanel.add(teljesNevPanelKozep);
		teljesNevPanel.add(teljesNevPanelJobb);
		teljesNevPanelBal.add(new JLabel("Teljes név:"));
		teljesNevPanelKozep.add(teljesNevTextField);
		teljesNevPanelJobb.add(teljesNevHibasLabel);
		
		tajSzamPanel.setLayout(new GridLayout(1, 3));
		JPanel tajSzamPanelBal = new JPanel();
		tajSzamPanelBal.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel tajSzamPanelKozep = new JPanel();
		tajSzamPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel tajSzamPanelJobb = new JPanel();
		tajSzamPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		tajSzamPanel.add(tajSzamPanelBal);
		tajSzamPanel.add(tajSzamPanelKozep);
		tajSzamPanel.add(tajSzamPanelJobb);
		tajSzamPanelBal.add(new JLabel("TAJ szám:"));
		tajSzamPanelKozep.add(tajSzamTextField);
		tajSzamPanelJobb.add(tajSzamHibasLabel);
		
		felhasznaloLetrehozasaPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		felhasznaloLetrehozasaPanel.add(felhasznaloLetrehozasaButton);
		felhasznaloLetrehozasaPanel.add(mezokTorleseButton);
		
		nevHibasLabel.setForeground(Color.RED);
		jelszoHibasLabel.setForeground(Color.RED);
		teljesNevHibasLabel.setForeground(Color.RED);
		tajSzamHibasLabel.setForeground(Color.RED);
		
		nevHibasLabel.setVisible(false);
		jelszoHibasLabel.setVisible(false);
		teljesNevHibasLabel.setVisible(false);
		tajSzamHibasLabel.setVisible(false);
	}

	public UjFelhasznaloPanel() {
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED) , "Új felhasználó létrehozása"));
		setLayout(new GridLayout(5, 1));
		add(nevPanel);
		add(jelszoPanel);
		add(teljesNevPanel);
		add(tajSzamPanel);
		add(felhasznaloLetrehozasaPanel);
		
		felhasznaloLetrehozasaButton.addActionListener(this);
		mezokTorleseButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if(actionEvent.getSource() == felhasznaloLetrehozasaButton) {
			nevHibasLabel.setVisible(false);
			jelszoHibasLabel.setVisible(false);
			teljesNevHibasLabel.setVisible(false);
			tajSzamHibasLabel.setVisible(false);
			boolean hibasAdat = false;
			if(nevTextField.getText().trim().equals("")) {
				nevHibasLabel.setVisible(true);
				hibasAdat = true;
			}
			if(jelszoTextField.getText().trim().equals("")) {
				jelszoHibasLabel.setVisible(true);
				hibasAdat = true;
			}
			if(teljesNevTextField.getText().trim().equals("")) {
				teljesNevHibasLabel.setVisible(true);
				hibasAdat = true;
			}
			if(tajSzamTextField.getText().trim().equals("")) {
				tajSzamHibasLabel.setVisible(true);
				hibasAdat = true;
			}
			if(hibasAdat)
				return;
			else {
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				Felhasznalo felhasznalo = new Felhasznalo.Builder(nevTextField.getText().trim(), jelszoTextField.getText().trim()).teljesNev(teljesNevTextField.getText().trim()).tajSzam(tajSzamTextField.getText().trim()).build();
				Object parameterek[] = { felhasznalo };
				Valasz valasz = null;
				boolean kivetelNemVolt = true;
				if(GuiEszkozok.kapcsolatokNyitasa()) {
					try {
						GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.FELHASZNALO_LETREHOZASA, parameterek, GuiEszkozok.getFelhasznalo()));
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
								JOptionPane.showMessageDialog(this, "Már van ilyen felhasználó", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(this, "Felhasználó létrehozva " + felhasznalo.getNev() + " néven", "Felhasználó létrehozása", JOptionPane.INFORMATION_MESSAGE);
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
			jelszoTextField.setText("");
			teljesNevTextField.setText("");
			tajSzamTextField.setText("");
		}
	}
}
