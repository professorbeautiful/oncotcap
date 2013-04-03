/*
 * Educational Resource for Tumor Heterogeneity
 *             Oncology Thinking Cap
 *
 * Copyright (c) 2003  University of Pittsburgh
 * All rights reserved.
 *
 *  SourceSafe Info:
 *               $Header: $
 *               Revision: $Revision$
 *               Author: $Author$
 *
 * Code Review History:
 *     (mm.dd.yyyy initials)
 *
 * Test History:
 *     (mm.dd.yyyy initials)
 */
package oncotcap.display.common;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.beans.*;
import java.io.*;

import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import javax.accessibility.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.*;
import javax.swing.tree.*;

import oncotcap.datalayer.*;
import oncotcap.display.browser.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;

public class OncScrollTableButtonPanel extends JPanel {
		static Dimension defaultSize = new Dimension(600,400);
		Icon EDIT_ICON = 
				oncotcap.util.OncoTcapIcons.getImageIcon("edit-icon.jpg");
		Icon UNLINK_ICON = 
				oncotcap.util.OncoTcapIcons.getImageIcon("remove-link-icon.jpg");
		Icon CREATE_ICON = 
				oncotcap.util.OncoTcapIcons.getImageIcon("add-link-icon.jpg");
		Icon DELETE_ICON = 
				oncotcap.util.OncoTcapIcons.getImageIcon("remove-small.jpg");
		Icon FIND_ICON = 
				oncotcap.util.OncoTcapIcons.getImageIcon("find-icon.jpg");
		Icon WEB_ICON = 
				oncotcap.util.OncoTcapIcons.getImageIcon("web-icon.jpg");		
		OncBrowserButton createBtn = new OncBrowserButton(CREATE_ICON);
		OncBrowserButton unlinkBtn = new OncBrowserButton(UNLINK_ICON);
		OncBrowserButton editBtn = new OncBrowserButton(EDIT_ICON);
		OncBrowserButton deleteBtn = new OncBrowserButton(DELETE_ICON);
		OncBrowserButton findBtn = new OncBrowserButton(FIND_ICON);		
		OncBrowserButton webBtn = new OncBrowserButton(WEB_ICON);		
		OncScrollTable scrollTable = null;
		AddTreeNode addAction = null;
		RemoveNode removeAction = null;
		EditNode editAction = null;
		UnlinkNode unlinkAction = null;
		LaunchBrowser launchBrowser = null;
		
		JTable list = null;
		Class listType = null;

		Collection objectsToLink = null;

		public OncScrollTableButtonPanel() {
				init();
		}
		
		public OncScrollTableButtonPanel(OncScrollTable scrollTable) {
				this.scrollTable = scrollTable;
				init();
		}
		
		public OncScrollTableButtonPanel(JTable list, Class listType) {
				init();
				this.list = list;
				this.listType = listType;
				setTable(list);
		}
		
		public void setScrollTable(OncScrollTable scrollTable) {
				this.scrollTable = scrollTable;
				addAction.setSource(this.scrollTable);
				addAction.setSourceParent(this.scrollTable);
		}

		private void init() {
				setLayout(new FlowLayout(FlowLayout.CENTER, 0 ,0));
			
				// Remove linked object
				deleteBtn.setMargin(new Insets(0,0,0,0));
				removeAction = new RemoveNode();
				System.out.println("OSTBP scrollTable" + this.scrollTable.getClass());
				removeAction.setSource(this.scrollTable);
				deleteBtn.setAction(removeAction);
				deleteBtn.setIcon(DELETE_ICON);
				deleteBtn.setText("");
				deleteBtn.setParentComponent(scrollTable);


				// Unlink linked object
				unlinkBtn.setMargin(new Insets(0,0,0,0));
				unlinkAction = new UnlinkNode();
				unlinkAction.setSource(this.scrollTable);
				unlinkBtn.setAction(unlinkAction);
				unlinkBtn.setIcon(UNLINK_ICON);
				unlinkBtn.setText("");
				unlinkBtn.setParentComponent(scrollTable);


				// Edit
				editBtn.setMargin(new Insets(0,0,0,0));
				editAction = new EditNode();
				editAction.setSource(this.scrollTable);
				editBtn.setAction(editAction);
				editBtn.setIcon(EDIT_ICON);
				editBtn.setText("");
				editBtn.setParentComponent(scrollTable);

				findBtn.setMargin(new Insets(0,0,0,0));
				findBtn.setAction(new Find());
				findBtn.setParentComponent(scrollTable);


				addAction = new AddTreeNode("Add Link Node", true, AddTreeNode.NONE);
				addAction.setSource(this.scrollTable);
				createBtn.setOpaque(false);
				createBtn.setAction(addAction);
				createBtn.setIcon(CREATE_ICON);
				createBtn.setMargin(new Insets(0,0,0,0));
				//createBtn.setBorder(BorderFactory.createEmptyBorder());
				createBtn.setText("");
				createBtn.setParentComponent(scrollTable);

				addAction.setTree(OncBrowser.getTree());
				setPreferredSize(new Dimension(300,35));

				add(createBtn);
				add(editBtn);
				add(unlinkBtn);
				add(deleteBtn);
				add(findBtn);

		}
		
		public void setTable(JTable list) {
				this.list = list;
		}
		
		public Object getValue(){
				return list.getModel();
		}
		public void setValue(Object obj){
		}
		public void setObjectsToLink(Collection selectedObjects) {
				this.objectsToLink = selectedObjects;
		}
		
		public Collection getObjectsToLink() {
				return objectsToLink;
		}
		/*
		private void registerAcceleratorKeys() {
				// Register accelerator keys
				oncScrollList.getInputMap
						(JComponent.WHEN_FOCUSED).put
						(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0),
						 linkNodeAdd.getValue(Action.NAME));
				oncScrollList.getActionMap().put(linkNodeAdd.getValue(Action.NAME), 
																 linkNodeAdd);

				oncScrollList.getInputMap
						(JComponent.WHEN_FOCUSED).put
						(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, InputEvent.ALT_MASK, true),
						 rootNodeAdd.getValue(Action.NAME));
				oncScrollList.getActionMap().put(rootNodeAdd.getValue(Action.NAME), 
																rootNodeAdd);
				oncScrollList.getInputMap
						(JComponent.WHEN_FOCUSED).put
						(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, InputEvent.CTRL_MASK, true),
						 keywordAdd.getValue(Action.NAME));
				oncScrollList.getActionMap().put(keywordAdd.getValue(Action.NAME), 
																 keywordAdd);
				oncScrollList.getInputMap
						(JComponent.WHEN_FOCUSED).put
						(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
						 remove.getValue(Action.NAME));
				oncScrollList.getActionMap().put(remove.getValue(Action.NAME), 
																 remove);

				oncScrollList.getInputMap
						(JComponent.WHEN_FOCUSED).put
						(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK, true),
						 find.getValue(Action.NAME));
				oncScrollList.getActionMap().put(find.getValue(Action.NAME), 
																 find);
		}
		*/
		class Find extends AbstractAction {
				public Find() {
						super(null, FIND_ICON);
				}
				public Find(String actionName) {
						super(actionName, FIND_ICON);
				}

				public void actionPerformed(ActionEvent e) {
						JFrame kf = new JFrame();
						JDialog f = new JDialog((JFrame)null, true);
						OncFindPanel p = new OncFindPanel();
						p.setSearchComponent(scrollTable);
						f.getContentPane().add(p);
						f.setSize(500,75);
						f.setVisible(true);
				}
		}
 }
		
