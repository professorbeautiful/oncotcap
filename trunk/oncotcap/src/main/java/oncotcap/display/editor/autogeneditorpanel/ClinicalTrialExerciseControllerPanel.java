package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class ClinicalTrialExerciseControllerPanel extends DefaultEditorPanel
{
	private OncScrollList outputScreens = null;
	private OncTextField modelControllerName = null;
	private OncScrollList modelConfiguration = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList introScreens = null;
	private OncScrollList keywords = null;
	private OncScrollList submodelGroups = null;
	private OncScrollList starterProcess = null;


	public  ClinicalTrialExerciseControllerPanel() {
		super();
		editObj = new ClinicalTrialExerciseController();
		initUI();
		fillUiHashtable();
	}


	public  ClinicalTrialExerciseControllerPanel(ClinicalTrialExerciseController editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (ClinicalTrialExerciseController)editObj;
	initUI();
	}
	private void initUI() {
		starterProcess = new OncScrollList(ProcessDeclaration.class, editObj, "StarterProcess", true,true, false);
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		modelConfiguration = new OncScrollList(ModelConfiguration.class, editObj, "ModelConfiguration", true,true, false);
		modelControllerName = new OncTextField(editObj, "ModelControllerName", true);
		outputScreens = new OncScrollList(String.class, editObj, "OutputScreens", true,true);
		introScreens = new OncScrollList(String.class, editObj, "IntroScreens", true,true);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
		submodelGroups = new OncScrollList(SubModelGroup.class, editObj, "SubmodelGroups", true,true);
starterProcess.setBounds(0,890,250,60);
versionNumber.setBounds(0,830,125,60);
modelConfiguration.setBounds(0,590,250,60);
modelControllerName.setBounds(10,0,570,60);
outputScreens.setBounds(0,710,250,120);
introScreens.setBounds(370,80,250,120);
keywords.setBounds(10,330,250,120);
submodelGroups.setBounds(10,80,350,230);
starterProcess.setVisible(true);
versionNumber.setVisible(true);
modelConfiguration.setVisible(true);
modelControllerName.setVisible(true);
outputScreens.setVisible(true);
introScreens.setVisible(true);
keywords.setVisible(true);
submodelGroups.setVisible(true);
		add(starterProcess);
		add(versionNumber);
		add(modelConfiguration);
		add(modelControllerName);
		add(outputScreens);
		add(introScreens);
		add(keywords);
		add(submodelGroups);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(starterProcess, "starterProcess");
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(modelConfiguration, "modelConfiguration");
		uiHashtable.put(modelControllerName, "modelControllerName");
		uiHashtable.put(outputScreens, "outputScreens");
		uiHashtable.put(introScreens, "introScreens");
		uiHashtable.put(keywords, "keywords");
		uiHashtable.put(submodelGroups, "submodelGroups");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		ClinicalTrialExerciseControllerPanel p = new ClinicalTrialExerciseControllerPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
