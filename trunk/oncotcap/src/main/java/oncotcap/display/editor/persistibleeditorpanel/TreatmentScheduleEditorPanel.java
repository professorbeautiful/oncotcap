package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import oncotcap.util.*;
import oncotcap.process.treatment.*;
import oncotcap.datalayer.persistible.parameter.TreatmentSchedule;
import oncotcap.display.common.CheckBoxRow;
import oncotcap.display.common.ListDialog;
import oncotcap.display.editor.EditorDialog;

public class TreatmentScheduleEditorPanel extends EditorPanel implements ItemListener
{

	TreatmentSchedule schedule;
	JTextField duration;
	JTextField nCourses;
	JTextField txtDayList;
	CheckBoxRow checkBoxRow;
//	JPanel pan;
	
	
	boolean programmaticUpdate = false;
	
	public TreatmentScheduleEditorPanel()
	{
		init();
	}
	public static void main(String[] args)
	{
		Collection allTS = oncotcap.Oncotcap.getDataSource().find(ReflectionHelper.classForName("oncotcap.datalayer.persistible.TreatmentSchedule"));
		TreatmentSchedule schedToEdit;
		int answer = Util.yesNoQuestion("Edit an existing treatement schedule?");
		if(answer == JOptionPane.YES_OPTION)
		{
			if(allTS.isEmpty())
			{
				OncMessageBox.showWarning("No treatment schedules exist in the current project.", "TreatmentScheduleEditorPanel");
				return;
			}
			Object bndl = ListDialog.getValue("Choose a Treatement Schedule.", allTS);
			if(bndl == null)
				return;
			else if (bndl instanceof TreatmentSchedule)
			{
				schedToEdit = (TreatmentSchedule) bndl;
			}
			else
				return;			
		}
		else
			schedToEdit = new TreatmentSchedule();

		EditorDialog.showEditor(schedToEdit);
	}
	public void init()
	{		
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		duration = new JTextField();
		nCourses = new JTextField();
		nCourses.setPreferredSize(new Dimension(100, (int)nCourses.getPreferredSize().getHeight()));
		nCourses.setSize(100, (int) nCourses.getPreferredSize().getHeight());
		txtDayList = new JTextField();
		checkBoxRow = new CheckBoxRow();
		checkBoxRow.addItemListener(this);
		setLayout(gb);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.fill = GridBagConstraints.NONE;
		duration.setMaximumSize(new Dimension(150, (int) duration.getPreferredSize().getHeight()));
		duration.setMinimumSize(new Dimension(150, (int) duration.getPreferredSize().getHeight()));
		duration.setPreferredSize(new Dimension(150, (int) duration.getPreferredSize().getHeight()));
		addComponent(duration, gb, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH;
		addComponent(new JLabel("  Enter the course duration in days."), gb, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		nCourses.setMaximumSize(new Dimension(150, (int) duration.getPreferredSize().getHeight()));
		nCourses.setMinimumSize(new Dimension(150, (int) duration.getPreferredSize().getHeight()));
		nCourses.setPreferredSize(new Dimension(150, (int) duration.getPreferredSize().getHeight()));
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

		setPreferredSize(new Dimension(600, 325));
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
	public void edit(Object obj)
	{
		if(obj instanceof TreatmentSchedule)
			edit((TreatmentSchedule) obj);
	}
	public void edit(TreatmentSchedule sched)
	{
		schedule = sched;
		if(sched != null)
		{
			if(sched.getDuration() != null)
				duration.setText(sched.getDuration());
			else
				duration.setText("");
			
			if(sched.getNumberOfCourses() != null)
				nCourses.setText(sched.getNumberOfCourses());
			else
				nCourses.setText("");
			
			if(sched.getDayList() != null)
				txtDayList.setText(sched.getDayList());
			else
				txtDayList.setText("");
		}
	}
	private void setDuration()
	{
		int dur;
		try
		{
			if(StringHelper.isInteger(duration.getText()))
			{
				dur = new Integer(duration.getText()).intValue();
				checkBoxRow.setDays(dur);
				txtDayList.setText(checkBoxRow.toString());
				revalidate();
				setSize((int) getPreferredSize().getWidth() + 90, (int) getPreferredSize().getHeight()+92);
				repaint();
			}
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
		add(c);
	}
	public void save()
	{
		schedule.setDuration(duration.getText());
		schedule.setNumberOfCourses(nCourses.getText());
		schedule.setDayList(txtDayList.getText());
	}
	public boolean check()
	{
/*		try {
			Integer i = new Integer(duration.getText());
			if ( i.intValue() < 7 || i.intValue() > 84) {
				JOptionPane.showMessageDialog(
											  null,
											  "The duration is out of range.\nPlease enter a number between\n7 and 84.\n",
											  "Please try again",
											  JOptionPane.ERROR_MESSAGE);
				return(false);
			}
		}
		catch (NumberFormatException e){
			JOptionPane.showMessageDialog(
										  null,
										  "Please input an integer value for duration.\n",
										  "Please try again",
										  JOptionPane.ERROR_MESSAGE);
			return(false);
		}
		try {
			Integer i = new Integer(nCourses.getText());
			if ( i.intValue() < 1) {
				JOptionPane.showMessageDialog(
											  null,
											  "The number of courses is out of range.\nPlease enter a number greater than or equal to 1.\n",
											  "Please try again",
											  JOptionPane.ERROR_MESSAGE);
				return(false);
			}
		}
		catch (NumberFormatException e){
			JOptionPane.showMessageDialog(
										  null,
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
										  null,
										  "Sorry, invalid list of treatment days.\n",
										  "Please try again",
										  JOptionPane.ERROR_MESSAGE);
			return(false);
		}
		try {
			daylist = new DayList (dayString);
		} catch (NumberFormatException e){
			JOptionPane.showMessageDialog(
										  null,
										  "Sorry, invalid list of treatment days.\n",
										  "Please try again",
										  JOptionPane.ERROR_MESSAGE);
			return(false);
		}
		catch (Throwable e){
			if ( e.toString().equals("Q-notation not yet implemented"))
				JOptionPane.showMessageDialog(
											  null,
											  "Q-notation not implemented??",
											  "Please try again",
											  JOptionPane.ERROR_MESSAGE);
			else Logger.log("DayListInputDialog: Don't know this Throwable " + e);
			return(false);
		}
		*/
		return(true);
	}
	public void itemStateChanged(ItemEvent e)
	{
		programmaticUpdate = true;
		txtDayList.setText(checkBoxRow.toString());
		programmaticUpdate = false;
	}
	public Object getValue()
	{
//		if(check())
//		{
			save();
			return(schedule);
//		}
//		else
//			return(null);
	}
}