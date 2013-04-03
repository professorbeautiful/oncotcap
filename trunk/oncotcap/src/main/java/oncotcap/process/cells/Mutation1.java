package oncotcap.process.cells;

import oncotcap.process.OncIDEnum;

public class Mutation1 extends KinEvent{
    OncIDEnum destLevel;

    public Mutation1(String cellpopLabel, OncIDEnum destLevel )
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
          mutct.changeProp(destLevel);
          myCollection.add(mutct);
        }
        cpop.setCellCount(new Double(n), mutct);
        return (cpop);
    }
}