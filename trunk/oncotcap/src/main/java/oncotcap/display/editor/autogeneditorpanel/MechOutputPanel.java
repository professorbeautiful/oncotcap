package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class MechOutputPanel extends DefaultEditorPanel
{
	private OncTextField direction = null;
	private OncTextField magnitude = null;
	private OncTextField name = null;
	private OncScrollList sentencesAboutThis = null;


	public  MechOutputPanel() {
		super();
		editObj = new MechOutput();
		initUI();
		fillUiHashtable();
	}


	public  MechOutputPanel(MechOutput editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (MechOutput)editObj;
	initUI();
	}
	private void initUI() {
		name = new OncTextField(editObj, "Name", true);
		magnitude = new OncTextField(editObj, "Magnitude", true);
		direction = new OncTextField(editObj, "Direction", true);
		sentencesAboutThis = new OncScrollList(Sentence.class, editObj, "SentencesAboutThis", true,true);
name.setBounds(0,120,250,60);
magnitude.setBounds(0,60,250,60);
direction.setBounds(0,0,250,60);
sentencesAboutThis.setBounds(250,0,250,180);
name.setVisible(true);
magnitude.setVisible(true);
direction.setVisible(true);
sentencesAboutThis.setVisible(true);
		add(name);
		add(magnitude);
		add(direction);
		add(sentencesAboutThis);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(name, "name");
		uiHashtable.put(magnitude, "magnitude");
		uiHashtable.put(direction, "direction");
		uiHashtable.put(sentencesAboutThis, "sentencesAboutThis");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		MechOutputPanel p = new MechOutputPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
