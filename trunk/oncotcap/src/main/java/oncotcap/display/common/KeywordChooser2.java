package oncotcap.display.common;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;

import oncotcap.display.browser.TreeDisplayModePanel;
import oncotcap.display.browser.TreePanelEvent;
import oncotcap.display.browser.TreePanelListener;
import oncotcap.datalayer.persistible.Keyword;
import oncotcap.datalayer.persistible.OncFilter;
import oncotcap.datalayer.persistible.TcapLogicalOperator;
import oncotcap.display.browser.*;
import oncotcap.display.common.KeywordChooser.TreeDialog;
import oncotcap.util.CollectionHelper;


public class KeywordChooser2 extends JPanel implements TreeSelectionListener {

	private Keyword rootKeyword = null;
	private Keyword keyword = null;
	private Keyword endingKeyword = null;
	private JTextField keywordField;
	private JButton btnSet;
	private OntologyTree ot = null;
	
	public KeywordChooser2()
	{
		init();
	}
	public KeywordChooser2(Keyword k)
	{
		this.endingKeyword = k;
		init();
	}
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(300,300);
		jf.getContentPane().add(new KeywordChooser());
		jf.setVisible(true);
	}
	private void init()
	{
			System.out.println("ROOT KEYWORD " + rootKeyword);
		setLayout(new BorderLayout());
		keywordField = new JTextField();
		keywordField.setEnabled(false);
		keywordField.setMinimumSize(new Dimension(125,25));
		//keywordField.setPreferredSize(new Dimension(150,25));
		//keywordField.setMaximumSize(new Dimension(150,25));
		keywordField.setDisabledTextColor(Color.black);

		ot = new OntologyTree();
		ot.collapseController();
		ot.setName("Keyword Chooser Ontology Tree");
		ot.getOntologyButtonPanel().setRoot(OntologyMap.K);
		ot.getOntologyButtonPanel().setLeaves
				(CollectionHelper.makeVector(new Integer(OntologyMap.K)));
		ot.getTree().updateTree(getKeywordTree());
		addTreeSelectionListener(this);
		OncBrowser.addOntologyTree(ot);
		
		add(keywordField, BorderLayout.NORTH);
		add(ot, BorderLayout.CENTER);
	}

	public void setKeyword(Keyword word)
	{
		keyword = word;
		if(word != null) {
			keywordField.setText(word.toString());
			rootKeyword = word;
			// TODO: Set the keyword selected in the tree
		}
	}
	public Keyword getKeyword()
	{
		return(keyword);
	}
	public void setRootKeyword(Keyword root)
	{
		rootKeyword = root;
	}
	public Keyword getRootKeyword()
	{
		return(rootKeyword);
	}
	public void setEndingKeyword(Keyword ending)
	{
		endingKeyword = ending;
	}
	public Keyword getEndingKeyword()
	{
		return(endingKeyword);
	}
	private Hashtable getParentOnlyKeywordTree(Keyword childKeyword)
	{
		Hashtable keywordHashtable =
				oncotcap.Oncotcap.getDataSource().getParentTree
				("Keyword",
				 CollectionHelper.makeVector("Keyword"),
				 CollectionHelper.makeVector(childKeyword),
				 TreeDisplayModePanel.ROOT );
		return(keywordHashtable);
	}
	private Hashtable getKeywordTree()
	{
		int filterStatus = TreeDisplayModePanel.ROOT;
		if ( this.endingKeyword != null )
			return getParentOnlyKeywordTree(this.endingKeyword);
		else {
			if(rootKeyword == null)
				filterStatus = TreeDisplayModePanel.NONE;
			return(oncotcap.Oncotcap.getDataSource().getInstanceTree
					("Keyword", 
							CollectionHelper.makeVector("Keyword"),
							getFilter(rootKeyword),
							filterStatus));
		}
	}

		public OncFilter getFilter(Keyword keywordConstraint) {
				// Build a filter with the desired keyword as the only node
				OncFilter filter = null;
				if ( keywordConstraint != null ) {
						// Create a filter
						filter = new OncFilter();	
						OncTreeNode rootNode = filter.getRootNode();
						OncTreeNode orNode = new OncTreeNode(TcapLogicalOperator.OR);
					  OncTreeNode keywordNode = new OncTreeNode(keywordConstraint);
						orNode.add(keywordNode);
						rootNode.add(orNode);
				}
				return filter;
		}

		public void addTreeSelectionListener(TreeSelectionListener tsl) {
// 				System.out.println("ot " + ot + " get tree " + ot.getTree());
				ot.getTree().addTreeSelectionListener(tsl);
		}

   public void valueChanged(TreeSelectionEvent e) {
			 JTree tree = (JTree)e.getSource();
			System.out.println("changed " + e);
			if ( e.getSource() instanceof Keyword)
					setKeyword((Keyword)e.getSource() );

	// 		 System.out.println("valueChanged " 
// 													+ tree.getLastSelectedPathComponent());
			 GenericTreeNode obj = (GenericTreeNode)tree.getLastSelectedPathComponent();
			 if ( obj != null ) 
					 setKeyword((Keyword)obj.getUserObject());
	 }

	class TreeDialog extends JFrame
	{
		private JOptionPane optionPane;
		final String btnString1 = "Done";
		final String btnString2 = "Cancel";
		Object[] options = {btnString1, btnString2};
		
		public TreeDialog(Hashtable treeData)
		{
			super();
			init(treeData);
		}
		private void init(Hashtable treeData)
		{
				ot = new OntologyTree();
				ot.collapseController();
				ot.setName("Keyword Chooser Ontology Tree");
				ot.getOntologyButtonPanel().setRoot(OntologyMap.K);
				ot.getOntologyButtonPanel().setLeaves
						(CollectionHelper.makeVector(new Integer(OntologyMap.K)));
				ot.getTree().updateTree(treeData);
				OncBrowser.addOntologyTree(ot);
				
				setContentPane(ot);
				pack();
				
		}
	}
}
