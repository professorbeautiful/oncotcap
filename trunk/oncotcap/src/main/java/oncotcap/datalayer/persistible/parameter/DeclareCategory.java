package oncotcap.datalayer.persistible.parameter;

import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.*;

public class DeclareCategory extends AbstractIndividualParameter
{
	public Keyword topKeyword;
	public String overridingStringValue = null;
	public DeclareCategory()
	{
		this(true);
	}
	public DeclareCategory(boolean saveToDataSource)
	{
		super(saveToDataSource);
	}
	public DeclareCategory(oncotcap.util.GUID guid) {
		super(guid);
	}
	
	public Object clone()
	{
 		return(clone(true));
	}
	public Parameter clone(boolean saveToDataSource)
	{
		DeclareCategory rCat = new DeclareCategory(saveToDataSource);
		rCat.setOriginalSibling(getOriginalSibling());
		rCat.setTopKeyword(topKeyword);
		rCat.overridingStringValue = overridingStringValue;
		return(rCat);		
	}
	public String getOverridingStringValue() {
		return overridingStringValue;
	}
	public void setOverridingStringValue(String str) {

		overridingStringValue = str;
		update(); 
	}
	public void initSingleParams()
	{
		setDefaultName("Category");
		setSingleParameterID("DeclareCategory.Name");

		singleParameterList.add(this);
	}
	public String getValue()
	{
		if(overridingStringValue != null)
			return(overridingStringValue.trim());
		else if(topKeyword == null)
			return("");
		else
			return(topKeyword.getKeyword().trim());
	}
	public String getStringValue()
	{
		return(getValue());
	}
	public Keyword getTopKeyword()
	{
		return(topKeyword);
	}
	public void setTopKeyword(Keyword word)
	{
		topKeyword = word;
		if(word != null)
		{
			setDisplayValue(word.toString());
			update(); //this method actually modifies data source then fireSaveEvent
		}
	}
	public void setDisplayValue(Object val)
	{
		if(val instanceof Keyword)
			setTopKeyword((Keyword) val);
		else if(val instanceof String)
				overridingStringValue = (String) val;
		
		update(); //this method actually modifies data source then fireSaveEvent
	}
	public String getDisplayValue()
	{
		return(getValue());
	}
	public String getCodeValue()
	{
		return(StringHelper.javaNameKeepBQuotes(getValue()));
	}
	public ParameterType getParameterType()
	{
		return(ParameterType.CATEGORY);
	}
	public boolean check()
	{
		return(true);
	}
	public VariableType getType()
	{
		return(null);
	}
	public EditorPanel getEditorPanel()
	{
		return(new ParameterEditor());	
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		EditorPanel ep = new ParameterEditor();
		ep.edit(this);
		return(ep);
	}
	public EditorPanel getParameterEditorPanel()
	{
		return(new CategoryEditor());	
	}
	public EditorPanel getParameterEditorPanelWithInstance()
	{
		EditorPanel ep = new CategoryEditor();
		ep.edit(this);
		return(ep);
	}

/*	public void writeDeclarationSection(Writer write) throws IOException
	{
	}
	public void writeMethodSection(Writer write, StatementBundle sb) throws IOException {}
	public void writeDeclarationSection(Writer write, StatementBundle sb) throws IOException
	{
	}*/
	
	public boolean equalDeclaration(Object obj)
	{
		if(obj != null && obj instanceof DeclareCategory)
		{
			String compValue = ((DeclareCategory) obj).getValue();
			if(compValue != null && getValue() != null)
			{
				return(compValue.equals(getValue()));
			}
			else
				return(false);
		}
		else
			return(false);
	}

}
