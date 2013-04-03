package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.DataSourceStatus;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;

import oncotcap.util.SwingUtil;
import oncotcap.util.TcapColor;
import oncotcap.util.OncMessageBox;
import oncotcap.util.Util;

public class CodeBundleEditorPanel extends EditorPanel
{
	private static final int VAR_PANE_WIDTH = 400;
	private EventChooser eventChooser;
	private LabeledComboBox oncProcess;
	private LabeledComboBox oncEvent;
	private LabeledComboBox oncMethod;
	private JLabel eventLabel;
	private Box actionListLabelBox;
	OncList actionList;
	private JScrollPane actionListSP;
	private JPanel actionPanel = new JPanel();
	private String blankLabel = "";
	private String addProcess = "Add a new Process";
	private String addEvent = "Add a new Event";
	private String addMethod = "Add a new Method";
	private Box comboBoxPanel;
	private LabeledTextBox ifClause = new LabeledTextBox(" IF: ");
	private ActionEditor actionEditor = null;
	private OncAction previousAction;
	private CodeBundle codeBundle = null;

	private StatementTemplateAndParametersPanel stpp = null;
	
	private static CodeBundleEditorPanel editPanel;  //used only by TestButtons

	private static Vector<Object> varListBeginningObjects = new Vector<Object>();
	public static final String upstreamParameterPlaceholderName = "INITIALIZE VIA AN UPSTREAMED PARAMETER";
	public static final DeclareEnum upstreamParameterPlaceholder = new DeclareEnum(false);
	static
	{ 
		upstreamParameterPlaceholder.setName(upstreamParameterPlaceholderName);
		upstreamParameterPlaceholder.setDisplayName(upstreamParameterPlaceholderName);
		varListBeginningObjects.add(upstreamParameterPlaceholder);
	}
	public CodeBundleEditorPanel()
	{
		init();
	}
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(400,600);
		Vector allBundles = CodeBundle.getAllCodeBundles();
		CodeBundle cbToEdit;
		int answer = Util.yesNoQuestion("Edit an existing code bundle?");
		if(answer == JOptionPane.YES_OPTION)
		{
			if(allBundles.isEmpty())
			{
				OncMessageBox.showWarning("No code bundles exist in the current project.", "Code Bundle Editor");
				return;
			}
			Object bndl = ListDialog.getValue("Choose a CodeBundle.", allBundles);
			if(bndl == null)
				return;
			else if (bndl instanceof CodeBundle)
			{
				cbToEdit = (CodeBundle) bndl;
			}
			else
				return;			
		}
		else
			cbToEdit = new CodeBundle();

		CodeBundleEditorPanel cbep;
		jf.setLayout(new BorderLayout());
		jf.getContentPane().add((cbep = new CodeBundleEditorPanel()), BorderLayout.CENTER);
		cbep.setCodeBundle(cbToEdit);
		jf.setVisible(true);
	//	SwingUtil.revalidateAll(cbep);
	//	cbep.repaint();

		editPanel = cbep;
		TestButtons tb = new TestButtons();
	}
	private void init()
	{
		setLayout(new BorderLayout());
		Container codeBox = Box.createVerticalBox();

		eventChooser = new EventChooser();
		eventChooser.chooseProcess.setText("Method");
		eventChooser.addActionListener(new EventTypeChanged());
		comboBoxPanel = Box.createHorizontalBox();
		actionListLabelBox = Box.createHorizontalBox();
		actionPanel.setLayout(new BorderLayout());
		
		oncProcess = new LabeledComboBox("Process", ProcessDeclaration.getAllProcesses());
		oncProcess.insertItemAt(blankLabel, 0);
		oncProcess.addItem(addProcess);
		oncProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processChanged();
			}
		});
		oncMethod = new LabeledComboBox("Method");
		oncMethod.addItem(blankLabel);
		oncMethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				methodChanged();
			}
		});

		oncEvent = new LabeledComboBox("Event", oncotcap.datalayer.persistible.EventDeclaration.getAllEvents());
		oncEvent.setRenderer(new ComboDisplayLabel());
		oncEvent.insertItemAt(blankLabel, 0);
		oncEvent.addItem(addEvent);
		oncEvent.setVisible(false);
		oncEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventChanged();
			}
		});

		eventLabel = new JLabel(" responds to event: ");
		comboBoxPanel.add(new JLabel(" WHEN: "));
		comboBoxPanel.add(oncProcess);
		comboBoxPanel.add(eventLabel);
		comboBoxPanel.add(oncEvent);
		//comboBoxPanel.add(Box.createHorizontalStrut(5));
		comboBoxPanel.add(oncMethod);

		 // need to do this to get the ifClause to left justify- I hate
		 // Swing.
		ifClause.setLayout(new BoxLayout(ifClause, BoxLayout.X_AXIS));
		ifClause.setTextBoxWidth(500);

		JLabel lblActions = new JLabel(" THEN DO:  Actions");
		HyperLabel lblAddAction = new HyperLabel("Add an Action");
		lblAddAction.addHyperListener(new AddActionListener());
		HyperLabel lblRemoveAction = new HyperLabel("Remove Action");
		lblRemoveAction.addHyperListener(new RemoveActionListener());
		actionListLabelBox.add(lblActions);
		actionListLabelBox.add(Box.createHorizontalStrut(10));
		actionListLabelBox.add(lblAddAction);
		actionListLabelBox.add(Box.createHorizontalStrut(10));
		actionListLabelBox.add(lblRemoveAction);
		actionListLabelBox.add(Box.createHorizontalGlue());
		actionList = new OncList();
