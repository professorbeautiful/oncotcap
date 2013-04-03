package oncotcap.display.editor.autogeneditorpanel;

import java.util.*;

import oncotcap.datalayer.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.*;

import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;


public class RelevanceItemPanel extends DefaultEditorPanel
{
	private OncTextField timeStamp = null;
	private OncScrollList knowledgeNugget = null;
	//private OncScrollList assessmentItemType = null;
	private OncComboBox assessmentItemType = null;
	
	private OncComboBox assessmentScore = null;
	//private OncScrollableTextArea assessmentScore = null;
	private OncScrollList assessor = null;
	private OncScrollableTextArea comment = null;
	//private OncTextField comment = null;
	private OncScrollableTextArea assessmentType = null;


	public  RelevanceItemPanel() {
		super();
		editObj = new RelevanceItem();
		initUI();
		fillUiHashtable();
	}


	public  RelevanceItemPanel(RelevanceItem editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (RelevanceItem)editObj;
	initUI();
	}
//	private void initUI() {
//		assessmentType = new OncScrollableTextArea(editObj, "assessmentType", true);
//		comment = new OncTextField(editObj, "comment", true);
//		assessor = new OncScrollList(Person.class, editObj, "assessor", true,true, false);
//		assessmentScore = new OncScrollableTextArea(editObj, "assessmentScore", true);
//		assessmentItemType = new OncScrollList(AssessmentItemType.class, editObj, "assessmentItemType", true,true, false);
//		knowledgeNugget = new OncScrollList(KnowledgeNugget.class, editObj, "knowledgeNugget", true,true, false);
//		timeStamp = new OncTextField(editObj, "timeStamp", true);
//assessmentType.setBounds(125,120,125,60);
//comment.setBounds(0,0,250,60);
//assessor.setBounds(250,60,250,60);
//assessmentScore.setBounds(0,120,125,60);
//assessmentItemType.setBounds(250,0,250,60);
//knowledgeNugget.setBounds(250,120,250,60);
//timeStamp.setBounds(0,60,250,60);
//assessmentType.setVisible(true);
//comment.setVisible(true);
//assessor.setVisible(true);
//assessmentScore.setVisible(true);
//assessmentItemType.setVisible(true);
//knowledgeNugget.setVisible(true);
//timeStamp.setVisible(true);
//		add(assessmentType);
//		add(comment);
//		add(assessor);
//		add(assessmentScore);
//		add(assessmentItemType);
//		add(knowledgeNugget);
//		add(timeStamp);
//	}

	private void initUI() {
		assessmentType = new OncScrollableTextArea(editObj, 
																							 "assessmentType", true);
		comment = new OncScrollableTextArea(editObj, "Comment", true);
		assessor = new OncScrollList(Person.class, editObj, 
																 "Assessor", true,true, false);
		Object[] comboBoxList1 = {"High","High/Medium","Medium",
															"Medium/Low","Low"};
		assessmentScore = 
				 new OncComboBox(editObj, "Assessment Score", true, comboBoxList1);
		
		//assessmentItemType = new OncScrollList(AssessmentItemType.class, editObj, "assessmentItemType", true,true, false);
		knowledgeNugget = new OncScrollList(KnowledgeNugget.class, editObj, "knowledgeNugget", false, true, false);
		timeStamp = new OncTextField(editObj, "timeStamp", true);
		Vector vec1 = 
				new Vector(AutoGenPersistibleWithKeywords.getAll(RelevanceAssessmentType.class));
		 assessmentItemType = 
				 new OncComboBox(editObj, "Assessment Item Type", true, vec1 );
		
		 //assessmentType.setBounds(125,120,125,60);
		 knowledgeNugget.setBounds(5,5, 500,30);
		 assessmentItemType.setBounds(5,45,150,40);
		 assessmentScore.setBounds(165, 45,125,40);
		 assessor.setBounds(5,90,275,40);
		 comment.setBounds(5, 145, 500,100);
		 //assessmentType.setVisible(true);
		 //timeStamp.setBounds(0,60,250,60);
		 comment.setVisible(true);
		 assessor.setVisible(true);
		 assessmentScore.setVisible(true);
		 assessmentItemType.setVisible(true);
		 knowledgeNugget.setVisible(true);
		 //timeStamp.setVisible(true);
		 add(assessmentType);
		 add(comment);
		 add(assessor);
		 add(assessmentScore);
		 add(assessmentItemType);
		 add(knowledgeNugget);
		 add(timeStamp);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(assessmentType, "assessmentType");
		uiHashtable.put(comment, "comment");
		uiHashtable.put(assessor, "assessor");
		uiHashtable.put(assessmentScore, "assessmentScore");
		uiHashtable.put(assessmentItemType, "assessmentItemType");
		uiHashtable.put(knowledgeNugget, "knowledgeNugget");
		uiHashtable.put(timeStamp, "timeStamp");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		RelevanceItemPanel p = new RelevanceItemPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
