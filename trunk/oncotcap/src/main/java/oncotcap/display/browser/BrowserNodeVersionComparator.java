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
import java.util.*;
import javax.swing.tree.*;

import oncotcap.datalayer.*;
import oncotcap.util.*;

public class BrowserNodeVersionComparator implements Comparator {
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
				return compare(n1.getUserObject(), n2.getUserObject());
		}
		public int compare(Persistible n1, 
											 Persistible n2) {
				Integer version1 = null;
				Integer version2 = null;
				version1 = n1.getVersionNumber();
				version2 = n2.getVersionNumber();
				return compare(version1, version2);
		}	

		public int compare(Integer i1,
											 Integer i2) {
				if ( i1 == null ) 
						i1 = new Integer(-1);
				if ( i2 == null ) 
						i2 = new Integer(-1);
				return i1.compareTo(i2);
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
