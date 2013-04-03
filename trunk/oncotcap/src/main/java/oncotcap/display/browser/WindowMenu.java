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
package oncotcap.display.browser;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import java.lang.reflect.*;
import javax.swing.ImageIcon;

import oncotcap.util.*;
import oncotcap.display.common.OncListModelListener;

public class WindowMenu extends JMenu implements WindowListener,
																								 OncListModelListener  {
		static public Hashtable windows = null;
		//static public Vector windowMenus = new Vector();
		static public OncListModel windowList = new OncListModel();
		static WindowListener windowListener = null;
		static Vector removedActions = new Vector();
		static JMenu menu = null;
		static int counter = 0;
		static {
				windows = new Hashtable();
				windowListener = new WindowMenu("MainMenuListener");
				
		}
		Window window = null;
		public WindowMenu(String menuName) {
				super(menuName);
				if ( menu == null ) 
						menu = this;
				if ( !menuName.equals("MainMenuListener") ) {
						//windowMenus.addElement(this);
						windowList.addListDataListener(this); //TODO
						init();
				}
				setMnemonic('w');
		}
		public WindowMenu(String menuName, Window window) {
				this(menuName);
				addWindow(window);
		}

		private void init() { 
				add(new CloseAllWindowAction());
				add(new HideAllWindowAction());
				
				addAllWindows();
		}

		public void removeWindow(WindowMenuAction action) {
				if ( action == null ) 
						return;
				JMenuItem menuItem = getMenuItem(action);
				if ( menuItem != null ) { 
						remove(menuItem);
				}
		}

		private void openWindow(Window window) {
		}
		
		public static WindowListener getWindowListener() {
				
				return windowListener;
		}
	
		public void addAllWindows() {
			for (Enumeration e=windowList.elements(); 
						 e.hasMoreElements(); ) {
					WindowMenuAction windowAction = (WindowMenuAction)e.nextElement();
					if ( windowAction != null ) {
							add(new JMenuItem(windowAction));
					}
			}
		}
		public JMenuItem getMenuItem(WindowMenuAction action) {
				// In this instance of the Window menu determine where the action is 
				// Loop through entire menu of windows and find the one with the 
				// matching action
				for ( int i = 0; i < getItemCount(); i++) {
						Action menuAction = getItem(i).getAction();
						if ( menuAction != null && menuAction.equals(action) ) {
								return getItem(i);
						}
				}
				return null;
		}

		// Add window to the list of windows 
		public WindowMenuAction addWindow(Object window) {
				
					if ( window == null ) 
						return null;
				WindowMenuAction windowAction = null;
				if ( !windowListContains((Window)window) ) {
						String name = "Window " + String.valueOf(counter);
						ImageIcon icon = OncoTcapIcons.getDefault();
						counter++;
						if ( window instanceof JFrame ) {
								name = ((JFrame)window).getTitle();
								icon = 
										new ImageIcon(((JFrame)window).getIconImage());
						}
						windowAction = 
								new WindowMenuAction(name, icon);
						windowAction.setWindow((Window)window);
						windowList.addElement(windowAction);
				}
				return windowAction;
		}
		private boolean windowListContains(Window window) {				
				for (Enumeration e=windowList.elements(); 
						 e.hasMoreElements(); ) {
						WindowMenuAction action = (WindowMenuAction)e.nextElement();
						if ( action != null && action.getWindow()!= null &&
								 action.getWindow().equals(window) ) 
								return true;
				}
				return false;
		}
		private WindowMenuAction getWindowMenuAction(Window window) {				
				for (Enumeration e=windowList.elements(); 
						 e.hasMoreElements(); ) {
						WindowMenuAction action = (WindowMenuAction)e.nextElement();
						if ( action != null && action.getWindow()!= null &&
								 action.getWindow().equals(window) ) 
								return action;
				}
				return null;
		}
		public void contentsChanged(ListDataEvent e) {
		}
		public void intervalAdded(ListDataEvent e) {
				WindowMenuAction menuAction = 
						(WindowMenuAction)((DefaultListModel)e.getSource()).elementAt(e.getIndex0());
				if ( menuAction != null ) {
						add(new JMenuItem(menuAction));
				}
		}
		public void intervalRemoved(ListDataEvent e) {
				// Ignore this one not enough info
		}
		public void intervalRemoved(ListDataEvent e, Vector removedItems) {
				// take the items off the menu
				Iterator i = removedItems.iterator();
				WindowMenuAction menuAction = null;
				JMenuItem removeItem  = null;
				while ( i.hasNext() ) {
						menuAction = (WindowMenuAction)i.next();
						removeItem = getMenuItem(menuAction);
						if ( removeItem != null ) 
								remove(removeItem);
				}
		}
		// private JMenuItem getMenuItem(WindowMenuAction action) {
				
// 		}
// 		//CLASS
// 		class WindowMenuWindowListener implements WindowListener {
				public void windowActivated(WindowEvent e) {
						if ( ((Component)e.getSource()).isVisible() == true ) {
								addWindow(e.getSource());
						}
						else {
								System.out.println("don't know how");
						}
				}
				public void windowClosed(WindowEvent e){
						//System.out.println("Window closed");
						// Remove window from list of windows
				}
				public void windowClosing(WindowEvent e){
						// WindowMenuAction action = 
// 								(WindowMenuAction)windows.get(e.getSource());
// 						JMenuItem menuItem = getMenuItem(e.getSource(), action);
// 						if ( menuItem != null ) { 
// 								System.out.println("Window closing");
// 								remove(menuItem);
// 						}
				}
				public void windowDeactivated(WindowEvent e){
						if ( ((Component)e.getSource()).isVisible() == false 
								 && e.getSource() instanceof Window) {
								//Window is closed by our standard"
								WindowMenuAction action = getWindowMenuAction((Window)e.getSource());
								windowList.removeElement(action);
						}
						else {
								// hiding but accessible
						}
				}
				public void windowDeiconified(WindowEvent e) {
						//System.out.println("Window deiconified");
				}
				public void windowIconified(WindowEvent e) {
						//System.out.println("Window iconified");
				}
				public void windowOpened(WindowEvent e) {
						WindowMenuAction windowAction = addWindow(e.getSource());
						// add new window to the window list and the chaneg will trigger
						// it to get added to all listenening menus
						windowList.addElement(windowAction);
						// For every window menu add the new window
						// Iterator i = windowMenus.iterator();
// 						while ( i.hasNext() ) {
// 								((WindowMenu)i.next()).add(new JMenuItem(windowAction));
// 						}
				}
				public void actionPerformed(ActionEvent e) {
						//...Get information from the action event...
						//...Display it in the text area...
				}
				
				public void itemStateChanged(ItemEvent e) {
						//...Get information from the item event...
						//...Display it in the text area...
				}

		class HideAllWindowAction extends AbstractAction {

				public HideAllWindowAction() {
						super("Hide all windows");
				}
				public void actionPerformed(ActionEvent e) {
						// Loop through entire list windows and remove each from the list this
						// will trigger the menu items to be removed from each menu
						for ( int i = 0; i < windowList.size(); i ++) {
								WindowMenuAction menuAction = (WindowMenuAction)windowList.elementAt(i);
								if ( menuAction == null )
										continue;
								Window window = menuAction.getWindow();
								if ( !window.equals(OncBrowser.getOncBrowser()) ) {
										if (window instanceof Frame)
												((Frame)window).setExtendedState(Frame.ICONIFIED);
								}
						}
				}
				
		}
		class CloseAllWindowAction extends AbstractAction {

				public CloseAllWindowAction() {
						super("Close all windows");
				}
				public void actionPerformed(ActionEvent e) {
						// Loop through entire list windows and remove each from the list this
						// will trigger the menu items to be removed from each menu
						for ( int i = 0; i < windowList.size(); i ++) {
								WindowMenuAction menuAction = (WindowMenuAction)windowList.elementAt(i);
								if ( menuAction == null )
										continue;
								Window window = menuAction.getWindow();
								if ( !window.equals(OncBrowser.getOncBrowser()) ) {
										window.setVisible(false);
										windowList.removeElement(menuAction);
								}
						}
				}

		}

		// Need this to override the removeElement method so I can capture what was removed
		static class OncListModel extends DefaultListModel {
				public Object remove(int indx) {
						Object obj = super.remove(indx);
						return obj;
				} 
				public boolean removeElement(Object obj) {
						int removedIndex = indexOf(obj);
						if ( removedIndex < 0 ) 
								return false;
						boolean successful = super.removeElement(obj);
						Vector removedItems = new Vector();
						removedItems.addElement(obj);
						ListDataListener[] listeners = getListDataListeners();
						ListDataEvent event = new ListDataEvent(windowList, 
																										ListDataEvent.INTERVAL_REMOVED,
																										removedIndex,
																										removedIndex);
																										
						for (int i=0; i<listeners.length; i++) {
								if (listeners[i] instanceof OncListModelListener) {
										((OncListModelListener)listeners[i]).intervalRemoved(event,
																																				 removedItems);
								}
						}
						return successful;
				} 
		}
}
