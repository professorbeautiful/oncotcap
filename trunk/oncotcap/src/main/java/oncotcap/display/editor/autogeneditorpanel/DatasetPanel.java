package oncotcap.display.editor.autogeneditorpanel;

import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.*;
import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;


public class DatasetPanel extends DefaultEditorPanel
{
	private OncTextField creator = null;
	private OncTextField modifier = null;
	private OncTextField modificationTime = null;
	private OncScrollList quotesInMe = null;
	private OncIntegerTextField sampleSize = null;
	private OncTextField creationTime = null;
	private OncTextField dataSource = null;


	public  DatasetPanel() {
		super();
		editObj = new Dataset();
		initUI();
		fillUiHashtable();
	}


	public  DatasetPanel(Dataset editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Dataset)editObj;
	initUI();
	}
	private void initUI() {
		dataSource = new OncTextField(editObj, "dataSource", true);
		creationTime = new OncTextField(editObj, "creationTime", true);
		sampleSize = new OncIntegerTextField(editObj, "sampleSize", true);
		modificationTime = new OncTextField(editObj, "modificationTime", true);
		modifier = new OncTextField(editObj, "modifier", true);
		creator = new OncTextField(editObj, "creator", true);
		quotesInMe = new OncScrollList(KnowledgeNugget.class, editObj, "quotesInMe", true,true);
dataSource.setBounds(0,780,250,60);
creationTime.setBounds(0,290,250,60);
sampleSize.setBounds(0,840,125,60);
modificationTime.setBounds(0,410,250,60);
modifier.setBounds(250,290,250,60);
creator.setBounds(0,350,250,60);
quotesInMe.setBounds(0,660,250,120);
dataSource.setVisible(true);
creationTime.setVisible(true);
sampleSize.setVisible(true);
modificationTime.setVisible(true);
modifier.setVisible(true);
creator.setVisible(true);
quotesInMe.setVisible(true);
		add(dataSource);
		add(creationTime);
		add(sampleSize);
		add(modificationTime);
		add(modifier);
		add(creator);
		add(quotesInMe);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(dataSource, "dataSource");
		uiHashtable.put(creationTime, "creationTime");
		uiHashtable.put(sampleSize, "sampleSize");
		uiHashtable.put(modificationTime, "modificationTime");
		uiHashtable.put(modifier, "modifier");
		uiHashtable.put(creator, "creator");
		uiHashtable.put(quotesInMe, "quotesInMe");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		DatasetPanel p = new DatasetPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
