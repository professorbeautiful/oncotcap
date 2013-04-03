package oncotcap.display.common;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import oncotcap.*;
import oncotcap.datalayer.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.browser.*;
import oncotcap.display.editor.*;

public class VariableChooser extends JDialog implements ActionListener
{
	private FilterEditorPanel filterPanel;
	private OncList varList;
	private ProcessDeclaration process;
	private Editable caller;
	private boolean includeEnums;
	private Vector chooserListeners = new Vector();
	private Vector<Object> objectsToIncludeAtBeginningOfList = null;
	private JComboBox scopeCB = new JComboBox();
	private Class selectedScope = StatementTemplate.class;
	private Object selectedVariable = null;
	
	public VariableChooser(ProcessDeclaration process, Editable caller, boolean includeEnums, Vector<Object> objectsToIncludeAtBeginningOfList, boolean modal)
	{
		super(EditorFrame.getEditor(caller), "Choose a variable", modal);
		this.process = process;
		this.caller = caller;
		this.includeEnums = includeEnums;
		this.objectsToIncludeAtBeginningOfList = objectsToIncludeAtBeginningOfList;
		init();
	}	
	public VariableChooser(ProcessDeclaration process, Editable caller, boolean includeEnums, Vector<Object> objectsToIncludeAtBeginningOfList)
	{
		this(process, caller, includeEnums, objectsToIncludeAtBeginningOfList, false);
	}
	public VariableChooser(ProcessDeclaration process, Editable caller, boolean includeEnums)
	{
		this(process, caller, includeEnums, null);
	}

	private void init()
	{
		varList = new OncList();
		setSize(500, 500);
		filterPanel = new FilterEditorPanel();
		filterPanel.setClassPanelVisible(false);
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		ScopeComboBoxRenderer renderer = new ScopeComboBoxRenderer();
		scopeCB.addItem(CodeBundle.class);
		scopeCB.addItem(StatementTemplate.class);
		scopeCB.addItem(StatementBundle.class);
		scopeCB.addItem(Encoding.class);
		scopeCB.addItem(SubModel.class);
		scopeCB.addItem(SubModelGroup.class);
		scopeCB.addItem(ModelController.class);
		scopeCB.setSelectedItem(selectedScope);
		scopeCB.setEditable(false);
		scopeCB.setRenderer(renderer);
		
		scopeCB.addActionListener(this);
		JScrollPane varListSP = new JScrollPane(varList);
		cp.add(scopeCB, BorderLayout.NORTH);
		cp.add(varListSP, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e)
			{
				chooserListeners.clear();
				EditorFrame.showEditor(caller);
			}
		});
		updateVariableList(selectedScope);
		varList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e)
			{
				fireVariableSelected(varList.getSelectedValue());
			}
		});
	}

	private static Vector enumDefClass = new Vector();
	static{ enumDefClass.add("EnumDefinition"); }
	private void updateVariableList(Class scopeClass)
	{
		if(process != null)
		{
			varList.clear();
			//filterPanel.save();
			if(this.objectsToIncludeAtBeginningOfList != null)
				varList.addAll(this.objectsToIncludeAtBeginningOfList);
			//varList.addAll(process.findVariables((OncFilter) filterPanel.getValue(), includeEnums));
			varList.addAll(process.findVariablesByScope(scopeClass,
					(CodeBundle)caller, includeEnums));
		}
	}

	public void addVariableListener(VariableListener listener)
	{
		if(! chooserListeners.contains(listener))
			chooserListeners.add(listener);
	}
	public void removeVariableListener(VariableListener listener)
	{
		if(chooserListeners.contains(listener))
			chooserListeners.remove(listener);
	}
	public void fireVariableSelected(Object variable)
	{
		if(variable instanceof Persistible)
		{
			selectedVariable = variable;
			Iterator it = chooserListeners.iterator();
			while(it.hasNext())
				((VariableListener) it.next()).variableChanged((Persistible) variable);
		}
	}
	public void actionPerformed(ActionEvent e)
	{
		selectedScope = (Class)scopeCB.getSelectedItem();
		updateVariableList(selectedScope);
	}
	class ScopeComboBoxRenderer extends JLabel
	implements ListCellRenderer {
		public ScopeComboBoxRenderer() {
			setOpaque(true);
			//setHorizontalAlignment(CENTER);
			//setVerticalAlignment(CENTER);
		}
		
		/*
		 * This method finds the image and text corresponding
		 * to the selected value and returns the label, set up
		 * to display the text and image.
		 */
		public Component getListCellRendererComponent(
				JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus) {
		
			if (isSelected) {
				setBackground(Color.BLUE);
				setForeground(Color.WHITE);
			} else {
				setBackground(Color.WHITE);
				setForeground(Color.BLACK);
			}
			
			if (value instanceof Class ) {
				if (((Class)value).getSimpleName().equals("Encoding"))
					setText("Edict");
				else
					setText(((Class)value).getSimpleName());
			}
			return this;
		}
	}
	/**
	 * @return Returns the selectedVariable.
	 */
	public Object getSelectedVariable() {
		return selectedVariable;
	}
	/**
	 * @param selectedVariable The selectedVariable to set.
	 */
	public void setSelectedVariable(Object selectedVariable) {
		this.selectedVariable = selectedVariable;
	}
}
