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

public	class PreprocessedAction {
		PreprocessedCodeBundle ppCB = null;
		OncAction oncAction = null;
		Hashtable vars = new Hashtable();
		int actionType = -1;
		DependencyTablePanel depTable = null;
		Variable variable = null;
				// Move this to ActionType - when we move to 1.5 can use enums

				public PreprocessedAction (PreprocessedCodeBundle cb, 
																	 OncAction oncAction,
																	 DependencyTablePanel depTable) {
						ppCB = cb;
						this.depTable = depTable;
						this.oncAction = oncAction;
						actionType = getActionType(oncAction);
						vars = initVariableHashtable(ppCB);
				}
				
				public Hashtable getVariableHashtable() {
						return vars;
				}
				public OncAction getOncAction() {
						return oncAction;
				}
				public PreprocessedCodeBundle getPreprocessedCodeBundle() {
						return ppCB;
				}
				public int getActionType() {
						return actionType;
				}
				// Move this to actiontype
				public int getActionType(OncAction oncAction) {
						if ( oncAction instanceof AddGenericCode )
								return DependencyTablePanel.ADD_CODE;
						if ( oncAction instanceof AddVariableAction)
								return DependencyTablePanel.ADD_VARIABLE;
						if ( oncAction instanceof ScheduleEventAction)
								return DependencyTablePanel.SCHEDULE_EVENT;
						if ( oncAction instanceof  TriggerEventAction )
								return DependencyTablePanel.TRIGGER_EVENT;
						if ( oncAction instanceof  InstantiateAction )
								return DependencyTablePanel.INSTANTIATE;
						if ( oncAction instanceof ModifyVariableAction )
								return DependencyTablePanel.MODIFY_VARIABLE;
						if ( oncAction instanceof  ModifyScheduleAction )
								return DependencyTablePanel.MODIFY_SCHEDULE;
						if ( oncAction instanceof InitVariableAction )
								return DependencyTablePanel.INIT_VARIABLE;
	

						return -1;
				}
				
