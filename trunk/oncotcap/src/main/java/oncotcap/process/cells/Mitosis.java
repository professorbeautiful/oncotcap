package oncotcap.process.cells;

public class Mitosis extends KinEvent{
	public Mitosis(String cellpopLabel ){
		super(cellpopLabel);
		name=new String("mitosis");
	}

	public CellPopulation daughters(Object parent, double n, AbstractCell ct){
		CellPopulation cpop = new CellPopulation();
		cpop.setCellCount(new Double(2*n), ct);
		return (cpop);
	}
}