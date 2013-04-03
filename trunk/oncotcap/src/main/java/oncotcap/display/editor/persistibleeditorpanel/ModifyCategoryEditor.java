package oncotcap.display.editor.persistibleeditorpanel;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.*;
import oncotcap.display.browser.*;
import oncotcap.display.common.OncTreeNode;
import oncotcap.util.CollectionHelper;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.*;

public class ModifyCategoryEditor extends EditorPanel implements SaveListener
{
	private Keyword savedTopKeyword;
	GenericTree keywordTree = null;
	JScrollPane keywordTreeSP;
	ModifyCategory modCat = null;
	JTextField txtName;
	MCatTreeListener treeListener;

	public ModifyCategoryEditor()
	{
		init();
	}
	private void init()
	{
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(200,425));
		keywordTreeSP = new JScrollPane();
		add(keywordTreeSP, BorderLayout.CENTER);

		Box nameBox = Box.createHorizontalBox();
		nameBox.add(Box.createHorizontalStrut(5));
		nameBox.add(new JLabel("Name: "));
		txtName = new JTextField();
		txtName.setMaximumSize(new Dimension(150, 25));
		nameBox.add(txtName);
		add(nameBox, BorderLayout.NORTH);

		treeListener = new MCatTreeListener();
	}
	public void edit(Object obj)
	{
		if(obj instanceof ModifyCategory)
			edit((ModifyCategory) obj);
	}
	public void edit(ModifyCategory mod)
	{
		if(modCat != null && modCat.baseCategory != null)
			modCat.baseCategory.removeSaveListener(this);
		modCat = mod;
		savedTopKeyword = modCat.getTopKeyword();
		modCat.baseCategory.addSaveListener(this);
		txtName.setText(modCat.getValue());
		keywordTree = new GenericTree(getKeywordTree(), false);
		keywordTree.addTreeSelectionListener(treeListener);
		keywordTreeSP.setViewportView(keywordTree);
		keywordTreeSP.setPreferredSize(getPreferredSize());
		repaint();
	}
	public void objectSaved(SaveEvent e)
	{
		if(modCat != null && modCat.getTopKeyword() != null)
		{
			if(savedTopKeyword != null && 
				 !savedTopKeyword.equals(modCat.getTopKeyword()))
			{
				savedTopKeyword = modCat.getTopKeyword();
				if(savedTopKeyword != null)
				{
					keywordTree.updateTree(getKeywordTree());
				}
			}
		}
	}
	public void objectDeleted(SaveEvent e)
	{
		
	}
	public void save()
	{
		modCat.setDisplayValue(txtName.getText());
		if(keywordTree != null && keywordTree.getSelectionPath() != null)
		{
			TreePath path = keywordTree.getSelectionPath();
			Object selectedObj = path.getLastPathComponent();
			if(selectedObj != null && modCat != null)
			{
				if(selectedObj instanceof Keyword)
					modCat.setCategory((Keyword) selectedObj);
				else if(selectedObj instanceof DefaultMutableTreeNode)
				{
					Object userObj = ((DefaultMutableTreeNode) selectedObj).getUserObject();
					if(userObj != null && userObj instanceof Keyword)
						modCat.setCategory((Keyword) userObj);
				}
			}
		}
		
		// If the user has typed in text unselect keyword category
		Keyword cat = modCat.getCategory();
		String txt = txtName.getText();
		if ( (cat == null) || 
				 (cat != null 
					&& !txt.equals(cat.toString())) ) {
				keywordTree.clearSelection();
				modCat.setCategory(null);
		}

	}
	public Object getValue()
	{
		return(modCat);
	}

	private Hashtable getKeywordTree() {
			KeywordHashtable keywordHashtable = new KeywordHashtable();
			if(modCat == null || modCat.getTopKeyword() == null) {
					return(oncotcap.Oncotcap.getDataSource().getInstanceTree
								 ("Keyword", 
									CollectionHelper.makeVector("Keyword"),
									(OncFilter)null,
									TreeDisplayModePanel.NONE));
			}
			else {
					return(oncotcap.Oncotcap.getDataSource().getInstanceTree
								 ("Keyword", 
									CollectionHelper.makeVector("Keyword"),
									getFilter(modCat.getTopKeyword()),
									TreeDisplayModePanel.ROOT));
			}
	}	
		private OncFilter getFilter(Keyword keywordConstraint) {
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
	private class MCatTreeListener implements TreeSelectionListener
	{
		public void valueChanged(TreeSelectionEvent e)
		{
			if(keywordTree != null && keywordTree.getSelectionPath() != null)
			{
				TreePath path = keywordTree.getSelectionPath();
				Object selectedObj = path.getLastPathComponent();
				if(selectedObj != null)
				{
					if(selectedObj instanceof Keyword)
						txtName.setText(selectedObj.toString());
					else if(selectedObj instanceof DefaultMutableTreeNode)
					{
						Object userObj = ((DefaultMutableTreeNode) selectedObj).getUserObject();
						if(userObj != null && userObj instanceof Keyword)
							txtName.setText(userObj.toString());
					}
				}
			}
		}
	}
	
}
