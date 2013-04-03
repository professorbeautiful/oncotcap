package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class BrowserNodePanel extends DefaultEditorPanel
{
	private OncIntegerTextField versionNumber = null;


	public  BrowserNodePanel() {
		super();
		editObj = new BrowserNode();
		initUI();
		fillUiHashtable();
	}


	public  BrowserNodePanel(BrowserNode editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (BrowserNode)editObj;
	initUI();
	}
	private void initUI() {
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
versionNumber.setBounds(0,590,125,60);
versionNumber.setVisible(true);
		add(versionNumber);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(versionNumber, "versionNumber");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		BrowserNodePanel p = new BrowserNodePanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
