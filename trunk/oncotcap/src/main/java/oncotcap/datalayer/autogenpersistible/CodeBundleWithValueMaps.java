package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class CodeBundleWithValueMaps extends AutoGenPersistibleWithKeywords 
 {
	private CodeBundle codeBundle;
	private DefaultPersistibleList valueMap;


public CodeBundleWithValueMaps() {
init();
}


public CodeBundleWithValueMaps(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setCodeBundle", CodeBundle.class);
	setMethodMap.put("codeBundle", setter);
	getter = getGetter("getCodeBundle");
	getMethodMap.put("codeBundle", getter);
	setter = getSetter("setValueMap", DefaultPersistibleList.class);
	setMethodMap.put("valueMap", setter);
	getter = getGetter("getValueMap");
	getMethodMap.put("valueMap", getter);
}

	public CodeBundle getCodeBundle(){
		return codeBundle;
	}
	public DefaultPersistibleList getValueMap(){
		return valueMap;
	}
	public void setCodeBundle(CodeBundle var ){
		codeBundle = var;
	}

	public void setValueMap(java.util.Collection  var ){
		if ( valueMap== null)
			valueMap = new DefaultPersistibleList();
		valueMap.set(var);
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return CodeBundleWithValueMapsPanel.class;
	}
	public String getPrettyName()
	{
		return "CodeBundleWithValueMaps";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
