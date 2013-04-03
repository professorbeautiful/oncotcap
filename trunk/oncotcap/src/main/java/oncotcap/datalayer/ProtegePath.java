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
package oncotcap.datalayer;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import oncotcap.util.CollectionHelper;

import edu.stanford.smi.protege.model.*;

/**
 * Find the slots/frames necessary to traverse from one protege class to another
 * @author     morris
 * @created    March 11, 2003
 */
public class ProtegePath extends Object {
	/** Ordered list of classes between two protege classes*/
	private Vector classPath = new Vector();
	/** Desitination class */
	private Cls endClass = null;
	/** */
	private boolean found = false;
	private boolean foundDefinedPath = false;
	/** */
	private KnowledgeBase kb = null;
	/** Protege Knowledge Base */
	private int pathIndex = -1;
	/** Ordered list of slot names between two protege classes*/
	private Vector slotPath = new Vector();
	/** origin class*/
	private Cls startClass = null;

		/** tree that holds paths between pts */
		private ProtegeClassPath pathTree = null; 
		private Slot clsPathSlot = null;
		private Slot clsTypeSlot = null;

	public ProtegePath() {
	}
		
	/**
	 *  Constructor for the ProtegePath object
	 *
	 * @param  startClass Description of Parameter
	 * @param  endClass   Description of Parameter
	 * @param  kb         Description of Parameter
	 */
	public ProtegePath(String startClass, String endClass, KnowledgeBase kb) {


		this.kb = kb;
		this.startClass = kb.getCls(startClass);
		this.endClass = kb.getCls(endClass);
		clsPathSlot = kb.getSlot("clsPath");
		clsTypeSlot = kb.getSlot("clsType");
		// Create an omit class list to keep the search from
		// looping back on itself
		// Always add Keyword class siince it will definitely create a loop
		Vector superClasses = new Vector(this.startClass.getSuperclasses());
		
		// See if omiting subclasses helps or hurts
		Vector omitClasses = null;
		Vector subClasses = new Vector(this.startClass.getSubclasses());
	
		omitClasses = new Vector(CollectionHelper.or(superClasses,subClasses)); 
		omitClasses.addElement(kb.getCls("Keyword"));
		omitClasses.addElement(this.startClass);
// 		System.out.println("superclasses " + superClasses 
// 											 + " subClasses " + subClasses
// 											 + " omitClasses " + omitClasses);


		findPath(this.startClass, this.endClass, omitClasses);
	// 	System.out.println("ProtegePath " + this 
// 											 + " Defined Path " + pathTree);

	}

	

	/**
	 *  Constructor for the ProtegePath object
	 *
	 * @param  startClass Description of Parameter
	 * @param  endClass   Description of Parameter
	 */
	public ProtegePath(Cls startClass, Cls endClass) {
		this.startClass = startClass;
		this.endClass = endClass;
		//System.out.println("ProtegePath " + startClass 
		//		   + " end class " + endClass);
		// Create an omit class list to keep the search from
		// looping back on itself
		// Always add Keyword class siince it will definitely create a loop
		Vector superClasses = new Vector(this.startClass.getSuperclasses());
		
		// See if omiting subclasses helps or hurts
		Vector omitClasses = null;
		Vector subClasses = new Vector(this.startClass.getSubclasses());
		omitClasses = new Vector(CollectionHelper.or(superClasses,subClasses)); 
		omitClasses.addElement(kb.getCls("Keyword"));
		omitClasses.addElement(this.startClass);
	// 	// Always add Keyword class siince it wiall definitely create a loop
// 		Vector omitClasses = new Vector(this.startClass.getSuperclasses());
// 		omitClasses.addElement(kb.getCls("Keyword"));
// 		omitClasses.addElement(this.startClass);
// 		System.out.println("superclasses " + superClasses 
// 											 + " subClasses " + subClasses
// 											 + " omitClasses " + omitClasses);
		findPath(startClass, endClass, new Vector());
// 		System.out.println("ProtegePath " + this 
// 		+ " Defined Path " + pathTree);

	}


	public ProtegePath(Persistible startPers, Persistible endPers, 
										 KnowledgeBase kb) {
		this.kb = kb;
		Instance startInstance = kb.getInstance(startPers.getGUID().toString());
		Instance endInstance = kb.getInstance(endPers.getGUID().toString());
		this.endClass = endInstance.getDirectType();
		this.startClass = startInstance.getDirectType();
		findPath(startClass, endClass, new Vector());
	// 		System.out.println("ProtegePath " + this 
// 												 + " Defined Path " + pathTree);
	}

