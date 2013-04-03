package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class DiagramNuggetPanel extends DefaultEditorPanel
{
	private OncScrollList assessments = null;
	private OncScrollList interpretations = null;
	private OncScrollList relevanceAssessments = null;
	private OncScrollList confidenceAssessments = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList keywords = null;
	private OncScrollList informationSource = null;


	public  DiagramNuggetPanel() {
		super();
		editObj = new DiagramNugget();
		initUI();
		fillUiHashtable();
	}


	public  DiagramNuggetPanel(DiagramNugget editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (DiagramNugget)editObj;
	initUI();
	}
	private void initUI() {
		informationSource = new OncScrollList(InformationSource.class, editObj, "InformationSource", true,true, false);
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		assessments = new OncScrollList(AssessmentItem.class, editObj, "Assessments", true,true);
		interpretations = new OncScrollList(Interpretation.class, editObj, "Interpretations", true,true);
		relevanceAssessments = new OncScrollList(RelevanceItem.class, editObj, "RelevanceAssessments", true,true);
		confidenceAssessments = new OncScrollList(ConfidenceItem.class, editObj, "ConfidenceAssessments", true,true);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
informationSource.setBounds(10,40,330,60);
versionNumber.setBounds(0,590,125,60);
assessments.setBounds(10,110,250,190);
interpretations.setBounds(350,40,390,120);
relevanceAssessments.setBounds(290,190,390,120);
confidenceAssessments.setBounds(290,330,400,120);
keywords.setBounds(10,330,250,120);
informationSource.setVisible(true);
versionNumber.setVisible(true);
assessments.setVisible(true);
interpretations.setVisible(true);
relevanceAssessments.setVisible(true);
confidenceAssessments.setVisible(true);
keywords.setVisible(true);
		add(informationSource);
		add(versionNumber);
		add(assessments);
		add(interpretations);
		add(relevanceAssessments);
		add(confidenceAssessments);
		add(keywords);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(informationSource, "informationSource");
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(assessments, "assessments");
		uiHashtable.put(interpretations, "interpretations");
		uiHashtable.put(relevanceAssessments, "relevanceAssessments");
		uiHashtable.put(confidenceAssessments, "confidenceAssessments");
		uiHashtable.put(keywords, "keywords");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		DiagramNuggetPanel p = new DiagramNuggetPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
