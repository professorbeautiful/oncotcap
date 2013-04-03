package oncotcap.process.cells;

public class CellPopLabel {
	String s;
	CellPopLabel(String s){
		this.s = new String(s);
	}
	public String getLabel(){
		return(s);
	}
}