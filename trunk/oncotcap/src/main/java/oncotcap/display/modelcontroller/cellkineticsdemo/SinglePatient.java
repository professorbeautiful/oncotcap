package oncotcap.display.modelcontroller.cellkineticsdemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import oncotcap.display.*;
import oncotcap.display.browser.OncBrowser;
import oncotcap.process.*;
import oncotcap.process.treatment.*;

import oncotcap.util.*;

public class SinglePatient extends JFrame implements OutputDevice
{
	private AbstractPatient patient;
	private CellGraphPanel cellGraphPanel;
	private TreatmentPanel treatmentPanel;
	private EventPanel eventPanel;
	private String modelName = "";
	public static void main(String [] args)
	{
//		oncotcap.simtest.patient pat = new oncotcap.simtest.patient(new TopOncParent());
//		SinglePatient sp = new SinglePatient(pat);
//		sp.setSize(800, 600);
//		sp.setVisible(true);
//		oncotcap.Oncotcap.handleCommandLine(args);
//		MasterScheduler.execute();
	}
	public SinglePatient()
	{
		init();
	}
	public SinglePatient(OncProcess patient)
	{
		init();
		if(patient instanceof AbstractPatient)
			setPatient((AbstractPatient) patient);
		else
			Logger.log("WARNING: SinglePatient instantiated with " + patient.getClass() + "\n" + "Patient required.");
//		init();
	}
	
	private void init()
	{
		int eventPanelHeight = 110;
		int treatmentPanelHeight = 200;
		Container cp = getContentPane();
		setTitle("Single Patient Output "); // 
		setIconImage(OncoTcapIcons.getImage("OutputWindow.jpg"));
		
		
		setSize(800, 600);
		cellGraphPanel = new CellGraphPanel();
//		cellGraphPanel.setPatient(patient);
		LegendDialog legendDialog = new LegendDialog(this);
		LegendPanel legendPanel = new LegendPanel(legendDialog, cellGraphPanel);
		legendDialog.showLegend(legendPanel);
		cellGraphPanel.setLegend(legendPanel);
		
		treatmentPanel = new TreatmentPanel();
		treatmentPanel.dontDrawYAxis();

		//fixLayout();
		treatmentPanel.setPreferredSize(new Dimension(getWidth(), Math.round((float)(getHeight()*0.3))));
		Logger.log("preferred size = " + treatmentPanel.getPreferredSize());
		
		eventPanel = new EventPanel();
		eventPanel.setMinimumSize(new Dimension(0, 100));
		eventPanel.setPreferredSize(new Dimension(200, 100));
		JSplitPane jsp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, cellGraphPanel, treatmentPanel);
		JSplitPane jsp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, eventPanel, jsp2);
		cp.setLayout(new BorderLayout());
		jsp1.setDividerLocation(eventPanelHeight);
		jsp2.setDividerLocation( 600 - eventPanelHeight - treatmentPanelHeight);
		cp.add(jsp1, BorderLayout.CENTER);
		cp.validate();

		//TODO:   manage the height of the treatment panel to reflect the number of treatments.
		if(legendPanel != null)
		{
				legendDialog.setVisible(true);
				legendDialog.setSize();
				legendDialog.setLocation();
		}
//		setPatient(patient);
		addWindowListener(new WindowAdapter(){

 						public void windowClosing(WindowEvent e)
 						{
							if(patient != null)
								patient.stopSimulation();
							dispose(); 							
 						}
				});
		OncBrowser.enableHelp(this);
	}
/*	void fixLayout() {
		treatmentPanel.setLocation(0,Math.round(getHeight()*0.80f));
		treatmentPanel.setSize(new Dimension(getWidth(),Math.round(getHeight()*0.18f)));
		treatmentPanel.setPreferredSize(treatmentPanel.getSize());
		cellGraphPanel.setLocation(0, 0);
		cellGraphPanel.setSize(new Dimension(getWidth(),treatmentPanel.getHeight()-17));
		cellGraphPanel.setPreferredSize(cellGraphPanel.getSize());
	}*/
	public void setDisplayObject(OncProcess obj)
	{
		if(obj instanceof AbstractPatient)
			setPatient((AbstractPatient) obj);
	}
	public void setPatient(AbstractPatient patient)
	{
		this.patient = patient;
		if(cellGraphPanel != null)
			cellGraphPanel.setPatient(patient);
		if(treatmentPanel != null)
			treatmentPanel.setPatient(patient);
		if(eventPanel != null)
			eventPanel.setPatient(patient);
	}
	public CellGraphPanel getCellGraphPanel(){
		return cellGraphPanel;
	}
}
