package oncotcap.process.cells;

import java.util.*;
import oncotcap.*;
import oncotcap.sim.random.*;
import oncotcap.util.*;
import oncotcap.sim.schedule.*;
import oncotcap.annotation.VariablePlotDescriptor;
import oncotcap.process.*;
import oncotcap.process.treatment.AbstractPatient;
import oncotcap.process.treatment.AbstractOncologist;

abstract public class AbstractCell extends OncProcess
{
	protected double probSum;
	@VariablePlotDescriptor(getYMin = 1.0, getYMax=1e13, isYAxisLogScale=true)
	public double cellcount = 0;
	protected Vector kinEventVector = new Vector();
	public AbstractCell(OncParent parent)
	{
		super(parent);
	}
	public void init()
	{
		super.init();
		setScheduleOffset(0.0);  //This is necessary!!
	}

//This needs later to be improved to make it robust to 
//    null parents,
//    changes in the heirarchy.
//These next three methods were removed from Protege.

	public void addKinEvent(AbstractKineticEvent kinEvent)
	{
		kinEventVector.add(kinEvent);
	}
/*	public CellCollection getCellCollection(){
		return ((CellCollection)oncParent);
	}
	public AbstractPatient getPatient(){
		return (AbstractPatient)(getCellCollection().oncParent);
	}

	public AbstractOncologist getOncologist(){
		return  (AbstractOncologist)(getPatient().getOncologist());
	}
*/
	public void  addToCellCount(Double n)
	{
		cellcount = cellcount + n.doubleValue();
	}
	public void  addToCellCount(CellCount n)
	{
		cellcount = cellcount + n.get().doubleValue();
	}
	public void  addToCellCount( int n)
	{
		cellcount = cellcount + (double)n;
	}
	public void setCellCount(double n)
	{
		cellcount = n;
	}
	public double getCellCount()
	{
		return(cellcount);
	}

	AbstractKineticEvent[] kinEventArray;
	
	protected void updateRunningSums()
	{
		double runningProbSum = 0;
		double runningHazardSum = 0;
		Iterator kinEvents;
		if(kinEventVector == null)
			System.out.println("BLECH2");
		kinEvents = kinEventVector.iterator();
		int n = kinEventVector.size();
		kinEventArray = new AbstractKineticEvent[n];
		int i=0;
		while(kinEvents.hasNext())
		{
			kinEventArray [i] = (AbstractKineticEvent) kinEvents.next();
			kinEventArray [i] .runningHazardSum = runningHazardSum;
			runningHazardSum = runningHazardSum + kinEventArray [i] .hazard;
			i++;
			//Logger.log("KinEvent " + ke.name + "  hazard=" + ke.hazard);
		}
		probSum = runningHazardSum * getCollection().getDeltaT();
		for (int i2=0; i2<kinEventArray.length; i2++)
		{
			AbstractKineticEvent ke = kinEventArray[i2];
			ke.condProb =  ke.hazard/(runningHazardSum - ke.runningHazardSum);
			//Logger.log("KinEvent " + ke.name + "  condProb=" + ke.condProb);
		}
	}

	public void update()
	{
		super.update();
		updateRunningSums();
		updateCellActions();
	}

	double nEvents, nEventsLeft;
	double nEventsAccountedFor;
	
