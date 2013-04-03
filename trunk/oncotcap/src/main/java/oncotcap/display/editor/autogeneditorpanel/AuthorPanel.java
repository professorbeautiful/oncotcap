package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class AuthorPanel extends DefaultEditorPanel
{
	private OncTextField name = null;


	public  AuthorPanel() {
		super();
		editObj = new Author();
		initUI();
		fillUiHashtable();
	}


	public  AuthorPanel(Author editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Author)editObj;
	initUI();
	}
	private void initUI() {
		name = new OncTextField(editObj, "Name", true);
name.setBounds(0,0,250,60);
name.setVisible(true);
		add(name);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(name, "name");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		AuthorPanel p = new AuthorPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
