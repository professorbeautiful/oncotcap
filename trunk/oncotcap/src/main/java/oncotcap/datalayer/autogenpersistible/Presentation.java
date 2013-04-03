package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class Presentation extends InformationSource
 {
	private Integer versionNumber;
	private DefaultPersistibleList quotesInMe;


public Presentation() {
init();
}


public Presentation(oncotcap.util.GUID guid) {
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
	setter = getSetter("setQuotesInMe", DefaultPersistibleList.class);
	setMethodMap.put("quotesInMe", setter);
	getter = getGetter("getQuotesInMe");
	getMethodMap.put("quotesInMe", getter);
}

	public Integer getVersionNumber(){
		return versionNumber;
	}
	public DefaultPersistibleList getQuotesInMe(){
		return quotesInMe;
	}
	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setQuotesInMe(java.util.Collection  var ){
		if ( quotesInMe== null)
			quotesInMe = new DefaultPersistibleList();
		quotesInMe.set(var);
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return PresentationPanel.class;
	}
	public String getPrettyName()
	{
		return "Presentation";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
