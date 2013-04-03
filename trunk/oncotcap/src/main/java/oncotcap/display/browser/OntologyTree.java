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
import javax.swing.text.*;
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
import oncotcap.display.editor.EditorFrame;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.util.*;


/**
 * @author    morris
 * @created   April 24, 2003
 */
public class OntologyTree extends JPanel 
		implements OntologyPanelListener,
							 ListSelectionListener,
							 TreeModelListener ,
							 SaveListener,
							 TreePanelListener,
							 DoubleClickListener {

		static OntologyTree oncBrowser = null;
		static int treeCounter = 0;
		static Dimension defaultSize = new Dimension(700,650);
		static private KeyStroke ENTER_KS = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		// One button for each ontology object see OntologyMap
		// Additional buttons 0 - 11
		// NUmber of add buttons
		static public final int NUMBER_OF_ADD_BUTTONS = 11;
		//static public final int ADD_ROOT_BUTTON = 1;
		//static public final int ADD_LINK_BUTTON = 2;
		//static public final int ADD_KEYWORD_BUTTON = 3;
		static public final int COLLAPSE_BUTTON = 12;
		//static public final int EXPAND_BUTTON = 5;
		static public final int DELETE_BUTTON = 13;
		static public final int FIND_BUTTON = 14;
		static public final int UNLINK_BUTTON = 15;
		static public final int FILTER_TOGGLE_BUTTON = 16;
		static public final int SIBLINGS_TOGGLE_BUTTON = 17;
		static public final int SORT_TOGGLE_BUTTON = 18;
		static public final int SORT_BY_ALPHA = 1;
		static public final int SORT_BY_VERSION = 2;
		int sortMode = SORT_BY_ALPHA;

		OncoTCapDataSource dataSource = null;

		JScrollBar hScrollBar = null;
		JScrollBar vScrollBar = null;
		boolean isTreeModal = false;
		// static public final Hashtable plusButtonIcons = new Hashtable();
// 		static {
// 				plusButtonIcons.put(OntologyMap.INFO_SOURCE,
// 															new AddButton(OntologyMap.INFO_SOURCE));
// 				plusButtonIcons.put(OntologyMap.NUGGET, 
// 														new AddButton(OntologyMap.NUGGET));
// 				plusButtonIcons.put(OntologyMap.INTERPRETATION, 
// 														new AddButton(OntologyMap.INTERPRETATION));
// 				plusButtonIcons.put(OntologyMap.ENCODING, 
// 														new AddButton(OntologyMap.ENCODING));
// 				plusButtonIcons.put(OntologyMap.STATEMENT_BUNDLE,  
// 														new AddButton(OntologyMap.STATEMENT_BUNDLE));
// 				plusButtonIcons.put(OntologyMap.STATEMENT_TEMPLATE, 
// 														new AddButton(OntologyMap.STATEMENT_TEMPLATE));
// 				plusButtonIcons.put(OntologyMap.CODE_BUNDLE,  
// 														new AddButton(OntologyMap.CODE_BUNDLE));
// 				plusButtonIcons.put(OntologyMap.MODEL_CONTROLLER, 
// 														new AddButton(OntologyMap.MODEL_CONTROLLER));
// 				plusButtonIcons.put(OntologyMap.SUBMODEL_GROUP, 
// 														new AddButton(OntologyMap.SUBMODEL_GROUP));
// 				plusButtonIcons.put(OntologyMap.SUBMODEL, 
// 														new AddButton(OntologyMap.SUBMODEL));
// 				plusButtonIcons.put(OntologyMap.ONC_PROCESS,  
// 														new AddButton(OntologyMap.ONC_PROCESS));
// 				plusButtonIcons.put(OntologyMap.KEYWORD, 
// 														new AddButton(OntologyMap.KEYWORD));
// 		}

		
		public boolean onModal = false;
		public boolean pruningOn = true;
		public int currentRootButtonId = 0;
		// Toolbar buttons
		public AbstractButton [] toolbarBtn = 
				(AbstractButton [])Array.newInstance(AbstractButton.class, 19);

		public JButton filterTreeBtn = null;
		public JPanel mainFilterPanel = null;
		public String name = "Not set";
				
		public GenericTree tree = null;
		public OntologyToolBar  toolBar = null; // tree toolbar
		public Hashtable treeHashtable = null;
		public Vector selectedObjects = new Vector();
		public OntologyMap ontologyMap = new OntologyMap();
		public JScrollPane scrollPane = null;
		public OntologyButtonPanel ontologyButtonPanel = null;
		public FilterEditorPanel filterPanel = null;
		private GenericTreeSelectionListener sl = null;

		OntologyTree frame = null;

		public JPanel treeMainPanel = new JPanel(new BorderLayout());
		public TreeDisplayModePanel displayModePanel = null;
		public DisplaySiblingsModePanel displaySiblingsModePanel = null;
		
		
		public JSplitPane topSplitPane = null;
		public JSplitPane splitPane = null;


		public AddTreeNode linkNodeAdd = null;
		public AddTreeNode rootNodeAdd = null;
		public AddTreeNode keywordAdd = null;
		public RemoveNode remove = null;
		public UnlinkNode unlink = null;
		public FindAction find = null;
		public ShowEditorAction showEditor = null;
		public FilterToggleAction filterToggle = null;
		public FilterToggleAction siblingsToggle = null;
		public FilterToggleAction sortToggle = null;


	/** Constructor for the OntologyTree object */
		public OntologyTree() {
				dataSource = oncotcap.Oncotcap.getDataSource();
				init();
				
		}
		public OntologyTree(OncoTCapDataSource ds) {
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
				filterTreeBtn = new OncBrowserButton(this,filterTree);
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
				ontologyButtonPanel.setMinimumSize(new Dimension(400,200));
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
				filterPanel.addDoubleClickListener(this);

				//mainFilterPanel.add(textPanel, BorderLayout.NORTH);
				mainFilterPanel.add(filterPanel, BorderLayout.CENTER);
				treeControllerPanel.add(mainFilterPanel,c);
				splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
				javax.swing.plaf.basic.BasicBorders.SplitPaneBorder ourBorder = new
						javax.swing.plaf.basic.BasicBorders.SplitPaneBorder(Color.black, Color.blue) ;
				splitPane.setOneTouchExpandable(true);
				splitPane.setDividerSize(10);
				topSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			
				topSplitPane.setTopComponent(ontologyButtonPanel);
				topSplitPane.setForeground(Color.BLUE);
				topSplitPane.setBottomComponent(mainFilterPanel);
				topSplitPane.setOneTouchExpandable(true);
				topSplitPane.setDividerSize(10);
				
				initButtons();
				treeMainPanel.add(getTreePanel(), BorderLayout.CENTER);
				
				splitPane.setTopComponent(topSplitPane);
				splitPane.setBottomComponent(treeMainPanel);
				
				//registerAcceleratorKeys();
				setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
										 OncBrowser.getDefaultInputMap());
				setInputMap(JComponent.WHEN_FOCUSED,
											 OncBrowser.getDefaultInputMap());
				setActionMap(OncBrowser.getDefaultActionMap());
				
				Dimension minimumSize = new Dimension(300, 200);
				
				// Split the right pane into two parts
				//Add the split pane to this frame.
				add(splitPane, BorderLayout.CENTER);
				JPanel mainMenuPanel = new JPanel(new BorderLayout());
				JMenuBar menuBar = new JMenuBar();
				menuBar.add(new JMenu("Empty Menu"));
				ModelBuilderToolbarPanel mainToolBar = new ModelBuilderToolbarPanel();
				JButton filter = new OncBrowserButton(this,new Filter("Filter"));
				checkConsistency();
		}

		public void setOntologyButtonPanel(OntologyButtonPanel obp) {
				ontologyButtonPanel = obp;
				ontologyMap.setOntologyButtonPanel(ontologyButtonPanel);
		}

		public AbstractButton getButton(int button) {
				//System.out.println("getButton " + button + " -- " + toolbarBtn[button].getAction() );
				return toolbarBtn[button];
		}
    public void disableButton(int button) {
				toolbarBtn[button].setEnabled(false);
				toolbarBtn[button].setVisible(false);
		}
    public void enableButton(int button) {
				toolbarBtn[button].setEnabled(true);
				toolbarBtn[button].setVisible(true);
		}
		public void hideFilter(boolean hide) {
				mainFilterPanel.setVisible(!hide);
		}
		public void collapseFilter() {
				topSplitPane.remove(mainFilterPanel);
				topSplitPane.setOneTouchExpandable(false);
		}
		public void showFilterPanelAtBottom() {
				mainFilterPanel.setPreferredSize(new Dimension(300,200));
				treeMainPanel.add(mainFilterPanel, BorderLayout.SOUTH);
		}
		public void setFilter(OncFilter filter) {
				filterPanel.setFilter(filter);
				filterPanel.repaint();
		}
		public OncFilter getFilter() {
				return (OncFilter)filterPanel.getValue();
		}
		public void setFilterSelected(boolean selected) {
				toolbarBtn[FILTER_TOGGLE_BUTTON].setSelected(selected);
		}

		public void setLeaflessSelected(boolean selected) {
				toolbarBtn[SIBLINGS_TOGGLE_BUTTON].setSelected(selected);
		}
		public void setSortSelected(boolean selected) {
				toolbarBtn[SORT_TOGGLE_BUTTON].setSelected(selected);
		}

		public void hideController(boolean hide) {
				splitPane.setVisible(!hide);
		}
		public void collapseController() {
				Component bottomComponent = splitPane.getBottomComponent();
				if ( bottomComponent != null ) 
						splitPane.setTopComponent(bottomComponent);
				splitPane.remove(topSplitPane);
				splitPane.setOneTouchExpandable(false);
		}
		public JPanel getTreePanel() {
				JPanel panel = new JPanel(new BorderLayout());

				scrollPane = new JScrollPane();

				panel.add(scrollPane, BorderLayout.CENTER);
				panel.add(toolBar, BorderLayout.EAST);
				tree = initTree();
				scrollPane.setViewportView(tree);

				return panel;
		}

		public void addPopup(Class treeObjectType, JPopupMenu popup) {
				getTree().addPopup(treeObjectType, popup);
		}

		public void initButtons() {
				System.out.println("initButtons");
				createAllAddButtons();
				//toolbarBtn[EXPAND_BUTTON] = 
				// new JButton(new ExpandAll("Expand All"));
				//toolbarBtn[EXPAND_BUTTON].setToolTipText("Show all nodes.");
				//toolbarBtn[EXPAND_BUTTON].setBorder(new EmptyBorder(new Insets(0,0,0,0)));

				linkNodeAdd = 
						new AddTreeNode("Add Link Node", true, 
														AddTreeNode.CONNECTED_CLASSES);
				linkNodeAdd.setOntologyButtonPanel(ontologyButtonPanel);
				//toolbarBtn[ADD_LINK_BUTTON] = new JButton(linkNodeAdd);
				//toolbarBtn[ADD_LINK_BUTTON].setToolTipText(
				// "<html>Add element to currently selected item. <br> INSERT</html>");
				//toolbarBtn[ADD_LINK_BUTTON].setIcon(OncBrowserConstants.addLinkIcon);
				//toolbarBtn[ADD_LINK_BUTTON].setText("");
				rootNodeAdd = new AddTreeNode("Add Root Node", 
																			false);
				rootNodeAdd.isModal(onModal);
				rootNodeAdd.setOntologyButtonPanel(ontologyButtonPanel);

				//toolbarBtn[ADD_ROOT_BUTTON] = new JButton(rootNodeAdd);
				//toolbarBtn[ADD_ROOT_BUTTON].setIcon(OncBrowserConstants.addRootIcon);
				//toolbarBtn[ADD_ROOT_BUTTON].setText("");
				//toolbarBtn[ADD_ROOT_BUTTON].setToolTipText(
				// "Add root node type. <br>ALT-INSERT");

				keywordAdd = 
						new AddTreeNode("Add Keyword", true, AddTreeNode.KEYWORD_ONLY);
				keywordAdd.setOntologyButtonPanel(ontologyButtonPanel);
				keywordAdd.isModal(onModal);
				toolbarBtn[OntologyMap.K] = new OncBrowserButton(this,keywordAdd);
				toolbarBtn[OntologyMap.K].setText("");
				toolbarBtn[OntologyMap.K].setToolTipText("<html>Add keyword to currently selected item. <br> CTRL-K</html>");
				toolbarBtn[OntologyMap.K].setIcon(OncBrowserConstants.addKeywordIcon);

				remove = 
						new RemoveNode();
				toolbarBtn[DELETE_BUTTON] = new OncBrowserButton(this,remove);
				toolbarBtn[DELETE_BUTTON].setText("");
				toolbarBtn[DELETE_BUTTON].setToolTipText("<html>Delete currently selected item. <br> DELETE</html>");
				toolbarBtn[DELETE_BUTTON].setIcon(OncBrowserConstants.removeIcon);


				unlink = 
						new UnlinkNode();
				toolbarBtn[UNLINK_BUTTON] = new OncBrowserButton(this,unlink);
				toolbarBtn[UNLINK_BUTTON].setText("");
				toolbarBtn[UNLINK_BUTTON].setToolTipText("<html>Unlink currently selected item(s) from the parent node. <br>BACKSPACE </html>");
				toolbarBtn[UNLINK_BUTTON].setIcon(OncBrowserConstants.unlinkIcon);

				find = new FindAction("Find");
				toolbarBtn[FIND_BUTTON] = new OncBrowserButton(this,find);
				toolbarBtn[FIND_BUTTON].setToolTipText("<html>Locate an element matching the given string <br>within the displayed tree.<br>CTRL-F</html>");
				toolbarBtn[FIND_BUTTON].setText("");
				
				filterToggle = new FilterToggleAction();
				toolbarBtn[FILTER_TOGGLE_BUTTON] = new JCheckBox(OncBrowserConstants.filterOffIcon);
				toolbarBtn[FILTER_TOGGLE_BUTTON].setBorderPainted(true);
				toolbarBtn[FILTER_TOGGLE_BUTTON].setBorder(new BevelBorder(BevelBorder.RAISED));
				toolbarBtn[FILTER_TOGGLE_BUTTON].setText("");
				toolbarBtn[FILTER_TOGGLE_BUTTON].setSelectedIcon
						(OncBrowserConstants.filterOnIcon);
				toolbarBtn[FILTER_TOGGLE_BUTTON].setAction(filterToggle);
				toolbarBtn[FILTER_TOGGLE_BUTTON].setToolTipText("<html>[ON]Display tree nodes that satisfy filter condition. <br>[OFF]Display all tree nodes. Ignore filter condition.</html>");

				siblingsToggle = new FilterToggleAction();
				toolbarBtn[SIBLINGS_TOGGLE_BUTTON] = new JCheckBox(OncBrowserConstants.siblingOffIcon);
				toolbarBtn[SIBLINGS_TOGGLE_BUTTON].setBorderPainted(true);
				toolbarBtn[SIBLINGS_TOGGLE_BUTTON].setBorder(new BevelBorder(BevelBorder.RAISED));
				toolbarBtn[SIBLINGS_TOGGLE_BUTTON].setText("");
				toolbarBtn[SIBLINGS_TOGGLE_BUTTON].setSelectedIcon
						(OncBrowserConstants.siblingOnIcon);
				toolbarBtn[SIBLINGS_TOGGLE_BUTTON].setAction(siblingsToggle);
				toolbarBtn[SIBLINGS_TOGGLE_BUTTON].setToolTipText("<html>[ON]Show all tree nodes. <br>[OFF]Hide root nodes that are not attached to at least one leaf node.</html>");

				sortToggle = new FilterToggleAction();
				toolbarBtn[SORT_TOGGLE_BUTTON] = new JCheckBox(OncBrowserConstants.sortAlphaIcon);
				toolbarBtn[SORT_TOGGLE_BUTTON].setBorderPainted(true);
				toolbarBtn[SORT_TOGGLE_BUTTON].setBorder(new BevelBorder(BevelBorder.RAISED));
				toolbarBtn[SORT_TOGGLE_BUTTON].setText("");
				toolbarBtn[SORT_TOGGLE_BUTTON].setSelectedIcon
						(OncBrowserConstants.sortDateIcon);
				toolbarBtn[SORT_TOGGLE_BUTTON].setAction(sortToggle);
				toolbarBtn[SORT_TOGGLE_BUTTON].setToolTipText("<html>[A2Z]Display tree in alphabetical order.<br>[Clock]Show tree in chronological order</html>");
				
				// Toolbar 
				toolBar = new OntologyToolBar(JToolBar.VERTICAL);
				toolBar.setOntologyTree(this);

				toolbarBtn[COLLAPSE_BUTTON] = 
						new JCheckBox(OncBrowserConstants.expandIcon);
				toolbarBtn[COLLAPSE_BUTTON].setSelected(true);
				toolbarBtn[COLLAPSE_BUTTON].setToolTipText("Show only root nodes.");
				toolbarBtn[COLLAPSE_BUTTON].setBorderPainted(true);
				toolbarBtn[COLLAPSE_BUTTON].setBorder(new BevelBorder(BevelBorder.RAISED));
				toolbarBtn[COLLAPSE_BUTTON].setText("");
				toolbarBtn[COLLAPSE_BUTTON].setMargin(new Insets(0,0,0,0));
				toolbarBtn[COLLAPSE_BUTTON].setAction(new CollapseAll("Collapse All"));
				toolbarBtn[COLLAPSE_BUTTON].setSelectedIcon
						(OncBrowserConstants.collapseIcon);
				toolbarBtn[OntologyMap.K].setMargin(new Insets(0,0,0,0));
				toolbarBtn[DELETE_BUTTON].setMargin(new Insets(0,0,0,0));
				toolbarBtn[UNLINK_BUTTON].setMargin(new Insets(0,0,0,0));
				toolbarBtn[FIND_BUTTON].setMargin(new Insets(0,0,0,0));
				toolbarBtn[FILTER_TOGGLE_BUTTON].setMargin(new Insets(0,0,0,0));
				toolbarBtn[SIBLINGS_TOGGLE_BUTTON].setMargin(new Insets(0,0,0,0));
				toolbarBtn[SORT_TOGGLE_BUTTON].setMargin(new Insets(0,0,0,0));

				toolBar.add(toolbarBtn[COLLAPSE_BUTTON]);
				toolBar.add(toolbarBtn[FIND_BUTTON]);
				toolBar.add(toolbarBtn[FILTER_TOGGLE_BUTTON]);
				toolBar.add(toolbarBtn[SIBLINGS_TOGGLE_BUTTON]);
				toolBar.add(toolbarBtn[SORT_TOGGLE_BUTTON]);
				toolBar.addSeparator(new Dimension(0, 5));

				toolBar.add(toolbarBtn[DELETE_BUTTON]);
				toolBar.add(toolbarBtn[UNLINK_BUTTON]);
				toolBar.addSeparator(new Dimension(0, 5));
				toolBar.add(toolbarBtn[OntologyMap.K]);
				//toolBar.addSeparator();
				addAllOntologyButtons();

		}

		public void setSources(Object source) {
				linkNodeAdd.setSource(source);
				rootNodeAdd.setSource(source);
				keywordAdd.setSource(source);
				remove.setSource(source);
				unlink.setSource(source);
		}

		public void setTrees(GenericTree tree) {
				linkNodeAdd.setTree(tree);
				rootNodeAdd.setTree(tree);
				keywordAdd.setTree(tree);
				remove.setTree(tree);
				unlink.setTree(tree);
				find.setTree(tree);
				tree.addTreePanelListener(this);
			
		}
		public void setFindModal(boolean b) {
				find.setModal(b);
		}
		public boolean getModal() {
				return isTreeModal;
		}
		public void setModal(boolean modal) {
				isTreeModal = modal;
		}
		public void registerAcceleratorKeys() {
				// Register accelerator keys
					tree.getInputMap
						(JComponent.WHEN_FOCUSED).put
						(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK, true),
						 ModelBuilderToolbarPanel.saveAction.getValue(Action.NAME));
				tree.getActionMap().put(ModelBuilderToolbarPanel.saveAction.getValue(Action.NAME), 
														 ModelBuilderToolbarPanel.saveAction);
				tree.getInputMap
						(JComponent.WHEN_FOCUSED).put
						(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK, true),
						 ModelBuilderToolbarPanel.optionAction.getValue(Action.NAME));
				tree.getActionMap().put(ModelBuilderToolbarPanel.optionAction.getValue
														 (Action.NAME), 
														 ModelBuilderToolbarPanel.optionAction);

				tree.getInputMap
						(JComponent.WHEN_FOCUSED).put
						(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK, true),
						 linkNodeAdd.getValue(Action.NAME));
				tree.getActionMap().put(linkNodeAdd.getValue(Action.NAME), 
																 linkNodeAdd);

				tree.getInputMap
						(JComponent.WHEN_FOCUSED).put
						(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK, true),
						 rootNodeAdd.getValue(Action.NAME));
				tree.getActionMap().put(rootNodeAdd.getValue(Action.NAME), 
																rootNodeAdd);
				// add keyword
				tree.getInputMap
						(JComponent.WHEN_FOCUSED).put
						(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK, true),
						 keywordAdd.getValue(Action.NAME));
				tree.getActionMap().put(keywordAdd.getValue(Action.NAME), 
																 keywordAdd);
				// delete
				tree.getInputMap
						(JComponent.WHEN_FOCUSED).put
						(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
						 remove.getValue(Action.NAME));
				tree.getActionMap().put(remove.getValue(Action.NAME), 
																 remove);

				// Find
				tree.getInputMap
						(JComponent.WHEN_FOCUSED).put
						(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK, true),
						 find.getValue(Action.NAME));
				tree.getActionMap().put(find.getValue(Action.NAME), 
																 find);
				setActionMap(tree.getActionMap());
				// Enter
				showEditor = new ShowEditorAction("ShowEditor");
				showEditor.setTree(tree);
				tree.getInputMap
						(JComponent.WHEN_FOCUSED).put
						(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
						 showEditor.getValue(Action.NAME));
				tree.getActionMap().put(showEditor.getValue(Action.NAME), 
																showEditor);
		}
		

		public void overrideDoubleClickAction(String actionName, AbstractAction action) {
				tree.getInputMap(JComponent.WHEN_FOCUSED).put
													(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
													 actionName);
				tree.getActionMap().put(actionName, 
																action);	
				
		}

		public void onModal(boolean o) {
				onModal = o;
				// Set any addtreenode actions
			// 	((AddTreeNode)toolbarBtn[ADD_LINK_BUTTON].getAction()).isModal(onModal);
// 				((AddTreeNode)toolbarBtn[ADD_ROOT_BUTTON].getAction()).isModal(onModal);
				((AddTreeNode)toolbarBtn[OntologyMap.K].getAction()).isModal(onModal);
		}

		public OntologyButtonPanel getOntologyButtonPanel() {
				return ontologyButtonPanel;
		}

		public void refresh() {
				//System.out.println("OT Refreshing tree "  + tree);
				if(ProtegeSaveAction.isCurrentlySaving())
					return;
				updateTree();
				//repaint();

		}

		public GenericTree initTree() {
				if (treeHashtable == null ) 
						treeHashtable = new Hashtable();
				tree = new GenericTree(treeHashtable, true);
				sl = tree.getSelectionListener();
				sl.addDoubleClickListener(this);
				tree.setOntologyTree(this);
				JScrollBar hScrollBar = scrollPane.getHorizontalScrollBar();
				JScrollBar vScrollBar = scrollPane.getVerticalScrollBar();
				hScrollBar.addAdjustmentListener(tree);
				vScrollBar.addAdjustmentListener(tree);
				tree.allowDoubleClickEditing(true);
				setSources(toolBar);
				setTrees(tree);
				return tree;
		}


		public GenericTreeSelectionListener getGenericTreeSelectionListener() {
				return sl;
		}
		public GenericTree getTree() {
				return tree;
		}

		public void updateTree() {
				updateTree(ontologyButtonPanel);
		}

		public void updateTree(OntologyButtonPanel ontologyButtonPanel) {
				// Get information from filter to determine related items for now
				// next step pass filter instead of related items
				//System.out.println("updating tree "  + tree);

  			// Get the onc filter
				OncFilter filter = (OncFilter)filterPanel.getValue();
				// Get what the current state of the allRootNone radio button is
				int displayMode = TreeDisplayModePanel.NONE;
				if ( toolbarBtn[FILTER_TOGGLE_BUTTON].isSelected() == true ) 
						displayMode = TreeDisplayModePanel.ROOT;
				updateTree(OntologyMap.getRootNodeName(ontologyButtonPanel), 
									 ontologyMap.getEndNodeNames(ontologyButtonPanel),
									 filter,
									 displayMode);
				//repaint();
		}

		public void updateTree(String rootNodeName, 
													 Vector endingClassNames, 
													 Collection relatedItems,
													 int allRootNone){
		
				// display tree hashtable
			if(ProtegeSaveAction.isCurrentlySaving())
				return;
				Hashtable h = getTreeInstances(rootNodeName, 
																			 endingClassNames,
																			 relatedItems,
																			 allRootNone);
				
 				//System.out.println("OT updateTree " + h);
				removeSaveListeners();
				tree.updateTree(h);
				if ( pruningOn )
						tree.pruneNodes(CollectionHelper.makeVector
														(SubModelGroup.class));
				// Listen to every object that is in the tree 
				updateSaveListeners();
				//scrollPane.setViewportView(tree);
				//scrollPane.revalidate();
				//repaint();
		}

		public void removeSaveListeners(){
				// Loop through every node in the tree and remove a listener to
				// each persistible
				GenericTreeNode rootNode = tree.getRootNode();
				for (Enumeration e=rootNode.depthFirstEnumeration(); 
						 e.hasMoreElements(); ) {
						GenericTreeNode n = (GenericTreeNode)e.nextElement();		
						if ( n.getUserObject() instanceof Persistible) {
								((Persistible)n.getUserObject()).removeSaveListener(this);
						}
				}
		}

		public void updateSaveListeners(){
				// Loop through every node in the tree and add a listener to
				// each persistible
				GenericTreeNode rootNode = tree.getRootNode();
				for (Enumeration e=rootNode.depthFirstEnumeration(); 
						 e.hasMoreElements(); ) {
						GenericTreeNode n = (GenericTreeNode)e.nextElement();		
						if ( n.getUserObject() instanceof Persistible) {
								((Persistible)n.getUserObject()).addSaveListener(this);
						}
				}
		}
		public void objectSaved(SaveEvent e) {
			if(ProtegeSaveAction.isCurrentlySaving())
				return;
			tree.refresh();
		}
		public void objectDeleted(SaveEvent e) {
			if(ProtegeSaveAction.isCurrentlySaving())
				return;
			tree.refresh();
		}
		public void updateTree(Hashtable h, String rootNodeName){
			if(ProtegeSaveAction.isCurrentlySaving())
				return;
			tree.updateTree(h);
			if ( pruningOn ) {
					Vector unselectedClasses = 
							ontologyMap.getAllUnselectedLeaves();
					tree.pruneNodes(unselectedClasses);
			}
			if ( toolbarBtn[SIBLINGS_TOGGLE_BUTTON].isSelected() == true) {
					Vector selectedClasses = 
							ontologyMap.getAllSelectedLeafClasses();
					// Keyword is the root & leaf then allow keywords to be removed 
					if ( rootNodeName.equals("Keyword") ) 
							selectedClasses.removeElement(Keyword.class);
					if (selectedClasses.size() > 0 ) 
							tree.pruneBranches(selectedClasses);
			}
		}
		
		public void updateTree(String rootNodeName, 
													 Vector endingClassNames, 
													 OncFilter filter,
													 int allRootNone){
				// display tree hashtable
				// 	System.out.println(" getTreeInstances(rootNodeName," 
				// + rootNodeName 
				// + "\n endingClassNames, " + endingClassNames 
				// + "\nfilter, " + filter  
				// + "\n allRootNone) " + allRootNone);  
				Hashtable h = getTreeInstances(rootNodeName, 
																			 endingClassNames,
																			 filter,
																			 allRootNone);
				
				// 		System.out.println("TREE HASHTABLE " + this +
				// 													 " -- " + h);
				updateTree(h, rootNodeName);
		}

		public Hashtable getTreeInstances(String rootNode, 
				Vector endingClassNames, 
				OncFilter filter,
				int allRootNone) {
				//System.out.println("getTreeInstances " + rootNode + " IN DS " + dataSource);
				return dataSource.getInstanceTree
						(rootNode,
						 endingClassNames,
						 filter,
						 allRootNone);
		}

		public Hashtable getTreeInstances(String rootNode, 
																			Vector endingClassNames, 
																			Collection relatedItems,
																			int allRootNone) {
				// This really should return a subset list for only things that are
				// specifically linked to the selected item
				return dataSource.getInstanceTree(rootNode,
																																 endingClassNames);		
		}
		public Object getSelectedUserObject() {
				return getTree().getLastSelectedUserObject();
		}
		public void changed(TreePanelEvent evt) {
				//System.out.println("tree panel event ");
				// Try to make sure the darn horizontal scroll bar stays 
				// left justified
				if ( hScrollBar != null ) 
						hScrollBar.setValue(0);
				// put add buttons on the toolabar depending on 
				// what is currently selected 
				Object selectedObj = null;
				if ( evt.getTree().getSelectionPath() != null ) 
						selectedObj = 
								evt.getTree().getSelectionPath().getLastPathComponent();
				if ( selectedObj != null ) {
						Object userObj = ((GenericTreeNode)selectedObj).getUserObject();
						String ontologyClass = 
								OntologyMap.getOntologyClassName(userObj.getClass());
						if ( ontologyClass != null )
								updateToolbarAddButtons(ontologyClass);
				}
				else {
						updateToolbarAddButtons((String)null);
				}
				

		}
		public void refreshRootNode(OntologyButtonPanel ontologyButtonPanel) {
				String rootClassName = 
						OntologyMap.getRootNodeName(ontologyButtonPanel);
				Object obj = tree.getRootNode().getUserObject();
				if (obj instanceof GenericRootNodeObject)
						((GenericRootNodeObject)obj).setName(rootClassName);
		}
		public void refreshRootNode(String rootClassName) {
				Object obj = tree.getRootNode().getUserObject();
				if (obj instanceof GenericRootNodeObject)
						((GenericRootNodeObject)obj).setName(rootClassName);
		}
		public void changed(OntologyPanelEvent evt) {
				// The user changed their selections update the tree
				// If the user has changed the root object then change 
				// what buttons to display on the toolbar
				if ( !(evt.getSource() instanceof OntologyButtonPanel) ) 
						oncotcap.util.ForceStackTrace.showStackTrace();
				OntologyButtonPanel ontologyButtonPanel = 
						(OntologyButtonPanel)evt.getSource();
				String rootClassName = 
						OntologyMap.getRootNodeName(ontologyButtonPanel);
				refreshRootNode(ontologyButtonPanel);
				// show the button that corresponds to the root
				// unless it is keyword then just remove what was 
				// the previous root button
				// If the root has not changed fdo nothing otherwise 
				// clear all add buttons
				clearAllAddButtons();
				enableAddButton(rootClassName, true);
				//getTree().clearSelection();
				updateTree(ontologyButtonPanel);
				
		}

		public void updateToolbarAddButtons(String selectedClassName) { 
				String rootClassName = 
						OntologyMap.getRootNodeName(ontologyButtonPanel);
				clearAllAddButtons();
				enableAddButton(rootClassName, true);
				if ( selectedClassName == null )
						return;
				Vector availableClasses = 
						new Vector(OntologyMap.getValidDirectConnectedTypes
											 (selectedClassName));
				
				//System.out.println("AVAILABLE CLASSES " + availableClasses 
				// 											 + " selectedClassName " + selectedClassName);
				Iterator i = availableClasses.iterator();
				while (i.hasNext() ) {
						String availableClassName = (String)i.next();
						//System.out.println("checking visibility .... " + availableClassName);
						if ( OntologyMap.isVisibleNode(availableClassName, 
																					 ontologyButtonPanel) && 
								 !availableClassName.equals(rootClassName) ) {
								// Add buttons for linkable types
								enableAddButton(availableClassName, false);
						}
				}

		}
		public void createAllAddButtons() {
				// Clear all "ADD" buttons except add keyword which is always present
				for ( int i = 0; i < NUMBER_OF_ADD_BUTTONS-1; i++) {
						toolbarBtn[i] = new OncBrowserButton(this);
						toolbarBtn[i].setToolTipText("<html>Add </html>");
						toolbarBtn[i].setText(null);
						toolbarBtn[i].setFocusable(false);
				}
		}

		public void addAllOntologyButtons() {
				// Add all "ADD" buttons except add keyword which is always present
				for ( int i = 0; i < NUMBER_OF_ADD_BUTTONS-1; i++) {
						toolBar.add(toolbarBtn[i]);
						disableButton(i);
				}
		}

		public void clearAllAddButtons() {
				// Clear all "ADD" buttons except add keyword which is always present

				for ( int i = 0; i < NUMBER_OF_ADD_BUTTONS-1; i++) 
						disableButton(i);				
		}


		public int getCurrentRootAddButton() {
				return currentRootButtonId;
		}
		public void setCurrentRootAddButton(int id) {
				currentRootButtonId = id;
		}
		public void enableAddButton(String ontologyName, boolean root) {
				if ( ontologyName.equals(OntologyMap.KEYWORD))
						return;

				int buttonId = -1;
				buttonId = 
						((Integer)OntologyMap.getButtonIndex(ontologyName)).intValue();
				if ( root ) {
						// Make sure there isn't anything selected in the tree if there 
						// is the add function will try to link to it and
						// sometimes that can be very undesirable
						int currentId = getCurrentRootAddButton();
						disableButton(currentId);
						setCurrentRootAddButton(buttonId);
				}
				AbstractButton button = getButton(buttonId);
				// set the add action
				if ( root ) {
						rootNodeAdd.setCreateClassName(ontologyName);
						button.setAction(rootNodeAdd);
						button.setFocusable(false);
						button.setToolTipText("<html>Create new " + ontologyName + ".<BR>CTRL-R</html>");
				}
				else {
						AddTreeNode addAction =
								new AddTreeNode("Add " + ontologyName, 
																true, 
																AddTreeNode.CONNECTED_CLASSES);
						addAction.setOntologyButtonPanel(ontologyButtonPanel);
						addAction.setCreateClassName(ontologyName);
						addAction.setTree(tree);
						addAction.setSource(toolBar);
						button.setAction(addAction);
						button.setToolTipText("<html>Add " 
															+ ontologyName + " to selected tree node.<BR>CTRL-L</html>");
				}
				//System.out.println("button " + ontologyName + " " + button.getAction());

				button.setIcon( getButtonIcon(ontologyName));
				button.setMargin(new Insets(0,0,0,0));
 				button.setText(null);
				enableButton(buttonId);
		}
		

    public void valueChanged(ListSelectionEvent e) {
			
		}

  public void valueChanged(TreeSelectionEvent e) {
				GenericTree tree = (GenericTree)e.getSource();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           ((JTree)e.getSource()).getLastSelectedPathComponent();
	}


		// TreeModelListener
		public void treeNodesChanged(TreeModelEvent e) {
				// Invoked after a node (or a set of siblings) has changed in some way. 
		}
		public void treeNodesInserted(TreeModelEvent e) {
				//Invoked after nodes have been inserted into the tree. 
		}
		public void treeNodesRemoved(TreeModelEvent e) {
				// Invoked after nodes have been removed from the tree. 
// 			  System.out.println("Filter has changed - node(s) removed" );
				filterPanel.save();
				updateTree();

		}
		public void treeStructureChanged(TreeModelEvent e) {
// 			  System.out.println("Filter has changed " );
				filterPanel.save();
				updateTree();

		}
 
		public void setName(String name) {
				this.name = name;
				getTree().setName(name + String.valueOf(treeCounter));
				treeCounter++;
		}
		public String getName() {
				return getTree().getName();
		}
		public String toString() {
				return this.name;
		}

		public void setPruningOn(boolean p) {
				pruningOn = p;
		}

		public boolean pruningIsOn() {
				return pruningOn;
		}
		////Inner classes
		class ExpandAll extends AbstractAction {
				public ExpandAll(String actionName) {
							super(null, OncBrowserConstants.expandIcon);
				}

				public void actionPerformed(ActionEvent e) {
						tree.expandAll();
				}
				
		}

		// Control tree display - show allleaves or collapse to first level leaves
		class CollapseAll extends AbstractAction {
				
				public CollapseAll(String actionName) {
						super(null, OncBrowserConstants.collapseIcon);
				}
				public void actionPerformed(ActionEvent e) {
						if (e.getSource() instanceof JCheckBox
								&& ((JCheckBox)e.getSource()).isSelected()) 
								tree.expandAll();
						else 
								tree.collapseAll();


				}
				
		}





	class Filter extends AbstractAction {
				
				public Filter(String actionName) {
						super(null, OncBrowserConstants.filterIcon);
				}
				public void actionPerformed(ActionEvent e) {
					
				}
				
		}



		public void checkConsistency() { 
				// Loop through all selected code bundles
				// not implemented yet
		}


		static public OntologyTree getOntologyTree() {
				return oncBrowser;
		}
		public int getSortMode() {
				if ( toolbarBtn[SORT_TOGGLE_BUTTON].isSelected() )
						return SORT_BY_VERSION;
				else
						return SORT_BY_ALPHA;
		}
		public void setSortMode(int mode) {
				sortMode = mode;
		}

		public JToolBar getToolBar() {
				return toolBar;
		}

		public Vector getEndNodeNames() {
				return ontologyMap.getEndNodeNames(ontologyButtonPanel);
		}

		public void setLeaves(Collection leafNames) {
				OntologyButtonPanel obp = getOntologyButtonPanel();
				// Convert ending class names to obp ids
				Vector endingClassIds = new Vector();
				Iterator i = leafNames.iterator();
				while ( i.hasNext() ) {
						String endingClassName = (String)i.next();
						Integer id = OntologyMap.getButtonIndex(endingClassName);
						if ( id != null )
								endingClassIds.add(id);
				}
				obp.setLeavesQuietly(endingClassIds);
		}

		public Vector  getAllSelectedLeaves() {
				return ontologyMap.getAllSelectedLeaves();
		}
		public OncoTCapDataSource getDataSource() {
				return dataSource;
		}

		/**
		 * The main program for the OncViewer class
		 *
		 * @param args The command line arguments
		 */
		public static void main(String[] args) {
				try {
						UIManager.setLookAndFeel(OncBrowserConstants.WINDOWS);
				}
				catch (Exception ex) {
						System.out.println("Failed loading L&F: ");
				}
				UIManager.put( "Table.selectionBackground",new Color(228,225,220)); 
				UIManager.put( "Table.selectionForeground",Color.BLACK); 
				UIManager.put( "List.selectionBackground",new Color(228,225,220));
				UIManager.put( "List.selectionForeground",Color.BLACK); 
				oncotcap.Oncotcap.handleCommandLine(args);
				OncoTCapDataSource dataSource = oncotcap.Oncotcap.getDataSource();
	// 			Persistible p1 = 
// 						dataSource.find(new GUID("5459172400000007000000f57bb24c81") );
// 				Persistible p2 = 	
// 						dataSource.find(new GUID("888e669c00000003000000f58c70b08e"));
// 				System.out.println("Are they connected " + p1.connectedTo(p2));
				OntologyTree frame = new OntologyTree();
		}

		public ImageIcon getButtonIcon(String ontologyName) {
				// Put the image on the button
				// get a scaled version of the ontology initial icon
				// add the plus sign to it
				// Make a button
				if ( ontologyName == null )
					return null;
				ImageIcon icon = 
					     OncoTcapIcons.getImageIcon(ontologyName.toLowerCase() + "-add.jpg");
				return icon;
		}

		public void addOntologyPanelListener(OntologyPanelListener opl) {
				ontologyButtonPanel.addOntologyPanelListener(opl);
		}

		public void doubleClicked(DoubleClickEvent evt){
				int x = 0;
				int y = 0;
				if ( evt.getMouseEvent() != null ) { 
						x = evt.getMouseEvent().getX();
						y = evt.getMouseEvent().getY();
				}
				if ( evt.getSource() instanceof JTree ) {
						Object obj = ((JTree)evt.getSource()).getLastSelectedPathComponent();
						Object userObject = null;
						if ( obj instanceof DefaultMutableTreeNode ) 
								userObject = ((DefaultMutableTreeNode)obj).getUserObject();
						else
								return;
						if ( userObject instanceof Editable) {
								EditorPanel editorPanel =
										EditorFrame.showEditor((Editable)userObject, 
																					 null,
																					 x, y, 
																					 (JTree)evt.getSource());
								if ( userObject instanceof Persistible ) {
										((Persistible)userObject).addSaveListener(editorPanel);
								}
						}
						else if ( obj instanceof DefaultMutableTreeNode
											&& ((DefaultMutableTreeNode)obj).isRoot()) {
								// Root node open all if last known state was closed  
								// or close all nodes open
								tree.setExpandState(!tree.getExpandState());
						}
						else {
								JOptionPane.showMessageDialog
										(null, 
										 "There is no editor for the selected object.");
						}
				}
		}
		public void setExpandState(boolean state) {
				tree.setExpandState(state);
		}

}

