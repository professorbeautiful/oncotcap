package oncotcap.util;

import javax.swing.*;

public class SwingUtil
{
	
	private static JFrame modeFrame = null;
	/**
	 ** Returns a JFrame to use with modal dialog boxes that don't have a
	 ** frame to use as a parent.  It's icon (which will be used by a
	 ** child JDialog) is set to the current project default.
	 **/
	public static JFrame getModeFrame()
	{
		if(modeFrame == null)
		{
			modeFrame = new JFrame();
			modeFrame.setIconImage(OncoTcapIcons.getDefault().getImage());
		}
		return(modeFrame);
	}
	public static void revalidateAll(JComponent comp)
	{
		comp.revalidate();
		java.awt.Component [] comps = comp.getComponents();
		for(int n = 0; n<comps.length; n++)
		{
			if(comps[n] instanceof JComponent)
				revalidateAll((JComponent)comps[n]);
		}
	}

}