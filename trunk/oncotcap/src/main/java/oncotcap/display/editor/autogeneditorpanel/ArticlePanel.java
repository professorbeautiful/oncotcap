package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class ArticlePanel extends DefaultEditorPanel
{
	public OncScrollList quotesInMe = null;
	public OncScrollableTextArea pubMedURL = null;
	public OncScrollList fullSourceURL = null;
	public OncTextField title = null;
	public OncTextField pages = null;
	public OncIntegerTextField volume = null;
	public OncScrollList weblink = null;
	public OncTextField journalAsString = null;
	public OncIntegerTextField year = null;
	public OncScrollList journal = null;
	public OncScrollList keywords = null;
	public OncIntegerTextField issue = null;
	public OncIntegerTextField PMID = null;
	public OncScrollList authors = null;
	public OncIntegerTextField versionNumber = null;


	public  ArticlePanel() {
		super();
		editObj = new Article();
		initUI();
		fillUiHashtable();
	}


	public  ArticlePanel(Article editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Article)editObj;
	initUI();
	}
	private void initUI() {
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		PMID = new OncIntegerTextField(editObj, "PMID", true);
		issue = new OncIntegerTextField(editObj, "Issue", true);
		journal = new OncScrollList(Journal.class, editObj, "Journal", true,true, false);
		year = new OncIntegerTextField(editObj, "Year", true);
		journalAsString = new OncTextField(editObj, "JournalAsString", true);
		weblink = new OncScrollList(WebLink.class, editObj, "Weblink", true,true, false);
		volume = new OncIntegerTextField(editObj, "Volume", true);
		pages = new OncTextField(editObj, "Pages", true);
		title = new OncTextField(editObj, "Title", true);
		pubMedURL = new OncScrollableTextArea(editObj, "PubMedURL", true);
		quotesInMe = new OncScrollList(KnowledgeNugget.class, editObj, "QuotesInMe", true,true);
		fullSourceURL = new OncScrollList(String.class, editObj, "Another FullSourceURL", true,true);
		fullSourceURL.getButtonPanel().showWebButton(true);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
		authors = new OncScrollList(Author.class, editObj, "Authors", true,true);
versionNumber.setBounds(10,730,125,60);
PMID.setBounds(10,195,125,60);
issue.setBounds(180,510,70,60);
journal.setBounds(10,450,330,60);
year.setBounds(10,510,80,60);
journalAsString.setBounds(10,570,350,60);
volume.setBounds(100,510,70,60);
pages.setBounds(260,510,80,60);
title.setBounds(10,0,650,60);
quotesInMe.setBounds(10,65,650,110);
fullSourceURL.setBounds(10,340,590,80);
keywords.setBounds(380,450,270,160);
authors.setBounds(380,185,270,120);
versionNumber.setVisible(true);
PMID.setVisible(true);
issue.setVisible(true);
journal.setVisible(true);
year.setVisible(true);
journalAsString.setVisible(true);
weblink.setVisible(true);
volume.setVisible(true);
pages.setVisible(true);
title.setVisible(true);
pubMedURL.setVisible(true);
quotesInMe.setVisible(true);
fullSourceURL.setVisible(true);
keywords.setVisible(true);
authors.setVisible(true);
		add(versionNumber);
		add(PMID);
		add(issue);
		add(journal);
		add(year);
		add(journalAsString);
		add(weblink);
		add(volume);
		add(pages);
		add(title);
		add(pubMedURL);
		add(quotesInMe);
		add(fullSourceURL);
		add(keywords);
		add(authors);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(PMID, "PMID");
		uiHashtable.put(issue, "issue");
		uiHashtable.put(journal, "journal");
		uiHashtable.put(year, "year");
		uiHashtable.put(journalAsString, "journalAsString");
		uiHashtable.put(weblink, "weblink");
		uiHashtable.put(volume, "volume");
		uiHashtable.put(pages, "pages");
		uiHashtable.put(title, "title");
		uiHashtable.put(pubMedURL, "pubMedURL");
		uiHashtable.put(quotesInMe, "quotesInMe");
		uiHashtable.put(fullSourceURL, "fullSourceURL");
		uiHashtable.put(keywords, "keywords");
		uiHashtable.put(authors, "authors");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		ArticlePanel p = new ArticlePanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
