package oncotcap.process.cells;

import oncotcap.*;
import oncotcap.process.*;

public abstract class AbstractKineticEvent extends OncProcess
{
	public double hazard, runningHazardSum, condProb;
	
	public AbstractKineticEvent(OncParent parent)
	{
		super(parent);
		if(parent instanceof AbstractCell)
			((AbstractCell) parent).addKinEvent(this);
	}
	abstract public CellPopulation daughters(CellCollection collectionParent, double n, AbstractCell ct);

}