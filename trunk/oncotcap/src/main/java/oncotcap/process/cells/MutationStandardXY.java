package oncotcap.process.cells;

import oncotcap.process.*;

public abstract class MutationStandardXY extends AbstractKineticEvent{

	public OncIDEnum [] destLevels;

	public MutationStandardXY(OncParent parent)
	{
		super(parent);
	}

	public CellPopulation daughters(CellCollection myCollection, double nParentCells, AbstractCell ct)
	{
		CellPopulation cpop = new CellPopulation();
		cpop.setCellCount(new Double(nParentCells), ct);

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