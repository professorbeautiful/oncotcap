package oncotcap.display.editor.persistibleeditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class ConditionalOutcomePanel extends DefaultEditorPanel
{
	private OncScrollableTextArea isDefaultValue = null;
	private OncComboBox instanceUsabilityStatus = null;
	private OncScrollList conditionValue = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList outcome = null;
	private OncScrollList stateMatrixRow = null;


	public  ConditionalOutcomePanel() {
		super();
		editObj = new ConditionalOutcome();
		initUI();
		fillUiHashtable();
	}


	public  ConditionalOutcomePanel(ConditionalOutcome editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (ConditionalOutcome)editObj;
	initUI();
	}
	private void initUI() {
		stateMatrixRow = new OncScrollList(StateMatrixRow.class, editObj, "StateMatrixRow", true,true, false);
		outcome = new OncScrollList(Persistible.class, editObj, "Outcome", true,true, false);
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		conditionValue = new OncScrollList(Persistible.class, editObj, "ConditionValue", true,true, false);
		Object[] comboBoxList1 = {"ReadyForUse","InProgress","Retired"};
		instanceUsabilityStatus = new OncComboBox(editObj, "InstanceUsabilityStatus", true, comboBoxList1);
		isDefaultValue = new OncScrollableTextArea(editObj, "IsDefaultValue", true);
stateMatrixRow.setBounds(250,180,250,60);
outcome.setBounds(250,120,250,60);
versionNumber.setBounds(125,240,125,60);
conditionValue.setBounds(250,60,250,60);
instanceUsabilityStatus.setBounds(375,0,125,60);
isDefaultValue.setBounds(0,240,125,60);
stateMatrixRow.setVisible(true);
outcome.setVisible(true);
versionNumber.setVisible(true);
conditionValue.setVisible(true);
instanceUsabilityStatus.setVisible(true);
isDefaultValue.setVisible(true);
		add(stateMatrixRow);
		add(outcome);
		add(versionNumber);
		add(conditionValue);
		add(instanceUsabilityStatus);
		add(isDefaultValue);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(stateMatrixRow, "stateMatrixRow");
		uiHashtable.put(outcome, "outcome");
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(conditionValue, "conditionValue");
		uiHashtable.put(instanceUsabilityStatus, "instanceUsabilityStatus");
		uiHashtable.put(isDefaultValue, "isDefaultValue");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		ConditionalOutcomePanel p = new ConditionalOutcomePanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
