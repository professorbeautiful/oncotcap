package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class Sentence extends AutoGenPersistibleWithKeywords 
 {
	private String sentenceText;


public Sentence() {
init();
}


public Sentence(oncotcap.util.GUID guid) {
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
		return sentenceText;
	}
	public Class getPanelClass()
	{
		return SentencePanel.class;
	}
	public String getPrettyName()
	{
		return "Sentence";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
