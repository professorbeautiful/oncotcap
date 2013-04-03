package oncotcap.display.common;

import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;
import java.awt.*;
import java.lang.reflect.Array;

import java.awt.event.*;
import java.beans.*;
import oncotcap.util.*;
import oncotcap.display.browser.*;

public class GenericOptionPane extends JDialog implements TreePanelListener {

	protected JList classList;
	protected JList instanceList;
	protected JScrollPane scrollPane;
	protected JFrame parentFrame;
	protected JOptionPane optionPane;
	private String title;
	private Object returnVal;
		private JComponent theSelectables = null;
	private Dimension size = null;
	private JButton btnString1 = new JButton("Done");
	private JButton btnString2 = new JButton("Cancel");
	Object[] promptArray  = null;
	Object[] options = {btnString1, btnString2};
		private Object selectedValue = null;
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

		public GenericOptionPane() {
		}
		
		public GenericOptionPane(JFrame frame, String title,
														 JComponent selectionListObject){
				super(frame, title, true);
				OptionPaneMouseListener mouseListener = new OptionPaneMouseListener();
				//if (tSize == null)
				size = new Dimension(600,600);
				//else
				//size = tSize;
				setSize(size);
				this.title = title;
				parentFrame = frame;

				promptArray = (Object[]) Util.newArray("java.lang.Object", 1);
				System.out.println("components == > " + selectionListObject);

				selectionListObject.addMouseListener(mouseListener);
				promptArray[0] = new JScrollPane(selectionListObject);
				init();
				setVisible(true);
		}
		
		public GenericOptionPane(JFrame frame, String title,
														 JComponent [] jComponents){
				super(frame, title, true);
				OptionPaneMouseListener mouseListener = new OptionPaneMouseListener();
				promptArray = (Object[]) Util.newArray("java.lang.Object", 
																							 Array.getLength(jComponents));
				for ( int i = 0; i < Array.getLength(jComponents); i++) {
						if ( jComponents[i] instanceof JScrollPane ) {
								Component child = 
										((JComponent)jComponents[i].getComponent(0)).getComponent(0);	
								child.addMouseListener(mouseListener);
						}
						else 
								jComponents[i].addMouseListener(mouseListener);
						promptArray[i] = jComponents[i];
				}
				//if (tSize == null)
				size = new Dimension(600,600);
				//else
				//size = tSize;
				setSize(size);
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
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		optionPane = new JOptionPane(promptArray, 
											  JOptionPane.QUESTION_MESSAGE,
											  JOptionPane.YES_NO_OPTION,
											  null,
											  options,
											  options[0]);
		setContentPane(optionPane);
		ButtonActionListener actionListener = new ButtonActionListener();
		btnString1.addActionListener(actionListener);
		btnString2.addActionListener(actionListener);
		pack();
		if(size != null)
			setSize(size);
	}
	private void selectAndReturn(Object obj)
	{
		if(obj != null) {
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
						// 	System.out.println("PropertyChangeEvent " + e.getSource());
						// 				System.out.println("message " + ((JOptionPane)e.getSource()).getMessage().getClass());
						Object o = optionPane.getValue();
						Object value = JOptionPane.UNINITIALIZED_VALUE;
						if ( o != null && o instanceof JButton)
								value = ((JButton)optionPane.getValue()).getText();
					
						if (value == JOptionPane.UNINITIALIZED_VALUE)
								{
										return;
								}
						if (value.equals("Done") || value.equals("New")){
								// Make sure you have the current selected value 
								selectAndReturn(selectedValue);
						}
						else if (value.equals("Cancel")) {
								returnVal = null;
								setVisible(false);
						}
				}
		}
		
		public static InputDialog show(JFrame frame, String title)
		{
				return(new InputDialog(frame, title));
		}
		public static Object getValue(String title, JComponent selectionListObject)
		{
				GenericOptionPane id = 
						new GenericOptionPane(SwingUtil.getModeFrame(), 
																	title, selectionListObject);
				return(id.getInput());
		}
		
		public static Object getValue(String title, JComponent[] jcomponents)
		{
				GenericOptionPane id = 
						new GenericOptionPane(SwingUtil.getModeFrame(), 
																	title, jcomponents);
				return(id.getInput());
		}
		
		public Object getInput()
		{
				return(returnVal);
		}
		
		private void processTreeSelection(GenericTree tree) {
				// determine what was just selected and 
				// deselect everything else in other list
				TreePath treePath = 
						tree.getSelectionPath();
				if (treePath == null ) 
						return;
				Object obj = treePath.getLastPathComponent();
				System.out.println("treePath.getLastPathComponent() " + obj);
				if ( obj instanceof GenericTreeNode ) {
						selectedValue = ((GenericTreeNode)obj).getUserObject();
						if ( ((GenericTreeNode)obj).getUserObject() instanceof Class) {
								btnString1.setText("New");
						}
						else if ( ((GenericTreeNode)obj).getUserObject() 
											instanceof oncotcap.datalayer.Persistible ) {
								btnString1.setText("Done");
						}
				}
		}	
		
		public void changed(TreePanelEvent e) {
				GenericTree tree = e.getTree();
				processTreeSelection(tree);
		}

		class OptionPaneMouseListener extends MouseAdapter	{
				public void mousePressed(MouseEvent e)
				{
						System.out.println("MOuseEVent " + e);
						// determine what was just selected and deselect everything else
						// in other list
						if (e.getSource() instanceof GenericTree ) {
								GenericTree tree = (GenericTree)e.getSource();
								processTreeSelection(tree);
							
						}
						else if (e.getSource() instanceof JList ) {
								// See if an instance was selected first
								JList list = (JList)e.getSource();
								selectedValue = list.getSelectedValue();
						}	
						//System.out.println("selectedValue " + selectedValue);
						if (e.getClickCount() == 2) {
								selectAndReturn(selectedValue);	
						}
						
				}
		}
		
		class ButtonActionListener implements ActionListener {
				public void actionPerformed(ActionEvent e) {
						Object value = JOptionPane.UNINITIALIZED_VALUE;
						if ( e.getSource() != null && e.getSource() instanceof JButton)
								value = ((JButton)e.getSource()).getText();
						System.out.println("value " + value + " " + value.getClass());
						
						if (value == JOptionPane.UNINITIALIZED_VALUE) {
								return;
						}
						if (value.equals("Done") || value.equals("New")){
								selectAndReturn(selectedValue);
						}
						else if (value.equals("Cancel")) {
								returnVal = null;
								setVisible(false);
						}
				}
		}
		

}
