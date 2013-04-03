package oncotcap.display.modelcontroller.cellkineticsdemo;


	import java.awt.Color;
import java.lang.reflect.Array;
	import java.util.*;

	import oncotcap.util.*;
	import oncotcap.display.*;
	import oncotcap.process.treatment.*;
	import oncotcap.sim.*;
import oncotcap.sim.schedule.*;

	public class TreatmentPanel extends GraphComp0
	{
		private AbstractPatient patient;
		private Scheduler treatmentUpdater = null;
		private Hashtable treatmentYPosition = new Hashtable();
		
		public TreatmentPanel ()
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
		}
		public void setPatient(AbstractPatient patient)
		{
			if(treatmentUpdater != null)
				treatmentUpdater.endRecurrentEvent();
			clear();
			this.patient = patient;
			treatmentUpdater = new Scheduler(this, "updateTreatmentPanel", patient.getMasterScheduler());
			treatmentUpdater.addRecurrentEvent(MasterScheduler.NOW, 0.2);
			patient.getEventManager().registerEvent(this, "treatmentReceived");
			patient.getEventManager().registerEvent(this, "Begin_a_treatment_course");
			patient.getEventManager().registerEvent(this, "continuousTreatmentBegun");
			patient.getEventManager().registerEvent(this, "continuousTreatmentEnded");
		}

		Vector<String> continuousTreatments = new Vector<String>();
		Vector<Boolean> continuousTreatmentToggles = new Vector<Boolean>();
		Vector<String> treatments = new Vector<String>();
		
		double treatment_y0 = 2;
		double treatment_y_del = 1.25;
		
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
				//Enumeration k = eventParameters.keys(); 
				//if( k.hasMoreElements()){
				//	System.out.print(" Parameters available are: "
				//			+ k.nextElement());
				//	while( k.hasMoreElements()){
				//		System.out.print(", " + k.nextElement());
				//	}
				//	System.out.println();
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
					this.addYAxisLabel(drugName);
				}
				if ( !treatmentYPosition.containsKey(drugName) ) {
					treatmentYPosition.put(drugName, new Double((treatmentYPosition.size()+1)*treatment_y_del));
					continuousTreatmentToggles.add(Boolean.TRUE);
				}
				int continuousTreatmentIndex = continuousTreatments.indexOf(drugName);
				continuousTreatmentToggles.set(continuousTreatmentIndex, Boolean.TRUE);
				//addLine(drugName);
				double currentPointX = patient.getMasterScheduler().globalTime;
				//addData(currentPointX,  ((Double)treatmentYPosition.get(drugName)).doubleValue(), drugName);
				addString(patient.getMasterScheduler().globalTime ,  
									((Double)treatmentYPosition.get(drugName)).doubleValue(),  
									"|", drugName);
				int strLength = getFontMetrics(getFont()).stringWidth(drugName + " ");
				double xScaleFactor = (getWidth() - xLeftOff - xRightOff)/(maxXVal - minXVal);
				// Write y axis label for continuous treatment
				addString(-(strLength/xScaleFactor),  
						((Double)treatmentYPosition.get(drugName)).doubleValue(), drugName + " ", drugName);
//				System.out.println("Administering continuous drug " 
//							 + drugName + " at time " 
//							 + patient.getMasterScheduler().globalTime);
//				System.out.println("xScaleFactor = " + xScaleFactor + "   strLength = " + strLength);
		}
		public void continuousTreatmentsContinuing() {
			// called from updateCellGraph()
			for(int treatmentIndex = 0; treatmentIndex<continuousTreatments.size(); treatmentIndex++ ) {
				String drugName = (String)continuousTreatments.elementAt(treatmentIndex);
				if(continuousTreatmentToggles.elementAt(treatmentIndex)==Boolean.TRUE)
					addString(patient.getMasterScheduler().globalTime ,  ((Double)treatmentYPosition.get(drugName)).doubleValue(),  "|", continuousTreatments.elementAt(treatmentIndex));
			}
			
		}
		public void continuousTreatmentEnded(Object caller, EventParameters eventParameters)
		{
//				System.out.println("continuousTreatmentEnded: " + " at time " +
//						 patient.getMasterScheduler().globalTime);
				String drugName = getDrugName(eventParameters);
				int treatmentIndex = continuousTreatments.indexOf(drugName);
				continuousTreatmentToggles.set(treatmentIndex, Boolean.FALSE);
				double currentPointX = patient.getMasterScheduler().globalTime;
				//addData(currentPointX,  ((Double)treatmentYPosition.get(drugName)).doubleValue(), drugName);
				addString(patient.getMasterScheduler().globalTime ,  
									((Double)treatmentYPosition.get(drugName)).doubleValue(),  
									"|", drugName);
//				System.out.println("End of continuous drug " + drugName + " at time " +
//					 patient.getMasterScheduler().globalTime);
		}
			
		public void treatmentReceived(Object caller, EventParameters eventParameters)
		{
				String drugName = getDrugName(eventParameters);
				if (treatments.indexOf(drugName) < 0) {
					treatments.add(drugName);

				}
				if ( !treatmentYPosition.containsKey(drugName) ) 
						treatmentYPosition.put(drugName, 
								 new Double((treatmentYPosition.size()+1)*treatment_y_del));
				//treatment_y -= 2e12;
				//String drugString = drugName.substring( 0, 1);
				String drugString = "|";
				addString(patient.getMasterScheduler().globalTime,  ((Double)treatmentYPosition.get(drugName)).doubleValue(), drugString, drugName);
//				System.out.println("Administering drug " + drugName + " at time " +
//						 patient.getMasterScheduler().globalTime);
		}
		public void Begin_a_treatment_course(Object caller, EventParameters eventParameters)
		{
				String drugName = getDrugName(eventParameters);
				if ( !treatmentYPosition.containsKey(drugName) ) {
					treatmentYPosition.put(drugName, new Double((treatmentYPosition.size()+1)*treatment_y_del));
					this.addYAxisLabel(drugName);
				}
				//String drugString = drugName.substring( 0, 1);
				String drugString = drugName + " ";
				int strLength = getFontMetrics(getFont()).stringWidth(drugString);
				double xScaleFactor = (getWidth() - xLeftOff - xRightOff)/(maxXVal - minXVal);

				// Write y axis label for non-continuous treatment
				addString(-(strLength/xScaleFactor), ((Double)treatmentYPosition.get(drugName)).doubleValue(), drugString, drugName);
//				System.out.println("Administering drug " + drugName + " at time " +
//						 patient.getMasterScheduler().globalTime);
		}
				
		public void updateTreatmentPanel()
		{
			if(patient != null)
			{
				continuousTreatmentsContinuing();
			}
			if(patient.getMasterScheduler().globalTime > 80.0)
				patient.stopSimulation();
		}
	}
