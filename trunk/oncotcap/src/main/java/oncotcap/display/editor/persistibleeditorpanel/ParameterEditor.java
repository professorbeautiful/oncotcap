package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import oncotcap.util.*;
//import oncotcap.data.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.common.HelpEnabled;
import oncotcap.display.common.ListDialog;

/**
 ** ParameterEditor
 **
 ** A GUI editor for parameters.  This editor, implemented as an input
 ** dialog box, allows users to define the name, type and
 ** <code>SingleParameter</code> list of a <code>Parameter</code>.
 ** This editor can only be invoked with the <code>editParameter</code>
 ** methods.
 **
 ** @version 1, 9/12/2002
 ** @author  shirey
 **/
public class ParameterEditor extends EditorPanel implements HelpEnabled
{
	private JComboBox typeCombo;
	private ParameterBox paramBox;
	private JSplitPane mainBox;
	private JPanel paramEditBox;
	private Parameter parameter = null;
//	private Parameter parameterClone = null;
	private ParameterType initialType = null;
	private boolean descriptionLocked = false;
	private EditorPanel paramEditor;
//	private Container cp;
	
//	private JTextField txtName;
	private static JFrame modeFrame = SwingUtil.getModeFrame();
	
   //Create a blank parameter editor.
	private ParameterEditor(EditorPanel ep)
	{
//		super(modeFrame, true);
		init();
		this.paramEditor = ep;
	}

	public ParameterEditor(Parameter initialVal)
	{
//		parameter = initialVal;
		descriptionLocked = false;
		init();
		edit(initialVal);
	}
	public ParameterEditor()
	{
		init();
	}
	//Create a parameter editor with the type initially set to
	//initialType.
	private ParameterEditor(ParameterType initialType)
	{
//		super(modeFrame, true);
		this.initialType = initialType;
		init();
	}

	//used to test this editor
	public static void main(String [] args)
	{
/*		JFrame jf = new JFrame();
		jf.setSize(300,300);
		JButton but = new JButton("DO IT");
		but.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {editParameter();}});
		but.setLocation(10,10);
		but.setSize(100,30);
		jf.getContentPane().setLayout(null);
		jf.getContentPane().add(but);
		jf.setVisible(true); */
		editParameter();
	}
	private Parameter getParameter()
	{
		return(parameter);
	}
	public Object getValue()
	{
		return(getParameter());
	}
	public void save()
	{
		setValues();
	}
	public void edit(Object obj)
	{
		if(obj instanceof Parameter)
			edit((Parameter) obj);
	}
	public void lockDescription(boolean lock)
	{
		descriptionLocked = lock;
		paramBox.lockDescriptions(lock);
	}
	/**
	 ** Invoke the editor initially displaying the parameter.
	 **
	 ** @param parameter The parameter initially displayed in the
	 ** editor.
	 **
	 ** @returns The parameter that is currently visible.  Returns null
	 ** if the Cancel button is pressed.
	 **/
//	private static ParameterEditor local_pe_save_to_param = null;
//	private static ParameterEditor local_pe_save_to_map = null;
	public static void editParameter(Parameter parameter)
	{
//		ParameterEditor pe = new ParameterEditor();
//		pe.edit(parameter);
//		pe.setVisible(true);
		//EditorFrame.showEditor(parameter);
/*		if(local_pe_save_to_param == null)
		{
			local_pe_save_to_param = new ParameterEditor(parameter);
		}
		else
		{
			local_pe_save_to_param.edit(parameter);
		}
		Parameter p = local_pe_save_to_param.getParameter();
		return(p); */
	}
	
	/**
	 ** Invoke the editor initially displaying a given type.
	 **
	 ** @param paramType The parameter type initially displayed in the
	 ** editor.
	 **
	 ** @returns The parameter that is currently visible.  Returns null
	 ** if the Cancel button is pressed.
	 **/
	public static void editParameter(ParameterType paramType)
	{
//		ParameterEditor pe = new ParameterEditor();
//		Parameter param = paramType.newStorageObject();
//		if(param != null)
//		{
//			pe.edit(param);
//			pe.setVisible(true);
//		}
			//EditorFrame.showEditor(param);
		
//		return(pe.getParameter());
	}
	
