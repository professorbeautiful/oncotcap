package oncotcap.display.editor.persistibleeditorpanel;

import java.util.*;
import javax.swing.*;
import java.awt.Dimension;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;


public class KeywordPanel extends DefaultEditorPanel
{
	public  Keyword editObj = null;
	private OncTextField keyword = null;


	public  KeywordPanel() {
		super();
		editObj = new Keyword();
		initUI();
		fillUiHashtable();
	}


	public  KeywordPanel(Keyword editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
			this.editObj = (Keyword)editObj;
			initUI();
	}

	private void initUI() {
		setPreferredSize( new Dimension(250,60));

		keyword = new OncTextField(editObj, "keyword", true);
		keyword.setBounds(0,0,250,60);
		keyword.setVisible(true);
		add(keyword);

	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(keyword, "keyword");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		KeywordPanel p = new KeywordPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
