package oncotcap.process.cells;

import oncotcap.util.*;
import oncotcap.sim.schedule.*;
import oncotcap.process.treatment.*;
import java.util.*;

public class TumorSizeDetector extends OncObservable implements Observer, HasScheduler {
	double threshhold = 1.0e9;
	public Scheduler scheduler;  //Must be public so it can be rescheduled.
	AbstractPatient patient;
	Hashtable cellsubpopulation = new Hashtable();
	boolean sendBroadcast = true;
	private OncEnum value = null;
	
	public TumorSizeDetector(AbstractPatient patient) {
		super(true); //		mustBeReset = true;
		this.patient = patient;
		patient.myOncReporterObservers.add(this);
	}
	protected AbstractPatient getPatient()
	{
		return(patient);
	}
	public void setThreshHold (double threshhold) {
		this.threshhold = threshhold;
	}

	public void setCellType(OncEnum value)
	{
		this.value = value;
	}
	
	public double getThreshHold ()
	{
		return(threshhold);
	}

	public void setScheduler (Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public Scheduler getScheduler () {
		return scheduler;
	}

/*	public void setCellPopulation (String label, Hashtable cellsubpopulation) {
		//Later, we will have to distinguish kinds of populations,
		//cancer, endothelial, etc..
		this.cellsubpopulation = cellsubpopulation;
	} */
	
	public double getCellCount() {
		double cellcount = 0;
		Iterator it = patient.getCollections(AbstractCancerCell.class).iterator();
		if(value == null)
		{
			while(it.hasNext())
				cellcount += ((CellCollection) it.next()).getCellCount();
		}
		else
		{
			while(it.hasNext())
				cellcount += ((CellCollection) it.next()).getCellCount(value);
		}

	//this is the right way to do it, no doubt.......
/*		if (cellsubpopulation.size() == 0)
			cellcount = patient.cancerCellCollection.getCellCount();
		else if (cellsubpopulation.size() == 1) {
			//Map.Entry map = (Map.Entry) (cellsubpopulation.keys().toArray() [0]);
			String property = (String)(cellsubpopulation.keySet().toArray() [0]);
			String value = (String)cellsubpopulation.get(property);
			cellcount = patient.cancerCellCollection.getCellCount(property, value);
		}
		else
			Logger.log("Error in TumorSizeDetector.update, subpopulations with more than one condition are not yet implemented");
*/
		return cellcount;
	}

	public void update(Observable obs, Object arg) {
		update();
	}
	public void update() {
		if (getCellCount() > threshhold)
			notifyObservers(patient);
		// This will disArm this detector. 
	}
	
	public void reArm() {
		setChanged();
		// Unles this is called, the detector will not fire again.
	}

	public void setBroadcast(boolean bc)
	{
		sendBroadcast = bc;
	}
	public boolean getBroadcast()
	{
		return sendBroadcast;
	}
}