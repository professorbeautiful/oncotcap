package oncotcap.datalayer.persistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;
import java.util.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class Encoding extends AutoGenPersistibleWithKeywords 
 {
	private Integer versionNumber;
	private String sentenceText;
	private DefaultPersistibleList interpretationsIEncode;
	private DefaultPersistibleList statementBundlesImplementingMe;
	private DefaultPersistibleList submodelsIJoin;
		 private String modelType;
public ImageIcon icon =	
	oncotcap.util.OncoTcapIcons.getImageIcon("encoding.jpg");


public Encoding() {
init();
}


public Encoding(oncotcap.util.GUID guid) {
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
	setter = getSetter("setSentenceText", String.class);
	setMethodMap.put("sentenceText", setter);
	getter = getGetter("getSentenceText");
	getMethodMap.put("sentenceText", getter);
	setter = getSetter("setInterpretationsIEncode", DefaultPersistibleList.class);
	setMethodMap.put("interpretationsIEncode", setter);
	getter = getGetter("getInterpretationsIEncode");
	getMethodMap.put("interpretationsIEncode", getter);
	setter = getSetter("setKeywords", DefaultPersistibleList.class);
	setMethodMap.put("keywords", setter);
	getter = getGetter("getKeywords");
	getMethodMap.put("keywords", getter);
	setter = getSetter("setStatementBundlesImplementingMe", DefaultPersistibleList.class);
	setMethodMap.put("statementBundlesImplementingMe", setter);
	getter = getGetter("getStatementBundlesImplementingMe");
	getMethodMap.put("statementBundlesImplementingMe", getter);
	setter = getSetter("setSubmodelsIJoin", DefaultPersistibleList.class);
	setMethodMap.put("submodelsIJoin", setter);
	getter = getGetter("getSubmodelsIJoin");
	getMethodMap.put("submodelsIJoin", getter);
	setter = getSetter("setModelType", String.class);
	setMethodMap.put("modelType", setter);
	getter = getGetter("getModelType");
	getMethodMap.put("modelType", getter);

}

	public Integer getVersionNumber(){
		return versionNumber;
	}
	public String getSentenceText(){
		return sentenceText;
	}
	public String getModelType(){
		return modelType;
	}
	public DefaultPersistibleList getInterpretationsIEncode(){
		return interpretationsIEncode;
	}
	public DefaultPersistibleList getStatementBundlesImplementingMe(){
		return statementBundlesImplementingMe;
	}
	public DefaultPersistibleList getSubmodelsIJoin(){
		return submodelsIJoin;
	}
	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setSentenceText(String var ){
		sentenceText = var;
	}
	public void setModelType(String var ){
		modelType = var;
	}

	public void setInterpretationsIEncode(java.util.Collection  var ){
		if ( interpretationsIEncode== null)
			interpretationsIEncode = new DefaultPersistibleList();
		interpretationsIEncode.set(var);
	}

	public void setStatementBundlesImplementingMe(java.util.Collection  var ){
		if ( statementBundlesImplementingMe== null)
			statementBundlesImplementingMe = new DefaultPersistibleList();
		statementBundlesImplementingMe.set(var);
	}

	public void setSubmodelsIJoin(java.util.Collection  var ){
		if ( submodelsIJoin== null)
			submodelsIJoin = new DefaultPersistibleList();
		submodelsIJoin.set(var);
	}

	public String toString() {
		return sentenceText;
	}
	public Class getPanelClass()
	{
		return EncodingPanel.class;
	}
	public String getPrettyName()
	{
		return "Edict";
	}
	public String getClassName()
	{
		return getPrettyName();
	}
	
	public ImageIcon getIcon()
	{
		return icon;
	}
	public Collection getStatementBundles()
	{
		return(statementBundlesImplementingMe);
	}
}
