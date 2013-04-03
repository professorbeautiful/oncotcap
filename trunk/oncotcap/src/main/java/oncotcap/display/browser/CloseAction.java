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
public class CloseAction extends AbstractAction {
		WaitWindow waitWindow = null;

	/** Constructor for the ProtegeSaveAction object */
	protected CloseAction() {
		super("Exit");
	}

	/**
	 * Constructor for the ProtegeSaveAction object
	 *
	 * @param actionName Description of Parameter
	 */
	protected CloseAction(String actionName) {
		super(actionName);
	}

	/*
	 * Save the the proteg project
	 */
	/**
	 * @param e Description of Parameter
	 */
	public void actionPerformed(ActionEvent e) {
		OncBrowser.getOncBrowser().close();
		
	}

}

