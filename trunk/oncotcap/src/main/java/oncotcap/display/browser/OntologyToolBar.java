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
import javax.swing.JToolBar;



public class OntologyToolBar extends JToolBar {
		private OntologyTree ontologyTree = null;
		public OntologyToolBar(int orientation) {
				super(orientation);
		}
		public void setOntologyTree(OntologyTree ot) {
				ontologyTree = ot;
		}
		public OntologyTree getOntologyTree() {
				return ontologyTree;
		}
																					
}
