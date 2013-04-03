package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class QuoteNugget extends KnowledgeNugget
 {
	private String quoteSection;
	private Integer bookmarkLocation;
	private DefaultPersistibleList relevanceAssessments;
	private DefaultPersistibleList confidenceAssessments;
	private DefaultPersistibleList assessments;
	private DefaultPersistibleList interpretations;
	private String quote;
	private Integer versionNumber;
	private InformationSource informationSource;
public ImageIcon icon =	
	oncotcap.util.OncoTcapIcons.getImageIcon("knowledgenugget.jpg");


public QuoteNugget() {
init();
}


public QuoteNugget(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setQuoteSection", String.class);
	setMethodMap.put("quoteSection", setter);
	getter = getGetter("getQuoteSection");
	getMethodMap.put("quoteSection", getter);
	setter = getSetter("setBookmarkLocation", Integer.class);
	setMethodMap.put("bookmarkLocation", setter);
	getter = getGetter("getBookmarkLocation");
	getMethodMap.put("bookmarkLocation", getter);
	setter = getSetter("setRelevanceAssessments", DefaultPersistibleList.class);
	setMethodMap.put("relevanceAssessments", setter);
	getter = getGetter("getRelevanceAssessments");
	getMethodMap.put("relevanceAssessments", getter);
	setter = getSetter("setConfidenceAssessments", DefaultPersistibleList.class);
	setMethodMap.put("confidenceAssessments", setter);
	getter = getGetter("getConfidenceAssessments");
	getMethodMap.put("confidenceAssessments", getter);
	setter = getSetter("setAssessments", DefaultPersistibleList.class);
	setMethodMap.put("assessments", setter);
	getter = getGetter("getAssessments");
	getMethodMap.put("assessments", getter);
	setter = getSetter("setInterpretations", DefaultPersistibleList.class);
	setMethodMap.put("interpretations", setter);
	getter = getGetter("getInterpretations");
	getMethodMap.put("interpretations", getter);
	setter = getSetter("setQuote", String.class);
	setMethodMap.put("quote", setter);
	getter = getGetter("getQuote");
	getMethodMap.put("quote", getter);
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);
	setter = getSetter("setKeywords", DefaultPersistibleList.class);
	setMethodMap.put("keywords", setter);
	getter = getGetter("getKeywords");
	getMethodMap.put("keywords", getter);
	setter = getSetter("setInformationSource", InformationSource.class);
	setMethodMap.put("informationSource", setter);
	getter = getGetter("getInformationSource");
	getMethodMap.put("informationSource", getter);
}

	public String getQuoteSection(){
		return quoteSection;
	}
	public Integer getBookmarkLocation(){
		return bookmarkLocation;
	}
	public DefaultPersistibleList getRelevanceAssessments(){
		return relevanceAssessments;
	}
	public DefaultPersistibleList getConfidenceAssessments(){
		return confidenceAssessments;
	}
	public DefaultPersistibleList getAssessments(){
		return assessments;
	}
	public DefaultPersistibleList getInterpretations(){
		return interpretations;
	}
	public String getQuote(){
		return quote;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public InformationSource getInformationSource(){
		return informationSource;
	}
	public void setQuoteSection(String var ){
		quoteSection = var;
	}

	public void setBookmarkLocation(Integer var ){
		bookmarkLocation = var;
	}

	public void setRelevanceAssessments(java.util.Collection  var ){
		if ( relevanceAssessments== null)
			relevanceAssessments = new DefaultPersistibleList();
		relevanceAssessments.set(var);
	}

	public void setConfidenceAssessments(java.util.Collection  var ){
		if ( confidenceAssessments== null)
			confidenceAssessments = new DefaultPersistibleList();
		confidenceAssessments.set(var);
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

	public void setQuote(String var ){
		quote = var;
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setInformationSource(InformationSource var ){
		informationSource = var;
	}

	public String toString() {
		return quote;
	}
	public Class getPanelClass()
	{
		return QuoteNuggetPanel.class;
	}
	public String getPrettyName()
	{
		return "Quote Nugget";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
