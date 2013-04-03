package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class OntologyCategoryPanel extends DefaultEditorPanel
{
	private OncIntegerTextField minimumValue = null;
	private OncTextField name = null;
	private OncIntegerTextField maximumValue = null;


	public  OntologyCategoryPanel() {
		super();
		editObj = new OntologyCategory();
		initUI();
		fillUiHashtable();
	}


	public  OntologyCategoryPanel(OntologyCategory editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (OntologyCategory)editObj;
	initUI();
	}
	private void initUI() {
		maximumValue = new OncIntegerTextField(editObj, "MaximumValue", true);
		name = new OncTextField(editObj, "Name", true);
		minimumValue = new OncIntegerTextField(editObj, "MinimumValue", true);
maximumValue.setBounds(0,60,125,60);
name.setBounds(0,0,250,60);
minimumValue.setBounds(125,60,125,60);
maximumValue.setVisible(true);
name.setVisible(true);
minimumValue.setVisible(true);
		add(maximumValue);
		add(name);
		add(minimumValue);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(maximumValue, "maximumValue");
		uiHashtable.put(name, "name");
		uiHashtable.put(minimumValue, "minimumValue");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		OntologyCategoryPanel p = new OntologyCategoryPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
