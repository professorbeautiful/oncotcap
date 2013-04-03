package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class BiophysSubModelGroup extends SubModelGroup
 {
	private Integer versionNumber;
	private DefaultPersistibleList modelControllersUsingMe;
	private String name;
	private DefaultPersistibleList submodelsInGroup;


public BiophysSubModelGroup() {
init();
}


public BiophysSubModelGroup(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);
	setter = getSetter("setKeywords", DefaultPersistibleList.class);
	setMethodMap.put("keywords", setter);
	getter = getGetter("getKeywords");
	getMethodMap.put("keywords", getter);
	setter = getSetter("setModelControllersUsingMe", DefaultPersistibleList.class);
	setMethodMap.put("modelControllersUsingMe", setter);
	getter = getGetter("getModelControllersUsingMe");
	getMethodMap.put("modelControllersUsingMe", getter);
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
	setter = getSetter("setSubmodelsInGroup", DefaultPersistibleList.class);
	setMethodMap.put("submodelsInGroup", setter);
	getter = getGetter("getSubmodelsInGroup");
	getMethodMap.put("submodelsInGroup", getter);
}

	public Integer getVersionNumber(){
		return versionNumber;
	}
	public DefaultPersistibleList getModelControllersUsingMe(){
		return modelControllersUsingMe;
	}
	public String getName(){
		return name;
	}
	public DefaultPersistibleList getSubmodelsInGroup(){
		return submodelsInGroup;
	}
	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setModelControllersUsingMe(java.util.Collection  var ){
		if ( modelControllersUsingMe== null)
			modelControllersUsingMe = new DefaultPersistibleList();
		modelControllersUsingMe.set(var);
	}

	public void setName(String var ){
		name = var;
	}

	public void setSubmodelsInGroup(java.util.Collection  var ){
		if ( submodelsInGroup== null)
			submodelsInGroup = new DefaultPersistibleList();
		submodelsInGroup.set(var);
	}

	public String toString() {
		return name;
	}
	public Class getPanelClass()
	{
		return BiophysSubModelGroupPanel.class;
	}
	public String getPrettyName()
	{
		return "BiophysSubModelGroup";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
