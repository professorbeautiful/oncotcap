package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class FullSourceProvider extends AutoGenPersistibleWithKeywords 
 {
	private String name;
	private String sourceLocatorMethod;


public FullSourceProvider() {
init();
}


public FullSourceProvider(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
	setter = getSetter("setSourceLocatorMethod", String.class);
	setMethodMap.put("sourceLocatorMethod", setter);
	getter = getGetter("getSourceLocatorMethod");
	getMethodMap.put("sourceLocatorMethod", getter);
}

	public String getName(){
		return name;
	}
	public String getSourceLocatorMethod(){
		return sourceLocatorMethod;
	}
	public void setName(String var ){
		name = var;
	}

	public void setSourceLocatorMethod(String var ){
		sourceLocatorMethod = var;
	}

	public String toString() {
		return name;
	}
	public Class getPanelClass()
	{
		return FullSourceProviderPanel.class;
	}
	public String getPrettyName()
	{
		return "FullSourceProvider";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
