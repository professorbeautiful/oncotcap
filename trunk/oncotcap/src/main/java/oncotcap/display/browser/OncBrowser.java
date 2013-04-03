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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.DefaultPersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;

import oncotcap.Oncotcap;
import oncotcap.datalayer.OncoTCapDataSource;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.SearchText;
import oncotcap.datalayer.autogenpersistible.InformationSource;
import oncotcap.datalayer.autogenpersistible.Interpretation;
import oncotcap.datalayer.autogenpersistible.KnowledgeNugget;
import oncotcap.datalayer.persistible.CodeBundle;
import oncotcap.datalayer.persistible.Encoding;
import oncotcap.datalayer.persistible.Keyword;
import oncotcap.datalayer.persistible.ModelController;
import oncotcap.datalayer.persistible.OncFilter;
import oncotcap.datalayer.persistible.OncFilterLite;
import oncotcap.datalayer.persistible.StatementBundle;
import oncotcap.datalayer.persistible.StatementTemplate;
import oncotcap.datalayer.persistible.SubModel;
import oncotcap.datalayer.persistible.SubModelGroup;
import oncotcap.display.Splash;
import oncotcap.display.browser.HelpMenu.ShowScreenHelpAction;
import oncotcap.display.common.HelpEnabled;
import oncotcap.display.common.InitialFocusSetter;
import oncotcap.display.common.OncFrame;
import oncotcap.display.common.OncScrollList;
import oncotcap.display.common.OncScrollListButtonPanel;
import oncotcap.display.common.SplashScreen;
import oncotcap.display.editor.EditorFrame;
import oncotcap.display.editor.persistibleeditorpanel.AddConditionAction;
import oncotcap.display.editor.persistibleeditorpanel.AddProbabilityAction;
import oncotcap.display.editor.persistibleeditorpanel.ParameterEditor;
import oncotcap.util.CollectionHelper;
import oncotcap.util.ComponentHelper;
import oncotcap.util.GUID;
import oncotcap.util.OncoTcapIcons;
import oncotcap.util.SystemUtil;

/**
 * @author morris
 * @created April 24, 2003
 */
