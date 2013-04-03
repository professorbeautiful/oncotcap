package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;

public class Article extends InformationSource
 {
	private DefaultPersistibleList quotesInMe;
	private String pubMedURL;
	private DefaultPersistibleList fullSourceURL;
	private String title;
	private String pages;
	private Integer volume;
	private WebLink weblink;
	private String journalAsString;
	private Integer year;
	private Journal journal;
	private Integer issue;
	private Integer PMID;
	private DefaultPersistibleList authors;
  public ImageIcon icon =	
	oncotcap.util.OncoTcapIcons.getImageIcon("informationsource.jpg");


public Article() {
init();
}


public Article(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setQuotesInMe", DefaultPersistibleList.class);
	setMethodMap.put("quotesInMe", setter);
	getter = getGetter("getQuotesInMe");
	getMethodMap.put("quotesInMe", getter);
	setter = getSetter("setPubMedURL", String.class);
	setMethodMap.put("pubMedURL", setter);
	getter = getGetter("getPubMedURL");
	getMethodMap.put("pubMedURL", getter);
	setter = getSetter("setFullSourceURL", DefaultPersistibleList.class);
	setMethodMap.put("fullSourceURL", setter);
	getter = getGetter("getFullSourceURL");
	getMethodMap.put("fullSourceURL", getter);
	setter = getSetter("setTitle", String.class);
	setMethodMap.put("title", setter);
	getter = getGetter("getTitle");
	getMethodMap.put("title", getter);
	setter = getSetter("setPages", String.class);
	setMethodMap.put("pages", setter);
	getter = getGetter("getPages");
	getMethodMap.put("pages", getter);
	setter = getSetter("setVolume", Integer.class);
	setMethodMap.put("volume", setter);
	getter = getGetter("getVolume");
	getMethodMap.put("volume", getter);
	setter = getSetter("setWeblink", WebLink.class);
	setMethodMap.put("weblink", setter);
	getter = getGetter("getWeblink");
	getMethodMap.put("weblink", getter);
	setter = getSetter("setJournalAsString", String.class);
	setMethodMap.put("journalAsString", setter);
	getter = getGetter("getJournalAsString");
	getMethodMap.put("journalAsString", getter);
	setter = getSetter("setYear", Integer.class);
	setMethodMap.put("year", setter);
	getter = getGetter("getYear");
	getMethodMap.put("year", getter);
	setter = getSetter("setJournal", Journal.class);
	setMethodMap.put("journal", setter);
	getter = getGetter("getJournal");
	getMethodMap.put("journal", getter);
	setter = getSetter("setKeywords", DefaultPersistibleList.class);
	setMethodMap.put("keywords", setter);
	getter = getGetter("getKeywords");
	getMethodMap.put("keywords", getter);
	setter = getSetter("setIssue", Integer.class);
	setMethodMap.put("issue", setter);
	getter = getGetter("getIssue");
	getMethodMap.put("issue", getter);
	setter = getSetter("setPMID", Integer.class);
	setMethodMap.put("PMID", setter);
	getter = getGetter("getPMID");
	getMethodMap.put("PMID", getter);
	setter = getSetter("setAuthors", DefaultPersistibleList.class);
	setMethodMap.put("authors", setter);
	getter = getGetter("getAuthors");
	getMethodMap.put("authors", getter);
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);
}

	public DefaultPersistibleList getQuotesInMe(){
		return quotesInMe;
	}
	public String getPubMedURL(){
		return pubMedURL;
	}
	public DefaultPersistibleList getFullSourceURL(){
		return fullSourceURL;
	}
	public String getTitle(){
		return title;
	}
	public String getPages(){
		return pages;
	}
	public Integer getVolume(){
		return volume;
	}
	public WebLink getWeblink(){
		return weblink;
	}
	public String getJournalAsString(){
		return journalAsString;
	}
	public Integer getYear(){
		return year;
	}
	public Journal getJournal(){
		return journal;
	}
	public Integer getIssue(){
		return issue;
	}
	public Integer getPMID(){
		return PMID;
	}
	public DefaultPersistibleList getAuthors(){
		return authors;
	}

	public void setQuotesInMe(java.util.Collection  var ){
		if ( quotesInMe== null)
			quotesInMe = new DefaultPersistibleList();
		quotesInMe.set(var);
	}

	public void setPubMedURL(String var ){
		pubMedURL = var;
	}

	public void setFullSourceURL(java.util.Collection  var ){
		if ( fullSourceURL== null)
			fullSourceURL = new DefaultPersistibleList();
		fullSourceURL.set(var);
	}

	public void setTitle(String var ){
		title = var;
	}

	public void setPages(String var ){
		pages = var;
	}

	public void setVolume(Integer var ){
		volume = var;
	}

	public void setWeblink(WebLink var ){
		weblink = var;
	}

	public void setJournalAsString(String var ){
		journalAsString = var;
	}

	public void setYear(Integer var ){
		year = var;
	}

	public void setJournal(Journal var ){
		journal = var;
	}

	public void setIssue(Integer var ){
		issue = var;
	}

	public void setPMID(Integer var ){
		PMID = var;
	}

	public void setAuthors(java.util.Collection  var ){
		if ( authors== null)
			authors = new DefaultPersistibleList();
		authors.set(var);
	}

	public String toString() {
		return title;
	}
	public Class getPanelClass()
	{
		return ArticlePanelWithPMIDPanel.class;
	}
	public String getPrettyName()
	{
		return "Article";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
