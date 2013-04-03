/*
 * Educational Resource for Tumor Heterogeneity
 *             Oncology Thinking Cap
 *
 * Copyright (c) 2004  University of Pittsburgh
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
package oncotcap.display.editor.persistibleeditorpanel;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

import oncotcap.display.common.*;
import oncotcap.datalayer.persistible.ModelController;
import oncotcap.display.browser.DependencyTablePanel;
import oncotcap.display.browser.GenericTree;
/**
 * @author    morris
 * @created   Dec 6, 2004
 */
public class ReviewTableAction extends AbstractAction {
	/** Constructor for the ReviewTableAction object */
	public ReviewTableAction() {
		super("Review Table");
	}

	/**
	 * Constructor for the  ReviewTableAction object
	 *
	 * @param actionName Description of Parameter
	 */
	public ReviewTableAction(String actionName) {
		super(actionName);
	}

	/*
	 * Save the the proteg project
	 */
	/**
	 * @param e Description of Parameter
	 */
	public void actionPerformed(ActionEvent e) {
			// Get the menu that invoked this action
			Container component = ((JComponent)e.getSource()).getParent();
			if ( component instanceof OncPopupMenu ) {
					OncPopupMenu menu = ((OncPopupMenu)component);
					Object currentInstance =
							menu.getCurrentObject();
					if ( currentInstance instanceof ModelController ) {
							DependencyTablePanel dependencyTable = 
									new DependencyTablePanel();
							OncFrame frame = new OncFrame();
							dependencyTable.buildDependencyTable((ModelController)currentInstance);
							frame.addFrameableComponent(dependencyTable);
							frame.pack();
							frame.setSize(new Dimension(800,600));
							frame.setVisible(true);
							
					}
			}
	}
}
