package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class ValidationCriterionPanel extends DefaultEditorPanel
{


	public  ValidationCriterionPanel() {
		super();
		editObj = new ValidationCriterion();
		initUI();
		fillUiHashtable();
	}


	public  ValidationCriterionPanel(ValidationCriterion editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (ValidationCriterion)editObj;
	initUI();
	}
	private void initUI() {
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		ValidationCriterionPanel p = new ValidationCriterionPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
