package oncotcap.util;

import javax.swing.*;
import java.awt.*;

public class IrbMessageBox extends OncMessageBox
{

	public static final int STATUS_CANCELLED = 1;
	public static final int STATUS_DOANYWAY = 2;
	
	private JButton btnDoAnyway = new JButton("Pull Strings");
	private int status = STATUS_CANCELLED;
	
	IrbMessageBox(Frame parent, Object message, String title, int type)
	{
		super(parent, message, title, type, false);
		addExtraComponents();
		waitForDismiss();
	}
	protected void addExtraComponents()
	{
		btnDoAnyway.setSize(100, BUTTON_HEIGHT);
		btnDoAnyway.setPreferredSize(new Dimension(100, BUTTON_HEIGHT));
		btnDoAnyway.setMnemonic('P');
		btnDoAnyway.addActionListener(this);
		addButton(btnDoAnyway);
	}
	public static void main(String [] args)
	{
		IrbMessageBox imb = (IrbMessageBox) showMessageDialog( null , "<html><body><font color=red size=5>There needs to be an Eligibility Criterion restricting enrollment to patients with sufficient hepatic function!  Try again next month (or \"pull strings\" if you think you know better)!</font></body></html>", "IRB Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
		if (imb.getStatus() == STATUS_CANCELLED)
			Logger.log("cancelled");
		else
			Logger.log("do it anyway");
		System.exit(1);
	}
	public static OncMessageBox showMessageDialog(Frame parent, Object message, String title, int type)
	{
		return(new IrbMessageBox(parent, message, title, type));
	}
	public int getStatus()
	{
		return(status);
	}
	public void actionPerformed(java.awt.event.ActionEvent e)
	{
		Object o = e.getSource();
		if (o instanceof JButton)
		{
			if(((JButton) o).getMnemonic() == 'P')
			{
				status = STATUS_DOANYWAY;
				setVisible(false);
				dismiss = true;
			}
		}
		super.actionPerformed(e);
	}
}