//		actionList.setModel(new DefaultListModel());
		actionList.addListSelectionListener(new ActionChangedListener());
		actionListSP = new JScrollPane(actionList);


		codeBox.add(eventChooser);
		codeBox.add(Box.createVerticalStrut(5));
		codeBox.add(comboBoxPanel);
		codeBox.add(Box.createVerticalStrut(5));
		codeBox.add(new HorizontalLine(5));
		codeBox.add(ifClause);
		codeBox.add(new HorizontalLine(5));
		codeBox.add(actionListLabelBox);
		codeBox.add(actionListSP);
		codeBox.add(Box.createVerticalStrut(5));
		codeBox.add(new HorizontalLine(5));
		codeBox.add(Box.createVerticalStrut(5));
		codeBox.add(actionPanel);

		
		add(codeBox, BorderLayout.CENTER);
	//	resize();
		setPreferredSize(new Dimension(500, 600));
		addComponentListener(new ResizeListener());
	}
	public CodeBundle getCodeBundle()
	{
		return(codeBundle);
	}
	public void setCodeBundle(CodeBundle cb)
	{
		actionList.removeAll();
		((DefaultListModel)actionList.getModel()).clear();
		codeBundle = cb;
		actionPanel.removeAll();
		previousAction = null;
		ifClause.setText(cb.getIfClause());
		eventChooser.setType(cb.getEventType());
		oncProcess.setSelectedItem(cb.getProcessDeclaration());
		oncEvent.setSelectedItem(cb.getEventDeclaration());
		oncMethod.setSelectedItem(cb.getMethodDeclaration());
		Iterator it = cb.getActionList().iterator();
		while(it.hasNext())
		{
			((DefaultListModel) actionList.getModel()).addElement(it.next());
		}
		eventTypeChanged();
	}
	public Object getValue()
	{
		return(codeBundle);
	}
	public void edit(Object obj)
	{
		if(obj instanceof CodeBundle)
			edit((CodeBundle) obj);
	}
	public void edit(CodeBundle cb)
	{
//		save();
		setCodeBundle(cb);
	}
	private void eventChanged()
	{
		Object event = oncEvent.getSelectedItem();
		if(event.equals(addEvent))
		{
			String eventName = InputDialog.getValue("Enter an event name.");
			if(eventName != null && ! EventDeclaration.exists(eventName))
			{
				EventDeclaration oe = new EventDeclaration(eventName);
				oncEvent.insertItemAt(oe, oncEvent.getItemCount() - 1);
				oncEvent.setSelectedItem(oe);
				oe.update();
			}
			else
				oncEvent.setSelectedItem(blankLabel);
		}
	}

	private void displayAction(OncAction action)
	{
		if(action != previousAction)
		{
			save();
		//	System.out.println("displayAction before removeAll");
			actionPanel.removeAll();
			actionEditor = action.getEditor();
			actionPanel.add((Component)actionEditor, BorderLayout.CENTER);
			actionEditor.setCBParent(this);
			actionEditor.edit(action);
			actionPanel.revalidate();
			actionPanel.repaint();
			previousAction = action;
			//resize();
		}
	}
	
