package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.common.*;
import oncotcap.process.OncProcess;

public class InstantiateActionEditor extends EditorPanel implements ActionEditor
{
	private InstantiateAction action;
	private OncList varListPanel;
	private JPanel varPanel = new JPanel(new FlowLayout());
	private JPanel editPanel;
	private JLabel procLabel = new JLabel();
	private JPanel instantiateTitle = new JPanel();
	private JPanel instantiateOneOrMany = new JPanel();

	private Vector vars = new Vector();
	private EditorPanel currentEditor = null;
	private CodeBundleEditorPanel cbParent;
	private LabeledTextBox enumInitArgs = new LabeledTextBox("<html><body>Identifying characteristics <br>(Characteristic values from Instantiation Subset Picker or Table Parameter)</body></html>");
	private LabeledTextBox numInstancesField = new LabeledTextBox("Number of instances to create.");
	private JCheckBox aggregateCheckBox = new JCheckBox("Aggregate");
		
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(400,300);
		jf.getContentPane().add(new InstantiateActionEditor(new InstantiateAction()));
		jf.setVisible(true);
	}
	public InstantiateActionEditor()
	{
		init();
	}
	public InstantiateActionEditor(InstantiateAction action)
	{
		init();
		edit(action);
	}

	JPanel topPanel = null;
	JPanel varEditorPanel = null;
	private void init()
	{
		setLayout(new BorderLayout());
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(instantiateTitle);
		topPanel.add(instantiateOneOrMany);
		//topPanel.setBackground(Color.BLUE);
		varEditorPanel = new JPanel();
		//varEditorPanel.setBackground(Color.RED);
		JScrollPane scrollPane = new JScrollPane();
		//scrollPane.setBackground(Color.YELLOW);
		scrollPane.setViewportView(varEditorPanel);
		initTopPanel();
		add(topPanel, BorderLayout.NORTH);
		scrollPane.setPreferredSize(new Dimension(500, 200));
		add(scrollPane, BorderLayout.CENTER);
	}

	private void initTopPanel(){
		
	}
	
	
		/*
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		enumInitArgs.setTextBoxWidth(200);
		enumInitArgs.setPreferredSize(new Dimension(600,60));
		enumInitArgs.setMaximumSize(new Dimension(600,60));
		
//		varListPanel = new OncList();
//		varListPanel.setCellRenderer(new VariableCellRenderer());
//		varListPanel.addListSelectionListener(new MyListSelectionListener());
		JScrollPane varListSP = new JScrollPane(varListPanel);
		varListSP.setMinimumSize(new Dimension(0, 100));
		varListSP.setMaximumSize(new Dimension(Short.MAX_VALUE, 100));
		//varListSP.setPreferredSize(new Dimension(300,300));
		//varListPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
		editPanel = new JPanel();
		add(procLabel);
		add(Box.createVerticalStrut(10));
		add(enumInitArgs);
		add(Box.createVerticalStrut(10));
		numInstancesField.setTextBoxWidth(200);
		if ( numInstancesField.getText() == null || numInstancesField.getText().equals("")){
			numInstancesField.setText("1");
		}
		numInstancesField.setPreferredSize(new Dimension(600,40));
		numInstancesField.setMaximumSize(new Dimension(600,40));
		add(numInstancesField);
		// Per roger do not init variables in instantiate action
		//varListSP It limits ability to combine submodels
		// It also creates a problem when using TableParameter to define instantiation
		//
		add(new JLabel("Variables to initialize."));
		add(varPanel);
		//add(varListSP);
		add(Box.createVerticalStrut(3));
		add(new JLabel("* indicates a required variable"));
		add(Box.createVerticalStrut(10));
		add(editPanel);

	}
		*/
	public void edit(Object action)
	{
		if(action instanceof InstantiateAction)
			edit((InstantiateAction) action);
	}
	public void edit(OncAction action)
	{
		if(action instanceof InstantiateAction)
			edit((InstantiateAction) action);
	}
	public void edit(InstantiateAction act)
	{
		action = act;
		updateTopPanel();
		updateEditVarPanel();
	}
	
	private void updateTopPanel(){
		instantiateTitle.add(new JLabel("Instantiate"));
		JLabel processNameLabel = new JLabel(action.getInitializationProcessDeclaration().getName());
		processNameLabel.setForeground(Color.BLUE);
		
		instantiateTitle.add(processNameLabel);
		enumInitArgs.setText(action.getEnumInitializations());
		enumInitArgs.setAlignmentX(Component.LEFT_ALIGNMENT);
		topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		topPanel.add(enumInitArgs);
		numInstancesField.setText(action.getNumNewProcesses());
		numInstancesField.setTextBoxWidth(100);
		if ( numInstancesField.getText() == null || numInstancesField.getText().equals("")){
			numInstancesField.setText("1");
		}
		numInstancesField.setPreferredSize(new Dimension(200,30));
		numInstancesField.setMaximumSize(new Dimension(200,30));
		topPanel.add(numInstancesField);
		aggregateCheckBox.setSelected(action.getInstantiateAggregate());
		topPanel.add(aggregateCheckBox);
	}
	private void updateEditVarPanel(){
		ProcessDeclaration proc = action.getInitializationProcessDeclaration();
		if(proc != null)
		{
			Vector initVars = OncProcess.getInitializationVars(proc.getProcessClass());
			Iterator it = initVars.iterator();
			SpringLayout springLayout = new SpringLayout();
			varEditorPanel.setLayout(springLayout);
			int rows = 0;
			while(it.hasNext()) {
				Object obj = it.next();
				//assignVariableInitialization(obj);
				// Create an edit panel for each variable to init
				JPanel editPanel = new JPanel();
				JLabel varLabel = new JLabel(obj.toString());
				// Set up label constraints
				varEditorPanel.add(varLabel);
				EditorPanel varEditor = null;
				if(obj instanceof DeclareVariable) {
					varEditor = ((DeclareVariable) obj).getType().getEditor();
					
				}
				else if ( obj instanceof VariableDefinition ){
					varEditor = ((VariableDefinition)obj).getDeclarationEditor();
					
				}
//			varEditorPanel.add(new JTextField());
				varEditor.setPreferredSize(new Dimension(400, 20));
				//set up editor constraints
				
				varEditorPanel.add(varEditor);
//				springLayout.putConstraint(SpringLayout.WEST, varEditor, 10, SpringLayout.EAST, varLabel);
//				springLayout.putConstraint(SpringLayout.NORTH, varEditorPanel, 10, SpringLayout.NORTH, varLabel );
//				springLayout.putConstraint(SpringLayout.NORTH, varLabel, 5, SpringLayout.NORTH, varEditor);
				//springLayout.putConstraint(SpringLayout.NORTH,varEditor , 10, SpringLayout.NORTH, varEditorPanel);
				//springLayout.putConstraint(SpringLayout.SOUTH,varEditorPanel  , 100, SpringLayout.SOUTH, varEditor);
				//varEditorPanel.add( editPanel );
				rows++;
			}
			
			SpringUtilities.makeCompactGrid(varEditorPanel,
					rows, 2, //rows, cols
					2, 2,        //initX, initY
					20, 20);       //xPad, yPad

		}
		//enumInitArgs.setText(action.getEnumInitializations());
	}
	
