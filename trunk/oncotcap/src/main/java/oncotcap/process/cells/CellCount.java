package oncotcap.process.cells;

public class CellCount{
	private Double d;
	CellCount(double d){
		this.d = new Double(d);
	}
	CellCount(Double d){
		this.d = new Double(d.doubleValue());
		/* Careful, you could skip the constructor,
		 * but then it's just a reference-
		 * the original might change.
		 */ 
	}
	public Double get(){
		return(this.d);
	}
	public void set(Double d){
		this.d = new Double(d.doubleValue());
	}
	public void change(Double d){
		this.d = new Double
			(this.d.doubleValue() + d.doubleValue());
		//This will be called many times! Make it efficient.
	}
}
