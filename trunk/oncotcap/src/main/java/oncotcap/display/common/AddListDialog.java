package oncotcap.display.common;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import oncotcap.util.*;

public class AddListDialog extends JDialog {

	protected JList classList;
	protected JList instanceList;
	protected JScrollPane classListScrollPane;
	protected JScrollPane instanceListScrollPane;
	protected JFrame parentFrame;
	protected JOptionPane optionPane;
	private String title;
	private Object returnVal;
	private Dimension size = null;
	final String btnString1 = "Done";
	final String btnString2 = "Cancel";
	Object[] options = {btnString1, btnString2};

	public static final String CREATE = "CREATE!";

	MyListCellRenderer cellRenderer = new MyListCellRenderer();
	
	class MyListCellRenderer extends JLabel implements ListCellRenderer { 
		public Component getListCellRendererComponent(
			JList list,
			Object value,            // value to display
			int index,               // cell index
			boolean isSelected,      // is the cell selected
			boolean cellHasFocus)    // the list and the cell have the focus
		{
							 //(JLabel) super.getListCellRendererComponent(list, value,     
				 //index, isSelected, cellHasFocus);
			setText(value.toString());
			if ( value.equals(CREATE)) {
				this.setFont(this.getFont().deriveFont(Font.ITALIC+Font.BOLD));
				setForeground(Color.BLUE);
			}
			else {
				this.setFont(this.getFont().deriveFont(0));
				setForeground(Color.BLACK);
			}
			setOpaque(true);
			if (isSelected)
				setBackground(oncotcap.util.TcapColor.yellow);
			else
				setBackground(oncotcap.util.TcapColor.white);
			if ( value != null  && value.toString() != null) {
					String strVal = removeTags(value.toString()).trim();
					strVal = strVal.substring(0,Math.min(20,strVal.length()));
			}
			return this;
		}
	}

	String removeTags(String s) {
		int lb;
		if ( s != null ) {
				while ((lb = s.indexOf("<")) >= 0)
						s = s.substring(0,lb) + s.substring(s.indexOf(">")+1);
		}
		return(s);
	}
	private static Vector ArrayToVector(Object [] list) {
		Vector v = new Vector();
		for (int i=0; i<list.length; i++)
			v.add(list[i]);
		return (v);
	}		
	
	public AddListDialog(JFrame frame, String title, Object [] list){
		this(frame, title, list, false);
	}
	public AddListDialog(JFrame frame, String title, Vector list){
		this(frame, title, list, null, false);
	}

	public AddListDialog(JFrame frame, String title, Object [] list, boolean shouldCreate){
		this(frame, title, ArrayToVector(list), null, shouldCreate);
	}

	public AddListDialog(JFrame frame, String title, Vector v, boolean shouldCreate){
			this(frame, title, v, shouldCreate, null);
	}
		public AddListDialog(JFrame frame, String title, Vector v, Vector v2,
												 boolean shouldCreate){
				this(frame, title, v, v2, shouldCreate, null);
		}

		public AddListDialog(JFrame frame, String title, Vector v, 
												 Vector v2, boolean shouldCreate, Dimension tSize){
				super(frame, title, true);
				if (tSize == null)
						size = new Dimension(300,300);
				else
						size = tSize;
				setSize(size);
				if (shouldCreate)
						v.add(0, CREATE);
				classList = new JList(v);
				instanceList = new JList(v2);
				this.title = title;
				parentFrame = frame;
				init();
				setVisible(true);
		}
		
		public AddListDialog(JFrame frame, String title, Vector v, 
												 boolean shouldCreate, Dimension tSize){
				super(frame, title, true);
				if (tSize == null)
						size = new Dimension(300,300);
				else
						size = tSize;
				setSize(size);
				if (shouldCreate)
						v.add(0, CREATE);
				classList = new JList(v);
				instanceList = null;
				this.title = title;
				parentFrame = frame;
				init();
				setVisible(true);
		}

