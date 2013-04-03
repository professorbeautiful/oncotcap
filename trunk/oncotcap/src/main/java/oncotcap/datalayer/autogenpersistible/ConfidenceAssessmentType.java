package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class ConfidenceAssessmentType extends AssessmentItemType 
 {
	private String description;
	private String name;


public ConfidenceAssessmentType() {
init();
}


public ConfidenceAssessmentType(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setDescription", String.class);
	setMethodMap.put("description", setter);
	getter = getGetter("getDescription");
	getMethodMap.put("description", getter);
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
}

	public String getDescription(){
		return description;
	}
	public String getName(){
		return name;
	}
	public void setDescription(String var ){
		description = var;
	}

	public void setName(String var ){
		name = var;
	}

	public String toString() {
		return name;
	}
	public Class getPanelClass()
	{
		return ConfidenceAssessmentTypePanel.class;
	}
	public String getPrettyName()
	{
		return "ConfidenceAssessmentType";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
