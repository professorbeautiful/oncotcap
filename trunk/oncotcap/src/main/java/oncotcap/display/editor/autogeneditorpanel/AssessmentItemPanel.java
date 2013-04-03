package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class AssessmentItemPanel extends DefaultEditorPanel
{
	private OncScrollList assessmentItemType = null;
	private OncScrollList knowledgeNugget = null;
	private OncTextField timeStamp = null;
	//private OncScrollList problemContext = null;
	private OncScrollableTextArea assessmentType = null;
	private OncTextField comment = null;
	private OncComboBox assessmentScore = null;
	private OncScrollList assessor = null;


	public  AssessmentItemPanel() {
		super();
		editObj = new AssessmentItem();
		initUI();
		fillUiHashtable();
	}


	public  AssessmentItemPanel(AssessmentItem editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (AssessmentItem)editObj;
	initUI();
	}
	private void initUI() {
		assessor = new OncScrollList(Person.class, editObj, "Assessor", true,true, false);
		Object[] comboBoxList1 = {"low","low/med","medium","med/high","high"};
		assessmentScore = new OncComboBox(editObj, "AssessmentScore", true, comboBoxList1);
		comment = new OncTextField(editObj, "Comment", true);
		assessmentType = new OncScrollableTextArea(editObj, "AssessmentType", true);
//		problemContext = new OncScrollList(null.class, editObj, "ProblemContext", true,true, false);
		timeStamp = new OncTextField(editObj, "TimeStamp", true);
		knowledgeNugget = new OncScrollList(KnowledgeNugget.class, editObj, "KnowledgeNugget", true,true, false);
		assessmentItemType = new OncScrollList(AssessmentItemType.class, editObj, "AssessmentItemType", true,true, false);
assessor.setBounds(300,60,250,30);
assessmentScore.setBounds(0,120,125,30);
comment.setBounds(0,0,250,30);
assessmentType.setBounds(0,180,250,30);
//problemContext.setBounds(250,180,250,60);
timeStamp.setBounds(0,60,250,30);
knowledgeNugget.setBounds(300,120,250,30);
assessmentItemType.setBounds(300,0,250,30);
assessor.setVisible(true);
assessmentScore.setVisible(true);
comment.setVisible(true);
assessmentType.setVisible(true);
//problemContext.setVisible(true);
timeStamp.setVisible(true);
knowledgeNugget.setVisible(true);
assessmentItemType.setVisible(true);
		add(assessor);
		add(assessmentScore);
		add(comment);
		add(assessmentType);
//		add(problemContext);
		add(timeStamp);
		add(knowledgeNugget);
		add(assessmentItemType);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(assessor, "assessor");
		uiHashtable.put(assessmentScore, "assessmentScore");
		uiHashtable.put(comment, "comment");
		uiHashtable.put(assessmentType, "assessmentType");
//		uiHashtable.put(problemContext, "problemContext");
		uiHashtable.put(timeStamp, "timeStamp");
		uiHashtable.put(knowledgeNugget, "knowledgeNugget");
		uiHashtable.put(assessmentItemType, "assessmentItemType");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		AssessmentItemPanel p = new AssessmentItemPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
