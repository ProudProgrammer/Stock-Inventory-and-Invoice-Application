package client.print;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import common.entities.Szamla;
import common.entities.Termek;

public class NyomtatasiKepPanel extends JPanel {
	
	public enum Tipus {
		NYOMTATASI_KEP, NYOMTATAS;
	}

	private static final long serialVersionUID = 1L;
	
	private JPanel szamlaPanel = new JPanel();
	private JPanel eladoEsVevoPanel = new JPanel();
	private JPanel datumPanel = new JPanel();
	private JScrollPane tablazatPanel = new JScrollPane();
	private JPanel vegosszegPanel = new JPanel();
	
	private JLabel szamlaLabel = new JLabel("Számla");
	
	private JTextArea eladoTextArea = new JTextArea();
	private JTextArea vevoTextArea = new JTextArea();
	private JTextArea alsoTextArea = new JTextArea();
	
	private JLabel szamlaszamLabel = new JLabel();
	private JLabel fizetesiModLabel = new JLabel("Fizetési mód: Készpénzes");
	private JLabel teljesitesLabel = new JLabel();
	private JLabel szamlaKelteLabel = new JLabel();
	private JLabel fizetesiHataridoLabel = new JLabel();
	
	private Vector<Vector<String>> tablazatSorai = new Vector<Vector<String>>();
	private static final Vector<String> TABLAZAT_MEZO_NEVEK = new Vector<String>();
	private JTable szamlakTable = new JTable(null, TABLAZAT_MEZO_NEVEK);
	
	private static final NumberFormat penzFormatum = NumberFormat.getCurrencyInstance();
	private static final NumberFormat szazalekFormatum = NumberFormat.getPercentInstance();
	private static final DateFormat datumFormatum = DateFormat.getDateInstance();
	
	static {
		TABLAZAT_MEZO_NEVEK.add("Termékkód");
		TABLAZAT_MEZO_NEVEK.add("Megnevezés");
		TABLAZAT_MEZO_NEVEK.add("Mennyiség");
		TABLAZAT_MEZO_NEVEK.add("Menny.egy.");
		TABLAZAT_MEZO_NEVEK.add("Egységár");
		TABLAZAT_MEZO_NEVEK.add("Nettó érték");
		TABLAZAT_MEZO_NEVEK.add("Áfa%");
		TABLAZAT_MEZO_NEVEK.add("Áfa összes");
		TABLAZAT_MEZO_NEVEK.add("Bruttó érték");
		
		penzFormatum.setMinimumFractionDigits(2);
		penzFormatum.setMaximumFractionDigits(2);
		szazalekFormatum.setMinimumFractionDigits(2);
		szazalekFormatum.setMaximumFractionDigits(2);
	}
	
	{
		eladoTextArea.setLineWrap(true);
		vevoTextArea.setLineWrap(true);
		alsoTextArea.setLineWrap(true);
		
		eladoTextArea.setEditable(false);
		vevoTextArea.setEditable(false);
		alsoTextArea.setEditable(false);
		
		szamlaPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		szamlaPanel.setBackground(Color.WHITE);
		szamlaLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		szamlaPanel.add(szamlaLabel);
		
		eladoEsVevoPanel.setLayout(new GridLayout(1, 2));
		eladoEsVevoPanel.add(eladoTextArea);
		eladoEsVevoPanel.add(vevoTextArea);
		
		datumPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		datumPanel.setBackground(Color.WHITE);
		szamlaszamLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		fizetesiModLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		teljesitesLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		szamlaKelteLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		fizetesiHataridoLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		datumPanel.add(szamlaszamLabel);
		datumPanel.add(fizetesiModLabel);
		datumPanel.add(teljesitesLabel);
		datumPanel.add(szamlaKelteLabel);
		datumPanel.add(fizetesiHataridoLabel);
		
		tablazatPanel.setViewportView(szamlakTable);
		
		vegosszegPanel.setLayout(new BorderLayout());
		alsoTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		vegosszegPanel.add(alsoTextArea, BorderLayout.CENTER);
	}

	public NyomtatasiKepPanel(Szamla szamla, Tipus tipus) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(szamlaPanel);
		add(eladoEsVevoPanel);
		add(datumPanel);
		add(tablazatPanel);
		add(vegosszegPanel);
		
		String elado =
				"\n" + szamla.getElado().getNev() + 
				"\n" + szamla.getElado().getCim() +  
				(szamla.getElado().getAdoszam() == 0 ? "" : "\nAdószám: " + szamla.getElado().getAdoszam()) + 
				(szamla.getElado().getTelefon().equals("") ? "" : "\nTelefon: " + szamla.getElado().getTelefon()) + 
				(szamla.getElado().getFax().equals("") ? "" : "\nFax: " + szamla.getElado().getFax()) + 
				(szamla.getElado().getEmail().equals("") ? "" : "\nEmail: " + szamla.getElado().getEmail()) + 
				(szamla.getElado().getWebcim().equals("") ? "" : "\nWeb: " + szamla.getElado().getWebcim());
		eladoTextArea.append(elado);
		
