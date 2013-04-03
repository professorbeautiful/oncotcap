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
import oncotcap.datalayer.persistible.*;
import oncotcap.display.browser.GenericTree;
import oncotcap.display.browser.OncMergeBrowser;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class OncScrollListListener extends MouseAdapter
		implements ListSelectionListener {

		/** Used to make sure the paths are unique, will contain all the paths
     * in <code>selection</code>.
     */
    static private Vector selectedItems = new Vector();
		static Dimension defaultSize = new Dimension(600,400);

		public OncScrollListListener() {
				selectedItems = new Vector();
		}


    public void valueChanged(ListSelectionEvent e) {
				// Place holder
				// 				System.out.println("List HAS CHANGED " + e);
				// Notify list listeners that something has happened
				Object source = e.getSource();
    }

		public void mouseClicked(MouseEvent e) {
				// 	System.out.println("clicked but not pressed " 
				// 				 + e.getX() + ", " + e.getY());
				// 	System.out.println("pressed click count " + 
				// 				 e.getClickCount());
		}

		public void mousePressed(MouseEvent e) {
				if ( e.getSource() instanceof JList) {
						processListSelection(e);
				}
				else if ( e.getSource() instanceof JTable) {
						processTableSelection(e);
				}
		}

		public void mouseReleased(MouseEvent e) {
			// 	System.out.println("released but not clicked " 
// 													 + e.getX() + ", " + e.getY());
// 				System.out.println("pressed click count " + 
// 													 e.getClickCount());
		}


		private void processListSelection(MouseEvent e) {
						JList list = (JList)e.getSource();
						//System.out.println("selected indices " + list.getSelectedIndex());
						int index = list.locationToIndex(e.getPoint());
						//list.setSelectedIndex(index);
						if ( index < 0 || index >= list.getModel().getSize())
							return;
						Object obj = list.getModel().getElementAt(index);
						
						if (e.getClickCount() == 1 
								&& ( e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3)) {
							// Right-click for a popup menu.
							Class cls = 
									((DefaultMutableTreeNode)obj).getUserObject().getClass();
							Object inst = 
									((DefaultMutableTreeNode)obj).getUserObject();
							System.out.println("showing popup " + cls);
							JPopupMenu popup = 
									((AbstractPersistible)obj).getPopupMenu();
							if ( popup != null && popup instanceof OncPopupMenu){
									System.out.println("showing popup");
									((OncPopupMenu)popup).setCurrentObject(inst);
									((OncPopupMenu)popup).setContext(this);
									popup.show(list, e.getX(), e.getY() );
							}
							return;
						}
					
						if (e.getClickCount() == 1 
								&& e.getButton() == MouseEvent.BUTTON1 
								&& (e.isControlDown() )) {
										// See if it is a valid URL
										if ( obj instanceof String) {
												try {
														// if it is a valid url launch browser
														oncotcap.util.BrowserLauncher2.openURL((String)obj);

												}catch(MalformedURLException mfue) {
														System.out.println("ERROR Malformed URL: "
																							 + obj);
												}
												catch(IOException ioe) {
														System.out.println("ERROR IO exception  launching browser: "
																							 + obj);
												}
										}
										
						}
						else if (e.getClickCount() == 2 ) {
								// select item
								// do double click action ( edit )
								if ( index >= list.getModel().getSize() )
										return;
								if ( obj instanceof Editable) {
										// 		System.out.println("editing obj: " 
										//+ ((AbstractPersistible)obj).getGUID() + 
										// 				 " " + obj + " " + obj.getClass() );
										EditorFrame.showEditor((Editable)obj, null);
								}
								else if ( obj instanceof String ) {
										// Make a label
										String label = new String("Please enter string value");
										String inputValue = 
												JOptionPane.showInputDialog(label, obj); 
										if ( inputValue != null ) {
												DefaultListModel model = (DefaultListModel)list.getModel();
												model.set(index, inputValue);
												list.setModel(model);
												list.revalidate();
										}
										else 
												System.out.println("How did you get to ad a string");
								}
						}
		}
		private void processTableSelection(MouseEvent e) {
				// THis is really for OncModelTable only
				JTable table = (JTable)e.getSource();
				int row = table.getSelectedRow();
				int column = table.getSelectedColumn();
				Object obj = table.getModel().getValueAt(row, column);
				//System.out.println("processTableSelection");
				//list.setSelectedIndex(index);
				if (e.getClickCount() == 2 ) {
						if ( obj instanceof Editable) {
								EditorFrame.showEditor((Editable)obj, null);
						}
						
						// select item
						// do double click action ( edit )
						// Table Model Needed HERE 
						// 	Object obj = table.getDataObject(index);
						// 								if ( obj instanceof Editable) {
						// 								// 		System.out.println("editing obj: " 
						// 								//+ ((AbstractPersistible)obj).getGUID() + 
						// 								// 				 " " + obj + " " + obj.getClass() );
						// 								EditorFrame.showEditor((Editable)obj, defaultSize);
						// 						}
				}
				else {
						if ( obj instanceof JButton) {
								((JButton)obj).doClick();
						}
						else {
								table.repaint();
						}
		// 				else { //if ( obj instanceof String) {
// 								// Find all cells that match the selected 
// 								// cell and highlight
// 								((OncModelTable)table).highlightMatches(obj);
// 						}
				}
		}

}

