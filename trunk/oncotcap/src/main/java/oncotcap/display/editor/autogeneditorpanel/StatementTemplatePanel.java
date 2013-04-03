package oncotcap.display.editor.autogeneditorpanel;

import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.*;
import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.autogenpersistible.*;


public class StatementTemplatePanel extends DefaultEditorPanel
{
	private OncScrollList parameter = null;
	private OncScrollList statementBundles = null;
	private OncScrollList codeBundles = null;
	private OncTextField creator = null;
	private OncTextField modifier = null;
	private OncTextField modificationTime = null;
	private OncScrollableTextArea statementHTML = null;
	private OncTextField creationTime = null;
	private OncScrollableTextArea readyToUse = null;


	public  StatementTemplatePanel() {
		super();
		editObj = new StatementTemplate();
		initUI();
		fillUiHashtable();
	}


	public  StatementTemplatePanel(StatementTemplate editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (StatementTemplate)editObj;
	initUI();
	}
	private void initUI() {
		readyToUse = new OncScrollableTextArea(editObj, "readyToUse", true);
		creationTime = new OncTextField(editObj, "creationTime", true);
		statementHTML = new OncScrollableTextArea(editObj, "statementHTML", true);
		modificationTime = new OncTextField(editObj, "modificationTime", true);
		modifier = new OncTextField(editObj, "modifier", true);
		creator = new OncTextField(editObj, "creator", true);
		parameter = new OncScrollList(Parameter.class, editObj, "parameter", true,true);
		statementBundles = new OncScrollList(StatementBundle.class, editObj, "statementBundles", true,true);
		codeBundles = new OncScrollList(CodeBundle.class, editObj, "codeBundles", true,true);
readyToUse.setBounds(290,350,125,60);
creationTime.setBounds(10,530,250,60);
statementHTML.setBounds(10,0,690,120);
modificationTime.setBounds(290,530,250,60);
modifier.setBounds(290,460,250,60);
creator.setBounds(10,460,250,60);
parameter.setBounds(450,330,250,120);
statementBundles.setBounds(10,130,250,170);
codeBundles.setBounds(300,130,400,170);
readyToUse.setVisible(true);
creationTime.setVisible(true);
statementHTML.setVisible(true);
modificationTime.setVisible(true);
modifier.setVisible(true);
creator.setVisible(true);
parameter.setVisible(true);
statementBundles.setVisible(true);
codeBundles.setVisible(true);
		add(readyToUse);
		add(creationTime);
		add(statementHTML);
		add(modificationTime);
		add(modifier);
		add(creator);
		add(parameter);
		add(statementBundles);
		add(codeBundles);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(readyToUse, "readyToUse");
		uiHashtable.put(creationTime, "creationTime");
		uiHashtable.put(statementHTML, "statementHTML");
		uiHashtable.put(modificationTime, "modificationTime");
		uiHashtable.put(modifier, "modifier");
		uiHashtable.put(creator, "creator");
		uiHashtable.put(parameter, "parameter");
		uiHashtable.put(statementBundles, "statementBundles");
		uiHashtable.put(codeBundles, "codeBundles");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		StatementTemplatePanel p = new StatementTemplatePanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
