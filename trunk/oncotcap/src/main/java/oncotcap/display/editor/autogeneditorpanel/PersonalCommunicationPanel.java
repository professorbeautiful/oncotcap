package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class PersonalCommunicationPanel extends DefaultEditorPanel
{
	private OncScrollList communicator = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList keywords = null;
	private OncScrollList quotesInMe = null;
	private OncTextField recipient = null;


	public  PersonalCommunicationPanel() {
		super();
		editObj = new PersonalCommunication();
		initUI();
		fillUiHashtable();
	}


	public  PersonalCommunicationPanel(PersonalCommunication editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (PersonalCommunication)editObj;
	initUI();
	}
	private void initUI() {
		recipient = new OncTextField(editObj, "Recipient", true);
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		communicator = new OncScrollList(Person.class, editObj, "Communicator", true,true, false);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
		quotesInMe = new OncScrollList(KnowledgeNugget.class, editObj, "QuotesInMe", true,true);
recipient.setBounds(0,840,250,60);
versionNumber.setBounds(0,780,125,60);
communicator.setBounds(0,900,250,60);
keywords.setBounds(0,540,250,120);
quotesInMe.setBounds(0,660,250,120);
recipient.setVisible(true);
versionNumber.setVisible(true);
communicator.setVisible(true);
keywords.setVisible(true);
quotesInMe.setVisible(true);
		add(recipient);
		add(versionNumber);
		add(communicator);
		add(keywords);
		add(quotesInMe);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(recipient, "recipient");
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(communicator, "communicator");
		uiHashtable.put(keywords, "keywords");
		uiHashtable.put(quotesInMe, "quotesInMe");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		PersonalCommunicationPanel p = new PersonalCommunicationPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
