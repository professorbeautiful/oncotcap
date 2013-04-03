package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class BrowserNode extends AutoGenPersistibleWithKeywords 
 {
	private Integer versionNumber;


public BrowserNode() {
init();
}


public BrowserNode(oncotcap.util.GUID guid) {
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
		return BrowserNodePanel.class;
	}
	public String getPrettyName()
	{
		return "BrowserNode";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
