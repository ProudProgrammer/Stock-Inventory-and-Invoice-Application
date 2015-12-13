package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class TermekKivalasztasDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private TermekListazasPanel panel = new TermekListazasPanel(GuiEszkozok.panelTipus.KIVALASZTAS);

	public TermekKivalasztasDialog(JPanel owner) {
		setBounds(GuiEszkozok.getAlapKepernyoMeret());
		setModal(true);
		setTitle("Term�k kiv�laszt�sa");
		setIconImage(this.getToolkit().createImage("images/Add.png"));
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
