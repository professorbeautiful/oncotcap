package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class WebLink extends AutoGenPersistibleWithKeywords 
 {
	private String fullTextSourceLink;
	private String pubMedURL;
	private Integer PMID;
	private Article article;


public WebLink() {
init();
}


public WebLink(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setFullTextSourceLink", String.class);
	setMethodMap.put("fullTextSourceLink", setter);
	getter = getGetter("getFullTextSourceLink");
	getMethodMap.put("fullTextSourceLink", getter);
	setter = getSetter("setPubMedURL", String.class);
	setMethodMap.put("pubMedURL", setter);
	getter = getGetter("getPubMedURL");
	getMethodMap.put("pubMedURL", getter);
	setter = getSetter("setPMID", Integer.class);
	setMethodMap.put("PMID", setter);
	getter = getGetter("getPMID");
	getMethodMap.put("PMID", getter);
	setter = getSetter("setArticle", Article.class);
	setMethodMap.put("article", setter);
	getter = getGetter("getArticle");
	getMethodMap.put("article", getter);
}

	public String getFullTextSourceLink(){
		return fullTextSourceLink;
	}
	public String getPubMedURL(){
		return pubMedURL;
	}
	public Integer getPMID(){
		return PMID;
	}
	public Article getArticle(){
		return article;
	}
	public void setFullTextSourceLink(String var ){
		fullTextSourceLink = var;
	}

	public void setPubMedURL(String var ){
		pubMedURL = var;
	}

	public void setPMID(Integer var ){
		PMID = var;
	}

	public void setArticle(Article var ){
		article = var;
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return WebLinkPanel.class;
	}
	public String getPrettyName()
	{
		return "WebLink";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