	/**
	 ** Invoke the editor with a new Parameter.
	 **
	 ** Asks the user for the parameter type first.
	 **
	 ** @returns The parameter that is currently visible.  Returns null
	 ** if the Cancel button is pressed.
	 **/
	public static void editParameter()
	{
		ParameterType param = (ParameterType) ListDialog.getValue("Choose a Parameter type.", ParameterType.getParameterTypes());
		if(param != null)
		{
//			ParameterEditor pe = new ParameterEditor();
//			Parameter p = param.newStorageObject();
//			if(p != null)
//			{
//				pe.edit(param);
//				pe.setVisible(true);
//			}

//				EditorFrame.showEditor(p);
		}
	}

	//initialize the editor.  Add all graphic components and initialize
	//them to display the current parameter.
	private void init()
	{
//		cp = getContentPane();
			setLayout(new BorderLayout());
		mainBox = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainBox.setOneTouchExpandable(true); 
//		mainBox.add(Box.createVerticalStrut(10));
//		mainBox.add(Box.createVerticalStrut(5));
		setupTypeChoice(); //type field (combo box)
//		mainBox.add(Box.createVerticalStrut(5));
//		mainBox.add(new HorizontalLine(4));
//		mainBox.add(Box.createVerticalStrut(5));
		setupParameterBox(); //list of SingleParameters
//		mainBox.add(new HorizontalLine(4));
		paramEditBox = new JPanel(new BorderLayout());
		mainBox.add(paramEditBox, JSplitPane.BOTTOM);
//		mainBox.add(Box.createVerticalGlue());
		setupButtonBar(); //done and cancel buttons
//		mainBox.add(Box.createVerticalStrut(5));
		add(mainBox, BorderLayout.CENTER);
//		initData();
//		sizeFrame();
//		sizeFrame();  //doing this twice because occasionally the resize doesn't happen.
		setVisible(true);
	}

	public Dimension getPreferredSize()
	{
		int pBoxHeight = 45 + paramBox.parameterLines.size() * 35;
		Dimension pSize;
		if(paramEditor == null)
		{
			pSize = new Dimension(pBoxHeight, 300);
		}
		else
		{
			pSize = new Dimension(Math.max(300, (int) paramEditor.getPreferredSize().getWidth() + 20), pBoxHeight + (int) paramEditor.getPreferredSize().getHeight());
		}
		mainBox.setPreferredSize(pSize);
		mainBox.setMinimumSize(pSize);
		return(pSize);
	}
	//Size the frame based on the size that the mainBox wants to be.
	//For some reason the mainbox reports a smaller size that will make
	//it completly visible.  I'm guessing (but not sure...) that it isn't
	//including the space taken up by the added glue and strut
	//components.  So, I have added a 75 to the width and 50 to the
	//height.
	private void sizeFrame()
	{
//		mainBox.revalidate();
//		Dimension pSize = mainBox.getPreferredSize();
//		setSize((int) pSize.getWidth() + 75, (int) pSize.getHeight() + 50);
//		revalidate();
//		repaint();
	}

	//Add, position and initialize the JLabel and JTextField components
	//that are used to get the name of the Parameter.
	private final static Dimension fieldSize = new Dimension(100,30);
/*	private void setupNameBox()
	{
		Box nameBox = Box.createHorizontalBox();
		txtName = new JTextField( (parameter == null) ? "" : parameter.getName());
		txtName.setMaximumSize(fieldSize);
		txtName.setPreferredSize(fieldSize);
		JLabel lblName = new JLabel("                                Enter a name: ");
		lblName.setMaximumSize(fieldSize);
		lblName.setMinimumSize(fieldSize);
		nameBox.add(Box.createHorizontalGlue());
		nameBox.add(lblName);
		nameBox.add(Box.createRigidArea(new Dimension(5,30)));
		nameBox.add(txtName);
		nameBox.add(Box.createHorizontalGlue());
		mainBox.add(nameBox);
	} */
	
