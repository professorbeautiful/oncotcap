package oncotcap.display.editor.autogeneditorpanel;

import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.*;
import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;


public class EnumLevelListPanel extends DefaultEditorPanel
{
	private OncScrollList levelList = null;
	private OncTextField creator = null;
	private OncTextField modifier = null;
	private OncTextField modificationTime = null;
	private OncTextField name = null;
	private OncTextField creationTime = null;


	public  EnumLevelListPanel() {
		super();
		editObj = new EnumLevelList();
		initUI();
		fillUiHashtable();
	}


	public  EnumLevelListPanel(EnumLevelList editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (EnumLevelList)editObj;
	initUI();
	}
	private void initUI() {
		creationTime = new OncTextField(editObj, "creationTime", true);
		name = new OncTextField(editObj, "name", true);
		modificationTime = new OncTextField(editObj, "modificationTime", true);
		modifier = new OncTextField(editObj, "modifier", true);
		creator = new OncTextField(editObj, "creator", true);
		levelList = new OncScrollList(EnumLevel.class, editObj, "levelList", true,true);
creationTime.setBounds(0,420,250,60);
name.setBounds(0,120,250,60);
modificationTime.setBounds(0,540,250,60);
modifier.setBounds(0,600,250,60);
creator.setBounds(0,480,250,60);
levelList.setBounds(0,300,250,120);
creationTime.setVisible(true);
name.setVisible(true);
modificationTime.setVisible(true);
modifier.setVisible(true);
creator.setVisible(true);
levelList.setVisible(true);
		add(creationTime);
		add(name);
		add(modificationTime);
		add(modifier);
		add(creator);
		add(levelList);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(creationTime, "creationTime");
		uiHashtable.put(name, "name");
		uiHashtable.put(modificationTime, "modificationTime");
		uiHashtable.put(modifier, "modifier");
		uiHashtable.put(creator, "creator");
		uiHashtable.put(levelList, "levelList");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		EnumLevelListPanel p = new EnumLevelListPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
