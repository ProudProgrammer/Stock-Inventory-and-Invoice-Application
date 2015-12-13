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
import javax.swing.JDialog;
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

public final class TermekModositasDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final Termek termek;
	
	private final JPanel tartalomPanel = new JPanel();
	private final JPanel megnevezesPanel = new JPanel();
	private final JPanel vtszPanel = new JPanel();
	private final JPanel afaSzazalekLabPanel = new JPanel();
	private final JPanel mennyisegEgysegePanel = new JPanel();
	private final JPanel buttonPanel = new JPanel();
	
	private final JTextField megnevezesTextField = new JTextField(15);
	private final JTextField vtszTextField = new JTextField(15);
	private final JTextField afaSzazalekLabTextField = new JTextField(15);
	
	private JComboBox<String> mennyisegEgysegeComboBox = new JComboBox<String>();
	
	private JLabel megnevezesHibasLabel = new JLabel("Hiányzó adat!");
	private JLabel vtszHibasLabel = new JLabel("Hibás adat!");
	private JLabel afaSzazalekLabHibasLabel = new JLabel("Hiányzó vagy hibás adat!");
	
	private final JButton modositasButton = new JButton("Módosítás", new ImageIcon("images/Save.png"));
	private final JButton megsemButton = new JButton("Mégsem", new ImageIcon("images/Exit.png"));
	
	{
		megnevezesPanel.setLayout(new GridLayout(1, 3));
		JPanel megnevezesPanelBal = new JPanel();
		megnevezesPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel megnevezesPanelKozep = new JPanel();
		megnevezesPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel megnevezesPanelJobb = new JPanel();
		megnevezesPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		megnevezesPanel.add(megnevezesPanelBal);
		megnevezesPanel.add(megnevezesPanelKozep);
		megnevezesPanel.add(megnevezesPanelJobb);
		megnevezesPanelBal.add(new JLabel("Megnevezés:"));
		megnevezesPanelKozep.add(megnevezesTextField);
		megnevezesPanelJobb.add(megnevezesHibasLabel);
		
		vtszPanel.setLayout(new GridLayout(1, 3));
		JPanel vtszPanelBal = new JPanel();
		vtszPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel vtszPanelKozep = new JPanel();
		vtszPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel vtszPanelJobb = new JPanel();
		vtszPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		vtszPanel.add(vtszPanelBal);
		vtszPanel.add(vtszPanelKozep);
		vtszPanel.add(vtszPanelJobb);
		vtszPanelBal.add(new JLabel("VTSZ:"));
		vtszPanelKozep.add(vtszTextField);
		vtszPanelJobb.add(vtszHibasLabel);
		
		afaSzazalekLabPanel.setLayout(new GridLayout(1, 3));
		JPanel afaSzazalekLabPanelBal = new JPanel();
		afaSzazalekLabPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel afaSzazalekLabPanelKozep = new JPanel();
		afaSzazalekLabPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel afaSzazalekLabPanelJobb = new JPanel();
		afaSzazalekLabPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		afaSzazalekLabPanel.add(afaSzazalekLabPanelBal);
		afaSzazalekLabPanel.add(afaSzazalekLabPanelKozep);
		afaSzazalekLabPanel.add(afaSzazalekLabPanelJobb);
		afaSzazalekLabPanelBal.add(new JLabel("Aktuális áfa százalékláb:"));
		afaSzazalekLabPanelKozep.add(afaSzazalekLabTextField);
		afaSzazalekLabPanelJobb.add(afaSzazalekLabHibasLabel);
		
		mennyisegEgysegePanel.setLayout(new GridLayout(1, 3));
		JPanel mennyisegEgysegePanelBal = new JPanel();
		mennyisegEgysegePanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel mennyisegEgysegePanelKozep = new JPanel();
		mennyisegEgysegePanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel mennyisegEgysegePanelJobb = new JPanel();
		mennyisegEgysegePanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		mennyisegEgysegePanel.add(mennyisegEgysegePanelBal);
		mennyisegEgysegePanel.add(mennyisegEgysegePanelKozep);
		mennyisegEgysegePanel.add(mennyisegEgysegePanelJobb);
		mennyisegEgysegePanelBal.add(new JLabel("Mennyiseg egysége:"));
		mennyisegEgysegePanelKozep.add(mennyisegEgysegeComboBox);
		mennyisegEgysegePanelJobb.add(new JLabel());
		
		for(Termek.MennyisegEgysege me : Termek.MennyisegEgysege.values()) {
			mennyisegEgysegeComboBox.addItem(me.irottFormaban());
		}
		mennyisegEgysegeComboBox.setSelectedItem(Termek.MennyisegEgysege.DARAB.irottFormaban());
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
		buttonPanel.add(modositasButton);
		buttonPanel.add(megsemButton);
		
		megnevezesHibasLabel.setForeground(Color.RED);
		vtszHibasLabel.setForeground(Color.RED);
		afaSzazalekLabHibasLabel.setForeground(Color.RED);
		
		megnevezesHibasLabel.setVisible(false);
		vtszHibasLabel.setVisible(false);
		afaSzazalekLabHibasLabel.setVisible(false);
	
		tartalomPanel.setLayout(new GridLayout(5,1));
		tartalomPanel.add(megnevezesPanel);
		tartalomPanel.add(vtszPanel);
		tartalomPanel.add(afaSzazalekLabPanel);
		tartalomPanel.add(mennyisegEgysegePanel);
		tartalomPanel.add(buttonPanel);
	}
	
	public TermekModositasDialog(JPanel owner, Termek termek) {
		this.termek = termek;
		megnevezesTextField.setText(termek.getMegnevezes());
		vtszTextField.setText(Integer.toString(termek.getVtsz()));
		afaSzazalekLabTextField.setText(Double.toString(termek.getAfaSzazaleklab()));
		mennyisegEgysegeComboBox.setSelectedItem(termek.getMennyisegEgysege().irottFormaban());
		setSize(400, 200);
		setResizable(false);
		setModal(true);
		setTitle("Termék módosítása");
		setIconImage(this.getToolkit().createImage("images/Modify.png"));
		setContentPane(tartalomPanel);
		pack();
		setLocationRelativeTo(owner);
		
		modositasButton.addActionListener(this);
		megsemButton.addActionListener(this);
		
		setVisible(true);
	}

	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == modositasButton) {
			megnevezesHibasLabel.setVisible(false);
			vtszHibasLabel.setVisible(false);
			afaSzazalekLabHibasLabel.setVisible(false);
			boolean hibasAdat = false;
			if(megnevezesTextField.getText().trim().equals("")) {
				megnevezesHibasLabel.setVisible(true);
				hibasAdat = true;
			}
			int vtsz = 0;
			if(!vtszTextField.getText().trim().equals("")) {
				try {
					vtsz = Integer.parseInt(vtszTextField.getText().trim());
				} catch (NumberFormatException e) {
					vtszHibasLabel.setVisible(true);
					hibasAdat = true;
				}
			}
			double afaSzazaleklab = 0;
			if(afaSzazalekLabTextField.getText().trim().equals("")) {
				afaSzazalekLabHibasLabel.setVisible(true);
				hibasAdat = true;
			} else {
				try {
					afaSzazaleklab = Double.parseDouble(afaSzazalekLabTextField.getText().trim());
				} catch (NumberFormatException e) {
					afaSzazalekLabHibasLabel.setVisible(true);
					hibasAdat = true;
				}
			}
			if(hibasAdat)
				return;
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			termek.setMegnevezes(megnevezesTextField.getText().trim());
			termek.setVtsz(vtsz);
			termek.setAfaSzazaleklab(afaSzazaleklab);
			termek.setMennyisegEgysege(mennyisegEgysegeComboBox.getItemAt(mennyisegEgysegeComboBox.getSelectedIndex()));
			Object parameterek[] = { termek.getTermekkod(), termek };
			Valasz valasz = null;
			boolean kivetelNemVolt = true;
			if(GuiEszkozok.kapcsolatokNyitasa()) {
				try {
					GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.TERMEK_MODOSITASA, parameterek, GuiEszkozok.getFelhasznalo()));
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
						if(valasz.getEset() == Eset.NINCS_ILYEN_OBJEKTUM)
							JOptionPane.showMessageDialog(this, "Nincs ilyen termék\n" +
									"Lehetséges, hogy egy másik kezelõ éppen törölte vagy módosította", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
						else if(valasz.getEset() == Eset.MAR_VAN_ILYEN_OBJEKTUM)
							JOptionPane.showMessageDialog(this, "A termék már szerepel az adatbázisban", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
						else
							JOptionPane.showMessageDialog(this, valasz.getEset(), "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "Termék módosítva", "Termék módosítása", JOptionPane.INFORMATION_MESSAGE);
					}
				} else if(valasz == null) {
					JOptionPane.showMessageDialog(this, "Megszakadt a kapcsolat az alkalmazásszerverrel", "Nincs válasz a szervertõl", JOptionPane.ERROR_MESSAGE);
				}
			}
			setCursor(Cursor.getDefaultCursor());
			setVisible(false);
		}
		if(event.getSource() == megsemButton) {
			setVisible(false);
		}
	}
}

