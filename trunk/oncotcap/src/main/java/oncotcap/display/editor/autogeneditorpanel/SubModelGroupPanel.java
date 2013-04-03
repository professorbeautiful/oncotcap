package oncotcap.display.editor.autogeneditorpanel;

import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.*;
import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;


public class SubModelGroupPanel extends DefaultEditorPanel
{
	private OncTextField creator = null;
	private OncTextField modifier = null;
	private OncScrollList modelControllersUsingMe = null;
	private OncTextField modificationTime = null;
	private OncScrollList submodelsInGroup = null;
	private OncTextField name = null;
	private OncTextField creationTime = null;
	private OncComboBox modelType = null;
	private OncScrollList keywords = null;




	public  SubModelGroupPanel() {
		super();
		editObj = new SubModelGroup();
		initUI();
		fillUiHashtable();
	}


	public  SubModelGroupPanel(SubModelGroup editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (SubModelGroup)editObj;
	initUI();
	}
	private void initUI() {
		creationTime = new OncTextField(editObj, "creationTime", true);
		name = new OncTextField(editObj, "Name", true);
		modificationTime = new OncTextField(editObj, "modificationTime", true);
		modifier = new OncTextField(editObj, "modifier", true);
		creator = new OncTextField(editObj, "creator", true);
		modelControllersUsingMe = new OncScrollList(ModelController.class, editObj, "Model Controllers Using Me", true,true);
		submodelsInGroup = new OncScrollList(SubModel.class, editObj, "Submodels In Group", true,true);
	Object[] comboBoxList1 = {"Bio","Setting","Validation"};
		modelType = new OncComboBox(editObj, "Type", true, comboBoxList1);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,
																 true);		

creationTime.setBounds(10,530,250,60);
name.setBounds(10,0,670,45);
modelType.setBounds(10,50,125,60);
modificationTime.setBounds(290,530,250,60);
modifier.setBounds(290,460,250,60);
creator.setBounds(10,460,250,60);
modelControllersUsingMe.setBounds(330,100,400,200);
submodelsInGroup.setBounds(10,100,300,200);
keywords.setBounds(10,320,250,120);

creationTime.setVisible(false);
name.setVisible(true);
modificationTime.setVisible(false);
modifier.setVisible(false);
creator.setVisible(false);
modelType.setVisible(true);
modelControllersUsingMe.setVisible(true);
submodelsInGroup.setVisible(true);
keywords.setVisible(true);

		add(creationTime);
		add(name);
		add(modificationTime);
		add(modifier);
		add(creator);
		add(modelControllersUsingMe);
		add(submodelsInGroup);
		add(modelType);
		add(keywords);

	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(creationTime, "creationTime");
		uiHashtable.put(name, "name");
		uiHashtable.put(modificationTime, "modificationTime");
		uiHashtable.put(modifier, "modifier");
		uiHashtable.put(creator, "creator");
		uiHashtable.put(modelControllersUsingMe, "modelControllersUsingMe");
		uiHashtable.put(submodelsInGroup, "submodelsInGroup");
		uiHashtable.put(modelType, "modelType");
		uiHashtable.put(keywords, "keywords");

	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		SubModelGroupPanel p = new SubModelGroupPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
