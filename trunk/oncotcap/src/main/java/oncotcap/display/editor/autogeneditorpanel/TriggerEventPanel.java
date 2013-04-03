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


public class TriggerEventPanel extends DefaultEditorPanel
{
	private OncScrollList oncEvent = null;
	private OncTextField creator = null;
	private OncTextField modifier = null;
	private OncIntegerTextField eventType = null;
	private OncTextField modificationTime = null;
	private OncScrollList oncProcess = null;
	private OncTextField name = null;
	private OncTextField creationTime = null;
	private OncScrollList oncMethod = null;


	public  TriggerEventPanel() {
		super();
		editObj = new TriggerEventAction();
		initUI();
		fillUiHashtable();
	}


	public  TriggerEventPanel(TriggerEventAction editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (TriggerEventAction)editObj;
	initUI();
	}
	private void initUI() {
		oncMethod = new OncScrollList(DefaultPersistibleList.class, editObj, "oncMethod", true,true, false);
		creationTime = new OncTextField(editObj, "creationTime", true);
		name = new OncTextField(editObj, "name", true);
		oncProcess = new OncScrollList(ProcessDeclaration.class, editObj, "oncProcess", true,true, false);
		modificationTime = new OncTextField(editObj, "modificationTime", true);
		eventType = new OncIntegerTextField(editObj, "eventType", true);
		modifier = new OncTextField(editObj, "modifier", true);
		creator = new OncTextField(editObj, "creator", true);
		oncEvent = new OncScrollList(EventDeclaration.class, editObj, "oncEvent", true,true, false);
oncMethod.setBounds(250,120,250,60);
creationTime.setBounds(0,0,250,60);
name.setBounds(0,240,250,60);
oncProcess.setBounds(250,180,250,60);
modificationTime.setBounds(0,120,250,60);
eventType.setBounds(250,0,125,60);
modifier.setBounds(0,180,250,60);
creator.setBounds(0,60,250,60);
oncEvent.setBounds(250,60,250,60);
oncMethod.setVisible(true);
creationTime.setVisible(true);
name.setVisible(true);
oncProcess.setVisible(true);
modificationTime.setVisible(true);
eventType.setVisible(true);
modifier.setVisible(true);
creator.setVisible(true);
oncEvent.setVisible(true);
		add(oncMethod);
		add(creationTime);
		add(name);
		add(oncProcess);
		add(modificationTime);
		add(eventType);
		add(modifier);
		add(creator);
		add(oncEvent);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(oncMethod, "oncMethod");
		uiHashtable.put(creationTime, "creationTime");
		uiHashtable.put(name, "name");
		uiHashtable.put(oncProcess, "oncProcess");
		uiHashtable.put(modificationTime, "modificationTime");
		uiHashtable.put(eventType, "eventType");
		uiHashtable.put(modifier, "modifier");
		uiHashtable.put(creator, "creator");
		uiHashtable.put(oncEvent, "oncEvent");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		TriggerEventPanel p = new TriggerEventPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
