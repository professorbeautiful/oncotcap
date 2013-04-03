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
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.swing.*;

import javax.swing.tree.TreeSelectionModel;
import oncotcap.datalayer.persistible.CodeBundle;

/**
 * @author    morris
 * @created   April 24, 2003
 */
public class OncViewer extends JFrame {
	/** */
	private JPanel editorContainer = new JPanel();
	private JPanel leftPanel = new JPanel(new BorderLayout());
	/** */
	private URL helpURL;
	/** */
	private JEditorPane htmlPane;
	/** */
	private String lineStyle = "Angled";
	//Optionally play with line styles.  Possible values are
	//"Angled", "Horizontal", and "None" (the default).
	/** */
	private boolean playWithLineStyle = false;
	/** */
	private JSplitPane rightSplitPane = null;
	private JSplitPane leftSplitPane = null;
	/** */
		private JList codeBundleList = new  JList();
		private JList statementTemplateList = new  JList();
		private JList infoSourceList = new  JList();
	private static boolean DEBUG = false;

	/** Constructor for the OncViewer object */
	public OncViewer() {
		super("OncViewer");
		setSize(new Dimension(700,600));
		//Create the nodes.
		OncViewerTreeNode top = new OncViewerTreeNode("Onc Processes & Onc Events", null);
		buildTree(top);

		//Create a tree that allows one selection at a time.
		final JTree tree = new JTree(top);
		tree.getSelectionModel().setSelectionMode
		(TreeSelectionModel.SINGLE_TREE_SELECTION);

		if (playWithLineStyle) {
			tree.putClientProperty("JTree.lineStyle", lineStyle);
		}

		//Create the scroll pane and add the tree to it.
		JScrollPane treeView = new JScrollPane(tree);

		//Create the HTML viewing pane. to hold generated code
		htmlPane = new JEditorPane();
		htmlPane.setEditable(false);

		JScrollPane htmlView = new JScrollPane(htmlPane);

		// Create an editor viewing pane
		JPanel editorPanel = new JPanel(new BorderLayout());
		JPanel editorButtonPanel = new JPanel();
		editorButtonPanel.add(new JButton("Update"));
		editorButtonPanel.add(new JButton("Help"));
		JScrollPane editorView = new JScrollPane();
		editorPanel.add(editorView, BorderLayout.CENTER);
		editorPanel.add(editorButtonPanel, BorderLayout.SOUTH);


		//Add the scroll panes to a split pane.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		leftPanel.add(treeView, BorderLayout.CENTER);

		// Add a tabbed pane with lists of selected items
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Statement Bundles", statementTemplateList);
		tabbedPane.addTab("Code Bundles", codeBundleList);
		tabbedPane.addTab("Information Sources", infoSourceList);

		leftSplitPane.setTopComponent(leftPanel);
		leftSplitPane.setBottomComponent(tabbedPane);

		splitPane.setTopComponent(leftSplitPane);
		splitPane.setBottomComponent(rightSplitPane);

		Dimension minimumSize = new Dimension(300, 200);
		htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(300);
		splitPane.setPreferredSize(new Dimension(600, 300));

		// Split the right pane into two parts
		rightSplitPane.setDividerLocation(300);
		//XXX: ignored in some releases
		rightSplitPane.setPreferredSize(new Dimension(700, 400));

		// The upper panel can hold the editor
		rightSplitPane.setTopComponent(editorPanel);

		// THe lower panel will display and codebundle code
		JLabel codeBundleLabel = new JLabel("Generated Code", 0);
		JPanel codeBundlePanel = new JPanel(new BorderLayout());
		codeBundlePanel.add(codeBundleLabel, BorderLayout.NORTH);
		codeBundlePanel.add(htmlView, BorderLayout.CENTER);
		rightSplitPane.setBottomComponent(codeBundlePanel);

		//Add the split pane to this frame.
		getContentPane().add(splitPane, BorderLayout.CENTER);
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(new JMenu("Empty Menu"));
		getContentPane().add(menuBar, BorderLayout.NORTH);

		//Listen for when the selection changes.
		OncViewerSelectionListener oncTreeListener =
				new OncViewerSelectionListener(tree, editorView, htmlPane);
		tree.addTreeSelectionListener(oncTreeListener);
		fillLists();
		checkConsistency();
	}

		private void fillLists() {
				Collection codeBundles =
						oncotcap.Oncotcap.getDataSource().find(CodeBundle.class);
				codeBundleList.setListData(new Vector(codeBundles));
				// Find all statement bundles related to selected code bundles
				Collection statementTemplates =
						oncotcap.Oncotcap.getDataSource().find(
																									 oncotcap.datalayer.persistible.StatementTemplate.class);
// ,
// 																	codeBundles,
// 																	false,
// 																	true);
				statementTemplateList.setListData(new Vector(statementTemplates));

// 			 all statement bundles related to selected code bundles
				Collection infoSources =
						oncotcap.Oncotcap.getDataSource().find(oncotcap.datalayer.autogenpersistible.InformationSource.class);
// 																	"InformationSource",
// 																	codeBundles,
// 																	true);
				infoSourceList.setListData(new Vector(infoSources));
		}

		private void checkConsistency() { 
				// Loop through all selected code bundles
				// not implemented yet
		}

	/**
	 * @param top Description of Parameter
	 */
		private void buildTree(OncViewerTreeNode top) {
				OncViewerTreeNode oncProcess = null;
				// Go through the list of oncviewer objects and build the tree
				OncViewerTreeNodes o = new OncViewerTreeNodes();
				Vector viewables = o.getOncViewerTreeNodes();
				for (int i = 0; i < viewables.size(); i++) {
						OncViewerTreeNode ovo =
								(OncViewerTreeNode)viewables.elementAt(i);
						if (ovo.parent == null){
								top.add(ovo);
						}
						else {
								ovo.parent.add(ovo);
						}
				}
		}
		
		/**
		 * @param url Description of Parameter
		 */
		private void displayURL(URL url) {
				try {
						htmlPane.setPage(url);
				}
				catch (IOException e) {
						System.err.println("Attempted to read a bad URL: " + url);
				}
		}


	/**
	 * The main program for the OncViewer class
	 *
	 * @param args The command line arguments
	 */
	public static void main(String[] args) {
		JFrame frame = new OncViewer();
	
		frame.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});

		frame.pack();
		frame.setVisible(true);
	}
}
