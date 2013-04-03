package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class Biorelationship extends AutoGenPersistibleWithKeywords 
 {
	private String biorelationshipType;
	private String name;
	private DefaultPersistibleList sentencesAboutThis;


public Biorelationship() {
init();
}


public Biorelationship(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setBiorelationshipType", String.class);
	setMethodMap.put("biorelationshipType", setter);
	getter = getGetter("getBiorelationshipType");
	getMethodMap.put("biorelationshipType", getter);
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
	setter = getSetter("setSentencesAboutThis", DefaultPersistibleList.class);
	setMethodMap.put("sentencesAboutThis", setter);
	getter = getGetter("getSentencesAboutThis");
	getMethodMap.put("sentencesAboutThis", getter);
}

	public String getBiorelationshipType(){
		return biorelationshipType;
	}
	public String getName(){
		return name;
	}
	public DefaultPersistibleList getSentencesAboutThis(){
		return sentencesAboutThis;
	}
	public void setBiorelationshipType(String var ){
		biorelationshipType = var;
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
		return BiorelationshipPanel.class;
	}
	public String getPrettyName()
	{
		return "Biorelationship";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
