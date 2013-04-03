package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class MolecularNetworkPanel extends DefaultEditorPanel
{
	private OncScrollList molecules = null;
	private OncTextField name = null;
	private OncScrollList biorelationships = null;
	private OncScrollList sentencesAboutThis = null;


	public  MolecularNetworkPanel() {
		super();
		editObj = new MolecularNetwork();
		initUI();
		fillUiHashtable();
	}


	public  MolecularNetworkPanel(MolecularNetwork editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (MolecularNetwork)editObj;
	initUI();
	}
	private void initUI() {
		name = new OncTextField(editObj, "Name", true);
		molecules = new OncScrollList(DefaultPersistibleList.class, editObj, "Molecules", true,true);
		biorelationships = new OncScrollList(Biorelationship.class, editObj, "Biorelationships", true,true);
		sentencesAboutThis = new OncScrollList(Sentence.class, editObj, "SentencesAboutThis", true,true);
name.setBounds(0,0,250,60);
molecules.setBounds(0,60,250,120);
sentencesAboutThis.setBounds(0,180,250,120);
name.setVisible(true);
molecules.setVisible(true);
biorelationships.setVisible(true);
sentencesAboutThis.setVisible(true);
		add(name);
		add(molecules);
		add(biorelationships);
		add(sentencesAboutThis);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(name, "name");
		uiHashtable.put(molecules, "molecules");
		uiHashtable.put(biorelationships, "biorelationships");
		uiHashtable.put(sentencesAboutThis, "sentencesAboutThis");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		MolecularNetworkPanel p = new MolecularNetworkPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
