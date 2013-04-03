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

import java.util.*;
import oncotcap.datalayer.persistible.Keyword;
import oncotcap.datalayer.persistible.ProblemContext;
import oncotcap.display.common.OncTreeNode;
import oncotcap.datalayer.persistible.OncFilter;
import oncotcap.datalayer.persistible.TcapLogicalOperator;

/**
 * Superclass of any object that would like to be saved to the datasource
 * (protege, RDB,etc). Makes sure object has a GUID.
 *
 * @author   morris
 * @created  March 11, 2003
 *
 */
public abstract class PersistibleWithKeywords extends AbstractPersistible
{

	/** */
	public DefaultPersistibleList keywords = null;
	/** */
	public Vector problemContexts = null;


	/** Constructor for the Persistible object */
	public PersistibleWithKeywords() {
		super();
		keywords = new DefaultPersistibleList();
		
		}
	public PersistibleWithKeywords(oncotcap.util.GUID guid) {
		super(guid);
		keywords = new DefaultPersistibleList();
		
		}


	/**
	 * Gets the Keywords attribute of the PersistibleWithKeywords object
	 *
	 * @return   The Keywords value
	 */
	public PersistibleList getKeywords() {
		return keywords;
	}

	/**
	 * Gets the ProblemContexts attribute of the PersistibleWithKeywords object
	 *
	 * @return   The ProblemContexts value
	 */
	public Vector getProblemContexts() {
		return problemContexts;
	}

	/**
	 * Sets the Keywords attribute of the PersistibleWithKeywords object
	 *
	 * @param keywords The new keywords value
	 */
	public void setKeywords(java.util.Collection var) {
			if ( keywords== null)
					keywords = new DefaultPersistibleList();
			keywords.set(var);
	}

	/**
	 * Sets the Keywords attribute of the PersistibleWithKeywords object
	 *
	 * @param keywords The new keywords value
	 */
	public void setKeywords(Keyword keyword) {
			addKeyword(keyword);
	}


	/**
	 * Sets the ProblemContexts attribute of the PersistibleWithKeywords object
	 *
	 * @param problemContexts The new problem contexts value
	 */
	public void setProblemContexts(Vector problemContexts) {
		this.problemContexts = problemContexts;
	}

	/**
	 * Handle Keywords - regular and problem contexts
	 *
	 * @param keyword The feature to be added to the Keyword attribute
	 */
	public void addKeyword(Keyword keyword) {
		// Make sure the keyword is not already attached
		// directly to object
		if (keywords.indexOf(keyword) < 0) {
			keywords.addElement(keyword);
		}
	}


	// ProblemContexts
	/**
	 * Adds a feature to the ProblemContext attribute of the
	 * PersistibleWithKeywords object
	 *
	 * @param problemContext The feature to be added to the ProblemContext
	 *                       attribute
	 */
	public void addProblemContext(ProblemContext problemContext) {
		// Make sure the problemContext is not already attached
		// directly to object
		//System.out.println("Adding problem context " + problemContext.keyword
		//									 + " to " + this);
		if (problemContexts.indexOf(problemContext) < 0
		&& problemContext != null) {
			System.out.println("Adding problem context " + problemContext.keyword
			+ " to " + this);
			problemContexts.addElement(problemContext);
		}
	}

	/**
	 * Over-rides Object.equals() compares the object's guid for equalness.
	 *
	 * @param obj    Description of Parameter
	 * @return       A boolean. False if the compared object isn't a Persistible or
	 *               if the guid of the compared object isn't the same as the
	 *               current. True if the compared object has the same guid.
	 * @author       shirey
	 * @version      initial 04/07/2003
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Persistible) {
			return (guid.equals(((Persistible)obj).getGUID()));
		}
		else {
			return (false);
		}
	}

	/**
	 * Has the Keyword
	 *
	 * @param keyword Description of Parameter
	 * @return        true/false
	 */
	public boolean hasKeyword(Keyword keyword) {
		return keywords.contains(keyword);
	}

	/**
	 * @param keywordString Description of Parameter
	 * @return               Description of the Returned Value
	 */
	public boolean hasKeyword(String keywordString) {
		Vector keywordStrings = new Vector();
		//Build a list of the keyword strings
		Iterator i = keywords.iterator();
		while (i.hasNext()) {
			keywordStrings.addElement((String)((Keyword)i).keyword);
		}
		return keywordStrings.contains(keywordString);
	}

	/**
	 * Has the ProblemContext
	 *
	 * @param problemContext Description of Parameter
	 * @return               true/false
	 */
	public boolean hasProblemContext(ProblemContext problemContext) {
		return problemContexts.contains(problemContext);
	}

	/**
	 * @param problemContextString Description of Parameter
	 * @return                      Description of the Returned Value
	 */
	public boolean hasProblemContext(String problemContextString) {
		Vector problemContextStrings = new Vector();
		//Build a list of the problemContext strings
		Iterator i = problemContexts.iterator();
		while (i.hasNext()) {
			problemContextStrings.addElement((String)((ProblemContext)i).keyword);
		}
		return problemContextStrings.contains(problemContextString);
	}

	/**
	 * Over-rides Object.hashCode()
	 *
	 * @return   An integer hashCode derived from the object's GUID.
	 * @author   shirey
	 * @version  initial 04/07/2003
	 */
	public int hashCode() {
		return (guid.hashCode());
	}
}
