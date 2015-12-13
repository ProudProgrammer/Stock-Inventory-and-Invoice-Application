package client.gui;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
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

public final class FelhasznaloModositasDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final Felhasznalo felhasznalo;
	
	private final JPanel tartalomPanel = new JPanel();
	private final JPanel jelszoPanel = new JPanel();
	private final JPanel teljesNevPanel = new JPanel();
	private final JPanel tajSzamPanel = new JPanel();
	private final JPanel buttonPanel = new JPanel();
	
	private final JTextField jelszoTextField = new JTextField(15);
	private final JTextField teljesNevTextField = new JTextField(15);
	private final JTextField tajSzamTextField = new JTextField(15);
	private final JButton modositasButton = new JButton("M�dos�t�s", new ImageIcon("images/Save.png"));
	private final JButton megsemButton = new JButton("M�gsem", new ImageIcon("images/Exit.png"));
	
	{
		jelszoPanel.setLayout(new GridLayout(1, 2));
		JPanel jelszoPanelBal = new JPanel();
		jelszoPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel jelszoPanelJobb = new JPanel();
		jelszoPanelJobb.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jelszoPanel.add(jelszoPanelBal);
		jelszoPanel.add(jelszoPanelJobb);
		jelszoPanelBal.add(new JLabel("Jelsz�:"));
		jelszoPanelJobb.add(jelszoTextField);
		
		teljesNevPanel.setLayout(new GridLayout(1, 2));
		JPanel teljesNevPanelBal = new JPanel();
		teljesNevPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel teljesNevPanelJobb = new JPanel();
		teljesNevPanelJobb.setLayout(new FlowLayout(FlowLayout.RIGHT));
		teljesNevPanel.add(teljesNevPanelBal);
		teljesNevPanel.add(teljesNevPanelJobb);
		teljesNevPanelBal.add(new JLabel("Teljes n�v:"));
		teljesNevPanelJobb.add(teljesNevTextField);
		
		tajSzamPanel.setLayout(new GridLayout(1, 2));
		JPanel tajSzamPanelBal = new JPanel();
		tajSzamPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel tajSzamPanelJobb = new JPanel();
		tajSzamPanelJobb.setLayout(new FlowLayout(FlowLayout.RIGHT));
		tajSzamPanel.add(tajSzamPanelBal);
		tajSzamPanel.add(tajSzamPanelJobb);
		tajSzamPanelBal.add(new JLabel("TAJ sz�m:"));
		tajSzamPanelJobb.add(tajSzamTextField);
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
		buttonPanel.add(modositasButton);
		buttonPanel.add(megsemButton);
		
		tartalomPanel.setLayout(new GridLayout(4,1));
		tartalomPanel.add(jelszoPanel);
		tartalomPanel.add(teljesNevPanel);
		tartalomPanel.add(tajSzamPanel);
		tartalomPanel.add(buttonPanel);
	}
	
	public FelhasznaloModositasDialog(JPanel owner, Felhasznalo felhasznalo) {
		this.felhasznalo = felhasznalo;
		jelszoTextField.setText(felhasznalo.getJelszo());
		teljesNevTextField.setText(felhasznalo.getTeljesNev());
		tajSzamTextField.setText(felhasznalo.getTajSzam());
		
		setSize(400, 200);
		setResizable(false);
		setModal(true);
		setTitle("Felhaszn�l� m�dos�t�sa");
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
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			felhasznalo.setJelszo(jelszoTextField.getText().trim());
			felhasznalo.setTeljesNev(teljesNevTextField.getText().trim());
			felhasznalo.setTajSzam(tajSzamTextField.getText().trim());
			Object parameterek[] = { felhasznalo.getNev(), felhasznalo };
			Valasz valasz = null;
			boolean kivetelNemVolt = true;
			if(GuiEszkozok.kapcsolatokNyitasa()) {
				try {
					GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.FELHASZNALO_MODOSITASA, parameterek, GuiEszkozok.getFelhasznalo()));
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
									"Lehets�ges, hogy egy m�sik kezel� �ppen t�r�lte vagy m�dos�totta", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
						else if(valasz.getEset() == Eset.MAR_VAN_ILYEN_OBJEKTUM)
							JOptionPane.showMessageDialog(this, "A felhaszn�l� m�r szerepel az adatb�zisban", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
						else
							JOptionPane.showMessageDialog(this, valasz.getEset(), "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "Felhaszn�l� m�dos�tva", "Felhaszn�l� m�dos�t�sa", JOptionPane.INFORMATION_MESSAGE);
					}
				} else if(valasz == null) {
					JOptionPane.showMessageDialog(this, "Megszakadt a kapcsolat az alkalmaz�sszerverrel", "Nincs v�lasz a szervert�l", JOptionPane.ERROR_MESSAGE);
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

