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

import java.awt.datatransfer.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.beans.XMLEncoder;

import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.lang.reflect.Array;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import oncotcap.datalayer.persistible.CodeBundle;

import oncotcap.Oncotcap;

import oncotcap.display.common.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.EditorFrame;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.ListDialog;
import oncotcap.display.browser.*;
import oncotcap.util.*;
import java.awt.dnd.*;


/**
 * @author    morris
 * @created   April 24, 2003
 */
public class OncMergeBrowser extends JFrame 
		implements TreeModelListener,
							 OntologyPanelListener,
							 TreePanelListener,
							 ActionListener {
		public static JFrame mainWindow = null;
		private static JPopupMenu mergePopup = null;
		static OncoTCapDataSource masterDataSource = null;
		static OncoTCapDataSource localDataSource = null;

		static Vector ontologyTrees = new Vector();
		static OncMergeBrowser oncBrowser = null;
		static boolean suggestKeywords = false;
		static boolean saveWorkspace = true;
		static boolean useWorkspaceSettings = true;
		static OntologyTreeMerge o1 = null;
		static OntologyTreeMerge o2 = null;
		static Properties props = null;
		static Integer localProjectVersion = null;
		static Integer masterProjectVersion = null;

		static public int switchOrientation = 2;
		static Dimension defaultSize = new Dimension(700,650);
		static GenericTree tree = null;
		Vector selectedObjects = new Vector();
		JScrollPane scrollPane = null;
		OntologyButtonPanel ontologyButtonPanel = null;


		OntologyTabbedPanel tabbedMainPanel  = null;
		static OncMergeBrowser frame = null;
		
		/** */
		private JSplitPane topSplitPane = null;
		/** */
		private JList codeBundleList = new  JList();
		private JList statementTemplateList = new  JList();
		private JList infoSourceList = new  JList();
		
		private Collection statementTemplates  = null;
		private static boolean DEBUG = false;
		
		private final static String WINDOWS =
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		public String sillyVariable = "Michele";
		
		
		/** Constructor for the OncBrowser object */
		public OncMergeBrowser() {
				super(" OncoTcap Merge Browser " 
							+ "0.0.0");
				Oncotcap.setDataSourceMode(false); // make it multiple data sources
				setSize(new Dimension(800,600));
				if(mainWindow == null) mainWindow = this;
				setIconImage(OncoTcapIcons.getDefault().getImage());
				JPanel mainMenuPanel = new JPanel(new BorderLayout());
				JMenuBar menuBar = new JMenuBar();
				menuBar.add(new JMenu("Empty Menu"));
				ModelBuilderToolbarPanel mainToolBar = new ModelBuilderToolbarPanel();
				mainToolBar.addActionListener(this);
				MergeToolBarPanel mergeToolBar = new MergeToolBarPanel();
				mainMenuPanel.add(mainToolBar, BorderLayout.WEST);		
				mainMenuPanel.add(mergeToolBar, BorderLayout.EAST);		
				getContentPane().add(mainMenuPanel, BorderLayout.NORTH);
				checkConsistency();
	}

		private void init() {
				initMergePopup();
				//Add the scroll panes to a split pane.
				JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
				topSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				
				// 	tabbedMainPanel = new OntologyTabbedPanel();
				// 		tree.addTreePanelListener(tabbedMainPanel);
		
				o1 = new OntologyTreeMerge(localDataSource);
				o1.setName("LocalModelOntologyTree");
				o1.collapseController();
				addOntologyTree(o1);
				o2 = new OntologyTreeMerge(masterDataSource);
				o2.setName("MasterModelOntologyTree");
				o2.addOntologyPanelListener(o1);
				// Use one controller to control both trees
				o1.setOntologyButtonPanel(o2.getOntologyButtonPanel());
				addOntologyTree(o2);

				// Listen to changes in the trees 
				o1.getTree().addTreePanelListener(this);
				o2.getTree().addTreePanelListener(this);
				o2.addOntologyPanelListener(this);
				o1.setLabel("Local Project" + o1.getDataSource().toString());
				o2.addExtraTree(o1);
				

		
				// Make upper tree display info source tree by default
				int rootName = OntologyMap.IS;
				String useWorkspaceSettings = 
						props.getProperty("OncBrowser.useWorkspaceSettings");
				if ( "true".equals(useWorkspaceSettings) ) {
						rootName = OntologyMap.getButtonIndex
								(props.getProperty("TopOntologyTree.root", 
																	 OntologyMap.INFO_SOURCE)).intValue();
						System.out.println("rootName " + rootName + " for " 
															 + o1.getName()+".root"
															 + " " + props.getProperty("TopOntologyTree.root"));
				}
				o1.getOntologyButtonPanel().setRoot(rootName);
				//o1.getOntologyButtonPanel().setLeaves
				//		(CollectionHelper.makeVector(new Integer(OntologyMap.K)));
				o1.changed(new OntologyPanelEvent(o1.getOntologyButtonPanel(), null));
				
				// Make lower tree display keyword tree by default
				o2.getOntologyButtonPanel().setRoot(rootName);
				o2.changed(new OntologyPanelEvent(o2.getOntologyButtonPanel(), null));
				// Determine instance status  by comparing projects 
				colorCodeTrees();
				
			// 	System.out.println("DataSources " + AbstractPersistible.dataSources);
// 				for (Enumeration e = AbstractPersistible.dataSources.keys() ; 
// 						 e.hasMoreElements() ;) {
// 						Object key = e.nextElement();
// 						System.out.println("key " + key 
// 															 + " hashtable " + AbstractPersistible.dataSources.get(key));
// 				}

				// 		o2.getOntologyButtonPanel().setLeaves
				// 				(CollectionHelper.makeVector(new Integer(OntologyMap.K)));
				topSplitPane.setTopComponent(o2);
				//topSplitPane.setBottomComponent(o1);
				//topSplitPane.setDividerLocation(350);
				topSplitPane.setOneTouchExpandable(true);
				topSplitPane.setDividerSize(10);
				// Set a proportional location
				topSplitPane.setDividerLocation(0.5);
				splitPane.setTopComponent(topSplitPane);
				
				//splitPane.setDividerLocation(700);
				// 	splitPane.setBottomComponent(tabbedMainPanel);
				
				Dimension minimumSize = new Dimension(300, 200);
				splitPane.setPreferredSize(new Dimension(900,700));
				splitPane.setOneTouchExpandable(true);
				splitPane.setDividerSize(10);
				
				//Add the split pane to this frame.
				getContentPane().add(splitPane, BorderLayout.CENTER);
				setSize(new Dimension(800,600));
				setVisible(true);
				//o1.refresh();
				//o2.refresh();
				o1.getTree().pruneNodes(Color.white);
				o2.getTree().pruneNodes(Color.white);
		}
		public void initMergePopup() {
				if ( mergePopup == null ) {
						// define the mergePopup
						mergePopup = new JPopupMenu();
						JMenuItem mi;
						mi = new JMenuItem("Merge");
						mi.setAction(new MergeAction("Merge to Master"));
						mergePopup.add(mi);
						mergePopup.setOpaque(true);
						mergePopup.setLightWeightPopupEnabled(true);
						
				}
		}
		public static Integer getMasterProjectVersion() {
				return masterProjectVersion;
		}
		public void colorCodeTrees() {
				GenericTree localProjectTree = o1.getTree();
				GenericTree masterProjectTree = o2.getTree();
				OncoTCapDataSource localProjectDataSource = o1.getDataSource();
				OncoTCapDataSource masterProjectDataSource = o2.getDataSource();
				//System.out.println("DATASOURCE 1 " + localProjectDataSource 
				// 						 + " DATASOURCE 2 " + masterProjectDataSource);
				Hashtable localProjectHashtable = localProjectTree.getTreeNodeHashtable();
				Hashtable masterProjectHashtable = 
						masterProjectTree.getTreeNodeHashtable();
				Collection localProjectNodes = localProjectHashtable.keySet();
				Collection masterProjectNodes = masterProjectHashtable.keySet();

				Collection allNodes = 
						CollectionHelper.or(localProjectNodes, masterProjectNodes);
				if ( allNodes == null || allNodes.size() == 0)
						return;

				Iterator i = allNodes.iterator();
				while ( i.hasNext() ) {
						Persistible pers = (Persistible)i.next();
						Persistible localProjectPers = 
								AbstractPersistible.getPersistible(pers.getGUID(),
																									 localProjectDataSource);	
						Persistible masterProjectPers = 
								AbstractPersistible.getPersistible(pers.getGUID(),
																									 masterProjectDataSource);
// 						System.out.println(" pers, local, master" + 
// 															 pers + ", " 
// 															 + localProjectPers + ", " 
// 															 + masterProjectPers);
						//Instance deleted in the local project
						if ( masterProjectPers != null && localProjectPers == null) {
								// local in the master project
								
								if ( masterProjectPers.getVersionNumber() != null &&
										 masterProjectPers.getVersionNumber().intValue() > 
										 localProjectVersion.intValue() )
										masterProjectPers.setForeground(Color.green);
								else
										masterProjectPers.setForeground(Color.orange);
						}
						// Instance deleted in the master project
						else if ( masterProjectPers == null 
											&& localProjectPers != null) {
								// and changed in the local project
								if ( localProjectPers.getVersionNumber() != null &&
										 localProjectPers.getVersionNumber().intValue() 
										 > localProjectVersion.intValue()) 
										localProjectPers.setForeground(Color.green);
								else
										localProjectPers.setForeground(Color.orange);
						}
						// The instance exists in both projects
						else if ( masterProjectPers != null 
											&& localProjectPers != null)  {
								if ( (masterProjectPers.getForeground() == Color.gray) 
										|| (localProjectPers.getForeground() == Color.gray) )
										continue;
																	
								boolean changedInLocalProjectPers = false;
								boolean changedInMasterProjectPers = false;
								// if the instance is changed in localProjectPers
								if ( localProjectPers.getVersionNumber() != null &&
										 localProjectPers.getVersionNumber().intValue() > 
										 localProjectVersion.intValue() ) {
										changedInLocalProjectPers = true;
								}
								// if the instance is changed in masterProjectPers 
								if (  masterProjectPers.getVersionNumber() != null &&
											masterProjectPers.getVersionNumber().intValue() > 
											localProjectVersion.intValue() ) {
										changedInMasterProjectPers = true;
								}
								// Instance has changed in local project and the master 
								// project - most difficult change to analyze
								if ( changedInLocalProjectPers 
										 && changedInMasterProjectPers ) {
										localProjectPers.setForeground(Color.red);
										masterProjectPers.setForeground(Color.red);
								}
								// Changed in local project only
								else if ( changedInLocalProjectPers )
										localProjectPers.setForeground(Color.blue);
								// Changed in master project only
								else if ( changedInMasterProjectPers ) 
										masterProjectPers.setForeground(Color.blue);
								// Same in both projects - probably shouldn't show
								else {
										localProjectPers.setForeground(Color.white);
										masterProjectPers.setForeground(Color.white);
								}									
						}
				}
		}
		
		public static void pruneNodes(Color color) {
					GenericTree localProjectTree = o1.getTree();
					GenericTree masterProjectTree = o2.getTree();
					localProjectTree.pruneNodes(color);
					masterProjectTree.pruneNodes(color);
		}

		public static boolean mergeNode(GenericTreeNode copiedNode, 
																		GenericTree masterTree) {
				AbstractPersistible abstractPers = 
						((AbstractPersistible)copiedNode.getUserObject());
				// Determine if this is an existing object in the 
				// master project
				//oncotcap.util.ForceStackTrace.showStackTrace();
				GUID guid = abstractPers.getGUID();
				abstractPers.setDataSource
						(masterTree.getOntologyTree().getDataSource());
				abstractPers.setForeground(Color.gray);
				abstractPers.setVersionNumber
						(OncMergeBrowser.getMasterProjectVersion());
				abstractPers.update();
				masterTree.addNode(abstractPers, true);
				refresh();
				return true;	
		}

		public static void showPopup(JTree tree, int x, int y) {
				// If this is the local tree 
				if ( tree.equals(o1.getTree())) {
						// Show the popup
						mergePopup.show(tree, x,y);

				}
		}

		static public void addOntologyTree(OntologyTree oTree) {
				ontologyTrees.addElement(oTree);
		}

		static public void removeOntologyTree(OntologyTree oTree) {
				ontologyTrees.removeElement(oTree);

		}

	static public Iterator getOntologyTreeIterator() {
				return ontologyTrees.iterator();
		}

	static public void refresh() {
				// Now loop through ontology trees and refresh them too 
				Iterator i = ontologyTrees.iterator();
				while ( i.hasNext() ) {
						System.out.println("OB Refreshing tree...");
						OntologyTree oTree = (OntologyTree)i.next();
						oTree.updateTree();
						oTree.getTree().pruneNodes(Color.white);
				}
		}

		public static void setSuggestKeywords(boolean b) {
				suggestKeywords = b;
		}
		
		public static boolean suggestKeywords() {
				return suggestKeywords;
		}
		public static void setSaveWorkspace(boolean b) {
				saveWorkspace = b;
		}
		
		public static boolean saveWorkspace() {
				return saveWorkspace;
		}
		public static void setUseWorkspaceSettings(boolean b) {
				useWorkspaceSettings = b;
		}
		
		public static boolean useWorkspaceSettings() {
				return useWorkspaceSettings;
		}

		// Merge nodes from local project to master project
		public static void mergeLocalToMaster() {
				// Get trees
				GenericTree localTree = o1.getTree();
				GenericTree masterTree = o2.getTree();

				// Get transferHandlers
				GenericTreeTransferHandler localTransferHandler = 
						(GenericTreeTransferHandler)localTree.getTransferHandler();
				GenericTreeTransferHandler masterTransferHandler = 
							(GenericTreeTransferHandler)masterTree.getTransferHandler();
				
				// Create a transferable from the local tree - 
				// a list of selected nodes from the local tree
				Transferable localTransferables =
						localTransferHandler.createTransferable(localTree);
				
				// Import nodes into master project tree
				// Make sure master tree has root node selected or 
				// no node selection
				masterTree.clearSelection();
				masterTransferHandler.importData(masterTree, localTransferables);
				
		}
		
		// tree panel listener
		public void changed(TreePanelEvent evt) {
				// The user changed their selections update the tree
				//System.out.println("TREE CHANGED: " + evt); 
		}
		public void changed(OntologyPanelEvent evt) {
				// The user changed their selections update the tree
				//System.out.println(this + " heard you " );
				colorCodeTrees();
				o1.getTree().pruneNodes(Color.white);
				o2.getTree().pruneNodes(Color.white);

		}


		class OntologyTabbedPanel extends JPanel
				implements TreePanelListener,
									 ChangeListener {
				JTabbedPane tabbedPane = null;
				OncScrollList statementTemplateList = null;
				OncScrollList nuggetList = null;
				OncScrollList codeBundleList = null;
				OncScrollList infoSourceList = null;
				OncScrollList encodingList = null;
				OncScrollList keywordList = null;
				OncScrollList submodelList = null;
				OncScrollList submodelGroupsList = null;
				OncScrollList modelControllerList = null;
				OncScrollList oncProcessList = null;
				OncScrollList statementBundleList = null;
				OncScrollList interpretationList = null;
				OncScrollListButtonPanel buttonPanel   = null;
				Vector tabList = new Vector();
				public OntologyTabbedPanel() {
						init();
				}
				
				private void init() {
						setLayout(new BorderLayout());
						JPanel northPanel = new JPanel();
					// 	buttonPanel = 
// 								new OncScrollListButtonPanel();
						GridBagLayout g = new GridBagLayout();
						GridBagConstraints c3 = new GridBagConstraints();
						g.columnWeights = new double[]{0.0f, 1.0f};
						northPanel.setLayout(g);
						northPanel.add(new JLabel("Items Related to Selected Tree Node"),
													 c3);
						c3.anchor = GridBagConstraints.SOUTHEAST;
						c3.insets = new Insets(8,5,5,5);
// 						northPanel.add(buttonPanel, c3);
						add(northPanel,
								BorderLayout.NORTH);
		
						// Add a tabbed pane with lists of selected items
						tabbedPane = new JTabbedPane();

						// Make this a method
						codeBundleList = new OncScrollList(CodeBundle.class, false, false);
						tabbedPane.addTab("Code Bundle", 
															codeBundleList);
						tabList.addElement(codeBundleList);
						encodingList = new OncScrollList(Encoding.class, false, false);
						tabbedPane.addTab("Encoding", encodingList);
						tabList.addElement(encodingList);

						infoSourceList  = 	new OncScrollList(InformationSource.class,
																									false, false);
						tabbedPane.addTab("Info Source", infoSourceList);
						tabList.addElement(infoSourceList);
						interpretationList  = 	new OncScrollList(Interpretation.class,
																									false, false);
						tabbedPane.addTab("Interpretation", interpretationList);
						tabList.addElement(interpretationList);

						keywordList = new OncScrollList(Keyword.class, false, false);
						tabbedPane.addTab("Keyword", 
															keywordList);
						tabList.addElement(keywordList);

						nuggetList = new OncScrollList(KnowledgeNugget.class, false, false);
						tabbedPane.addTab("Knowledge Nugget", 
															nuggetList);
						tabList.addElement(nuggetList);

						modelControllerList = new OncScrollList(ModelController.class,
																											false, false);
						tabbedPane.addTab("Model Controller",
															modelControllerList);
						tabList.addElement(modelControllerList);

						submodelList = new OncScrollList(SubModel.class,
																											false, false);
						tabbedPane.addTab("SubModel", 
															submodelList);
						tabList.addElement(codeBundleList);

						submodelGroupsList = new OncScrollList(SubModelGroup.class,
																											false, false);
						tabbedPane.addTab("SubModel Group", 
															submodelGroupsList);
						tabList.addElement(submodelGroupsList);

						statementBundleList = new OncScrollList(StatementBundle.class,
																											false, false);
						tabbedPane.addTab("Statement Bundle", 
															statementBundleList);
						tabList.addElement(statementBundleList);

						statementTemplateList = new OncScrollList(StatementTemplate.class,
																											false, false);
						tabbedPane.addTab("Statement Template", 
															statementTemplateList);
						tabList.addElement(statementTemplateList);
						add(tabbedPane,	BorderLayout.CENTER);

						// Register a change listener
						tabbedPane.addChangeListener(this);
					
				}
				
				
				public void stateChanged(ChangeEvent evt) {
// 						System.out.println("TAB CHANGED: " + evt);
						JTabbedPane pane = (JTabbedPane)evt.getSource();
						
						// Get current tab
						Component list = pane.getSelectedComponent();
// 						System.out.println("buttonPanel " + buttonPanel);
// 						System.out.println("OntologyMap " + ontologyButtonPanel);
// 						buttonPanel.setScrollList((OncScrollList)list);
// 						tree.refresh();
				}

				public void changed(TreePanelEvent evt) {
						// The user changed their selections update the tree
		 				System.out.println("TREE CHANGED: " + evt);
						Object userObject = evt.getSource();
						if ( userObject instanceof Persistible ) {
								if ( selectedObjects.size() == 1) 
										selectedObjects.set(0,userObject);
								else 
										selectedObjects.add(0,userObject);
						}	
						refreshTabs();
				}

				public void refreshTabs() {
						// 						System.out.println("SELECTED OBJECTS : " +
						// 															 selectedObjects );
						if ( selectedObjects.size() > 0) {
							Object first =	selectedObjects.firstElement();
						}
						Iterator i = tabList.iterator();
						while (i.hasNext()) {
								OncScrollList tabList = (OncScrollList)i.next();
								tabList.clear();
								// TEMPORARILY DISABLE 
								// tabList.setData(getList(tabList.getListType(),
// 																			 selectedObjects));
								tabList.setLinkTos(selectedObjects);
						}
				}
				
				
		}

		private Vector getList(Class ontologyType, 
												 Collection relatedItems) {
			// NEED NEW FIND	Collection resultSet =
// 						oncotcap.Oncotcap.getDataSource().find(
// 																									 ontologyType,
// 																									 relatedItems,
// 																									 false,
// 																									 true);
				return new Vector(); //resultSet);
		}


		private void setList(Class ontologyType, 
												 Collection relatedItems, // filter
												 JList theList) {
			// REPLACE FIND 	Collection resultSet =
// 						oncotcap.Oncotcap.getDataSource().find(
// 																	ontologyType,
// 																	relatedItems,
// 																	false,
// 																	true);
				theList.setListData(new Vector()); // resultSet));
		}

		private void checkConsistency() { 
				// Loop through all selected code bundles
				// not implemented yet
		}


		public JFrame getFrame() {
				return this;
		}
		
		static public OncMergeBrowser getOncBrowser() {
				return oncBrowser;
		}
		static public GenericTree getTree() {
				return tree;
		}

		private void setTree() {
		}
		/**
		 * The main program for the OncViewer class
		 *
		 * @param args The command line arguments
		 */
		public static void main(String[] args) {
				try {
						UIManager.setLookAndFeel(WINDOWS);
				}
				catch (Exception ex) {
						System.out.println("Failed loading L&F: ");
				}
				UIManager.put( "Table.selectionBackground",new Color(228,225,220)); 
				UIManager.put( "Table.selectionForeground",Color.BLACK); 
				UIManager.put( "List.selectionBackground",new Color(228,225,220));
				UIManager.put( "List.selectionForeground",Color.BLACK); 
				oncotcap.Oncotcap.handleCommandLine(args);
				loadWorkspace();
				frame = new OncMergeBrowser();
				oncBrowser = frame;
 				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				WindowListener windowListener = new WindowAdapter() {
								public void windowActivated(WindowEvent e) {
										Component recentFocusOwner = 
												oncBrowser.getMostRecentFocusOwner();
								}
								
								public void windowClosing(WindowEvent e) {
										// Check to see if the user wants to save 
										// Check to see if the user wants to save workspace
										// Save workspace
										saveWorkspaceToFile();
										int saveAnswer = 1;

										if (o1 == null ) // nothing ever loaded
												System.exit(0);
										if (oncotcap.Oncotcap.getDataSource().hasChanged() == true) {
												saveAnswer = JOptionPane.showConfirmDialog
														(frame, 
														 "Would you like to save changes", 
														 "information",
														 JOptionPane.YES_NO_CANCEL_OPTION, 
														 JOptionPane.INFORMATION_MESSAGE);
												if ( saveAnswer == 0 ){
														boolean successful = 
																oncotcap.Oncotcap.getDataSource().commit();
														if ( successful )
																System.exit(0);
												}
												else if ( saveAnswer == 1 ) {
														System.exit(0);
												}
										}
										else {
												System.exit(0);
										}
								}
						};

				frame.addWindowListener(windowListener);
				frame.pack();
				frame.setVisible(true);
		}
				
		private static void loadProjects(String masterProjectName, 
																		 String localProjectName) {
				masterDataSource = 
						new ProtegeDataSource(masterProjectName);
				localDataSource = 
						new ProtegeDataSource(localProjectName);
				// Register the data sources
				AbstractPersistible.addDataSource(masterDataSource);
				AbstractPersistible.addDataSource(localDataSource);
				oncotcap.Oncotcap.setDataSource(masterDataSource);

				// Should only ever be one instance
				Vector localProjectInfos = 
						new Vector(localDataSource.find(ProjectInfo.class));
				if ( localProjectInfos.size() > 0)
						localProjectVersion = 
								((ProjectInfo)localProjectInfos.elementAt(0)).getVersionNumber();
				System.out.println("local " + localDataSource + " - " 
													 + localProjectInfos);
				System.out.println("localVersion " + localProjectVersion);
				Vector masterProjectInfos = new Vector(masterDataSource.find(ProjectInfo.class));
				if ( masterProjectInfos.size() > 0) {
						ProjectInfo projectInfo = 
								(ProjectInfo)masterProjectInfos.elementAt(0);
						System.out.println("master " + projectInfo);

						masterProjectVersion = projectInfo.getVersionNumber();
						// Increment the master projects version 
						System.out.println("master " + masterDataSource);
						int n = masterProjectVersion.intValue();
						n++;
						masterProjectVersion = new Integer(n);
						projectInfo.setVersionNumber(masterProjectVersion);
						projectInfo.update();
						System.out.println("master " + projectInfo);
				}
				System.out.println("masterVersion " + masterProjectVersion);
		}


		// Collect the current workspace settings and save to a XML file
		private static void saveWorkspaceToFile() {
				try {
						// write property file for 1st pass
						
						FileOutputStream outputStream =
								new FileOutputStream("OncBrowserWorkspace.prop");
						Properties props = new Properties();
						props.setProperty("OncBrowser.suggestKeywords", 
															String.valueOf(OncBrowser.suggestKeywords()));
						props.setProperty("OncBrowser.saveWorkspaceOnExit", 
															String.valueOf(OncBrowser.saveWorkspace()));
						props.setProperty("OncBrowser.useWorkspaceSettings", 
															String.valueOf(OncBrowser.useWorkspaceSettings()));
						if ( o1 != null ) {
								AbstractButton btn = o1.getButton(OntologyTree.FILTER_TOGGLE_BUTTON);
								props.setProperty("TopOntologyTree.filterStatus",
																	String.valueOf(btn.isSelected()));
								btn = o1.getButton(OntologyTree.SIBLINGS_TOGGLE_BUTTON);
								props.setProperty("TopOntologyTree.siblingStatus",
																	String.valueOf(btn.isSelected()));
								// Get root name
								String rootName = OntologyMap.getRootNodeName
										(o1.getOntologyButtonPanel());
								props.setProperty("TopOntologyTree.root",
																	rootName);
								Vector leafNames = o1.getAllSelectedLeaves();
								props.setProperty("TopOntologyTree.leaves",
																	leafNames.toString());

						}
						props.store(outputStream, 
												"Workspace properties for OncoTCap browser");
						outputStream.flush();
						outputStream.close();
				} catch (Exception e) {
						e.printStackTrace();
				}
				
		}

		static private void loadWorkspace() {
				try {
						// load property file and set settings
						props = new Properties();
						FileInputStream inputStream =
								new FileInputStream("OncBrowserWorkspace.prop");
						props.load(inputStream);
						if ( props.getProperty("OncBrowser.useWorkspaceSettings").equals
								 ("true") )
								setUseWorkspaceSettings(true);
						else
								setUseWorkspaceSettings(false);
						if ( useWorkspaceSettings() ) {
								if ( props.getProperty("OncBrowser.suggestKeywords").equals
										 ("true") )
										setSuggestKeywords(true);
								else
										setSuggestKeywords(false);
								
								if ( props.getProperty("OncBrowser.saveWorkspaceOnExit").equals
										 ("true") )
										setSaveWorkspace(true);
								else
										setSaveWorkspace(false);
						}
								
				} catch (Exception e) {
						System.out.println("No OncBrowserWorkspace.props file");
						return;
				}
				
				 
		}

		public static void showUserObjects(Object userObject) {
				GUID guid = null;
				if ( userObject instanceof Persistible ) {
						guid = ((Persistible)userObject).getGUID();
				}
				Persistible masterPersistible = 
						AbstractPersistible.getPersistible(guid, masterDataSource);
				Persistible localPersistible = 
						AbstractPersistible.getPersistible(guid, localDataSource);
				EditorFrame editorFrame = null;
				if ( masterPersistible != null ) {
						EditorPanel editorPanel =
								EditorFrame.showEditor
								((Editable)masterPersistible, 
								 new Dimension(800,600),
								 30, 30, oncBrowser);
						editorFrame = editorPanel.getMyFrame();
						if ( localPersistible != null ) {
								editorFrame.addEditor
										(((Editable)localPersistible).getEditorPanel(), true);
								
						}
				}
				else if  ( localPersistible != null ) {
						EditorPanel editorPanel =
								EditorFrame.showEditor((Editable)localPersistible, 
																			 new Dimension(800,600),
																			 30, 30, oncBrowser);
						editorFrame = editorPanel.getMyFrame();
						editorFrame.placeOnRightSplitPane();
				}

		}
		/*
		 * Save the the proteg project
		 */
		/**
		 * @param e Description of Parameter
		 */
		public void actionPerformed(ActionEvent e) {
				String projectDir = Oncotcap.getInstallDir() + "TcapData" 
						+ File.separator;
				JFileChooser chooser = new JFileChooser(projectDir);
				chooser.addChoosableFileFilter(new MyFilter());
				chooser.setDialogTitle("Select master project...");
				JFileChooser chooser2 = new JFileChooser(projectDir);
				chooser2.addChoosableFileFilter(new MyFilter());
				chooser2.setDialogTitle("Select local project...");
				int returnVal = chooser.showOpenDialog(null);
				int returnVal2 = chooser2.showOpenDialog(null);
				if ( returnVal2 == JFileChooser.APPROVE_OPTION && 
						 returnVal == JFileChooser.APPROVE_OPTION ) {
						System.out.println("loading " 
															 + chooser.getSelectedFile().getPath() +
															 " local " 
															 + chooser2.getSelectedFile().getPath());
						loadProjects(chooser.getSelectedFile().getPath(), 
												 chooser2.getSelectedFile().getPath());
						init();

    
 
				}
		}
		public void treeNodesChanged(TreeModelEvent e) {
		}
		
		public void treeNodesInserted(TreeModelEvent e) {	
		}
		
		public void treeNodesRemoved(TreeModelEvent e) {
		}

		public void treeStructureChanged(TreeModelEvent e) {
		}

		class MergeAction extends AbstractAction {
				public MergeAction(String name) {
						super(name);
				}
																						 
				public void actionPerformed(ActionEvent ae) {
						OncMergeBrowser.mergeLocalToMaster();
				}
		}

    
    class MyFilter extends javax.swing.filechooser.FileFilter {
        public boolean accept(File file) {
            String filename = file.getName();
            return file.isDirectory() || filename.endsWith(".pprj");
        }
        public String getDescription() {
            return "*.pprj";
        }
    }

}
