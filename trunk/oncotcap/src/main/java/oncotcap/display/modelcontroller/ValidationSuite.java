/**
 * 
 */
package oncotcap.display.modelcontroller;

/**
 * @author day
 *
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import oncotcap.*;
import oncotcap.display.*;
import oncotcap.display.modelcontroller.cellkineticsdemo.CellGraphPanel;
import oncotcap.display.modelcontroller.cellkineticsdemo.TreatmentPanel;
import oncotcap.process.*;
import oncotcap.process.cells.*;
import oncotcap.process.treatment.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.sim.schedule.*;
import oncotcap.util.*;

public class ValidationSuite extends JFrame implements OutputDevice
{
	private AbstractPatient patient;
	private CellGraphPanel cellGraphPanel;
	private TreatmentPanel treatmentPanel ;
	
	public static void main(String [] args)
	{
		ValidationSuite vs = new ValidationSuite ();
		vs.setSize(800, 600);
		vs.setVisible(true);
		String [][] contents = {
				{"","","","",""},
				{"","","","",""},
				{"","","","",""},
				{"","","","",""},
				{"","","","",""},
				{"","","","",""},
				{"","","","",""},
				{"","","","",""},
				{"","","","",""}
		};
		String [] columns = {"Model 1","Model 2","Model 3","Model 4","Model 5"};
		
		contents[1][0]="Setting 1";
		contents[2][0]="Validation 1A";
		contents[3][0]="Validation 1B";
		contents[4][0]="Setting 2";
		contents[5][0]="Validation 2A";
		contents[6][0]="Validation 2B";
		
		contents[2][0]="Setting 1"; 

		JTable jt = new JTable(contents,columns);
		vs.getContentPane().add(jt);
		jt.setVisible(false);
		jt.setBounds(20,20,500,400);
		
//		oncotcap.Oncotcap.handleCommandLine(args);
//		MasterScheduler.execute();
	}
	public ValidationSuite()
	{
		init();
	}

	private void init()
	{
		Container cp = getContentPane();
		setTitle("Validation Suite");
		setIconImage(OncoTcapIcons.getDefault().getImage());
	}
	public void setDisplayObject(OncProcess obj)
	{
	}
}

