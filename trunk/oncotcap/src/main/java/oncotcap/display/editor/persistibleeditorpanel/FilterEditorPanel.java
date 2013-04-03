package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.datatransfer.*;
import javax.swing.border.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import oncotcap.util.*;
import oncotcap.datalayer.Persistible;
import oncotcap.display.browser.OncBrowser;
import oncotcap.display.browser.GenericTreeNode;
import oncotcap.display.browser.GenericTreeNodeRenderer;
import oncotcap.display.browser.DoubleClickListener;
import oncotcap.display.browser.DoubleClickEvent;
import oncotcap.display.common.DragDropLabel;
import oncotcap.display.common.Droppable;
import oncotcap.display.common.OncTreeNode;
import oncotcap.display.editor.EditorFrame;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;

import javax.swing.tree.*;

public class FilterEditorPanel extends EditorPanel
		implements TreeModelListener,
							 DoubleClickListener,
							 NodeDeleteListener
{
	public BooleanTree booleanTree;
	protected OperatorPane operators;
	protected	JPanel stageAndClassPanel;

	private JPanel classPanel;
	private JPanel stagePanel;
	private OncFilter filter;
	protected OncTreeNode rootNode;
	protected DefaultTreeModel model;

	private Droppable droppedItem = null;
	private boolean keywordsOnly = false;
	
   public FilterEditorPanel()
	{
	// setBorder(BorderFactory.createLineBorder(Color.blue, 3));		
// 		rootNode = new OncTreeNode(OncFilter.rootFilterObj);
// 		model = new DefaultTreeModel(rootNode);
		init();

	}
	public FilterEditorPanel(Droppable dropped)
	{
		this(dropped, false);
	}
	public FilterEditorPanel(Droppable dropped, boolean keywordsOnly)
	{
		droppedItem = dropped;
		this.keywordsOnly = keywordsOnly;
	}
	public FilterEditorPanel(OncFilter filter)
	{
		init();
		edit(filter);
	}
	public static void main(String [] args)
	{
		EditorFrame.showEditor(new OncFilter());
//		JFrame jf = new JFrame();
//		FilterEditorPanel fp = new FilterEditorPanel();
//		fp.edit(new OncFilter());
//		jf.getContentPane().add(fp);
//		jf.setSize(500,500);
//		jf.setVisible(true);
	}

	private void init()
	{
//		setBorder(BorderFactory.createLineBorder(Color.blue, 3));		
		rootNode = new OncTreeNode(OncFilter.rootFilterObj, 
															 Persistible.DO_NOT_SAVE);
		model = new DefaultTreeModel(rootNode);

		// Update only one tree instance
		booleanTree = new BooleanTree(model);
		booleanTree.getModel().addTreeModelListener(this);
		//booleanTree.addNodeDeleteListener(this);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(booleanTree);

		operators = new OperatorPane();
		//operators.setPreferredSize(new Dimension(150, Short.MAX_VALUE));
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		addOperators(BorderLayout.NORTH, SwingConstants.HORIZONTAL);
		setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
			    OncBrowser.getDefaultInputMap());
		setInputMap(JComponent.WHEN_FOCUSED,
			    OncBrowser.getDefaultInputMap());
		setActionMap(OncBrowser.getDefaultActionMap());

		
	}

		private JPanel getClassPanel() {
				classPanel = new JPanel();
				//classPanel.add(new HorizontalLine());
				classPanel.setLayout(new BoxLayout(classPanel, BoxLayout.Y_AXIS));
				Iterator it = SubsetClass.getAllClasses().iterator();
				while(it.hasNext())
						classPanel.add(new DragDropLabel((SubsetClass)it.next()));
				return classPanel;
		}
		private JPanel getStagePanel() {
				stagePanel = new JPanel();
				stagePanel.setLayout(new BoxLayout(stagePanel, BoxLayout.Y_AXIS));
				return stagePanel;
		}		

	public void edit(Object obj)
	{
		if(obj instanceof OncFilter)
			edit((OncFilter) obj);
	}
	public void edit(OncFilter filter)
	{
		this.filter = filter;
		rootNode = filter.getRootNode();
		keywordsOnly = filter.getKeywordsOnly();
		((DefaultTreeModel)booleanTree.getModel()).setRoot(rootNode);
		((DefaultTreeModel)booleanTree.getModel()).reload(); //sideeffect collapses
		booleanTree.expandAll();
		addListenersToExistingNodes(rootNode);
		booleanTree.revalidate();
		Iterator stagedObjs = filter.getStagedObjects().iterator();
		while(stagedObjs.hasNext())
		{
			Object obj = stagedObjs.next();
			if(obj instanceof Droppable)
				addToStage((Droppable) obj);
		}
		
		operators.revalidate();
		repaint();
	}


		private void addListenersToExistingNodes(DefaultMutableTreeNode rootNode){
				// All the nodes that were read in from the KB need to be heard too
				for (Enumeration e=rootNode.breadthFirstEnumeration();
						 e.hasMoreElements(); ) {
						OncTreeNode n = (OncTreeNode)e.nextElement();
						if ( n.getUserObject() instanceof Persistible)
								((Persistible)n.getUserObject()).addSaveListener(booleanTree);
				}
		}

		public void addOperators(String borderLayoutLocation, int orientation) {
				add(operators, borderLayoutLocation);

		}
		public void addTextLabel() {
				operators.add(new SearchTextLabel());

		}
	public Object getValue()
	{
		return(filter);
	}

	public void setFilter(OncFilter f) 
	{
		filter = f;
		if ( filter != null ) 
				edit(filter);
	}
	public OncFilter getFilter() 
	{
			return filter;
	}
	public void save()
	{
		if(filter == null)
			filter = new OncFilter();

		filter.setRootNode(rootNode);
		filter.setKeywordsOnly(keywordsOnly);
	}
	public boolean getKeywordsOnly()
	{
		return(keywordsOnly);
	}
	public void setKeywordsOnly(boolean keywordsOnly)
	{
		this.keywordsOnly = keywordsOnly;
	}
	public void removeOperator(TcapLogicalOperator op)
	{
		operators.removeOperator(op);
	}
	public void addNodeDeleteListener(NodeDeleteListener listener)
	{
		booleanTree.addNodeDeleteListener(listener);
	}
	public void removeNodeDeleteListener(NodeDeleteListener listener)
	{
		booleanTree.removeNodeDeleteListener(listener);
	}
	
		///////////
	public void setClassPanelVisible(boolean vis)
	{
		if(classPanel != null)
		{
			classPanel.setVisible(vis);
			revalidate();
			repaint();
		}
	}
	public void addToStage(Droppable d)
	{
		stagePanel.add(new DragDropLabel(d));
		//		System.out.println("added " + d );
		revalidate();
		repaint();
	}
	public void setClassesVisible(boolean vis)
	{
		classPanel.setVisible(vis);
	}
	
		public JTree getTree() {
				return booleanTree;
		}


		public void treeNodesChanged(TreeModelEvent e) {
				//System.out.println("Tree has changed 1");
		}
		
		public void treeNodesInserted(TreeModelEvent e) {	
				//System.out.println("Tree has inserted 1");
		}
		
		public void treeNodesRemoved(TreeModelEvent e) {
				//System.out.println("Tree has removed nodes 1");
		}

		public void treeStructureChanged(TreeModelEvent e) {
				//System.out.println("Tree has structure change 1");
		}
		public void addDoubleClickListener
				(DoubleClickListener listener) {
				booleanTree.getSelectionListener().addDoubleClickListener(listener);
		}
		public void doubleClicked(DoubleClickEvent evt){
		// 		int x = 0;
// 				int y = 0;
// 				if ( evt.getMouseEvent() != null ) { 
// 						x = evt.getMouseEvent().getX();
// 						y = evt.getMouseEvent().getY();
// 				}
// 				if ( evt.getSource() instanceof BooleanTree ) {
// 						Object obj = tree.getLastSelectedPathComponent();
// 						Object userObject = null;
// 						if ( obj instanceof DefaultMutableTreeNode ) 
// 								userObject = ((DefaultMutableTreeNode)obj).getUserObject();
// 						else
// 								return;
// 						if ( userObject instanceof Editable) {
// 								EditorPanel editorPanel =
// 										EditorFrame.showEditor((Editable)userObject, 
// 																					 null,
// 																					 x, y, 
// 																					 (GenericTree)evt.getSource());
// 								if ( userObject instanceof Persistible ) {
// 										((Persistible)userObject).addSaveListener(editorPanel);
// 								}
// 						}
// 						else {
// 								JOptionPane.showMessageDialog
// 										(null, 
// 										 "There is no editor for the selected object.");
// 						}
// 				}
		}

		public String toString() {
				return "Filter==> " + filter;
		}
		//NodeDeleteListener
		public void nodeDeleted(OncTreeNode deletedNode){
				System.out.println("Node deleted");
		}

		///////////////////////
		public class  SearchTextLabel extends JLabel 
				implements oncotcap.display.common.Droppable {
				private  DataFlavor nodeFlavors [] = { Droppable.searchText };
				
				public SearchTextLabel() {
						super("");
						setBackground(new Color(235,232,227));
						setBorder(new EmptyBorder(new Insets(0,0,0,0)));
						ImageIcon textIcon = 
								oncotcap.util.OncoTcapIcons.getImageIcon
								("text.jpg");
						setIcon(textIcon);
						// Enable drag on this label
						LabelTransferHandler th = new LabelTransferHandler("text");
						setTransferHandler(th);
						MouseInputListener ml = new MyMouseListener();
						addMouseListener(ml);
						addMouseMotionListener(ml);
				}
				
				public boolean dropOn(Object dropOnObject){
						System.out.println("Drop on");
						return false;
				}
				public DataFlavor [] getTransferDataFlavors()
				{
						//System.out.println("getTransferDataFlavors  - oo " + nodeFlavors);
						return(nodeFlavors);
				}
				public boolean isDataFlavorSupported( DataFlavor flavor)
				{
						if ( flavor.equals(Droppable.searchText ) )
								return true;
						return false;
				}
				public Object getTransferData(DataFlavor flavor) 
						throws UnsupportedFlavorException, IOException
				{
						//System.out.println("flavor getTransferData "+ flavor);
						if (isDataFlavorSupported(flavor))
								return(new oncotcap.datalayer.SearchText("cancer"));
						else
								throw(new UnsupportedFlavorException(flavor));
				}

				class MyMouseListener implements MouseInputListener {
						public void mouseEntered(MouseEvent e) {
								setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						}
						public void mouseExited(MouseEvent e)	{
								setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						}
						public void mouseDragged(MouseEvent e)
						{	
								JComponent c = (JComponent)e.getSource();
								TransferHandler handler = c.getTransferHandler();
								handler.exportAsDrag(c, e, 
																		 TransferHandler.COPY);
						}
						public void mouseMoved(MouseEvent e)
						{
						}
						public void mousePressed(MouseEvent e)
						{
						}
						public void mouseClicked(MouseEvent e)
						{
						}
						public void mouseReleased(MouseEvent e)
						{
						}
				}	
		}	
		class LabelTransferHandler extends TransferHandler {
				public LabelTransferHandler() {
						super("Text");
				}
				public LabelTransferHandler(String s) {
						super(s);
				}
				public void exportAsDrag(JComponent comp, InputEvent e, int action)
				{
						super.exportAsDrag(comp, e, action); 
				}
				public void exportToClipboard(JComponent comp, Clipboard clip, int action)
				{
						super.exportToClipboard(comp, clip, action);
				}
				
				
				public Transferable createTransferable(JComponent c)
				{
						//System.out.println("createtransferable");
						return (Transferable)c;
				}
				protected void exportDone(JComponent c, Transferable t, int action)
				{
				}
				public boolean canImport(JComponent c, DataFlavor[] flavors)
				{
						return false;
				}
				
		}

}
class OperatorPane extends JPanel
{

