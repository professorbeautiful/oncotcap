package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class SettingSubmodel extends SubModel
 {
	private DefaultPersistibleList submodelGroupsIJoin;
	private DefaultPersistibleList encodingsInMe;
	private Integer versionNumber;
	private String name;


public SettingSubmodel() {
init();
}


public SettingSubmodel(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setSubmodelGroupsIJoin", DefaultPersistibleList.class);
	setMethodMap.put("submodelGroupsIJoin", setter);
	getter = getGetter("getSubmodelGroupsIJoin");
	getMethodMap.put("submodelGroupsIJoin", getter);
	setter = getSetter("setEncodingsInMe", DefaultPersistibleList.class);
	setMethodMap.put("encodingsInMe", setter);
	getter = getGetter("getEncodingsInMe");
	getMethodMap.put("encodingsInMe", getter);
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);
	setter = getSetter("setKeywords", DefaultPersistibleList.class);
	setMethodMap.put("keywords", setter);
	getter = getGetter("getKeywords");
	getMethodMap.put("keywords", getter);
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
}

	public DefaultPersistibleList getSubmodelGroupsIJoin(){
		return submodelGroupsIJoin;
	}
	public DefaultPersistibleList getEncodingsInMe(){
		return encodingsInMe;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public String getName(){
		return name;
	}
	public void setSubmodelGroupsIJoin(java.util.Collection  var ){
		if ( submodelGroupsIJoin== null)
			submodelGroupsIJoin = new DefaultPersistibleList();
		submodelGroupsIJoin.set(var);
	}

	public void setEncodingsInMe(java.util.Collection  var ){
		if ( encodingsInMe== null)
			encodingsInMe = new DefaultPersistibleList();
		encodingsInMe.set(var);
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setName(String var ){
		name = var;
	}

	public String toString() {
		return name;
	}
	public Class getPanelClass()
	{
		return SettingSubmodelPanel.class;
	}
	public String getPrettyName()
	{
		return "SettingSubmodel";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
