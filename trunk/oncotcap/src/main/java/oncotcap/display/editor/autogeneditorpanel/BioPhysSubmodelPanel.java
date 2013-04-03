package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class BioPhysSubmodelPanel extends DefaultEditorPanel
{
	private OncScrollList submodelGroupsIJoin = null;
	private OncScrollList encodingsInMe = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList keywords = null;
	private OncTextField name = null;


	public  BioPhysSubmodelPanel() {
		super();
		editObj = new BioPhysSubmodel();
		initUI();
		fillUiHashtable();
	}


	public  BioPhysSubmodelPanel(BioPhysSubmodel editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (BioPhysSubmodel)editObj;
	initUI();
	}
	private void initUI() {
		name = new OncTextField(editObj, "Name", true);
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		submodelGroupsIJoin = new OncScrollList(SubModelGroup.class, editObj, "SubmodelGroupsIJoin", true,true);
		encodingsInMe = new OncScrollList(Encoding.class, editObj, "EncodingsInMe", true,true);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
name.setBounds(0,10,700,60);
versionNumber.setBounds(0,590,125,60);
submodelGroupsIJoin.setBounds(290,320,410,120);
encodingsInMe.setBounds(0,90,700,170);
keywords.setBounds(10,330,250,120);
name.setVisible(true);
versionNumber.setVisible(true);
submodelGroupsIJoin.setVisible(true);
encodingsInMe.setVisible(true);
keywords.setVisible(true);
		add(name);
		add(versionNumber);
		add(submodelGroupsIJoin);
		add(encodingsInMe);
		add(keywords);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(name, "name");
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(submodelGroupsIJoin, "submodelGroupsIJoin");
		uiHashtable.put(encodingsInMe, "encodingsInMe");
		uiHashtable.put(keywords, "keywords");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		BioPhysSubmodelPanel p = new BioPhysSubmodelPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
