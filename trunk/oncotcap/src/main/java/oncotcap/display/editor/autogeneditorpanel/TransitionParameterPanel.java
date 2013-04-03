package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class TransitionParameterPanel extends DefaultEditorPanel
{
	private OncScrollList valueMapEntriesContainingMe = null;
	private OncComboBox instanceUsabilityStatus = null;
	private OncScrollList variableDefinitions = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList keywords = null;


	public  TransitionParameterPanel() {
		super();
		editObj = new TransitionParameter();
		initUI();
		fillUiHashtable();
	}


	public  TransitionParameterPanel(TransitionParameter editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (TransitionParameter)editObj;
	initUI();
	}
	private void initUI() {
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		Object[] comboBoxList1 = {"ReadyForUse","InProgress","Retired"};
		instanceUsabilityStatus = new OncComboBox(editObj, "InstanceUsabilityStatus", true, comboBoxList1);
		valueMapEntriesContainingMe = new OncScrollList(ValueMapEntry.class, editObj, "ValueMapEntriesContainingMe", true,true);
		variableDefinitions = new OncScrollList(VariableDefinition.class, editObj, "VariableDefinitions", true,true);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
versionNumber.setBounds(0,1320,125,60);
instanceUsabilityStatus.setBounds(0,1500,125,60);
valueMapEntriesContainingMe.setBounds(0,1380,250,120);
variableDefinitions.setBounds(250,1020,250,180);
keywords.setBounds(0,1080,250,120);
versionNumber.setVisible(true);
instanceUsabilityStatus.setVisible(true);
valueMapEntriesContainingMe.setVisible(true);
variableDefinitions.setVisible(true);
keywords.setVisible(true);
		add(versionNumber);
		add(instanceUsabilityStatus);
		add(valueMapEntriesContainingMe);
		add(variableDefinitions);
		add(keywords);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(instanceUsabilityStatus, "instanceUsabilityStatus");
		uiHashtable.put(valueMapEntriesContainingMe, "valueMapEntriesContainingMe");
		uiHashtable.put(variableDefinitions, "variableDefinitions");
		uiHashtable.put(keywords, "keywords");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		TransitionParameterPanel p = new TransitionParameterPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
