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
package oncotcap.datalayer.persistible;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

import  oncotcap.datalayer.AbstractPersistible;
import  oncotcap.datalayer.Persistible;
import oncotcap.display.common.*;
import oncotcap.display.browser.GenericRootNodeObject;
import oncotcap.display.browser.GenericTree;
import oncotcap.display.browser.GenericTreeNode;
import oncotcap.display.browser.OntologyTree;
/**
 * @author    morris
 * @created   April 22, 2003
 */
public class CopyPersistibleAction extends AbstractAction {
	/** Constructor for the CopyPersistibleAction object */
	public CopyPersistibleAction() {
		super("Copy");
	}

	/**
	 * Constructor for the ProtegeSaveAction object
	 *
	 * @param actionName Description of Parameter
	 */
	protected CopyPersistibleAction(String actionName) {
		super(actionName);
		System.out.println("initialize Copy Persistible" );
	}

	
	/**
	 * @param e Description of Parameter
	 */
	public void actionPerformed(ActionEvent e) {
			// Get the menu that invoked this action
			Container component = ((JComponent)e.getSource()).getParent();
			if ( component instanceof OncPopupMenu ) {
					OncPopupMenu menu = ((OncPopupMenu)component);
					Object currentInstance = menu.getCurrentObject();
					// Note that context is only set by an OncScrollListListener.
					if ( currentInstance instanceof AbstractPersistible ) {
						AbstractPersistible currentPersistible = (AbstractPersistible ) currentInstance;
						//Make a copy of the object
						Object clonedObject = ((AbstractPersistible)currentInstance).clone();
						Component menuParent = menu.getInvoker();
						if ( clonedObject instanceof Persistible ) 
								((Persistible)clonedObject).update();
						if ( menuParent instanceof GenericTree ) {
								// Add new object to the tree - and refresh the tree 
								// - if not a root node it won't show up.
							GenericTree tree = (GenericTree)menuParent;
							// If the parent is the root, then just add it.
							//  TODO: need to insert the NODE of the currentInstance here
							GenericRootNodeObject rootObject = (GenericRootNodeObject) tree.getRootNode().getUserObject();
							if (currentPersistible.getClassName().equals(rootObject.toString()))
								tree.addNode(clonedObject, true, (Vector)null);
							else {
								// TODO: get the immediate connection in the path to the parent.
								// then link the clone to this connection.
								GenericTreeNode currentNode = tree.findUserObject(currentPersistible);
								Object[] path = currentNode.getUserObjectPath();
								AbstractPersistible linkToMe = (AbstractPersistible) path[path.length-2];
								currentPersistible.link(linkToMe);
								System.err.println("CopyPersistibleAction.actionPerformed: "
										+ "  linking to the parent.");
							}
							tree.refresh();
							// TODO: make sure that the tree shows the sub-objects of the new cloned thing.
							//  (The sub-objects are in fact added to the thing.)
							//  refresh() does not do it, but updateTree does.
							tree.getOntologyTree().updateTree();
							// I think this does it!
						}
						if ( menuParent instanceof OncScrollList ) {
							// Add new object to the tree - and refresh the tree 
							// - if not a root node it won't show up
							OncScrollList list = ((OncScrollList)menuParent);
							Component parent = list.getParent();
							JOptionPane d = new JOptionPane("parent component is " + parent + "  of type "
									+ parent.getClass());
							Object context = menu.getContext();
							if ( ! (context instanceof OncScrollListListener)) {
								String trouble = "CopyPersistibleAction: I'm confused about OncScrollList." ;
							}
						}
						if ( clonedObject instanceof Persistible ) 
							((Persistible)clonedObject).update();
					}
			}
	}
}

