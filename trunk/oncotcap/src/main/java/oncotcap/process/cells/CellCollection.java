package oncotcap.process.cells;

import java.util.*;
import oncotcap.*;
import oncotcap.process.*;
import oncotcap.sim.EventManager;
import oncotcap.sim.schedule.*;
import oncotcap.util.*;

public class CellCollection<T extends OncProcess> extends OncCollection<T>
{
	private boolean verbose = false;
	
	public CellCollection(MasterScheduler sched, EventManager evMgr)
	{
		super(sched, evMgr);
	}
	Vector cellCountChanges = new Vector();

	public AbstractCell foundCellType(AbstractCell ct)
	{
		Iterator iter = objs.iterator();
		while (iter.hasNext()){
			AbstractCell cp = (AbstractCell) iter.next();
			if ( ct.equals(cp))
			   return(cp);
		}
		return (null);
	}
	public String getName()
	{
		return("Cells");
	}
	public double getCellCount()
	{
		double count = 0;
		Iterator iter = objs.iterator();
		while(iter.hasNext())
		{
			AbstractCell cp = (AbstractCell) iter.next();
			count = count + cp.getCellCount();
		}
		return(count);
	}
	public double getCellCount(OncEnum value)
	{
		double count = 0;
		Iterator iter = objs.iterator();
		while (iter.hasNext())
		{
			AbstractCell cp = (AbstractCell) iter.next();
			if (cp.containsProp(value))
				count = count + cp.getCellCount();
		}
		return(count);
	}
	public AbstractCell foundCellType(AbstractCell ct, OncIDEnum value)
	{
		Iterator iter = objs.iterator();
		while (iter.hasNext()){
			AbstractCell cp = (AbstractCell) iter.next();
			if ( cp.equals(ct, value))
				return(cp);
		}
		return (null);
	}
	public AbstractCell foundCellType(AbstractCell ct, OncIDEnum [] props)
	{
		Iterator iter = objs.iterator();
		while (iter.hasNext())
		{
			AbstractCell cp = (AbstractCell) iter.next();
			if(props.length == 0 && cp.equals(ct))
				return(cp);
			else
			{
				if(cp.equals(ct, props))
					return(cp);
			}
		}
		return(null);
	}

	public void collection_update()
	{
		cellCountChanges.clear();
		super.collection_update();
		applyCellCountChanges();
		if(verbose)
		{
			//System.out.println("CANCER cell collection time = " + MasterScheduler.globalTime);
			Iterator it = objs.iterator();
			while(it.hasNext())
			{
				AbstractCell cellType = (AbstractCell) it.next();
				//System.out.println("CELLTYPE: " + cellType.getLevels() + " = " + cellType.getCellCount());
			}
		}

	}
	public void update(){}
	public void addCellCountChange(Object o)
	{
		cellCountChanges.add(o);
	}

	public void clearCellCountChanges()
	{
		cellCountChanges.clear();
	}

	public void applyCellCountChanges()
	{
		Iterator iter = cellCountChanges.iterator();
		while (iter.hasNext()){
			CellPopulation cpop = (CellPopulation) iter.next();
			Iterator iter2 = cpop.entrySet().iterator();
			while (iter2.hasNext()){
				Map.Entry thisEntry = (Map.Entry) iter2.next();
				AbstractCell ct = (AbstractCell) thisEntry.getKey();
				AbstractCell foundCT = foundCellType(ct);
				if ( foundCT == null){
					foundCT = (AbstractCell) ct.clone();
				}
				foundCT.addToCellCount((CellCount)thisEntry.getValue());
			}
		}
	}
}