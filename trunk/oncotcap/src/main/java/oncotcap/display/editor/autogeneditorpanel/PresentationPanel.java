package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class PresentationPanel extends DefaultEditorPanel
{
	private OncIntegerTextField versionNumber = null;
	private OncScrollList keywords = null;
	private OncScrollList quotesInMe = null;


	public  PresentationPanel() {
		super();
		editObj = new Presentation();
		initUI();
		fillUiHashtable();
	}


	public  PresentationPanel(Presentation editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Presentation)editObj;
	initUI();
	}
	private void initUI() {
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
		quotesInMe = new OncScrollList(KnowledgeNugget.class, editObj, "QuotesInMe", true,true);
versionNumber.setBounds(0,780,125,60);
keywords.setBounds(0,540,250,120);
quotesInMe.setBounds(0,660,250,120);
versionNumber.setVisible(true);
keywords.setVisible(true);
quotesInMe.setVisible(true);
		add(versionNumber);
		add(keywords);
		add(quotesInMe);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(keywords, "keywords");
		uiHashtable.put(quotesInMe, "quotesInMe");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		PresentationPanel p = new PresentationPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
