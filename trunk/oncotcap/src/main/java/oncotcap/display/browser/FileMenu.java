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

import oncotcap.util.*;
import oncotcap.display.common.OncListModelListener;

public class FileMenu extends JMenu {
	
		public FileMenu(String menuName) {
				super(menuName);
				init();
		}

		private void init() { 
			add(new ProtegeSaveAction());
			add(new CloseAction());
			add(new BrowserSplitPaneAction("Show Top Window"));
			add(new BrowserSplitPaneAction("Show Bottom Window"));
			add(new BrowserSplitPaneAction("Show Both Windows"));
		}

	
}
