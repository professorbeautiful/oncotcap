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

public class ProtegeClassPath extends Object implements ProtegePathElement {
		private Cls theCls = null;
		private Vector slotPaths = null;

		public ProtegeClassPath() {
		}
		
		public Cls getCls() {
				return theCls;
		}
		public void setCls(Cls aCls) {
				theCls = aCls;
		}

		public Vector getSlotPaths() {
				return 	this.slotPaths;
		}

		public void setSlotPaths(Vector slotPaths) {
				this.slotPaths = slotPaths;
		}
		public void addSlotPath(ProtegeSlotPath slotPath) {
				if ( slotPaths == null )
						slotPaths = new Vector();
				slotPaths.addElement(slotPath);
				//System.out.println("slotPATHs " + slotPaths); 
		}
		public void removeSlotPath(ProtegeSlotPath slotPath) {
				slotPaths.removeElement(slotPath);
		}
		public String toString() {
				return addToName().toString();
		}

		public StringBuffer addToName() {
				// Visit each node 
				return addToName(new StringBuffer(), null);
		}

		public StringBuffer addToName(StringBuffer nameString, 
																	String separator) {
				if (separator != null) 
						nameString.append(separator);
				nameString.append("\t");
				nameString.append(this.getCls().getName());
				if ( slotPaths != null ) {
						Iterator i = slotPaths.iterator();
						while (i.hasNext()) {
								Object obj = i.next();
								if ( obj instanceof ProtegeSlotPath) {
										((ProtegeSlotPath)obj).addToName(nameString,
																										 "-->(class) ");
								}
							// 	if (i.hasNext() ) 
// 										nameString.append(separator);
						}
				}
				return nameString;
    }

}
