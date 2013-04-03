package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class CancerEntityPanel extends DefaultEditorPanel
{
	private OncTextField name = null;
	private OncScrollList sentencesAboutThis = null;


	public  CancerEntityPanel() {
		super();
		editObj = new CancerEntity();
		initUI();
		fillUiHashtable();
	}


	public  CancerEntityPanel(CancerEntity editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (CancerEntity)editObj;
	initUI();
	}
	private void initUI() {
		name = new OncTextField(editObj, "Name", true);
		sentencesAboutThis = new OncScrollList(Sentence.class, editObj, "SentencesAboutThis", true,true);
name.setBounds(0,0,250,60);
sentencesAboutThis.setBounds(0,60,250,120);
name.setVisible(true);
sentencesAboutThis.setVisible(true);
		add(name);
		add(sentencesAboutThis);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(name, "name");
		uiHashtable.put(sentencesAboutThis, "sentencesAboutThis");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		CancerEntityPanel p = new CancerEntityPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
