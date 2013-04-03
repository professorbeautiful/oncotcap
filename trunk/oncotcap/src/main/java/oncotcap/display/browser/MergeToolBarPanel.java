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


import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;


/**
 * @author    morris
 * @created   April 22, 2003
 */
public class MergeToolBarPanel extends ToolBarPanel implements ActionListener {
	/** */
	JToolBar toolbar = null;
		MergeCheckBox mergedCB = null;
		MergeCheckBox deletedModifiedCB= null;
		MergeCheckBox changedBothCB = null;
		MergeCheckBox modifiedCB = null;
		MergeCheckBox deletedCB= null;
		/**
	 * Constructor for the MergeToolbarPanel object
	 *
	 * @param modelBuilder Description of Parameter
	 */
	public MergeToolBarPanel()  {
		try {
			toolbar = new JToolBar();
			add(toolbar);
			mergedCB = new MergeCheckBox(Color.gray, "Merged");
			mergedCB.addActionListener(this);
			deletedModifiedCB = new MergeCheckBox(Color.orange, "Delete/Modified");
			deletedModifiedCB.addActionListener(this);
			changedBothCB = new MergeCheckBox(Color.red, "Modified in BOTH");
			changedBothCB.addActionListener(this);
			modifiedCB = new MergeCheckBox(Color.blue, "Modified");
			modifiedCB.addActionListener(this);
			deletedCB = new MergeCheckBox(Color.green, "Deleted");
			deletedCB.addActionListener(this);
			toolbar.add(mergedCB);
			toolbar.add(deletedModifiedCB);
			toolbar.add(changedBothCB);
			toolbar.add(modifiedCB);
			toolbar.add(deletedCB);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
		public void actionPerformed(ActionEvent ae) {
				// Reset the display
				MergeCheckBox mergeCheckBox = (MergeCheckBox)ae.getSource();
				// If the checkbox is unselected
				OncMergeBrowser.refresh();	
				if ( !mergedCB.isSelected()) 
						OncMergeBrowser.pruneNodes(mergedCB.getColor());
				if ( !modifiedCB.isSelected()) 
						OncMergeBrowser.pruneNodes(modifiedCB.getColor());
				if ( !deletedModifiedCB.isSelected()) 
						OncMergeBrowser.pruneNodes(deletedModifiedCB.getColor());
				if ( !deletedCB.isSelected()) 
						OncMergeBrowser.pruneNodes(deletedCB.getColor());
				if ( !changedBothCB.isSelected()) 
						OncMergeBrowser.pruneNodes(changedBothCB.getColor());
				
		}

		class MergeCheckBox extends JCheckBox  {
				Color color = null;
				public MergeCheckBox(Color color, String label) {
						super(label, true);
						this.color = color;
						setBackground(Color.white);
						setForeground(color);
						setBorder
								(BorderFactory.createLineBorder(color, 
																								5));
						setBorderPainted(true);
				}
				public Color getColor() {
						return color;
				}
		}



}
