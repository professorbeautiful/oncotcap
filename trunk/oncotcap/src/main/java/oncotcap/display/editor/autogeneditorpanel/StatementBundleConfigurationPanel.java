package oncotcap.display.editor.autogeneditorpanel;

import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.*;
import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;


public class StatementBundleConfigurationPanel extends DefaultEditorPanel
{
	private OncScrollableTextArea visible = null;
	private OncScrollList statementBundles = null;
	private OncScrollableTextArea required = null;
	private OncTextField creator = null;
	private OncTextField modifier = null;
	private OncTextField modificationTime = null;
	private OncScrollableTextArea multiple = null;
	private OncTextField creationTime = null;


	public  StatementBundleConfigurationPanel() {
		super();
		editObj = new StatementBundleConfiguration();
		initUI();
		fillUiHashtable();
	}


	public  StatementBundleConfigurationPanel(StatementBundleConfiguration editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (StatementBundleConfiguration)editObj;
	initUI();
	}
	private void initUI() {
		creationTime = new OncTextField(editObj, "creationTime", true);
		multiple = new OncScrollableTextArea(editObj, "multiple", true);
		modificationTime = new OncTextField(editObj, "modificationTime", true);
		modifier = new OncTextField(editObj, "modifier", true);
		creator = new OncTextField(editObj, "creator", true);
		required = new OncScrollableTextArea(editObj, "required", true);
		visible = new OncScrollableTextArea(editObj, "visible", true);
		statementBundles = new OncScrollList(StatementBundle.class, editObj, "statementBundles", true,true);
creationTime.setBounds(0,0,250,60);
multiple.setBounds(250,0,125,60);
modificationTime.setBounds(0,120,250,60);
modifier.setBounds(0,180,250,60);
creator.setBounds(0,60,250,60);
required.setBounds(375,0,125,60);
visible.setBounds(250,60,125,60);
statementBundles.setBounds(250,120,250,60);
creationTime.setVisible(true);
multiple.setVisible(true);
modificationTime.setVisible(true);
modifier.setVisible(true);
creator.setVisible(true);
required.setVisible(true);
visible.setVisible(true);
statementBundles.setVisible(true);
		add(creationTime);
		add(multiple);
		add(modificationTime);
		add(modifier);
		add(creator);
		add(required);
		add(visible);
		add(statementBundles);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(creationTime, "creationTime");
		uiHashtable.put(multiple, "multiple");
		uiHashtable.put(modificationTime, "modificationTime");
		uiHashtable.put(modifier, "modifier");
		uiHashtable.put(creator, "creator");
		uiHashtable.put(required, "required");
		uiHashtable.put(visible, "visible");
		uiHashtable.put(statementBundles, "statementBundles");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		StatementBundleConfigurationPanel p = new StatementBundleConfigurationPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
