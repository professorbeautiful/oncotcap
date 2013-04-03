package oncotcap.display.modelcontroller.cellkineticsdemo;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import oncotcap.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.StatementBundleTreeModel;
import oncotcap.display.browser.HelpMenu;
import oncotcap.display.browser.OncBrowser;
import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.StatementBundleEditorPanel;
import oncotcap.display.modelcontroller.AskQuestion;
import oncotcap.engine.*;
import oncotcap.engine.montecarlo.MonteCarloEngine;
//import sun.awt.geom.AreaOp.AddOp;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DefaultInputEditor extends JFrame implements ActionListener, EditorSelectionListener //, ModelEditor
{
	private static final String STARTINGOBJECT = "IRB";
	private static final int BUTTON_BAR_HEIGHT = 34;
	private int splitWidth = 200;
	private JPanel pnlButtons = new JPanel();
//	private JButton btnExit = new JButton("Exit");
	JButton btnCheat = new JButton("CHEAT");
	private JButton btnPrint = new JButton("Print");
	private JButton btnNext = new JButton("Run");
	private JButton btnAdd = new JButton("Add");
	private JButton btnPrevious = new JButton(" Previous");
	private JButton btnBeginning = new JButton(" Beginning");
	private JButton btnAsk = new JButton(" Ask Question");
	private JCheckBox checkSameSimulation = new JCheckBox("Run the same simulation again.");
	private JCheckBox checkUseOneRNGPerAgent = new JCheckBox("Use a separate random number generator for each process.");
	private JTree treStatements = new JTree();
	private StatementBundleTreeModel treeData;
	private JScrollPane spStatements = new JScrollPane(treStatements);
	private StatementBundleEditorPanel cse;
	private JLabel lblTree = new JLabel(OncoTcapIcons.getImageIcon("treeheader.gif"));
	private Vector statementBundles;
	private StatementListPanel pnlList;
	private JPanel pnlLine = new JPanel();
	private JPanel pnlTree = new JPanel();
	private JPanel pnlProtocolTitle = new JPanel();
//	private NoviceEditor me = this;
	private IntroFrame introFrame;
	private JSplitPane splitPane;
	private JSplitPane splitRHS;
	private int selectedRow = -1;
	private boolean programmaticChange = false;
//	private SummaryFrame summaryFrame = null;
//	private OncInstance helpInfo = null;
	private ValueMap globalValueMap = new ValueMap(ValueMap.GLOBAL);
	private boolean firstInit = true;
	private TreeSelectionListener selList = null;
	private AskQuestion askQ = null;
	private RetrieveQuestion retrieveQ = null;
	private ModelDefinition model;
	private DefaultInputEditor me;
	private WaitWindow waitWindow;
	private HelpMenu helpMenu = null;
	private JMenuBar menuBar = null;
	private Long savedSeed = null;
	
	private StatementChangeListener statementChangeListener;
	
	JLabel lblList = new JLabel(OncoTcapIcons.getImageIcon("listheader.gif"), JLabel.LEFT);
//	SortedList codeBundles = new SortedList(new CodeBundleCompare());
	boolean shouldShowInstructorCheats = false;
//	String project = "C:\\TcapData\\Interventions.pprj";
	String scenario = "Brufomycin Scenario";
/*	public static void main(String [] args)
	{
		Logger.log(new Integer(args.length));
		String project = Oncotcap.getInstallDir() + "TcapData\\Interventions.pprj";
		String scenario = "Brufomycin Scenario";
		OncTreeModel td;
		if (args.length > 0)
			scenario = args[0];
		if (args.length == 2)
			project = args[1];
		td = new OncTreeModel(project, scenario);
		
		NoviceEditor nui = new NoviceEditor(td, null);
	}
*/
	public DefaultInputEditor(ModelDefinition model)
	{
		this(model, null);
	}
	public DefaultInputEditor(ModelDefinition model, IntroFrame introFrame)
	{
		this("Model Simulation Input for: " + model.getController().toString(), model, introFrame);
	}

	public DefaultInputEditor(String title, ModelDefinition model, IntroFrame introFrame)
	{
		super(title);
		// add keys
//		getRootPane().getInputMap().setParent(OncBrowser.getDefaultInputMap());
//		getRootPane().getActionMap().setParent(OncBrowser.getDefaultActionMap());
		//Add help access
		//OncBrowser.enableHelp(this);
		// INitialize the help menu
		if ( helpMenu == null ) {
			menuBar = new JMenuBar();
			helpMenu = new HelpMenu("Help", this);
			menuBar.add(helpMenu);
		}
		
		this.introFrame = introFrame;
		this.model = model;
		model.getController().getConfiguration().sortVisibleConfigurations();
		model.getConfigurations().addConfigurations(model.getController().getStatementConfigurations());
		this.treeData = new StatementBundleTreeModel(model);
		init();
	}
	public DefaultInputEditor(String title, ModelDefinition model)
	{
		this(title, model, null);
	}

	public ModelDefinition getModel()
	{
		return(model);
	}
	private void expandallnodes() {
		// Time expended:  1.5 hours.
		// Next statement is just to see info.
		treStatements.addTreeExpansionListener(
											   new TreeExpansionListener() {
			public void treeCollapsed(TreeExpansionEvent event) {
				//Logger.log("Tree collapsed " + event.getPath()
				//  + " visibleRowCount=" + treStatements.getRowCount());
			}
			public void treeExpanded(TreeExpansionEvent event) {
				//Logger.log("Tree expanded " + event.getPath()
				//+ " visibleRowCount=" + treStatements.getRowCount());
			}
			//Oddly, getVisibleRowCount is greater than getRowCount,
			// because it includes *potential* rows.
		}
		);
		// Here's the real action.
		int currentRowCount = treStatements.getRowCount();
		int diff;
		do {
			expandNodes();
			int newRowCount = treStatements.getRowCount();
			diff = newRowCount - currentRowCount;
			currentRowCount = newRowCount;
		} while (diff != 0);
	}
	void expandNodes() {
		for (int i=treStatements.getRowCount(); i>=0 ; i--) {
			TreePath descendant = treStatements.getPathForRow(i);
			//Logger.log("TreePath #" + i + " expands: " + descendant);
			treStatements.expandPath(descendant);
		}
	}

	private void init()
	{
		me = this;
		StatementBundle sb;
//		OncVariable var;
		if(firstInit)
		{
//			addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){
//				System.exit(0);}});

//			addCheatKey();
		}
		treeData.reload();
    	expandallnodes();
//		statementBundles = treeData.statementBundles;
		if(firstInit)
		{
			pnlList = new StatementListPanel(model);
			pnlList.setBorder(BorderFactory.createLineBorder(Color.green, 4));
		}
//		else
//			pnlList.setStatementBundles(statementBundles);
		
//		SelectionManager.setStatementList(pnlList);
//		StatementEditor.setNoviceEditor(this);
//		StatementEditor.setParent(this);

		if(firstInit)
		{
			pnlTree.setLayout(null);
			pnlTree.setBackground(TcapColor.lightGreen);
			pnlTree.add(lblTree);
			pnlTree.add(spStatements);
			lblTree.setLocation(40,0);
			lblTree.setSize(200,30);
			lblTree.setPreferredSize(new Dimension(200,30));
			pnlProtocolTitle.setLayout(null);
			pnlProtocolTitle.setBackground(TcapColor.darkBrown);
			pnlProtocolTitle.setLocation(0,0);
			pnlProtocolTitle.setSize(300,40);
			pnlProtocolTitle.setMinimumSize(new Dimension(300,40));
			lblList.setLocation(0,0);
			lblList.setSize(300,30);
			lblList.setPreferredSize(new Dimension(300,30));
			pnlProtocolTitle.add(lblList);
			splitRHS = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlProtocolTitle, pnlList);
			splitRHS.setDividerSize(0);
			splitRHS.setDividerLocation(30);
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlTree, splitRHS);
		}
		
