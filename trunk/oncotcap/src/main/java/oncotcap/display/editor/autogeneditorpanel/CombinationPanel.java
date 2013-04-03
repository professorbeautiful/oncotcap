package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class CombinationPanel extends DefaultEditorPanel
{
	private OncScrollList agents = null;
	private OncTextField name = null;
	private OncScrollList sentencesAboutThis = null;


	public  CombinationPanel() {
		super();
		editObj = new Combination();
		initUI();
		fillUiHashtable();
	}


	public  CombinationPanel(Combination editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Combination)editObj;
	initUI();
	}
	private void initUI() {
		name = new OncTextField(editObj, "Name", true);
		agents = new OncScrollList(Agent.class, editObj, "Agents", true,true);
		sentencesAboutThis = new OncScrollList(Sentence.class, editObj, "SentencesAboutThis", true,true);
name.setBounds(0,0,250,60);
agents.setBounds(0,60,250,120);
sentencesAboutThis.setBounds(250,0,250,180);
name.setVisible(true);
agents.setVisible(true);
sentencesAboutThis.setVisible(true);
		add(name);
		add(agents);
		add(sentencesAboutThis);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(name, "name");
		uiHashtable.put(agents, "agents");
		uiHashtable.put(sentencesAboutThis, "sentencesAboutThis");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		CombinationPanel p = new CombinationPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
