package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;

import oncotcap.util.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.browser.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.*;


public class SubsetParameterEditorPanel extends EditorPanel
		implements SaveListener, TreeModelListener, ActionListener, 
							 DocumentListener,
							 DoubleClickListener {
		protected SubsetParameter param = null;
		
		protected	JPanel stageAndClassPanel;
		
		
		private OncFilter filter;
		protected SubsetFilter subsetFilter;
		protected Keyword keywordConstraint = null;
		
		protected OncTreeNode rootNode;
		protected DefaultTreeModel model;
		
		protected Droppable droppedItem = null;
		protected boolean keywordsOnly = false;
		protected OntologyTree ontologyTree = null;
		protected OncScrollableTextArea nameField;
		protected JButton addLevelListBtn = new JButton("LL+");
		protected JButton showLevelListBtn = new JButton("LLs"); //temporary
		
		protected BooleanTree booleanTree = null;
		protected FilterEditorPanel filterEditorPanel = null;
		private Vector nodesShowingLevelList = new Vector();

		public SubsetParameterEditorPanel() {
				setLayout(new BorderLayout());
				SubsetParameterTransferHandler transferHandler = 
						new SubsetParameterTransferHandler();
				filterEditorPanel = new FilterEditorPanel();
				booleanTree = (BooleanTree)filterEditorPanel.getTree();
				booleanTree.setTransferHandler(transferHandler);
				//filterEditorPanel.edit(subsetFilter);
				booleanTree.getModel().addTreeModelListener(this);
				((GenericTreeSelectionListener)booleanTree.getSelectionListener()).addDoubleClickListener(this);
				JPanel fPanel = new JPanel(new BorderLayout());
				JLabel fPanelLabel = new JLabel("Define subset here");
				fPanelLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
				fPanel.add(fPanelLabel,
										BorderLayout.NORTH);
				fPanel.add(filterEditorPanel, BorderLayout.CENTER);
				add(fPanel, BorderLayout.CENTER);
				JPanel nameBox = new JPanel(new BorderLayout());
				String displayText = new String();
				nameField = new OncScrollableTextArea(displayText, "DISPLAY", false);
				nameField.setFont(new Font("Helvetica", Font.PLAIN, 11));
				nameField.setEnabled(true);
				nameField.setMinimumSize(new Dimension(500, 150));
				nameField.addDocumentListener(this);
				JLabel nameLabel = new JLabel("Subset as a phrase");
				nameLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
				nameBox.add(nameLabel, BorderLayout.NORTH);
				nameBox.add(nameField, BorderLayout.SOUTH);
				add(nameBox, BorderLayout.NORTH);
				setPreferredSize(new Dimension(800,600));
				//param.getBasePicker().addSaveListener(this);
		}
		
	public  void edit(Object objectToEdit){
			setFocusSavingEnabled(false);
			edit((SubsetParameter)objectToEdit );
	}
	public void edit(SubsetParameter parameter)
	{
		this.param = parameter;
		this.setSaveToDataSourceOnCreate((parameter.getPersistibleState() == Persistible.DO_NOT_SAVE ? false : true));
		if ( param != null ) {
				param.getBasePicker().addSaveListener(this);
				if ( param.getAllowMultiples() == false ) {
						// THis is a one one picker
						OneOneSubsetParameterTH oneOneTransferHandler = 
								new OneOneSubsetParameterTH();
						booleanTree.setTransferHandler(oneOneTransferHandler);
				}	
		}
		if ( param.subsetFilter == null ) {
				param.subsetFilter =
						new SubsetFilter(getFilterName(param.getPickerType()));
				param.subsetFilter.setDisplayValueFlavor
		 						(param.getPickerType());
		}
		nameField.setValue(param.subsetFilter.getDisplayName());
		//param.setValue(param.subsetFilter.getDisplayName()); 
		subsetFilter = param.subsetFilter;
	
		rootNode = subsetFilter.getRootNode();
		filterEditorPanel.edit(subsetFilter);
		// EnumSubsetFilter filt = this.param.getAttributeFilter();
		// 		filt.addSaveListener(this);
		// 		charFilter.setFilter(filt);
		ontologyTree = getKeywordTree();
		ontologyTree.addPopup(UndefinedBooleanExpression.class, 
													new BooleanExpressionPopup());
		JPanel otPanel = new JPanel(new BorderLayout());
		JLabel levelLabel = new JLabel("Available characteristics and levels");
		levelLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
		otPanel.add(levelLabel,
								BorderLayout.NORTH);
		otPanel.add(ontologyTree, BorderLayout.CENTER);

		add(otPanel, BorderLayout.EAST);
											 
	}

		private String getFilterName(int pickerType) {
				switch ( pickerType ) {
				case DeclareEnumPicker.BOOLEAN_EXPRESSION:
						return "subset";
				case DeclareEnumPicker.ASSIGNMENT: 
						return "assignment";
				case DeclareEnumPicker.INSTANTIATION: 
						return "instantiation";
				default:
						return "subset";
				}
		}

	public void save()
	{
		if(param != null)
		{
				//param.setValue(param.subsetFilter.getDisplayValue());
				if (param.subsetFilter == null )
						param.subsetFilter = subsetFilter;
				else 
						param.subsetFilter.setRootNode(rootNode);
				param.setName(param.subsetFilter.getDisplayName());
				param.subsetFilter.getRootNode().setPersistibleState(Persistible.UNSET);
				param.subsetFilter.getRootNode().update();
				param.subsetFilter.update();
				param.update();
		}
	}
	public Object getValue()
	{
		if(param != null)
			return(param);
		else
			return(null);
	}

		// Double Click 	
		public void doubleClicked(DoubleClickEvent evt){
				int x = 0;
				int y = 0;
				if ( evt.getMouseEvent() != null ) { 
						x = evt.getMouseEvent().getX();
						y = evt.getMouseEvent().getY();
				}
					if ( evt.getSource() instanceof JTree ) {
						Object obj = ((JTree)evt.getSource()).getLastSelectedPathComponent();						Object userObject = null;
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
						else {
								JOptionPane.showMessageDialog
										(null, 
										 "Double click heard in subset parameter editor");
						}
				}
		}


	//for save listener on attributeFilter
	public void objectSaved(SaveEvent e)
	{
			//System.out.println("objectSaved");
			// If keyword changed then refresh tree
			if ( keywordConstraint != param.getAttributeBaseKeyword() ) {
					ontologyTree.setFilter(getFilter(param.getAttributeBaseKeyword()));
					keywordConstraint = param.getAttributeBaseKeyword();
			}
			refreshKeywordTree();
	}

		public void treeNodesChanged(TreeModelEvent e) {
				treeStructureChanged(e);
		}
		
		public void treeNodesInserted(TreeModelEvent e) {	
				treeStructureChanged(e);
		}
		
		public void treeNodesRemoved(TreeModelEvent e) {
				treeStructureChanged(e);
		}

		public void treeStructureChanged(TreeModelEvent e) {
				nameField.setValue(param.subsetFilter.getDisplayName());
				param.update();
				param.subsetFilter.update();
		}

		public void actionPerformed(ActionEvent a) {
				if ( a.getSource() instanceof JButton ) {
						if ( ((JButton)a.getSource()).getText().equals("LL+") ) {
								showLevelListEditor();
						}
				}
		}
		public void changedUpdate(DocumentEvent de) {
					// 	System.out.println("setValue " + 
// 			
//			the following lines should no longer be needed.
//			SubsetParameter.getCodeValue() and SubsetParameter.getDisplayName()
//			return values directly from the subsetFilter now.												 param.subsetFilter.getDisplayName());
/*						if ( param != null )
						{
							param.setDisplayValue(param.subsetFilter.getDisplayName());
							param.setCodeValue(param.subsetFilter.getDisplayValue());
						} */
		}
		public void insertUpdate(DocumentEvent e) {
		}
		public void removeUpdate(DocumentEvent e) {
		}

		private void refreshKeywordTree() {
				ontologyTree.refresh();
				// For each node in the showLL vector refresh the level lists
				Iterator i = nodesShowingLevelList.iterator();
				while ( i.hasNext() ) {
						GenericTreeNode node = (GenericTreeNode)i.next();
						//System.out.println("refreshing ll for node " + node.getUserObject());
						showAssociatedLevelLists(node);
				}
		}

		protected OntologyTree getKeywordTree() {
				OntologyTree ot = new OntologyTree();
				ot.collapseController();
				ot.setFilterSelected(true);
				if ( param != null ) {
						ot.setFilter(getFilter(param.getAttributeBaseKeyword()));
						param.getBasePicker().addSaveListener(this);
				}
				
				ot.setName("Subset Parameter Keyword Ontology Tree");
				ot.getOntologyButtonPanel().setRoot(OntologyMap.K);
				ot.getOntologyButtonPanel().setLeaves
						(CollectionHelper.makeVector(new Integer(OntologyMap.K)));
				//ot.showFilterPanelAtBottom();
				//ot.getTree().updateTree(getTreeData(null));
				JToolBar toolBar  = ot.getToolBar();
				addLevelListBtn.addActionListener(this);
				toolBar.add(addLevelListBtn);
				OncBrowser.addOntologyTree(ot);
				return ot;
		}	
		public Hashtable getTreeData(OncFilter filter) {
				// get all enums and levels related to the filter keywords
				// 	Vector endPts = CollectionHelper.makeVector("EnumLevelList");
				// 				endPts.addElement("Keyword");
				Vector endPts = CollectionHelper.makeVector("Keyword");
				Hashtable treeHashtable = 
						oncotcap.Oncotcap.getDataSource().getInstanceTree
						("Keyword", 
						 endPts,
						 filter,
						 TreeDisplayModePanel.ROOT);
				return treeHashtable;
		}
		public OncFilter getFilter(Keyword keywordConstraint) {
				if ( keywordConstraint == null ) {
						keywordConstraint = KeywordFilter.CHARACTERISTIC_ROOT;
				}
// 				System.out.println("getFilter " + keywordConstraint);
				// Build a filter with the desired keyword as the only node
				OncFilter filter = null;
				if ( keywordConstraint != null ) {
						// Create a filter
						filter = new OncFilter(this.getSaveToDataSourceOnCreate());	
						OncTreeNode rootNode = filter.getRootNode();
						OncTreeNode andNode = new OncTreeNode(TcapLogicalOperator.AND, (this.getSaveToDataSourceOnCreate() ? Persistible.DIRTY : Persistible.DO_NOT_SAVE) );
					  OncTreeNode keywordNode = new OncTreeNode(keywordConstraint, (this.getSaveToDataSourceOnCreate() ? Persistible.DIRTY : Persistible.DO_NOT_SAVE));
						andNode.add(keywordNode);
						rootNode.add(andNode);
				}
				return filter;
		}

		public GenericTreeNode getSelectedNode() {
				TreePath treePath = ontologyTree.getTree().getSelectionPath();
				if (treePath == null) {
						// create an object equivalent to the root node type
						return null;
				}
				else {
						GenericTreeNode node =
								(GenericTreeNode)treePath.getLastPathComponent();
						return node;
				}
		}

		protected Keyword getSelectedKeyword() {
				// Determine which keyword is currently selected
				GenericTreeNode node = getSelectedNode();
				
				if ( node != null &&  node.getUserObject() instanceof Keyword )
						return (Keyword)node.getUserObject();
				else
						return null;
		}
		
		protected Keyword getThisKeyword(GenericTreeNode node) {
				if ( node.getUserObject() instanceof Keyword )
						return (Keyword)node.getUserObject();
				else
						return null;
		}

		// this will be changed to a pop menu later
		protected void showAssociatedLevelLists() {
				// Determine which keyword is currently selected
				GenericTreeNode node = getSelectedNode();
				if ( !nodesShowingLevelList.contains(node) ) 
						nodesShowingLevelList.addElement(node);
				showAssociatedLevelLists(node);
		}
		protected void showAssociatedLevelLists(GenericTreeNode node) {
				// Remove any existing boolean expressions from the node
				Vector existingLLs = new Vector();
				if (node.getChildCount() >= 0) {
						for (Enumeration e=node.children(); e.hasMoreElements(); ) {
								GenericTreeNode n = (GenericTreeNode)e.nextElement();
								if ( n.getUserObject() instanceof UndefinedBooleanExpression)
										existingLLs.addElement(n);
            }
        }
				ontologyTree.getTree().removePrunedNodes(existingLLs);
				Keyword keyword = null;
				if ( (keyword = getThisKeyword(node)) != null ) {
						Vector endPts = CollectionHelper.makeVector("Keyword");
						Hashtable keywordParents = 
								oncotcap.Oncotcap.getDataSource().getParentTree
								("Keyword",
								 endPts,
								 CollectionHelper.makeVector(keyword),
								 TreeDisplayModePanel.ROOT );
						// Take each of the parents and get the associated level lists
						if (keywordParents.size() <= 0)
								return;
						Hashtable levelLists = 
								oncotcap.Oncotcap.getDataSource().getInstanceTree
								("EnumLevelList",
								 new Vector(),
								 makeLevelListFilter(keywordParents),
								 TreeDisplayModePanel.ROOT);
																																	
						// Collect all the level lists from the hashtable
						Vector selectedItems = CollectionHelper.makeVector(keyword);
						for (Enumeration e = levelLists.keys();
								 e.hasMoreElements(); ) {
								Object obj = e.nextElement();
								if ( obj instanceof EnumLevelList ) {
										// Create a "undefined" boolean expression to 
										// add to the tree
										ontologyTree.getTree().addNode
												(getExpression(keyword, 
																			 (EnumLevelList)obj),
												 false, selectedItems);
								}										
						}
				}
				
		}

		protected UndefinedBooleanExpression getExpression(Persistible keyword,
																										 Persistible levelList) {
				UndefinedBooleanExpression expr = new UndefinedBooleanExpression();
				expr.setLeftHandSide(keyword);
				expr.setRightHandSide(levelList);
				return expr;
		}

		protected OncFilter makeLevelListFilter(Hashtable keywords) {
				// Create a filter
				filter = new OncFilter();	
				OncTreeNode rootNode = filter.getRootNode();
				OncTreeNode orNode = new OncTreeNode(TcapLogicalOperator.OR);
				OncTreeNode keywordNode;
				for (Enumeration e = keywords.keys();
						 e.hasMoreElements(); ) {
						Object obj = e.nextElement();
						if ( obj instanceof Keyword )
								orNode.add(new OncTreeNode(obj));
				}
				rootNode.add(orNode);
				return filter;
				
		}
		protected void showLevelListEditor() {
				// Get the editor panel and set ending keywboard to
				// control thekeyword list in the level list editor
				EnumLevelList ll = new EnumLevelList();
				LevelListEditorPanel llep = 
						(LevelListEditorPanel)ll.getEditorPanelWithInstance();
				llep.setEndingKeyword(getSelectedKeyword());
								
				EditorPanel editorPanel =
						EditorFrame.showEditor((Editable)ll,
																	 new Dimension(400,400));
				ll.addSaveListener(this);
		}
		public static void main(String [] args)
		{
				EditorFrame.showEditor(new OncFilter());
				//		JFrame jf = new JFrame();
				//		SubsetParameterEditorPanel fp = new SubsetParameterEditorPanel();
				//		fp.edit(new OncFilter());
				//		jf.getContentPane().add(fp);
				//		jf.setSize(500,500);
				//		jf.setVisible(true);
		}

		class BooleanExpressionPopup extends JPopupMenu {
				public BooleanExpressionPopup() {
						JMenuItem mi;
						mi = new JMenuItem("Test 1");
						mi.setActionCommand("insert");
						add(mi);
						mi = new JMenuItem("Test 2");
						mi.setActionCommand("remove");
						add(mi);  
						setOpaque(true);
						setLightWeightPopupEnabled(true);
				}
		}
}


