package client.gui;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import common.entities.Felhasznalo;
import common.entities.Keres;
import common.entities.Valasz;
import common.entities.Keres.Operations;

public final class BejelentkezesDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final JPanel tartalomPanel = new JPanel();
	private final JPanel nevPanel = new JPanel();
	private final JPanel jelszoPanel = new JPanel();
	private final JPanel buttonPanel = new JPanel();
	
	private final JTextField nevTextField = new JTextField(10);
	private final JPasswordField jelszoTextField = new JPasswordField(10);
	private final JButton bejelentkezesButton = new JButton("Bejelentkezés", new ImageIcon("images/Loading.png"));
	private final JButton kilepesButton = new JButton("Kilépés", new ImageIcon("images/Exit.png"));
	
	{
		nevPanel.setLayout(new GridLayout(1, 2));
		JPanel nevPanelBal = new JPanel();
		nevPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel nevPanelJobb = new JPanel();
		nevPanelJobb.setLayout(new FlowLayout(FlowLayout.RIGHT));
		nevPanel.add(nevPanelBal);
		nevPanel.add(nevPanelJobb);
		nevPanelBal.add(new JLabel("Név:"));
		nevPanelJobb.add(nevTextField);
		
		jelszoPanel.setLayout(new GridLayout(1, 2));
		JPanel jelszoPanelBal = new JPanel();
		jelszoPanelBal.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel jelszoPanelJobb = new JPanel();
		jelszoPanelJobb.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jelszoPanel.add(jelszoPanelBal);
		jelszoPanel.add(jelszoPanelJobb);
		jelszoPanelBal.add(new JLabel("Jelszó:"));
		jelszoPanelJobb.add(jelszoTextField);
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
		buttonPanel.add(bejelentkezesButton);
		buttonPanel.add(kilepesButton);
		
		tartalomPanel.setLayout(new GridLayout(3,1));
		tartalomPanel.add(nevPanel);
		tartalomPanel.add(jelszoPanel);
		tartalomPanel.add(buttonPanel);
	}
	
	public BejelentkezesDialog(Frame owner) {
		setSize(400, 200);
		setResizable(false);
		setModal(true);
		setTitle("Bejelentkezés");
		setIconImage(this.getToolkit().createImage("images/Loading.png"));
		setContentPane(tartalomPanel);
		pack();
		setLocationRelativeTo(owner);
		
		nevTextField.addActionListener(this);
		jelszoTextField.addActionListener(this);
		bejelentkezesButton.addActionListener(this);
		kilepesButton.addActionListener(this);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		setVisible(true);
	}

	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == jelszoTextField || event.getSource() == nevTextField || event.getSource() == bejelentkezesButton) {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			@SuppressWarnings("deprecation")
			Felhasznalo felhasznalo = new Felhasznalo.Builder(nevTextField.getText().trim(), jelszoTextField.getText().trim()).build();
			Object parameterek[] = { felhasznalo };
			Valasz valasz = null;
			boolean kivetelNemVolt = true;
			if(GuiEszkozok.kapcsolatokNyitasa()) {
				try {
					GuiEszkozok.getOutputStream().writeObject(new Keres(Operations.FELHASZNALO_KIVALASZTASA_NEV_JELSZO_ALAPJAN, parameterek, felhasznalo));
					GuiEszkozok.getOutputStream().flush();
					valasz = (Valasz)GuiEszkozok.getInputStream().readObject();
				} catch (IOException e) {
					kivetelNemVolt = false;
					JOptionPane.showMessageDialog(null, "Nem elérhetõ a szerver a data/client.properties fájl beállításai alapján\n" +
							"Lehetséges, hogy nincs is elindítva az alkalmazásszerver", "IOException", JOptionPane.ERROR_MESSAGE);
				} catch (ClassNotFoundException e) {
					kivetelNemVolt = false;
					JOptionPane.showMessageDialog(null, "Az alkalmazás egyes részei nem elérhetõk. Telepítse újra", "ClassNotFoundException", JOptionPane.ERROR_MESSAGE);
				} finally {
					GuiEszkozok.kapcsolatokZarasa();
				}
				if(valasz != null && kivetelNemVolt) {
					if(valasz.getAdat() != null) {
						GuiEszkozok.setFelhasznalo((Felhasznalo)valasz.getAdat());
						setVisible(false);
					} else if(valasz.getVoltESQLException()) {
						int hibakod = valasz.getSQLException().getErrorCode();
						JOptionPane.showMessageDialog(this, GuiEszkozok.getHibauzenetek(hibakod), "SQLException " + hibakod + " Error Code", JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "A megadott név/jelszó párossal a belépés nem engedélyezett", "Belépés nem engedélyezett", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			setCursor(Cursor.getDefaultCursor());
		}
		if(event.getSource() == kilepesButton) {
			System.exit(0);
		}
	}
}

