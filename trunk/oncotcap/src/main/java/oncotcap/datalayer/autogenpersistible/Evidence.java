package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class Evidence extends AutoGenPersistibleWithKeywords 
 {
	private String direction;
	private Claim statement;
	private DefaultPersistibleList assessments;
	private String sentenceText;


public Evidence() {
init();
}


public Evidence(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setDirection", String.class);
	setMethodMap.put("direction", setter);
	getter = getGetter("getDirection");
	getMethodMap.put("direction", getter);
	setter = getSetter("setStatement", Claim.class);
	setMethodMap.put("statement", setter);
	getter = getGetter("getStatement");
	getMethodMap.put("statement", getter);
	setter = getSetter("setAssessments", DefaultPersistibleList.class);
	setMethodMap.put("assessments", setter);
	getter = getGetter("getAssessments");
	getMethodMap.put("assessments", getter);
	setter = getSetter("setSentenceText", String.class);
	setMethodMap.put("sentenceText", setter);
	getter = getGetter("getSentenceText");
	getMethodMap.put("sentenceText", getter);
}

	public String getDirection(){
		return direction;
	}
	public Claim getStatement(){
		return statement;
	}
	public DefaultPersistibleList getAssessments(){
		return assessments;
	}
	public String getSentenceText(){
		return sentenceText;
	}
	public void setDirection(String var ){
		direction = var;
	}

	public void setStatement(Claim var ){
		statement = var;
	}

	public void setAssessments(java.util.Collection  var ){
		if ( assessments== null)
			assessments = new DefaultPersistibleList();
		assessments.set(var);
	}

	public void setSentenceText(String var ){
		sentenceText = var;
	}

	public String toString() {
		return sentenceText;
	}
	public Class getPanelClass()
	{
		return EvidencePanel.class;
	}
	public String getPrettyName()
	{
		return "Evidence";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
