package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class AssessmentPanel extends DefaultEditorPanel
{
	private OncTextField timeStamp = null;
	//private OncScrollList problemContext = null;
	private OncScrollList assessor = null;


	public  AssessmentPanel() {
		super();
		editObj = new Assessment();
		initUI();
		fillUiHashtable();
	}


	public  AssessmentPanel(Assessment editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Assessment)editObj;
	initUI();
	}
	private void initUI() {
		assessor = new OncScrollList(Person.class, editObj, "Assessor", true,true, false);
		//problemContext = new OncScrollList(null.class, editObj, "ProblemContext", true,true, false);
		timeStamp = new OncTextField(editObj, "TimeStamp", true);
assessor.setBounds(0,60,250,60);
//problemContext.setBounds(0,120,250,60);
timeStamp.setBounds(0,0,250,60);
assessor.setVisible(true);
//problemContext.setVisible(true);
timeStamp.setVisible(true);
		add(assessor);
//		add(problemContext);
		add(timeStamp);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(assessor, "assessor");
//		uiHashtable.put(problemContext, "problemContext");
		uiHashtable.put(timeStamp, "timeStamp");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		AssessmentPanel p = new AssessmentPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
