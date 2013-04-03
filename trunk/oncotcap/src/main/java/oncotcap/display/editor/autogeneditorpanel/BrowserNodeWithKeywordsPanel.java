package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class BrowserNodeWithKeywordsPanel extends DefaultEditorPanel
{
	private OncIntegerTextField versionNumber = null;
	private OncScrollList keywords = null;


	public  BrowserNodeWithKeywordsPanel() {
		super();
		editObj = new BrowserNodeWithKeywords();
		initUI();
		fillUiHashtable();
	}


	public  BrowserNodeWithKeywordsPanel(BrowserNodeWithKeywords editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (BrowserNodeWithKeywords)editObj;
	initUI();
	}
	private void initUI() {
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
versionNumber.setBounds(0,590,125,60);
keywords.setBounds(10,330,250,120);
versionNumber.setVisible(true);
keywords.setVisible(true);
		add(versionNumber);
		add(keywords);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(keywords, "keywords");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		BrowserNodeWithKeywordsPanel p = new BrowserNodeWithKeywordsPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
