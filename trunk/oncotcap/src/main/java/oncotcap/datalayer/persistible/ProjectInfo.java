package oncotcap.datalayer.persistible;

import java.util.*;
import java.awt.datatransfer.*;
import javax.swing.ImageIcon;
import java.awt.datatransfer.*;
import javax.swing.*;


import oncotcap.datalayer.AbstractPersistible;
import oncotcap.display.common.Droppable;
import oncotcap.datalayer.AutoGenEditable;
import oncotcap.datalayer.DefaultPersistibleList;
import oncotcap.datalayer.PersistibleWithKeywords;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.editor.persistibleeditorpanel.KeywordPanel;
import oncotcap.util.ReflectionHelper;
import java.lang.reflect.*;


public class ProjectInfo extends AbstractPersistible {
//		implements Droppable, Editable, AutoGenEditable,
//							 oncotcap.display.browser.TreeBrowserNode{

	public ProjectInfo(oncotcap.util.GUID guid){
		super(guid);
		init();
	}	
	
	public ProjectInfo(){
			init();
	}

		private void init() {
		}

	public String toString() {
		return("Project Info: Version Number = " + getVersionNumber());
	}
		
}	 
