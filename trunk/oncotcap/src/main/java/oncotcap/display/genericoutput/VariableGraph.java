package oncotcap.display.genericoutput;

import java.awt.event.*;
import java.lang.reflect.Field;
import java.util.*;

import oncotcap.display.*;
import oncotcap.process.OncCollection;
import oncotcap.process.OncProcess;
import oncotcap.sim.EventManager;
import oncotcap.sim.EventParameters;
import oncotcap.sim.schedule.MasterScheduler;
import oncotcap.sim.schedule.MasterSchedulerListener;
import oncotcap.sim.schedule.Scheduler;
import oncotcap.util.Logger;
import oncotcap.util.Util;

public class VariableGraph extends GraphComp0 implements ComponentListener, MasterSchedulerListener
{
	private OncProcess process;
	private Field field;
	private Class collectionType;
	private Scheduler variableGraphSched = null;
	protected LegendPanel legend;
	
	public VariableGraph()
	{
		init();
	}
	public VariableGraph(LegendPanel legend)
	{
		super(legend);
		this.legend = legend;
		//legend.setBounds(600,400,250,150);
		init();
	}
	public void setLegend(LegendPanel legend)
	{
		this.legend = legend;
		super.setLegend(legend);
	}
	private void init()
	{
		setAxisBounds("x", 0.0, 80.0, 10.0);
		setAxisIsLog10("y", true);
		setAxisBounds("y", 1, 1.0e13, 1);
		setAxisName("x", "Months");
		setAxisName("y", "Cell counts");
		addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseMoved(MouseEvent e){
				highlightClosestLine(e);
			}
		});
		addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				highlightClosestLine(e);
			}
		});
		addComponentListener(this) ;
	}
    public void componentResized(ComponentEvent e){
    	if(e.getSource() == this){
    		refreshPixelsForAllLines();
    	}
    }
    public void componentMoved(ComponentEvent e){}
    public void componentShown(ComponentEvent e){}
    public void componentHidden(ComponentEvent e){}
	void highlightClosestLine(MouseEvent e){
		if(e.getSource() instanceof VariableGraph)
		{
			try
			{
				LegendPanel legend = ((VariableGraph)e.getSource()).legend;
				//System.out.println("mouse location " + e.getX() + " " + e.getY());
				/**  Needs a little debugging
				 **  still.
				 **/  
				if(legend != null)
				{
					int x = e.getX();
					int y = e.getY();
					DataLine closestLine = whichLineIsClosest(x, y);
					String lineName = closestLine.getName();
					Integer legendRow = (Integer)(legend.strings.get(lineName));
					legend.table.getSelectionModel().setLeadSelectionIndex(legendRow.intValue());
					legend.table.getSelectionModel().setAnchorSelectionIndex(legendRow.intValue());
					// Adding the previous line solves the "line doesn't show up until the table is clicked" bug.(april 2006 - rd)
					legend.table.scrollRectToVisible(legend.table.getCellRect(legendRow, 0, true));
					// Adding the previous line solves the "selected table row is off screen" bug.(april 2006 - rd)
				}
			}
			catch (NullPointerException npe){}
		}
	}
	public void setVariable(OncProcess process, Class collectionType, Field field)
	{
		setName(collectionType.getSimpleName() + "." + field.getName());
		if(variableGraphSched != null)
			variableGraphSched.endRecurrentEvent();
		clear();
		this.process = process;
		this.field = field;
		this.collectionType = collectionType;
		variableGraphSched = new Scheduler(this, "updateVariableGraph", process.getMasterScheduler());
		variableGraphSched.addRecurrentEvent(MasterScheduler.NOW, 0.2);

		process.getEventManager().registerEvent(this, "Patient_dies");
		
		process.getMasterScheduler().addMasterSchedulerListener(this);
	}

	public void Patient_dies(Object caller, EventParameters eventParameters)
	{
		System.out.println("Patient dies");
		process.stopSimulation();
	}
	
	boolean columnsAreSet = false;
	double currentPointX;
	double currentPointY;
	private static final Object [] noIdentifiers = {"No Identifiers"};
	public void updateVariableGraph()
	{
		if(process != null)
		{
			Vector colls = process.getCollections(collectionType);
			Iterator it = colls.iterator();
			while(it.hasNext())
			{
				oncotcap.process.OncProcess first = null;
				OncCollection coll = (OncCollection) it.next();
				Iterator it2 = coll.iterator();
				while(it2.hasNext())
				{
					OncProcess proc = (OncProcess) it2.next();
					if(!columnsAreSet) {
						if(legend != null)
						{
							if(proc.getAllEnums().length == 0)
								legend.setColumnIdentifiers(noIdentifiers);
							else
								legend.setColumnIdentifiers(proc.getAllEnums());
							columnsAreSet = true;
						}
					}
					if (first == null) {
						first = proc;
					}
					String lineName = proc.toString(); 
						//cellColl.getName() + ":" + cell.getAllLevels();
					//System.out.println(lineName + "\n" + cell);
					currentPointX = process.getLocalTime();
					try{currentPointY = field.getDouble(proc);}
					catch(IllegalAccessException e){Logger.log("ERROR: IllegalAccess Exception while trying to graph variable " + process.getClass().getSimpleName() + "." + field.getName() + ".");}
					
					addData(currentPointX, currentPointY, lineName);
				}
			}
		}
		//TODO: add a user changable MAX_X_Axis value to graph
		//use here instead of hard coding
		if(process.getLocalTime() > 80.0)
			variableGraphSched.endRecurrentEvent();
//			Util.exitSim();
	}
	

	public void schedulerStarted(){}
	public void schedulerReset()
	{
		autoScaleY();
	}
	public void schedulerPaused(){}
}
