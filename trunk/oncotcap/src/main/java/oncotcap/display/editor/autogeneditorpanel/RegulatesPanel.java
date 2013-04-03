package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class RegulatesPanel extends DefaultEditorPanel
{
	private OncTextField biorelationshipType = null;
	private OncTextField direction = null;
	private OncScrollList firstObject = null;
	private OncScrollList secondObject = null;
	private OncDoubleTextField magnitude = null;
	private OncTextField name = null;
	private OncScrollList sentencesAboutThis = null;


	public  RegulatesPanel() {
		super();
		editObj = new Regulates();
		initUI();
		fillUiHashtable();
	}


	public  RegulatesPanel(Regulates editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Regulates)editObj;
	initUI();
	}
	private void initUI() {
		name = new OncTextField(editObj, "Name", true);
		magnitude = new OncDoubleTextField(editObj, "Magnitude", true);
		secondObject = new OncScrollList(Molecule.class, editObj, "SecondObject", true,true, false);
		firstObject = new OncScrollList(Gene.class, editObj, "FirstObject", true,true, false);
		direction = new OncTextField(editObj, "Direction", true);
		biorelationshipType = new OncTextField(editObj, "BiorelationshipType", true);
		sentencesAboutThis = new OncScrollList(Sentence.class, editObj, "SentencesAboutThis", true,true);
name.setBounds(0,120,250,60);
magnitude.setBounds(0,180,125,60);
secondObject.setBounds(250,60,250,60);
firstObject.setBounds(250,0,250,60);
direction.setBounds(0,60,250,60);
biorelationshipType.setBounds(0,0,250,60);
sentencesAboutThis.setBounds(250,120,250,120);
name.setVisible(true);
magnitude.setVisible(true);
secondObject.setVisible(true);
firstObject.setVisible(true);
direction.setVisible(true);
biorelationshipType.setVisible(true);
sentencesAboutThis.setVisible(true);
		add(name);
		add(magnitude);
		add(secondObject);
		add(firstObject);
		add(direction);
		add(biorelationshipType);
		add(sentencesAboutThis);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(name, "name");
		uiHashtable.put(magnitude, "magnitude");
		uiHashtable.put(secondObject, "secondObject");
		uiHashtable.put(firstObject, "firstObject");
		uiHashtable.put(direction, "direction");
		uiHashtable.put(biorelationshipType, "biorelationshipType");
		uiHashtable.put(sentencesAboutThis, "sentencesAboutThis");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		RegulatesPanel p = new RegulatesPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
