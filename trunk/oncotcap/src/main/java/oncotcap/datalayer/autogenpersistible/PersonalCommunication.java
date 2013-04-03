package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class PersonalCommunication extends InformationSource
 {
	private Person communicator;
	private Integer versionNumber;
	private DefaultPersistibleList quotesInMe;
	private String recipient;


public PersonalCommunication() {
init();
}


public PersonalCommunication(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setCommunicator", Person.class);
	setMethodMap.put("communicator", setter);
	getter = getGetter("getCommunicator");
	getMethodMap.put("communicator", getter);
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
	setter = getSetter("setRecipient", String.class);
	setMethodMap.put("recipient", setter);
	getter = getGetter("getRecipient");
	getMethodMap.put("recipient", getter);
}

	public Person getCommunicator(){
		return communicator;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public DefaultPersistibleList getQuotesInMe(){
		return quotesInMe;
	}
	public String getRecipient(){
		return recipient;
	}
	public void setCommunicator(Person var ){
		communicator = var;
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setQuotesInMe(java.util.Collection  var ){
		if ( quotesInMe== null)
			quotesInMe = new DefaultPersistibleList();
		quotesInMe.set(var);
	}

	public void setRecipient(String var ){
		recipient = var;
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return PersonalCommunicationPanel.class;
	}
	public String getPrettyName()
	{
		return "PersonalCommunication";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