	/**
	 *  Constructor for the ProtegePath object
	 *
	 * @param  endClassName   Description of Parameter
	 * @param  targetInstance Description of Parameter
	 * @param  kb             Description of Parameter
	 */
	public ProtegePath(String endClassName, Instance targetInstance,
										 KnowledgeBase kb, String pathId) {
			this.kb = kb;
			this.endClass = kb.getCls(endClassName);
			
			// FUTURE - For these protege classes don't find direct type
			// find the type directly beneath the browser node type
			this.startClass = targetInstance.getDirectType();
			
			findPath(startClass, endClass, new Vector(), pathId);
	// 		System.out.println("ProtegePath " + this 
// 												 + " Defined Path " + pathTree);
	}
	/**
	 *  Constructor for the ProtegePath object
	 *
	 * @param  endClassName   Description of Parameter
	 * @param  targetInstance Description of Parameter
	 * @param  kb             Description of Parameter
	 */
	public ProtegePath(String endClassName, Instance targetInstance, KnowledgeBase kb) {

		this.kb = kb;
		this.endClass = kb.getCls(endClassName);

		// FUTURE - For these protege classes don't find direct type
		// find the type directly beneath the browser node type
		this.startClass = targetInstance.getDirectType();

		findPath(startClass, endClass, new Vector());
		//System.out.println("ProtegePath " + this 
												 //+ " Defined Path " + pathTree);

		//System.out.println("Path slots: " + slotPath);
	}


	/**
	 *  Adds a feature to the Class attribute of the ProtegePath object
	 *
	 * @param  aClass The feature to be added to the Class attribute
	 */
	public void addClass(Cls aClass) {
			classPath.addElement(aClass);
			//System.out.println(classPath);
	}

		public void addToOmitClasses(Vector classes, Cls cls) {
				// - don't assume that just because the superclass was a dead end
				// that the subclassess would be a dead end also classes.addAll(cls.getSubclasses());
				classes.addElement(cls);
		}


	/**
	 *  Adds a feature to the Slot attribute of the ProtegePath object
	 *
	 * @param  aSlot The feature to be added to the Slot attribute
	 */
	public void addSlot(Slot aSlot) {
		slotPath.addElement(aSlot);
	}

	/**
	 * @param  fromClass   Description of Parameter
	 * @param  toClass     Description of Parameter
	 * @param  omitClasses Description of Parameter
	 */
		public boolean findDefinedPath(String pathId) {
				//System.out.println("Find defined path " + pathId);
				clsPathSlot = kb.getSlot("clsPaths");
				clsTypeSlot = kb.getSlot("clsType");

				Cls searchPath = kb.getCls("SearchPath");
				Slot slot = kb.getSlot("searchPathId");
				Collection searchPaths = searchPath.getInstances();
				Iterator i = searchPaths.iterator();
				while (i.hasNext()) {
						Instance inst = (Instance)i.next();
						if ( pathId.equals((String)inst.getOwnSlotValue(slot)) ) {
								// This is the desired path
								constructPath(inst);
								return true;
						}
				}
				return false;
		}
	/**
	 * @param  fromClass   Description of Parameter
	 * @param  toClass     Description of Parameter
	 * @param  omitClasses Description of Parameter
	 */
		public boolean findDefinedPath(Cls fromCls, Cls toCls) {
				//System.out.println("Find defined path " );
				clsPathSlot = kb.getSlot("clsPaths");
				clsTypeSlot = kb.getSlot("clsType");
				Slot fromClsSlot = kb.getSlot("fromCls");
				Slot toClsSlot = kb.getSlot("toCls");


				Cls searchPath = kb.getCls("SearchPath");
				Collection searchPaths = searchPath.getInstances();
				Iterator i = searchPaths.iterator();
				while (i.hasNext()) {
						Instance inst = (Instance)i.next();
						if ( fromCls.equals((Cls)inst.getOwnSlotValue(fromClsSlot))
								 && toCls.equals((Cls)inst.getOwnSlotValue(toClsSlot))) {
								// This is the desired path
								constructPath(inst);
								return true;
						}
				}
				return false;
		}

		private void constructPath(Instance searchPath) {
				Instance inst = 
						(Instance)searchPath.getOwnSlotValue(clsPathSlot); 
				// Start here and build the tree 
 				fillInClassPath(inst, 
												(Cls)searchPath.getOwnSlotValue(kb.getSlot("toCls")));
		}

