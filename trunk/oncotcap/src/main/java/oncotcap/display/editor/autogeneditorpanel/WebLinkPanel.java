package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class WebLinkPanel extends DefaultEditorPanel
{
	private OncTextField fullTextSourceLink = null;
	private OncTextField pubMedURL = null;
	private OncIntegerTextField PMID = null;
	private OncScrollList article = null;


	public  WebLinkPanel() {
		super();
		editObj = new WebLink();
		initUI();
		fillUiHashtable();
	}


	public  WebLinkPanel(WebLink editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (WebLink)editObj;
	initUI();
	}
	private void initUI() {
		article = new OncScrollList(Article.class, editObj, "Article", true,true, false);
		PMID = new OncIntegerTextField(editObj, "PMID", true);
		pubMedURL = new OncTextField(editObj, "PubMedURL", true);
		fullTextSourceLink = new OncTextField(editObj, "FullTextSourceLink", true);
article.setBounds(0,270,250,60);
PMID.setBounds(0,210,125,60);
pubMedURL.setBounds(0,90,650,60);
fullTextSourceLink.setBounds(0,150,250,60);
article.setVisible(true);
PMID.setVisible(true);
pubMedURL.setVisible(true);
fullTextSourceLink.setVisible(true);
		add(article);
		add(PMID);
		add(pubMedURL);
		add(fullTextSourceLink);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(article, "article");
		uiHashtable.put(PMID, "PMID");
		uiHashtable.put(pubMedURL, "pubMedURL");
		uiHashtable.put(fullTextSourceLink, "fullTextSourceLink");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		WebLinkPanel p = new WebLinkPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
