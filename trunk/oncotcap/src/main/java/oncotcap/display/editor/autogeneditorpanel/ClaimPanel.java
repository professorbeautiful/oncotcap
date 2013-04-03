package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class ClaimPanel extends DefaultEditorPanel
{
	private OncScrollList assessments = null;
	private OncScrollList refinements = null;
	private OncScrollList interpretationsOfMe = null;
	private OncScrollList infoSources = null;
	private OncTextField sentenceText = null;


	public  ClaimPanel() {
		super();
		editObj = new Claim();
		initUI();
		fillUiHashtable();
	}


	public  ClaimPanel(Claim editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Claim)editObj;
	initUI();
	}
	private void initUI() {
		sentenceText = new OncTextField(editObj, "SentenceText", true);
		assessments = new OncScrollList(AssessmentItem.class, editObj, "Assessments", true,true);
		refinements = new OncScrollList(Claim.class, editObj, "Refinements", true,true);
		interpretationsOfMe = new OncScrollList(Interpretation.class, editObj, "InterpretationsOfMe", true,true);
		infoSources = new OncScrollList(InformationSource.class, editObj, "InfoSources", true,true);
sentenceText.setBounds(0,0,250,60);
assessments.setBounds(0,60,250,120);
refinements.setBounds(250,120,250,180);
interpretationsOfMe.setBounds(250,0,250,120);
infoSources.setBounds(0,180,250,120);
sentenceText.setVisible(true);
assessments.setVisible(true);
refinements.setVisible(true);
interpretationsOfMe.setVisible(true);
infoSources.setVisible(true);
		add(sentenceText);
		add(assessments);
		add(refinements);
		add(interpretationsOfMe);
		add(infoSources);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(sentenceText, "sentenceText");
		uiHashtable.put(assessments, "assessments");
		uiHashtable.put(refinements, "refinements");
		uiHashtable.put(interpretationsOfMe, "interpretationsOfMe");
		uiHashtable.put(infoSources, "infoSources");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		ClaimPanel p = new ClaimPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
