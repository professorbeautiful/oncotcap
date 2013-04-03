package oncotcap.display.modelcontroller.cellkineticsdemo;

import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import oncotcap.util.*;
import oncotcap.display.*;
import oncotcap.process.cells.*;
import oncotcap.process.treatment.*;
import oncotcap.sim.*;
import oncotcap.sim.schedule.*;
import javax.swing.*;

public class CellGraphPanel extends GraphComp0 implements ComponentListener
{
	private AbstractPatient patient;
	private Scheduler cellGraphSched = null;
	protected LegendPanel legend;
	
	public CellGraphPanel()
	{
		init();
	}
	public CellGraphPanel(LegendPanel legend)
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
		setName("cellcounts");
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
		//addComponentListener(legend) ;
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
		if(e.getSource() instanceof CellGraphPanel) {
			try {
				int x = e.getX();
				int y = e.getY();
				DataLine closestLine = whichLineIsClosest(x, y);
				String lineName = closestLine.getName();
				LegendPanel legend = ((CellGraphPanel)e.getSource()).legend;
				Integer legendRow = (Integer)(legend.strings.get(lineName));
				legend.table.getSelectionModel().setLeadSelectionIndex(legendRow.intValue());
				legend.table.getSelectionModel().setAnchorSelectionIndex(legendRow.intValue());
				// Adding the previous line solves the "line doesn't show up until the table is clicked" bug.(april 2006 - rd)
				legend.table.scrollRectToVisible(legend.table.getCellRect(legendRow, 0, true));
				// Adding the previous line solves the "selected table row is off screen" bug.(april 2006 - rd)
			}
			catch (NullPointerException npe){}
		}
	}
	public void setPatient(AbstractPatient patient)
	{
		if(cellGraphSched != null)
			cellGraphSched.endRecurrentEvent();
		clear();
		this.patient = patient;
		cellGraphSched = new Scheduler(this, "updateCellGraph", patient.getMasterScheduler());
		cellGraphSched.addRecurrentEvent(MasterScheduler.NOW, 0.2);
		patient.getEventManager().registerEvent(this, "Patient_dies");
	}

	public void Patient_dies(Object caller, EventParameters eventParameters)
	{
		System.out.println("Patient dies");
		patient.stopSimulation();
	}
	
	boolean columnsAreSet = false;
	double currentPointX;
	double currentPointY;
	public void updateCellGraph()
	{
		if(patient != null)
		{
			Vector cellColls = patient.getCollections(AbstractCell.class);
			Iterator it = cellColls.iterator();
			while(it.hasNext())
			{
				oncotcap.process.OncProcess first = null;
				CellCollection cellColl = (CellCollection) it.next();
				Iterator it2 = cellColl.iterator();
				while(it2.hasNext())
				{
					AbstractCell cell = (AbstractCell) it2.next();
					if(!columnsAreSet) {
						legend.setColumnIdentifiers(cell.getAllEnums());
						columnsAreSet = true;
					}
					if (first == null) {
						first = cell;
					}
					String lineName = cell.toString(); 
						//cellColl.getName() + ":" + cell.getAllLevels();
					//System.out.println(lineName + "\n" + cell);
					currentPointX = patient.getMasterScheduler().globalTime;
					currentPointY = cell.getCellCount();
					addData(currentPointX, currentPointY, lineName);
				}
			}
		}
		//TODO: add a user changable MAX_X_Axis value to graph
		//use here instead of hard coding
		if(patient.getMasterScheduler().globalTime > 80.0)
			patient.stopSimulation();
	}
}