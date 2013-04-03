package oncotcap.display.editor.persistibleeditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class ConditionalDiscreteStateFunctionPanel extends DefaultEditorPanel
{
	private OncComboBox instanceUsabilityStatus = null;
	private OncScrollList conditionValue = null;
	private OncScrollList stateMatrix = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList outcomes = null;


	public  ConditionalDiscreteStateFunctionPanel() {
		super();
		editObj = new ConditionalDiscreteStateFunction();
		initUI();
		fillUiHashtable();
	}


	public  ConditionalDiscreteStateFunctionPanel(ConditionalDiscreteStateFunction editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (ConditionalDiscreteStateFunction)editObj;
	initUI();
	}
	private void initUI() {
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		stateMatrix = new OncScrollList(StateMatrix.class, editObj, "StateMatrix", true,true, false);
		conditionValue = new OncScrollList(Persistible.class, editObj, "ConditionValue", true,true, false);
		Object[] comboBoxList1 = {"ReadyForUse","InProgress","Retired"};
		instanceUsabilityStatus = new OncComboBox(editObj, "InstanceUsabilityStatus", true, comboBoxList1);
		outcomes = new OncScrollList(ConditionalOutcome.class, editObj, "Outcomes", true,true);
versionNumber.setBounds(0,240,125,60);
stateMatrix.setBounds(250,120,250,60);
conditionValue.setBounds(250,60,250,60);
instanceUsabilityStatus.setBounds(250,0,125,60);
outcomes.setBounds(250,180,250,120);
versionNumber.setVisible(true);
stateMatrix.setVisible(true);
conditionValue.setVisible(true);
instanceUsabilityStatus.setVisible(true);
outcomes.setVisible(true);
		add(versionNumber);
		add(stateMatrix);
		add(conditionValue);
		add(instanceUsabilityStatus);
		add(outcomes);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(stateMatrix, "stateMatrix");
		uiHashtable.put(conditionValue, "conditionValue");
		uiHashtable.put(instanceUsabilityStatus, "instanceUsabilityStatus");
		uiHashtable.put(outcomes, "outcomes");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		ConditionalDiscreteStateFunctionPanel p = new ConditionalDiscreteStateFunctionPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
