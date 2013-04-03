package oncotcap.process.cells;

import oncotcap.process.*;

public abstract class MutationStandardYZ extends AbstractKineticEvent{

	public OncIDEnum [] destLevelsY, destLevelsZ;

	public MutationStandardYZ(OncParent parent )
	{
		super(parent);
	}

	public CellPopulation daughters(CellCollection myCollection, double nParentCells, AbstractCell ct)
	{
		CellPopulation cpop = new CellPopulation();

		AbstractCell mutctY = myCollection.foundCellType(ct, destLevelsY);
		if(mutctY == null) {
			mutctY = (AbstractCell) ct.clone(destLevelsY);
//			mutctY.changeProp( destLevelsY);
//			myCollection.add(mutctY);
		}
		cpop.setCellCount(new Double(nParentCells), mutctY);
		AbstractCell mutctZ = myCollection.foundCellType(ct, destLevelsZ);
		if(mutctZ == null) {
			mutctZ = (AbstractCell) ct.clone(destLevelsZ);
//			mutctZ.changeProp( destLevelsZ);
//			myCollection.add(mutctZ);
		}
		cpop.setCellCount(new Double(nParentCells), mutctZ);
		return (cpop);
	}
}