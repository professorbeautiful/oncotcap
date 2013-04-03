package oncotcap.process.cells;

import oncotcap.process.OncParent;
import oncotcap.process.OncProcess;
import oncotcap.sim.EventManager;
import oncotcap.sim.schedule.MasterScheduler;

public class CancerCellCollection<T extends OncProcess> extends CellCollection<T>
{

	public CancerCellCollection(MasterScheduler sched, EventManager evMgr)
	{
		super(sched, evMgr);
	}
	public String getName()
	{
		return("CancerCells");
	}

}