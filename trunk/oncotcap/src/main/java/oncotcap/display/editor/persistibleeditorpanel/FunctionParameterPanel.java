package oncotcap.display.editor.persistibleeditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class FunctionParameterPanel extends DefaultEditorPanel
{
	private OncScrollList keywords = null;
	private OncComboBox instanceUsabilityStatus = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList valueMapEntriesContainingMe = null;
	private OncScrollList inputs = null;
	private OncScrollList variableDefinitions = null;
	private OncScrollList outcomes = null;
	private OncScrollList function = null;


	public  FunctionParameterPanel() {
		super();
		editObj = new FunctionParameter();
		initUI();
		fillUiHashtable();
	}


	public  FunctionParameterPanel(FunctionParameter editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (FunctionParameter)editObj;
	initUI();
	}
	private void initUI() {
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		Object[] comboBoxList1 = {"ReadyForUse","InProgress","Retired"};
		instanceUsabilityStatus = new OncComboBox(editObj, "InstanceUsabilityStatus", true, comboBoxList1);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
		valueMapEntriesContainingMe = new OncScrollList(ValueMapEntry.class, editObj, "ValueMapEntriesContainingMe", true,true);
		inputs = new OncScrollList(Persistible.class, editObj, "Inputs", true,true);
		variableDefinitions = new OncScrollList(VariableDefinition.class, editObj, "VariableDefinitions", true,true);
		outcomes = new OncScrollList(ConditionalOutcome.class, editObj, "Outcomes", true,true);
		function = new OncScrollList(Persistible.class, editObj, "Function", true,true);
versionNumber.setBounds(0,1320,125,60);
instanceUsabilityStatus.setBounds(0,1500,125,60);
keywords.setBounds(0,1080,250,120);
valueMapEntriesContainingMe.setBounds(0,1380,250,120);
inputs.setBounds(0,1680,250,120);
variableDefinitions.setBounds(250,1020,250,180);
outcomes.setBounds(250,1560,250,240);
function.setBounds(0,1560,250,120);
versionNumber.setVisible(true);
instanceUsabilityStatus.setVisible(true);
keywords.setVisible(true);
valueMapEntriesContainingMe.setVisible(true);
inputs.setVisible(true);
variableDefinitions.setVisible(true);
outcomes.setVisible(true);
function.setVisible(true);
		add(versionNumber);
		add(instanceUsabilityStatus);
		add(keywords);
		add(valueMapEntriesContainingMe);
		add(inputs);
		add(variableDefinitions);
		add(outcomes);
		add(function);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(instanceUsabilityStatus, "instanceUsabilityStatus");
		uiHashtable.put(keywords, "keywords");
		uiHashtable.put(valueMapEntriesContainingMe, "valueMapEntriesContainingMe");
		uiHashtable.put(inputs, "inputs");
		uiHashtable.put(variableDefinitions, "variableDefinitions");
		uiHashtable.put(outcomes, "outcomes");
		uiHashtable.put(function, "function");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		FunctionParameterPanel p = new FunctionParameterPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
