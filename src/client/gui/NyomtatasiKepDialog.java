package client.gui;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import client.print.NyomtatasiKepPanel;
import client.print.NyomtatasiKepPanel.Tipus;

import common.entities.Szamla;

public class NyomtatasiKepDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public NyomtatasiKepDialog(JPanel owner, Szamla szamla) {
		setSize(800, 600);
		setResizable(false);
		setModal(false);
		setTitle("Nyomtatási kép");
		setIconImage(this.getToolkit().createImage("images/Picture.png"));
		setLocationRelativeTo(owner);
		
		NyomtatasiKepPanel nykp = new NyomtatasiKepPanel(szamla, Tipus.NYOMTATASI_KEP);
		JScrollPane sp = new JScrollPane(nykp);
		add(sp);
		
		setVisible(true);
	}
}
