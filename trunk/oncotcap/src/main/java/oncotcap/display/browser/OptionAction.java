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

import oncotcap.display.browser.OptionPanel;

/**
 * @author    morris
 * @created   April 22, 2003
 */
public class OptionAction extends AbstractAction {
	/** Constructor for the OptionAction object */
		static OptionPanel optionFrame = null;
	public OptionAction() {
		super("Options");
	}

	/**
	 * Constructor for the OptionAction object
	 *
	 * @param actionName Description of Parameter
	 */
	public OptionAction(String actionName) {
		super(actionName);
	}

	/*
	 * Save the the proteg project
	 */
	/**
	 * @param e Description of Parameter
	 */
	public void actionPerformed(ActionEvent e) {
			if ( optionFrame == null ) 
					optionFrame = new OptionPanel();
			optionFrame.setSize(new Dimension(300, 300 ));
			optionFrame.setVisible(true);
	}
}

