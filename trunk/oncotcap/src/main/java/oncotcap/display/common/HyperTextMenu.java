package oncotcap.display.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import javax.swing.border.EmptyBorder;
import oncotcap.util.*;

import oncotcap.datalayer.Persistible;
import oncotcap.display.browser.OncBrowserConstants;
import oncotcap.display.browser.GenericTreeNode;
import oncotcap.display.browser.GenericTreeNodeRenderer;
import oncotcap.display.common.DragDropLabel;
import oncotcap.display.common.Droppable;
import oncotcap.display.common.OncTreeNode;
import oncotcap.display.common.HyperLabel;
import oncotcap.display.editor.EditorFrame;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;

import javax.swing.tree.*;

public class HyperTextMenu extends JButton implements ActionListener {
		private static final String OPEN_UNDERLINE = "<html><u>";
		private static final String CLOSE_UNDERLINE = "</u></html>";
		public Hashtable menuMap = new Hashtable();
		JPopupMenu popup = new JPopupMenu();
		Object selectedObject = null;
		Collection theList = null;
		String defaultTitle = new String("Undefined");
		Color currentColor = OncBrowserConstants.CBColorDarkest;

		public HyperTextMenu(String defaultTitle) {
				this.defaultTitle = defaultTitle;
				init();
		}

		public HyperTextMenu(Object selectedObject, Collection list) {
				init();
				setSelectedObject(selectedObject);
				setTheList(list);
				setDefaultUIValue();
				
		}
		public void setDefaultUIValue(){
				if ( selectedObject == null )
						setDisplayString(defaultTitle);
				else { 
						
						setDisplayString(selectedObject.toString());
						
				}
		}
		private void setDisplayString(String str) {
				String textStr = str;
				if ( str.equals(">") )
						textStr = "&gt";
				if ( str.equals("<") )
						textStr = "&lt";
				if ( str.equals(">=") )
						textStr = "&gt=";
				if ( str.equals("<=") )
						textStr = "&lt=";
				if ( str.equals("<>") )
						textStr = "&lt&gt";

				setText(OPEN_UNDERLINE + textStr + CLOSE_UNDERLINE);
		}

		private void init() {
				setForeground(Color.blue);
				setBorder(new EmptyBorder(new Insets(0,0,0,0)));
				setRolloverEnabled(true);
				setFocusPainted(false);
				addActionListener(this);
				// Set the list name to the first element in the list
				setDisplayString(defaultTitle);
		}
		public Collection getTheList() {
				return this.theList;
		}
		public void clear() {
				popup.removeAll();
				menuMap.clear();
		}
		public void setTheList(Collection list) {
				this.theList = list;
				popup.removeAll();
				menuMap.clear();
				// Fill the menu with elements from the list
				Iterator i = this.theList.iterator();
				JMenuItem mi = null;
				while (i.hasNext()) {
						Object obj = i.next();
						mi = new JMenuItem(obj.toString());
						mi.setForeground(currentColor);
						addMenuListeners(mi);
						menuMap.put(mi, obj);
						popup.add(mi);
				}

		}
		public void addToTheList(Collection list) {
				if ( theList == null)
						theList = new Vector();
				theList.addAll(list);
// 				System.out.println("addToTheList " + list
// 													 + " THELIST " + theList);
				// Fill the menu with elements from the list
				Iterator i = list.iterator();
				JMenuItem mi = null;
				if ( list.size() > 0) {
						//alternate the background color
						popup.addSeparator();
						if ( currentColor == OncBrowserConstants.MBColorDarkest )
								currentColor = OncBrowserConstants.CBColorDarkest;
						else
								currentColor = OncBrowserConstants.MBColorDarkest;
				}
				while (i.hasNext()) {
						Object obj = i.next();
						mi = new JMenuItem(obj.toString());
						mi.setForeground(currentColor);
						addMenuListeners(mi);
						menuMap.put(mi, obj);
						popup.add(mi);
				}

		}
		public void setSelectedObject(Object selectedObject) {
				this.selectedObject = selectedObject;
				if ( selectedObject != null ) 
						setDisplayString(selectedObject.toString());
				fireHyperTextMenuChanged(new HyperTextMenuEvent(this, selectedObject));
		}
		public Object getSelectedObject() {
				return this.selectedObject;
		}

		public void addMenuListeners(JMenuItem mi) {
				ActionListener[] actionListeners = getActionListeners();
				for ( int i = 0; i < Array.getLength(actionListeners); i++) {
						mi.addActionListener(actionListeners[i]);
				}
		}
		public void actionPerformed(ActionEvent ae) {
				if ( ae.getSource() instanceof JMenuItem){
						JMenuItem mi = (JMenuItem)ae.getSource();
						setDisplayString(mi.getText());
						setSelectedObject(menuMap.get(mi));
				}
				else if ( ae.getSource().equals(this) ){
						popup.show( this,0,0);
				}
		}
	// This methods allows classes to register for HyperTextMenuEvents
		public void addHyperTextMenuListener
				(HyperTextMenuListener listener) {
				if ( !listenerListContains(HyperTextMenuListener.class, listener) ) 
						listenerList.add(HyperTextMenuListener.class, listener);
		}
    
		// This methods allows classes to unregister for HyperTextMenuEvents
		public void removeHyperTextMenuListener
				(HyperTextMenuListener listener) {
				listenerList.remove(HyperTextMenuListener.class, listener);
		}
    
		// This private class is used to fire OntologyPanelEvents
		private void fireHyperTextMenuChanged(HyperTextMenuEvent evt) {
				Object[] listeners = listenerList.getListenerList();
				// Each listener occupies two elements 
				// - the first is the listener class
				// and the second is the listener instance
				for (int i=0; i<listeners.length; i++) {
						if (listeners[i] == HyperTextMenuListener.class) {
								((HyperTextMenuListener)listeners[i+1]).hyperTextMenuChanged(evt);
						}
				}
		}

		private boolean listenerListContains(Class cls, HyperTextMenuListener listener) {
				Object[] listeners = listenerList.getListeners(cls);
				for (int i=0; i<listeners.length; i++) {
						if ( listeners[i].equals(listener) )  {
								return true;
						}
				}
				return false;
		}
		
}
