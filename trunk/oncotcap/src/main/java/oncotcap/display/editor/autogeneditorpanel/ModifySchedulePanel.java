package oncotcap.display.editor.autogeneditorpanel;

import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.*;
import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.autogenpersistible.*;


public class ModifySchedulePanel extends DefaultEditorPanel
{
	private OncIntegerTextField nTimesChange = null;
	private OncTextField modificationType = null;
	private OncTextField creator = null;
	private OncTextField modifier = null;
	private OncTextField modificationTime = null;
	private OncScrollList timeChange = null;
	private OncTextField name = null;
	private OncTextField creationTime = null;
	private OncScrollList scheduleToModify = null;


	public  ModifySchedulePanel() {
		super();
		editObj = new ModifyScheduleAction();
		initUI();
		fillUiHashtable();
	}


	public  ModifySchedulePanel(ModifyScheduleAction editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (ModifyScheduleAction)editObj;
	initUI();
	}
	private void initUI() {
		scheduleToModify = new OncScrollList(ScheduleEventAction.class, editObj, "scheduleToModify", true,true, false);
		creationTime = new OncTextField(editObj, "creationTime", true);
		name = new OncTextField(editObj, "name", true);
		timeChange = new OncScrollList(OncTime.class, editObj, "timeChange", true,true, false);
		modificationTime = new OncTextField(editObj, "modificationTime", true);
		modifier = new OncTextField(editObj, "modifier", true);
		creator = new OncTextField(editObj, "creator", true);
		modificationType = new OncTextField(editObj, "modificationType", true);
		nTimesChange = new OncIntegerTextField(editObj, "nTimesChange", true);
scheduleToModify.setBounds(250,120,250,60);
creationTime.setBounds(0,0,250,60);
name.setBounds(250,0,250,60);
timeChange.setBounds(250,180,250,60);
modificationTime.setBounds(0,120,250,60);
modifier.setBounds(0,240,250,60);
creator.setBounds(0,60,250,60);
modificationType.setBounds(0,180,250,60);
nTimesChange.setBounds(250,60,125,60);
scheduleToModify.setVisible(true);
creationTime.setVisible(true);
name.setVisible(true);
timeChange.setVisible(true);
modificationTime.setVisible(true);
modifier.setVisible(true);
creator.setVisible(true);
modificationType.setVisible(true);
nTimesChange.setVisible(true);
		add(scheduleToModify);
		add(creationTime);
		add(name);
		add(timeChange);
		add(modificationTime);
		add(modifier);
		add(creator);
		add(modificationType);
		add(nTimesChange);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(scheduleToModify, "scheduleToModify");
		uiHashtable.put(creationTime, "creationTime");
		uiHashtable.put(name, "name");
		uiHashtable.put(timeChange, "timeChange");
		uiHashtable.put(modificationTime, "modificationTime");
		uiHashtable.put(modifier, "modifier");
		uiHashtable.put(creator, "creator");
		uiHashtable.put(modificationType, "modificationType");
		uiHashtable.put(nTimesChange, "nTimesChange");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		ModifySchedulePanel p = new ModifySchedulePanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
