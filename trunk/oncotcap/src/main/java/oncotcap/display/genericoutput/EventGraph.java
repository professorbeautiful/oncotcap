package oncotcap.display.genericoutput;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import oncotcap.display.GraphComp0;
import oncotcap.engine.EventHandler;
import oncotcap.process.OncCollection;
import oncotcap.process.OncProcess;
import oncotcap.process.treatment.AbstractPatient;
import oncotcap.sim.ArgumentNotFoundException;
import oncotcap.sim.EventManager;
import oncotcap.sim.EventParameters;
import oncotcap.sim.schedule.MasterScheduler;
import oncotcap.sim.schedule.Scheduler;
import oncotcap.util.Util;

public class EventGraph extends GraphComp0 implements EventHandler
{
	private OncProcess process;
	private Scheduler treatmentUpdater = null;
	
	public EventGraph ()
	{
		init();
	}
	private void init()
	{
		setVisible(true);
		setBackground(Color.black);
		setName("treatments");
		setAxisBounds("x", 0.0, 80.0, 10.0);
		setAxisBounds("y", 1, 5, 1);
		setAxisName("x", "Months");
		setAxisName("y", "Treatments");
		dontDrawYAxis();
		this.setMinimumSize(new Dimension(100,100));
	}
	public void setProcess(OncProcess process)
	{
		if(treatmentUpdater != null)
			treatmentUpdater.endRecurrentEvent();
		clear();
		this.process = process;
		treatmentUpdater = new Scheduler(this, "updateTreatmentPanel", process.getMasterScheduler());
		treatmentUpdater.addRecurrentEvent(MasterScheduler.NOW, 0.2);
		process.getEventManager().registerForAllEvents(this);
		//EventManager.registerEvent(this, "treatmentReceived");
		//EventManager.registerEvent(this, "Begin_a_treatment_course");
		//EventManager.registerEvent(this, "continuousTreatmentBegun");
		//EventManager.registerEvent(this, "continuousTreatmentEnded");
	}
	public void handleEvent(String eventName, Object caller, EventParameters eventParameters)
	{
		if( ( !eventName.endsWith(".update") && !eventName.endsWith(".collection_update") ) 
				&&((caller instanceof OncProcess && process.inProcessTree((OncProcess) caller)) ||
			        caller instanceof OncCollection && process.inProcessTree((OncCollection) caller)))
		{
			if(eventName.equals("treatmentReceived"))
				treatmentReceived(caller, eventParameters);
			else if(eventName.equals("Begin_a_treatment_course"))
				Begin_a_treatment_course(caller, eventParameters);
			else if(eventName.equals("continuousTreatmentBegun"))
				continuousTreatmentBegun(caller, eventParameters);
			else if(eventName.equals("continuousTreatmentEnded"))
				continuousTreatmentEnded(caller, eventParameters);
			else
			{
				//addEvent(eventName, caller, eventParameters);
			}
		}
	}
	Vector<String> continuousTreatments = new Vector<String>();
	Vector<Boolean> continuousTreatmentToggles = new Vector<Boolean>();
	Vector<String> events = new Vector<String>();
	
	String getDrugName(EventParameters eventParameters){
		String name;
		if (null != (name=paramNameIsFound("drug", eventParameters))) return(name);
		//System.out.println("Trying paramName \"Drug\"");
		if (null != (name=paramNameIsFound("Drug", eventParameters))) return(name);
		//System.out.println("Trying paramName \"treatment\"");
		if (null != (name=paramNameIsFound("treatment", eventParameters))) return(name);
		//System.out.println("Trying paramName \"Treatment\"");
		if (null != (name=paramNameIsFound("Treatment", eventParameters))) return(name);
		return null;
	}
	String paramNameIsFound(String s, EventParameters eventParameters){
		try {
			String drugName = eventParameters.stringValue(s);
			return drugName;
		} catch(ArgumentNotFoundException e) {
			//System.out.print("treatmentReceived: " + e);
			Enumeration k = eventParameters.keys(); 
			//if( k.hasMoreElements()){
				//System.out.print(" Parameters available are: "
				//		+ k.nextElement());
				//while( k.hasMoreElements()){
				//	System.out.print(", " + k.nextElement());
				//}
				//System.out.println();
			//} else
			//	System.out.println(" No parameters found.");
			return null;
		}
	}
	
