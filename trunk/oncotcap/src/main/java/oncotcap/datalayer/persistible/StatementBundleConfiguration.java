package oncotcap.datalayer.persistible;

import javax.swing.ImageIcon;

import oncotcap.display.common.AbstractDroppable;
import oncotcap.util.*;
import javax.swing.tree.TreePath;

public class StatementBundleConfiguration extends AbstractDroppable
{
	private StatementBundle statementBundle;
	private boolean visible = false;
	private boolean multiple = false;
	private boolean required = false;
	
	private TreePath selectionPath;
	private int treeRow;
	private int sortKey = 0;
	private	ImageIcon icon =	
		oncotcap.util.OncoTcapIcons.getImageIcon("statementbundle.jpg");
	
	//null constructor needed only by DataAccess stuff
	public StatementBundleConfiguration(oncotcap.util.GUID guid){
		super(guid);
	}
	public StatementBundleConfiguration(){}
	public StatementBundleConfiguration(StatementBundle sb)
	{
		setStatementBundle(sb);
	}
	public void setStatementBundle(StatementBundle sb)
	{
		statementBundle = sb;
		update();
	}
	public StatementBundle getStatementBundle()
	{
		if(statementBundle != null)
			statementBundle.setSortKey(sortKey);
		return(statementBundle);
	}
	public void setVisibility(boolean visibility)
	{
		visible = visibility;
		update();
	}
	public boolean isVisible()
	{
		return(visible);
	}
	public void setAllowMultiples(boolean multiples)
	{
		multiple = multiples;
		update();
	}
	public boolean areMultiplesAllowed()
	{
		return(multiple);
	}

	public String toString()
	{
		if(statementBundle == null)
			return("Empty Statement Bundle Configuration");
		else
			return(StringHelper.htmlNoParagraphs(statementBundle.toString()));
	}
	public void setSelectionPath(TreePath path)
	{
		selectionPath = path;
	}
	public TreePath getSelectionPath()
	{
		return(selectionPath);
	}
	public int getTreeRow()
	{
		return(treeRow);
	}
	public void setTreeRow(int row)
	{
		treeRow = row;
	}
	public void setRequired(boolean req)
	{
		required = req;
		update();
	}
	public boolean isRequired()
	{
		return(required);
	}
	public int getSortKey()
	{
		return(sortKey);
	}
	public void setSortKey(int key)
	{
		sortKey = key;
	}
	public ImageIcon getIcon() {
		return icon;
	}
}