/*		Iterator iter2;
		Iterator iter = statementBundles.iterator();
		globalValueMap.clear();
		while(iter.hasNext())
		{
			sb = (StatementBundle) iter.next();
			sb.defaultLocalValueMap.clear();
			iter2 = sb.getVariables().iterator();
			while(iter2.hasNext())
			{
				var = (OncVariable) iter2.next();
				if(var.getScope() == ValueMap.GLOBAL)
					globalValueMap.put(var.getName(), var);
				else
					sb.defaultLocalValueMap.put(var.getName(), var);
			}
		}
*/
//		SelectionManager.setTree(treStatements);
		if(firstInit)
			treStatements.setCellRenderer(new NoviceTreeCellRenderer());
		((DefaultTreeCellRenderer)treStatements.getCellRenderer()).setBackgroundSelectionColor(TcapColor.lightBlue);
		((DefaultTreeCellRenderer)treStatements.getCellRenderer()).setBackgroundNonSelectionColor(TcapColor.lightGreen);
		((DefaultTreeCellRenderer)treStatements.getCellRenderer()).setTextNonSelectionColor(Color.black);
		((DefaultTreeCellRenderer)treStatements.getCellRenderer()).setTextSelectionColor(Color.black);
		treStatements.setModel(treeData);

		Logger.log(" ===tree model is set");
		expandallnodes();

		treStatements.setRootVisible(false);
		treStatements.addTreeSelectionListener(selList = new StatementTreeSelectionListener());

		if(firstInit)
		{
			pnlTree.addComponentListener(new ResizeListener());
		
			treStatements.setExpandsSelectedPaths(true);
			Rectangle bnds = getGraphicsConfiguration().getBounds();
		
			addComponentListener(new ResizeListener());
			//getContentPane().setLayout(null);
			checkSameSimulation.setEnabled(false);
			checkUseOneRNGPerAgent.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					if(!checkUseOneRNGPerAgent.isSelected())
					{
						checkSameSimulation.setSelected(false);
						checkSameSimulation.setEnabled(false);
					}
				}
			});
			setLayout(new BorderLayout());
			pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
			pnlButtons.add(this.checkUseOneRNGPerAgent);
			pnlButtons.add(this.checkSameSimulation);
			
			setIconImage(OncoTcapIcons.getImage(getClass().getSimpleName()+".jpg"));
			setSize(800,600);
			treStatements.setBorder(BorderFactory.createEmptyBorder());
			treStatements.setBackground(TcapColor.lightGreen);
			treStatements.setPreferredSize(new Dimension(700,700));
			treStatements.setSize(700,700);
			spStatements.setLocation(0,30);
			spStatements.setBorder(BorderFactory.createEmptyBorder());
			spStatements.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			spStatements.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//			spStatements.setLocation(0,lblTree.getHeight());

