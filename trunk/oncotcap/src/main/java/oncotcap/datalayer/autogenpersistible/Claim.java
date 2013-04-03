package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class Claim extends AutoGenPersistibleWithKeywords 
 {
	private DefaultPersistibleList assessments;
	private DefaultPersistibleList refinements;
	private DefaultPersistibleList interpretationsOfMe;
	private DefaultPersistibleList infoSources;
	private String sentenceText;


public Claim() {
init();
}


public Claim(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setAssessments", DefaultPersistibleList.class);
	setMethodMap.put("assessments", setter);
	getter = getGetter("getAssessments");
	getMethodMap.put("assessments", getter);
	setter = getSetter("setRefinements", DefaultPersistibleList.class);
	setMethodMap.put("refinements", setter);
	getter = getGetter("getRefinements");
	getMethodMap.put("refinements", getter);
	setter = getSetter("setInterpretationsOfMe", DefaultPersistibleList.class);
	setMethodMap.put("interpretationsOfMe", setter);
	getter = getGetter("getInterpretationsOfMe");
	getMethodMap.put("interpretationsOfMe", getter);
	setter = getSetter("setInfoSources", DefaultPersistibleList.class);
	setMethodMap.put("infoSources", setter);
	getter = getGetter("getInfoSources");
	getMethodMap.put("infoSources", getter);
	setter = getSetter("setSentenceText", String.class);
	setMethodMap.put("sentenceText", setter);
	getter = getGetter("getSentenceText");
	getMethodMap.put("sentenceText", getter);
}

	public DefaultPersistibleList getAssessments(){
		return assessments;
	}
	public DefaultPersistibleList getRefinements(){
		return refinements;
	}
	public DefaultPersistibleList getInterpretationsOfMe(){
		return interpretationsOfMe;
	}
	public DefaultPersistibleList getInfoSources(){
		return infoSources;
	}
	public String getSentenceText(){
		return sentenceText;
	}
	public void setAssessments(java.util.Collection  var ){
		if ( assessments== null)
			assessments = new DefaultPersistibleList();
		assessments.set(var);
	}

	public void setRefinements(java.util.Collection  var ){
		if ( refinements== null)
			refinements = new DefaultPersistibleList();
		refinements.set(var);
	}

	public void setInterpretationsOfMe(java.util.Collection  var ){
		if ( interpretationsOfMe== null)
			interpretationsOfMe = new DefaultPersistibleList();
		interpretationsOfMe.set(var);
	}

	public void setInfoSources(java.util.Collection  var ){
		if ( infoSources== null)
			infoSources = new DefaultPersistibleList();
		infoSources.set(var);
	}

	public void setSentenceText(String var ){
		sentenceText = var;
	}

	public String toString() {
		return sentenceText;
	}
	public Class getPanelClass()
	{
		return ClaimPanel.class;
	}
	public String getPrettyName()
	{
		return "Claim";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
