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
package oncotcap.display.editor.persistibleeditorpanel;

import java.awt.*;
import java.awt.event.*;

import java.awt.datatransfer.*;
import java.awt.dnd.*;

import java.util.*;
import java.lang.reflect.Array;
import javax.swing.*;
import javax.swing.tree.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.OncTreeNode;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.Keyword;
import oncotcap.datalayer.persistible.BooleanExpression;

import oncotcap.datalayer.persistible.OntologyObjectName;
import oncotcap.datalayer.persistible.TcapLogicalOperator;
import oncotcap.datalayer.persistible.parameter.TcapString;
import oncotcap.display.editor.persistibleeditorpanel.BooleanExpressionEditorPanel;
import edu.stanford.smi.protege.model.*;

public class BooleanTreeNodeEditor extends DefaultTreeCellEditor {
		// This is the only method defined by ListCellRenderer.
		// We just reconfigure the JLabel each time we're called.
		JTree tree = null;
		DefaultTreeCellRenderer renderer = null;
		JComponent editorComponent = null;

		public BooleanTreeNodeEditor(JTree tree, DefaultTreeCellRenderer renderer){
				super(tree, renderer);
				this.tree = tree;
				this.renderer = renderer;
		}

		public boolean isCellEditable(java.util.EventObject anEvent) {
				return true;
		}
		public boolean shouldSelectCell(EventObject anEvent) {
				if( editorComponent != null &&  anEvent instanceof MouseEvent
						&& ((MouseEvent)anEvent).getID() == MouseEvent.MOUSE_PRESSED )
						{
								Component dispatchComponent = SwingUtilities.getDeepestComponentAt(tree, 3, 3 );
								MouseEvent e = (MouseEvent)anEvent;
								MouseEvent e2 = new MouseEvent( dispatchComponent, MouseEvent.MOUSE_RELEASED,
																								e.getWhen() + 100000, e.getModifiers(), 3, 3, e.getClickCount(),
																								e.isPopupTrigger() );
								dispatchComponent.dispatchEvent(e2); 
								e2 = new MouseEvent( dispatchComponent, MouseEvent.MOUSE_CLICKED,
																		 e.getWhen() + 100001, e.getModifiers(), 3, 3, 1,
																		 e.isPopupTrigger() );
								dispatchComponent.dispatchEvent(e2); 
						}
				return false;
		}
		public Component getTreeCellEditorComponent(JTree tree,
																									Object value,
																									boolean isSelected,
																									boolean expanded,
																									boolean leaf,
																									int row,
																									boolean hasFocus) {
				editorComponent = (JComponent)value;
				Component nodeComponent = new JLabel("EMPTY");
				System.out.println("I guess i can try to edit" + value.getClass() );
				//System.out.println("tree value " + value + " " + value.getClass());
				// TREE NODE
				if ( value instanceof DefaultMutableTreeNode) {
						Object userObj = ((DefaultMutableTreeNode)value).getUserObject();
						if ( userObj  instanceof BooleanExpression ) {
								nodeComponent = new BooleanExpressionEditorPanel();
						}
				}
				return nodeComponent;
		}

}
