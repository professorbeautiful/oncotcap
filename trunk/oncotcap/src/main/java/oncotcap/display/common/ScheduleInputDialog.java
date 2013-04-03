package oncotcap.display.common;

import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import oncotcap.util.*;
import oncotcap.process.treatment.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;

public class ScheduleInputDialog extends UserInputDialog implements ItemListener
{

	JTextField duration;
	JTextField nCourses;
	JTextField txtDayList;
	CheckBoxRow checkBoxRow;
	JPanel pan;
	
	
	boolean programmaticUpdate = false;
	
	public static void main(String [] args)
	{
		ScheduleInputDialog sd = new ScheduleInputDialog(null, null, null);
	}
	public ScheduleInputDialog(JFrame frame, Observer observer, Parameter parameter)
	{
		super(frame, observer, parameter);
		if(parameter.getParameterType() != ParameterType.SCHEDULE)
			Logger.log("ERROR: Wrong parameter type for ScheduleInputDialog.");

		parameter = null;
	}
	Object [] prompt()
	{
		Object[] objs = new Object[1];
		objs[0] = pan;
		return (objs);
	}
	public void setupEntry()
	{		
		pan = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		duration = new JTextField();
		nCourses = new JTextField();
		nCourses.setPreferredSize(new Dimension(100, (int)nCourses.getPreferredSize().getHeight()));
		nCourses.setSize(100, (int) nCourses.getPreferredSize().getHeight());
		txtDayList = new JTextField();
		checkBoxRow = new CheckBoxRow();
		checkBoxRow.addItemListener(this);
		pan.setLayout(gb);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.fill = GridBagConstraints.NONE;
		duration.setMaximumSize(new Dimension(50, (int) duration.getPreferredSize().getHeight()));
		duration.setMinimumSize(new Dimension(50, (int) duration.getPreferredSize().getHeight()));
		duration.setPreferredSize(new Dimension(50, (int) duration.getPreferredSize().getHeight()));
		addComponent(duration, gb, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH;
		addComponent(new JLabel("  Enter the course duration in days."), gb, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		nCourses.setMaximumSize(new Dimension(50, (int) duration.getPreferredSize().getHeight()));
		nCourses.setMinimumSize(new Dimension(50, (int) duration.getPreferredSize().getHeight()));
		nCourses.setPreferredSize(new Dimension(50, (int) duration.getPreferredSize().getHeight()));
		addComponent(nCourses, gb, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		addComponent(new JLabel("  Enter the number of courses that should be administered."), gb, gbc);
		addComponent(new JLabel(" "), gb, gbc);
		addComponent(new JLabel("Specify the days of treatment by:"), gb, gbc);
		addComponent(new JLabel("  entering comma-separated values like 1,3,5,7 or like Q2x4@1 or both"), gb, gbc);
		addComponent(new JLabel("  or by checking the day below"), gb, gbc);
		addComponent(txtDayList, gb, gbc);
		gbc.anchor = GridBagConstraints.WEST;
		addComponent(checkBoxRow, gb, gbc);


		if(parameter != null)
		{
/*TODO				txtDayList.setText(parameter.getPrimitive("TreatmentDays").toString());
				updateBoxesFromText();
				duration.setText(parameter.getPrimitive("CourseDuration").toString());
				setDuration();
				nCourses.setText(parameter.getPrimitive("NumberOfCourses").toString());
				*/
		}


		
		txtDayList.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				updateBoxesFromText();
			}
			public void changedUpdate(DocumentEvent e) {
				updateBoxesFromText();
			}
			public void removeUpdate(DocumentEvent e) {
				updateBoxesFromText();
			}
			public void documentChanged(DocumentEvent e) {
				updateBoxesFromText();
			}
		});
		duration.addFocusListener(new FocusListener()
		{
			public void focusLost(FocusEvent e) {
				setDuration();
			}
			public void focusGained(FocusEvent e) {

			}
		});
	}

