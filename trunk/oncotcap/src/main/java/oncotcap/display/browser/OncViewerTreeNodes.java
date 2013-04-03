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

import java.util.*;
import java.lang.reflect.Array;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.util.*;
import oncotcap.*;

/**
 * @author   morris
 * @created  April 24, 2003
 */
public class OncViewerTreeNodes {
		public static Hashtable instantiatedVariables = new Hashtable();
		public static Hashtable modifiedVariables = new Hashtable();
		public static Hashtable instantiatedProcessEvents = new Hashtable();
		public static Hashtable executedProcessEvents = new Hashtable();
		
	/** Constructor for the OncViewerObjects object */
	public OncViewerTreeNodes() { }

	/**
	 * Gets the OncViewerObjects attribute of the OncViewerObjects object
	 *
	 * @return  The OncViewerObjects value
	 */
	public Vector getOncViewerTreeNodes() {
		OncViewerTreeNode parent = null;
		// Get all Code Bundles in the current context and
		// Build the tree structure
		Collection codeBundles =
		Oncotcap.getDataSource().find(ReflectionHelper.classForName(
		"oncotcap.datalayer.persistible.CodeBundle"));
		// Build a giant hashtable using these codebundles
		Hashtable theTree = buildTree();
		// Translate that into a treemodel
		TreeViewableList viewableList2 = new TreeViewableList();
		for (Enumeration e = theTree.keys() ; 
				 e.hasMoreElements();) {
				ProcessDeclaration oncProcess = (ProcessDeclaration)e.nextElement();
				OncViewerTreeNode oncViewerObject = 
						new OncViewerTreeNode(oncProcess.getDisplayString(),	null);
				//oncViewerObject.setCode(oncProcess.getCode());
				oncViewerObject.setUserObject(oncProcess);
				viewableList2.getViewableList().addElement(oncViewerObject);
				viewableList2.setViewableObject(oncViewerObject);
				Hashtable h = (Hashtable)theTree.get(oncProcess);
				OncViewerTreeNode oncTreeNode = null;
				for (Enumeration ee = h.keys(); 
						 ee.hasMoreElements();){
						MethodDeclaration oncMethod = (MethodDeclaration)ee.nextElement();
						oncTreeNode = 
								new OncViewerTreeNode(oncMethod.getDisplayString(),	
																			oncViewerObject);
						//oncTreeNode.setCode(oncMethod.getCode());
						oncTreeNode.setUserObject(oncProcess);
						viewableList2.getViewableList().addElement(oncTreeNode);
						viewableList2.setViewableObject(oncTreeNode);
						//check(oncProcess, method, oncTreeNode);

						// add actions to tree
						Vector actions = (Vector)h.get(oncMethod); //vector of action lists
						Iterator i = actions.iterator();
						OncViewerTreeNode oncTreeNode2 = null;
						while (i.hasNext()) {
								ActionList actionList = (ActionList)i.next();
								Iterator ii = actionList.iterator();
								while (ii.hasNext()) {
										DefaultOncAction action = (DefaultOncAction)ii.next();
										oncTreeNode2 = 
												new OncViewerTreeNode(action.getDisplayString(),	
																							oncTreeNode);
										//oncTreeNode2.setCode(action.getCode());
										oncTreeNode2.setUserObject(action);
										viewableList2.getViewableList().addElement(oncTreeNode2);
										viewableList2.setViewableObject(oncTreeNode2);
										//check(action, oncTreeNode);
								}
						}
				}
		}

		Object oncProcesses[] = ProcessDeclaration.getAllProcesses();
		TreeViewableList viewableList = new TreeViewableList();
		Hashtable treeNodeHashtable = new Hashtable();
		Hashtable parentGuidHashtable = new Hashtable();
		for ( int i = 0; i < Array.getLength(oncProcesses); i++) {
			ProcessDeclaration oncProcess = (ProcessDeclaration)oncProcesses[i];
			//viewableList = oncProcess.getViewableList(viewableList, null, null);
		}
		//while codeBundles
		//System.out.println("treeNodeHashtable "  + treeNodeHashtable);
		return viewableList2.getViewableList();
	}


