package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class FullSourceProviderPanel extends DefaultEditorPanel
{
	private OncTextField name = null;
	private OncScrollableTextArea sourceLocatorMethod = null;


	public  FullSourceProviderPanel() {
		super();
		editObj = new FullSourceProvider();
		initUI();
		fillUiHashtable();
	}


	public  FullSourceProviderPanel(FullSourceProvider editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (FullSourceProvider)editObj;
	initUI();
	}
	private void initUI() {
		sourceLocatorMethod = new OncScrollableTextArea(editObj, "SourceLocatorMethod", true);
		name = new OncTextField(editObj, "Name", true);
sourceLocatorMethod.setBounds(0,60,250,260);
name.setBounds(0,0,250,60);
sourceLocatorMethod.setVisible(true);
name.setVisible(true);
		add(sourceLocatorMethod);
		add(name);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(sourceLocatorMethod, "sourceLocatorMethod");
		uiHashtable.put(name, "name");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		FullSourceProviderPanel p = new FullSourceProviderPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
