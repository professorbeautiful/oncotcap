package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.QuantitativeInterpretation;
import oncotcap.datalayer.autogenpersistible.KnowledgeNugget;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class QuantitativeInterpretationPanel extends DefaultEditorPanel
{
	private OncScrollList keywords = null;
	private OncScrollList encodingsImplementingMe = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList sourceNuggets = null;
	private OncScrollableTextArea sentenceText = null;
	private OncScrollableTextArea rCode = null;
	
	public  QuantitativeInterpretationPanel() {
		super();
		editObj = new QuantitativeInterpretation();
		initUI();
		fillUiHashtable();
	}


	public  QuantitativeInterpretationPanel(QuantitativeInterpretation editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (QuantitativeInterpretation)editObj;
	initUI();
	}
	private void initUI() {
		sentenceText = new OncScrollableTextArea(editObj, "SentenceText", true);
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
		encodingsImplementingMe = new OncScrollList(Encoding.class, editObj, "EdictsImplementingMe", true,true);
		sourceNuggets = new OncScrollList(KnowledgeNugget.class, editObj, "SourceNuggets", true,true);
		rCode = new OncScrollableTextArea(editObj, "R Code", true);
		sentenceText.setBounds(10,10,480,120);
		rCode.setBounds(10,140, 700, 120);
		sourceNuggets.setBounds(10,280,370,130);
		encodingsImplementingMe.setBounds(390,280,330,120);
		versionNumber.setBounds(10,550,125,60);
		sentenceText.setVisible(true);
		keywords.setBounds(10,420,250,120);
		versionNumber.setVisible(true);
		keywords.setVisible(true);
		encodingsImplementingMe.setVisible(true);
		sourceNuggets.setVisible(true);
		rCode.setVisible(true);
		
		add(sentenceText);
		add(versionNumber);
		add(keywords);
		add(encodingsImplementingMe);
		add(sourceNuggets);
		add(rCode);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(sentenceText, "sentenceText");
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(keywords, "keywords");
		uiHashtable.put(encodingsImplementingMe, "encodingsImplementingMe");
		uiHashtable.put(sourceNuggets, "sourceNuggets");
		uiHashtable.put(rCode, "rCode");
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