	public static void main(String [] args)
	{
			oncotcap.Oncotcap.handleCommandLine(args);
			oncotcap.datalayer.OncoTCapDataSource dataSource = 
					oncotcap.Oncotcap.getDataSource();
			Class stClass = 
					ReflectionHelper.classForName("oncotcap.datalayer.persistible.StatementTemplate");
			Collection statementTemplates = dataSource.find(stClass);

//		SearchDialog sd = new SearchDialog(null, {"TEST");
		String [] list = {"a","b"} ;
		//JFrame f = new JFrame("my frame...............");
	}
	private void init()
	{
		classListScrollPane = new JScrollPane(classList);
		instanceListScrollPane = new JScrollPane(instanceList);
		MouseListener mouseListener = new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
						// determine what was just selected and deselect everything else
						// in other list
						// See if an instance was selected first
						int index = instanceList.locationToIndex(e.getPoint());
						if(index >= 0 && index < instanceList.getModel().getSize())
								selectAndReturn(instanceList.getModel().getElementAt(index));
						
						// then see if the user wants a new item
						index = classList.locationToIndex(e.getPoint());
						if(index >= 0 && index < classList.getModel().getSize())
								selectAndReturn(classList.getModel().getElementAt(index));
				}
				else {
						if (e.getSource() == classList ) 
								instanceList.clearSelection();
						else
								classList.clearSelection();
				}
			}
		};
		classList.addMouseListener(mouseListener);
		classList.setCellRenderer(cellRenderer);
		if ( instanceList != null ) {
				instanceList.setCellRenderer(cellRenderer);
				instanceList.addMouseListener(mouseListener);
		}
		Object[] promptArray = (Object[]) Util.newArray("java.lang.Object", 3);
		promptArray[0] = new String(title);
		promptArray[1] = classListScrollPane;
		if ( instanceList != null )
				promptArray[2] = instanceListScrollPane;
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		optionPane = new JOptionPane(promptArray, 
											  JOptionPane.QUESTION_MESSAGE,
											  JOptionPane.YES_NO_OPTION,
											  null,
											  options,
											  options[0]);
		setContentPane(optionPane);
		optionPane.addPropertyChangeListener(new InputPropertyChangeListener());
		pack();
		if(size != null)
			setSize(size);
	}
	private void selectAndReturn(Object obj)
	{
		if(obj != null)
		{
			returnVal = obj;
			setVisible(false);
		}
	}
	class InputPropertyChangeListener implements PropertyChangeListener {
		/**
		 ** Property Change event handler.
		 **/
		public void propertyChange(PropertyChangeEvent e)
		{
			Object value = optionPane.getValue();
			if (value == JOptionPane.UNINITIALIZED_VALUE)
			{
				return;
			}
			if (value.equals("Done"))
			{
					// First see if an instnace was selected
					if ( instanceList.getSelectedValue() != null)
							selectAndReturn(instanceList.getSelectedValue());
					else 
							selectAndReturn(classList.getSelectedValue());
			}
			else if (value.equals("Cancel"))
			{
				returnVal = null;
				setVisible(false);
			}
		}
	}

	public static InputDialog show(JFrame frame, String title)
	{
		return(new InputDialog(frame, title));
	}
	public static Object getValue(Object [] list)
	{
		return(getValue("Choose a value.", ArrayToVector(list)));
	}
	public static Object getValue(String title, Object [] list)
	{
		AddListDialog id = new AddListDialog(SwingUtil.getModeFrame(), title, list);
		return(id.getInput());
	}
	public static Object getValue(Vector list)
	{
		return(getValue("Enter value.", list));
	}
	public static Object getValue(String title, Vector list)
	{
		return(getValue(title, list, false));
	}
	public static Object getValue(String title, Vector list, boolean shouldCreate)
	{
		AddListDialog id = new AddListDialog(SwingUtil.getModeFrame(), title, list, shouldCreate);
		return(id.getInput());
	}
	public static Object getValue(String title, Vector list, Vector list2, boolean shouldCreate)
	{
		AddListDialog id = 
				new AddListDialog(SwingUtil.getModeFrame(), title, list, list2, shouldCreate);
		return(id.getInput());
	}
	public static Object getValue(String title, Collection list)
	{
		AddListDialog id = new AddListDialog(SwingUtil.getModeFrame(), title, new Vector(list));
		return(id.getInput());
	}

	public Object getInput()
	{
		return(returnVal);
	}
}
