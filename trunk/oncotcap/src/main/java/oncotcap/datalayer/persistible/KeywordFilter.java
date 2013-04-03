package oncotcap.datalayer.persistible;

import oncotcap.Oncotcap;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.GUID;
import oncotcap.datalayer.*;

public class KeywordFilter extends OncFilter
{
	public final static Keyword CHARACTERISTIC_ROOT = (Keyword) Oncotcap.getDataSource().find(new GUID("888e66a300000000000000f6b4be01fe"));

	private Keyword rootKeyword = null;
	
	public KeywordFilter(oncotcap.util.GUID guid){
		super(guid);
		keywordsOnly = true;
	}
	
	public KeywordFilter() {

		keywordsOnly = true;
	}
	public KeywordFilter(Keyword rootKeyword)
	{
		keywordsOnly = true;
		this.rootKeyword = rootKeyword;
	}
	public static void main(String [] args)
	{
		KeywordFilter f = new KeywordFilter();
		Keyword t = new Keyword("TEST KEYWORD");
		f.setRootKeyword(t);
		f.update();
		oncotcap.Oncotcap.getDataSource().commit();
	}
	public Object clone()
	{
		KeywordFilter cFilter = new KeywordFilter();
		cFilter.rootNode = new OncTreeNode(rootNode.getPersistibleObject());
		cFilter.rootKeyword = rootKeyword;
		cFilter.keywordsOnly = keywordsOnly;

		return(cFilter);
	}
	public EditorPanel getEditorPanel()
	{
		return(new KeywordFilterEditorPanel());
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		return(new KeywordFilterEditorPanel(this));
	}
	public Keyword getRootKeyword()
	{
		return(rootKeyword);
	}
	public void setRootKeyword(Keyword node)
	{
		rootKeyword = node;
	}
}