//		btnExit.setMnemonic('X');
			btnCheat.setMnemonic('C');
			btnNext.setMnemonic('R');  
			btnAdd.setMnemonic('A');  
			btnPrevious.setMnemonic('P');
			btnBeginning.setMnemonic('B');
			btnAsk.setMnemonic('K');
			btnPrint.setMnemonic('T');
			btnPrevious.setIcon(OncoTcapIcons.getImageIcon("leftarrow.gif"));
			btnAsk.setIcon(OncoTcapIcons.getImageIcon("ask.gif"));
			btnPrevious.setDisabledIcon(OncoTcapIcons.getImageIcon("leftarrow-disabled.gif"));
			btnBeginning.setIcon(OncoTcapIcons.getImageIcon("leftarrow-end.gif"));
			btnBeginning.setDisabledIcon(OncoTcapIcons.getImageIcon("leftarrow-end-disabled.gif"));
			btnNext.setIcon(OncoTcapIcons.getImageIcon("littleguy.gif"));
			btnNext.setDisabledIcon(OncoTcapIcons.getImageIcon("littleguy-disabled.gif"));
			btnNext.setToolTipText("Simulate this clinical trial design");
			btnPrevious.setToolTipText("Go to previous screen");
			btnBeginning.setToolTipText("Go to the first screen");
			btnAsk.setToolTipText("Ask a question");
			btnPrint.setToolTipText("Print this clinical trial design");
	//		btnExit.addActionListener(this);
			btnCheat.addActionListener(this);
			btnNext.addActionListener(this);
			btnPrevious.addActionListener(this);
			btnBeginning.addActionListener(this);
			btnAsk.addActionListener(this);
			btnPrint.addActionListener(this);
			
			if(introFrame != null)
			{
				btnPrevious.setEnabled(true);
				btnBeginning.setEnabled(true);
			}
			else
			{
				btnPrevious.setEnabled(false);
				btnBeginning.setEnabled(false);
			}

			btnPrint.setEnabled(false);

			//set button sizes
	//		btnExit.setSize(100,50);
//			btnCheat.setSize(75, 30);
//			btnPrint.setSize(105,30);
//			btnNext.setSize(105,30);
//			btnPrevious.setSize(105,30);
//			btnBeginning.setSize(110,30);
//			btnAsk.setSize(150,30);

			//add buttons to button panel
	//		pnlButtons.add(btnExit);
//			pnlLine.setBackground(Color.black);
//			pnlLine.setLocation(0,BUTTON_BAR_HEIGHT - 4);
//			pnlLine.setSize(getWidth(), 4);
//			pnlLine.setPreferredSize(new Dimension(getWidth(),4));

//			pnlButtons.add(btnCheat);
//			pnlButtons.add(btnPrint);
			pnlButtons.add(btnBeginning);
			pnlButtons.add(btnPrevious);
			pnlButtons.add(btnNext);
			
