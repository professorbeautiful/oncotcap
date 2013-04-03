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

import javax.swing.tree.*;
import oncotcap.datalayer.persistible.CodeBundle;

/**
 * @author   morris
 * @created  April 11, 2003
 */
public class OncViewerTreeNode extends DefaultMutableTreeNode  {

	/** */
	public CodeBundle codeBundle = null;
	/** */
	public String detailString = null;

	public String code = null;
	/** */
	public String name = null;
	/** */
	public OncViewerTreeNode parent = null;
	/** */
	public Class type = null;

	/**
	 * Constructor for the OncViewerTreeNode object
	 *
	 * @param displayString Description of Parameter
	 * @param parent        Description of Parameter
	 */
	public OncViewerTreeNode(String displayString,
													 OncViewerTreeNode parent) {
			this.parent = parent;
			this.detailString = displayString;
	}

	/**
	 * Gets the Code attribute of the ViewerObject object
	 *
	 * @return  The Code value
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the CodeBundle attribute of the OncViewerTreeNode object
	 *
	 * @param cb The new code bundle value
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets the CodeBundle attribute of the OncViewerTreeNode object
	 *
	 * @return   The CodeBundle value
	 */
	public CodeBundle getCodeBundle() {
		return codeBundle;
	}

	/**
	 * Sets the CodeBundle attribute of the OncViewerTreeNode object
	 *
	 * @param cb The new code bundle value
	 */
	public void setCodeBundle(CodeBundle cb) {
		codeBundle = cb;
	}

		public void setParentNode(OncViewerTreeNode parentNode){
				parent = parentNode;
		}
		
		public OncViewerTreeNode getParentNode() {
				return parent;
		}
		

	/**
	 * @return   Description of the Returned Value
	 */
	public String toString() {
		return detailString;
	}

}
