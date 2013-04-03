package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class SentencePanel extends DefaultEditorPanel
{
	private OncTextField sentenceText = null;


	public  SentencePanel() {
		super();
		editObj = new Sentence();
		initUI();
		fillUiHashtable();
	}


	public  SentencePanel(Sentence editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Sentence)editObj;
	initUI();
	}
	private void initUI() {
		sentenceText = new OncTextField(editObj, "SentenceText", true);
sentenceText.setBounds(0,0,250,60);
sentenceText.setVisible(true);
		add(sentenceText);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(sentenceText, "sentenceText");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		SentencePanel p = new SentencePanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