		private ProtegeClassPath fillInClassPath(Instance inst, Cls toCls) {
				ProtegeClassPath classPath = new ProtegeClassPath();
				ProtegeSlotPath slotPath = null;
				if (pathTree == null )
						pathTree = classPath;
				Cls startingCls = 
						(Cls)inst.getOwnSlotValue(kb.getSlot("clsType"));

				classPath.setCls(startingCls);
				if ( toCls == startingCls )
						return classPath;
				Collection slotPaths = 
						inst.getOwnSlotValues(kb.getSlot("slotPaths"));
				//classPath.setSlotPaths(new Vector(slotPaths));
				Iterator i = slotPaths.iterator();
				while ( i.hasNext() ) {
						slotPath = fillInSlotPath((Instance)i.next(), toCls);
						//System.out.println("Add Slot Path " + slotPath);
						classPath.addSlotPath(slotPath);
				}
				return classPath;
		}

		private ProtegeSlotPath fillInSlotPath(Instance inst, Cls toCls) {
				ProtegeSlotPath slotPath = new ProtegeSlotPath();
				ProtegeClassPath clsPath = null;
				Slot slot = 
						(Slot)inst.getOwnSlotValue(kb.getSlot("slotType"));
				slotPath.setSlot(slot);
				Collection clsPaths = 
						inst.getOwnSlotValues(kb.getSlot("clsPaths"));
				//slotPath.setClassPaths(new Vector(clsPaths));
				Iterator i = clsPaths.iterator();
				while ( i.hasNext() ) {
						clsPath = fillInClassPath((Instance)i.next(), toCls);
						slotPath.addClassPath(clsPath);
				}
				return slotPath;
		}


	/**
	 * @param  fromClass   Description of Parameter
	 * @param  toClass     Description of Parameter
	 * @param  omitClasses Description of Parameter
	 */
	public void findPath(Cls fromClass,
	                     Cls toClass,
	                     Vector omitClasses,
											 String pathId) {
			//			oncotcap.util.ForceStackTrace.showStackTrace();
			if ( pathId != null ) {
					// If a specific path is being requested
					foundDefinedPath = findDefinedPath(pathId);
					if ( pathTree == null ) {
							System.out.println("Error: Unable to find predefined path " 
																 + pathId);
					}
			}
			else {
					// if a defined path exists as default path
					foundDefinedPath = findDefinedPath(fromClass, toClass);
					//return;
			}
			findPath(fromClass, toClass, omitClasses);
	// 		System.out.println("ProtegePath " + this 
// 			+ " Defined Path " + pathTree);
	}

	/**
	 * @param  fromClass   Description of Parameter
	 * @param  toClass     Description of Parameter
	 * @param  omitClasses Description of Parameter
	 */
	public void findPath(Cls fromClass,
	                     Cls toClass,
	                     Vector omitClasses) {
			//					oncotcap.util.ForceStackTrace.showStackTrace();

// 			System.out.println("findPath from " + fromClass + " TO " + toClass);
			// If the to and from classes are the same only allow condition in 
			// special cases Keyword, submodels & submodel groups
			if ( fromClass == toClass ) {
					if (fromClass == kb.getCls("Keyword") ) {
							// Force the path to go via children 
							// otherwise the tree can have unexpected 
							// results
							addSlot(kb.getSlot("childKeywords"));
							addClass(toClass);
							found();
							return;
					}
					else if ( fromClass == kb.getCls("StatementTemplate") ) {
							// Force the path to go via statement bundle 
							// then back to statement template
							addSlot(kb.getSlot("statementBundles"));
							addSlot(kb.getSlot("statementTemplate"));
							addClass(kb.getCls("StatementBundle"));
							addClass(kb.getCls("StatementTemplate"));
							found();
							return;
					}
					else if ( fromClass == kb.getCls("StatementBundle") ) {
							// Force the path to go via statement bundle 
							// then back to statement template
							addSlot(kb.getSlot("statementTemplate"));
							addSlot(kb.getSlot("statementBundles"));
							addClass(kb.getCls("StatementTemplate"));
							addClass(kb.getCls("StatementBundle"));
							found();
							return;
					}
					else
							return;

			}
			else {
					// if a defined path exists as default path
					if ( (foundDefinedPath = findDefinedPath(fromClass, toClass)) == true ) {
							//System.out.println("Here is definedPath " + pathTree);
							return;
					}
		 
			}

		// Get all the slots that the class has
		omitClasses.addElement(fromClass);

		Collection fromClassSlots = fromClass.getTemplateSlots();

		// - TRY ANOTHER WAY
		// FUTURE - Get subclasses of the to class to see
		// if this slot contains any of them

		// Look ahead to see if the class exists if not do the normal
		// loop thing
		for (Iterator i = fromClassSlots.iterator(); i.hasNext(); ) {
			//Determine if the slot is a classInstance or Class Type
			Slot fromSlot = (Slot)i.next();

			if (fromSlot.getValueType() != ValueType.INSTANCE) {
				continue;
			}

			// Get all the classes allowed to be connected to this class
			// via this slot
			// Collection allowedClasses = fromSlot.getAllowedClses();
			Collection allowedClasses = getAllAllowedClasses(fromSlot);
			
			if (allowedClasses.contains(toClass)) {
				// Found the class you are looking for
				addSlot(fromSlot);
				addClass(toClass);
				found();
				return;
			}
		}
		

		// -TRY ANOTHER WAY

		//If there aren't any slots and you haven't reached
		// your destination return to the caller
		// The class was not located in the look ahead - continue looking
		// Loop thru slots
		for (Iterator i = fromClassSlots.iterator(); i.hasNext(); ) {
			//Determine if the slot is a classInstance or Class Type
			Slot fromSlot = (Slot)i.next();
// 			System.out.println("Look at slot " + fromSlot + 
// 												 "from class " + fromClass);
			if (fromSlot.getValueType() != ValueType.INSTANCE) {
				continue;
			}
			
			Collection allowedClasses = getAllAllowedClasses(fromSlot);
//  			System.out.println("allowed classes " + allowedClasses);
			for (Iterator j = allowedClasses.iterator(); j.hasNext(); ) {
				Cls allowedClass = (Cls)j.next();

				// If the class is in the omit class list move on to next one
				if (omitClasses.contains(allowedClass) == true) {
						continue;
				}

				// Otherwise check if it is the class we are looking
				// for
				if (allowedClass == toClass) {
					addSlot(fromSlot);
					addClass(allowedClass);
					found();
					return;
				}
				else if (allowedClass.hasSuperclass(
					kb.getCls("BrowserNodeWithKeywords"))) {
 					// 	System.out.println("adding class " + allowedClass 
					// 							 + " slot " + fromSlot);
					addSlot(fromSlot); 
					addClass(allowedClass);
					addToOmitClasses(omitClasses,allowedClass);

					findPath(allowedClass, toClass,
									 omitClasses);
					if (isFound()) {
						return;
					}
					else {
						// if all classpaths are removed then remove the slot path
						removeSlot();
						removeClass();
					}

				}
			}// for
		}
		return;
	}

