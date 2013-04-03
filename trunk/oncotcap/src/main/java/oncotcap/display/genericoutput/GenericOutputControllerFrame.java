package oncotcap.display.genericoutput;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import oncotcap.display.OutputDevice;
import oncotcap.engine.ProcessController;
import oncotcap.process.OncProcess;
import oncotcap.sim.schedule.*;

public class GenericOutputControllerFrame extends JDialog implements OutputDevice, MasterSchedulerListener
{
	private OncProcess process;
	private OutputFrame mainOutputFrame;
	private ProcessController controller;
	
	public GenericOutputControllerFrame(OncProcess process, ProcessController controller, OutputFrame mainOutputWindow)
	{
		super(mainOutputWindow);
		mainOutputFrame = mainOutputWindow;
		init();
		setDisplayObject(process);
		setController(controller);
	}
	private void setController(ProcessController controller)
	{
		this.controller = controller;
	}
	public void setDisplayObject(OncProcess process)
	{
		if(process != null)
			process.getMasterScheduler().removeMasterSchedulerListener(this);
		this.process = process;
		mainOutputFrame.setProcess(process);
		MasterScheduler sched = process.getMasterScheduler();
		sched.addMasterSchedulerListener(this);
		if(sched.isPaused())
		{
			btnRunPause.setText("Run");
			btnStop.setEnabled(true);
		}
		else if(sched.isRunning())
		{
			btnRunPause.setText("Pause");
			btnStop.setEnabled(true);
		}
		else
		{
			btnRunPause.setText("Run");
			btnStop.setEnabled(false);
		}
	}
	
	private JButton btnRunPause = new JButton("Run");
	private JButton btnStop = new JButton("Stop");
	private JButton btnDestroy = new JButton("Destroy");
	
	private void init()
	{
		setSize(320, 100);
		setLocation((int) mainOutputFrame.getLocation().getX(), (int) mainOutputFrame.getLocation().getY() + 80);
		Container cp = getContentPane();
		cp.setLayout(null);
		btnRunPause.setSize(100, 30);
		btnStop.setSize(100, 30);
		btnDestroy.setSize(100, 30);
		btnRunPause.setLocation(10,10);
		btnStop.setLocation(110, 10);
		btnDestroy.setLocation(210, 10);
		cp.add(btnRunPause);
		cp.add(btnStop);
		cp.add(btnDestroy);
		btnStop.setEnabled(false);
		btnRunPause.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				if(btnRunPause.getText().equals("Run"))
					controller.resume();
				else
					controller.pause();
			}
		});
		
		btnStop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				controller.stop();
			}
		});
		
		btnDestroy.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				controller.destroy();
			}
		});
	}
	
	private boolean firstPass = true;
	public void dispose()
	{
		if(firstPass)
		{
			firstPass = false;
			mainOutputFrame.destroyChildrenWindows();
			mainOutputFrame.dispose();
		}
		else
			super.dispose();
	}
	public void setVisible(boolean visible)
	{
		mainOutputFrame.setVisible(visible);		
		super.setVisible(visible);
	}
	
	public void schedulerStarted()
	{
		btnRunPause.setText("Pause");
		btnStop.setEnabled(true);
	}
	public void schedulerReset()
	{
		btnRunPause.setText("Run");
		btnStop.setEnabled(false);
	}
	public void schedulerPaused()
	{
		btnRunPause.setText("Run");
	}
}
