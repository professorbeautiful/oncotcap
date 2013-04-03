package oncotcap.display.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import oncotcap.util.*;

public class FieldNavigator extends JPanel implements ActionListener
{
	private JButton btnPrevious = new JButton(OncoTcapIcons.getSmallLeftArrow());
	private JButton btnNext = new JButton(OncoTcapIcons.getSmallRightArrow());
	private JLabel current = new JLabel();
	private JLabel total = new JLabel();
	private JLabel description = new JLabel();
	private int value = 0;
	private int maxValue = 0;
	private OncObservable obs = new OncObservable();
	
	public FieldNavigator()
	{
		setLayout(new FlowLayout());
		//setBorder(BorderFactory.createLineBorder(Color.black));
		btnPrevious.setPreferredSize(new Dimension(17,17));
		btnNext.setPreferredSize(new Dimension(17,17));
		current.setText("0");
		current.setHorizontalAlignment(SwingConstants.CENTER);
		total.setText("of 0");
		current.setHorizontalAlignment(SwingConstants.CENTER);
		btnNext.setMnemonic('N');
		btnNext.addActionListener(this);
		btnPrevious.setMnemonic('P');
		btnPrevious.addActionListener(this);

		add(description);
		add(btnPrevious);
		add(current);
		add(btnNext);
		add(total);
	}

	public void addObserver(Observer o)
	{
		obs.addObserver(o);
	}
	
	public void clear()
	{
		maxValue = 0;
		value = 0;
		current.setText("0");
		total.setText("of 0");
	}
	private void next()
	{
		setValue(value + 1, true);
	}

	private void previous()
	{
		setValue(value - 1, true);
	}

	public void setMaxRecords(int maxRecords)
	{
		if( maxRecords == 0 )
			clear();
		else if (maxRecords > 0)
		{
			maxValue = maxRecords;
			if(value == 0)
				setValue(1);
			total.setText("of " + Integer.toString(maxRecords));
		}
	}
	public void setValue(int val, boolean sendEvent)
	{
		if(val >= 1 && val <= maxValue)
		{
			value = val;
			current.setText(Integer.toString(val));
			if(sendEvent)
				obs.notifyObservers(new NavigationUpdate(value));
		}		
	}
	public void setValue(int val)
	{
		setValue(val, false);
	}
	public int getValue()
	{
		return(value);
	}
	public int getMaxRecords()
	{
		return(maxValue);
	}
	public void setDescriptionText(String desc)
	{
		description.setText(desc);
	}	

	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();
		if (o instanceof JButton)
		{
			JButton button = (JButton) o;
			switch(button.getMnemonic())
			{
				case 'N':
				{
					next();
				}
				break;
				case 'P':
				{
					previous();
				}
				break;
				default:
				{
					Logger.log("WARNING: UNHANDLED BUTTON ACTION IN FieldNavigator");
				}
			}
		}
	}

}

