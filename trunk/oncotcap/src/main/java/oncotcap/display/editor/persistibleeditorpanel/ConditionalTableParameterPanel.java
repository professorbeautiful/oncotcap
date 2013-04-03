package oncotcap.display.editor.persistibleeditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class ConditionalTableParameterPanel extends DefaultEditorPanel
{
	private OncScrollList keywords = null;
	private OncScrollList conditionValue = null;
	private OncComboBox instanceUsabilityStatus = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList valueMapEntriesContainingMe = null;
	private OncScrollList possibleOutcomes = null;
	private OncScrollList stateMatrix = null;
	private OncScrollList variableDefinitions = null;
	private OncScrollList inputs = null;
	private OncScrollList outcomes = null;
	private OncScrollList function = null;


	public  ConditionalTableParameterPanel() {
		super();
		editObj = new ConditionalTableParameter();
		initUI();
		fillUiHashtable();
	}


	public  ConditionalTableParameterPanel(ConditionalTableParameter editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (ConditionalTableParameter)editObj;
	initUI();
	}
	private void initUI() {
		stateMatrix = new OncScrollList(StateMatrix.class, editObj, "StateMatrix", true,true, false);
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		Object[] comboBoxList1 = {"ReadyForUse","InProgress","Retired"};
		instanceUsabilityStatus = new OncComboBox(editObj, "InstanceUsabilityStatus", true, comboBoxList1);
		conditionValue = new OncScrollList(Persistible.class, editObj, "ConditionValue", true,true, false);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
		valueMapEntriesContainingMe = new OncScrollList(ValueMapEntry.class, editObj, "ValueMapEntriesContainingMe", true,true);
		possibleOutcomes = new OncScrollList(ConditionalOutcome.class, editObj, "PossibleOutcomes", true,true);
		variableDefinitions = new OncScrollList(VariableDefinition.class, editObj, "VariableDefinitions", true,true);
		inputs = new OncScrollList(Persistible.class, editObj, "Inputs", true,true);
		outcomes = new OncScrollList(ConditionalOutcome.class, editObj, "Outcomes", true,true);
		function = new OncScrollList(Persistible.class, editObj, "Function", true,true);
stateMatrix.setBounds(0,1620,250,60);
versionNumber.setBounds(0,1320,125,60);
instanceUsabilityStatus.setBounds(0,1500,125,60);
conditionValue.setBounds(0,1560,250,60);
keywords.setBounds(0,1080,250,120);
valueMapEntriesContainingMe.setBounds(0,1380,250,120);
possibleOutcomes.setBounds(250,1680,250,240);
variableDefinitions.setBounds(250,1020,250,180);
inputs.setBounds(0,1800,250,120);
outcomes.setBounds(250,1560,250,120);
function.setBounds(0,1680,250,120);
stateMatrix.setVisible(true);
versionNumber.setVisible(true);
instanceUsabilityStatus.setVisible(true);
conditionValue.setVisible(true);
keywords.setVisible(true);
valueMapEntriesContainingMe.setVisible(true);
possibleOutcomes.setVisible(true);
variableDefinitions.setVisible(true);
inputs.setVisible(true);
outcomes.setVisible(true);
function.setVisible(true);
		add(stateMatrix);
		add(versionNumber);
		add(instanceUsabilityStatus);
		add(conditionValue);
		add(keywords);
		add(valueMapEntriesContainingMe);
		add(possibleOutcomes);
		add(variableDefinitions);
		add(inputs);
		add(outcomes);
		add(function);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(stateMatrix, "stateMatrix");
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(instanceUsabilityStatus, "instanceUsabilityStatus");
		uiHashtable.put(conditionValue, "conditionValue");
		uiHashtable.put(keywords, "keywords");
		uiHashtable.put(valueMapEntriesContainingMe, "valueMapEntriesContainingMe");
		uiHashtable.put(possibleOutcomes, "possibleOutcomes");
		uiHashtable.put(variableDefinitions, "variableDefinitions");
		uiHashtable.put(inputs, "inputs");
		uiHashtable.put(outcomes, "outcomes");
		uiHashtable.put(function, "function");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		ConditionalTableParameterPanel p = new ConditionalTableParameterPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
