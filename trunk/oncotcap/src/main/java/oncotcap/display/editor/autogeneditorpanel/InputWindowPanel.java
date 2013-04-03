package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class InputWindowPanel extends DefaultEditorPanel
{
	private OncScrollList instantiationParameters = null;
	private OncTextField name = null;


	public  InputWindowPanel() {
		super();
		editObj = new InputWindow();
		initUI();
		fillUiHashtable();
	}


	public  InputWindowPanel(InputWindow editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (InputWindow)editObj;
	initUI();
	}
	private void initUI() {
		name = new OncTextField(editObj, "Name", true);
		instantiationParameters = new OncScrollList(String.class, editObj, "InstantiationParameters", true,true);
name.setBounds(0,0,250,60);
instantiationParameters.setBounds(0,60,250,120);
name.setVisible(true);
instantiationParameters.setVisible(true);
		add(name);
		add(instantiationParameters);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(name, "name");
		uiHashtable.put(instantiationParameters, "instantiationParameters");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		InputWindowPanel p = new InputWindowPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}