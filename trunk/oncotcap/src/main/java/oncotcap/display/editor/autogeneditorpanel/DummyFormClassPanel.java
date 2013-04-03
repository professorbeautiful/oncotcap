package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class DummyFormClassPanel extends DefaultEditorPanel
{
	private OncScrollList dummyField = null;


	public  DummyFormClassPanel() {
		super();
		editObj = new DummyFormClass();
		initUI();
		fillUiHashtable();
	}


	public  DummyFormClassPanel(DummyFormClass editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (DummyFormClass)editObj;
	initUI();
	}
	private void initUI() {
		dummyField = new OncScrollList(DefaultPersistibleList.class, editObj, "DummyField", true,true, false);
dummyField.setBounds(0,0,250,60);
dummyField.setVisible(true);
		add(dummyField);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(dummyField, "dummyField");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		DummyFormClassPanel p = new DummyFormClassPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
