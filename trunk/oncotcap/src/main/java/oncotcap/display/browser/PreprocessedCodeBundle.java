package oncotcap.display.browser;

import java.awt.datatransfer.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.beans.*;
import java.util.regex.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.TextAction;

import java.lang.reflect.Array;
import javax.swing.tree.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import oncotcap.datalayer.persistible.CodeBundle;
import oncotcap.engine.ValueMap;
import oncotcap.Oncotcap;

import oncotcap.display.common.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.persistible.parameter.*;

import oncotcap.display.editor.EditorFrame;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.ListDialog;
import oncotcap.display.browser.*;
import oncotcap.util.*;
import java.awt.dnd.*;

// Preprocessed Code Bundles and actions - with substituted code
public	class PreprocessedCodeBundle {
		GenericTreeNode treeNode = null;
		CodeBundle cb = null;
		Hashtable valueMap = null;
		String ifClause = null;
		String processName = null;
		String eventName = null;
		String methodName = null;
		Vector preprocessedActions = null;
		DependencyTablePanel depTable = null;

		public PreprocessedCodeBundle(GenericTreeNode cbTreeNode, 
																	DependencyTablePanel depTable){
				this.depTable = depTable;
						treeNode = cbTreeNode;
						if (treeNode.getUserObject() instanceof CodeBundle)
								cb = (CodeBundle)treeNode.getUserObject();
						else 
								System.out.println("ERROR: non-CodeBundle Tree node used to create PreprocessedCodeBundle");
						// Create a value map with backquotes resolved
						valueMap = createValueMap();
						// 				System.out.println("CB  " + cb
						// 															 + " VALUE MAP " + valueMap);
						
						// Now use this new valuemap to get the resolved version 
						// of the if clause and the process and method names
						// This should support backquoting in all three
						if ( cb.getIfClause() != null ) {
								ifClause = 
										ValueMap.substitute(valueMap, 
																							 cb.getIfClause());
								ifClause = DependencyTablePanel.fixGetContainerInstance(ifClause);
								//  System.out.println("CB if clause " + cb.getIfClause()
// 								 															 + " AFTER " + ifClause);
						}

						if ( cb.getProcessDeclaration() != null ) 
								processName = 
										ValueMap.substitute(valueMap,
																							 cb.getProcessDeclaration().getName());
						if ( cb.getMethodDeclaration() != null ) 
								methodName = 
										ValueMap.substitute(valueMap,
																							 cb.getMethodDeclaration().getName());
						if ( cb.getEventDeclaration() != null )
								eventName = 
										ValueMap.substitute(valueMap,
																							 cb.getEventDeclaration().getName());

						// Preprocess the actions 
						Collection actions = cb.getActionList();
						Iterator i = actions.iterator();
						if ( actions.size() > 0 ) 
								preprocessedActions = new Vector();
						while ( i.hasNext() ) {
								OncAction action = (OncAction)i.next();
								// Create preprocessed action and put on a list
								PreprocessedAction preprocessedAction = 
										new PreprocessedAction(this, 
																					 action,
																					 depTable);
								preprocessedActions.addElement(preprocessedAction);
								depTable.allPreprocessedActions.addElement(preprocessedAction);
						}
				} // constructor

				public Hashtable getValueMap() {
						return valueMap;
				}
				
				public String getMethodName() {
						return methodName;
				}
				public String getIfClause() {
						return ifClause;
				}
				public String getProcessName() {
						return processName;
				}
				public String getEventName() {
						return eventName;
				}
				public CodeBundle getCodeBundle() {
						return cb;
				}
				public GenericTreeNode getTreeNode() {
						return treeNode;
				}

				// Create a value map for the code bundle using the tree hierarchy to 
				// determine SBs this cb is dependent on 
				// in this context
				public Hashtable createValueMap() {
						// If the tree node or cb is not yet set can't do this
						if ( treeNode == null || cb == null ) 
								return null;
						// Based on the CBs location in the tree determine the 
						// fully resolved value map that should be used for substitution
						// do this instead of the SB setting stuff because that 
						// is very confusing and a little more complicated to 
						// maintain ( the state of STusingme ) when you are 
						// doing multiple passes through CBs
						
						// Start with the valuemap from the SB directly connected to the
						// ST this CB is attached to
						// Get the SB to start the resolution of this code bundle from 
						GenericTreeNode sbNode = 
								GenericTreeNode.getClosestNode(StatementBundle.class,
																							 treeNode);
						if ( sbNode == null ) {
								System.out.println("How can SB be null " + cb);
								return null;
						}
						Hashtable map = buildValueMap(sbNode);
						// TEST TEST TEST Hashtable map = buildValueMapWithParameter(sbNode);
					
						return map;
				}// getvaluemap

