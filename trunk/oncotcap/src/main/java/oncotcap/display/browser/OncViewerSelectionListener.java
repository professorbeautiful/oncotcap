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
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;

/**
 * @author    morris
 * @created   April 24, 2003
 */
public class OncViewerSelectionListener
	implements TreeSelectionListener {
	/** */
	JScrollPane editorView = null;
	/** */
	JEditorPane htmlPane = null;
	/** */
	JTree tree = null;

	/**
	 * Constructor for the OncViewerSelectionListener object
	 *
	 * @param tree            Description of Parameter
	 * @param editorContainer Description of Parameter
	 * @param htmlPane        Description of Parameter
	 */
	public OncViewerSelectionListener(JTree tree,
																		JScrollPane editorView,
																		JEditorPane htmlPane) {
		this.tree = tree;
		this.editorView = editorView;
		this.htmlPane = htmlPane;
	}

	/**
	 * @param e Description of Parameter
	 */
	public void valueChanged(TreeSelectionEvent e) {
		OncViewerTreeNode node = (OncViewerTreeNode)
		tree.getLastSelectedPathComponent();

		if (node == null) {
			return;
		}
		String code = node.getCode();
		htmlPane.setText(code);

		if (node.getUserObject() instanceof Editable) {
			// Put in the new editorpanel
			EditorPanel editorPanel =
			((Editable)node.getUserObject()).getEditorPanelWithInstance();
			editorView.setViewportView(editorPanel);
		}
	}
}
