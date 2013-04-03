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
import java.awt.Dimension;
import javax.swing.*;


/**
 * @author    morris
 * @created   April 22, 2003
 */
public class OpenAction extends AbstractAction {
	/** Constructor for the OpenAction object */
		static OptionPanel optionFrame = null;
		public OpenAction() {
				super("Open Project");
		}

	/**
	 * Constructor for the OpenAction object
	 *
	 * @param actionName Description of Parameter
	 */
	public OpenAction(String actionName) {
		super(actionName);
	}

	/*
	 * Save the the proteg project
	 */
	/**
	 * @param e Description of Parameter
	 */
	public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			int returnVal = chooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("Open Project: " +
														 chooser.getSelectedFile().getName());
    }
	}
}

