package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class InstantiateOncProcessPanel extends DefaultEditorPanel
{
	private OncScrollList initializationList = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList keywords = null;
	private OncScrollList oncProcess = null;
	private OncScrollList codeBundlesContainingMe = null;
	private OncTextField name = null;
	private OncTextField enumInitializations = null;


	public  InstantiateOncProcessPanel() {
		super();
		editObj = new InstantiateOncProcess();
		initUI();
		fillUiHashtable();
	}


	public  InstantiateOncProcessPanel(InstantiateOncProcess editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (InstantiateOncProcess)editObj;
	initUI();
	}
	private void initUI() {
		enumInitializations = new OncTextField(editObj, "EnumInitializations", true);
		name = new OncTextField(editObj, "Name", true);
		codeBundlesContainingMe = new OncScrollList(CodeBundle.class, editObj, "CodeBundlesContainingMe", true,true, false);
		oncProcess = new OncScrollList(ProcessDeclaration.class, editObj, "OncProcess", true,true, false);
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		initializationList = new OncScrollList(DeclareVariable.class, editObj, "InitializationList", true,true);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
enumInitializations.setBounds(0,120,250,60);
name.setBounds(0,300,250,60);
codeBundlesContainingMe.setBounds(250,0,250,60);
oncProcess.setBounds(250,60,250,60);
versionNumber.setBounds(0,360,125,60);
initializationList.setBounds(250,120,250,120);
keywords.setBounds(250,240,250,180);
enumInitializations.setVisible(true);
name.setVisible(true);
codeBundlesContainingMe.setVisible(true);
oncProcess.setVisible(true);
versionNumber.setVisible(true);
initializationList.setVisible(true);
keywords.setVisible(true);
		add(enumInitializations);
		add(name);
		add(codeBundlesContainingMe);
		add(oncProcess);
		add(versionNumber);
		add(initializationList);
		add(keywords);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(enumInitializations, "enumInitializations");
		uiHashtable.put(name, "name");
		uiHashtable.put(codeBundlesContainingMe, "codeBundlesContainingMe");
		uiHashtable.put(oncProcess, "oncProcess");
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(initializationList, "initializationList");
		uiHashtable.put(keywords, "keywords");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		InstantiateOncProcessPanel p = new InstantiateOncProcessPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
