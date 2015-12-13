package client.gui;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NyitoPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JLabel logo = new JLabel(new ImageIcon("images/Pie Chart.png"));
	private JLabel cim = new JLabel("Készletnyilvántartó- és számlázó alkalmazás", JLabel.CENTER);
	private JLabel nev = new JLabel("Gábor Balázs szakdolgozata", JLabel.CENTER);
	private JLabel megbizo = new JLabel("Miskolci Egyetem 2012", JLabel.CENTER);
	
	{
		logo.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
		cim.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		nev.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
		megbizo.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
	}
	
	public NyitoPanel() {
		setBorder(BorderFactory.createEmptyBorder(50, 1, 1, 1));
		setLayout(new GridLayout(4, 1));
		add(logo);
		add(cim);
		add(nev);
		add(megbizo);
	}
}