	//Add, initialize and position the "type" combo box and the label
	//describing it.
	private void setupTypeChoice()
	{
		Box typeBox = Box.createHorizontalBox();
		typeCombo = new JComboBox(ParameterType.getParameterTypes());
		typeCombo.setMaximumSize(fieldSize);
		typeCombo.setMinimumSize(fieldSize);
		if(parameter != null)
			typeCombo.setSelectedItem(parameter.getParameterType());
		JLabel typeLabel = new JLabel("Choose the parameter type: ");
		typeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		typeLabel.setMaximumSize(fieldSize);
		typeLabel.setMinimumSize(fieldSize);
		typeBox.add(Box.createHorizontalGlue());
		typeBox.add(typeLabel);
		typeBox.add(Box.createRigidArea(new Dimension(5,30)));
		typeBox.add(typeCombo);
		typeBox.add(Box.createHorizontalGlue());
//		mainBox.add(typeBox);
		//Catch changes to the combo box.  Redisplay the single parameter
		//list when it changes.
		typeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	//			resetParameters();
			}
		});

	}
	public void edit(Parameter param)
	{
		this.parameter = param;
//		parameterClone = (Parameter) param.clone();
		initData();
	//	setupParameterBox();
	//	try{setVisible(true);}
//		catch(Exception e){ e.printStackTrace()  ;}
	}
	private void initData()
	{

		//initialize the box to current parameter or initialType.  Use
		//the parameter if available first.
		if(parameter != null)
		{
			setCombo(parameter.getParameterType());
		}
		else if(initialType != null)
		{
			setCombo(initialType);
		}
		setupSingleParameters();
		if(paramEditor != null)
			paramEditBox.remove(paramEditor);
		paramEditor = parameter.getParameterEditorPanelWithInstance();
		//System.out.println("paramEditor " + paramEditor);
		//paramEditBox.add(paramEditor, BorderLayout.CENTER);
		mainBox.add(paramEditor, JSplitPane.BOTTOM);
		if(paramEditor instanceof VariableEditor)
			((VariableEditor)paramEditor).setEditMode(VariableEditor.EDITPARAM);
		paramBox.update(parameter);
		
	}

	public void enableDescriptionEditing(boolean enable)
	{
		if(!enable)
		{
			paramBox.setVisible(false);
			mainBox.setDividerLocation(0);
			mainBox.setOneTouchExpandable(false);
		}
		else
		{
			paramBox.setVisible(true);
			mainBox.setDividerLocation((int) paramBox.getPreferredSize().getHeight());
			mainBox.setOneTouchExpandable(true);
		}
		mainBox.revalidate();
		mainBox.repaint();

	}
	//Set the value of the type ComboBox
	private void setCombo(ParameterType pType)
	{
		ParameterType tParam;
		int n;
		boolean found = false;
		typeCombo.setEnabled(false);
		for(n=0; n < typeCombo.getItemCount() && ! found; n++)
		{
			tParam = (ParameterType) typeCombo.getItemAt(n);
			if(tParam == pType)
			{
				typeCombo.setSelectedIndex(n);
				found = true;
			}
		}
		if(!found)
			typeCombo.setSelectedIndex(0);
			
		sizeFrame();
//		revalidate();
		repaint();
	}

	private void setCombo(Parameter param)
	{
		setCombo(param.getParameterType());
	}
	
	//Add, initialize and position the box that contains the list of
	//editable single parameters
	private static Dimension parameterSize = new Dimension(Short.MAX_VALUE,35);
	private static Dimension paramLabelSize = new Dimension(120,25);
	private static Dimension paramTextSize = new Dimension(300,25);
	private void setupParameterBox()
	{
		paramBox = new ParameterBox();
		Box labelBox = Box.createHorizontalBox();
		JLabel labDesc = new JLabel("Enter a description");
		JLabel labGlue = new JLabel();
		labDesc.setMaximumSize(paramTextSize);
		labDesc.setHorizontalAlignment(SwingConstants.LEFT);
		labGlue.setHorizontalAlignment(SwingConstants.RIGHT);
		labGlue.setMinimumSize(paramLabelSize);
		labGlue.setMaximumSize(paramLabelSize);
		labelBox.add(labGlue);
		labelBox.add(Box.createHorizontalStrut(10));
		labelBox.add(labDesc);
//		mainBox.add(labelBox);
//		resetParameters();
		mainBox.add(paramBox, JSplitPane.TOP);
		mainBox.add(Box.createVerticalStrut(5));
		mainBox.revalidate();
		mainBox.repaint();
	}

	//clear the single parameter list and redisplay based on the
	//type currently selected in the type ComboBox.
