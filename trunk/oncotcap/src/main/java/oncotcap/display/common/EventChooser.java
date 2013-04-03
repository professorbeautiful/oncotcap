package oncotcap.display.common;

import javax.swing.*;
import java.awt.event.*;

public class EventChooser extends JPanel
{
	public static final int PROCESS = 1;
	public static final int EVENT = 2;
	public static final int DECLARATION_SECTION = 8;
	
	private Box eventTypeBox;
	private JRadioButton chooseEvent = new JRadioButton("Event");
	public JRadioButton chooseProcess = new JRadioButton("Process");
	private ButtonGroup eventTypeGroup = new ButtonGroup();

	public EventChooser()
	{
		init();
	}

	private void init()
	{
		chooseProcess.setSelected(true);
		eventTypeGroup.add(chooseProcess);
		eventTypeGroup.add(chooseEvent);
		eventTypeBox = Box.createHorizontalBox();
		eventTypeBox.add(chooseProcess);
		eventTypeBox.add(chooseEvent);
		add(eventTypeBox);
	}
	public void addActionListener(ActionListener list)
	{
		chooseEvent.addActionListener(list);
		chooseProcess.addActionListener(list);
	}
	public void setType(int type)
	{
		if(type == PROCESS)
			chooseProcess.setSelected(true);
		else
			chooseEvent.setSelected(true);
	}
	public int getType()
	{
		if(chooseProcess.isSelected())
			return(PROCESS);
		else
			return(EVENT);
	}
}