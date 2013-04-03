package oncotcap.process.cells;

import oncotcap.process.OncIDEnum;

public class MutationXY extends KinEvent{
    OncIDEnum destLevel;

/** It seems to me that it would save time if
 ** (a) propToChange were an Enum from the parent celltype.
 ** (b) the parent celltype were passed as an argument & saved.
 ** Otherwise, maybe we don't need distinct copies of this object for each
 ** parent celltype.  Maybe store just one copy--
 ** say, in the CellCollection?
 ** **/

	public MutationXY(String cellpopLabel, OncIDEnum destLevel )
    {
      super(cellpopLabel);
      name=new String("mutation to " + destLevel.getClass() + "=" + destLevel);
      this.destLevel = destLevel;
    }

    public CellPopulation daughters(Object cellColl, double n, AbstractCell ct)
    {
    	CellCollection myCollection = (CellCollection) cellColl;
        CellPopulation cpop = new CellPopulation();
        cpop.setCellCount(new Double(n), ct);

	    AbstractCell mutct = myCollection.foundCellType(ct, destLevel);
        if(mutct == null)
        {
          mutct = (AbstractCell) ct.clone();
          mutct.changeProp( destLevel);
          myCollection.add(mutct);
        }
        cpop.setCellCount(new Double(n), mutct);
        return (cpop);
    }
}