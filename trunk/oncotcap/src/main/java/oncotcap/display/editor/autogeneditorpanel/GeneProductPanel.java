package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class GeneProductPanel extends DefaultEditorPanel
{
	private OncScrollList bindsTo = null;
	private OncScrollList upregulates = null;
	private OncScrollList downregulates = null;
	private OncScrollList isCodedBy = null;
	private OncScrollList participatesIn = null;
	private OncTextField name = null;
	private OncScrollList sentencesAboutThis = null;
	private OncScrollList activates = null;
	private OncScrollList deactivates = null;


	public  GeneProductPanel() {
		super();
		editObj = new GeneProduct();
		initUI();
		fillUiHashtable();
	}


	public  GeneProductPanel(GeneProduct editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (GeneProduct)editObj;
	initUI();
	}
	private void initUI() {
		name = new OncTextField(editObj, "Name", true);
		bindsTo = new OncScrollList(BioEntity.class, editObj, "BindsTo", true,true);
		upregulates = new OncScrollList(BioEntity.class, editObj, "Upregulates", true,true);
		downregulates = new OncScrollList(BioEntity.class, editObj, "Downregulates", true,true);
		isCodedBy = new OncScrollList(Gene.class, editObj, "IsCodedBy", true,true);
		participatesIn = new OncScrollList(MolecularNetwork.class, editObj, "ParticipatesIn", true,true);
		sentencesAboutThis = new OncScrollList(Sentence.class, editObj, "SentencesAboutThis", true,true);
		activates = new OncScrollList(BioEntity.class, editObj, "Activates", true,true);
		deactivates = new OncScrollList(BioEntity.class, editObj, "Deactivates", true,true);
name.setBounds(0,0,250,60);
bindsTo.setBounds(0,180,250,120);
upregulates.setBounds(250,360,250,180);
downregulates.setBounds(0,420,250,120);
isCodedBy.setBounds(250,0,250,120);
participatesIn.setBounds(250,120,250,120);
sentencesAboutThis.setBounds(250,240,250,120);
activates.setBounds(0,60,250,120);
deactivates.setBounds(0,300,250,120);
name.setVisible(true);
bindsTo.setVisible(true);
upregulates.setVisible(true);
downregulates.setVisible(true);
isCodedBy.setVisible(true);
participatesIn.setVisible(true);
sentencesAboutThis.setVisible(true);
activates.setVisible(true);
deactivates.setVisible(true);
		add(name);
		add(bindsTo);
		add(upregulates);
		add(downregulates);
		add(isCodedBy);
		add(participatesIn);
		add(sentencesAboutThis);
		add(activates);
		add(deactivates);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(name, "name");
		uiHashtable.put(bindsTo, "bindsTo");
		uiHashtable.put(upregulates, "upregulates");
		uiHashtable.put(downregulates, "downregulates");
		uiHashtable.put(isCodedBy, "isCodedBy");
		uiHashtable.put(participatesIn, "participatesIn");
		uiHashtable.put(sentencesAboutThis, "sentencesAboutThis");
		uiHashtable.put(activates, "activates");
		uiHashtable.put(deactivates, "deactivates");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		GeneProductPanel p = new GeneProductPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
