package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class Agent extends AutoGenPersistibleWithKeywords 
 {
	private DefaultPersistibleList toxicities;
	private String name;
	private DefaultPersistibleList sentencesAboutThis;


public Agent() {
init();
}


public Agent(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setToxicities", DefaultPersistibleList.class);
	setMethodMap.put("toxicities", setter);
	getter = getGetter("getToxicities");
	getMethodMap.put("toxicities", getter);
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
	setter = getSetter("setSentencesAboutThis", DefaultPersistibleList.class);
	setMethodMap.put("sentencesAboutThis", setter);
	getter = getGetter("getSentencesAboutThis");
	getMethodMap.put("sentencesAboutThis", getter);
}

	public DefaultPersistibleList getToxicities(){
		return toxicities;
	}
	public String getName(){
		return name;
	}
	public DefaultPersistibleList getSentencesAboutThis(){
		return sentencesAboutThis;
	}
	public void setToxicities(java.util.Collection  var ){
		if ( toxicities== null)
			toxicities = new DefaultPersistibleList();
		toxicities.set(var);
	}

	public void setName(String var ){
		name = var;
	}

	public void setSentencesAboutThis(java.util.Collection  var ){
		if ( sentencesAboutThis== null)
			sentencesAboutThis = new DefaultPersistibleList();
		sentencesAboutThis.set(var);
	}

	public String toString() {
		return name;
	}
	public Class getPanelClass()
	{
		return AgentPanel.class;
	}
	public String getPrettyName()
	{
		return "Agent";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
