package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class ConditionalEventOutcomePanel extends DefaultEditorPanel
{
	private OncScrollableTextArea outcome = null;
	private OncScrollList stateMatrixRow = null;
	private OncComboBox instanceUsabilityStatus = null;
	private OncScrollableTextArea isDefaultValue = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList conditionValue = null;


	public  ConditionalEventOutcomePanel() {
		super();
		editObj = new ConditionalEventOutcome();
		initUI();
		fillUiHashtable();
	}


	public  ConditionalEventOutcomePanel(ConditionalEventOutcome editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (ConditionalEventOutcome)editObj;
	initUI();
	}
	private void initUI() {
		conditionValue = new OncScrollList(Persistible.class, editObj, "ConditionValue", true,true, false);
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		isDefaultValue = new OncScrollableTextArea(editObj, "IsDefaultValue", true);
		Object[] comboBoxList1 = {"ReadyForUse","InProgress","Retired"};
		instanceUsabilityStatus = new OncComboBox(editObj, "InstanceUsabilityStatus", true, comboBoxList1);
		stateMatrixRow = new OncScrollList(StateMatrixRow.class, editObj, "StateMatrixRow", true,true, false);
		outcome = new OncScrollableTextArea(editObj, "Outcome", true);
conditionValue.setBounds(250,60,250,60);
versionNumber.setBounds(125,240,125,60);
isDefaultValue.setBounds(0,240,125,60);
instanceUsabilityStatus.setBounds(375,0,125,60);
stateMatrixRow.setBounds(250,180,250,60);
outcome.setBounds(250,120,250,60);
conditionValue.setVisible(true);
versionNumber.setVisible(true);
isDefaultValue.setVisible(true);
instanceUsabilityStatus.setVisible(true);
stateMatrixRow.setVisible(true);
outcome.setVisible(true);
		add(conditionValue);
		add(versionNumber);
		add(isDefaultValue);
		add(instanceUsabilityStatus);
		add(stateMatrixRow);
		add(outcome);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(conditionValue, "conditionValue");
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(isDefaultValue, "isDefaultValue");
		uiHashtable.put(instanceUsabilityStatus, "instanceUsabilityStatus");
		uiHashtable.put(stateMatrixRow, "stateMatrixRow");
		uiHashtable.put(outcome, "outcome");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		ConditionalEventOutcomePanel p = new ConditionalEventOutcomePanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
