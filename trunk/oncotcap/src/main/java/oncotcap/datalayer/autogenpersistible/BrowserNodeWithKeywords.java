package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class BrowserNodeWithKeywords extends AutoGenPersistibleWithKeywords 
 {
	private Integer versionNumber;


public BrowserNodeWithKeywords() {
init();
}


public BrowserNodeWithKeywords(oncotcap.util.GUID guid) {
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
}

	public Integer getVersionNumber(){
		return versionNumber;
	}
	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return BrowserNodeWithKeywordsPanel.class;
	}
	public String getPrettyName()
	{
		return "BrowserNodeWithKeywords";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
