package oncotcap.display.editor.autogeneditorpanel;

import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.*;
import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;


public class TimestampablePanel extends DefaultEditorPanel
{
	private OncTextField creator = null;
	private OncTextField modifier = null;
	private OncTextField modificationTime = null;
	private OncTextField creationTime = null;


	public  TimestampablePanel() {
		super();
		editObj = new Timestampable();
		initUI();
		fillUiHashtable();
	}


	public  TimestampablePanel(Timestampable editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Timestampable)editObj;
	initUI();
	}
	private void initUI() {
		creationTime = new OncTextField(editObj, "creationTime", true);
		modificationTime = new OncTextField(editObj, "modificationTime", true);
		modifier = new OncTextField(editObj, "modifier", true);
		creator = new OncTextField(editObj, "creator", true);
creationTime.setBounds(0,0,250,60);
modificationTime.setBounds(0,120,250,60);
modifier.setBounds(250,0,250,60);
creator.setBounds(0,60,250,60);
creationTime.setVisible(true);
modificationTime.setVisible(true);
modifier.setVisible(true);
creator.setVisible(true);
		add(creationTime);
		add(modificationTime);
		add(modifier);
		add(creator);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(creationTime, "creationTime");
		uiHashtable.put(modificationTime, "modificationTime");
		uiHashtable.put(modifier, "modifier");
		uiHashtable.put(creator, "creator");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		TimestampablePanel p = new TimestampablePanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
