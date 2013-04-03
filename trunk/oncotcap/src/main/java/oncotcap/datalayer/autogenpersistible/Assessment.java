package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class Assessment extends AutoGenPersistibleWithKeywords 
 {
	private String timeStamp;
	private Person assessor;


public Assessment() {
init();
}


public Assessment(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setTimeStamp", String.class);
	setMethodMap.put("timeStamp", setter);
	getter = getGetter("getTimeStamp");
	getMethodMap.put("timeStamp", getter);
	setter = getSetter("setAssessor", Person.class);
	setMethodMap.put("assessor", setter);
	getter = getGetter("getAssessor");
	getMethodMap.put("assessor", getter);
}

	public String getTimeStamp(){
		return timeStamp;
	}
	public Person getAssessor(){
		return assessor;
	}
	public void setTimeStamp(String var ){
		timeStamp = var;
	}


	public void setAssessor(Person var ){
		assessor = var;
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return AssessmentPanel.class;
	}
	public String getPrettyName()
	{
		return "Assessment";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
