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

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;

import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import javax.accessibility.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.*;
import oncotcap.display.browser.*;


// *******************************************************
// *********  ToolBar Panel / Docking Listener ***********
// *******************************************************
/**
 * @author    morris
 * @created   April 22, 2003
 */
class TreeToolBarPanel extends JPanel
	implements ContainerListener {
	/** */
	ToggleButtonToolBar toolbar;
	/** */
	static Insets zeroInsets = new Insets(0, 0, 0, 0);

	/** Constructor for the TreeToolBarPanel object */
	public TreeToolBarPanel() {
		super();
		//System.out.println("Created toolbar panel");
		setLayout(new BorderLayout());
		toolbar = new ToggleButtonToolBar();
		add(toolbar, BorderLayout.CENTER);
		addContainerListener(this);
	}

	/**
	 * Gets the ToolBar attribute of the TreeToolBarPanel object
	 *
	 * @return   The ToolBar value
	 */
	public ToggleButtonToolBar getToolBar() {
		return toolbar;
	}

	/**
	 * @param e Description of Parameter
	 */
	public void componentAdded(ContainerEvent e) {
		Container c = e.getContainer().getParent();
		if (c != null) {
			c.getParent().validate();
			c.getParent().repaint();
		}
	}

	/**
	 * @param e Description of Parameter
	 */
	public void componentRemoved(ContainerEvent e) {
		Container c = e.getContainer().getParent();
		if (c != null) {
			c.getParent().validate();
			c.getParent().repaint();
		}
	}

	/**
	 * @param x Description of Parameter
	 * @param y Description of Parameter
	 * @return   Description of the Returned Value
	 */
	public boolean contains(int x, int y) {
		Component c = getParent();
		if (c != null) {
			Rectangle r = c.getBounds();
			return (x >= 0) && (x < r.width) && (y >= 0) && (y < r.height);
		}
		else {
			return super.contains(x, y);
		}
	}
}
