package oncotcap.display.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import oncotcap.util.*;
import oncotcap.process.treatment.DayList;

public class CheckBoxRow extends JPanel implements ItemSelectable {
	private static final int HORSPACE = 20;
	private static final int VERTSPACE = 20;
	private static final int CHECKSPERROW = 28;
	private static final int MAXCHECKS = 84;
	JCheckBox [] dayboxes;
	JLabel [] daylabels;
	int nBoxes = 0;
	Vector listeners = new Vector();
	CheckBoxRow me;
	CheckBoxListener checkBoxListener = new CheckBoxListener();
	public CheckBoxRow(){
		me = this;
		setSize(550, 40);
		setLocation(20,200);
		setLayout(null);
		dayboxes = (JCheckBox[])Util.newArray("javax.swing.JCheckBox", MAXCHECKS);
		daylabels = (JLabel[])Util.newArray("javax.swing.JLabel", MAXCHECKS);
		setDays(28);
//		setBackground(Color.red);
		setVisible(true);
	}
	public void setDays(int days) throws IndexOutOfBoundsException
	{
		int i;
		if(days >= 7 && days <= MAXCHECKS)
		{
			if(days > nBoxes)
			{
				for (i=nBoxes; i<days; i++)
					addCheckBox(i);
			}
			else if(days < nBoxes)
			{
				for (i = nBoxes - 1; i >= days; i--)
					removeCheckBox(i);
			}
			nBoxes = days;
			setPreferredSize(new Dimension(HORSPACE * CHECKSPERROW, ((days / (CHECKSPERROW + 1)) + 1) * 2 * VERTSPACE));
		}
		else throw(new IndexOutOfBoundsException("Check box row range is 7 to " + MAXCHECKS + " days. [CheckBoxRow]"));
		revalidate();
		repaint();
	}
	private void addCheckBox(int index)
	{
		int row = index / CHECKSPERROW;
		dayboxes[index] = new JCheckBox();
		dayboxes[index].setLocation(HORSPACE * (index % 28), row * 2 * VERTSPACE);
		dayboxes[index].setSize(dayboxes[index].getPreferredSize());
		add(dayboxes[index]);
		daylabels[index] = new JLabel(String.valueOf(index+1));
		daylabels[index].setLabelFor(dayboxes[index]);
		daylabels[index].setLocation(HORSPACE * (index % 28), (row * 2 + 1) * VERTSPACE);
		daylabels[index].setSize(daylabels[index].getPreferredSize());
		add(daylabels[index]);
		((JCheckBox)(dayboxes[index])).addMouseListener(checkBoxListener);
		((JCheckBox)(dayboxes[index])).addChangeListener(checkBoxListener);
	}
	private void removeCheckBox(int index)
	{
		dayboxes[index].removeMouseListener(checkBoxListener);
		remove(daylabels[index]);
		remove(dayboxes[index]);
		daylabels[index] = null;
		dayboxes[index] = null;
	}
	private void addComponent(Component c, GridBagLayout gridBag, GridBagConstraints gbc)
	{
		gridBag.setConstraints(c, gbc);
		add(c);
	}

	public String toString()
	{
		String s = new String("");
		for (int i=0; i<nBoxes; i++){
			if (dayboxes[i].isSelected()){
				if(s.length()!=0)
					s = s.concat(", ");
				s = s.concat(daylabels[i].getText());
			}
		}
		return(s);
	}

	public void set(String txtUpdate)
	{
		String s = "Not initialized";
		try {
			for (int i=0; i<nBoxes; i++)
				dayboxes[i].setSelected(false);
			DayList daylist = new DayList(txtUpdate);
			Iterator iter = daylist.iterator();
			while (iter.hasNext())
			{
				s = iter.next().toString();
				Integer iOb = new Integer(s);
				dayboxes[iOb.intValue()-1].setSelected(true);
			}
		}catch (java.lang.Throwable e){
			Logger.log("updateBoxesFromText:  s is " + s + " =>throwable:  " + e);
		}
	}
	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(null);
		frame.setSize(600,500);
		frame.getContentPane().add(new CheckBoxRow());
		frame.setVisible(true);
	}

	public void addItemListener(ItemListener listen)
	{
		listeners.add(listen);
	}
	public void removeItemListener(ItemListener listen)
	{
		listeners.remove(listen);
	}

	public Object [] getSelectedObjects()
	{
		return(null);
	}

	class CheckBoxListener implements MouseListener, ChangeListener
	{
		public void stateChanged(ChangeEvent e)
		{
		}
		public void mouseReleased(MouseEvent e)
		{
			Iterator iter = listeners.iterator();
			ItemListener callto;
			while(iter.hasNext())
			{
				callto = (ItemListener) iter.next();
				callto.itemStateChanged(new ItemEvent(me, 0, me, ItemEvent.SELECTED));
			}
		}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
	}

}

