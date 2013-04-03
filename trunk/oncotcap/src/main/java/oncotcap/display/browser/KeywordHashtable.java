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

public class KeywordHashtable {
		// Get all the Keywords the current 'context' from the data source
		// and return a vector of them
		

	public Hashtable getTreeInstances(Keyword rootKeyword) {
			// Get all the Keywords the current 'context' from the data source
			// and return a vector of them
			Hashtable keywordHashtable = new Hashtable();
// 			return makeKeywordHashtable(rootKeyword, null);
						addKeyword(keywordHashtable, rootKeyword, null);
						return keywordHashtable;
	}
		
		private Hashtable makeKeywordHashtable(Keyword word, Keyword parent) {
				// Get all the Keywords the current 'context' from the data source
				// and return a vector of them
				Vector endingClassNames = new Vector();
				endingClassNames.addElement("Keyword");
				Hashtable fullKeywordTree = 
						oncotcap.Oncotcap.getDataSource().getInstanceTree
						("Keyword",
						 endingClassNames,
						 word);
				return  fullKeywordTree;
				// remove any node that is not the passed in keyword and 
				// is connected to root
		}

		private void addKeyword(Hashtable table, Keyword word, Keyword parent)
		{
				boolean recurse = true;
				
				if(table.containsKey(word))
						recurse = false;
				
				if(parent == null)
						table.put(word, CollectionHelper.makeVector(String.valueOf(-1)));
				else
						table.put(word, CollectionHelper.makeVector(parent));
				
				if(recurse)
						{
								Iterator it = word.getChildren().iterator();
								while(it.hasNext())
										{
												addKeyword(table, (Keyword) it.next(), word);
										}
						}
		}

		static public Hashtable getTreeInstances() {
				// Get all the Keywords the current 'context' from the data source
				// and return a vector of them
				Vector endingClassNames = new Vector();
				endingClassNames.addElement("Keyword");
				return oncotcap.Oncotcap.getDataSource().getInstanceTree("Keyword",
																											 endingClassNames);
		}

}