//	public void resize()
//	{
//		int pw = (int) getSize().getWidth()- 20;
//		int ph = (int) getSize().getHeight() - eventChooser.getHeight() - oncProcess.getHeight() - actionListLabelBox.getHeight() - ifClause.getHeight() - 20;
//		oncProcess.setPreferredSize(new Dimension(pw/3 - 5, 40));
// 		oncEvent.setPreferredSize(new Dimension(pw/3 - 5, 40));
//		actionListSP.setPreferredSize(new Dimension(pw, 100));
//		actionPanel.setPreferredSize(new Dimension(pw, ph - 120));
//		SwingUtil.revalidateAll(this);
//		repaint();
//	}
	private void processChanged()
	{
		Object process = oncProcess.getSelectedItem();
		if(process.equals(addProcess))
		{
			ProcessDeclaration op = OncProcessEditor.edit();
			if(op != null)
			{
				oncProcess.insertItemAt(op, oncProcess.getItemCount() - 1);
				oncProcess.setSelectedItem(op);
				op.update();
			}
			else
				oncProcess.setSelectedItem(blankLabel);
		}
		setMethodList();
	}
	private void setMethodList()
	{
		oncMethod.removeAllItems();
		oncMethod.addItem(blankLabel);
		Object process = oncProcess.getSelectedItem();
		if(process != null && process instanceof ProcessDeclaration)
		{
			Iterator it = ((ProcessDeclaration) process).getMethodList();
			while(it.hasNext())
			{
				Object meth = it.next();
				oncMethod.addItem(meth);
			}
		}
		oncMethod.addItem(addMethod);
	}
	private void methodChanged()
	{
		Object method = oncMethod.getSelectedItem();
		if(method != null && method.equals(addMethod))
		{

			Object proc = oncProcess.getSelectedItem();
			if(proc != null && proc instanceof ProcessDeclaration)
			{
				String methodName = InputDialog.getValue("Enter an event name.");
				if(methodName != null)
				{
					MethodDeclaration om = new MethodDeclaration(methodName);
					oncMethod.insertItemAt(om, oncMethod.getItemCount() - 1);
					oncMethod.setSelectedItem(om);
					((ProcessDeclaration) proc).addMethod(om);
					((ProcessDeclaration) proc).update();
				}
				else
					oncMethod.setSelectedItem(blankLabel);
			}
		}
	}
	private boolean isVariableAction(Object obj)
	{
		if(obj == null || !(obj instanceof OncAction))
			return(false);
		else
		{
			OncAction act = (OncAction) obj;
			if(act.getType() == ActionType.ADD_VARIABLE ||
				  act.getType() == ActionType.MODIFY_VARIABLE ||
				  act.getType() == ActionType.INSTANTIATE ||
				  act.getType() == ActionType.INIT_VARIABLE)
				return(true);
			else
				return(false);
		}
	}
	private void eventTypeChanged()
	{
		if(eventChooser.getType() == EventChooser.EVENT)
		{
			// check to make sure there are no actions dependent on a
			// specified process first.
/*			int answer = 0;
			boolean answerGiven = false;
			DefaultListModel listModel = (DefaultListModel) actionList.getModel();
			Object [] acts = listModel.toArray();
			for(int n=0; n<acts.length; n++)
			{
				OncAction act = (OncAction) acts[n];
				if(isVariableAction(act))
				{
					if(!answerGiven)
					{
						answer = Util.yesNoQuestion("Variable actions exist in this Code Bundle.\nThis operation will remove these actions.\nIs this okay?");
						answerGiven = true;
						if(answer == JOptionPane.YES_OPTION && isVariableAction(actionList.getSelectedValue()))
						{
							save();
							actionPanel.removeAll();
							actionPanel.revalidate();
							actionPanel.repaint();
						}
					}
					if(answer == JOptionPane.YES_OPTION)
					{
						listModel.removeElement(act);
						codeBundle.removeAction(act);
					}
				}
			}
*/
//			if(! answerGiven || (answerGiven && answer == JOptionPane.YES_OPTION))
//			{
				oncProcess.setVisible(true);
				eventLabel.setVisible(true);
				oncEvent.setVisible(true);
				oncMethod.setVisible(false);
//			}
//			else
//				eventChooser.setType(EventChooser.PROCESS);
		}
		else
		{
			oncEvent.setVisible(false);
			eventLabel.setVisible(false);
			oncProcess.setVisible(true);
			oncMethod.setVisible(true);
		}

	}
	private void actionChanged()
	{
		Object act = actionList.getSelectedValue();
		if(act instanceof OncAction)
		{
			displayAction((OncAction) act);
		}
	}
	public void removeAction(OncAction act)
	{
		actionList.removeElement(act);
		codeBundle.removeAction(act);

		
		if(previousAction.equals(act))
		{
			previousAction = null;
			actionPanel.removeAll();
			actionEditor = null;
		}

		actionPanel.repaint();

	}
	public void save()
	{
		if(codeBundle != null)
		{
		//	System.out.println("Save codebundle");
		//	oncotcap.util.ForceStackTrace.showStackTrace();
			codeBundle.setProcessDeclaration(oncProcess.getSelectedItem());
			codeBundle.setEventDeclaration(oncEvent.getSelectedItem());
			codeBundle.setMethodDeclaration(oncMethod.getSelectedItem());
			codeBundle.setEventType(eventChooser.getType());
			codeBundle.setIfClause(ifClause.getText());
			codeBundle.clearActions();
			if(actionEditor != null) {
			//	System.out.println("saving immediate after clearing actions");
				actionEditor.save();
			}
			Object [] acts = ((DefaultListModel) actionList.getModel()).toArray();
			for(int n=0; n<acts.length; n++)
			{
				codeBundle.addAction((OncAction) acts[n]);
			//	System.out.println("updating action " 
			//		+ ((OncAction)acts[n]).getGUID());
 				((OncAction)acts[n]).update();
			}
		}
	}
	public void setTemplatePanel(StatementTemplateAndParametersPanel stpp)
	{
		this.stpp = stpp;
	}
	private class ResizeListener implements ComponentListener
	{
		public void componentHidden(ComponentEvent e) {}
		public void componentMoved(ComponentEvent e) {}
		public void componentResized(ComponentEvent e)
		{
			//resize();
		}
		public void componentShown(ComponentEvent e) {}

	}

	class AddActionListener extends HyperListener
	{
		public void mouseActivated(MouseEvent e)
		{
			OncAction action = null;
			if(!(oncProcess.getSelectedItem() instanceof ProcessDeclaration))
			{
				OncMessageBox.showError("Select a process first.", "Add Action");
				return;
			}
			else if(eventChooser.getType() == EventChooser.EVENT &&  ! (oncEvent.getSelectedItem() instanceof EventDeclaration))
			{
				OncMessageBox.showError("Select an Event first.", "Add Action");
				return;
			}
			else if(eventChooser.getType() == EventChooser.PROCESS && ! (oncMethod.getSelectedItem() instanceof MethodDeclaration ))
			{
				OncMessageBox.showError("Select a method first.", "Add Action");
				return;
			}
			save();
			ActionType type = ActionType.typeChooser(eventChooser.getType());
			if(type != null)
			{
				if(type == ActionType.ADD_VARIABLE)
				{
					DeclareVariable var = VariableType.varChooser();
					if(var != null)
					{
						//TODO: get action based on variable type
						//"initial variable action editors" need to be based on a
						//common sub-class just like modify variable actions
						action = type.getStorageInstance();
						((AddVariableAction) action).setVariable(var);
					}
					else
					{
						return;
					}
				}
				else if(type == ActionType.MODIFY_VARIABLE)
				{
					if(!(oncProcess.getSelectedItem() instanceof ProcessDeclaration))
					{
						OncMessageBox.showError("Select a process first.", "Modify Variable");
						return;
					}
					else
					{
						ProcessDeclaration proc = (ProcessDeclaration) oncProcess.getSelectedItem();
						if(proc != null)
						{
							VariableChooser vc = new VariableChooser(proc, (CodeBundle) getValue(), false);
							vc.setVisible(true);
							action = new ModifyVariableAction();
							vc.addVariableListener(((ModifyVariableAction) action).getChooserListener());
						}
/*						Vector vars = proc.findNonEnumVariables();
						
						if(vars.size() <= 0)
						{
							OncMessageBox.showError("The selected process has no variables.", "Modify Variable");
							return;
						}
						else
						{
							Object var = ListDialog.getValue("Choose a variable.", vars);
							if(var == null)
								return;
							else if (var instanceof DeclareVariable)
							{
								//replace action here with a specific modify
								//variable action for this variable type
								action = ((DeclareVariable) var).getType().getModifierStorageInstance();
								((ModifyVariableAction) action).setVariable((DeclareVariable) var);
							}
							else if (var instanceof VariableDefinition)
							{
								DeclareVariable v2 = ((VariableDefinition) var).getDeclarationInstance();
								v2.setName(((VariableDefinition) var).getName());
								action = v2.getType().getModifierStorageInstance();
								((ModifyVariableAction) action).setVariable(v2);
							}
							else
								return;
						} */
					}
				}
				else if(type == ActionType.INIT_VARIABLE)
				{
					if(!(oncProcess.getSelectedItem() instanceof ProcessDeclaration))
					{
						OncMessageBox.showError("Select a process first.", "Initialize Variable");
						return;
					}
					else
					{
						ProcessDeclaration proc = (ProcessDeclaration) oncProcess.getSelectedItem();
						if(proc != null)
						{
							VariableChooser vc = new VariableChooser(proc, (CodeBundle) getValue(), false, varListBeginningObjects);
							vc.setVisible(true);
							action = new InitVariableAction();
							vc.addVariableListener(((InitVariableAction) action).getChooserListener());
						}
					}
				}
				else if(type == ActionType.INSTANTIATE)
				{
					if(! (oncProcess.getSelectedItem() instanceof ProcessDeclaration))
					{
						OncMessageBox.showError("Select a process first.", "Instantiate Process");
						return;
					}
					Vector pList = new Vector();
					//pList.add(oncProcess);
					pList.addAll(ProcessDeclaration.getChildrenProcesses((ProcessDeclaration) oncProcess.getSelectedItem()));
					pList.add(addProcess);
					Object proc = ListDialog.getValue("Choose a process to instantiate.", pList);
					if(proc != null)
					{
						if(proc.equals(addProcess))
						{
							ProcessDeclaration op = OncProcessEditor.edit();
							if(op != null)
							{
								oncProcess.insertItemAt(op, oncProcess.getItemCount() - 1);
								op.update();
								action = new InstantiateAction(op);
							}
							else
								return;
						}
						else if(proc instanceof ProcessDeclaration)
						{
							action = new InstantiateAction((ProcessDeclaration) proc);
						}
						else
							return;
					}
					else
						return;
				}
				else if(type == ActionType.MODIFY_SCHEDULE)
				{
					Vector allScheds = ScheduleEventAction.getAllSchedules();
					if(allScheds.size() <= 0)
					{
						OncMessageBox.showWarning("No schedules exist in the current context.", "Modify Schedule");
						return;
					}
					else
					{
						Object sched = ListDialog.getValue("Choose a schedule to modify.", ScheduleEventAction.getAllSchedules());
						if(sched != null && sched instanceof ScheduleEventAction)
						{
							action = new ModifyScheduleAction((ScheduleEventAction) sched);
						}
						else
							return;
					}							
				}
				else
					action = type.getStorageInstance();

				((DefaultListModel) actionList.getModel()).addElement(action);
				action.setCodeBundleContainingMe(codeBundle);
				displayAction(action);
				actionList.setSelectedValue(action, true);

			}
		}
	}
	class RemoveActionListener extends HyperListener
	{
		public void mouseActivated(MouseEvent e)
		{
			int row = actionList.getSelectedIndex();
			OncAction act = (OncAction) actionList.getSelectedValue();
			if(row >= 0 && row <= actionList.getModel().getSize())
			{
				((DefaultListModel) actionList.getModel()).remove(row);
				actionPanel.removeAll();
				codeBundle.removeAction(act);
				actionEditor = null;
				actionPanel.repaint();
			}
		}
	}
	class EventTypeChanged implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			eventTypeChanged();
		}
	}

	static class TestButtons extends JFrame
	{
		TestButtons()
		{
			setSize(300,100);
			setLocation(800,0);
			getContentPane().setLayout(new FlowLayout());
			JButton test1 = new JButton("");
			JButton test2 = new JButton("Save");
			test1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					Iterator acts = editPanel.codeBundle.getActionList().iterator();
				}
			}
			);
			test2.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					editPanel.save();
					editPanel.codeBundle.update();
					DataSourceStatus.getDataSource().commit();
				}
			}
			);

			getContentPane().add(test1);
			getContentPane().add(test2);
			setVisible(true);
		} 
	}//end class TestButtons

	static int setCount = 0;
	class ComboDisplayLabel implements ListCellRenderer
	{
		JLabel rLabel = new JLabel();
		public Component getListCellRendererComponent(	JList list,
																		Object value,
																		int index,
																		boolean isSelected,
																		boolean cellHasFocus)
		{
			rLabel.setOpaque(true);
			rLabel.setForeground(Color.black);
			rLabel.setBackground(Color.lightGray);
			if(value != null)
			{
				if(value instanceof EventDeclaration)
				{
					EventDeclaration oe = (EventDeclaration) value;
					if(oe.isProcessEvent())
						rLabel.setForeground(Color.red);
				}
				else if(value.equals(addEvent))
					rLabel.setForeground(Color.blue);
				rLabel.setText(value.toString());
			}
			if(isSelected) rLabel.setBackground(TcapColor.lightBlue);
			if(cellHasFocus) rLabel.setBackground(TcapColor.lightBlue);
			return(rLabel);
		}
	}
	class ActionChangedListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			actionChanged();
		}
	}
}
class MyLabel extends JLabel
{

	public void setBackground(Color col)
	{
		super.setBackground(Color.blue);
	}
}
