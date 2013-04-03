package oncotcap.display.editor.persistibleeditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class StateMatrixRowPanel extends DefaultEditorPanel
{
	private OncScrollList rowColumns = null;
	private OncComboBox instanceUsabilityStatus = null;
	private OncIntegerTextField versionNumber = null;


	public  StateMatrixRowPanel() {
		super();
		editObj = new StateMatrixRow();
		initUI();
		fillUiHashtable();
	}


	public  StateMatrixRowPanel(StateMatrixRow editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (StateMatrixRow)editObj;
	initUI();
	}
	private void initUI() {
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		Object[] comboBoxList1 = {"ReadyForUse","InProgress","Retired"};
		instanceUsabilityStatus = new OncComboBox(editObj, "InstanceUsabilityStatus", true, comboBoxList1);
		rowColumns = new OncScrollList(Persistible.class, editObj, "RowColumns", true,true);
versionNumber.setBounds(250,0,125,60);
instanceUsabilityStatus.setBounds(250,60,125,60);
rowColumns.setBounds(250,120,250,120);
versionNumber.setVisible(true);
instanceUsabilityStatus.setVisible(true);
rowColumns.setVisible(true);
		add(versionNumber);
		add(instanceUsabilityStatus);
		add(rowColumns);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(instanceUsabilityStatus, "instanceUsabilityStatus");
		uiHashtable.put(rowColumns, "rowColumns");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		StateMatrixRowPanel p = new StateMatrixRowPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
