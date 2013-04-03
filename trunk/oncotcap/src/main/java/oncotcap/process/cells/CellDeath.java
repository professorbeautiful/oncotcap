package oncotcap.process.cells;

public class CellDeath extends KinEvent{
	public CellDeath(String cellpopLabel ){
		this(cellpopLabel, "cell death");
	}
	public CellDeath(String cellpopLabel, String name ){
		super(cellpopLabel);
		this.name= new String(name);
		descriptors = new String[1];
		descriptors[0] = "apoptosis 1";
	}
	public CellPopulation daughters(Object parent, double n, AbstractCell ct)
	{
		CellPopulation cpop = new CellPopulation();
		
		//if after a cell line dies out (cellcount == 0) should we
		//remove it from it's collection??????
		
		return (cpop);
	}
}