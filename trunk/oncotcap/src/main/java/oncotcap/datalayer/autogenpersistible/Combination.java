package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class Combination extends AutoGenPersistibleWithKeywords 
 {
	private DefaultPersistibleList agents;
	private String name;
	private DefaultPersistibleList sentencesAboutThis;


public Combination() {
init();
}


public Combination(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setAgents", DefaultPersistibleList.class);
	setMethodMap.put("agents", setter);
	getter = getGetter("getAgents");
	getMethodMap.put("agents", getter);
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
	setter = getSetter("setSentencesAboutThis", DefaultPersistibleList.class);
	setMethodMap.put("sentencesAboutThis", setter);
	getter = getGetter("getSentencesAboutThis");
	getMethodMap.put("sentencesAboutThis", getter);
}

	public DefaultPersistibleList getAgents(){
		return agents;
	}
	public String getName(){
		return name;
	}
	public DefaultPersistibleList getSentencesAboutThis(){
		return sentencesAboutThis;
	}
	public void setAgents(java.util.Collection  var ){
		if ( agents== null)
			agents = new DefaultPersistibleList();
		agents.set(var);
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
		return CombinationPanel.class;
	}
	public String getPrettyName()
	{
		return "Combination";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
