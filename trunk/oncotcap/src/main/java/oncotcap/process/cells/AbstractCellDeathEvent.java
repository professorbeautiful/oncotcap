package oncotcap.process.cells;
import oncotcap.*;
import oncotcap.process.*;

public abstract class AbstractCellDeathEvent extends AbstractKineticEvent
{
	public AbstractCellDeathEvent(OncParent parent)
	{
		super(parent);
	}
	public CellPopulation daughters(CellCollection myCollection, double n, AbstractCell ct)
	{
		CellPopulation cpop = new CellPopulation();

		//if after a cell line dies out (cellcount == 0) should we
		//remove it from it's collection??????

		return (cpop);
	}
}