package oncotcap.display.genericoutput;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import oncotcap.display.OutputDevice;
import oncotcap.display.browser.OncBrowser;
import oncotcap.process.CollectionListener;
import oncotcap.process.OncCollection;
import oncotcap.process.OncProcess;
import oncotcap.util.OncoTcapIcons;

public class OutputFrame extends JFrame implements CollectionListener, OutputDevice
{
	private ArrayList <ProcessPane> processes = new ArrayList<ProcessPane>();
	private Hashtable <OncProcess, ProcessPane> processTable = new Hashtable<OncProcess, ProcessPane>();
	private Container contentPane;
	private ProcessPane currentProcessPane;
	private JLabel maxProcessesLabel = new JLabel();
	private JLabel processTypeLabel = new JLabel();
	private JSpinner spinner = new JSpinner();
	private JPanel spinnerPanel = new JPanel();
	private Class processType = null;
	private ProcessPane shownPane = null;
	private JLabel idLabel = new JLabel();
	
	public static void main(String [] args)
	{
		//oncotcap.simtest.Patient pat = new oncotcap.simtest.Patient(new TopOncParent());
		//OutputFrame frame = new OutputFrame(pat);
		//frame.setVisible(true);
		//oncotcap.Oncotcap.handleCommandLine(args);
		//MasterScheduler.execute();
	}
	public OutputFrame()
	{
		init();
	}
	public OutputFrame(OncCollection<? extends OncProcess> collection)
	{
		init();
		setCollection(collection);
	}
	public OutputFrame(OncProcess process)
	{
		init();
		setProcess(process);
	}
	public void setCollection(OncCollection<? extends OncProcess> coll)
	{
		clear();
		processType = coll.getType();
		for(OncProcess proc : coll.getProcesses())
			addDisplayObject(proc);
		coll.addCollectionListener(this);
	}
	public void setProcess(OncProcess process)
	{
		clear();
		addDisplayObject(process);
	}
	private void clear()
	{
		if(processTable.size() > 0)
		{
			destroyChildrenWindows();
			for(OncProcess p : processTable.keySet())
			{
				//only need to stop the simulation from one process
				//because all processes will be running in the same MasterScheduler
				p.stopSimulation();
				break;
			}
			processTable.clear();
			processes.clear();
		}
	}
	public void destroyChildrenWindows()
	{
		for(ProcessPane pane : processes)
		{
			pane.destroyChildrenWindows();
		}
	}
	public void setDisplayObject(OncProcess process)
	{
		setProcess(process);
	}
	private void addDisplayObject(OncProcess process)
	{
		ProcessPane pane;
		if(! processTable.containsKey(process) )
		{
			if(processType == null)
				processType = process.getClass();
			
			processTable.put(process, pane = new ProcessPane(process, this));
			
			processes.add(pane);
			refreshSpinner();
			displayProcess();
		}
	}
	
	private void refreshSpinner()
	{
		if(processType != null && (processTypeLabel.getText() == null || processTypeLabel.getText().trim().equals("")))
		{
			String typeString = "    ";
			typeString = "  " + processType.getSimpleName() + "  ";
			processTypeLabel.setText(typeString);
			this.setTitle("Generic Output: " + processType.getSimpleName());
		}

		maxProcessesLabel.setText(" of " + (new Integer(processes.size()).toString()) + "  ");
		((SpinnerNumberModel) spinner.getModel()).setMaximum(processes.size());
		if(processes.size() >= 1)
			displayProcess(((Integer)spinner.getModel().getValue()).intValue());
	}
	private void displayProcess(int procNum)
	{
		ProcessPane pane = processes.get(procNum -1);
		if(pane != currentProcessPane)
		{
			if(currentProcessPane != null)
				contentPane.remove(currentProcessPane);

			currentProcessPane = pane;
			contentPane.add(currentProcessPane, BorderLayout.CENTER);
			if(pane.getProcess() != null)
			{
				String idText = pane.getProcess().getIDLevels();
				if(idText != null && !idText.trim().equals(""))
					idLabel.setText("(" + idText + ")");
				else
					idLabel.setText("");
						
			}
			else
				idLabel.setText("");
			//this.pack();
			pane.revalidate();
			pane.repaint();
		}
	}
	private void init()
	{
		contentPane = getContentPane();
		setTitle("Generic Output");
		setIconImage(OncoTcapIcons.getDefault().getImage());
				
		setSize(800, 600);
		contentPane.setLayout(new BorderLayout());

		processTypeLabel.setFont(new Font("Trebuchet", Font.BOLD, 18));
		maxProcessesLabel.setFont(new Font("Trebuchet", Font.BOLD, 18));
		spinner.setFont(new Font("Trebuchet", Font.BOLD, 18));

		spinner.setModel(new SpinnerNumberModel(1,1,1,1));
		spinner.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e)
			{
				displayProcess();
			}
		}
		);
		spinner.setMaximumSize(new Dimension(150, 30));
		maxProcessesLabel.setHorizontalAlignment(JLabel.LEFT);
		spinnerPanel.setLayout(new BoxLayout(spinnerPanel, BoxLayout.X_AXIS));
		spinnerPanel.add(Box.createHorizontalStrut(20));
		spinnerPanel.add(processTypeLabel);
		spinnerPanel.add(spinner);
		spinnerPanel.add(maxProcessesLabel);
		spinnerPanel.add(Box.createHorizontalStrut(2));
		spinnerPanel.add(idLabel);
		spinnerPanel.add(Box.createHorizontalGlue());
		contentPane.add(spinnerPanel, BorderLayout.NORTH);
		this.addWindowFocusListener(new WindowAdapter() {
						public void WindowClosed(WindowEvent e) {
								dispose();
						}
				} );
		OncBrowser.enableHelp(this);
	}
	private void displayProcess()
	{
		int paneNum = ((Integer) spinner.getValue()).intValue();
		if(paneNum >0 && paneNum <= processes.size())
		{
			this.displayProcess(paneNum);
		}
	}
	public void collectionChanged(OncCollection<? extends OncProcess> collection)
	{
		for(OncProcess proc : collection.getProcesses())
			addDisplayObject(proc);
	}

}
