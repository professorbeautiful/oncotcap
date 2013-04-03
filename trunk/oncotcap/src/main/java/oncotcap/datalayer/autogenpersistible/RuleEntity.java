package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class RuleEntity extends AutoGenPersistibleWithKeywords 
 {
	private String sentenceText;


public RuleEntity() {
init();
}


public RuleEntity(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setSentenceText", String.class);
	setMethodMap.put("sentenceText", setter);
	getter = getGetter("getSentenceText");
	getMethodMap.put("sentenceText", getter);
}

	public String getSentenceText(){
		return sentenceText;
	}
	public void setSentenceText(String var ){
		sentenceText = var;
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return RuleEntityPanel.class;
	}
	public String getPrettyName()
	{
		return "RuleEntity";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