		// Build a tree and as you go build the code
		private Hashtable buildTree() {
				// Get all Code Bundles in the current context and
				// Build the tree structure
				Hashtable treeHashtable = new Hashtable();
				Collection codeBundles =
						Oncotcap.getDataSource().find(ReflectionHelper.classForName(
																						"oncotcap.datalayer.persistible.CodeBundle"));
				Iterator i = codeBundles.iterator();
				while ( i.hasNext() ) {
						Hashtable methodHashtable = null;
						Vector actionVector = null;
						CodeBundle cb = (CodeBundle)i.next();
						if ( cb.oncProcess == null  || cb.method == null)
								continue;
						methodHashtable = (Hashtable)treeHashtable.get(cb.oncProcess);
						if (methodHashtable == null) {
								// New method 
								methodHashtable = new Hashtable();
						}
						// Get all the methods associated with the process 
						// and put it and its coressponding actions in a hashtable
						ActionList actionList = cb.getActions();
						// Loop thru the actions and set the code
						/*						if ( cb.ifClause != null) {
								cb.method.addCode("if ( " + cb.ifClause + " ) { \n");
						}
						Iterator ii = cb.getActionList();
						while (ii.hasNext()) {
								OncAction oa = (OncAction)ii.next();
								cb.method.addCode(oa.getCode() + "\n");
						}
						if ( cb.ifClause != null) {
								cb.method.addCode("}\n");
						}
						*/
						//check(method)
						Vector setOfActionLists = (Vector)methodHashtable.get(cb.method);
						if ( setOfActionLists == null) {
								// New method 
								setOfActionLists = new Vector();
						}
						if ( setOfActionLists.contains(actionList) == false ) {
								// add it to the list of actions
								setOfActionLists.addElement(actionList);
						}
						methodHashtable.put(cb.method, setOfActionLists);
						treeHashtable.put(cb.oncProcess, methodHashtable);
				}// while code bundles

				// Loop through and fill up the oncprocess code
				// removed this block, no longer needed, ProcessDeclaration
				// will be able to create it's own code...
				// wes 6/12/2003
/*				for (Enumeration e = treeHashtable.keys() ; 
						 e.hasMoreElements();) {
						ProcessDeclaration o = (ProcessDeclaration)e.nextElement();
						Hashtable h = (Hashtable)treeHashtable.get(o);
						//o.clearCode();
						for (Enumeration ee = h.keys(); 
								 ee.hasMoreElements();){
								o.addCode(((MethodDeclaration)ee.nextElement()).getCode());
						}
				} */
				return treeHashtable; 
		}
		/*
		private void check(ProcessDeclaration process, 
											 MethodDeclaration method,
											 OncViewerTreeNode treeNode) {
				String processMethod = process.getName() + "." + method.getName();
				instantiatedProcessMethod.put(processMethod, treeNode);
		}

		private void check(OncAction action,
											 OncViewerTreeNode treeNode) {
				if ( action instanceof InstantiateAction) {
						instantiatedProcessMethod.put(action, treeNode);
				}
				else if ( action instanceof AddVariableAction ) {
				}
				else if ( action instanceof ModifyVariableAction ) {
				}
				else if ( action instanceof ScheduleEventAction ||
									action instanceof TriggerEventAction ) {
				}

		}

		public void getUndeclaredVariables() {
		}
		
		public void getUnDeclaredEvents() {
		}
		
		public void getUndeclaredProcessMethods() {
		}

		public void colorCodeTree() {
				Vector undeclaredVariables = getUndeclaredVariables();
				// Find corresponding tree nodes and change the background color
				// to yellow

				Vector undeclardProcessMethods = getUndeclaredProcessMethods();
				// Find corresponding tree nodes and change the background color
				// to orange
		}
		*/
}
