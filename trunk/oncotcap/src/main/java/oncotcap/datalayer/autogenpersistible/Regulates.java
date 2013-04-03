package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class Regulates extends AutoGenPersistibleWithKeywords 
 {
	private String biorelationshipType;
	private String direction;
	private Gene firstObject;
	private Molecule secondObject;
	private Float magnitude;
	private String name;
	private DefaultPersistibleList sentencesAboutThis;


public Regulates() {
init();
}


public Regulates(oncotcap.util.GUID guid) {
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
	setter = getSetter("setDirection", String.class);
	setMethodMap.put("direction", setter);
	getter = getGetter("getDirection");
	getMethodMap.put("direction", getter);
	setter = getSetter("setFirstObject", Gene.class);
	setMethodMap.put("firstObject", setter);
	getter = getGetter("getFirstObject");
	getMethodMap.put("firstObject", getter);
	setter = getSetter("setSecondObject", Molecule.class);
	setMethodMap.put("secondObject", setter);
	getter = getGetter("getSecondObject");
	getMethodMap.put("secondObject", getter);
	setter = getSetter("setMagnitude", Float.class);
	setMethodMap.put("magnitude", setter);
	getter = getGetter("getMagnitude");
	getMethodMap.put("magnitude", getter);
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
	public String getDirection(){
		return direction;
	}
	public Gene getFirstObject(){
		return firstObject;
	}
	public Molecule getSecondObject(){
		return secondObject;
	}
	public Float getMagnitude(){
		return magnitude;
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

	public void setDirection(String var ){
		direction = var;
	}

	public void setFirstObject(Gene var ){
		firstObject = var;
	}

	public void setSecondObject(Molecule var ){
		secondObject = var;
	}

	public void setMagnitude(Float var ){
		magnitude = var;
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
		return RegulatesPanel.class;
	}
	public String getPrettyName()
	{
		return "Regulates";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
