package oncotcap.display.editor.autogeneditorpanel;

import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.*;
import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.autogenpersistible.*;


public class DeclareEnumPickerPanel extends DefaultEditorPanel
{
	private OncScrollList singleParameterList = null;
	private OncTextField creator = null;
	private OncTextField modifier = null;
	private OncTextField modificationTime = null;
	private OncTextField name = null;
	private OncTextField creationTime = null;


	public  DeclareEnumPickerPanel() {
		super();
		editObj = new DeclareEnumPicker();
		initUI();
		fillUiHashtable();
	}


	public  DeclareEnumPickerPanel(DeclareEnumPicker editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (DeclareEnumPicker)editObj;
	initUI();
	}
	private void initUI() {
		creationTime = new OncTextField(editObj, "creationTime", true);
		name = new OncTextField(editObj, "name", true);
		modificationTime = new OncTextField(editObj, "modificationTime", true);
		modifier = new OncTextField(editObj, "modifier", true);
		creator = new OncTextField(editObj, "creator", true);
		singleParameterList = new OncScrollList(SingleParameter.class, editObj, "singleParameterList", true,true);
creationTime.setBounds(0,780,250,60);
name.setBounds(0,1020,250,60);
modificationTime.setBounds(0,900,250,60);
modifier.setBounds(0,960,250,60);
creator.setBounds(0,840,250,60);
singleParameterList.setBounds(0,660,250,120);
creationTime.setVisible(true);
name.setVisible(true);
modificationTime.setVisible(true);
modifier.setVisible(true);
creator.setVisible(true);
singleParameterList.setVisible(true);
		add(creationTime);
		add(name);
		add(modificationTime);
		add(modifier);
		add(creator);
		add(singleParameterList);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(creationTime, "creationTime");
		uiHashtable.put(name, "name");
		uiHashtable.put(modificationTime, "modificationTime");
		uiHashtable.put(modifier, "modifier");
		uiHashtable.put(creator, "creator");
		uiHashtable.put(singleParameterList, "singleParameterList");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		DeclareEnumPickerPanel p = new DeclareEnumPickerPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
