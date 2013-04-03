package oncotcap.datalayer.autogenpersistible;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import javax.swing.*;
import java.lang.reflect.*;
import oncotcap.display.editor.autogeneditorpanel.*;

public class ConfidenceItem extends AutoGenPersistibleWithKeywords 
																		implements Tableable
 {
	private String assessmentScore;
	private Person assessor;
	private String comment;
	private AssessmentItemType assessmentItemType;
	private String timeStamp;
	private KnowledgeNugget knowledgeNugget;
	private String assessmentType;
		 String columnNamesInOrder[] = 
		 {"Item Type" , "Type", "Assessor", "Score", "Comment"};
		 String columnsInOrder[] = 
		 {"assessmentItemType", "assessmentType", "assessor", 
			"assessmentScore", "comment"};

	public ConfidenceItem(oncotcap.util.GUID guid){
			super(guid);
			initMaps();
	}

		 public ConfidenceItem() {
				 initMaps();
		 }
		 private void initMaps() {
				 Method setter = null;
				 Method getter = null;
				 setter = getSetter("setAssessmentScore", String.class);
				 setMethodMap.put("assessmentScore", setter);
				 getter = getGetter("getAssessmentScore");
				 getMethodMap.put("assessmentScore", getter);
				 setter = getSetter("setAssessor", Person.class);
				 setMethodMap.put("assessor", setter);
				 getter = getGetter("getAssessor");
				 getMethodMap.put("assessor", getter);
				 setter = getSetter("setComment", String.class);
				 setMethodMap.put("comment", setter);
				 getter = getGetter("getComment");
				 getMethodMap.put("comment", getter);
				 setter = getSetter("setAssessmentItemType", AssessmentItemType.class);
				 setMethodMap.put("assessmentItemType", setter);
				 getter = getGetter("getAssessmentItemType");
				 getMethodMap.put("assessmentItemType", getter);
				 setter = getSetter("setTimeStamp", String.class);
				 setMethodMap.put("timeStamp", setter);
				 getter = getGetter("getTimeStamp");
				 getMethodMap.put("timeStamp", getter);
				 setter = getSetter("setKnowledgeNugget", KnowledgeNugget.class);
				 setMethodMap.put("knowledgeNugget", setter);
				 getter = getGetter("getKnowledgeNugget");
				 getMethodMap.put("knowledgeNugget", getter);
				 setter = getSetter("setAssessmentType", String.class);
				 setMethodMap.put("assessmentType", setter);
				 getter = getGetter("getAssessmentType");
				 getMethodMap.put("assessmentType", getter);
		 }
		 public String getAssessmentScore(){
				 return assessmentScore;
		 }
		 public Person getAssessor(){
				 return assessor;
		 }
		 public String getComment(){
				 return comment;
		 }
		 public AssessmentItemType getAssessmentItemType(){
				 return assessmentItemType;
		 }
		 public String getTimeStamp(){
				 return timeStamp;
		 }
		 public KnowledgeNugget getKnowledgeNugget(){
				 return knowledgeNugget;
		 }
	public String getAssessmentType(){
		return "CONFIDENCE";
	}

	public void setAssessmentScore(String var ){
		assessmentScore = var;
	}

	public void setAssessor(Person var ){
		assessor = var;
	}

	public void setComment(String var ){
		comment = var;
	}

	public void setAssessmentItemType(AssessmentItemType var ){
		assessmentItemType = var;
	}

	public void setTimeStamp(String var ){
		timeStamp = var;
	}

	public void setKnowledgeNugget(KnowledgeNugget var ){
		knowledgeNugget = var;
	}

	public void setAssessmentType(String var ){
			//assessmentType = var;  this is a constant
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return ConfidenceItemPanel.class;
	}
	public String getPrettyName()
	{
		return "Assessment";
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
