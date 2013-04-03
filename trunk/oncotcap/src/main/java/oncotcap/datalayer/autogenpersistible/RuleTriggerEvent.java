package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class RuleTriggerEvent extends AutoGenPersistibleWithKeywords 
 {
	private String description;
	private String name;
	private String sentenceText;


public RuleTriggerEvent() {
init();
}


public RuleTriggerEvent(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setDescription", String.class);
	setMethodMap.put("description", setter);
	getter = getGetter("getDescription");
	getMethodMap.put("description", getter);
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
	setter = getSetter("setSentenceText", String.class);
	setMethodMap.put("sentenceText", setter);
	getter = getGetter("getSentenceText");
	getMethodMap.put("sentenceText", getter);
}

	public String getDescription(){
		return description;
	}
	public String getName(){
		return name;
	}
	public String getSentenceText(){
		return sentenceText;
	}
	public void setDescription(String var ){
		description = var;
	}

	public void setName(String var ){
		name = var;
	}

	public void setSentenceText(String var ){
		sentenceText = var;
	}

	public String toString() {
		return name;
	}
	public Class getPanelClass()
	{
		return RuleTriggerEventPanel.class;
	}
	public String getPrettyName()
	{
		return "RuleTriggerEvent";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