public class OncBrowser extends JFrame 
	implements TreeModelListener,
			  ComponentListener {
	// Declare static keyword tree models that are used in all cases where
	// there are enum/category/keyword trees this allows keyword tree to
	// be centrally maintained and cut down on duplication
	// where the keyword tree has no leaves or has keywords only as leaves
	public static boolean notifyUIOfPersistibleChanges = true;
	
	public static DefaultTreeModel keywordNestedTreeModel = null;
	
	public static DefaultTreeModel keywordFlatTreeModel = null;
	
	public static JFrame mainWindow = null;
	
	public static InputMap defaultInputWhenFocusedMap = new InputMap();
		//new JPanel()
	//.getInputMap(JComponent.WHEN_FOCUSED);
	public static InputMap defaultInputWhenAncestorFocusedMap = new JPanel()
	.getInputMap(JComponent.WHEN_FOCUSED);
	
	public static FileMenu fileMenu = null;
	public static WindowMenu windowMenu = null;
	public static HelpMenu helpMenu = null;
	public static OptionMenu optionMenu = null;

	
	public static ActionMap defaultActionMap = (new JPanel()).getActionMap();
	
	private JSplitPane splitPane = null;
	
	static Vector ontologyTrees = new Vector();
	
	static OncBrowser oncBrowser = null;
	
	static boolean suggestKeywords = false;
	
	static boolean saveWorkspace = true;
	
	static boolean useWorkspaceSettings = true;
	
	static OntologyTree o1 = null;
	
	static OntologyTree o2 = null;
	
	static String topFilterName = "TopOntologyTreeFilter";
	
	static String bottomFilterName = "BottomOntologyTreeFilter";
	
	static int TOP_DEFAULT_ROOT = OntologyMap.IS;
	
	static int BOTTOM_DEFAULT_ROOT = OntologyMap.K;
	
	static Properties props = null;
	
	static OncFilter topFilter = null;
	
	static OncFilter bottomFilter = null;
	
	static public int switchOrientation = 2;
	
	static Dimension defaultSize = new Dimension(700, 650);
	
	static GenericTree tree = null;
	
	Hashtable treeHashtable = null;
	
	Vector selectedObjects = new Vector();
	
	OntologyMap ontologyMap = new OntologyMap();
	
	JScrollPane scrollPane = null;
	
	OntologyButtonPanel ontologyButtonPanel = null;
	
	OntologyTabbedPanel tabbedMainPanel = null;
	
	static OncBrowser frame = null;
	
	/** */
	private static JSplitPane topSplitPane = null;
	
	private static int dividerLocation = 0;
	
	
	
	private final static String WINDOWS = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	
	public String sillyVariable = "Michele";
	
	public AddTreeNode linkNodeAdd = null;
	
	public AddTreeNode rootNodeAdd = null;
	
	public AddTreeNode keywordAdd = null;
	
	public RemoveNode remove = null;
	
	public UnlinkNode unlink = null;
	
	public FindAction find = new FindAction("find");
	
	public ShowEditorAction showEditor = null;
	
	public FilterToggleAction filterToggle = null;
	
	public FilterToggleAction siblingsToggle = null;
	private static String helpHS = "oncotcap/resource/docs/oncotcapHelpSet.hs";
	private static HelpSet helpSet = null;
	private static HelpBroker helpBroker = null;
	
	  private static SplashScreen fSplashScreen;
//	  private static final Logger fLogger = 
//	                          Logger.getLogger(Launcher.class.getPackage().getName());  
//
//	
	/** Constructor for the OncBrowser object */
	@SuppressWarnings("serial")
	public OncBrowser() {
		super(" Oncology Thinking Cap Browser " + Oncotcap.getMajorVersion()
				+ "." + Oncotcap.getMinorVersion() + "." + Oncotcap.getBuild());
		addComponentListener(this);
		setSize(new Dimension(800, 600));
		if (mainWindow == null)
			mainWindow = this;
		setIconImage(OncoTcapIcons.getDefault().getImage());
		// Add the scroll panes to a split pane.
		setupInputMaps();
		initHelp();
		enableHelp(this);
		getRootPane().setInputMap(JComponent.WHEN_FOCUSED, defaultInputWhenFocusedMap);
		getRootPane().setActionMap(defaultActionMap);
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		// Use the shared InputMap and ActionMap
//		splitPane.setInputMap(JComponent.WHEN_FOCUSED, defaultInputWhenFocusedMap);
//		splitPane.setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
//				defaultInputWhenFocusedMap);
//		splitPane.setActionMap(defaultActionMap);
		topSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		topSplitPane.setResizeWeight(0);			
		topSplitPane.setPreferredSize(new Dimension(900, 700));
		
		// tabbedMainPanel = new OntologyTabbedPanel();
		// tree.addTreePanelListener(tabbedMainPanel);
		
		o1 = new OntologyTree();
		o1.setName("Bottom OntologyTree");
		addOntologyTree(o1);
		enableHelp(o1);
		o2 = new OntologyTree();
		o2.setName("Top OntologyTree");
		addOntologyTree(o2);
		topSplitPane.setBottomComponent(o1);

		enableHelp(o2);
		// Make upper tree display info source tree by default
		int rootName = OntologyMap.IS;
		Vector topLeaves = new Vector();
		Vector bottomLeaves = new Vector();
		
		int topRoot = TOP_DEFAULT_ROOT;
		int bottomRoot = BOTTOM_DEFAULT_ROOT;
		if (useWorkspaceSettings()) {
			topRoot = getRoot("TopOntologyTree");
			bottomRoot = getRoot("BottomOntologyTree");
			topLeaves = getLeaves("TopOntologyTree");
			bottomLeaves = getLeaves("BottomOntologyTree");
		}
		o1.getOntologyButtonPanel().setRoot(topRoot);
		if (topLeaves != null && topLeaves.size() > 0)
			o1.getOntologyButtonPanel().setLeavesQuietly(topLeaves);
		if (topFilter != null) {
			o1.setFilter(topFilter);
			o1.setFilterSelected(isFilterSelected("TopOntologyTree"));
		}
		// o1.getOntologyButtonPanel().setLeaves
		// (CollectionHelper.makeVector(new Integer(OntologyMap.K)));
		o1.changed(new OntologyPanelEvent(o1.getOntologyButtonPanel(), null));
		
		// Make lower tree display keyword tree by default
		o2.getOntologyButtonPanel().setRoot(bottomRoot);
		if (topLeaves != null && bottomLeaves.size() > 0)
			o2.getOntologyButtonPanel().setLeavesQuietly(bottomLeaves);
		else
			o2.getOntologyButtonPanel().setLeaves(
					CollectionHelper
					.makeVector(new Integer(BOTTOM_DEFAULT_ROOT)));
		
		if (bottomFilter != null) {
			o2.setFilter(bottomFilter);
			o2.setFilterSelected(isFilterSelected("BottomOntologyTree"));
		}
		o2.changed(new OntologyPanelEvent(o2.getOntologyButtonPanel(), null));
		o2.setPreferredSize(new Dimension(0, 0));
;
		
		// splitPane.setDividerLocation(0.5);
		// splitPane.setTopComponent(topSplitPane);
		// splitPane.setDividerLocation(700);
		// splitPane.setBottomComponent(tabbedMainPanel);
		
		splitPane.setPreferredSize(new Dimension(900, 700));
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerSize(0);
		
		// Add the split pane to this frame.
		getContentPane().add(topSplitPane, BorderLayout.CENTER);
		JPanel mainMenuPanel = new JPanel(new BorderLayout());
		JMenuBar menuBar = new JMenuBar();
//		ProtegeSaveAction saveAction = new ProtegeSaveAction();
//		OptionAction optionAction = new OptionAction();
//		FindAction findAction = new FindAction("find");
//		JButton saveBtn = new JButton(saveAction);
//		JButton findBtn = new JButton(findAction);
//		JButton optionBtn = new JButton(optionAction);
//		saveBtn
//		.setToolTipText("<html>Save project to data storage.<br> CTRL-S</html>");
//		optionBtn
//		.setToolTipText("<html>Display option panel.<br> CTRL-O</html>");
		//menuBar.add(saveBtn);
//		menuBar.add(findBtn);
		//menuBar.add(optionBtn);
		//menuBar.add(new JSeparator());
		fileMenu = new FileMenu("File");
		menuBar.add(fileMenu);
		fileMenu.setMnemonic('F');
		optionMenu = new OptionMenu("Options");
		optionMenu.setMnemonic('O');
		menuBar.add(optionMenu);
		windowMenu = new WindowMenu("Windows", this);
		menuBar.add(windowMenu);
		// Initialize the help menu
		if ( helpMenu == null ) {
			helpMenu = new HelpMenu("Help",
					this); 
			menuBar.add(helpMenu);
			helpMenu.setMnemonic('H');
		}
		//windowMenu.setBackground(Color.green);
		/*
		 * FindMenu findMenu = new FindMenu(); menuBar.add(findMenu);
		 *//*
		 * menuBar.add(new JMenuItem("Save",'s'){ public void
		 * addMenuKeyListener(MenuKeyListener l) { (new
		 * ProtegeSaveAction()).actionPerformed(null); };
		 * 
		 * });
		 */
		/*
		 * menuBar.add(new JMenuItem("Options",'o'){ public void
		 * addMenuKeyListener(MenuKeyListener l) { (new
		 * OptionAction()).actionPerformed(null); }; });
		 */// 
		ModelBuilderToolbarPanel mainToolBar = new
			ModelBuilderToolbarPanel();
		mainMenuPanel.add(menuBar, BorderLayout.WEST);
		mainMenuPanel.add(mainToolBar, BorderLayout.EAST);
		// mainMenuPanel.add(filter, BorderLayout.EAST);
		getContentPane().add(mainMenuPanel, BorderLayout.NORTH);
		checkConsistency();
		// Who has focus at this point
		// null is returned if none of the components in
		// this application has the focus
		Component compFocusOwner = KeyboardFocusManager
		.getCurrentKeyboardFocusManager().getFocusOwner();
		
		// null is returned if none of the windows in this
		// application has the focus
		Window windowFocusOwner = KeyboardFocusManager
		.getCurrentKeyboardFocusManager().getFocusedWindow();
		// Set component with initial focus; must be done before the
		// frame is made visible
		o2.getTree().requestFocusInWindow();
		//InitialFocusSetter.setInitialFocus(this, o1.getTree());
		
		// System.out.println(" compFocusOwner " + compFocusOwner);
		// System.out.println(" windowFocusOwner " + windowFocusOwner);
	}
	public OntologyTree ontologyTreeWithFocus(){
		if(o1.hasFocus()) return(o1);
		return(o2);
	}
	
	private void initializeKeywordTreeModels() {
		Vector endClasses = new Vector();
		endClasses.addElement(OntologyMap.KEYWORD);
		Hashtable keywords = oncotcap.Oncotcap.getDataSource().getInstanceTree(
				OntologyMap.KEYWORD, new Vector(), null,
				TreeDisplayModePanel.NONE);
		GenericTree tree = new GenericTree(keywords, true);
		keywordFlatTreeModel = (DefaultTreeModel) tree.getModel();
		keywords = oncotcap.Oncotcap.getDataSource().getInstanceTree(
				OntologyMap.KEYWORD, endClasses, null,
				TreeDisplayModePanel.NONE);
		tree = new GenericTree(keywords, true);
		keywordNestedTreeModel = (DefaultTreeModel) tree.getModel();
	}
	
	private int getRoot(String propName) {
		return OntologyMap.getButtonIndex(
				props.getProperty(propName + ".root", OntologyMap.INFO_SOURCE))
				.intValue();
	}
	
	private Vector getLeaves(String propName) {
		Vector leaves = new Vector();
		String leafNames = props.getProperty(propName + ".leaves", null);
		// Parse string and set the leaves
		if (leafNames != null) {
			leafNames = leafNames.substring(1, leafNames.length() - 1);
			StringTokenizer st = new StringTokenizer(leafNames, ",");
			while (st.hasMoreTokens()) {
				String tok = st.nextToken().trim();
				leaves.addElement(OntologyMap.getButtonIndex(tok));
			}
		}
		return leaves;
	}
	
	private boolean isFilterSelected(String propName) {
		if (useWorkspaceSettings()) {
			String prop = props
			.getProperty(propName + ".filterStatus", "false");
			if ("true".equals(prop))
				return true;
			else
				return false;
		} else
			return false;
	}
	
	public void setupInputMaps() {
		// Now, any change to the shared InputMap or ActionMap
		// will affect all components registered to use it
		// Register accelerator keys
		defaultActionMap.put("help", new HelpAction("Help"){
		public void actionPerformed(ActionEvent evt) {
				System.out.println("Help " + evt.paramString() + 
						" -- " + evt.getSource());
				if ( evt.getSource() instanceof ParameterEditor)
					System.out.println(((ParameterEditor)evt.getSource()).getHelpId());
				// process((JTextComponent)evt.getSource());
			}
		});
		defaultActionMap.put("actionName", new AbstractAction("actionName") {
			public void actionPerformed(ActionEvent evt) {
				System.out.println("Action Map 1");
				// process((JTextComponent)evt.getSource());
			}
		});
		AddProbabilityAction addProbabilityAction = 
			new AddProbabilityAction("Add Probability");
		
		defaultActionMap.put
				(addProbabilityAction.getValue(Action.NAME), 
				 addProbabilityAction);
		
		AddConditionAction addConditionAction = 
			new AddConditionAction("Add Condition");
		defaultActionMap.put
				(addConditionAction.getValue(Action.NAME), 
				 addConditionAction);
		defaultInputWhenFocusedMap.put(KeyStroke.getKeyStroke("F1"), "help");
		//defaultInputWhenFocusedMap.put(KeyStroke.getKeyStroke("F3"), "actionName2");
		defaultInputWhenFocusedMap.put(KeyStroke.getKeyStroke("F3"), addProbabilityAction.getValue(Action.NAME) );
		defaultInputWhenFocusedMap.put(KeyStroke.getKeyStroke("F4"), addConditionAction.getValue(Action.NAME));
		defaultInputWhenFocusedMap.put(KeyStroke.getKeyStroke("F5"),"actionName2");
		defaultInputWhenFocusedMap.put(KeyStroke.getKeyStroke("F7"),"actionName");
		addMainToolBarKeyMappings(defaultInputWhenFocusedMap, defaultActionMap);
		addPersistibleActionKeyMappings(defaultInputWhenFocusedMap, defaultActionMap);
	}

	
	static public InputMap getDefaultInputMap() {
		return defaultInputWhenFocusedMap;
	}
	
	static public ActionMap getDefaultActionMap() {
		return defaultActionMap;
	}
	/** TODO: Chaneg key mapping 
	 * 
	 */
	static public void addDefaultKeyMappings(){
	
	}
	/**
	 * 
	 */
	static public void addHelpKeyMapping(){
		
	}
	static public void addMainToolBarKeyMappings(JComponent component) {
	}
	static public void addMainToolBarKeyMappings(InputMap inputMap, ActionMap actionMap) {
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK, true),
				ModelBuilderToolbarPanel.saveAction.getValue(Action.NAME));
		actionMap.put(ModelBuilderToolbarPanel.saveAction
				.getValue(Action.NAME), ModelBuilderToolbarPanel.saveAction);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_MASK, true),
				ModelBuilderToolbarPanel.optionAction.getValue(Action.NAME));
		actionMap.put(ModelBuilderToolbarPanel.optionAction
				.getValue(Action.NAME), ModelBuilderToolbarPanel.optionAction);
