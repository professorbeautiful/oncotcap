/*
 * Educational Resource for Tumor Heterogeneity
 *             Oncology Thinking Cap
 *
 * Copyright (c) 2003  University of Pittsburgh
 * All rights reserved.
 *
 *  SourceSafe Info:
 *               $Header: $
 *               Revision: $Revision$
 *               Author: $Author$
 *
 * Code Review History:
 *     (mm.dd.yyyy initials)
 *
 * Test History:
 *     (mm.dd.yyyy initials)
 */
package oncotcap.display.browser;

import java.awt.event.*;
import javax.swing.*;
import oncotcap.display.common.WaitWindow;

/**
 * @author    morris
 * @created   April 22, 2003
 */
public class ProtegeSaveAction extends AbstractAction {
		WaitWindow waitWindow = null;

	/** Constructor for the ProtegeSaveAction object */
	protected ProtegeSaveAction() {
		super("Save KnowledgeBase");
	}

	/**
	 * Constructor for the ProtegeSaveAction object
	 *
	 * @param actionName Description of Parameter
	 */
	protected ProtegeSaveAction(String actionName) {
		super(actionName);
	}

	/*
	 * Save the the proteg project
	 */
	/**
	 * @param e Description of Parameter
	 */
	public void actionPerformed(ActionEvent e) {
			// JFrame jf = new JFrame();
			// 			jf.setSize(300,300);
			// 		jf.setVisible(true);
			// 			ww.setVisible(true);
			//System.out.println("SAVE source parent " +   ((JComponent)e.getSource()).getParent());
			//System.out.println("SAVE source " +   e.getSource());
			if(waitWindow == null)
					waitWindow = new WaitWindow(OncBrowser.getOncBrowser(), "Saving. Please wait.", true, OncBrowserConstants.KCColorPale);
			waitWindow.setVisible(true);
			Saver s = new Saver();
			Thread t = new Thread(s);
			t.start();
		
	}
	static public boolean isCurrentlySaving(){
		return isCurrentlySaving;
	}
	static private boolean isCurrentlySaving = false;
	private class Saver implements Runnable
	{
		public void run()
		{
				System.out.println("Saving " + oncotcap.datalayer.DataSourceStatus.getDataSource());
				long start =  (new java.util.GregorianCalendar()).getTimeInMillis();
				isCurrentlySaving = true;
				oncotcap.datalayer.DataSourceStatus.getDataSource().commit();
				if(waitWindow != null)
						waitWindow.setVisible(false);
				System.out.println("Save completed");
				isCurrentlySaving = false;
				long end = (new java.util.GregorianCalendar()).getTimeInMillis();
				System.out.println("Elapsed time = " + (end - start)/1000.0 + " seconds");
				//  I get roughly 18 seconds when the trees are empty, and 24-28 secs if not.
		}
	}

}

