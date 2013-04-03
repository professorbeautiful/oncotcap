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

public class GenericRootNodeObject extends Object {
		String name = null;
		public GenericRootNodeObject ( String n) {
				setName(n);
		}
		public void setName(String n) {
				name = n;
		}
		public String toString() {
				return name;
		}
}
