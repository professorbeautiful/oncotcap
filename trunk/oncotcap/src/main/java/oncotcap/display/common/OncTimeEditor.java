package oncotcap.display.common;

import javax.swing.*;
import java.awt.Dimension;
import oncotcap.util.OncTime;
import oncotcap.util.StringHelper;

public class OncTimeEditor extends JPanel
{
	public static Orientation HORIZONTAL_ORIENTATION = new Orientation();
	public static Orientation VERTICAL_ORIENTATION = new Orientation();
	private LabeledComboBox unit;
	private LabeledTextBox txtTime;
	private OncTime time;
	
	public OncTimeEditor(String title)
	{
		init(title, HORIZONTAL_ORIENTATION);
	}
	public OncTimeEditor(String title, Orientation orient)
	{
		init(title, orient);
	}
	public OncTimeEditor(String title, OncTime time)
	{
		init(title, HORIZONTAL_ORIENTATION);
		edit(time);
	}
	public OncTimeEditor(String title, OncTime time, Orientation orient)
	{
		init(title, orient);
		edit(time);
	}
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		OncTime ot = new OncTime(12, OncTime.MONTH);
		OncTimeEditor ote = new OncTimeEditor("asfd" , ot);
		jf.setSize(200,200);
		jf.getContentPane().add(ote);
		jf.setVisible(true);
	}
	private void init(String title, Orientation orient)
	{
		int layoutOrient;
		if(orient == HORIZONTAL_ORIENTATION)
			layoutOrient = BoxLayout.X_AXIS;
		else
			layoutOrient = BoxLayout.Y_AXIS;
		setLayout(new BoxLayout(this, layoutOrient));
		txtTime = new LabeledTextBox(" ");
		txtTime.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
		if(title == null || title.equals(""))
			title = " ";
		unit = new LabeledComboBox(title, OncTime.getTimeUnits());
		unit.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
		add(txtTime);
		add(unit);
	}
	public static class Orientation
	{
		private Orientation(){}
	}
	public void edit(OncTime time)
	{
		if(time != null)
		{
			this.time = time;
			if(time.getTimeUnit() != null)
				unit.setSelectedItem(time.getTimeUnit());
			txtTime.setText(time.getTimeString());
		}
	}
	public OncTime getTime()
	{
		if(time == null) time = new OncTime();
		save();
		return(time);
	}
	public void save()
	{
		if(time != null)
		{
			String strTime = txtTime.getText();
			time.setTime(strTime);
			time.setUnit((OncTime.TimeUnit) unit.getSelectedItem());
		}
	}
}