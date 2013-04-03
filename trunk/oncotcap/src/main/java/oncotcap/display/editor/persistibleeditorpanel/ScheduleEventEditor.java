package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.display.common.HorizontalLine;
import oncotcap.display.common.LabeledComboBox;
import oncotcap.display.common.LabeledTextBox;
import oncotcap.display.common.OncTimeEditor;

public class ScheduleEventEditor extends TriggerEventEditor
{
	public ScheduleEventAction action;
	private LabeledComboBox scheduleStartType;
	private OncTimeEditor oncStartTime;
	private LabeledComboBox scheduleEndType;
	private OncTimeEditor oncEndTime;
	private LabeledTextBox numberOfTimesUntilEnd;
	private LabeledTextBox txtDayList;
	private JCheckBox recur;
	private JCheckBox dayList;
	private OncTimeEditor gapTime;
	
	public ScheduleEventEditor()
	{
		init2();
	}
	public ScheduleEventEditor(ScheduleEventAction action)
	{
		init2();
		edit(action);
	}
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		ScheduleEventEditor see = new ScheduleEventEditor();
		see.edit(new ScheduleEventAction());
		jf.getContentPane().add(see);
		jf.setSize(200,200);
		jf.setVisible(true);
	}
	private void init2()
	{
		name.setLabel("Name of this schedule");
		oncProcess.setLabel("Scheduled process");
		oncMethod.setLabel("Scheduled method");
		Box startTimeBox = Box.createHorizontalBox();
		scheduleStartType = new LabeledComboBox("Schedule start", ScheduleEventAction.getAllScheduleStartTypes());
		scheduleStartType.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
		scheduleStartType.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e)
																				{ startTypeChanged(); } });
		oncStartTime = new OncTimeEditor(" ");
		oncStartTime.setVisible(false);

		startTimeBox.add(scheduleStartType);
		startTimeBox.add(oncStartTime);

		Box recurBox = Box.createHorizontalBox();
		recur = new JCheckBox("Recurring");
		recur.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e)
										{ recurChanged(); } });
		dayList = new JCheckBox("Time List");
		dayList.setVisible(false);
		dayList.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e)
										  { recurChanged(); }});
		gapTime = new OncTimeEditor("Time interval");
		gapTime.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
		gapTime.setVisible(false);


		txtDayList = new LabeledTextBox("List of days");
		txtDayList.setVisible(false);
		txtDayList.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

		recurBox.add(recur);
		recurBox.add(dayList);
		recurBox.add(gapTime);
		recurBox.add(txtDayList);
		Box endTimeBox = Box.createHorizontalBox();
		
		scheduleEndType = new LabeledComboBox("Schedule end", ScheduleEventAction.getAllScheduleEndTypes());
		scheduleEndType.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
		scheduleEndType.setVisible(false);
		scheduleEndType.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e)
													 { endTypeChanged(); } });

		oncEndTime = new OncTimeEditor(" ");
		oncEndTime.setVisible(false);
												 
		numberOfTimesUntilEnd = new LabeledTextBox("Number of Times");
		numberOfTimesUntilEnd.setVisible(false);
		numberOfTimesUntilEnd.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
		
		endTimeBox.add(scheduleEndType);
		endTimeBox.add(numberOfTimesUntilEnd);
		endTimeBox.add(oncEndTime);
		
		add(Box.createVerticalStrut(10));
		add(startTimeBox);
		add(Box.createVerticalStrut(5));
		add(new HorizontalLine(3));
		add(Box.createVerticalStrut(5));
		add(recurBox);
		add(Box.createVerticalStrut(10));
		add(endTimeBox);
	}
	public void edit(Object action)
	{
		if(action instanceof ScheduleEventAction)
			edit((ScheduleEventAction) action);
	}
	public void edit(OncAction action)
	{
		if(action instanceof ScheduleEventAction)
			edit((ScheduleEventAction) action);
	}

	public void edit(ScheduleEventAction action)
	{
		super.edit(action);
		this.action = action;
		scheduleStartType.setSelectedItem(action.getScheduleStartType());
		oncStartTime.edit(action.getStartDelayTime());
		gapTime.edit(action.getGapTime());
		recur.setSelected(action.getRecur());
		scheduleEndType.setSelectedItem(action.getScheduleEndType());
		oncEndTime.edit(action.getEndDelayTime());
		if(action.getNumberOfTimesUntilEnd() != null)
			numberOfTimesUntilEnd.setText(action.getNumberOfTimesUntilEnd());
		else
			numberOfTimesUntilEnd.setText("");

		if(action.getDayList() != null)
			txtDayList.setText(action.getDayList());
		dayList.setSelected(action.isDayList());
		startTypeChanged();
		recurChanged();
		endTypeChanged();
		dayListChanged();
	}
	public Object getValue()
	{
		return(action);
	}
	private void startTypeChanged()
	{
		ScheduleEventAction.ScheduleStartType type = (ScheduleEventAction.ScheduleStartType) scheduleStartType.getSelectedItem();
		if(type == ScheduleEventAction.NOW)
			oncStartTime.setVisible(false);
		else
			oncStartTime.setVisible(true);
	}
	private void recurChanged()
	{
		if(recur.isSelected())
		{
			scheduleEndType.setVisible(true);
			gapTime.setVisible(true);
			dayList.setVisible(true);
			endTypeChanged();
			dayListChanged();
		}
		else
		{
			gapTime.setVisible(false);
			scheduleEndType.setVisible(false);
			oncEndTime.setVisible(false);
			numberOfTimesUntilEnd.setVisible(false);
			txtDayList.setVisible(false);
			dayList.setVisible(false);
		}
	}
	private void dayListChanged()
	{
		if(recur.isSelected() && dayList.isSelected())
		{
			txtDayList.setVisible(true);
			gapTime.setVisible(false);
			scheduleEndType.setVisible(false);
			oncEndTime.setVisible(false);
			numberOfTimesUntilEnd.setVisible(false);
		}
		else if( ! dayList.isSelected())
		{
			txtDayList.setVisible(false);
		}
	}
	private void endTypeChanged()
	{
		if(recur.isSelected())
		{
			ScheduleEventAction.ScheduleEndType type = (ScheduleEventAction.ScheduleEndType) scheduleEndType.getSelectedItem();
			if(type == ScheduleEventAction.FOREVER)
			{
				oncEndTime.setVisible(false);
				numberOfTimesUntilEnd.setVisible(false);
			}
			else if(type == ScheduleEventAction.X_TIMES)
			{
				oncEndTime.setVisible(false);
				numberOfTimesUntilEnd.setVisible(true);
			}
			else
			{
				numberOfTimesUntilEnd.setVisible(false);
				oncEndTime.setVisible(true);
			}
		}
	}
	public void save()
	{
		action.setName(name.getText());
		action.setProcessDeclaration(oncProcess.getSelectedItem());
		action.setEvent(oncEvent.getSelectedItem());
		action.setStartDelayTime(oncStartTime.getTime());
		action.setScheduleStartType(scheduleStartType.getSelectedItem());
		action.setGapTime(gapTime.getTime());
		action.setEventType(eventChooser.getType());
		action.setMethod(oncMethod.getSelectedItem());
		action.setRecur(recur.isSelected());
		action.setIsDayList(dayList.isSelected());
		action.setDayList(txtDayList.getText());
		if(scheduleEndType.getSelectedItem() instanceof ScheduleEventAction.ScheduleEndType)
			action.setScheduleEndType((ScheduleEventAction.ScheduleEndType) scheduleEndType.getSelectedItem());
		action.setEndDelayTime(oncEndTime.getTime());
		action.setNumberOfTimesUntilEnd(numberOfTimesUntilEnd.getText());
	}

}
