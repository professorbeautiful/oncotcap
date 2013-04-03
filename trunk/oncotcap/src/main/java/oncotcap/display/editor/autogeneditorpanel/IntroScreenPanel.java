package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class IntroScreenPanel extends DefaultEditorPanel
{


	public  IntroScreenPanel() {
		super();
		editObj = new IntroScreen();
		initUI();
		fillUiHashtable();
	}


	public  IntroScreenPanel(IntroScreen editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (IntroScreen)editObj;
	initUI();
	}
	private void initUI() {
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		IntroScreenPanel p = new IntroScreenPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
