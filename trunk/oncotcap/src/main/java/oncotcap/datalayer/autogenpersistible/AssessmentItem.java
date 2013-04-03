package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class AssessmentItem extends AutoGenPersistibleWithKeywords 
 {
	private AssessmentItemType assessmentItemType;
	private KnowledgeNugget knowledgeNugget;
	private String timeStamp;
	private String assessmentType;
	private String comment;
	private String assessmentScore;
	private Person assessor;
	private String columnNamesInOrder[] = 
		 {"Item Type" , "Type", "Assessor", "Score", "Comment"};
	private String columnsInOrder[] = 
		 {"assessmentItemType", "assessmentType", "assessor", "assessmentScore", "comment"};

public AssessmentItem() {
init();
}


public AssessmentItem(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setAssessmentItemType", AssessmentItemType.class);
	setMethodMap.put("assessmentItemType", setter);
	getter = getGetter("getAssessmentItemType");
	getMethodMap.put("assessmentItemType", getter);
	setter = getSetter("setKnowledgeNugget", KnowledgeNugget.class);
	setMethodMap.put("knowledgeNugget", setter);
	getter = getGetter("getKnowledgeNugget");
	getMethodMap.put("knowledgeNugget", getter);
	setter = getSetter("setTimeStamp", String.class);
	setMethodMap.put("timeStamp", setter);
	getter = getGetter("getTimeStamp");
	getMethodMap.put("timeStamp", getter);
	setter = getSetter("setAssessmentType", String.class);
	setMethodMap.put("assessmentType", setter);
	getter = getGetter("getAssessmentType");
	getMethodMap.put("assessmentType", getter);
	setter = getSetter("setComment", String.class);
	setMethodMap.put("comment", setter);
	getter = getGetter("getComment");
	getMethodMap.put("comment", getter);
	setter = getSetter("setAssessmentScore", String.class);
	setMethodMap.put("assessmentScore", setter);
	getter = getGetter("getAssessmentScore");
	getMethodMap.put("assessmentScore", getter);
	setter = getSetter("setAssessor", Person.class);
	setMethodMap.put("assessor", setter);
	getter = getGetter("getAssessor");
	getMethodMap.put("assessor", getter);
}

	public AssessmentItemType getAssessmentItemType(){
		return assessmentItemType;
	}
	public KnowledgeNugget getKnowledgeNugget(){
		return knowledgeNugget;
	}
	public String getTimeStamp(){
		return timeStamp;
	}
	public String getAssessmentType(){
		return assessmentType;
	}
	public String getComment(){
		return comment;
	}
	public String getAssessmentScore(){
		return assessmentScore;
	}
	public Person getAssessor(){
		return assessor;
	}
	public void setAssessmentItemType(AssessmentItemType var ){
		assessmentItemType = var;
	}

	public void setKnowledgeNugget(KnowledgeNugget var ){
		knowledgeNugget = var;
	}

	public void setTimeStamp(String var ){
		timeStamp = var;
	}

	public void setAssessmentType(String var ){
		assessmentType = var;
	}

	public void setComment(String var ){
		comment = var;
	}

	public void setAssessmentScore(String var ){
		assessmentScore = var;
	}

	public void setAssessor(Person var ){
		assessor = var;
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return AssessmentItemPanel.class;
	}
	public String getPrettyName()
	{
		return "AssessmentItem";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
	 public String [] getColumnsInOrder() {
				 
				 return columnsInOrder;
		 }
		 public String [] getColumnNamesInOrder() {
				 
				 return columnNamesInOrder;
		 }
		 public String getColumnLabel(String columnName) {
				 // Get label position 
				 
				 return "EMPTY";
		 }
}
