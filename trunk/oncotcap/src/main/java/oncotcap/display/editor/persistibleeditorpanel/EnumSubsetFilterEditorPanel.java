package oncotcap.display.editor.persistibleeditorpanel;

import oncotcap.datalayer.persistible.*;
import oncotcap.util.*;

public class EnumSubsetFilterEditorPanel extends KeywordFilterEditorPanel
{
	public EnumSubsetFilterEditorPanel()
	{
		super();
		init();
	}
	public EnumSubsetFilterEditorPanel(KeywordFilter filter)
	{
		super();
		init();
		edit(filter);
	}
	private void init()
	{
		removeOperator(TcapLogicalOperator.NOT);
		removeOperator(TcapLogicalOperator.AND);
	}
}