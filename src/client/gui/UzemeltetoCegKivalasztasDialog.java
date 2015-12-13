package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class UzemeltetoCegKivalasztasDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private UzemeltetoCegListazasPanel panel = new UzemeltetoCegListazasPanel(GuiEszkozok.panelTipus.KIVALASZTAS);

	public UzemeltetoCegKivalasztasDialog(JPanel owner) {
		setBounds(GuiEszkozok.getAlapKepernyoMeret());
		setModal(true);
		setTitle("Üzemeltetõ cég kiválasztása");
		setIconImage(this.getToolkit().createImage("images/Profile.png"));
		setContentPane(panel);
		setLocationRelativeTo(owner);
		
		panel.getKivalasztasButton().addActionListener(this);
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == panel.getKivalasztasButton()) {
			setVisible(false);
		}
		
	}
}
