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

import java.io.Serializable;

public class OntologyObjectName implements Serializable {
		String name = null;
		public OntologyObjectName(String n) {
				name = n;
		}
		public OntologyObjectName() {
		}
		public String getName() {
			return this.name;
		}
		public void setName(String name){
			this.name = name;
		}
		public String toString() {
				return name;
		}
}
