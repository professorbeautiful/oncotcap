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
package oncotcap.display.common;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

import  oncotcap.datalayer.AbstractPersistible;
import  oncotcap.datalayer.persistible.*;
import oncotcap.display.browser.*;
/**
 * @author    morris
 * @created   April 22, 2003
 */
public class SortNodeAction extends AbstractAction {
		static BrowserNodeVersionComparator versionComparator =
				new BrowserNodeVersionComparator();
	/** Constructor for the SortNodeAction object */
	public SortNodeAction() {
		super("Sort");
	}

	/**
	 * Constructor for the ProtegeSaveAction object
	 *
	 * @param actionName Description of Parameter
	 */
	protected SortNodeAction(String actionName) {
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
					if ( currentInstance instanceof AbstractPersistible ) {
							//Make a copy of the object
							Object clonedObject =
									((AbstractPersistible)currentInstance).clone();
							Component menuParent = menu.getInvoker();
							if ( menuParent instanceof GenericTree ) {
									// Add new object to the tree - and refresh the tree 
									// - if not a root node it won't show up
									GenericTreeNode node =
											((GenericTree)menuParent).getSelectedNode(); 
									if ( node != null ) {
											System.out.println("Sorting");
											((GenericTree)menuParent).sort(node, versionComparator);
									}
							}
					}
			}
	}
}

