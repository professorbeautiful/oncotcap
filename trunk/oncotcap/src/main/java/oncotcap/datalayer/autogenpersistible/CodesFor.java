package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class CodesFor extends AutoGenPersistibleWithKeywords 
 {
	private String biorelationshipType;
	private Gene firstObject;
	private Molecule secondObject;
	private String name;
	private DefaultPersistibleList sentencesAboutThis;


public CodesFor() {
init();
}


public CodesFor(oncotcap.util.GUID guid) {
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
	setter = getSetter("setFirstObject", Gene.class);
	setMethodMap.put("firstObject", setter);
	getter = getGetter("getFirstObject");
	getMethodMap.put("firstObject", getter);
	setter = getSetter("setSecondObject", Molecule.class);
	setMethodMap.put("secondObject", setter);
	getter = getGetter("getSecondObject");
	getMethodMap.put("secondObject", getter);
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
	public Gene getFirstObject(){
		return firstObject;
	}
	public Molecule getSecondObject(){
		return secondObject;
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

	public void setFirstObject(Gene var ){
		firstObject = var;
	}

	public void setSecondObject(Molecule var ){
		secondObject = var;
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
		return CodesForPanel.class;
	}
	public String getPrettyName()
	{
		return "CodesFor";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