//		if(action != null)
//		{
//			ProcessDeclaration proc = action.getInitializationProcessDeclaration();
//			if(proc != null)
//			{
//				procLabel.setText("Instantiate a " + proc);
//				fillVarList(proc);
//				Iterator it = action.getVariableInitializations().iterator();
//				while(it.hasNext()) {
//					Object obj = it.next();
//					assignVariableInitialization(obj);
//					// Create an edit panel for each variable to init
//					varPanel.add( new JButton("Add me " + obj.toString()));
//				}
//			}
//			enumInitArgs.setText(action.getEnumInitializations());
//		}
	private void assignVariableInitialization(Object obj)
	{
		if(obj instanceof DeclareVariable)
		{
			DeclareVariable varInit = (DeclareVariable) obj;
			String varInitName = varInit.getName().trim();
			ListModel lm = varListPanel.getModel();
			for(int n = 0; n < lm.getSize(); n++)
			{
				Object li = lm.getElementAt(n);
				if(li instanceof VariableListItem)
				{
					VariableListItem vli = (VariableListItem) li;
					String vliName = vli.toString().trim();
					if(vliName.equalsIgnoreCase(varInitName))
						  vli.setInitialization(varInit);
				}
			}
		}
	}
	private void fillVarList(ProcessDeclaration oncProcess)
	{
		if(oncProcess != null)
		{
			vars.addAll(OncProcess.getInitializationVars(oncProcess.getProcessClass()));
					//oncProcess.findVariables(true));
//			varListPanel.clear();
//			Iterator it = vars.iterator();
//			while(it.hasNext())
//			{
//				Object obj = it.next();
//				addToListPanel(obj);
//			}
		}
	}
	public Object getValue()
	{
		return(action);
	}
 void addToListPanel(Object obj)
	{
		varListPanel.add(new VariableListItem(obj));
	}
	
	public void save()
	{
//		VariableListItem vli;
//		
//		if(action != null)
//		{
//			ListModel listModel = varListPanel.getModel();
//			action.clearVariableInitializations();
//			for(int n = 0; n < listModel.getSize(); n++)
//			{
//				Object obj = listModel.getElementAt(n);
//				if(obj instanceof VariableListItem)
//				{
//					vli = (VariableListItem) obj;
//					if(vli.getInitialization() != null)
//					{
//						action.addVariableInitialization(vli.getInitialization());
//					}
//				}
//			}
			action.setEnumInitializations(enumInitArgs.getText());
			action.setNumNewProcesses(numInstancesField.getText());
			action.setInstantiateAggregate(aggregateCheckBox.getModel().isSelected());
			if(currentEditor != null)
			{
				currentEditor.save();
			}
		//}
		
	}
	public ActionType getType()
	{
		return(ActionType.INSTANTIATE);
	}
	public CodeBundleEditorPanel getCBParent()
	{
		return(cbParent);	
	}
	public void setCBParent(CodeBundleEditorPanel par)
	{
		cbParent = par;
	}
	private class VariableListItem
	{
		private Object var;
		private DeclareVariable initialization = null;
		
		VariableListItem(Object obj)
		{
			var = obj;
		}
		boolean required()
		{
			if(var instanceof DeclareVariable)
				return(false);
			else if(var instanceof VariableDefinition)
			{
				if(((VariableDefinition) var).isFromPropFile())
					return(true);
				else
					return(false);
			}
			else
				return(false);
		}
		public String toString()
		{
			if(var == null)
				return("");
			else if(var.toString() == null)
				return("");
			else
				return(var.toString());
		}
		public Object getVariable()
		{
			return(var);
		}
		public DeclareVariable getInitialization()
		{
			return(initialization);
		}
		public void setInitialization(DeclareVariable init)
		{
			initialization = init;
		}
		public EditorPanel getEditor()
		{
			EditorPanel rPan = null;
			if(var instanceof DeclareVariable)
			{
				if(initialization == null)
				{
					initialization = ((DeclareVariable) var).getType().getStorageInstance();
					initialization.setName(var.toString());
				}
				rPan = ((DeclareVariable) var).getType().getEditor();

				rPan.edit(initialization);
				return(rPan);
			}
			else if(var instanceof VariableDefinition)
			{
				if(initialization == null)
				{
					initialization = ((VariableDefinition) var).getDeclarationInstance();
					initialization.setName(var.toString());
				}
				rPan =  ((VariableDefinition) var).getDeclarationEditor();
				rPan.edit(initialization);
				return(rPan);
			}
			else 
				return(null);
		}
	}

	class VariableCellRenderer extends JLabel implements ListCellRenderer {

		public Component getListCellRendererComponent(
			JList list,
			Object value,            // value to display
			int index,               // cell index
			boolean isSelected,      // is the cell selected
			boolean cellHasFocus)    // the list and the cell have the focus
		{
			String s = value.toString();
			setText(s);
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}
			else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			if(value instanceof VariableListItem)
			{
				VariableListItem vli = (VariableListItem) value;
				if( vli.required() )	setText("* " + s);
				if( vli.getInitialization() == null ) setForeground(Color.red);
			}
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			return this;
		}
	}
	class MyListSelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			Object var = varListPanel.getSelectedValue();
			if(currentEditor != null)
			{
				currentEditor.save();
				currentEditor = null;
			}
			editPanel.removeAll();
			if(var != null  && var instanceof VariableListItem)
			{
				EditorPanel ep;
				VariableListItem vli = (VariableListItem) var;
/*				if(vli.getVariable() instanceof DeclareEnum)
				{
					ep = new EnumComboBox();
					if(vli.getInitialization() != null)
						ep.edit(vli.getInitialization());
				}
				else */
				ep = vli.getEditor();
				currentEditor = ep;
				editPanel.add(ep);
				editPanel.revalidate();
				editPanel.repaint();
			}
		}
	}
}

