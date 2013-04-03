package oncotcap.display.genericoutput;

import java.awt.BorderLayout;
import java.awt.List;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.table.*;

import oncotcap.engine.*;
import oncotcap.engine.EventHandler;
import oncotcap.process.*;
import oncotcap.sim.*;
import oncotcap.sim.schedule.*;

/**
 * Displays in a table all events that happen during the course of a simulation.
 * 
 * @author shirey
 *
 */
public class EventTable extends JPanel implements EventHandler
{
	private ArrayList<String> eventIndex = new ArrayList<String>();
	private DefaultTableModel model = new DefaultTableModel();
	private JTable table = new JTable();
	private OncProcess process;
	
	public EventTable()
	{
		init();
	}
	private void init()
	{
		setLayout(new BorderLayout());
		JScrollPane sPane = new JScrollPane(table);
		this.add(sPane, BorderLayout.CENTER);
		model.addColumn("EventName");
		model.addColumn("Occurences");
		table.setModel(model);
	}
	public void setProcess(OncProcess process)
	{
		int i;
		for(i=0; i< model.getRowCount(); i++)
			model.removeRow(i);
		eventIndex.clear();
		if(process != null)
			process.getEventManager().unRegisterForAllEvents(this);
		this.process = process;
		process.getEventManager().registerForAllEvents(this);
	}
	public void handleEvent(String eventName, Object caller, EventParameters params)
	{
		if( ( !eventName.endsWith(".update") && !eventName.endsWith(".collection_update") ) 
				&&((caller instanceof OncProcess && process.inProcessTree((OncProcess) caller)) ||
			        caller instanceof OncCollection && process.inProcessTree((OncCollection) caller)))
		{
			if(! eventIndex.contains(eventName))
			{
				addEvent(eventName);
			}
			int rowNum = eventIndex.indexOf(eventName);
			Integer cnt = (Integer) model.getValueAt(rowNum, 1);
			model.setValueAt(new Integer(cnt.intValue()+1), rowNum, 1);
		}
	}
	public void addEvent(String eventName)
	{
		eventIndex.add(eventName);
		int nRows = model.getRowCount();
		model.setRowCount(nRows + 1);
		model.setValueAt(eventName, nRows, 0);
		model.setValueAt(new Integer(0), nRows, 1);
	}
}
