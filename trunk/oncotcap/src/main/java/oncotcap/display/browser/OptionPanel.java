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

public class OptionPanel extends JFrame {
		JPanel mainPanel = new JPanel();
		public JTabbedPane tabbedPane = null;
		JCheckBox suggestKeywordsCB = null;
		JCheckBox saveWorkspaceOnExitCB = null;
		JCheckBox useWorkspaceSettingsCB = null;

		public OptionPanel() {
				init();
		}
		
		private void init() {
				getContentPane().setLayout(new BorderLayout());
				JPanel northPanel = new JPanel();
			
				// Add a tabbed pane with lists of selected items
				tabbedPane = new JTabbedPane();
				tabbedPane.addTab("Global Settings", new GlobalSettingsPanel());
				tabbedPane.addTab("Keywords", new GlobalKeywordsPanel());
				JPanel buttonPanel = new JPanel();
				JButton okButton = new JButton("Ok");
				okButton.addActionListener( new ActionListener()
						{
								public void actionPerformed( ActionEvent actionEvent )
								{
										setOptions( actionEvent );
										setVisible(false);
								}
						} );

				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener( new ActionListener()
						{
								public void actionPerformed( ActionEvent actionEvent )
								{
										// close
										setVisible(false);
								}
						} );

				buttonPanel.add(okButton);
				buttonPanel.add(cancelButton);
				
				getContentPane().add(northPanel,
						BorderLayout.NORTH);
				getContentPane().add(tabbedPane,
						BorderLayout.CENTER);
				getContentPane().add(buttonPanel,
						BorderLayout.SOUTH);

		}

		private void setOptions(ActionEvent actionEvent) {
				
				OncBrowser.setSuggestKeywords(suggestKeywordsCB.isSelected() );
				System.out.println("suggestKeywordsCB.isSelected() " 
													 + suggestKeywordsCB.isSelected() );
				OncBrowser.setSaveWorkspace(saveWorkspaceOnExitCB.isSelected() );
				System.out.println("saveWorkspaceOnExitCB.isSelected() " 
													 + saveWorkspaceOnExitCB.isSelected() );
				OncBrowser.setUseWorkspaceSettings
						(useWorkspaceSettingsCB.isSelected());
				System.out.println("useWorkspaceSettingsCB.isSelected() " 
													 + useWorkspaceSettingsCB.isSelected() );
		}

		class GlobalSettingsPanel extends JPanel {
				public GlobalSettingsPanel () {
						init();
				}
				
				private void init() {
						setLayout(new GridBagLayout());
						GridBagConstraints gbc = new GridBagConstraints();
						suggestKeywordsCB = new JCheckBox("Suggest Keywords?");
						saveWorkspaceOnExitCB = 
								new JCheckBox("Save Workspace on Exit?");
						useWorkspaceSettingsCB = 
								new JCheckBox("Use Workspace Settings?");

						gbc.gridwidth = GridBagConstraints.REMAINDER;
						gbc.fill = GridBagConstraints.BOTH;
						gbc.anchor = GridBagConstraints.NORTHWEST;
						suggestKeywordsCB.setSelected(OncBrowser.suggestKeywords());
						saveWorkspaceOnExitCB.setSelected(OncBrowser.saveWorkspace());
						useWorkspaceSettingsCB.setSelected(OncBrowser.useWorkspaceSettings());

						add(suggestKeywordsCB, gbc);
						add(saveWorkspaceOnExitCB,gbc);
						add(useWorkspaceSettingsCB,gbc);
				}
		}

}
