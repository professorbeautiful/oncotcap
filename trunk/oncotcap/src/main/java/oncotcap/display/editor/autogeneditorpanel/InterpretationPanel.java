package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class InterpretationPanel extends DefaultEditorPanel
{
	private OncScrollList keywords = null;
	private OncScrollList encodingsImplementingMe = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList sourceNuggets = null;
	private OncScrollableTextArea sentenceText = null;


	public  InterpretationPanel() {
		super();
		editObj = new Interpretation();
		initUI();
		fillUiHashtable();
	}


	public  InterpretationPanel(Interpretation editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Interpretation)editObj;
	initUI();
	}
	private void initUI() {
		sentenceText = new OncScrollableTextArea(editObj, "SentenceText", true);
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
		encodingsImplementingMe = new OncScrollList(Encoding.class, editObj, "EdictsImplementingMe", true,true);
		sourceNuggets = new OncScrollList(KnowledgeNugget.class, editObj, "SourceNuggets", true,true);
sentenceText.setBounds(10,10,480,120);
versionNumber.setBounds(10,410,125,60);
keywords.setBounds(10,280,250,120);
encodingsImplementingMe.setBounds(390,140,330,120);
sourceNuggets.setBounds(10,140,370,130);
sentenceText.setVisible(true);
versionNumber.setVisible(true);
keywords.setVisible(true);
encodingsImplementingMe.setVisible(true);
sourceNuggets.setVisible(true);
		add(sentenceText);
		add(versionNumber);
		add(keywords);
		add(encodingsImplementingMe);
		add(sourceNuggets);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(sentenceText, "sentenceText");
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(keywords, "keywords");
		uiHashtable.put(encodingsImplementingMe, "encodingsImplementingMe");
		uiHashtable.put(sourceNuggets, "sourceNuggets");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		InterpretationPanel p = new InterpretationPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
