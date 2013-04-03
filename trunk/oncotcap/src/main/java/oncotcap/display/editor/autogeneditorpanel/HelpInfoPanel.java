package oncotcap.display.editor.autogeneditorpanel;

import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.*;
import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;


public class HelpInfoPanel extends DefaultEditorPanel
{
	private OncTextField creator = null;
	private OncTextField modifier = null;
	private OncTextField shortDescription = null;
	private OncTextField modificationTime = null;
	private OncIntegerTextField type = null;
	private OncTextField creationTime = null;
	private OncTextField infoText = null;


	public  HelpInfoPanel() {
		super();
		editObj = new HelpInfo();
		initUI();
		fillUiHashtable();
	}


	public  HelpInfoPanel(HelpInfo editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (HelpInfo)editObj;
	initUI();
	}
	private void initUI() {
		infoText = new OncTextField(editObj, "infoText", true);
		creationTime = new OncTextField(editObj, "creationTime", true);
		type = new OncIntegerTextField(editObj, "type", true);
		modificationTime = new OncTextField(editObj, "modificationTime", true);
		shortDescription = new OncTextField(editObj, "shortDescription", true);
		modifier = new OncTextField(editObj, "modifier", true);
		creator = new OncTextField(editObj, "creator", true);
infoText.setBounds(0,120,250,60);
creationTime.setBounds(0,0,250,60);
type.setBounds(250,120,125,60);
modificationTime.setBounds(0,180,250,60);
shortDescription.setBounds(250,60,250,60);
modifier.setBounds(250,0,250,60);
creator.setBounds(0,60,250,60);
infoText.setVisible(true);
creationTime.setVisible(true);
type.setVisible(true);
modificationTime.setVisible(true);
shortDescription.setVisible(true);
modifier.setVisible(true);
creator.setVisible(true);
		add(infoText);
		add(creationTime);
		add(type);
		add(modificationTime);
		add(shortDescription);
		add(modifier);
		add(creator);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(infoText, "infoText");
		uiHashtable.put(creationTime, "creationTime");
		uiHashtable.put(type, "type");
		uiHashtable.put(modificationTime, "modificationTime");
		uiHashtable.put(shortDescription, "shortDescription");
		uiHashtable.put(modifier, "modifier");
		uiHashtable.put(creator, "creator");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		HelpInfoPanel p = new HelpInfoPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