//		 Find
		FindAction find = new FindAction("find");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				InputEvent.CTRL_MASK, true), find.getValue(Action.NAME));
		actionMap.put(find.getValue(Action.NAME), find);
		// Enter
		ShowEditorAction showEditor = new ShowEditorAction("ShowEditor");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				showEditor.getValue(Action.NAME));
		actionMap.put(showEditor.getValue(Action.NAME), showEditor);
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W,
				InputEvent.ALT_MASK, true), "showWindowMenu");
		actionMap.put("showWindowMenu", new AbstractAction(
		"showWindowMenu") {
			public void actionPerformed(ActionEvent evt) {
				Component topParent = ComponentHelper
				.getTopParent((Component) evt.getSource());
				// System.out.println("topParent " + topParent);
				// Get the Window menu and click it
				WindowMenu wMenu = null;
				if (topParent instanceof EditorFrame) {
					wMenu = ((EditorFrame) topParent).getWindowMenu();
				} else if (topParent instanceof OncFrame) {
					wMenu = ((OncFrame) topParent).getWindowMenu();
					if (wMenu != null)
						wMenu.doClick();
				} else if (topParent instanceof OncBrowser) {
					wMenu = windowMenu;
				}
				if (wMenu != null)
					wMenu.doClick();
				
			}
			
		});
	}
	static public void addPersistibleActionKeyMappings(JComponent component) {
		
	}
	/**
	 * 
	 */
	static public void addPersistibleActionKeyMappings(InputMap inputMap, ActionMap actionMap) {
		AddTreeNode linkNodeAdd = new AddTreeNode("Add Link Node", true,
				AddTreeNode.CONNECTED_CLASSES);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				InputEvent.CTRL_MASK, true), linkNodeAdd.getValue(Action.NAME));
		actionMap.put(linkNodeAdd.getValue(Action.NAME), linkNodeAdd);
		// Root node add
		AddTreeNode rootNodeAdd = new AddTreeNode("Add Root Node", false);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				InputEvent.CTRL_MASK, true), rootNodeAdd.getValue(Action.NAME));
		actionMap.put(rootNodeAdd.getValue(Action.NAME), rootNodeAdd);
		// add keyword
		AddTreeNode keywordAdd = new AddTreeNode("Add Keyword", true,
				AddTreeNode.KEYWORD_ONLY);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_K,
				InputEvent.CTRL_MASK, true), keywordAdd.getValue(Action.NAME));
		actionMap.put(keywordAdd.getValue(Action.NAME), keywordAdd);
		// delete
		RemoveNode remove = new RemoveNode();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
				remove.getValue(Action.NAME));
		actionMap.put(remove.getValue(Action.NAME), remove);
		UnlinkNode unlink = new UnlinkNode();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
				unlink.getValue(Action.NAME));
		actionMap.put(unlink.getValue(Action.NAME), unlink);
		
		// add variable 
		AddVariableText addVariableText = new AddVariableText("Add Variable Text");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				InputEvent.ALT_MASK, true), addVariableText.getValue(Action.NAME));
		actionMap.put(addVariableText.getValue(Action.NAME), addVariableText);
		
		
		
	}
	
	static public void addOntologyTree(OntologyTree oTree) {
		System.out.println("addingOntologyTree " + ontologyTrees.size() + " : "
				+ oTree.getName());
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
		while (i.hasNext()) {
			OntologyTree oTree = (OntologyTree) i.next();
			oTree.updateTree();
		}
		// System.out.println("Runtime memory usage " +
		// Runtime.getRuntime().totalMemory()
		// + " free " + Runtime.getRuntime().freeMemory()
		// + " used "
		// +
		// (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())
		// );
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
	
	public static boolean notifyUIOfPersistibleChanges() {
		return notifyUIOfPersistibleChanges;
	}
	
	public static void setNotifyUIOfPersistibleChanges(boolean notify) {
		notifyUIOfPersistibleChanges = notify;
	}
	
	class OntologyTabbedPanel extends JPanel implements TreePanelListener,
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
		
		OncScrollListButtonPanel buttonPanel = null;
		
		Vector tabList = new Vector();
		
		public OntologyTabbedPanel() {
			init();
			initializeKeywordTreeModels();
		}
		
		private void init() {
			setLayout(new BorderLayout());
			JPanel northPanel = new JPanel();
			// buttonPanel =
			// new OncScrollListButtonPanel();
			GridBagLayout g = new GridBagLayout();
			GridBagConstraints c3 = new GridBagConstraints();
			g.columnWeights = new double[] { 0.0f, 1.0f };
			northPanel.setLayout(g);
			northPanel.add(new JLabel("Items Related to Selected Tree Node"),
					c3);
			c3.anchor = GridBagConstraints.SOUTHEAST;
			c3.insets = new Insets(8, 5, 5, 5);
			// northPanel.add(buttonPanel, c3);
			add(northPanel, BorderLayout.NORTH);
			
			// Add a tabbed pane with lists of selected items
			tabbedPane = new JTabbedPane();
			
			// Make this a method
			codeBundleList = new OncScrollList(CodeBundle.class, false, false);
			tabbedPane.addTab("Code Bundle", codeBundleList);
			tabList.addElement(codeBundleList);
			encodingList = new OncScrollList(Encoding.class, false, false);
			tabbedPane.addTab("Encoding", encodingList);
			tabList.addElement(encodingList);
			
			infoSourceList = new OncScrollList(InformationSource.class, false,
					false);
			tabbedPane.addTab("Info Source", infoSourceList);
			tabList.addElement(infoSourceList);
			interpretationList = new OncScrollList(Interpretation.class, false,
					false);
			tabbedPane.addTab("Interpretation", interpretationList);
			tabList.addElement(interpretationList);
			
			keywordList = new OncScrollList(Keyword.class, false, false);
			tabbedPane.addTab("Keyword", keywordList);
			tabList.addElement(keywordList);
			
			nuggetList = new OncScrollList(KnowledgeNugget.class, false, false);
			tabbedPane.addTab("Knowledge Nugget", nuggetList);
			tabList.addElement(nuggetList);
			
			modelControllerList = new OncScrollList(ModelController.class,
					false, false);
			tabbedPane.addTab("Model Controller", modelControllerList);
			tabList.addElement(modelControllerList);
			
			submodelList = new OncScrollList(SubModel.class, false, false);
			tabbedPane.addTab("SubModel", submodelList);
			tabList.addElement(codeBundleList);
			
			submodelGroupsList = new OncScrollList(SubModelGroup.class, false,
					false);
			tabbedPane.addTab("SubModel Group", submodelGroupsList);
			tabList.addElement(submodelGroupsList);
			
			statementBundleList = new OncScrollList(StatementBundle.class,
					false, false);
			tabbedPane.addTab("Statement Bundle", statementBundleList);
			tabList.addElement(statementBundleList);
			
			statementTemplateList = new OncScrollList(StatementTemplate.class,
					false, false);
			tabbedPane.addTab("Statement Template", statementTemplateList);
			tabList.addElement(statementTemplateList);
			add(tabbedPane, BorderLayout.CENTER);
			
			// Register a change listener
			tabbedPane.addChangeListener(this);
			
		}
		
		public void stateChanged(ChangeEvent evt) {
			// System.out.println("TAB CHANGED: " + evt);
			JTabbedPane pane = (JTabbedPane) evt.getSource();
			
			// Get current tab
			Component list = pane.getSelectedComponent();
			// System.out.println("buttonPanel " + buttonPanel);
			// System.out.println("OntologyMap " + ontologyButtonPanel);
			// buttonPanel.setScrollList((OncScrollList)list);
			// tree.refresh();
		}
		
		public void changed(TreePanelEvent evt) {
			// The user changed their selections update the tree
			// System.out.println("TREE CHANGED: " + evt);
			Object userObject = evt.getSource();
			if (userObject instanceof Persistible) {
				if (selectedObjects.size() == 1)
					selectedObjects.set(0, userObject);
				else
					selectedObjects.add(0, userObject);
			}
			refreshTabs();
		}
		
		public void refreshTabs() {
			// System.out.println("SELECTED OBJECTS : " +
			// selectedObjects );
			if (selectedObjects.size() > 0) {
				Object first = selectedObjects.firstElement();
			}
			Iterator i = tabList.iterator();
			while (i.hasNext()) {
				OncScrollList tabList = (OncScrollList) i.next();
				tabList.clear();
				// TEMPORARILY DISABLE
				// tabList.setData(getList(tabList.getListType(),
				// selectedObjects));
				tabList.setLinkTos(selectedObjects);
			}
		}
		
	}
	
	private void checkConsistency() {
		// Loop through all selected code bundles
		// not implemented yet
	}
	
	public JFrame getFrame() {
		return this;
	}
	
	static public OncBrowser getOncBrowser() {
		return oncBrowser;
	}
	
	static public WindowMenu getWindowMenu() {
		return windowMenu;
	}
	
	static public GenericTree getTree() {
		return tree;
	}
	
	private void setTree() {
	}
	
	/**
	 * The main program for the OncViewer class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		
		if (!SystemUtil.checkVersion())
			System.exit(1);
		start(args);
	}
	
	public static void start(String[] args)
	{
		showSplashScreen(); 
	    
		System.out.println("Maximum memory available: "
				+ Runtime.getRuntime().maxMemory());
		showMainWindow(args);
		EventQueue.invokeLater( new SplashScreenCloser() );
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		frame.setAlwaysOnTop(false);
		
	}
	
	private static void showMainWindow(String[] args){
		
		try {
			UIManager.setLookAndFeel(WINDOWS);
		} catch (Exception ex) {
			System.out.println("Failed loading L&F: ");
		}
		UIManager.put("Table.selectionBackground", new Color(228, 225, 220));
		UIManager.put("Table.selectionForeground", Color.BLACK);
		UIManager.put("List.selectionBackground", new Color(228, 225, 220));
		UIManager.put("List.selectionForeground", Color.BLACK);
		oncotcap.Oncotcap.handleCommandLine(args);
		OncoTCapDataSource dataSource = oncotcap.Oncotcap.getDataSource();
		loadWorkspace();
		frame = new OncBrowser();
		oncBrowser = frame;
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		WindowListener windowListener = new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				Component recentFocusOwner = oncBrowser
				.getMostRecentFocusOwner();
			}
			
			public void windowClosing(WindowEvent e) {
				// Check to see if the user wants to save
				// Check to see if the user wants to save workspace
				// Save workspace
				saveWorkspaceToFile();
				int saveAnswer = 1;
				if (oncotcap.Oncotcap.getDataSource().hasChanged() == true) {
					saveAnswer = JOptionPane.showConfirmDialog(frame,
							"Would you like to save changes", "information",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
					if (saveAnswer == 0) {
						boolean successful = oncotcap.Oncotcap.getDataSource()
						.commit();
						if (successful)
							System.exit(0);
					} else if (saveAnswer == 1) {
						System.exit(0);
					}
				} else {
					System.exit(0);
				}
			}
		};
		center(frame);
		frame.addWindowListener(windowListener);
		// Let the menu window listen in too
		frame.addWindowListener(WindowMenu.getWindowListener());
	}
//	 PRIVATE //
	  
	
	  /**
	  * Show a simple graphical splash screen, as a quick preliminary to the main screen.
	  */
	  private static void showSplashScreen(){
	    fSplashScreen = new SplashScreen("../resource/image/erthsplash.jpg");
	    fSplashScreen.splash();
	  }
	  
	  /**
	  * Removes the splash screen. 
	  *
	  * Invoke this <code>Runnable</code> using 
	  * <code>EventQueue.invokeLater</code>, in order to remove the splash screen
	  * in a thread-safe manner.
	  */
	  private static final class SplashScreenCloser implements Runnable {
	    public void run(){
	      fSplashScreen.dispose();
	    }
	  }

	  
	// Collect the current workspace settings and save to a XML file
	private static void saveWorkspaceToFile() {
		try {
			// write property file for 1st pass
			
			FileOutputStream outputStream = new FileOutputStream(
			"OncBrowserWorkspace.prop");
			Properties props = new Properties();
			props.setProperty("OncBrowser.suggestKeywords", String
					.valueOf(OncBrowser.suggestKeywords()));
			props.setProperty("OncBrowser.saveWorkspaceOnExit", String
					.valueOf(OncBrowser.saveWorkspace()));
			props.setProperty("OncBrowser.useWorkspaceSettings", String
					.valueOf(OncBrowser.useWorkspaceSettings()));
			// Store the position of the divider bar on main panel
			props.setProperty("OncBrowser.mainDividerLocation", String
					.valueOf(topSplitPane.getDividerLocation()));
			
			// Get settings from main tree
			AbstractButton btn = o1
			.getButton(OntologyTree.FILTER_TOGGLE_BUTTON);
			props.setProperty("TopOntologyTree.filterStatus", String
					.valueOf(btn.isSelected()));
			btn = o1.getButton(OntologyTree.SIBLINGS_TOGGLE_BUTTON);
			props.setProperty("TopOntologyTree.siblingStatus", String
					.valueOf(btn.isSelected()));
			// Other tree
			btn = o2.getButton(OntologyTree.FILTER_TOGGLE_BUTTON);
			props.setProperty("BottomOntologyTree.filterStatus", String
					.valueOf(btn.isSelected()));
			btn = o2.getButton(OntologyTree.SIBLINGS_TOGGLE_BUTTON);
			props.setProperty("BottomOntologyTree.siblingStatus", String
					.valueOf(btn.isSelected()));
			// Get root name
			String rootName = OntologyMap.getRootNodeName(o1
					.getOntologyButtonPanel());
			props.setProperty("TopOntologyTree.root", rootName);
			Vector leafNames = o1.getAllSelectedLeaves();
			props.setProperty("TopOntologyTree.leaves", leafNames.toString());
			// Get root name second browser tree
			rootName = OntologyMap.getRootNodeName(o2.getOntologyButtonPanel());
			props.setProperty("BottomOntologyTree.root", rootName);
			leafNames = o2.getAllSelectedLeaves();
			props
			.setProperty("BottomOntologyTree.leaves", leafNames
					.toString());
			// Store the names of the keywords
			props.setProperty("OncBrowser.workspaceKeywords",
					GlobalKeywordsPanel.getKeywordsString());
			
			props.store(outputStream,
			"Workspace properties for OncoTCap browser");
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Write STs and SBs to file
//		writeToXML(StatementTemplate.class);
//		writeToXML(StatementBundle.class);
//		readFromXML(StatementTemplate.class);
//		readFromXML(StatementBundle.class);
		// Now save the two main filters
		encodeFilter(o1.getFilter(), topFilterName);
		encodeFilter(o2.getFilter(), bottomFilterName);
		
	}
	

	private static void writeToXML(Class cls){
		//		Save all the statement bundles to a file
		OncoTCapDataSource dataSource = Oncotcap.getDataSource();
		Collection templates = dataSource.find(cls);
		try {
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream(cls.getName() + ".xml")));
			System.out.println("Writing " + cls.getName() + ".xml");
			for ( Object obj : templates) {
				encoder.writeObject(obj);
			}
			encoder.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Failed XML writing StatementBundles.xml");
		}
	}
	
	private static void readFromXML(Class cls){
		//		Read in from XML File
		try {
			BufferedInputStream buf = new BufferedInputStream(
		            new FileInputStream(cls.getName() + ".xml"));
	        XMLDecoder decoder = new XMLDecoder(buf);
	        boolean continueReading = true;
	        while ( continueReading ){
	        	try {
	        		 Object o = decoder.readObject();
	     	        System.out.println("Read in " + o.getClass() + " " + o);
	        	}catch(ArrayIndexOutOfBoundsException aiobe){
	        		continueReading = false;
	        	}
	        }
	    
	       
	      
	        decoder.close();

	    } catch (FileNotFoundException e) {
			System.out.println("ERROR: Failed XML writing StatementBundles.xml");
	    }


	}
	

	private static void encodeFilter(OncFilter filter, String filterName) {
		try {
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream(filterName + ".xml")));
			
			// Specify to the encoder, the name of the property
			// that is associated
			// with the constructor's parameter(s)
			// Keyword does not work without this delegate but some of the
			// other top level persistibles do
			String[] propertyNames = new String[] { "GUID" };
			encoder.setPersistenceDelegate(ModelController.class,
					new DefaultPersistenceDelegate(propertyNames));
			propertyNames = new String[] { "strId" };
			encoder.setPersistenceDelegate(GUID.class,
					new DefaultPersistenceDelegate(propertyNames));
			propertyNames = new String[] { "text" };
			encoder.setPersistenceDelegate(SearchText.class,
					new DefaultPersistenceDelegate(propertyNames));
			if (filter != null) {
				OncFilterLite oncFilterLite = new OncFilterLite();
				oncFilterLite.copyFilter(filter.getRootNode());
				encoder.writeObject(oncFilterLite);
			}
			
			encoder.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Failed XML writing " + filterName);
		}
		
	}
	
	static public OncFilter loadFilterFromFile(String fileName) {
		OncFilter workspaceFilter = null;
		try {
			System.out.println("Loading filter from..." + fileName);
			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(
					new FileInputStream(fileName)));
			
			Object workspaceFilterLite = decoder.readObject();
			decoder.close();
			// Rebuild a real filter
			if (workspaceFilterLite != null
					&& workspaceFilterLite instanceof OncFilterLite)
				workspaceFilter = ((OncFilterLite) workspaceFilterLite)
				.buildFilter();
		} catch (FileNotFoundException fnfe) {
			System.out.println("ERROR: File not found " + fileName);
		} catch (Exception e) {
			System.out.println("WARNING: problem loading filter from "
					+ fileName + " Probably empty filter.");
			e.getCause().printStackTrace();
		}
		return workspaceFilter;
	}
	
	static private void loadWorkspace() {
		try {
			// load property file and set settings
			props = new Properties();
			FileInputStream inputStream = new FileInputStream(
			"OncBrowserWorkspace.prop");
			props.load(inputStream);
			if (props.getProperty("OncBrowser.useWorkspaceSettings").equals(
			"true"))
				setUseWorkspaceSettings(true);
			else
				setUseWorkspaceSettings(false);
			if (useWorkspaceSettings()) {
				if (props.getProperty("OncBrowser.suggestKeywords").equals(
				"true"))
					setSuggestKeywords(true);
				else
					setSuggestKeywords(false);
				
				if (props.getProperty("OncBrowser.saveWorkspaceOnExit").equals(
				"true"))
					setSaveWorkspace(true);
				else
					setSaveWorkspace(false);
				try {
					dividerLocation = Integer
					.valueOf(
							props
							.getProperty("OncBrowser.mainDividerLocation"))
							.intValue();
				} catch (Exception nfe) {
					System.out.println("Problem setting browser location.");
					topSplitPane.setDividerLocation(0);
				}
				
			}
			
		} catch (Exception e) {
			System.out.println("No OncBrowserWorkspace.props file");
			return;
		}
		
		// Load any saved filters
		try {
			topFilter = loadFilterFromFile(topFilterName + ".xml");
			bottomFilter = loadFilterFromFile(bottomFilterName + ".xml");
		} catch (Exception ee) {
			System.out.println("ERROR: loading filters");
		}
		System.out.println("filters are loaded.");
	}
	
	public void treeNodesChanged(TreeModelEvent e) {
	}
	
	public void treeNodesInserted(TreeModelEvent e) {
	}
	
	public void treeNodesRemoved(TreeModelEvent e) {
	}
	
	public void treeStructureChanged(TreeModelEvent e) {
	}
	
	// implement component listener
	public void componentShown(ComponentEvent evt) {
		// System.out.println("visible");
	}
	
	public void componentHidden(ComponentEvent evt) {
		// Component is now hidden
		// System.out.println("hidden");
		
	}
	
	public void componentMoved(ComponentEvent evt) {
		// System.out.println("moved");
	}
	
	// This method is called after the component's size changes
	public void componentResized(ComponentEvent evt) {
		// System.out.println("resized");
	}
	
	public static void repaintIt() {
		if (oncBrowser != null)
			oncBrowser.repaint();
	}
	private static void center(JFrame frame){
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle rect = frame.getBounds();
		frame.setLocation((screen.width - rect.width)/2, (screen.height - rect.height)/2);
	}
	// Help
	private static void initHelp() { 
		// Find the HelpSet file and create the HelpSet object:
		
		ClassLoader cl = OncBrowser.class.getClassLoader();
		try {
			URL hsURL = HelpSet.findHelpSet(cl, helpHS);
			helpSet = new HelpSet(cl, hsURL);
		} catch (Exception ee) {
			// Say what the exception really is
			System.out.println( "HelpSet " + ee.getMessage());
			System.out.println("HelpSet "+ helpHS +" not found");
			return;
		}
		// Create a HelpBroker object:
		helpBroker = helpSet.createHelpBroker();
		helpBroker.setHelpSet(helpSet);
	}
	
	public static void enableHelp(Component component){
		String idString = "Help";
		if ( component instanceof HelpEnabled)
			idString = ((HelpEnabled)component).getHelpId();
		else 			
			idString = component.getClass().getSimpleName();
		if ( component instanceof JFrame )
			component = ((JFrame)component).getRootPane();
		CSH.setHelpIDString(component, idString);
		System.out.println("ENABLE HELP "+ idString + " COMPONENT " + component);
		getHelpBroker().enableHelp(component, idString, helpSet);    
		getHelpBroker().enableHelpKey(component, idString, helpSet);
	}
	
	/**
	 * @return Returns the helpBroker.
	 */
	public static HelpBroker getHelpBroker() {
		if ( helpBroker == null )
				initHelp();
		return helpBroker;
	}
	/**
	 * @param helpBroker The helpBroker to set.
	 */
	public static void setHelpBroker(HelpBroker hb) {
		OncBrowser.helpBroker = hb;
	}
	/**
	 * @return Returns the helpSet.
	 */
	public static HelpSet getHelpSet() {
		return helpSet;
	}
	/**
	 * @param helpSet The helpSet to set.
	 */
	public static void setHelpSet(HelpSet hs) {
		OncBrowser.helpSet = hs;
	}
	
	public void close() {
		processEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	public void showSplitPane(String windowConstant) {
		if ( topSplitPane != null ) 
			if ( windowConstant.equals(JSplitPane.BOTTOM)) {
				topSplitPane.remove(o2);
				topSplitPane.setBottomComponent(o1);
			}
			else if ( windowConstant.equals(JSplitPane.TOP)) {
				topSplitPane.remove(o1);
				topSplitPane.setTopComponent(o2);
			}
			else {
				topSplitPane.setTopComponent(o2);
				topSplitPane.setBottomComponent(o1);
				topSplitPane.setOneTouchExpandable(true);
				topSplitPane.setDividerSize(10);
				// Set a proportional location
				//topSplitPane.setDividerLocation(dividerLocation);
			}
				
	}
}