				// Build a value map for the code bundle - 
				// this can be moved to code bundle
				Hashtable buildValueMap(GenericTreeNode sbNode) {
						// Convert SB map to a 'good' values by name hashtable
						// ValueMap.valuesByName seems to give different results in 
						// different projects
						ValueMap valueMap = 
								((StatementBundle)sbNode.getUserObject()).getValueMap();
						Hashtable map = valueMap.getValuesByNameMap();
						String name = null;
						String myValue = null;
						for (Enumeration e=map.keys(); 
								 e.hasMoreElements(); ) {
								name = (String)e.nextElement();
								myValue = (String)map.get(name);
								if ( myValue != null && myValue.indexOf("`") > -1) {
										myValue = resolveValue(myValue, 
																				 GenericTreeNode.getClosestNode(StatementBundle.class,
																												sbNode));
								}
								// Fix this in the hashtable
								map.put(name,myValue);
						}
						return map;
				} // buildvaluemap

		/* TEST TEST TEST 
		// Build a value map for the code bundle - 
		// this can be moved to code bundle
		// Let this shadow test hashtree contain the key = st param name, 
		// value = parameter  ( instead of the string )
		Hashtable buildValueMapWithParameter(GenericTreeNode sbNode) {
				// Convert SB map to a 'good' values by name hashtable
				// ValueMap.valuesByName seems to give different results in 
				// different projects
				ValueMap valueMap = 
						((StatementBundle)sbNode.getUserObject()).getValueMap();
				Hashtable map = valueMap.getParametersByNameMap();
				String name = null;
				String myValue = null;
				// Loop through the param names
				for (Enumeration e=map.keys(); 
						 e.hasMoreElements(); ) {
						name = (String)e.nextElement();
						myValue = ()map.get(name);
						if ( myValue != null && myValue.indexOf("`") > -1) {
								myValue = resolveValue(myValue, 
																			 GenericTreeNode.getClosestNode(StatementBundle.class,
																																			sbNode));
						}
						// Fix this in the hashtable
						map.put(name,myValue);
				}
				return map;
		} // buildvaluemap
				
		*/
				String resolveValue(String value, 
														GenericTreeNode sbNode) {
						// Break string up into backquoted parts and non backquoted parts
						// substitute back quoted parts
						// if there are still backquotes keep on resolving
						StringBuffer newValue = new StringBuffer();
						int idx = 0;
						Matcher match = DependencyTablePanel.quotedVar.matcher(value.toUpperCase());
						String backQuotedString = null;
						String unbackQuotedString = null;
						if ( !match.find() ) // no backquotes the value is already resolved
								{
										return value;
								}
						// ELSE resolve the value
						match.reset();
						ValueMap valueMap = 
								((StatementBundle)sbNode.getUserObject()).getValueMap();
						Hashtable map = valueMap.getValuesByNameMap();
						while(match.find()) {
								newValue.append(value.substring(idx, match.start()));
								backQuotedString = value.substring(match.start(), 
																									 match.end()).trim();
								unbackQuotedString = value.substring(match.start()+1, 
																									 match.end()-1).trim();
								// Does the param exist in the value map - without the quotes
								String paramValue = (String)map.get(unbackQuotedString.toUpperCase());
								if ( paramValue == null ) {
										// Unable to resolve this value
										newValue.append(backQuotedString); // with backquotes
										// Put this on some list for unresolved params / strings
								}
								else {
										if ( paramValue.indexOf("`") > -1 ) {
												String resolvedValue = 
														resolveValue(paramValue, 
																				 GenericTreeNode.getClosestNode(StatementBundle.class,
																												sbNode));
												newValue.append(resolvedValue);
										}
										else {
												newValue.append(paramValue);
										}
								} // else
								idx = match.end();
						}// while
						newValue.append(value.substring(idx, 
																						value.length()));
						return newValue.toString();
				}

		public String toString() {
				return cb.toString() + " " + processName + "." 
						+ eventName + "." + methodName;
		}

				
		} // preprocessed code bundle