				public Hashtable initVariableHashtable(PreprocessedCodeBundle cb) {
						// 		System.out.println("ACTION TYPE " + oncAction.getClass()
						// 															 + " " + actionType);
						Hashtable hTable = new Hashtable();
						// Variables will have to be mined differently depending on the 
						// type of action
						DeclareVariable var = null;
						Variable depVar = null;
						String valString = "";
						switch ( actionType ) {
						case DependencyTablePanel.ADD_CODE:
								String snippet = ((AddGenericCode)oncAction).getGenericCode();
								String varName = null;
								// Parse the generic code but throw out everything that 
								// is not a recognizable var or func
								// First separate this block of code into statements
								// 1. remove commented out code
								Matcher match = DependencyTablePanel.commentedCode.matcher(snippet);
								String uncommentedCode = match.replaceAll("");

								// 2. break into individual ; separated statements
								StringTokenizer tokenizer = 
										new StringTokenizer(uncommentedCode, ";");
								Vector initDep = new Vector();
								String varString = null;
								while ( tokenizer.hasMoreTokens()) {
										String tok = tokenizer.nextToken().trim();
										//System.out.println("STATEMENT " + tok);
										// 3. Throw out print statements
										if ( !tok.startsWith("System.out.println")) {
												// 4. Determine what is on the left hand side 
												//System.out.println("parsing " + tok);
												// If the left hand side is empty  go to the next one
												if ( tok == null || tok.trim().length() == 0 )
														continue;
												varString = getLeftHandSide(tok);
												valString = getRightHandSide(tok);
												if ( varString.trim().length() == 0 
														 ||  valString.trim().length() == 0 )
														continue;
												varString = 
														ValueMap.substitute
														(cb.getValueMap(), varString);
												valString = 
														ValueMap.substitute
														(cb.getValueMap(), valString);
												depVar = new Variable(cb, varString);
												putVariable(hTable, depVar, valString);
												// System.out.println("generic code variable " 
												// + varString + " - " + valString);
										} //if not a print string
								}// while
								break;
								
						case DependencyTablePanel.ADD_VARIABLE:
								var = 
										((AddVariableAction)oncAction).getVariable();
								depVar = new Variable(cb, var.toString());
								valString = var.getInitialValue();
								if ( valString != null && valString.length() > 0)
										valString = ValueMap.substitute
												(cb.getValueMap(), valString);
								// System.out.println("add variable " + depVar + " value map " 
								// 								+ cb.getValueMap()
								// 	 + " valString " + valString);
								putVariable(hTable, depVar, valString);
								break;
								
						case DependencyTablePanel.INIT_VARIABLE: 
								System.out.println("INIT_VARIABLE " + cb
																	 + " var " + ((InitVariableAction)oncAction).getVariable());
								if ( ((InitVariableAction)oncAction).getVariable() == null ) {
									// 	System.out.println("getNewInitialization " + ((InitVariableAction)oncAction).getNewInitialization());
// 										System.out.println("getNewInitialization init value" + ((InitVariableAction)oncAction).getNewInitialization().getInitialValue());
										depVar = 	
												new Variable(cb, 
												 ((InitVariableAction)oncAction).getNewInitialization().getInitialValue().toString());
								}		
								else {								
										depVar = 
												new Variable(cb, 
																		 ((InitVariableAction)oncAction).getVariable().toString());
								}
								valString = 
										((InitVariableAction)oncAction).getNewInitialization().getInitialValue();
								valString = 	ValueMap.substitute
										(cb.getValueMap(), valString);
								valString = 
										DependencyTablePanel.fixGetContainerInstance(valString);
								// valString = ValueMap.substitute
								// 			 (cb.getValueMap(), getContainerInstance,
								// 								 valString);
								putVariable(hTable, depVar, valString);
								break;
								
						case DependencyTablePanel.TRIGGER_EVENT:
								break;
								
						case DependencyTablePanel.SCHEDULE_EVENT:
								break;
								
						case DependencyTablePanel.INSTANTIATE:
								// For each variable initialization
								Collection varInits = 
										((InstantiateAction)oncAction).getVariableInitializations();
								String processName = null;
								processName = ((InstantiateAction)oncAction).getName();

								Iterator i = varInits.iterator();
								Object obj = null;
								while (i.hasNext()) {
										obj = i.next();
										if(obj instanceof DeclareVariable) {
												DeclareVariable declareVar = 
														(DeclareVariable) obj;
												varName = declareVar.getName();

												// These variables are different
												// they belong to the class that is instantiated 
												// not this class
												depVar = new Variable(processName, varName);
												varName = 
														StringHelper.javaName(ValueMap.substitute
																									(cb.getValueMap(),
																									 depVar.getFullName()));
												
												valString = declareVar.getInitialValue();
												valString = 	ValueMap.substitute
														(cb.getValueMap(), valString);
												putVariable(hTable, depVar, valString);
												// 		System.out.println("instantiate variable " 
												// 		 + varName + " value " +
												// 															valString);
										} // if declare variable
								}// while init variables
								break;
								
						case DependencyTablePanel.MODIFY_VARIABLE:
								Object getVariable = ((ModifyVariableAction)oncAction).getVariable();
								System.out.println("GETVARIABLE " + getVariable + " " 
																	 + getVariable.getClass());
								//var = (DeclareVariable)getVariable;

								if ( ((ModifyVariableAction)oncAction).getModification() != null ) 
										valString = 
												((ModifyVariableAction)oncAction).getModification().getModification();
								else
										valString= "";
								if ( getVariable == null )
										return hTable;
								depVar = new Variable(cb, getVariable.toString());
								if (valString != null ) {
										valString = 	ValueMap.substitute
												(cb.getValueMap(), valString);
										putVariable(hTable, depVar, valString);
								}
								//System.out.println("mod variable " + var + " value " +
								//													 valString);
								break;
								
						case DependencyTablePanel.MODIFY_SCHEDULE:
								break;
								
						} // switch actionType
						return hTable;
				} //initVariableHashtable
				