		public static Vector getAllAllowedClasses(Slot fromSlot) {
				// Get all the classes allowed to be connected to this class
				// via this slot
				Vector directAllowedClasses = new Vector(fromSlot.getAllowedClses());
				// Get all the subclasses of allowed classes also 
				Vector allowedClasses = new Vector();
				allowedClasses.addAll(directAllowedClasses);
				Iterator ii = directAllowedClasses.iterator();
				while ( ii.hasNext() ) {
						Cls allowedClass = (Cls)ii.next();
						Collection subclasses  = allowedClass.getSubclasses();
						allowedClasses.addAll(subclasses);
				}
				return allowedClasses;
		}


	/**Indicate that the desired class has been found */
	public void found() {
		found = true;
	}


	/** */
	public void notFound() {
		found = false;
	}


	/** Remove a class that lead to a dead end*/
	public void removeClass() {
		int index = classPath.size() - 1;

		if (index > -1) {
			classPath.removeElementAt(index);
		}
	}


	/** Remove a slot t hat lead to a dead end*/
	public void removeSlot() {
		int index = slotPath.size() - 1;

		if (index > -1) {
			slotPath.removeElementAt(index);
		}
	}


	/**
	 *  Gets the ClassPath vector of the ProtegePath object
	 *
	 * @return    The ClassPath value
	 */
	public Vector getClassPath() {
		return classPath;
	}


	/**
	 *  Gets the SlotPath Vector ProtegePath object
	 *
	 * @return    The SlotPath value
	 */
	public Vector getSlotPath() {
		return slotPath;
	}

		public ProtegeClassPath getTreePath() {
				return this.pathTree;
		}

	/**
	 *  Gets the Found attribute of the ProtegePath object
	 *
	 * @return    The Found value
	 */
	public boolean isFound() {
		 return found;
	}
	public boolean hasDefinedPath() {
		return foundDefinedPath;
	}

		public String toString() {
				return "ProtegePath: " + startClass + " to " + endClass
						+ " using classes " + classPath + " and slots " + slotPath;
		}


		/**
		 * The main program for the OncViewer class
		 *
		 * @param args The command line arguments
		 */
		public static void main(String[] args) {
				Collection protegeErrors = new ArrayList();
				Project project = new Project("u:/wdir/TcapData/oncotcap.pprj",
																			protegeErrors);
				ProtegePath path = new ProtegePath(args[0], args[1],
																					 project.getKnowledgeBase());
				System.out.println("PATH " + path);
		}
}

