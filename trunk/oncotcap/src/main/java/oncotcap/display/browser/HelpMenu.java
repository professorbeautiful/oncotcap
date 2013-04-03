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
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import java.lang.reflect.*;
import javax.swing.ImageIcon;

import javax.help.*;
import java.net.URL;

import oncotcap.util.*;
import oncotcap.datalayer.Persistible;
import oncotcap.display.common.OncListModelListener;
import oncotcap.display.editor.persistibleeditorpanel.EditorPanel;

public class HelpMenu extends JMenu  {
		static public Hashtable windows = null;
		//static public Vector windowMenus = new Vector();
		static WindowListener windowListener = null;
		static JMenu menu = null;
		private JMenuItem mi = null;
		private String fileName = null;
		private HelpSet hs = null;
		private HelpBroker hb = null;
		private Object caller = null;

		Window window = null;
		public HelpMenu(String menuName, Object caller) {
				super(menuName);
				if ( menu == null ) 
						menu = this;
				this.caller = caller;
				init();
		}

		private void init() { 
				
				// Create a HelpBroker object:
//				hb = hs.createHelpBroker();
//				hb.setHelpSet(hs);
				System.out.println("HELP ID " + caller.getClass().getSimpleName());
				OncBrowser.enableHelp((Component)caller);
				mi = new JMenuItem("Oncotcap Help");
				mi.addActionListener(new CSH.DisplayHelpFromSource( OncBrowser.getHelpBroker() ));
				add(mi);


		}

		public String getHelpFileName(Object source) {
				return "Help.html";
		}
		public void displayHelp(String fileName) {
				try {
						oncotcap.util.BrowserLauncher2.openURL(fileName);
				} catch (java.net.MalformedURLException murle) { 
						return; 
				}
				catch (java.io.IOException ioe) { 
						System.out.println("IOException"); return; 
				}
				
		}

		public class ShowScreenHelpAction extends OncAbstractAction {
				Object source = null;
				
				public ShowScreenHelpAction(String actionName) {
						super(actionName);
				}
	
				public void setSource(Object source) {
						this.source = source;
				}
				public Object getSource() {
						return this.source;
				}
				
////				 1. create HelpSet and HelpBroker objects
//			     HelpSet hs = getHelpSet("sample.hs"); 
//			     HelpBroker hb = hs.createHelpBroker();
//
//			     // 2. assign help to components
//			     CSH.setHelpIDString(topics, "top");
//			     
//			     // 3. handle events
//			     topics.addActionListener(new CSH.DisplayHelpFromSource(hb));
				public void actionPerformed(ActionEvent e) {
						//Determine what screen is showing and display context specific
						// help access hashtable which maps screen to html file names
					System.out.println("ACTION PERFORMED " + e.getSource());
						if ( caller instanceof EditorPanel){
								
								hb.enableHelpKey(OncBrowser.getOncBrowser(), "top", null);

								CSH.setHelpIDString(((JComponent)caller), 
																		caller.getClass().toString());
						}
						else {
								JOptionPane.showMessageDialog
								(null, 
								 "Sorry. No context specific help exists at this time." );
						}
						
				}
				
		}

}
