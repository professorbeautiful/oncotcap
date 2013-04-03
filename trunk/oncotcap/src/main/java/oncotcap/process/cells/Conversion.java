package oncotcap.process.cells;

import oncotcap.process.*;

public abstract class Conversion extends AbstractKineticEvent{

	public OncIDEnum [] destLevels;

	public Conversion(OncParent parent)
	{
		super(parent);
	}

	public CellPopulation daughters(CellCollection myCollection, double nParentCells, AbstractCell ct)
	{
		CellPopulation cpop = new CellPopulation();
		
		AbstractCell mutct = myCollection.foundCellType(ct, destLevels);
		if(mutct == null) {
			mutct = (AbstractCell) ct.clone(destLevels);
//			mutct.changeProp( destLevels);
//			myCollection.add(mutct);
		}
		cpop.setCellCount(new Double(nParentCells), mutct);
		return (cpop);
	}
}