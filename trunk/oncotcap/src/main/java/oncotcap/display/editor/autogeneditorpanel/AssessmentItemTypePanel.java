package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class AssessmentItemTypePanel extends DefaultEditorPanel
{
	private OncTextField description = null;
	private OncTextField name = null;


	public  AssessmentItemTypePanel() {
		super();
		editObj = new AssessmentItemType();
		initUI();
		fillUiHashtable();
	}


	public  AssessmentItemTypePanel(AssessmentItemType editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (AssessmentItemType)editObj;
	initUI();
	}
	private void initUI() {
		name = new OncTextField(editObj, "Name", true);
		description = new OncTextField(editObj, "Description", true);
name.setBounds(0,60,250,60);
description.setBounds(0,0,250,60);
name.setVisible(true);
description.setVisible(true);
		add(name);
		add(description);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(name, "name");
		uiHashtable.put(description, "description");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		AssessmentItemTypePanel p = new AssessmentItemTypePanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