				void putVariable(Hashtable hTable, Variable var,
												 String valString) {
						Vector currentValues = (Vector)hTable.get(var.getFullName());
						if ( currentValues == null ) {
								currentValues = new Vector();
						}
						currentValues.addElement(valString);
						if ( depTable.allVariables.get(var.getFullName()) == null) {
								depTable.allVariables.put(var.getFullName(), var);
						}
						//	depTable.putVariableDependencyString(var.getFullName(), valString,
						//														 ppCB.getProcessName());
						hTable.put(var.getFullName(), currentValues);
				}

				public String makeName(PreprocessedCodeBundle cb, 
															 String varShortName) {
						if ( varShortName.indexOf(".") > -1 ) 
								return StringHelper.javaName(ValueMap.substitute(cb.getValueMap(),
																									 varShortName));
						else {
								String varString =  cb.getProcessName() 
										+ "."
										+ varShortName;
								String javaName = 
										StringHelper.javaName(ValueMap.substitute
																					(cb.getValueMap(),
																					 varString));
								return javaName;
						}
				}

		private String getLeftHandSide(String tok) {
				StringTokenizer tokenizer = 
						new StringTokenizer(tok, "=");
				String lastTok = null;
				
				if ( tokenizer.hasMoreTokens()) {
						String wholeLeftSide = 
								tokenizer.nextToken();
						//System.out.println("whole left side " + wholeLeftSide);
						StringTokenizer leftTok = 
								new StringTokenizer(wholeLeftSide, " ");
						// get the last token 
						int i = 0;
						String nextTok = null;
						for ( i = 0; i <= leftTok.countTokens(); i++) {
								nextTok = leftTok.nextToken();
								if ( nextTok.equals("+") ||
										 nextTok.equals("-") ) 
										break;
								lastTok = nextTok;
						}
						//System.out.println("How many tokens " + i);
				}	
				return lastTok;
				
		}
		
		private String getRightHandSide(String tok) {
				// WHere is the first = 
				int firstEqualPos = tok.indexOf("=");
				if ( firstEqualPos <= -1 )
						return "";
				return tok.substring(firstEqualPos+1, tok.length());
		}

		public String getActionDisplayName() {
				switch ( actionType ) {
				case DependencyTablePanel.ADD_CODE:
						return "Runs Generic Code";
				case DependencyTablePanel.ADD_VARIABLE:
						return "Adds Variable";
				case DependencyTablePanel.INIT_VARIABLE: 
						return "Initializes Variable";
				case DependencyTablePanel.TRIGGER_EVENT:
						return "Triggers";
				case DependencyTablePanel.SCHEDULE_EVENT:
						return "Schedules";
				case DependencyTablePanel.INSTANTIATE:
						return "Instantiates";
				case DependencyTablePanel.MODIFY_VARIABLE:
						return "Modifies Variable";
				case DependencyTablePanel.MODIFY_SCHEDULE:
						return "Modifies Schedule";
				} // switch actionType
				return "UNdefined Action Pretty Name";
		}
		
		public Variable getVariable() {
				Object decVar = null;
				if ( variable == null ) {
						if ( oncAction instanceof ModifyVariableAction)
								decVar = 
										((ModifyVariableAction)oncAction).getVariable();
						else if ( oncAction instanceof InitVariableAction) {
								decVar = 
										((InitVariableAction)oncAction).getVariable();
						}
						else if ( oncAction instanceof AddVariableAction)
								decVar = 
										((AddVariableAction)oncAction).getVariable();
						else 
								return null;
						if ( decVar != null ) {
								variable = new Variable(ppCB, decVar.toString());
						}
						else
								return null;
				}
				return variable;
		}

		public String toString() {
				return oncAction.toString();
		}
}// end class PreprocessedAction
