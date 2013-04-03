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


public class ScheduleEventPanel extends DefaultEditorPanel
{
	private OncScrollList oncEvent = null;
	private OncTextField dayList = null;
	private OncTextField creator = null;
	private OncTextField modifier = null;
	private OncIntegerTextField eventType = null;
	private OncTextField modificationTime = null;
	private OncScrollList gapTime = null;
	private OncTextField numberOfTimesUntilEnd = null;
	private OncScrollableTextArea isDayList = null;
	private OncTextField creationTime = null;
	private OncScrollableTextArea recur = null;
	private OncScrollList startDelayTime = null;
	private OncScrollList oncMethod = null;
	private OncTextField scheduleEndType = null;
	private OncScrollList endDelayTime = null;
	private OncScrollList oncProcess = null;
	private OncTextField name = null;
	private OncTextField scheduleStartType = null;


	public  ScheduleEventPanel() {
		super();
		editObj = new ScheduleEventAction();
		initUI();
		fillUiHashtable();
	}


	public  ScheduleEventPanel(ScheduleEventAction editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (ScheduleEventAction)editObj;
	initUI();
	}
	private void initUI() {
		scheduleStartType = new OncTextField(editObj, "scheduleStartType", true);
		name = new OncTextField(editObj, "name", true);
		oncProcess = new OncScrollList(ProcessDeclaration.class, editObj, "oncProcess", true,true, false);
		endDelayTime = new OncScrollList(OncTime.class, editObj, "endDelayTime", true,true, false);
		scheduleEndType = new OncTextField(editObj, "scheduleEndType", true);
		oncMethod = new OncScrollList(DefaultPersistibleList.class, editObj, "oncMethod", true,true, false);
		startDelayTime = new OncScrollList(OncTime.class, editObj, "startDelayTime", true,true, false);
		recur = new OncScrollableTextArea(editObj, "recur", true);
		creationTime = new OncTextField(editObj, "creationTime", true);
		isDayList = new OncScrollableTextArea(editObj, "isDayList", true);
		numberOfTimesUntilEnd = new OncTextField(editObj, "numberOfTimesUntilEnd", true);
		gapTime = new OncScrollList(OncTime.class, editObj, "gapTime", true,true, false);
		modificationTime = new OncTextField(editObj, "modificationTime", true);
		eventType = new OncIntegerTextField(editObj, "eventType", true);
		modifier = new OncTextField(editObj, "modifier", true);
		creator = new OncTextField(editObj, "creator", true);
		dayList = new OncTextField(editObj, "dayList", true);
		oncEvent = new OncScrollList(EventDeclaration.class, editObj, "oncEvent", true,true, false);
scheduleStartType.setBounds(0,480,250,60);
name.setBounds(0,300,250,60);
oncProcess.setBounds(250,360,250,60);
endDelayTime.setBounds(250,120,250,60);
scheduleEndType.setBounds(0,420,250,60);
oncMethod.setBounds(250,300,250,60);
startDelayTime.setBounds(250,420,250,60);
recur.setBounds(375,0,125,60);
creationTime.setBounds(0,0,250,60);
isDayList.setBounds(250,0,125,60);
numberOfTimesUntilEnd.setBounds(0,360,250,60);
gapTime.setBounds(250,180,250,60);
modificationTime.setBounds(0,180,250,60);
eventType.setBounds(250,60,125,60);
modifier.setBounds(0,240,250,60);
creator.setBounds(0,60,250,60);
dayList.setBounds(0,120,250,60);
oncEvent.setBounds(250,240,250,60);
scheduleStartType.setVisible(true);
name.setVisible(true);
oncProcess.setVisible(true);
endDelayTime.setVisible(true);
scheduleEndType.setVisible(true);
oncMethod.setVisible(true);
startDelayTime.setVisible(true);
recur.setVisible(true);
creationTime.setVisible(true);
isDayList.setVisible(true);
numberOfTimesUntilEnd.setVisible(true);
gapTime.setVisible(true);
modificationTime.setVisible(true);
eventType.setVisible(true);
modifier.setVisible(true);
creator.setVisible(true);
dayList.setVisible(true);
oncEvent.setVisible(true);
		add(scheduleStartType);
		add(name);
		add(oncProcess);
		add(endDelayTime);
		add(scheduleEndType);
		add(oncMethod);
		add(startDelayTime);
		add(recur);
		add(creationTime);
		add(isDayList);
		add(numberOfTimesUntilEnd);
		add(gapTime);
		add(modificationTime);
		add(eventType);
		add(modifier);
		add(creator);
		add(dayList);
		add(oncEvent);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(scheduleStartType, "scheduleStartType");
		uiHashtable.put(name, "name");
		uiHashtable.put(oncProcess, "oncProcess");
		uiHashtable.put(endDelayTime, "endDelayTime");
		uiHashtable.put(scheduleEndType, "scheduleEndType");
		uiHashtable.put(oncMethod, "oncMethod");
		uiHashtable.put(startDelayTime, "startDelayTime");
		uiHashtable.put(recur, "recur");
		uiHashtable.put(creationTime, "creationTime");
		uiHashtable.put(isDayList, "isDayList");
		uiHashtable.put(numberOfTimesUntilEnd, "numberOfTimesUntilEnd");
		uiHashtable.put(gapTime, "gapTime");
		uiHashtable.put(modificationTime, "modificationTime");
		uiHashtable.put(eventType, "eventType");
		uiHashtable.put(modifier, "modifier");
		uiHashtable.put(creator, "creator");
		uiHashtable.put(dayList, "dayList");
		uiHashtable.put(oncEvent, "oncEvent");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		ScheduleEventPanel p = new ScheduleEventPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
