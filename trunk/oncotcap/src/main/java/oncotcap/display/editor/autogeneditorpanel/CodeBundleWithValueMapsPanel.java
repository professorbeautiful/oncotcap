package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class CodeBundleWithValueMapsPanel extends DefaultEditorPanel
{
	private OncScrollList codeBundle = null;
	private OncScrollList valueMap = null;


	public  CodeBundleWithValueMapsPanel() {
		super();
		editObj = new CodeBundleWithValueMaps();
		initUI();
		fillUiHashtable();
	}


	public  CodeBundleWithValueMapsPanel(CodeBundleWithValueMaps editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (CodeBundleWithValueMaps)editObj;
	initUI();
	}
	private void initUI() {
		codeBundle = new OncScrollList(CodeBundle.class, editObj, "CodeBundle", true,true, false);
		valueMap = new OncScrollList(ValueMapEntry.class, editObj, "ValueMap", true,true);
codeBundle.setBounds(0,0,250,60);
valueMap.setBounds(0,60,250,120);
codeBundle.setVisible(true);
valueMap.setVisible(true);
		add(codeBundle);
		add(valueMap);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(codeBundle, "codeBundle");
		uiHashtable.put(valueMap, "valueMap");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		CodeBundleWithValueMapsPanel p = new CodeBundleWithValueMapsPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
