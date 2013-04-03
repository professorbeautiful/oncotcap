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

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.tree.*;

import oncotcap.datalayer.*;
import oncotcap.util.*;

public class BrowserNodeComparator implements Comparator, Serializable {
		public int compare(Object o1, Object o2) {
				if ( o1 instanceof GenericTreeNode && o2 instanceof GenericTreeNode )
						return compare((GenericTreeNode)o1, (GenericTreeNode)o2);
				if ( o1 instanceof Persistible && o2 instanceof Persistible )
						return compare((Persistible)o1, (Persistible)o2);
				if (  o1 instanceof String && o2 instanceof String )
						return compare((String)o1, (String)o2);
				return 0; // No comparison possible type not supported
		}
		public int compare(GenericTreeNode n1, 
											 GenericTreeNode n2) {
				String string1 = null;
				String string2 = null;
				if ( n1.getUserObject() instanceof String ) {
						string1 = (String) n1.getUserObject();
				}
				else 
						string1 = n1.getUserObject().toString();

				if ( n2.getUserObject() instanceof String ) {
						string2 = (String) n2.getUserObject();
				}
				else 
						string2 = n2.getUserObject().toString();
				return compare(string1, string2);
		}
		public int compare(Persistible n1, 
											 Persistible n2) {
				String string1 = null;
				String string2 = null;
				string1 = n1.toString();
				string2 = n2.toString();
				return compare(string1, string2);
		}	

		public int compare(String s1, 
											 String s2) {
				s1 = StringHelper.htmlToText(s1);
				s2 = StringHelper.htmlToText(s2);	
				if ( s1 == null )
						s1 = "";
				if ( s2 == null ) 
						s2 = "";
				return (s1.compareToIgnoreCase(s2));                         
		}
}
