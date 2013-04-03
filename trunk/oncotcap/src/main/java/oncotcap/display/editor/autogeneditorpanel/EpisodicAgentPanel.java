package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class EpisodicAgentPanel extends DefaultEditorPanel
{
	private OncScrollList toxicities = null;
	private OncTextField name = null;
	private OncScrollList sentencesAboutThis = null;


	public  EpisodicAgentPanel() {
		super();
		editObj = new EpisodicAgent();
		initUI();
		fillUiHashtable();
	}


	public  EpisodicAgentPanel(EpisodicAgent editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (EpisodicAgent)editObj;
	initUI();
	}
	private void initUI() {
		name = new OncTextField(editObj, "Name", true);
		toxicities = new OncScrollList(String.class, editObj, "Toxicities", true,true);
		sentencesAboutThis = new OncScrollList(Sentence.class, editObj, "SentencesAboutThis", true,true);
name.setBounds(0,0,250,60);
toxicities.setBounds(0,60,250,120);
sentencesAboutThis.setBounds(250,0,250,180);
name.setVisible(true);
toxicities.setVisible(true);
sentencesAboutThis.setVisible(true);
		add(name);
		add(toxicities);
		add(sentencesAboutThis);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(name, "name");
		uiHashtable.put(toxicities, "toxicities");
		uiHashtable.put(sentencesAboutThis, "sentencesAboutThis");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		EpisodicAgentPanel p = new EpisodicAgentPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
