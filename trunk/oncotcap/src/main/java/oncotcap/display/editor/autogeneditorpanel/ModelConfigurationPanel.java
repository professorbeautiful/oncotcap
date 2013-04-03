package oncotcap.display.editor.autogeneditorpanel;

import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.*;
import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;


public class ModelConfigurationPanel extends DefaultEditorPanel
{
	private OncScrollList statementBundleConfigurations = null;
	private OncTextField creator = null;
	private OncTextField modifier = null;
	private OncTextField modificationTime = null;
	private OncTextField creationTime = null;
	private OncScrollList visibleBundles = null;


	public  ModelConfigurationPanel() {
		super();
		editObj = new ModelConfiguration();
		initUI();
		fillUiHashtable();
	}


	public  ModelConfigurationPanel(ModelConfiguration editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (ModelConfiguration)editObj;
	initUI();
	}
	private void initUI() {
		visibleBundles = new OncScrollList(OncTreeNode.class, editObj, "visibleBundles", true,true, false);
		creationTime = new OncTextField(editObj, "creationTime", true);
		modificationTime = new OncTextField(editObj, "modificationTime", true);
		modifier = new OncTextField(editObj, "modifier", true);
		creator = new OncTextField(editObj, "creator", true);
		statementBundleConfigurations = new OncScrollList(StatementBundleConfiguration.class, editObj, "statementBundleConfigurations", true,true);
visibleBundles.setBounds(250,60,250,60);
creationTime.setBounds(0,0,250,60);
modificationTime.setBounds(0,120,250,60);
modifier.setBounds(0,180,250,60);
creator.setBounds(0,60,250,60);
statementBundleConfigurations.setBounds(250,120,250,120);
visibleBundles.setVisible(true);
creationTime.setVisible(true);
modificationTime.setVisible(true);
modifier.setVisible(true);
creator.setVisible(true);
statementBundleConfigurations.setVisible(true);
		add(visibleBundles);
		add(creationTime);
		add(modificationTime);
		add(modifier);
		add(creator);
		add(statementBundleConfigurations);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(visibleBundles, "visibleBundles");
		uiHashtable.put(creationTime, "creationTime");
		uiHashtable.put(modificationTime, "modificationTime");
		uiHashtable.put(modifier, "modifier");
		uiHashtable.put(creator, "creator");
		uiHashtable.put(statementBundleConfigurations, "statementBundleConfigurations");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		ModelConfigurationPanel p = new ModelConfigurationPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
