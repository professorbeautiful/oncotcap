package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class EncodingPanel extends DefaultEditorPanel
{
	private OncIntegerTextField versionNumber = null;
	private OncScrollableTextArea sentenceText = null;
	private OncScrollList interpretationsIEncode = null;
	private OncScrollList keywords = null;
	private OncScrollList statementBundlesImplementingMe = null;
	private OncScrollList submodelsIJoin = null;
	private OncComboBox modelType = null;

	public  EncodingPanel() {
		super();
		editObj = new Encoding();
		initUI();
		fillUiHashtable();
	}


	public  EncodingPanel(Encoding editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Encoding)editObj;
	initUI();
	}
	private void initUI() {
		sentenceText = new OncScrollableTextArea(editObj, "SentenceText", true);
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		interpretationsIEncode = new OncScrollList(Interpretation.class, editObj, "Interpretations", true,true);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);		Object[] comboBoxList1 = {"Bio","Setting","Validation"};
		modelType = new OncComboBox(editObj, "Type", true, comboBoxList1);

		statementBundlesImplementingMe = new OncScrollList(StatementBundle.class, editObj, "StatementBundlesImplementingMe", true,true);
		submodelsIJoin = new OncScrollList(SubModel.class, editObj, "SubmodelsIJoin", true,true);
sentenceText.setBounds(10,10,460,120);
versionNumber.setBounds(20,530,125,60);
interpretationsIEncode.setBounds(10,140,460,120);
keywords.setBounds(480,10,250,120);
modelType.setBounds(480,150,125,60);
statementBundlesImplementingMe.setBounds(10,260,460,120);
submodelsIJoin.setBounds(10,380,460,130);
sentenceText.setVisible(true);
versionNumber.setVisible(true);
interpretationsIEncode.setVisible(true);
keywords.setVisible(true);
modelType.setVisible(true);
statementBundlesImplementingMe.setVisible(true);
submodelsIJoin.setVisible(true);
		add(sentenceText);
		add(versionNumber);
		add(interpretationsIEncode);
		add(keywords);
		add(statementBundlesImplementingMe);
		add(submodelsIJoin);
		add(modelType);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(sentenceText, "sentenceText");
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(interpretationsIEncode, "interpretationsIEncode");
		uiHashtable.put(keywords, "keywords");
		uiHashtable.put(statementBundlesImplementingMe, "statementBundlesImplementingMe");
		uiHashtable.put(submodelsIJoin, "submodelsIJoin");
		uiHashtable.put(modelType, "modelType");

	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		EncodingPanel p = new EncodingPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