//			pnlButtons.add(btnAsk);
			//pnlButtons.add(pnlLine);
			JPanel northPanel = new JPanel(new BorderLayout());
			northPanel.add(menuBar, BorderLayout.NORTH);
			northPanel.add(pnlButtons, BorderLayout.SOUTH);
			getContentPane().add(northPanel, BorderLayout.NORTH);
			//getContentPane().add(pnlButtons);
			getContentPane().add(splitPane, BorderLayout.CENTER);

			//menuBar.setLocation(0,0);
			//btnCheat.setLocation(0,0);
			//pnlButtons.setLocation(0,0);
			splitPane.setBorder(BorderFactory.createEmptyBorder());
			splitPane.setLocation(0,BUTTON_BAR_HEIGHT);
			splitPane.setDividerSize(3);

			pnlList.addDeleteActionListener(this);
			pnlList.setMinimumSize(new Dimension(100,0));
			pnlList.setPreferredSize(new Dimension(430,0));
			treStatements.setMinimumSize(new Dimension(50,0));
			treStatements.setPreferredSize(new Dimension(270,0));
			splitPane.setDividerLocation(270);
			setLocation((((int) bnds.getWidth()) - 800)/2, (((int) bnds.getHeight())- 600)/2);
			pnlList.resizePanel(430,320);
			
			statementChangeListener = new StatementChangeListener();
			
			setVisible(true);
		}
		btnNext.setEnabled(true);
		btnNext.setEnabled(false);  //wes 
		
		firstInit = false;
		resize();
	}
	public Vector getStatementBundles()
	{
		return(statementBundles);
	}
	private void resize()
	{
		//pnlButtons.setSize(getWidth(), BUTTON_BAR_HEIGHT);
		
//		btnExit.setLocation(getWidth() - btnExit.getWidth()-10, 0);
//		btnPrint.setLocation(getWidth() - btnPrint.getWidth() /* - btnExit.getWidth()*/ - 7, 0);
//		btnNext.setLocation(getWidth() - btnNext.getWidth() /* - btnPrint.getWidth() */ /*- btnExit.getWidth()*/ - 7, 0);
//		btnPrevious.setLocation(getWidth() - btnPrevious.getWidth() - btnNext.getWidth() /* - btnPrint.getWidth() - btnExit.getWidth()*/ - 7, 0);
//		btnBeginning.setLocation(getWidth() - btnBeginning.getWidth() - btnPrevious.getWidth() - btnNext.getWidth() /*- btnPrint.getWidth() */ - 7, 0);
		//btnAsk.setLocation(getWidth() - btnAsk.getWidth() - btnBeginning.getWidth() - btnPrevious.getWidth() - btnNext.getWidth() - btnPrint.getWidth() - 7, 0);
		pnlLine.setSize(getWidth(),4);
		pnlLine.setPreferredSize(new Dimension(getWidth(),4));
		spStatements.setSize(splitPane.getDividerLocation(),getHeight()-BUTTON_BAR_HEIGHT-30);
		spStatements.setPreferredSize(new Dimension(300,300));
		int x = getWidth() - splitPane.getDividerLocation()-10;
		int y = this.getContentPane().getHeight() - menuBar.getHeight() - pnlButtons.getHeight() - lblList.getHeight() - 7;
		pnlList.resizePanel(x ,y );
		pnlList.revalidate();
		pnlList.repaint();
		//Logger.log("panel size set to " + x + " X " + y);

//		Dimension d = treStatements.getPreferredScrollableViewportSize();
//		treStatements.setSize(d);
//		treStatements.setPreferredSize(d);
		splitPane.setSize(getWidth(), getHeight() - pnlButtons.getHeight());
		splitPane.revalidate();
		splitPane.repaint();
	}
	private void reloadProject()
	{
		treStatements.removeTreeSelectionListener(selList);
		treeData = new StatementBundleTreeModel(model);  //pick the project and scenario
		init();
//		SelectionManager.unselect();
		pnlList.clearEditors();
	}
	private void startQuestionReader()
	{
		if(retrieveQ == null)
			retrieveQ = new RetrieveQuestion();
		retrieveQ.clear();
		retrieveQ.setVisible(true);
	}
	private void addCheatKey()
	{
		JComponent jc = (JComponent) getContentPane();

		ActionMap aMap = jc.getActionMap();
		InputMap iMap = jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		Action cheat = new AbstractAction("Cheat"){
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("ActionPerformed");
			}
		};
		aMap.put(cheat.getValue(Action.NAME), cheat);
		iMap.put(KeyStroke.getKeyStroke(new Character('D'), java.awt.event.InputEvent.ALT_DOWN_MASK /*new Character('d'), InputEvent.ALT_MASK*/), cheat.getValue(Action.NAME));
		jc.setActionMap(aMap);
		jc.setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, iMap);
		System.out.println("**************************************Key listener installed");
		addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent e) {System.out.println("KeyTyped");}
			public void keyPressed(KeyEvent e) {System.out.println("KeyPressed");}
			public void keyReleased(KeyEvent e){
				System.out.println("KeyReleased");
				if ((e.getKeyChar() == 'c' ||e.getKeyChar() == 'd') &&
					  (	KeyEvent.getKeyModifiersText(e.getModifiers()).equals("Alt")))
				{
					System.out.println("CHEAT KEY");
//					loadDefaultValues();
					pnlList.refreshEditors();					
				}
				else if ((e.getKeyChar() == 'w') &&
					  (	KeyEvent.getKeyModifiersText(e.getModifiers()).equals("Alt")))
				{
					reloadProject();
				}
				else if ((e.getKeyChar() == 'i') &&
						 (	KeyEvent.getKeyModifiersText(e.getModifiers()).equals("Alt")))
				{
					shouldShowInstructorCheats = ! shouldShowInstructorCheats;
					if (shouldShowInstructorCheats)
						showInstructorCheats();
					else
						hideInstructorCheats();
				}
				else if ((e.getKeyChar() == 'q') &&
						 (	KeyEvent.getKeyModifiersText(e.getModifiers()).equals("Alt")))
				{
					startQuestionReader();
				}
				

			}
		});
	}
	void showInstructorCheats() {
		programmaticChange = true; //prevents selectionlistener screwups.
		treeData.addInstructorCheatsToTree();
		expandallnodes();
		programmaticChange = false;
	}
	void hideInstructorCheats() {
         programmaticChange = true; //prevents selectionlistener screwups.
         treeData.removeInstructorCheatsFromTree();
         expandallnodes();
         programmaticChange = false;
	}
	private class ModelCompiler implements Runnable
	{
		private Long simulationSeed = null;
		private boolean useSingleRNG;
		
		public ModelCompiler(Long simulationSeed, boolean useSingleRNG)
		{
			this.simulationSeed = simulationSeed;
			this.useSingleRNG = useSingleRNG;
		}
		public void run()
		{
			OncModel oncModel = new OncModel(model);
			SimulationEngine engine = new MonteCarloEngine(new OncModel(model));
			try {
				RunnableModel rModel = engine.assemble();
				if(simulationSeed == null)
					simulationSeed = new java.util.Random().nextLong();
				rModel.setSimulationSeed(simulationSeed.longValue());
				savedSeed = rModel.getSimulationSeed();
				rModel.setSingleRNG(useSingleRNG);
				waitWindow.setVisible(false);
				waitWindow = null;
				if(rModel != null)
					rModel.run();
			}
			catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace(System.err);
				if(waitWindow != null)
				{
					waitWindow.setVisible(false);
					waitWindow = null;
				}
//				JOptionPane.showMessageDialog(me,e.getMessage(),"Compilation failed.",JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private class ResizeListener implements ComponentListener
	{
		public void componentHidden(ComponentEvent e) {}
		public void componentMoved(ComponentEvent e) {}
		public void componentResized(ComponentEvent e){ resize(); }
		public void componentShown(ComponentEvent e) {}
	}

	DefaultMutableTreeNode getTreeNode(TreePath path)
	{
		return (DefaultMutableTreeNode)(path.getLastPathComponent());
	}

	private boolean saveCurrentStatement()
	{
/*		if (cse.currentlyEditing)
		{
			if (!cse.addThisStatement())
			{
				JOptionPane.showMessageDialog(this, "Current statement is not complete");
				return(false);
			}

			if (treeData.allRequiredComplete())
				btnNext.setEnabled(true);
			else
				btnNext.setEnabled(false);
		} */
		return(true);

	}
	class StatementTreeSelectionListener implements TreeSelectionListener 
	{
		TreePath selectionPath;
		public void valueChanged(TreeSelectionEvent event)
		{
			StatementBundleConfiguration sbConfig;
			if (!programmaticChange)
			{
				if(!saveCurrentStatement())
				{
					programmaticChange = true;
					if (selectedRow >= 0) treStatements.setSelectionRow(selectedRow);
					return;
				}
				else
				{
					selectionPath = treStatements.getSelectionPath();
					selectedRow = treStatements.getSelectionRows()[0];
				}
				DefaultMutableTreeNode node = getTreeNode(event.getPath());
				Object a = node.getUserObject();
				if (a instanceof StatementBundleConfiguration)
				{
					try
					{
						sbConfig = (StatementBundleConfiguration) a;

						if(sbConfig.areMultiplesAllowed())
							pnlList.enableMore();
						else
							pnlList.disableMore();
						
						if(sbConfig.getSelectionPath() == null)
							sbConfig.setSelectionPath(selectionPath);
						
//						selSB.setIsListed(true, 0);
						StatementBundle firstStatement = model.getConfigurations().getFirstStatement(sbConfig);
						if(firstStatement == null)
						{
							firstStatement = model.getConfigurations().addStatement(sbConfig);
							model.addStatementBundle(firstStatement, sbConfig);
						}
						
						cse = firstStatement.getStatementEditor();
//						
						if (cse == null)
						{
							firstStatement.setStatementEditor(cse = (new StatementBundleEditorPanel(firstStatement)));
							new StatementSelectionListener(cse);
							cse.setEditControlsVisible(false);
							sbConfig.setTreeRow(selectedRow);
							cse.setStatementBundleConfiguration(sbConfig);
							cse.setEditorSelectionListener(me);
							cse.setSaveToDataSourceOnCreate(false);
							cse.addStatementChangeListener(statementChangeListener);
//							cse.refreshStatement();
						}
						//selSB.setSelected(true,0);
						SelectionManager.select(cse); // uncommented by rd 4/30/06
						pnlList.ensureIsShown(firstStatement.getStatementEditor());
						pnlList.selectStatement(cse);
						pnlList.ensureIsShown(cse); // added by rd 4/30/06
//						if(! cse.addedToPanel)
//						{
//							SelectionManager.select(cse);
//							pnlList.ensureIsShown(cse);
//							cse.addedToPanel = true;
//							pnlList.listPanelAdd(cse);
//						}
//						else
						pnlList.refreshEditors(true);
					}
					catch (java.lang.ArrayIndexOutOfBoundsException e)
					{
						Logger.log("index out of bounds");
					}
					checkRunButtonStatus(); //wes 
				}
				else
					SelectionManager.unselect();
			}
			else
			{
				programmaticChange = false;
				SelectionManager.unselect();
			}
		}
		
	}
	private void addAllStatements() {
		Logger.log("ADDING all statements");
		//TODO:  allow alt-A to add all statements to model at once.
	}
	private void addSelectedStatement() {
		// previously, this was connected to mnemonic 'A' in the case statement.
		// apparently never used, since this mnemonic was not set.
		StatementBundleEditorPanel parentSE;
		if((parentSE = pnlList.getSelectedStatement()) != null) {
			StatementBundle sb = parentSE.getStatementBundle();
			addOneStatement(sb);
		}
	}
	private void addOneStatement(StatementBundle sb) {
		StatementBundle newSB;
		StatementBundleEditorPanel se = new StatementBundleEditorPanel((newSB = model.getConfigurations().addStatement(model.getConfigurations().getConfiguration(sb))));
		model.addStatementBundle(newSB, model.getConfigurations().getConfiguration(sb));
		new StatementSelectionListener(se);
		se.setEditControlsVisible(false);
//			newSB.setIsListed(true);
		newSB.setStatementEditor(se);
//			selSB.setTreeRow(selectedRow);
		se.setEditorSelectionListener(me);
		se.setStatementBundleConfiguration(model.getConfigurations().getConfiguration(sb));
		se.setSaveToDataSourceOnCreate(false);
		se.addStatementChangeListener(statementChangeListener);
		se.refreshStatement();
		SelectionManager.select(se);
//			sb.setIsListed(true,editorIdx);
		pnlList.ensureIsShown(se);
		pnlList.selectStatement(se);
		se.addedToPanel = true;
		pnlList.listPanelAdd(se);
		pnlList.refreshEditors();
		treStatements.repaint();
		checkRunButtonStatus(); //wes 
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();
		if (o instanceof JButton)
		{
			JButton button = (JButton) o;
			switch(button.getMnemonic())
			{
				case 'A':
				{
					addAllStatements();
				}
				break;
				case 'X':
				{
					System.exit(1);
				}
				break;
				case 'K':
					{
					if(askQ == null)
						askQ = new AskQuestion();
					askQ.clear();
					askQ.setVisible(true);
				}
				break;
				case 'R':
				{
					Long seed = null;
					boolean oneRNG = false;
					waitWindow = new WaitWindow(this, "DefaultInputEditor: Compiling. Please wait.");
					waitWindow.setVisible(true);
					if(this.checkSameSimulation.isEnabled() && this.checkSameSimulation.isSelected() && savedSeed != null)
						seed = savedSeed;
					else
						seed = null;
					
					
					
					ModelCompiler mc = new ModelCompiler(seed, !checkUseOneRNGPerAgent.isSelected());
					Thread t = new Thread(mc);
					t.start();
					if(checkUseOneRNGPerAgent.isSelected())
						this.checkSameSimulation.setEnabled(true);
					//jf.setVisible(false);
					// oncotcap.simtest.patient pat = new oncotcap.simtest.patient(new oncotcap.process.TopOncParent());
// 					SinglePatient sp = new SinglePatient(pat);
// 					sp.setSize(800, 600);
// 					sp.setVisible(true);
// 					//oncotcap.Oncotcap.handleCommandLine(args);
// 					oncotcap.sim.schedule.MasterScheduler.execute();

				}
				break;
				case 'P':
				{
					if(introFrame != null)
					{
						setVisible(false);
						introFrame.setVisible(true);
					}
				}
				break;
				case 'B':
				{
					if(introFrame != null)
					{
						setVisible(false);
						introFrame.gotoBeginning();
						introFrame.setVisible(true);
					}
				}
				break;
				case 'T':
				{

					String fileToPrint = Oncotcap.getTempPath() + "tcap.html";
//					writeActiveStatements(fileToPrint);
					Util.printHTMLFile(fileToPrint);
//					Logger.log("writing file in " + Oncotcap.getTempPath());
//					if(mainWindow.printAtRun())
//						Util.printHTMLFile(om.getPackageDir() + "tcap.html");

				}
				break;
				case 'V':  //Remove statement
				{
					StatementBundleEditorPanel sbep = pnlList.getSelectedStatement();
					if(sbep != null)
					{
						StatementBundle sb = sbep.getStatementBundle();
						if(sb != null)
						{
							model.removeStatement(sb);
							pnlList.removeStatement(sbep);
							treStatements.setSelectionRow(0);
						}
					}
					
/*					StatementEditor se = SelectionManager.getSelectedEditor();
					if(se != null)
					{
						StatementBundle sb = se.getStatementBundle();
						int idx = se.getBundleIndex();
						sb.setIsListed(false, idx);
						SelectionManager.unselect();
						se.addedToPanel = false;
						se.setVisible(false);
						programmaticChange = true;
						sb.removeEditorInfo(idx);
						treStatements.setSelectionRow(0);
						programmaticChange = false;
						pnlList.refreshEditors();
						treStatements.repaint();
						checkRunButtonStatus();
					} */
				}
				break;
				case 'C':
				case 'D':
				{
//					loadDefaultValues();
					pnlList.refreshEditors();
				}
				break;
				default:
				{
					Logger.log("WARNING: UNHANDLED BUTTON ACTION IN FieldNavigator");
				}
			}
		}
	}

	public void editorSelected(StatementBundle sb)
	{
/*		Logger.log("EDITOR SELECTED");
		programmaticChange = true;
		if(selectedStatementBundle != null && sb != selectedStatementBundle)
		{
			Logger.log("unselected");
			selectedStatementBundle.unselect();
		}
		selectedStatementBundle = sb;
		if(sb.getTreeRow() >= 0)
		{
			Logger.log("Tree row = " + sb.getTreeRow());
			treStatements.setSelectionRow(sb.getTreeRow());
			selectedRow = sb.getTreeRow();
			pnlList.enableDelete();
		}
		programmaticChange = false;
		pnlList.ensureIsShown(sb.getStatementEditor(bundleIndex));
		pnlList.refreshEditors(true); */
	}

/*	void assembleCodeBundles()
	{
		StatementBundle sb;
		Iterator iter = statementBundles.iterator();
		codeBundles.clear();
		while(iter.hasNext())
		{
			sb = (StatementBundle) iter.next();
//			Logger.log("Editor Count = " + sb.getEditorCount());
			if((sb.required().booleanValue() && !sb.include().booleanValue()))
			{
				assembleCodeBundles(sb, 0);
			}
			else
			{
				for(int n=0;n<sb.getEditorCount();n++)
				{
					if(sb.getIsListed(n))
						assembleCodeBundles(sb, n);
				}
			}
		}
	}
	private void assembleCodeBundles(StatementBundle sb, int localValueMapIndex)
	{
		OldCodeBundle codeB;
		Collection cbs;
		Iterator iter2;
		cbs = sb.getInstances("CodeBundles");
		if(cbs != null)
		{
			iter2 = cbs.iterator();
			while(iter2.hasNext())
			{
//				Logger.log("Assembling BUNDLE");
				codeB = new OldCodeBundle(new OncInstance((Instance) iter2.next()));
				codeB.applyValueMap(globalValueMap, sb, localValueMapIndex);
				codeBundles.add(codeB);
			}
		}
	}
	
	void shipCodeBundles()
	{
		OldOncModel om = new OldOncModel(codeBundles, STARTINGOBJECT);
		om.run(this);
	}

	private void loadDefaultValues()
	{
		StatementBundle sb;
		OncVariable var;
		String defVal;
		Iterator iter2;
		Iterator iter = statementBundles.iterator();
		while(iter.hasNext())
		{
			sb = (StatementBundle) iter.next();
			iter2 = sb.getInstances("Variables").iterator();
			while(iter2.hasNext())
			{
				try
				{
					var = new OncVariable((Instance) iter2.next());
					defVal = var.getDefaultValue();
					if(defVal != null)
					{
						var.setValue(defVal);
						if(var.getScope() == ValueMap.GLOBAL)
							globalValueMap.setVariable(var);
						else
						{
							sb.defaultLocalValueMap.setVariable(var);
						}
					}
				}
				catch(VariableScopeException e){Logger.log("********** " + e);}
			}
			sb.setAllLocalMapsToDefault();
		}
	} */ 
	public boolean checkRunButtonStatus()
	{
		if(addedSBsComplete() && allRequiredAdded())
		{
			btnNext.setEnabled(true);
			return(true);
		}
		else
		{
			btnNext.setEnabled(false);
			return(false);
		}
	}
	private boolean addedSBsComplete()
	{
		if(model.getStatements().size() <= 0)
			return(false);
		
		Enumeration sbs = model.getStatements().keys();
		while(sbs.hasMoreElements())
		{
			StatementBundle sb = (StatementBundle) sbs.nextElement();
			if(! sb.isComplete())
				return(false);
		}
		return(true);
	}
	private boolean allRequiredAdded()
	{
		if(treStatements.getModel().getRoot() instanceof OncTreeNode)
		{
			OncTreeNode topNode = (OncTreeNode) treStatements.getModel().getRoot();
			return(allRequiredAdded(topNode));
		}
		else
			return(false);
	}
	private boolean allRequiredAdded(OncTreeNode node)
	{
		Iterator it = node.getChildren();
		Object testNode;
		while(it.hasNext())
		{
			testNode = it.next();
			if(testNode instanceof OncTreeNode)
				if(! allRequiredAdded((OncTreeNode) testNode))
					return(false);
		}
		if(node.getUserObject() instanceof StatementBundleConfiguration)
		{
			StatementBundleConfiguration sbc = (StatementBundleConfiguration) node.getUserObject();
			if(sbc.isRequired())
				if(! model.getConfigurations().contains(sbc) || model.getConfigurations().getStatements(sbc).size() <= 0)
					return(false);
		}
		return(true);
	}

	private class StatementChangeListener implements StatementBundleEditorPanel.StatementChangeListener
	{
		public void statementChanged(StatementBundleEditorPanel panel)
		{
			treStatements.repaint();
			pnlList.refreshEditors();
		}
	}

	private class StatementSelectionListener implements MouseListener
	{
		StatementBundleEditorPanel panel = null;
		public StatementSelectionListener(StatementBundleEditorPanel panel)
		{
			panel.getEditorPane().addMouseListener(this);
			this.panel = panel;
		}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e)
		{
			pnlList.selectStatement(panel);
			TreePath path = treeData.findObject(panel.getStatementBundleConfiguration());
			treStatements.setSelectionPath(path);
		}
		public void mouseReleased(MouseEvent e) {}

	}
/*
	public void refreshComponents()
	{
		pnlList.refreshEditors(true);
		treStatements.repaint();
//		checkRunButtonStatus();
	}

/*	public void writeActiveStatements(String fileName)
	{

		StatementBundle sb;
		StatementEditor se;
		Iterator iter = statementBundles.iterator();
		int n;
		try
		{
			FileWriter out = new FileWriter(fileName, false);
			out.write("<html>\n<body>\n");

			while(iter.hasNext())
			{
				sb = (StatementBundle) iter.next();
				for(n=0; n<sb.getEditorCount(); n++)
				{
					if((se = sb.getStatementEditor(n)) != null)
					{
						if(se.isVisible())
						{
							out.write("<p>" + StringHelper.getHTMLBody(se.getText()) + "</p>\n");
						}
					}
				}
			}
//!!
		
		out.write("</body></html>\n");
		out.close();
		}catch(IOException e){Logger.log("Error writing print file: " + fileName + "\n" + e);}
	}
*/

	private boolean isComplete(StatementBundleConfiguration sbc)
	{
		return(model.isComplete(sbc));
	}
	static int count = 0;
	static Icon check = OncoTcapIcons.getImageIcon("check.gif");
	static Icon exclam = OncoTcapIcons.getImageIcon("exclam.gif");
	static Icon sheet = OncoTcapIcons.getImageIcon("sheet.gif");
	static Icon multiplesheets = OncoTcapIcons.getImageIcon("multiplesheets.gif");
	static Icon requiredMultiple = OncoTcapIcons.getImageIcon("requiredmultiple.gif");
	private static StatementTreeRendererComponent statementRenderer = new StatementTreeRendererComponent();
	class NoviceTreeCellRenderer extends DefaultTreeCellRenderer
	{
		Icon currIcon = null;

		public void setLeafIcon(Icon icon){}
		public void mySetLeafIcon()
		{
			if(currIcon != null)
			{
				super.setLeafIcon(currIcon);
			}
			else
				Logger.log("Current icon is null");
		}

		public void paint(Graphics g)
		{
			super.paint(g);
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			checkRunButtonStatus();
			StatementBundleConfiguration sbc;
			Object o;
			if(value instanceof DefaultMutableTreeNode)
			{

				o = ((DefaultMutableTreeNode) value).getUserObject();
				if(o instanceof StatementBundleConfiguration)
				{
					sbc = (StatementBundleConfiguration) o;
					statementRenderer.setText(o.toString());
					if(isComplete(sbc))
						statementRenderer.setIcon(check);
					else if(sbc.isRequired() && sbc.areMultiplesAllowed())
						statementRenderer.setIcon(requiredMultiple);
					else if(sbc.isRequired())
						statementRenderer.setIcon(exclam);
					else if(sbc.areMultiplesAllowed())
						statementRenderer.setIcon(multiplesheets);
					else
						statementRenderer.setIcon(sheet);
					
					if(selected)
						statementRenderer.setBackground(this.getBackgroundSelectionColor());
					else
						statementRenderer.setBackground(this.getBackgroundNonSelectionColor());
					return(statementRenderer);
				}
				else
					super.setLeafIcon(sheet);
			}
			else
				super.setLeafIcon(sheet);
			return(super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus));
		}
	}
}

      

