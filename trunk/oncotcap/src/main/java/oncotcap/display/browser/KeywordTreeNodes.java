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

import java.util.*;
import java.lang.reflect.Array;
import oncotcap.datalayer.persistible.*;
import oncotcap.util.*;
import oncotcap.*;

/**
 * @author   morris
 * @created  April 24, 2003
 */
public class KeywordTreeNodes {
	/** Constructor for the KeywordObjects object */
	public KeywordTreeNodes() { }

	/**
	 * Gets the KeywordObjects attribute of the KeywordObjects object
	 *
	 * @return  The KeywordObjects value
	 */
	public Vector getOncViewerTreeNodes() {
		OncViewerTreeNode parent = null;
		// Get all the Keywords the current 'context' from the data source
		// and return a vector of them
		Collection keywords =
				Oncotcap.getDataSource().find(ReflectionHelper.classForName(
																							 "oncotcap.datalayer.persistible.Keyword"));

		// Create a hashtable to map the keywords to their tree node 
		// equivalant
		Iterator keyIter = keywords.iterator();
		Hashtable keywordHashtable = new Hashtable();
		while ( keyIter.hasNext() ) {
				Object keyObj = keyIter.next();
				System.out.println("Keyword obj: " + keyObj);
				if ( keyObj instanceof Keyword) {
						Keyword keyword = (Keyword)keyObj;
						OncViewerTreeNode treeNode = 
								new OncViewerTreeNode(keyword.keyword,
																			null);
						keywordHashtable.put(keyword, treeNode);
				}
		}
		// Probably should be making a tree model her -- 
		// FUTURE when making multipurpose model with switchable root
		// Now go through and add them to a vector and set their parent
		Vector listOfKeywords = new Vector();
		for (Enumeration e = keywordHashtable.keys() ; e.hasMoreElements() ;) {
         Keyword key = (Keyword)e.nextElement();
				 System.out.println(key);
				 OncViewerTreeNode keywordNode = 
						 (OncViewerTreeNode)keywordHashtable.get(key);
				 Keyword parentKeyword = key.getParentKeyword();
				 if ( parentKeyword != null ) {
						 OncViewerTreeNode parentKeywordNode = 
								 (OncViewerTreeNode)keywordHashtable.get(parentKeyword);
// 						 keywordNode.setParentNode(parentKeywordNode);
				 }
				 listOfKeywords.addElement(keywordNode);
     }

		return listOfKeywords;
	}


		// Build a tree and as you go build the code
		private Hashtable buildTree() {
				return null;
		}
}
