package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class OncTimePanel extends DefaultEditorPanel
{
	private OncTextField time = null;
	private OncIntegerTextField versionNumber = null;
	private OncIntegerTextField timeUnit = null;


	public  OncTimePanel() {
		super();
		editObj = new OncTime();
		initUI();
		fillUiHashtable();
	}


	public  OncTimePanel(OncTime editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (OncTime)editObj;
	initUI();
	}
	private void initUI() {
		timeUnit = new OncIntegerTextField(editObj, "TimeUnit", true);
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		time = new OncTextField(editObj, "Time", true);
timeUnit.setBounds(250,60,125,60);
versionNumber.setBounds(375,60,125,60);
time.setBounds(250,0,250,60);
timeUnit.setVisible(true);
versionNumber.setVisible(true);
time.setVisible(true);
		add(timeUnit);
		add(versionNumber);
		add(time);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(timeUnit, "timeUnit");
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(time, "time");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		OncTimePanel p = new OncTimePanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
