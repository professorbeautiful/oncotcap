package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class ClinicalTrialExerciseController extends ModelController
 {
	private String outputScreen;
	private String modelControllerName;
	private ModelConfiguration modelConfiguration;
	private Integer versionNumber;
	private DefaultPersistibleList introScreens;
	private DefaultPersistibleList submodelGroups;
	private ProcessDeclaration starterProcess;


public ClinicalTrialExerciseController() {
init();
}


public ClinicalTrialExerciseController(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setOutputScreen", String.class);
	setMethodMap.put("outputScreen", setter);
	getter = getGetter("getOutputScreen");
	getMethodMap.put("outputScreen", getter);
	setter = getSetter("setModelControllerName", String.class);
	setMethodMap.put("modelControllerName", setter);
	getter = getGetter("getModelControllerName");
	getMethodMap.put("modelControllerName", getter);
	setter = getSetter("setModelConfiguration", ModelConfiguration.class);
	setMethodMap.put("modelConfiguration", setter);
	getter = getGetter("getModelConfiguration");
	getMethodMap.put("modelConfiguration", getter);
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);
	setter = getSetter("setIntroScreens", DefaultPersistibleList.class);
	setMethodMap.put("introScreens", setter);
	getter = getGetter("getIntroScreens");
	getMethodMap.put("introScreens", getter);
	setter = getSetter("setKeywords", DefaultPersistibleList.class);
	setMethodMap.put("keywords", setter);
	getter = getGetter("getKeywords");
	getMethodMap.put("keywords", getter);
	setter = getSetter("setSubmodelGroups", DefaultPersistibleList.class);
	setMethodMap.put("submodelGroups", setter);
	getter = getGetter("getSubmodelGroups");
	getMethodMap.put("submodelGroups", getter);
	setter = getSetter("setStarterProcess", ProcessDeclaration.class);
	setMethodMap.put("starterProcess", setter);
	getter = getGetter("getStarterProcess");
	getMethodMap.put("starterProcess", getter);
}

	public String getOutputScreen(){
		return outputScreen;
	}
	public String getModelControllerName(){
		return modelControllerName;
	}
	public ModelConfiguration getModelConfiguration(){
		return modelConfiguration;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public DefaultPersistibleList getIntroScreens(){
		return introScreens;
	}
	public DefaultPersistibleList getSubmodelGroups(){
		return submodelGroups;
	}
	public ProcessDeclaration getStarterProcess(){
		return starterProcess;
	}
	public void setOutputScreens(String  var ){
		outputScreen = var;
	}

	public void setModelControllerName(String var ){
		modelControllerName = var;
	}

	public void setModelConfiguration(ModelConfiguration var ){
		modelConfiguration = var;
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setIntroScreens(java.util.Collection  var ){
		if ( introScreens== null)
			introScreens = new DefaultPersistibleList();
		introScreens.set(var);
	}

	public void setSubmodelGroups(java.util.Collection  var ){
		if ( submodelGroups== null)
			submodelGroups = new DefaultPersistibleList();
		submodelGroups.set(var);
	}

	public void setStarterProcess(ProcessDeclaration var ){
		starterProcess = var;
	}

	public String toString() {
		return modelControllerName;
	}
	public Class getPanelClass()
	{
		return ClinicalTrialExerciseControllerPanel.class;
	}
	public String getPrettyName()
	{
		return "ClinicalTrialExerciseController";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