		String vevo =
				"\n" + szamla.getVevo().getNev() + 
				"\n" + szamla.getVevo().getCim() +  
				(szamla.getVevo().getAdoszam() == 0 ? "" : "\nAdószám: " + szamla.getVevo().getAdoszam()) + 
				(szamla.getVevo().getTelefon().equals("") ? "" : "\nTelefon: " + szamla.getVevo().getTelefon()) + 
				(szamla.getVevo().getFax().equals("") ? "" : "\nFax: " + szamla.getVevo().getFax()) + 
				(szamla.getVevo().getEmail().equals("") ? "" : "\nEmail: " + szamla.getVevo().getEmail()) + 
				(szamla.getVevo().getWebcim().equals("") ? "" : "\nWeb: " + szamla.getVevo().getWebcim());;
		vevoTextArea.append(vevo);
		
		szamlaszamLabel.setText("Számlaszám: " + szamla.getSorszam());
		teljesitesLabel.setText("Teljesítés: " + datumFormatum.format(szamla.getDatum()));
		szamlaKelteLabel.setText("Számla  kelte: " + datumFormatum.format(szamla.getDatum()));
		fizetesiHataridoLabel.setText("Fizetési határidõ: " + datumFormatum.format(szamla.getDatum()));
		
		tablazatSorai.removeAllElements();
		for(Termek t : szamla.getTermekek()) {
			Vector<String> sor = new Vector<String>();
			sor.add(Integer.toString(t.getTermekkod()));
			sor.add(t.getMegnevezes());
			sor.add(Integer.toString(t.getMennyiseg()));
			sor.add(t.getMennyisegEgysege().irottFormaban());
			sor.add(penzFormatum.format(t.getEgysegNettoEladasiAr()));
			sor.add(penzFormatum.format(t.nettoEladasiAr()));
			sor.add(szazalekFormatum.format(t.getAfaSzazaleklab()/100));
			sor.add(penzFormatum.format(t.eladasiAfaErtek()));
			sor.add(penzFormatum.format(t.bruttoEladasiAr()));
			tablazatSorai.add(sor);
		}
		Vector<String> sor = new Vector<String>();
		sor.add("");
		sor.add("");
		sor.add("");
		sor.add("");
		sor.add("");
		sor.add(penzFormatum.format(szamla.nettoEladasiAr()));
		sor.add("");
		sor.add(penzFormatum.format(szamla.eladasiAfaErtek()));
		sor.add(penzFormatum.format(szamla.bruttoEladasiAr()));
		tablazatSorai.add(sor);
		szamlakTable = new JTable(tablazatSorai, TABLAZAT_MEZO_NEVEK) {

			private static final long serialVersionUID = 1L;
			
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		};
		tablazatPanel.setViewportView(szamlakTable);
		tablazatPanel.getViewport().setLayout(new GridLayout());
		tablazatPanel.getViewport().setBackground(Color.WHITE);
		
		long fizetendo = SzamlaVegosszegFormazo.kerekites(szamla.bruttoEladasiAr());
		String also = "Fizetendõ összesen: " + fizetendo + " forint" + 
				"\nAzaz " + SzamlaVegosszegFormazo.szamSzovegesen((int)fizetendo) + " forint" +
				"\n\n" + szamla.getMegjegyzes();
		alsoTextArea.append(also);
		
		if(tipus == Tipus.NYOMTATAS) {
			this.setMaximumSize(new Dimension(530,1000));  
	        this.setMinimumSize(new Dimension(530,1000));  
	        this.setPreferredSize(new Dimension(530,1000));
	        eladoTextArea.setFont(new Font(Font.SERIF, Font.PLAIN, 8));
	        vevoTextArea.setFont(new Font(Font.SERIF, Font.PLAIN, 8));
	        szamlaszamLabel.setFont(new Font(Font.SERIF, Font.BOLD, 8));
	        fizetesiModLabel.setFont(new Font(Font.SERIF, Font.BOLD, 8));
	        teljesitesLabel.setFont(new Font(Font.SERIF, Font.BOLD, 8));
	        szamlaKelteLabel.setFont(new Font(Font.SERIF, Font.BOLD, 8));
	        fizetesiHataridoLabel.setFont(new Font(Font.SERIF, Font.BOLD, 8));
	        alsoTextArea.setFont(new Font(Font.SERIF, Font.PLAIN, 8));
	        eladoTextArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), "Számlakiállító adatai", TitledBorder.DEFAULT_POSITION, TitledBorder.DEFAULT_JUSTIFICATION, new Font(Font.SERIF, Font.BOLD, 10)));
			vevoTextArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), "Vevõ adatai", TitledBorder.DEFAULT_POSITION, TitledBorder.DEFAULT_JUSTIFICATION, new Font(Font.SERIF, Font.BOLD, 10)));
			szamlaLabel.setFont(new Font(Font.SERIF, Font.BOLD, 15));
			szamlakTable.setFont(new Font(Font.SERIF, Font.PLAIN, 8));
			szamlakTable.getTableHeader().setFont(new Font(Font.SERIF, Font.BOLD, 8));
		} else {
			eladoTextArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), "Számlakiállító adatai", TitledBorder.DEFAULT_POSITION, TitledBorder.DEFAULT_JUSTIFICATION, new Font(Font.DIALOG, Font.BOLD, 15)));
			vevoTextArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), "Vevõ adatai", TitledBorder.DEFAULT_POSITION, TitledBorder.DEFAULT_JUSTIFICATION, new Font(Font.DIALOG, Font.BOLD, 15)));
			alsoTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			szamlaLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
		}
	}
}
