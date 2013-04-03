package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class SettingSubModelGroupPanel extends DefaultEditorPanel
{
	private OncIntegerTextField versionNumber = null;
	private OncScrollList keywords = null;
	private OncScrollList modelControllersUsingMe = null;
	private OncTextField name = null;
	private OncScrollList submodelsInGroup = null;


	public  SettingSubModelGroupPanel() {
		super();
		editObj = new SettingSubModelGroup();
		initUI();
		fillUiHashtable();
	}


	public  SettingSubModelGroupPanel(SettingSubModelGroup editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (SettingSubModelGroup)editObj;
	initUI();
	}
	private void initUI() {
		name = new OncTextField(editObj, "Name", true);
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
		modelControllersUsingMe = new OncScrollList(ModelController.class, editObj, "ModelControllersUsingMe", true,true);
		submodelsInGroup = new OncScrollList(SubModel.class, editObj, "SubmodelsInGroup", true,true);
name.setBounds(10,0,670,60);
versionNumber.setBounds(0,590,125,60);
keywords.setBounds(10,330,250,120);
modelControllersUsingMe.setBounds(420,80,250,200);
submodelsInGroup.setBounds(10,80,380,200);
name.setVisible(true);
versionNumber.setVisible(true);
keywords.setVisible(true);
modelControllersUsingMe.setVisible(true);
submodelsInGroup.setVisible(true);
		add(name);
		add(versionNumber);
		add(keywords);
		add(modelControllersUsingMe);
		add(submodelsInGroup);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(name, "name");
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(keywords, "keywords");
		uiHashtable.put(modelControllersUsingMe, "modelControllersUsingMe");
		uiHashtable.put(submodelsInGroup, "submodelsInGroup");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		SettingSubModelGroupPanel p = new SettingSubModelGroupPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
