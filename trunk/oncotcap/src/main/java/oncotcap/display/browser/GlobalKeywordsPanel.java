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
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.lang.reflect.Array;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.border.BevelBorder;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.tree.DefaultMutableTreeNode;

import oncotcap.display.common.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.browser.*;
import oncotcap.util.*;
import oncotcap.Oncotcap;

public class GlobalKeywordsPanel extends JPanel {
		static Hashtable keywordHashtable = new Hashtable();
		static Vector globalKeywords = loadKeywordsFromWorkspace();
		static OncScrollList scrollList = null;
		
		public GlobalKeywordsPanel() {
				init();
		}

		private void init(){
				//type, showButtons,  showLabel,  allowsMultiples
				scrollList = 
						new OncScrollList(globalKeywords, 
															"Place global keywords in list", 
															false, true);
				add(scrollList);
												
		}
		static public String getKeywordsString() {
				// Form a string with all the keywords in the keyword list
				Iterator i = null;
				if ( scrollList == null ) {
							i = globalKeywords.iterator();
				}
				else {
							i = scrollList.getData().iterator();
				}
				StringBuffer sb = null;
				if ( i.hasNext() ) { 
						sb = new StringBuffer();
						sb.append(((Keyword)i.next()).toString());
				}
				else 
						return new String();
				while (i.hasNext()) {
						sb.append(",");
						sb.append(((Keyword)i.next()).toString());
				}
				return sb.toString();
		}

		static public Collection getKeywords() {
				if (scrollList == null ) 
						return globalKeywords;
				return  scrollList.getData();
		}
		
		// Load any global keywords from the workspace file
		static public Vector loadKeywordsFromWorkspace() {
				Vector workspaceKeywords = new Vector();
				try { 
				Properties props = new Properties();
				FileInputStream inputStream =
						new FileInputStream("OncBrowserWorkspace.prop");
				props.load(inputStream);
				String keywordsProperty = 
						props.getProperty("OncBrowser.workspaceKeywords");
				// Parse into individual strings
				if ( keywordsProperty == null ) 
						return workspaceKeywords;
				StringTokenizer tokenizer = new StringTokenizer(keywordsProperty, ",");
				Collection allKeywords = null;
				if ( tokenizer.countTokens() > 0) {
						OncoTCapDataSource dataSource = Oncotcap.getDataSource();
						allKeywords = dataSource.find(Keyword.class);
						// Create a hashtable with keywords and the text as key
						Iterator keywords = allKeywords.iterator();
						Keyword keyword = null;
						while (keywords.hasNext()) {
								keyword = (Keyword)keywords.next();
								keywordHashtable.put(keyword.getKeyword(), keyword);
						}
						Keyword theKeyword = null;
						String keywordString = null;
						while ( tokenizer.hasMoreTokens() ) {
								keywordString = tokenizer.nextToken();
								// Get the keyword objects that match these keyword strings
								if ( (theKeyword = 
											(Keyword)keywordHashtable.get(keywordString)) != null) {
										workspaceKeywords.add(theKeyword);	
								}
						}
				}
				}catch(Exception ex) {
						ex.printStackTrace();
				}
				return workspaceKeywords;
		}
}
