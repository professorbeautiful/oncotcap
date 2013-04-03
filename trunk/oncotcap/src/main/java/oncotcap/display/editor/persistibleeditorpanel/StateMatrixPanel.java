package oncotcap.display.editor.persistibleeditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class StateMatrixPanel extends DefaultEditorPanel
{
	private OncComboBox instanceUsabilityStatus = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList stateMatrixRows = null;


	public  StateMatrixPanel() {
		super();
		editObj = new StateMatrix();
		initUI();
		fillUiHashtable();
	}


	public  StateMatrixPanel(StateMatrix editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (StateMatrix)editObj;
	initUI();
	}
	private void initUI() {
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		Object[] comboBoxList1 = {"ReadyForUse","InProgress","Retired"};
		instanceUsabilityStatus = new OncComboBox(editObj, "InstanceUsabilityStatus", true, comboBoxList1);
		stateMatrixRows = new OncScrollList(StateMatrixRow.class, editObj, "StateMatrixRows", true,true);
versionNumber.setBounds(250,0,125,60);
instanceUsabilityStatus.setBounds(250,60,125,60);
stateMatrixRows.setBounds(250,120,250,120);
versionNumber.setVisible(true);
instanceUsabilityStatus.setVisible(true);
stateMatrixRows.setVisible(true);
		add(versionNumber);
		add(instanceUsabilityStatus);
		add(stateMatrixRows);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(instanceUsabilityStatus, "instanceUsabilityStatus");
		uiHashtable.put(stateMatrixRows, "stateMatrixRows");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		StateMatrixPanel p = new StateMatrixPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
