package oncotcap.display.editor.autogeneditorpanel;

import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.*;
import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.autogenpersistible.*;


public class ModifySwitchablePanel extends DefaultEditorPanel
{
	private OncScrollableTextArea switchState = null;
	private OncTextField creator = null;
	private OncTextField modifier = null;
	private OncTextField modificationTime = null;
	private OncScrollList variable = null;
	private OncTextField name = null;
	private OncTextField creationTime = null;


	public  ModifySwitchablePanel() {
		super();
		editObj = new ModifySwitchable();
		initUI();
		fillUiHashtable();
	}


	public  ModifySwitchablePanel(ModifySwitchable editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (ModifySwitchable)editObj;
	initUI();
	}
	private void initUI() {
		creationTime = new OncTextField(editObj, "creationTime", true);
		name = new OncTextField(editObj, "name", true);
		variable = new OncScrollList(DeclareVariable.class, editObj, "variable", true,true, false);
		modificationTime = new OncTextField(editObj, "modificationTime", true);
		modifier = new OncTextField(editObj, "modifier", true);
		creator = new OncTextField(editObj, "creator", true);
		switchState = new OncScrollableTextArea(editObj, "switchState", true);
creationTime.setBounds(0,0,250,60);
name.setBounds(250,0,250,60);
variable.setBounds(250,120,250,60);
modificationTime.setBounds(0,120,250,60);
modifier.setBounds(0,180,250,60);
creator.setBounds(0,60,250,60);
switchState.setBounds(250,60,125,60);
creationTime.setVisible(true);
name.setVisible(true);
variable.setVisible(true);
modificationTime.setVisible(true);
modifier.setVisible(true);
creator.setVisible(true);
switchState.setVisible(true);
		add(creationTime);
		add(name);
		add(variable);
		add(modificationTime);
		add(modifier);
		add(creator);
		add(switchState);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(creationTime, "creationTime");
		uiHashtable.put(name, "name");
		uiHashtable.put(variable, "variable");
		uiHashtable.put(modificationTime, "modificationTime");
		uiHashtable.put(modifier, "modifier");
		uiHashtable.put(creator, "creator");
		uiHashtable.put(switchState, "switchState");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		ModifySwitchablePanel p = new ModifySwitchablePanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
