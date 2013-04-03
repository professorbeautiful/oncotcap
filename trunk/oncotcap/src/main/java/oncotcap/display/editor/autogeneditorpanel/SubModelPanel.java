package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class SubModelPanel extends DefaultEditorPanel
{
	private OncScrollList encodingsInMe = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList submodelGroupsIJoin = null;
	private OncTextField name = null;
	private OncScrollList keywords = null;
	private OncComboBox modelType = null;



	public  SubModelPanel() {
		super();
		editObj = new SubModel();
		initUI();
		fillUiHashtable();
	}


	public  SubModelPanel(SubModel editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (SubModel)editObj;
	initUI();
	}
	private void initUI() {
		name = new OncTextField(editObj, "Name", true);
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		encodingsInMe = new OncScrollList(Encoding.class, editObj, "EdictsInMe", true,true);
		submodelGroupsIJoin = new OncScrollList(SubModelGroup.class, editObj, "SubmodelGroupsIJoin", true,true);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,
																 true);		
		Object[] comboBoxList1 = {"Bio","Setting","Validation"};
		modelType = new OncComboBox(editObj, "Type", true, comboBoxList1);

name.setBounds(10,0,670,40);
modelType.setBounds(10,50,125,60);
versionNumber.setBounds(10,410,125,60);
encodingsInMe.setBounds(10,100,670,170);
submodelGroupsIJoin.setBounds(10,280,410,120);
keywords.setBounds(430,280,250,120);

name.setVisible(true);
modelType.setVisible(true);
versionNumber.setVisible(true);
encodingsInMe.setVisible(true);
submodelGroupsIJoin.setVisible(true);
keywords.setVisible(true);
		add(name);
		add(versionNumber);
		add(encodingsInMe);
		add(submodelGroupsIJoin);
		add(keywords);
		add(modelType);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(name, "name");
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(encodingsInMe, "encodingsInMe");
		uiHashtable.put(submodelGroupsIJoin, "submodelGroupsIJoin");
		uiHashtable.put(keywords, "keywords");
		uiHashtable.put(modelType, "modelType");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		SubModelPanel p = new SubModelPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
