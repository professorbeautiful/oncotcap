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
package oncotcap.datalayer;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import oncotcap.util.CollectionHelper;

import edu.stanford.smi.protege.model.*;

public class ProtegeSlotPath extends Object implements ProtegePathElement {
		private Slot theSlot = null;
		private Vector classPaths = null;

		public ProtegeSlotPath() {
		}

		public Slot getSlot() {
				return theSlot;
		}
		public void setSlot(Slot aSlot) {
				theSlot = aSlot;
		}

		public Vector getClassPaths() {
				return 	this.classPaths;
		}

		public void setClassPaths(Vector classPaths) {
				this.classPaths = classPaths;
		}
		public void addClassPath(ProtegeClassPath classPath) {
				if ( classPaths == null )
						classPaths = new Vector();
				classPaths.addElement(classPath);
				//System.out.println("ClassPATHs " + classPaths); 
		}
		public StringBuffer addToName(StringBuffer nameString, 
																	String separator) {
				if (separator != null) 
						nameString.append(separator);
				nameString.append("\t");
				nameString.append(this.getSlot().getName());
				if ( classPaths != null ) {
						Iterator i = classPaths.iterator();
						while (i.hasNext()) {
									((ProtegeClassPath)i.next()).addToName(nameString,
													"-->(slot) ");
						}
				}
				return nameString;
    }
		public StringBuffer addToName() {
				// Visit each node 
				return addToName(new StringBuffer(), null);
		}
		public String toString() {
				return addToName().toString();
		}
}
