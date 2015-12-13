package server.database;

public class DataBaseOperationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public enum Eset {
		NINCS_ILYEN_OBJEKTUM,
		TÖBB_EGYEZO_OBJEKTUM,
		MAR_VAN_ILYEN_OBJEKTUM,
		KONKURENCIA_PROBLEMA,
		BEJELENTKEZES_NEM_ENGEDELYEZETT,
		SZAMLA_LETREHOZAS_HIBA,
		NINCS_ENNYI_RAKTARON,
		A_SZAMLA_MAR_SZTORNOZOTT;
	}
	
	private final Eset eset;

	public DataBaseOperationException(String uzenet, Eset eset) {
		super(uzenet);
		this.eset = eset;
	}
	
	public Eset getEset() {
		return eset;
	}
}
