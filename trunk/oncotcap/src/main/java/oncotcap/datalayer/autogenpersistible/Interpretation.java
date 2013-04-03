package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class Interpretation extends AutoGenPersistibleWithKeywords 
 {
	protected DefaultPersistibleList encodingsImplementingMe;
	protected Integer versionNumber;
	protected DefaultPersistibleList sourceNuggets;
	protected String sentenceText;
public ImageIcon icon =	
	oncotcap.util.OncoTcapIcons.getImageIcon("interpretation.jpg");


public Interpretation() {
init();
}


public Interpretation(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
protected void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setKeywords", DefaultPersistibleList.class);
	setMethodMap.put("keywords", setter);
	getter = getGetter("getKeywords");
	getMethodMap.put("keywords", getter);
	setter = getSetter("setEncodingsImplementingMe", DefaultPersistibleList.class);
	setMethodMap.put("encodingsImplementingMe", setter);
	getter = getGetter("getEncodingsImplementingMe");
	getMethodMap.put("encodingsImplementingMe", getter);
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);
	setter = getSetter("setSourceNuggets", DefaultPersistibleList.class);
	setMethodMap.put("sourceNuggets", setter);
	getter = getGetter("getSourceNuggets");
	getMethodMap.put("sourceNuggets", getter);
	setter = getSetter("setSentenceText", String.class);
	setMethodMap.put("sentenceText", setter);
	getter = getGetter("getSentenceText");
	getMethodMap.put("sentenceText", getter);
}

	public DefaultPersistibleList getEncodingsImplementingMe(){
		return encodingsImplementingMe;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public DefaultPersistibleList getSourceNuggets(){
		return sourceNuggets;
	}
	public String getSentenceText(){
		return sentenceText;
	}
	public void setEncodingsImplementingMe(java.util.Collection  var ){
		if ( encodingsImplementingMe== null)
			encodingsImplementingMe = new DefaultPersistibleList();
		encodingsImplementingMe.set(var);
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setSourceNuggets(java.util.Collection  var ){
		if ( sourceNuggets== null)
			sourceNuggets = new DefaultPersistibleList();
		sourceNuggets.set(var);
	}

	public void setSentenceText(String var ){
		sentenceText = var;
	}

	public String toString() {
		return sentenceText;
	}
	public Class getPanelClass()
	{
		return InterpretationPanel.class;
	}
	public String getPrettyName()
	{
		return "Interpretation";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
