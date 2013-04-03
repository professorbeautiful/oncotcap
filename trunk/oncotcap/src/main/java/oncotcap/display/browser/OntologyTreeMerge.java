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
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.util.*;


/**
 * @author    morris
 * @created   April 24, 2003
 */
public class OntologyTreeMerge extends OntologyTree {

		JSplitPane treeSplitPane = null;
		JLabel treeLabel = null;
	// /** Constructor for the OntologyTree object */
// 		public OntologyTree() {
// 				dataSource = oncotcap.Oncotcap.getDataSource();
// 				init();
				
// 		}
 		public OntologyTreeMerge(OncoTCapDataSource ds) {
 				dataSource = ds;
 				init();
 		}
		
		public void init() {
				setLayout(new BorderLayout());
				ToolTipManager.sharedInstance().setInitialDelay(1000);
				ToolTipManager.sharedInstance().setDismissDelay(3000);
				
				// Create a tree controller panel
				GridBagLayout gridBagLayout = new GridBagLayout();
				JPanel treeControllerPanel = new JPanel(gridBagLayout);
				
				FilterTree filterTree = new FilterTree();
				filterTreeBtn = new JButton(filterTree);
				filterTreeBtn.setIcon(OncBrowserConstants.filterIcon);
				ontologyButtonPanel = 
						new OntologyButtonPanel();
				ontologyMap.setOntologyButtonPanel(ontologyButtonPanel);
				ontologyButtonPanel.addOntologyPanelListener(this);
				JPanel treeControllerLabelPanel = new JPanel(new BorderLayout());
				JLabel treeControllerLabelText = 
						new JLabel("Tree Controller");
				treeControllerLabelText.setFont(new Font("Helvetica", Font.BOLD, 14));
				treeControllerLabelPanel.add(treeControllerLabelText, BorderLayout.CENTER);
				
				GridBagConstraints cc = new GridBagConstraints();
				cc.gridwidth = GridBagConstraints.REMAINDER;
				
				
				GridBagConstraints c = new GridBagConstraints();
				c.anchor = GridBagConstraints.NORTHWEST;
				c.fill = GridBagConstraints.BOTH;
				//ontologyButtonPanel.setMinimumSize(new Dimension(400,200));
				treeControllerPanel.add(ontologyButtonPanel,c);
				c.gridwidth = GridBagConstraints.REMAINDER;
				c.weighty = 1.0;
				
				mainFilterPanel = new JPanel(new BorderLayout());
				JPanel textPanel = new JPanel(new BorderLayout());
				String displayText = new String();
				OncScrollableTextArea nameField = 
						new OncScrollableTextArea(displayText, "DISPLAY", false);
				textPanel.add(nameField, BorderLayout.NORTH);
				filterPanel = new FilterEditorPanel();
				filterPanel.addOperators(BorderLayout.NORTH, SwingConstants.VERTICAL);
				filterPanel.addTextLabel();

				// Make sure that this object listens for changes in the filter tree
				((DefaultTreeModel)filterPanel.getTree().getModel()).addTreeModelListener(this);
				//mainFilterPanel.add(textPanel, BorderLayout.NORTH);
				mainFilterPanel.add(filterPanel, BorderLayout.CENTER);
				treeControllerPanel.add(mainFilterPanel,c);
				splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				javax.swing.plaf.basic.BasicBorders.SplitPaneBorder ourBorder = new
						javax.swing.plaf.basic.BasicBorders.SplitPaneBorder(Color.black, Color.blue) ;
				splitPane.setOneTouchExpandable(true);
				splitPane.setDividerSize(10);
				topSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			
				topSplitPane.setTopComponent(ontologyButtonPanel);
				topSplitPane.setForeground(Color.BLUE);
				topSplitPane.setBottomComponent(mainFilterPanel);
				topSplitPane.setOneTouchExpandable(true);
				topSplitPane.setDividerSize(10);
				
				initButtons();
				treeMainPanel.add(getTreePanel(), BorderLayout.CENTER);
				
				splitPane.setTopComponent(topSplitPane);
				JPanel masterTreePanel = new JPanel(new BorderLayout());
				treeLabel = new JLabel("Master Project");
				masterTreePanel.add(treeLabel, BorderLayout.NORTH);
				masterTreePanel.add(treeMainPanel, BorderLayout.CENTER);
				treeSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
				treeSplitPane.setDividerSize(10);
				treeSplitPane.setDividerLocation(0.5);

				treeSplitPane.setOneTouchExpandable(true);
				treeSplitPane.setTopComponent(masterTreePanel);
				//treeSplitPane.setBottomComponent(treeSplitPane);
				splitPane.setBottomComponent(treeSplitPane);
				
				registerAcceleratorKeys();
				
				Dimension minimumSize = new Dimension(300, 200);
				
				// Split the right pane into two parts
				//Add the split pane to this frame.
				add(splitPane, BorderLayout.CENTER);
				JPanel mainMenuPanel = new JPanel(new BorderLayout());
				JMenuBar menuBar = new JMenuBar();
				menuBar.add(new JMenu("Empty Menu"));
				ModelBuilderToolbarPanel mainToolBar = new ModelBuilderToolbarPanel();
				JButton filter = new JButton(new Filter("Filter"));
				checkConsistency();
		}
		public void setLabel(String label) {
				treeLabel.setText(label);
				repaint();
		}
		public void addExtraTree(OntologyTree ot) {
				treeSplitPane.setBottomComponent(ot);
				repaint();
		}
}

