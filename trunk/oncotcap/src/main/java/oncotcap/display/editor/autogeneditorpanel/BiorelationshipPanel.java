package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class BiorelationshipPanel extends DefaultEditorPanel
{
	private OncTextField biorelationshipType = null;
	private OncTextField name = null;
	private OncScrollList sentencesAboutThis = null;


	public  BiorelationshipPanel() {
		super();
		editObj = new Biorelationship();
		initUI();
		fillUiHashtable();
	}


	public  BiorelationshipPanel(Biorelationship editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Biorelationship)editObj;
	initUI();
	}
	private void initUI() {
		name = new OncTextField(editObj, "Name", true);
		biorelationshipType = new OncTextField(editObj, "BiorelationshipType", true);
		sentencesAboutThis = new OncScrollList(Sentence.class, editObj, "SentencesAboutThis", true,true);
name.setBounds(0,60,250,60);
biorelationshipType.setBounds(0,0,250,60);
sentencesAboutThis.setBounds(0,120,250,120);
name.setVisible(true);
biorelationshipType.setVisible(true);
sentencesAboutThis.setVisible(true);
		add(name);
		add(biorelationshipType);
		add(sentencesAboutThis);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(name, "name");
		uiHashtable.put(biorelationshipType, "biorelationshipType");
		uiHashtable.put(sentencesAboutThis, "sentencesAboutThis");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		BiorelationshipPanel p = new BiorelationshipPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
