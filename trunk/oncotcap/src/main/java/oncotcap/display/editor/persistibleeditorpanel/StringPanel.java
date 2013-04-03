package oncotcap.display.editor.persistibleeditorpanel;

import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.common.*;

import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;


public class StringPanel extends DefaultEditorPanel
{
	public  String editObj = null;
	private OncTextField aString = null;


	public  StringPanel() {
		super();
		editObj = new String();
		initUI();
		fillUiHashtable();
	}


	public  StringPanel(String editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
			this.editObj = (String)editObj;
			initUI();
	}

	private void initUI() {
		aString = new OncTextField(editObj, "aString", true);
		aString.setBounds(0,0,250,60);
		aString.setVisible(true);
		add(aString);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(aString, "aString");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		StringPanel p = new StringPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
