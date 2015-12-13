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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import server.database.DataBaseOperationException.Eset;

import common.entities.Ceg;
import common.entities.Keres;
import common.entities.Valasz;
import common.entities.Keres.Operations;

public final class PartnerModositasDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final Ceg ceg;
	
	private final JPanel tartalomPanel = new JPanel();
	private final JPanel nevPanel = new JPanel();
	private final JPanel iranyitoszamPanel = new JPanel();
	private final JPanel telepulesPanel = new JPanel();
	private final JPanel kozteruletPanel = new JPanel();
	private final JPanel hazszamPanel = new JPanel();
	private final JPanel emeletPanel = new JPanel();
	private final JPanel ajtoPanel = new JPanel();
	private final JPanel adoszamPanel = new JPanel();
	private final JPanel telefonPanel = new JPanel();
	private final JPanel faxPanel = new JPanel();
	private final JPanel emailPanel = new JPanel();
	private final JPanel webPanel = new JPanel();
	private final JPanel buttonPanel = new JPanel();
	
	private final JTextField nevTextField = new JTextField(15);
	private final JTextField iranyitoszamTextField = new JTextField(15);
	private final JTextField telepulesTextField = new JTextField(15);
	private final JTextField kozteruletTextField = new JTextField(15);
	private final JTextField hazszamTextField = new JTextField(15);
	private final JTextField emeletTextField = new JTextField(15);
	private final JTextField ajtoTextField = new JTextField(15);
	private final JTextField adoszamTextField = new JTextField(15);
	private final JTextField telefonTextField = new JTextField(15);
	private final JTextField faxTextField = new JTextField(15);
	private final JTextField emailTextField = new JTextField(15);
	private final JTextField webcimTextField = new JTextField(15);
	
	private JLabel nevHibasLabel = new JLabel("Hi�nyz� adat!");
	private JLabel adoszamHibasLabel = new JLabel("Hib�s adat!");
	private JLabel iranyitoszamHibasLabel = new JLabel("Hi�nyz� vagy hib�s adat!");
	private JLabel telepulesHibasLabel = new JLabel("Hi�nyz� adat!");
	private JLabel kozteruletHibasLabel = new JLabel("Hi�nyz� adat!");
	private JLabel hazszamHibasLabel = new JLabel("Hi�nyz� vagy hib�s adat!");
	
	private final JButton modositasButton = new JButton("M�dos�t�s", new ImageIcon("images/Save.png"));
	private final JButton megsemButton = new JButton("M�gsem", new ImageIcon("images/Exit.png"));
	
	{
		nevPanel.setLayout(new GridLayout(1, 3));
		JPanel nevPanelBal = new JPanel();
		nevPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel nevPanelKozep = new JPanel();
		nevPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel nevPanelJobb = new JPanel();
		nevPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		nevPanel.add(nevPanelBal);
		nevPanel.add(nevPanelKozep);
		nevPanel.add(nevPanelJobb);
		nevPanelBal.add(new JLabel("N�v:"));
		nevPanelKozep.add(nevTextField);
		nevPanelJobb.add(nevHibasLabel);
		
		iranyitoszamPanel.setLayout(new GridLayout(1, 3));
		JPanel iranyitoszamPanelBal = new JPanel();
		iranyitoszamPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel iranyitoszamPanelKozep = new JPanel();
		iranyitoszamPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel iranyitoszamPanelJobb = new JPanel();
		iranyitoszamPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		iranyitoszamPanel.add(iranyitoszamPanelBal);
		iranyitoszamPanel.add(iranyitoszamPanelKozep);
		iranyitoszamPanel.add(iranyitoszamPanelJobb);
		iranyitoszamPanelBal.add(new JLabel("Ir�ny�t�sz�m:"));
		iranyitoszamPanelKozep.add(iranyitoszamTextField);
		iranyitoszamPanelJobb.add(iranyitoszamHibasLabel);
		
		telepulesPanel.setLayout(new GridLayout(1, 3));
		JPanel tajSzamPanelBal = new JPanel();
		tajSzamPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel tajSzamPanelKozep = new JPanel();
		tajSzamPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel tajSzamPanelJobb = new JPanel();
		tajSzamPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		telepulesPanel.add(tajSzamPanelBal);
		telepulesPanel.add(tajSzamPanelKozep);
		telepulesPanel.add(tajSzamPanelJobb);
		tajSzamPanelBal.add(new JLabel("Telep�l�s:"));
		tajSzamPanelKozep.add(telepulesTextField);
		tajSzamPanelJobb.add(telepulesHibasLabel);
		
		kozteruletPanel.setLayout(new GridLayout(1, 3));
		JPanel kozteruletPanelBal = new JPanel();
		kozteruletPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel kozteruletPanelKozep = new JPanel();
		kozteruletPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel kozteruletPanelJobb = new JPanel();
		kozteruletPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		kozteruletPanel.add(kozteruletPanelBal);
		kozteruletPanel.add(kozteruletPanelKozep);
		kozteruletPanel.add(kozteruletPanelJobb);
		kozteruletPanelBal.add(new JLabel("K�zter�let:"));
		kozteruletPanelKozep.add(kozteruletTextField);
		kozteruletPanelJobb.add(kozteruletHibasLabel);
		
		hazszamPanel.setLayout(new GridLayout(1, 3));
		JPanel hazszamPanelBal = new JPanel();
		hazszamPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel hazszamPanelKozep = new JPanel();
		hazszamPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel hazszamPanelJobb = new JPanel();
		hazszamPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		hazszamPanel.add(hazszamPanelBal);
		hazszamPanel.add(hazszamPanelKozep);
		hazszamPanel.add(hazszamPanelJobb);
		hazszamPanelBal.add(new JLabel("H�zsz�m:"));
		hazszamPanelKozep.add(hazszamTextField);
		hazszamPanelJobb.add(hazszamHibasLabel);
		
		emeletPanel.setLayout(new GridLayout(1, 3));
		JPanel emeletPanelBal = new JPanel();
		emeletPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel emeletPanelKozep = new JPanel();
		emeletPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel emeletPanelJobb = new JPanel();
		emeletPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		emeletPanel.add(emeletPanelBal);
		emeletPanel.add(emeletPanelKozep);
		emeletPanel.add(emeletPanelJobb);
		emeletPanelBal.add(new JLabel("Emelet:"));
		emeletPanelKozep.add(emeletTextField);
		emeletPanelJobb.add(new JLabel());
		
		ajtoPanel.setLayout(new GridLayout(1, 3));
		JPanel ajtoPanelBal = new JPanel();
		ajtoPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel ajtoPanelKozep = new JPanel();
		ajtoPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel ajtoPanelJobb = new JPanel();
		ajtoPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		ajtoPanel.add(ajtoPanelBal);
		ajtoPanel.add(ajtoPanelKozep);
		ajtoPanel.add(ajtoPanelJobb);
		ajtoPanelBal.add(new JLabel("Ajt�:"));
		ajtoPanelKozep.add(ajtoTextField);
		ajtoPanelJobb.add(new JLabel());
		
		adoszamPanel.setLayout(new GridLayout(1, 3));
		JPanel adoszamPanelBal = new JPanel();
		adoszamPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel adoszamPanelKozep = new JPanel();
		adoszamPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel adoszamPanelJobb = new JPanel();
		adoszamPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		adoszamPanel.add(adoszamPanelBal);
		adoszamPanel.add(adoszamPanelKozep);
		adoszamPanel.add(adoszamPanelJobb);
		adoszamPanelBal.add(new JLabel("Ad�sz�m:"));
		adoszamPanelKozep.add(adoszamTextField);
		adoszamPanelJobb.add(adoszamHibasLabel);
		
		telefonPanel.setLayout(new GridLayout(1, 3));
		JPanel telefonPanelBal = new JPanel();
		telefonPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel telefonPanelKozep = new JPanel();
		telefonPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel telefonPanelJobb = new JPanel();
		telefonPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		telefonPanel.add(telefonPanelBal);
		telefonPanel.add(telefonPanelKozep);
		telefonPanel.add(telefonPanelJobb);
		telefonPanelBal.add(new JLabel("Telefon:"));
		telefonPanelKozep.add(telefonTextField);
		telefonPanelJobb.add(new JLabel());
		
		faxPanel.setLayout(new GridLayout(1, 3));
		JPanel faxPanelBal = new JPanel();
		faxPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel faxPanelKozep = new JPanel();
		faxPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel faxPanelJobb = new JPanel();
		faxPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		faxPanel.add(faxPanelBal);
		faxPanel.add(faxPanelKozep);
		faxPanel.add(faxPanelJobb);
		faxPanelBal.add(new JLabel("Fax:"));
		faxPanelKozep.add(faxTextField);
		faxPanelJobb.add(new JLabel());
		
		emailPanel.setLayout(new GridLayout(1, 3));
		JPanel emailPanelBal = new JPanel();
		emailPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel emailPanelKozep = new JPanel();
		emailPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel emailPanelJobb = new JPanel();
		emailPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		emailPanel.add(emailPanelBal);
		emailPanel.add(emailPanelKozep);
		emailPanel.add(emailPanelJobb);
		emailPanelBal.add(new JLabel("Email:"));
		emailPanelKozep.add(emailTextField);
		emailPanelJobb.add(new JLabel());
		
		webPanel.setLayout(new GridLayout(1, 3));
		JPanel webPanelBal = new JPanel();
		webPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel webPanelKozep = new JPanel();
		webPanelKozep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel webPanelJobb = new JPanel();
		webPanelJobb.setLayout(new FlowLayout(FlowLayout.LEFT));
		webPanel.add(webPanelBal);
		webPanel.add(webPanelKozep);
		webPanel.add(webPanelJobb);
		webPanelBal.add(new JLabel("Web:"));
		webPanelKozep.add(webcimTextField);
		webPanelJobb.add(new JLabel());
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
		buttonPanel.add(modositasButton);
		buttonPanel.add(megsemButton);
		
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
		
		tartalomPanel.setLayout(new GridLayout(13,1));
		tartalomPanel.add(nevPanel);
		tartalomPanel.add(iranyitoszamPanel);
		tartalomPanel.add(telepulesPanel);
		tartalomPanel.add(kozteruletPanel);
		tartalomPanel.add(hazszamPanel);
		tartalomPanel.add(emeletPanel);
		tartalomPanel.add(ajtoPanel);
		tartalomPanel.add(adoszamPanel);
		tartalomPanel.add(telefonPanel);
		tartalomPanel.add(faxPanel);
		tartalomPanel.add(emailPanel);
		tartalomPanel.add(webPanel);
		tartalomPanel.add(buttonPanel);
	}
	
	public PartnerModositasDialog(JPanel owner, Ceg ceg) {
		this.ceg = ceg;
		nevTextField.setText(ceg.getNev());
		iranyitoszamTextField.setText(Integer.toString(ceg.getCim().getTelepules().getIranyitoSzam()));
		telepulesTextField.setText(ceg.getCim().getTelepules().getNev());
		kozteruletTextField.setText(ceg.getCim().getKozterulet());
		hazszamTextField.setText(Integer.toString(ceg.getCim().getHazSzam()));
		emeletTextField.setText(ceg.getCim().getEmelet());
		ajtoTextField.setText(ceg.getCim().getAjto());
		adoszamTextField.setText(Long.toString(ceg.getAdoszam()));
		telefonTextField.setText(ceg.getTelefon());
		faxTextField.setText(ceg.getFax());
		emailTextField.setText(ceg.getEmail());
		webcimTextField.setText(ceg.getWebcim());
		setSize(400, 200);
		setResizable(false);
		setModal(true);
		setTitle("Partner m�dos�t�sa");
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
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			ceg.setNev(nevTextField.getText().trim());
			ceg.setAdoszam(adoszam);
			ceg.setTelefon(telefonTextField.getText().trim());
			ceg.setFax(faxTextField.getText().trim());
			ceg.setEmail(emailTextField.getText().trim());
			ceg.setWebcim(webcimTextField.getText().trim());
			ceg.getCim().getTelepules().setIranyitoSzam(iranyitoszam);
			ceg.getCim().getTelepules().setNev(telepulesTextField.getText().trim());
			ceg.getCim().setKozterulet(kozteruletTextField.getText().trim());
			ceg.getCim().setHazSzam(hazszam);
			ceg.getCim().setAjto(ajtoTextField.getText().trim());
			ceg.getCim().setEmelet(emeletTextField.getText().trim());
			Object parameterek[] = { ceg.getId(), ceg };
			Valasz valasz = null;
			boolean kivetelNemVolt = true;
			if(GuiEszkozok.kapcsolatokNyitasa()) {
				try {
					GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.PARTNERCEG_MODOSITASA, parameterek, GuiEszkozok.getFelhasznalo()));
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
							JOptionPane.showMessageDialog(this, "Nincs ilyen partner\n" +
									"Lehets�ges, hogy egy m�sik kezel� �ppen t�r�lte vagy m�dos�totta", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
						else if(valasz.getEset() == Eset.MAR_VAN_ILYEN_OBJEKTUM)
							JOptionPane.showMessageDialog(this, "A partner m�r szerepel az adatb�zisban", "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
						else
							JOptionPane.showMessageDialog(this, valasz.getEset(), "DataBaseOperationException", JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "Partner m�dos�tva", "Partner m�dos�t�sa", JOptionPane.INFORMATION_MESSAGE);
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