	private void setDuration()
	{
		int dur;
		try
		{
			dur = new Integer(duration.getText()).intValue();
			checkBoxRow.setDays(dur);
			txtDayList.setText(checkBoxRow.toString());
			pan.revalidate();
			getContentPane().validate();
			setSize((int)pan.getPreferredSize().getWidth() + 90, (int) pan.getPreferredSize().getHeight()+92);
			repaint();
		}
		catch(NumberFormatException e){}
	}
	private void updateBoxesFromText()
	{
		programmaticUpdate = true;
		checkBoxRow.set(txtDayList.getText());
		programmaticUpdate = false;
	}
	private void addComponent(Component c, GridBagLayout gridBag, GridBagConstraints gbc)
	{
		gridBag.setConstraints(c, gbc);
		pan.add(c);
	}
	public void save()
	{
/*TODO		if(parameter == null)
			parameter = new Parameter(ParameterType.SCHEDULE);
		parameter.setPrimitive("CourseDuration", duration.getText(), PrimitiveType.INTEGER);
		parameter.setPrimitive("NumberOfCourses", nCourses.getText(), PrimitiveType.INTEGER);
		parameter.setPrimitive("TreatmentDays", txtDayList.getText(), PrimitiveType.DAY_LIST); */
	}
/*	OncVariable inputValue()
	{
		if(valueMap != null)
		{
			valueMap.setVariable("CourseDuration", duration.getText() );
			valueMap.setVariable("NumberOfCourses", nCourses.getText());
			valueMap.setVariable("ListOfDayNumbers", txtDayList.getText());
		}
		return(variable);
	}
*/	
	public boolean validateInput()
	{
		try {
			Integer i = new Integer(duration.getText());
			if ( i.intValue() < 7 || i.intValue() > 84) {
				JOptionPane.showMessageDialog(
											  optionPane,
											  "The duration is out of range.\nPlease enter a number between\n7 and 84.\n",
											  "Please try again",
											  JOptionPane.ERROR_MESSAGE);
				return(false);
			}
		}
		catch (NumberFormatException e){
			JOptionPane.showMessageDialog(
										  optionPane,
										  "Please input an integer value for duration.\n",
										  "Please try again",
										  JOptionPane.ERROR_MESSAGE);
			return(false);
		}
		try {
			Integer i = new Integer(nCourses.getText());
			if ( i.intValue() < 1) {
				JOptionPane.showMessageDialog(
											  optionPane,
											  "The number of courses is out of range.\nPlease enter a number greater than or equal to 1.\n",
											  "Please try again",
											  JOptionPane.ERROR_MESSAGE);
				return(false);
			}
		}
		catch (NumberFormatException e){
			JOptionPane.showMessageDialog(
										  optionPane,
										  "Please input an integer value for the number of courses.\n",
										  "Please try again",
										  JOptionPane.ERROR_MESSAGE);
			return(false);
		}
		String dayString = new String(txtDayList.getText());
		DayList daylist = null;
		if(dayString == null || dayString.trim().equals(""))
		{
			JOptionPane.showMessageDialog(
										  optionPane,
										  "Sorry, invalid list of treatment days.\n",
										  "Please try again",
										  JOptionPane.ERROR_MESSAGE);
			return(false);
		}
		try {
			daylist = new DayList (dayString);
		} catch (NumberFormatException e){
			JOptionPane.showMessageDialog(
										  optionPane,
										  "Sorry, invalid list of treatment days.\n",
										  "Please try again",
										  JOptionPane.ERROR_MESSAGE);
			return(false);
		}
		catch (Throwable e){
			if ( e.toString().equals("Q-notation not yet implemented"))
				JOptionPane.showMessageDialog(
											  optionPane,
											  "Q-notation not implemented??",
											  "Please try again",
											  JOptionPane.ERROR_MESSAGE);
			else Logger.log("DayListInputDialog: Don't know this Throwable " + e);
			return(false);
		}
		return(true);
	}
	public void itemStateChanged(ItemEvent e)
	{
		programmaticUpdate = true;
		txtDayList.setText(checkBoxRow.toString());
		programmaticUpdate = false;
	}
	public Parameter getValue()
	{
		if(validateInput())
		{
			save();
			return(parameter);
		}
		else
			return(null);
	}
}