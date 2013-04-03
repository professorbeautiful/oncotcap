package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class OntologyCategory extends AutoGenPersistibleWithKeywords 
 {
	private Integer minimumValue;
	private String name;
	private Integer maximumValue;


public OntologyCategory() {
init();
}


public OntologyCategory(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setMinimumValue", Integer.class);
	setMethodMap.put("minimumValue", setter);
	getter = getGetter("getMinimumValue");
	getMethodMap.put("minimumValue", getter);
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
	setter = getSetter("setMaximumValue", Integer.class);
	setMethodMap.put("maximumValue", setter);
	getter = getGetter("getMaximumValue");
	getMethodMap.put("maximumValue", getter);
}

	public Integer getMinimumValue(){
		return minimumValue;
	}
	public String getName(){
		return name;
	}
	public Integer getMaximumValue(){
		return maximumValue;
	}
	public void setMinimumValue(Integer var ){
		minimumValue = var;
	}

	public void setName(String var ){
		name = var;
	}

	public void setMaximumValue(Integer var ){
		maximumValue = var;
	}

	public String toString() {
		return name;
	}
	public Class getPanelClass()
	{
		return OntologyCategoryPanel.class;
	}
	public String getPrettyName()
	{
		return "OntologyCategory";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
