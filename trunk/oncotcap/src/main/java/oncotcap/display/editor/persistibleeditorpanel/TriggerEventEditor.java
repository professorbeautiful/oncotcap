package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.display.common.EventChooser;
import oncotcap.display.common.InputDialog;
import oncotcap.display.common.LabeledComboBox;
import oncotcap.display.common.LabeledTextBox;
import oncotcap.display.editor.OncProcessEditor;
import oncotcap.util.OncMessageBox;

public class TriggerEventEditor extends EditorPanel implements ActionEditor
{
	protected EventChooser eventChooser;
	protected LabeledComboBox oncEvent;
	protected LabeledComboBox oncProcess;
	protected LabeledComboBox oncMethod;
	protected LabeledTextBox name;

	protected TriggerEventAction action;

	private CodeBundleEditorPanel cbParent;
	
	private String blankLabel = "";
	private String addProcess = "Add a new Process";
	private String addEvent = "Add a new Event";
	private String addMethod = "Add a new Method";
	
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(400,300);
		jf.getContentPane().add(new TriggerEventEditor());
		jf.setVisible(true);
	}
	public TriggerEventEditor()
	{
		init();
	}
	public TriggerEventEditor(TriggerEventAction action)
	{
		init();
		this.action = action;
		edit(action);
	}

	private void init()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		name = new LabeledTextBox("Action Name");
		name.setMaximumSize(new Dimension(200,40));
		name.setPreferredSize(new Dimension(200,40));
		name.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		Box eventBox = Box.createHorizontalBox();
		eventChooser = new EventChooser();
		eventChooser.addActionListener(new EventTypeChanged());
		eventChooser.setMaximumSize(new Dimension(200, 25));
		
		oncEvent = new LabeledComboBox("Event", EventDeclaration.getAllEvents());
		oncEvent.insertItemAt(blankLabel, 0);
		oncEvent.addItem(addEvent);
		oncEvent.setMaximumSize(new Dimension(200, 45));
		oncEvent.setAlignmentY(JComponent.TOP_ALIGNMENT);
		oncProcess = new LabeledComboBox("Process", ProcessDeclaration.getAllProcesses());
		oncProcess.insertItemAt(blankLabel, 0);
		oncProcess.addItem(addProcess);
		oncProcess.setMaximumSize(new Dimension(200, 45));
		oncProcess.setAlignmentY(JComponent.TOP_ALIGNMENT);
		oncProcess.setVisible(false);
		oncMethod = new LabeledComboBox("Method");
		oncMethod.insertItemAt(blankLabel, 0);
		oncMethod.addItem(addMethod);
		oncMethod.setMaximumSize(new Dimension(200, 45));
		oncMethod.setAlignmentY(JComponent.TOP_ALIGNMENT);
		oncMethod.setVisible(false);

		
		eventBox.add(oncProcess);
		eventBox.add(oncEvent);
		eventBox.add(oncMethod);

		add(name);
		add(eventChooser);
		add(eventBox);
		
		oncEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventChanged();
			}
		});
		oncProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processChanged();
			}
		});
		oncMethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				methodChanged();
			}
		});
	}
	public void edit(Object action)
	{
		if(action instanceof TriggerEventAction)
			edit((TriggerEventAction) action);
	}
	public void edit(OncAction action)
	{
		if(action instanceof TriggerEventAction)
			edit((TriggerEventAction) action);
	}
	private void edit(TriggerEventAction action)
	{
		this.action = action;
		eventChooser.setType(action.getEventType());
		name.setText(action.getName());
		oncEvent.setSelectedItem(action.getEvent());
		oncProcess.setSelectedItem(action.getTriggeredProcessDeclaration());
		oncMethod.setSelectedItem(action.getMethod());
		eventTypeChanged();
	}
	public Object getValue()
	{
		return(action);
	}
	public void save()
	{
		if(action != null)
		{
			action.setName(name.getText());
			action.setEvent(oncEvent.getSelectedItem());
			action.setProcessDeclaration(oncProcess.getSelectedItem());
			action.setMethod(oncMethod.getSelectedItem());
			action.setEventType(eventChooser.getType());
			action.update();
		}
	}
	public ActionType getType()
	{
		return(ActionType.MODIFY_VARIABLE);
	}
	private void eventChanged()
	{
		oncProcess.setVisible(false);
		Object event = oncEvent.getSelectedItem();
		if(event != null)
		{
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
			else if(event instanceof EventDeclaration && ((EventDeclaration)event).isProcessEvent())
			{
				oncProcess.setVisible(true);
			}
		}
	}
	private void eventTypeChanged()
	{
		if(eventChooser.getType() == EventChooser.EVENT)
		{
			oncEvent.setVisible(true);
			oncProcess.setVisible(false);
			oncMethod.setVisible(false);
		}
		else
		{
			oncEvent.setVisible(false);
			oncProcess.setVisible(true);
			oncMethod.setVisible(true);
		}
	}
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
				String methodName = InputDialog.getValue("Enter a method name.");
				if(methodName != null)
				{
					MethodDeclaration om = new MethodDeclaration(methodName);
					oncMethod.insertItemAt(om, oncMethod.getItemCount() - 1);
					oncMethod.setSelectedItem(om);
					((ProcessDeclaration) proc).addMethod(om);
				}
				else
					oncMethod.setSelectedItem(blankLabel);
			}
			else
			{
				OncMessageBox.showWarning("Choose a process first.", "Add Method");
				oncMethod.setSelectedItem(blankLabel);
			}
		}
	}

	class EventTypeChanged implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			eventTypeChanged();
		}
	}
	
	public CodeBundleEditorPanel getCBParent()
	{
		return(cbParent);	
	}
	public void setCBParent(CodeBundleEditorPanel par)
	{
		cbParent = par;
	}
}

