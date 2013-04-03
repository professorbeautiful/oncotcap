package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.browser.*;

public class KeywordFilterEditorPanel extends EditorPanel
{
	private GenericTree keywordTree;
	private FilterEditorPanel filterTree;
	private KeywordFilter filter;
	
	public KeywordFilterEditorPanel()
	{
		init();
	}
	public KeywordFilterEditorPanel(KeywordFilter filter)
	{
		init();
		edit(filter);
	}
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(600,400);
		jf.getContentPane().add(new KeywordFilterEditorPanel(new KeywordFilter()));
		jf.setVisible(true);
	}
	private void init()
	{
		setLayout(new BorderLayout());
		JSplitPane splitPane = new JSplitPane();
		keywordTree = new GenericTree(getKeywordTree(), false);		
		keywordTree.setMinimumSize(new Dimension(200, Short.MAX_VALUE));
		filterTree = new FilterEditorPanel();
		filterTree.setMinimumSize(new Dimension(200, Short.MAX_VALUE));
		splitPane.setLeftComponent(keywordTree);
		splitPane.setRightComponent(filterTree);
		add(splitPane, BorderLayout.CENTER);
	}
	protected void removeOperator(TcapLogicalOperator op)
	{
		filterTree.removeOperator(op);
	}
	public Hashtable getKeywordTree() {

		KeywordHashtable keywordHashtable = new KeywordHashtable();

		if(filter == null || filter.getRootKeyword() == null)
			return(KeywordHashtable.getTreeInstances());
		else
			return(keywordHashtable.getTreeInstances(filter.getRootKeyword()));

	}

	public Object getValue()
	{
		return(filter);
	}
	public void save()
	{
		filterTree.save();
	}
	public void edit(Object obj)
	{
		if(obj instanceof KeywordFilter)
			edit((KeywordFilter) obj);
	}
	public void edit(KeywordFilter filter)
	{
		this.filter = filter;
		filterTree.edit(filter);
		keywordTree.updateTree(getKeywordTree());
	}
}