/*	private void resetParameters()
	{
		ParameterType pType = parameter.getParameterType(); //(ParameterType) typeCombo.getSelectedItem();
		
		//iterate through the list of single parameters for this type and
		//display a ParameterLine for each
		Iterator it = pType.getSingleParameters().getIterator();
		paramBox.clear();
		while(it.hasNext())
		{
			paramBox.addParameterLine(new DefaultSingleParameter((SingleParameter)it.next()));
		}

		//update the box based on the current parameter
		if(parameter != null)
			paramBox.update(parameter);
		
		paramBox.add(Box.createVerticalGlue());	//for some strange
																//reason this line
																//causes the app to
																//generate an
																//OutOfBoundsException
																//when a type other
																//than FLOAT is picked
		sizeFrame();
//		revalidate();
		repaint();
	}*/
	private void setupSingleParameters()
	{
		
		paramBox.clear();
		if(parameter != null )
		{
			SingleParameterList singleParams = parameter.getSingleParameterList();
			Iterator it = singleParams.getIterator();
			while(it.hasNext())
			{
				paramBox.addParameterLine((SingleParameter)it.next());
			}
			sizeFrame();
//			revalidate();
			repaint();
		}
	}
	//add, position and initialize the Done and Cancel buttons.
	private void setupButtonBar()
	{
		Box buttonBar = Box.createHorizontalBox();
		JButton btnDone = new JButton("Done");
		JButton btnCancel = new JButton("Cancel");
		buttonBar.add(Box.createHorizontalGlue());
		buttonBar.add(btnDone);
		buttonBar.add(btnCancel);
		buttonBar.add(Box.createHorizontalGlue());
//		mainBox.add(buttonBar);

		//when the done button is pressed check for valid entries then
		//set the current information in the editor to the "parameter"
		//field and hide the editor.
		btnDone.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				if(checkValues())
				{
					setValues();
					setVisible(false);
				}
			}
		});

		//when the cancel button is pressed set the parameter field to
		//null and hide the editor.
		btnCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				parameter = null;
				setVisible(false);
			}
		});

	}

	//check for valid entries in the editor screen.  Display warning
	//messages and return false if there are any problems.  Return true
	//if everything is okay.
	private boolean checkValues()
	{

/*		if(txtName.getText().trim().equals(""))
		{
			OncMessageBox.showMessageDialog( modeFrame, 
													  "Please enter a value for the name.",
													  "ERROR: Parameter Editor",
													  OncMessageBox.WARNING_MESSAGE);
			return(false);
		}
*/
		Iterator it = paramBox.getIterator();
		while(it.hasNext())
		{
			ParameterLine pl = (ParameterLine) it.next();
			if(pl.getDescription().trim().equals(""))
			{
				OncMessageBox.showMessageDialog( modeFrame, 
															"Please enter a value for the " + pl.getParameterType() + " type.",
															"ERROR: Parameter Editor",
															OncMessageBox.WARNING_MESSAGE);
				return(false);
			}
		}
		
		return(true);
	}

	//save the currently displayed information in the parameter field.
	private void setValues()
	{
		ParameterLine pl;
		SingleParameter sp;
		Iterator it = paramBox.getIterator();

		if(parameter == null)
			parameter = ((ParameterType) typeCombo.getSelectedItem()).newStorageObject();

		if(paramEditor != null) {
			paramEditor.save();
		}

		//parameter.clearSingleParameters();
		while(it.hasNext())
		{
			pl = (ParameterLine) it.next();
			pl.save();
//			parameter.addSingleParameter(pl.getSingleParameter());
		}

//		if (parameterClone == null) parameterClone = (Parameter) parameter.clone();

	}

	//a GUI box to display a list of single parameters and allow the
	//discription field of each single parameter to be edited.
	private class ParameterBox extends Box
	{
		Hashtable parameterLines = new Hashtable();
		JPanel labelBox = new JPanel();
		ParameterBox()
		{
			super(BoxLayout.Y_AXIS);
			init();
		}
		private void init()
		{
			labelBox.setLayout(new GridLayout(1,2));
			JLabel lblLeft = new JLabel("Component Parameter");
			lblLeft.setFont(new Font("Trebuchet", Font.BOLD, 12));
			JLabel lblRight = new JLabel("|     Parameter Name (for backquoting)");
			lblRight.setFont(new Font("Trebuchet", Font.BOLD, 12));
			JPanel pnlLeft = new JPanel();
			pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.X_AXIS));
			pnlLeft.add(Box.createHorizontalGlue());
			pnlLeft.add(lblLeft);
			pnlLeft.add(Box.createHorizontalStrut(20));
			JPanel pnlRight = new JPanel();
			pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.X_AXIS));
			pnlRight.add(Box.createHorizontalStrut(3));
			pnlRight.add(lblRight);
			pnlRight.add(Box.createHorizontalGlue());
			labelBox.add(pnlLeft);
			labelBox.add(pnlRight);
			reinit();
		}
		private void reinit()
		{
			add(labelBox);
		}
		void lockDescriptions(boolean lock)
		{
			Enumeration lines = parameterLines.elements();
			while(lines.hasMoreElements())
			{
				ParameterLine line = (ParameterLine) lines.nextElement();
				line.lockDescription(lock);
			}
		}
		void addParameterLine(SingleParameter singleParam)
		{
			//if a parameter line with the same name exists already do
			//nothing.
			if(! parameterLines.containsKey(singleParam.getID()))
			{
				ParameterLine pLine = new ParameterLine(singleParam);
				super.add(pLine);
				parameterLines.put(singleParam.getID(), pLine);
				setMaximumSize(new Dimension(Short.MAX_VALUE, parameterLines.size() * 26));
			}
		}

		void clear()
		{
			super.removeAll();
			parameterLines.clear();
			reinit();
		}
		Iterator getIterator()
		{
			return(parameterLines.values().iterator());
		}
		void update(Parameter param)
		{
			SingleParameter ps;
			Iterator it = param.getSingleParameters();
			while(it.hasNext())
			{
				ps = (SingleParameter) it.next();
				updateParameterLine(ps);
			}
		}
		void updateParameterLine(SingleParameter singleParam)
		{
			if( parameterLines.containsKey(singleParam.getID()))
			{
				ParameterLine pl = (ParameterLine) parameterLines.get(singleParam.getID());
				pl.update(singleParam);
			}
		}
	}

	//a GUI line to display a SingleParameter.  Displays the parameter
	//type and description, the description is displayed in a JTextField
	//so that it can be edited.
	Dimension minParam = new Dimension(0,35);
	Dimension maxParam = new Dimension(Short.MAX_VALUE,35);
	private class ParameterLine extends JPanel
	{
		JTextField txtDescription;
//		JTextField txtValue;
		JLabel labName;
		private String name;
		SingleParameter singleParameter;
		ParameterLine(SingleParameter singleParam)
		{
			Object val;
			singleParameter = singleParam;
			
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.name = singleParam.getDisplayName();
			setPreferredSize(minParam);

			Dimension txtMax = new Dimension(Short.MAX_VALUE, 25);

			labName = new JLabel(singleParam.getDefaultName());
			labName.setToolTipText(singleParam.getDefaultName());
			labName.setMaximumSize(txtMax);
			labName.setHorizontalAlignment(SwingConstants.RIGHT);
			txtDescription = new JTextField(singleParam.getDisplayName());
			txtDescription.setEditable(!descriptionLocked);
			txtDescription.setMaximumSize(txtMax);
			txtDescription.setHorizontalAlignment(JTextField.LEFT);
			add(labName);
			add(Box.createHorizontalStrut(10));
			add(txtDescription);
			add(Box.createHorizontalStrut(3));
			setBackground(oncotcap.display.browser.OncBrowserConstants.MBColorPale);
			addLineSelectionListener(this);
			addLineSelectionListener(labName);
			addLineSelectionListener(txtDescription);
			resize();
			
		}
		private void resize()
		{
			Dimension txtSize = new Dimension((int)getSize().getWidth()/3, 35);
			labName.setPreferredSize(txtSize);
			labName.setSize(txtSize);
			txtDescription.setPreferredSize(txtSize);
			txtDescription.setSize(txtSize);
//			txtValue.setPreferredSize(txtSize);
//			txtValue.setSize(txtSize);
			SwingUtil.revalidateAll(this);
			repaint();
			addComponentListener(new LineResizeListener());
		}
		void lockDescription(boolean lock)
		{
			txtDescription.setEditable(!lock);
		}
		private void addLineSelectionListener(Component comp)
		{
			LineSelectionListener ls = new LineSelectionListener(comp);
			comp.addFocusListener(ls);
			comp.addMouseListener(ls);
		}
/*		String getValue()
		{
			return(txtValue.getText());
		}
		*/
		private class LineResizeListener implements ComponentListener
		{
			public void componentHidden(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentResized(ComponentEvent e)
			{
				resize();
			}
			public void componentShown(ComponentEvent e) {}

		}
		class LineSelectionListener implements FocusListener, MouseListener
		{
			Component comp;
			LineSelectionListener(Component comp)
			{
				this.comp = comp;
				comp.setFocusable(true);
			}
			public void focusGained(FocusEvent e)
			{
				select();
			}
			public void focusLost(FocusEvent e)
			{
				unSelect();
			}
			public void mouseClicked(MouseEvent e)
			{
				select();
				comp.requestFocusInWindow();
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}

			private void select()
			{
				setBackground(Color.red);
				repaint();
			}
			private void unSelect()
			{
				setBackground(TcapColor.lightBlue);
				repaint();
			}

		}
		void save()
		{
			updateSingleParameter();
		}
		void update(SingleParameter sp)
		{
			txtDescription.setText(sp.getDisplayName());
			repaint();
		}
		SingleParameter getSingleParameter()
		{
			updateSingleParameter();
			return(singleParameter);
		}
		String getParameterType()
		{
			return(labName.getText());
		}
		String getDescription()
		{
			return(txtDescription.getText());
		}
		private void updateSingleParameter()
		{

			singleParameter.setDisplayName(txtDescription.getText());
//			singleParameter.setValue(txtValue.getText());
		}
		public String getName()
		{
			return(name);
		}	
	}
	public String getHelpId() {
		// Determine which parameter editor this is and return appropriate name
		Parameter param = getParameter();
		// TODO: Make all param editors help enabled 
		// but for now derive an id if ( param instanceof HelpEnabled )
		System.out.println("GET HELP ID PARAM " + param.getClass());
		if (param != null )
			return param.getClass().getSimpleName();
		return null;
	}

	public void setHelpId(String helpId) {
		
	}

		/**
	 * @return Returns the paramEditor.
	 */
	public EditorPanel getParamEditor() {
		return paramEditor;
	}
}