	public void updateCellActions()
	{
		// Put code common to all CellProcess objects here.
		nEvents = nEventsLeft = Gbinomi.get(getCellCount(),probSum,getRNG());
		//Util.mark("nEvents = " + nEvents);
		boolean flagmeTemp = false;
//		System.err.println("UPDATE CELL ACTIONS " + kinEventArray.length);
		for (int i2=0; i2<kinEventArray.length; i2++) {
			if(nEventsLeft <= 0.0)
				break;
			AbstractKineticEvent ke = kinEventArray[i2];
			nEventsAccountedFor = Gbinomi.get((double) nEventsLeft, (double) ke.condProb,	getRNG());
//			System.err.println("UPDATE CELLS: " + this.getMasterScheduler().globalTime + " EVENTS: " + nEventsAccountedFor);
			nEventsLeft = nEventsLeft - nEventsAccountedFor;
			if (nEventsAccountedFor > 0)
				((CellCollection) getCollection()).cellCountChanges.add(ke.daughters((CellCollection) getCollection(), nEventsAccountedFor, this));
		}
		CellPopulation takeAway = new CellPopulation();
		takeAway.put(this,new CellCount(new Double(-1*nEvents)));
		((CellCollection)getCollection()).cellCountChanges.add(takeAway);
		// We choose to take away the parent cells separately;
		// When there are many mutations, this is more
		// efficient.

		if (flagmeTemp == true) {
			CellCollection coll = ((CellCollection)getCollection());
			Iterator changes = coll.cellCountChanges.iterator();
			while (changes.hasNext()) {
				CellPopulation cpop = (CellPopulation) changes.next();
				Iterator iter2 = cpop.entrySet().iterator();
				while (iter2.hasNext()){
					Map.Entry thisEntry = (Map.Entry) iter2.next();
					AbstractCell ct = (AbstractCell) thisEntry.getKey();
					AbstractCell foundCT = coll.foundCellType(ct);
					if ( foundCT == null){
						foundCT = (AbstractCell) ct.clone();
					}
					Logger.log(" !!Change of " + ((CellCount)thisEntry.getValue()).get()
									   +  " to " + foundCT);
//					foundCT.addToCellCount((CellCount)thisEntry.getValue());
				}
			}
		}

	}
/*	void writeKinEventInfo(KinEvent ke) {
		AbstractPatient pt = (AbstractPatient)(((CellCollection)oncParent).oncParent);
		int patientnumber = pt.thisptnum;
		Logger.log("\n!!!!!!!!!Patient " + patientnumber
						   + ": " + ke.name + " happened at time "
						   + MasterScheduler.globalTime);
		Logger.log("   Celltype " + toString());
		//Util.mark("Event:" + ke.name + "  nEventsAccountedFor=" + nEventsAccountedFor
		//		  + "  nEventsLeft=" + nEventsLeft );
		Logger.log("    Gbinomi args:  nEventsLeft=" + nEventsLeft
						   + "  ke.condProb= " + ke.condProb
						   + "  nEventsAccountedFor= " + nEventsAccountedFor);
		Iterator kinEventsTemp = kinEventVector.iterator();
		while(kinEventsTemp.hasNext()){
			KinEvent keTemp = (KinEvent) kinEventsTemp.next();
			Logger.log("     For " + keTemp.name
							   + "\t runningHazardSum = "  + keTemp.runningHazardSum
							   + "\t  condProb= " + keTemp.condProb
							   + "\t hazard = " + keTemp.hazard);
		}
	}
*/
	public double computeProbOfDeathFromLogkill(double logKill) {
		return 1.0 - Math.pow(10.0, -logKill);
	}
	public double computeHazardFromProb(double probabilityOfDying, double dT) {
		// prob = 1 - exp(-h*dt);   h = -log(1-prob)/dt;
		return   -Math.log(1-probabilityOfDying)/dT;
	}
	protected void killCellsWithTreatment(double logKill) {
		double prob = computeProbOfDeathFromLogkill(logKill);
		double cellsKilled = Gbinomi.get((double) this.getCellCount(), prob , getRNG());
		if (this.getCellCount()-cellsKilled  > 0.5)
			this.setCellCount(this.getCellCount()-cellsKilled);
		else
			this.setCellCount(0.0);
	}
	/* (non-Javadoc)
	 * @see oncotcap.process.OncProcess#decrementCount(int)
	 */
	@Override
	public void decrementCount(int decrementBy) {
		// not represented
	}
	/* (non-Javadoc)
	 * @see oncotcap.process.OncProcess#getCount()
	 */
	@Override
	public int getCount() {
		return (int)getCellCount();
	}
	/* (non-Javadoc)
	 * @see oncotcap.process.OncProcess#incrementCount(int)
	 */
	@Override
	public void incrementCount(int incrementBy) {
		addToCellCount(new Double(incrementBy));
		
	}
	/* (non-Javadoc)
	 * @see oncotcap.process.OncProcess#setCount(int)
	 */
	@Override
	public void setCount(int count) {
		System.out.println("Set cell count on " + this.getIDLevels() + " #" + count);
		setCellCount(new Double(count));
	}
}