	public void continuousTreatmentBegun(Object caller, EventParameters eventParameters)
	{
			String drugName = getDrugName(eventParameters);
			if (continuousTreatments.indexOf(drugName) < 0) {
				continuousTreatments.add(drugName);
			}
			if (addYAxisLabel(drugName)){
//				yAxisLabels.put(drugName, new Double((yAxisLabels.size()+1)*event_y_del));
				continuousTreatmentToggles.add(Boolean.TRUE);
			}
			int continuousTreatmentIndex = continuousTreatments.indexOf(drugName);
			continuousTreatmentToggles.set(continuousTreatmentIndex, Boolean.TRUE);
			//addLine(drugName);
			double currentPointX = process.getLocalTime();
			//addData(currentPointX,  ((Double)treatmentYPosition.get(drugName)).doubleValue(), drugName);
			addString(process.getLocalTime() , getYAxisLabelPosition(drugName), "|", drugName);
			int strLength = getFontMetrics(getFont()).stringWidth(drugName + " ");
			double xScaleFactor = (getWidth() - xLeftOff - xRightOff)/(maxXVal - minXVal);
			// Write y axis label for continuous treatment
		//	addString(-(strLength/xScaleFactor),  
		//			((Double)yAxisLabels.get(drugName)).doubleValue(), drugName + " ", drugName);
		//	System.out.println("Administering continuous drug " 
		//				 + drugName + " at time " 
		//				 + process.getLocalTime());
		//	System.out.println("xScaleFactor = " + xScaleFactor + "   strLength = " + strLength);
	}
	public void continuousTreatmentsContinuing() {
		// called from updateCellGraph()
		for(int treatmentIndex = 0; treatmentIndex<continuousTreatments.size(); treatmentIndex++ ) {
			String drugName = (String)continuousTreatments.elementAt(treatmentIndex);
			if(continuousTreatmentToggles.elementAt(treatmentIndex)==Boolean.TRUE)
				addString(process.getLocalTime() ,  getYAxisLabelPosition(drugName),  "|", continuousTreatments.elementAt(treatmentIndex));
		}
		
	}
	public void continuousTreatmentEnded(Object caller, EventParameters eventParameters)
	{
			//System.out.println("continuousTreatmentEnded: " + " at time " +
			//		 process.getLocalTime());
			String drugName = getDrugName(eventParameters);
			int treatmentIndex = continuousTreatments.indexOf(drugName);
			if(treatmentIndex >= 0)
			{
				continuousTreatmentToggles.set(treatmentIndex, Boolean.FALSE);
				double currentPointX = process.getLocalTime();
				//addData(currentPointX,  ((Double)treatmentYPosition.get(drugName)).doubleValue(), drugName);
				addString(process.getLocalTime() , getYAxisLabelPosition(drugName), "|", drugName);
				//System.out.println("End of continuous drug " + drugName + " at time " +
				//	 process.getLocalTime());
			}
	}
		
	public void treatmentReceived(Object caller, EventParameters eventParameters)
	{
			String drugName = getDrugName(eventParameters);
			if (events.indexOf(drugName) < 0) {
				events.add(drugName);

			}
			addYAxisLabel(drugName);
//			if ( !yAxisLabels.containsKey(drugName) ) 
//				yAxisLabels.put(drugName, 
//							 new Double((yAxisLabels.size()+1)*event_y_del));
			//treatment_y -= 2e12;
			//String drugString = drugName.substring( 0, 1);
			String drugString = "|";
			addString(process.getLocalTime(),  getYAxisLabelPosition(drugName), drugString, drugName);
			//System.out.println("Administering drug " + drugName + " at time " +
			//		 process.getLocalTime());
	}
	public void Begin_a_treatment_course(Object caller, EventParameters eventParameters)
	{
			String drugName = getDrugName(eventParameters);
			addYAxisLabel(drugName);
	}
	public void updateTreatmentPanel()
	{
		if(process != null)
		{
			continuousTreatmentsContinuing();
		}
		if(process.getLocalTime() > 80.0)
			process.stopSimulation();
	}
}
