package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class ModelUIDEntityPanel extends DefaultEditorPanel
{


	public  ModelUIDEntityPanel() {
		super();
		editObj = new ModelUIDEntity();
		initUI();
		fillUiHashtable();
	}


	public  ModelUIDEntityPanel(ModelUIDEntity editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (ModelUIDEntity)editObj;
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
		ModelUIDEntityPanel p = new ModelUIDEntityPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