	Hashtable operatorBoxes = new Hashtable();
	
	OperatorPane()
	{
		init();
	}

	private void init()
	{
		setMaximumSize(new Dimension(150, 300));
//		setBorder(BorderFactory.createLineBorder(Color.blue, 3));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		Vector ops = TcapLogicalOperator.getDefinedOperators();
		Iterator it = ops.iterator();
		while(it.hasNext())
		{
			TcapLogicalOperator op = (TcapLogicalOperator) it.next();
			Box opBox = Box.createHorizontalBox();
			//opBox.setMaximumSize(new Dimension(Short.MAX_VALUE, 15));
			opBox.add(Box.createHorizontalStrut(3));
			// if the operator has an icon use it
			ImageIcon opIcon = 
					oncotcap.util.OncoTcapIcons.getImageIcon(op.getName() + ".jpg");
		if (opIcon != null) 
				opBox.add(new DragDropLabel(op, opIcon));
		else 
				opBox.add(new DragDropLabel(op));

		//opBox.add(Box.createHorizontalGlue());
			operatorBoxes.put(op, opBox);
			add(opBox);
		}
		
//add in some Keyword or StatementTemplate instances for testing purposes...
/*		add(new HorizontalLine(4));

//		Vector allTemplates = StatementTemplate.getAllStatementTemplates();
		Class sbClass = ReflectionHelper.classForName("oncotcap.datalayer.persistible.Keyword");
		Collection allTemplates = oncotcap.Oncotcap.getDataSource().find(sbClass);
		it = allTemplates.iterator();
		while(it.hasNext())
		{
			Object obj = it.next();
			if(obj instanceof Keyword)
				add(new DragDropLabel((Keyword) obj));
		}
*/
	}
	public void removeOperator(TcapLogicalOperator op)
	{
		Box rBox = (Box) operatorBoxes.get(op);

		if(rBox != null)
		{
			remove(rBox);
			operatorBoxes.remove(op);
			repaint();
		}
	}
	public class DragAdapter extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			JComponent c = (JComponent) e.getSource();
			c.setBackground(TcapColor.lightBlue);
			c.setOpaque(true);
			repaint();
			TransferHandler handler = c.getTransferHandler();
			handler.exportAsDrag(c, e, TransferHandler.COPY);
		}
	}
		//}



}

