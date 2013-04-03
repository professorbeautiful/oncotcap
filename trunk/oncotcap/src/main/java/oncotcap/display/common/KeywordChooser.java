package oncotcap.display.common;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import oncotcap.datalayer.persistible.Keyword;
import oncotcap.datalayer.persistible.OncFilter;
import oncotcap.datalayer.persistible.TcapLogicalOperator;
import oncotcap.display.browser.*;
import oncotcap.util.CollectionHelper;


public class KeywordChooser extends JPanel implements TreeSelectionListener
{

	private Keyword rootKeyword = null;
	private Keyword keyword = null;
	private Keyword endingKeyword = null;
	private JTextField keywordField;
	private JButton btnSet;
	private OntologyTree ot = null;
	
	public KeywordChooser()
	{
		init();
	}
	public KeywordChooser(Keyword k)
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
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		keywordField = new JTextField();
		keywordField.setEnabled(false);
		keywordField.setMinimumSize(new Dimension(150,25));
		keywordField.setPreferredSize(new Dimension(150,25));
		keywordField.setMaximumSize(new Dimension(150,25));
		keywordField.setDisabledTextColor(Color.black);
		btnSet = new JButton(". . .");
		btnSet.setMaximumSize(new Dimension(25,25));
		btnSet.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				pickKeyword();
			}
		});
		add(keywordField);
		add(btnSet);
		add(Box.createHorizontalGlue());
	}
	public void setKeyword(Keyword word)
	{
		keyword = word;
		if(word != null)
			keywordField.setText(word.toString());
	}
	public Keyword getKeyword()
	{
		return(keyword);
	}
	private void pickKeyword()
	{
			Hashtable keywords = null;
			if ( this.endingKeyword != null )
					keywords = getParentOnlyKeywordTree(this.endingKeyword);
 			else 
					keywords = getKeywordTree();
		TreeDialog td = new TreeDialog(keywords);
		addTreeSelectionListener(this);
		td.setVisible(true);
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
		KeywordHashtable keywordHashtable = new KeywordHashtable();
		int filterStatus = TreeDisplayModePanel.ROOT;
		if(rootKeyword == null)
				filterStatus = TreeDisplayModePanel.NONE;
		return(oncotcap.Oncotcap.getDataSource().getInstanceTree
					 ("Keyword", 
						CollectionHelper.makeVector("Keyword"),
						getFilter(rootKeyword),
						filterStatus));
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
