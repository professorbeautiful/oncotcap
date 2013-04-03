package oncotcap.display.editor.autogeneditorpanel;

import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.*;
import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.autogenpersistible.*;


public class CodeBundlePanel extends DefaultEditorPanel
{
	private OncScrollList oncEvent = null;
	private OncIntegerTextField eventType = null;
	private OncTextField modifier = null;
	private OncTextField creator = null;
	private OncIntegerTextField orderHint = null;
	private OncTextField modificationTime = null;
	private OncTextField IfClause = null;
	private OncTextField creationTime = null;
	private OncScrollList oncMethod = null;
	private OncScrollList oncProcess = null;
	private OncTextField name = null;
	private OncScrollList actions = null;
	private OncScrollList statementBundlesContainingMe = null;


	public  CodeBundlePanel() {
		super();
		editObj = new CodeBundle();
		initUI();
		fillUiHashtable();
	}


	public  CodeBundlePanel(CodeBundle editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (CodeBundle)editObj;
	initUI();
	}
	private void initUI() {
		name = new OncTextField(editObj, "name", true);
		oncProcess = new OncScrollList(ProcessDeclaration.class, editObj, "oncProcess", true,true, false);
		oncMethod = new OncScrollList(DefaultPersistibleList.class, editObj, "oncMethod", true,true, false);
		creationTime = new OncTextField(editObj, "creationTime", true);
		IfClause = new OncTextField(editObj, "IfClause", true);
		modificationTime = new OncTextField(editObj, "modificationTime", true);
		orderHint = new OncIntegerTextField(editObj, "orderHint", true);
		creator = new OncTextField(editObj, "creator", true);
		modifier = new OncTextField(editObj, "modifier", true);
		eventType = new OncIntegerTextField(editObj, "eventType", true);
		oncEvent = new OncScrollList(EventDeclaration.class, editObj, "oncEvent", true,true, false);
		actions = new OncScrollList(OncAction.class, editObj, "actions", true,true);
		statementBundlesContainingMe = new OncScrollList(StatementTemplate.class, editObj, "statementBundlesContainingMe", true,true);
name.setBounds(10,0,670,60);
oncProcess.setBounds(10,190,250,60);
oncMethod.setBounds(0,830,250,60);
creationTime.setBounds(10,530,250,60);
IfClause.setBounds(10,260,670,60);
modificationTime.setBounds(290,530,250,60);
orderHint.setBounds(125,710,125,60);
creator.setBounds(10,460,250,60);
modifier.setBounds(290,460,250,60);
eventType.setBounds(280,190,125,60);
oncEvent.setBounds(430,190,250,60);
actions.setBounds(290,330,390,120);
statementBundlesContainingMe.setBounds(10,70,670,120);
name.setVisible(true);
oncProcess.setVisible(true);
oncMethod.setVisible(true);
creationTime.setVisible(true);
IfClause.setVisible(true);
modificationTime.setVisible(true);
orderHint.setVisible(true);
creator.setVisible(true);
modifier.setVisible(true);
eventType.setVisible(true);
oncEvent.setVisible(true);
actions.setVisible(true);
statementBundlesContainingMe.setVisible(true);
		add(name);
		add(oncProcess);
		add(oncMethod);
		add(creationTime);
		add(IfClause);
		add(modificationTime);
		add(orderHint);
		add(creator);
		add(modifier);
		add(eventType);
		add(oncEvent);
		add(actions);
		add(statementBundlesContainingMe);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(name, "name");
		uiHashtable.put(oncProcess, "oncProcess");
		uiHashtable.put(oncMethod, "oncMethod");
		uiHashtable.put(creationTime, "creationTime");
		uiHashtable.put(IfClause, "IfClause");
		uiHashtable.put(modificationTime, "modificationTime");
		uiHashtable.put(orderHint, "orderHint");
		uiHashtable.put(creator, "creator");
		uiHashtable.put(modifier, "modifier");
		uiHashtable.put(eventType, "eventType");
		uiHashtable.put(oncEvent, "oncEvent");
		uiHashtable.put(actions, "actions");
		uiHashtable.put(statementBundlesContainingMe, "statementBundlesContainingMe");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		CodeBundlePanel p = new CodeBundlePanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
