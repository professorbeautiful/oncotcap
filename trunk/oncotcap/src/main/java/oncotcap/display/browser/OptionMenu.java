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
import java.beans.PropertyChangeListener;
import java.util.*;
import java.lang.reflect.*;
import javax.swing.ImageIcon;

import oncotcap.util.*;
import oncotcap.display.common.OncListModelListener;

public class OptionMenu extends JMenu {
	OptionPanel optionFrame = null;
	private JMenuItem itemK, itemG;
	
	public OptionMenu(String menuName) {
		super(menuName);
		optionFrame = new OptionPanel();
		if ( optionFrame == null ) 
			optionFrame = new OptionPanel();
		optionFrame.setSize(new Dimension(300, 300 ));
		optionFrame.setVisible(false);
		init();
	}
	
	private void init() { 
		itemK = add(new AbstractAction("Keywords"){
			public void actionPerformed(ActionEvent e) {
				optionFrame.tabbedPane.setSelectedIndex(1);	
				optionFrame.setVisible(true);
			}
		});
		itemG = add(new AbstractAction("Global Settings"){
			public void actionPerformed(ActionEvent e) {
				optionFrame.tabbedPane.setSelectedIndex(0);	
				optionFrame.setVisible(true);
			}
		});
	}
}
