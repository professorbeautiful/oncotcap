package oncotcap.display.modelcontroller.cellkineticsdemo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.*;
import javax.swing.plaf.metal.MetalBorders.TableHeaderBorder;
import javax.swing.table.*;

import oncotcap.process.treatment.AbstractPatient;
import oncotcap.sim.ArgumentNotFoundException;
import oncotcap.sim.EventManager;
import oncotcap.sim.EventParameters;
import oncotcap.sim.schedule.MasterScheduler;

import java.util.Vector;

public class EventPanel extends JScrollPane {

	private MasterScheduler masterScheduler;
	private EventManager eventManager;
	
	static EventPanel currentEventPanel;
	JTable table = new JTable();
	//JPanel panel = new JPanel();
	Vector<String>times = new Vector<String>();
	Vector<String>events = new Vector<String>();
	

	DefaultTableModel tableModel = new DefaultTableModel() {
	    public Object getValueAt(int row, int col) {
	    	//return super.getValueAt(row,col);
	    	if(times.size() > row) { 
		    	if(col==0 ) 
		    		return times.elementAt(row);
		    	else return events.elementAt(row);
	    	}
	    	return null;
	    }
	    public boolean isCellEditable(int row, int col)
	        { return false; }
	    public void setValueAt(Object value, int row, int col) {
	    	if(col==0) times.addElement((String)value);
	    	else events.addElement((String)value);
	    	// No provision here for changing entries.
	        fireTableCellUpdated(row, col);
	    }
	};
	
	public static EventPanel getCurrentEventPanel(){
		return currentEventPanel;
	}

	public EventPanel()
	{
		currentEventPanel = this;
		//setSize(getParent().getWidth(), getParent().getWidth());
		//setSize(600, 70);
		//add(panel);
		//setLayout(new BorderLayout());
		//JLabel label = (new JLabel("   Time    Event"));
		//add(label);
		//label.setSize(200,30);
		//add(table.getTableHeader(), BorderLayout.NORTH);
		//setBorder(BorderFactory .createLineBorder(Color.GREEN, 3));
		//table.setAutoscrolls(true);
/*		JFrame jf = new JFrame();
		jf.setSize(800,600);
		jf.getContentPane().add(panel);
		jf.setVisible(true);
*/		table.setModel(tableModel);
		table.getTableHeader().setFont(
			       table.getTableHeader().getFont()
			       .deriveFont(java.awt.Font.BOLD));
		table.setRowHeight(30);
		setViewportView(table);
		//setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//table.setSize(getWidth()-1,getHeight()-1);
		//table.setSize(750, 100);
		//table.setLocation(10,31);
		table.setDefaultRenderer(Object.class, new EventTableCellRenderer());
		tableModel.setColumnIdentifiers(new String[]{"Time","Event"});
		table.getColumnModel().getColumn(0).setPreferredWidth(70);
		table.getColumnModel().getColumn(0).setWidth(70);
		table.getColumnModel().getColumn(0).setMaxWidth(70);
//		setVisible(false);
/*		table.getTableHeader().setVisible(true);
		table.setAutoscrolls(true);
*/		 //TODO:  make this work.   
	}
	public void setPatient(AbstractPatient patient)
	{
		masterScheduler = patient.getMasterScheduler();
		if(eventManager != null)
			eventManager.removeEvent(this, "reportAnEvent");
		eventManager = patient.getEventManager();
		eventManager.registerEvent(this, "reportAnEvent");
	}
	public void reportAnEvent(Object caller, EventParameters eventParameters)
	{
		try {
			String messageVariableName = eventParameters.stringValue("messageVariable");
			// now do reflection.
			// TODO   reflection to obtain the value, then add to the  
		}
		catch(ArgumentNotFoundException e) {
				System.out.println("treatmentReceived: " + e);
		}
	}
	public void addEvent(String s){
		setVisible(true);
		tableModel.setNumRows(tableModel.getRowCount()+1);
		tableModel.setValueAt(Double.toString(masterScheduler.globalTime), tableModel.getRowCount(), 0);
		tableModel.setValueAt(s, tableModel.getRowCount(), 1);
		oncotcap.util.Logger.log("event: " +s);
	}
	class EventTableCellRenderer extends JLabel implements TableCellRenderer {
		public java.awt.Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) 
		{
			final javax.swing.border.LineBorder selectedBorder = new javax.swing.border.LineBorder(Color.red, 2);
			final javax.swing.border.LineBorder unselectedBorder = new javax.swing.border.LineBorder(Color.black, 0);
			setBackground(Color.red);
			if(value != null)
				setText("  " + value.toString());
			if(column==0) setAlignmentX(JLabel.RIGHT_ALIGNMENT);
			//oncotcap.util.Logger.log("EventTableCellRenderer: " + value);
			return(this);
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
