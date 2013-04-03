package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class EvidencePanel extends DefaultEditorPanel
{
	private OncComboBox direction = null;
	private OncScrollList statement = null;
	private OncScrollList assessments = null;
	private OncTextField sentenceText = null;


	public  EvidencePanel() {
		super();
		editObj = new Evidence();
		initUI();
		fillUiHashtable();
	}


	public  EvidencePanel(Evidence editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Evidence)editObj;
	initUI();
	}
	private void initUI() {
		sentenceText = new OncTextField(editObj, "SentenceText", true);
		statement = new OncScrollList(Claim.class, editObj, "Statement", true,true, false);
		Object[] comboBoxList1 = {""};
		direction = new OncComboBox(editObj, "Direction", true, comboBoxList1);
		assessments = new OncScrollList(AssessmentItem.class, editObj, "Assessments", true,true);
sentenceText.setBounds(0,0,250,60);
statement.setBounds(0,120,250,60);
direction.setBounds(0,60,125,60);
assessments.setBounds(250,0,250,180);
sentenceText.setVisible(true);
statement.setVisible(true);
direction.setVisible(true);
assessments.setVisible(true);
		add(sentenceText);
		add(statement);
		add(direction);
		add(assessments);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(sentenceText, "sentenceText");
		uiHashtable.put(statement, "statement");
		uiHashtable.put(direction, "direction");
		uiHashtable.put(assessments, "assessments");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		EvidencePanel p = new EvidencePanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
