package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.*;

import oncotcap.datalayer.persistible.action.*;
import oncotcap.display.common.LabeledComboBox;
import oncotcap.display.common.LabeledTextBox;
import oncotcap.display.common.OncTimeEditor;

public class ModifyScheduleEditor extends EditorPanel implements ActionEditor
{
	public static final String blankLabel = new String("");
	public static final ModifyScheduleAction.ModifyScheduleType DELAY_START = ModifyScheduleAction.DELAY_START;
	public static final ModifyScheduleAction.ModifyScheduleType ADVANCE_START = ModifyScheduleAction.ADVANCE_START;
	public static final ModifyScheduleAction.ModifyScheduleType PLACE_ON_HOLD = ModifyScheduleAction.PLACE_ON_HOLD;
	public static final ModifyScheduleAction.ModifyScheduleType RESUME = ModifyScheduleAction.RESUME;
	public static final ModifyScheduleAction.ModifyScheduleType DELAY_END = ModifyScheduleAction.DELAY_END;
	public static final ModifyScheduleAction.ModifyScheduleType ADVANCE_END = ModifyScheduleAction.ADVANCE_END;
	public static final ModifyScheduleAction.ModifyScheduleType RECURRENCE_CHANGE = ModifyScheduleAction.RECURRENCE_CHANGE;

	private LabeledComboBox type;
	private OncTimeEditor time;
	private JLabel schedule;
	private JLabel description;
	private ModifyScheduleAction modifySchedule;
	private LabeledTextBox nTimes;
	private CodeBundleEditorPanel cbParent;
		
	public ModifyScheduleEditor()
	{
		init();
	}
	public ModifyScheduleEditor(ModifyScheduleAction modSched)
	{
		init();
		edit(modSched);
	}
	private void init()
	{
		Box modLine = Box.createHorizontalBox();
		type = new LabeledComboBox("Modification Type");
		type.setMaximumSize(new Dimension(200,40));
		type.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				typeBoxChanged();
			}
		});
		time = new OncTimeEditor("");
		time.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

		nTimes = new LabeledTextBox(" ");
		nTimes.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
		nTimes.setVisible(false);
		
		schedule = new JLabel();
		description = new JLabel();
		modLine.add(description);
		modLine.add(time);
		modLine.add(nTimes);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(schedule);
		add(Box.createVerticalStrut(5));
		add(type);
		add(Box.createVerticalStrut(5));
		add(modLine);
	}
	public void edit(Object obj)
	{
		if(obj instanceof ModifyScheduleAction)
			edit((ModifyScheduleAction) obj);
	}
	public void edit(OncAction act)
	{
		if(act instanceof ModifyScheduleAction)
			edit((ModifyScheduleAction) act);
	}
	public void edit(ModifyScheduleAction modSched)
	{
		modifySchedule = modSched;
		if(modSched != null)
		{
			fillTypeBox(modSched);
			if(modSched.getModificationType() != null)
			{
				ModifyScheduleAction.ModifyScheduleType modType = modSched.getModificationType();
				type.setSelectedItem(modType);
				description.setText(modType.getDescription());
			}
			if(modSched.getScheduleToModify() != null)
			{
				schedule.setText("Modifying schedule: " + modSched.getScheduleToModify());
			}
			if(modSched.getTime() != null)
				time.edit(modSched.getTime());
			nTimes.setText(Integer.toString(modSched.getNTimesChange()));
		}
		else
		{
			type.setSelectedItem(blankLabel);
		}
		typeBoxChanged();
		repaint();
	}
	public Object getValue()
	{
		return(modifySchedule);
	}
	private void fillTypeBox(ModifyScheduleAction modSched)
	{
		ScheduleEventAction schedEvent = modSched.getScheduleToModify();
		type.removeAllItems();
		type.addItem(blankLabel);
		if(schedEvent != null)
		{
			type.addItem(ModifyScheduleAction.PLACE_ON_HOLD);
			type.addItem(ModifyScheduleAction.RESUME);
			if(schedEvent.getScheduleStartType() != null)
			{
				if(schedEvent.getScheduleStartType() == ScheduleEventAction.FUTURE)
				{
					type.addItem(ModifyScheduleAction.DELAY_START);
					type.addItem(ModifyScheduleAction.ADVANCE_START);
				}
			}
			if(schedEvent.getRecur())
			{
				type.addItem(ModifyScheduleAction.RECURRENCE_CHANGE);
				if(schedEvent.getScheduleEndType() != null)
				{
					if(schedEvent.getScheduleEndType() == ScheduleEventAction.X_TIMES)
					{
						type.addItem(ModifyScheduleAction.EXTEND_NO_OF_TIMES);
						type.addItem(ModifyScheduleAction.REDUCE_NO_OF_TIMES);
					}
					else if(schedEvent.getScheduleEndType() == ScheduleEventAction.AFTER_PERIOD)
					{
						type.addItem(ModifyScheduleAction.DELAY_END);
						type.addItem(ModifyScheduleAction.ADVANCE_END);
					}
				}
			}
		}
	}
	private void typeBoxChanged()
	{
		Object mType = type.getSelectedItem();
		if(mType instanceof ModifyScheduleAction.ModifyScheduleType)
		{
			ModifyScheduleAction.ModifyScheduleType modType = (ModifyScheduleAction.ModifyScheduleType) mType;
			time.setVisible(modType.isTimeRequired());
			nTimes.setVisible(modType.isNTimesRequired());
			description.setText(modType.getDescription());
			description.setVisible(true);
		}
		else
		{
			time.setVisible(false);
			description.setVisible(false);			
		}
		
	}
	public void save()
	{
		if(modifySchedule != null)
		{
			if(type.getSelectedItem() instanceof ModifyScheduleAction.ModifyScheduleType)
				modifySchedule.setModificationType((ModifyScheduleAction.ModifyScheduleType) type.getSelectedItem());
			modifySchedule.setTime(time.getTime());
			try{modifySchedule.setNTimesChange(Integer.parseInt(nTimes.getText()));}
			catch(NumberFormatException e){modifySchedule.setNTimesChange(0);}
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