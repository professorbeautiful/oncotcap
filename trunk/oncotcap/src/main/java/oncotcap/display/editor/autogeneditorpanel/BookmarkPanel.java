package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class BookmarkPanel extends DefaultEditorPanel
{
	private OncIntegerTextField bookmarkLocation = null;


	public  BookmarkPanel() {
		super();
		editObj = new Bookmark();
		initUI();
		fillUiHashtable();
	}


	public  BookmarkPanel(Bookmark editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Bookmark)editObj;
	initUI();
	}
	private void initUI() {
		bookmarkLocation = new OncIntegerTextField(editObj, "BookmarkLocation", true);
bookmarkLocation.setBounds(0,0,125,60);
bookmarkLocation.setVisible(true);
		add(bookmarkLocation);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(bookmarkLocation, "bookmarkLocation");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		BookmarkPanel p = new BookmarkPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
