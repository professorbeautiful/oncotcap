package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class KnowledgeNugget extends AutoGenPersistibleWithKeywords 
 {
	private DefaultPersistibleList assessments;
	private DefaultPersistibleList interpretations;
	private DefaultPersistibleList relevanceAssessments;
	private Integer versionNumber;
	private DefaultPersistibleList confidenceAssessments;
	private InformationSource informationSource;
public ImageIcon icon =	
	oncotcap.util.OncoTcapIcons.getImageIcon("knowledgenugget.jpg");


public KnowledgeNugget() {
init();
}


public KnowledgeNugget(oncotcap.util.GUID guid) {
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
	setter = getSetter("setInterpretations", DefaultPersistibleList.class);
	setMethodMap.put("interpretations", setter);
	getter = getGetter("getInterpretations");
	getMethodMap.put("interpretations", getter);
	setter = getSetter("setRelevanceAssessments", DefaultPersistibleList.class);
	setMethodMap.put("relevanceAssessments", setter);
	getter = getGetter("getRelevanceAssessments");
	getMethodMap.put("relevanceAssessments", getter);
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);
	setter = getSetter("setConfidenceAssessments", DefaultPersistibleList.class);
	setMethodMap.put("confidenceAssessments", setter);
	getter = getGetter("getConfidenceAssessments");
	getMethodMap.put("confidenceAssessments", getter);
	setter = getSetter("setKeywords", DefaultPersistibleList.class);
	setMethodMap.put("keywords", setter);
	getter = getGetter("getKeywords");
	getMethodMap.put("keywords", getter);
	setter = getSetter("setInformationSource", InformationSource.class);
	setMethodMap.put("informationSource", setter);
	getter = getGetter("getInformationSource");
	getMethodMap.put("informationSource", getter);
}

	public DefaultPersistibleList getAssessments(){
		return assessments;
	}
	public DefaultPersistibleList getInterpretations(){
		return interpretations;
	}
	public DefaultPersistibleList getRelevanceAssessments(){
		return relevanceAssessments;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public DefaultPersistibleList getConfidenceAssessments(){
		return confidenceAssessments;
	}
	public InformationSource getInformationSource(){
		return informationSource;
	}
	public void setAssessments(java.util.Collection  var ){
		if ( assessments== null)
			assessments = new DefaultPersistibleList();
		assessments.set(var);
	}

	public void setInterpretations(java.util.Collection  var ){
		if ( interpretations== null)
			interpretations = new DefaultPersistibleList();
		interpretations.set(var);
	}

	public void setRelevanceAssessments(java.util.Collection  var ){
		if ( relevanceAssessments== null)
			relevanceAssessments = new DefaultPersistibleList();
		relevanceAssessments.set(var);
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setConfidenceAssessments(java.util.Collection  var ){
		if ( confidenceAssessments== null)
			confidenceAssessments = new DefaultPersistibleList();
		confidenceAssessments.set(var);
	}

	public void setInformationSource(InformationSource var ){
		informationSource = var;
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return KnowledgeNuggetPanel.class;
	}
	public String getPrettyName()
	{
		return "KnowledgeNugget";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
