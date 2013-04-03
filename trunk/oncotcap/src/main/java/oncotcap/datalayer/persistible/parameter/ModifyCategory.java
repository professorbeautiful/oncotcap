package oncotcap.datalayer.persistible.parameter;

import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;

public class ModifyCategory extends DeclareCategory
{
	public Keyword category;
	public DeclareCategory baseCategory;
	
	public ModifyCategory(oncotcap.util.GUID guid){
		super(guid);
	}
	public ModifyCategory()
	{
		this(true);
	}
	public ModifyCategory(DeclareCategory cat, boolean saveToDataSource)
	{
		super(saveToDataSource);
		copy(cat);
	}
	public ModifyCategory(boolean saveToDataSource)
	{
		super(saveToDataSource);
	}
	private void copy(DeclareCategory cat)
	{
		baseCategory = cat;
		setTopKeyword(cat.topKeyword);
		overridingStringValue = cat.overridingStringValue;
	}
	public Keyword getTopKeyword()
	{
		if(baseCategory != null)
		{
			return(baseCategory.getTopKeyword());
		}
		else
		{
			return(null);
		}
	}
	public Object clone()
	{
		return(clone(true));
	}
	public Parameter clone(boolean saveToDataSource)
	{
		ModifyCategory rVal = new ModifyCategory(this, saveToDataSource);
		rVal.setOriginalSibling(getOriginalSibling());
		rVal.setCategory(category);
		return(rVal);		
	}
	public Keyword getCategory()
	{
		return(category);
	}
	public void setCategory(Keyword word)
	{
		category = word;
		update();
	}

	public ParameterType getParameterType()
	{
		return(null);
	}

	public void setDisplayValue(Object val)
	{
			if(val instanceof Keyword) { 
					setCategory((Keyword) val);
			}
		else if(val instanceof String) {
				overridingStringValue = (String)val;
		}
		update(); //this method actually modifies data source then fireSaveEvent
	}
	public EditorPanel getEditorPanel()
	{
		return(new ModifyCategoryEditor());
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		EditorPanel ep = new ModifyCategoryEditor();
		ep.edit(this);
		return(ep);
	}
	public String getValue()
	{
		if(this.overridingStringValue != null && ! this.overridingStringValue.trim().equals(""))
			return(this.overridingStringValue);
		else if(category != null &&  ! category.toString().trim().equals(""))
			return(category.toString());
		else
			return(super.getValue());
	}
}
