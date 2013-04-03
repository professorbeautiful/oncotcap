package oncotcap.process.cells;

import oncotcap.*;
import oncotcap.process.*;

public abstract class AbstractMitosisEvent extends AbstractKineticEvent
{
	public AbstractMitosisEvent(OncParent parent)
	{
		super(parent);
	}

	public CellPopulation daughters(CellCollection parent, double n, AbstractCell ct)
	{
		CellPopulation cpop = new CellPopulation();
		cpop.setCellCount(new Double(2*n), ct);
		return (cpop);
	}

}