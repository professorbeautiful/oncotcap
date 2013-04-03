package oncotcap.datalayer.persistible;

import oncotcap.Oncotcap;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.GUID;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.parameter.*;

public class EnumSubsetFilter extends KeywordFilter
{
		//Characteristic Constraints
		public static final TcapString rootFilterObj = 
				(TcapString)Oncotcap.getDataSource().find
				(new GUID("888e669800000000000000fc3b655f5d"));
	public EnumSubsetFilter(oncotcap.util.GUID guid){
		super(guid);
	}
	public EnumSubsetFilter() {
	}

	public EnumSubsetFilter(Keyword rootKeyword)
	{
		super(rootKeyword);
		init();
	}

	private void init()
	{
		rootNode = new OncTreeNode(rootFilterObj);
		rootNode.addChild(new OncTreeNode(TcapLogicalOperator.OR));
	}
	public EditorPanel getEditorPanel()
	{
		return(new EnumSubsetFilterEditorPanel());
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		return(new EnumSubsetFilterEditorPanel(this));
	}
}
