package client.print;

public final class SzamlaVegosszegFormazo {

	private SzamlaVegosszegFormazo() {}

	/**
	 * @param szam
	 * @return szam kerekítve egész számmá majd az utolsó számjegye ha 1,2,7,8 akkor 0, ha 3,4,6,7 akkor 5.
	 */
	public static long kerekites(double szam) {
		long kerekitettSzam = Math.round(szam);
		StringBuilder sb = new StringBuilder(Long.toString(kerekitettSzam));
		char utolsoSzamjegy = sb.charAt(sb.length() - 1);
		if (utolsoSzamjegy == '0' || utolsoSzamjegy == '1'
				|| utolsoSzamjegy == '2') {
			utolsoSzamjegy = '0';
			sb.setCharAt(sb.length() - 1, utolsoSzamjegy);
		} else if (utolsoSzamjegy == '8' || utolsoSzamjegy == '9') {
			utolsoSzamjegy = '9';
			sb.setCharAt(sb.length() - 1, utolsoSzamjegy);
			sb.append(".9");
			double koztesSzam = Double.valueOf(sb.toString());
			long koztesKerekitettSzam = Math.round(koztesSzam);
			sb = new StringBuilder(Long.toString(koztesKerekitettSzam));
		} else {
			utolsoSzamjegy = '5';
			sb.setCharAt(sb.length() - 1, utolsoSzamjegy);
		}
		return Long.valueOf(sb.toString());
	}

	/**
	 * @param szam intervallum : 0-999999999 
	 * @throws NumberFormatException ha szam.length > 9
	 */
	public static String szamSzovegesen(int szam) {
		char[] szamok = Integer.toString(szam).toCharArray();
		if(szamok.length > 9)
			throw new NumberFormatException("Túl nagy szám");
		StringBuilder szamSzovegesen = new StringBuilder();
		int helyertek = 0;
		for (int poz = szamok.length - 1; poz >= 0; poz--) {
			helyertek++;
			String szamjegy = "";
			if (helyertek == 2 || helyertek == 5 || helyertek == 8) {
				switch (szamok[poz]) {
				case '1':
					szamjegy = szamok[poz+1] == '0' ? "tíz" : "tizen";
					break;
				case '2':
					szamjegy = szamok[poz+1] == '0' ? "húsz" : "huszon";
					break;
				case '3':
					szamjegy = "harminc";
					break;
				case '4':
					szamjegy = "negyven";
					break;
				case '5':
					szamjegy = "ötven";
					break;
				case '6':
					szamjegy = "hatvan";
					break;
				case '7':
					szamjegy = "hetven";
					break;
				case '8':
					szamjegy = "nyolcvan";
					break;
				case '9':
					szamjegy = "kilencven";
					break;
				}
			}
			if (helyertek == 1 || helyertek == 3 || helyertek == 4 || helyertek == 6 || 
					helyertek == 7 || helyertek == 9) {
				switch (szamok[poz]) {
				case '1':
					szamjegy = "egy";
					break;
				case '2':
					szamjegy = "kettõ";
					break;
				case '3':
					szamjegy = "három";
					break;
				case '4':
					szamjegy = "négy";
					break;
				case '5':
					szamjegy = "öt";
					break;
				case '6':
					szamjegy = "hat";
					break;
				case '7':
					szamjegy = "hét";
					break;
				case '8':
					szamjegy = "nyolc";
					break;
				case '9':
					szamjegy = "kilenc";
					break;
				}
			}
			if((helyertek == 3 || helyertek == 6 || helyertek == 9) & szamok[poz] != '0') 
				szamjegy = szamjegy + "száz";
			if(helyertek == 4 & szamok[poz] != '0')
				szamjegy = (szamok[poz+1] == '0' & szamok[poz+2] == '0' & szamok[poz+3] == '0') ? szamjegy + "ezer" : szamjegy + "ezer-";
			if(helyertek == 5 & szamok[poz] != '0')
				if(szamok[poz+1] == '0')
					szamjegy = (szamok[poz+1] == '0' & szamok[poz+2] == '0' & szamok[poz+3] == '0' & szamok[poz+4] == '0') ? szamjegy + "ezer" : szamjegy + "ezer-";
			if(helyertek == 6 & szamok[poz] != '0')
				if(szamok[poz+1] == '0' & szamok[poz+2] == '0')
					szamjegy = (szamok[poz+1] == '0' & szamok[poz+2] == '0' & szamok[poz+3] == '0' & szamok[poz+4] == '0' & szamok[poz+5] == '0') ? szamjegy + "ezer" : szamjegy + "ezer-";
			if(helyertek == 7)
				szamjegy = (szamok[poz+1] == '0' & szamok[poz+2] == '0' & szamok[poz+3] == '0' & szamok[poz+4] == '0' & szamok[poz+5] == '0' & szamok[poz+6] == '0') ? szamjegy + "millió" : szamjegy + "millió-";
			szamSzovegesen.insert(0, szamjegy);
		}
		return szamSzovegesen.toString();
	}
}
