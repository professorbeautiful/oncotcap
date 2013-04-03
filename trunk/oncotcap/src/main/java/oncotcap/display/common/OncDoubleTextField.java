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

import oncotcap.display.common.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.CodeBundle;

public class OncDoubleTextField extends OncUiObject  {
		private final static String WINDOWS =
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		static DefaultFocusListener df = null;
		Icon EDIT_ICON = 
				oncotcap.util.OncoTcapIcons.getImageIcon("edit.jpg");
		Icon CREATE_ICON = 
				oncotcap.util.OncoTcapIcons.getImageIcon("create.jpg");
		Icon DELETE_ICON = 
				oncotcap.util.OncoTcapIcons.getImageIcon("delete.jpg");

		JTextField textField = null;
		JLabel label = null;

		boolean showButtons = false;
		boolean showLabel = false;
		String fieldName = null;

		// The actions
		Action nextFocusAction = new AbstractAction("Move Focus Forwards") {
						public void actionPerformed(ActionEvent evt) {
								System.out.println("Move Focus Forwards");
								((Component)evt.getSource()).transferFocus();
						}
				};
		Action prevFocusAction = new AbstractAction("Move Focus Backwards") {
						public void actionPerformed(ActionEvent evt) {
								((Component)evt.getSource()).transferFocusBackward();
						}
				};

		public OncDoubleTextField(boolean showButtons, boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				init();
		}

		public OncDoubleTextField(Object editObj, 
												String fieldName,
												boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.fieldName = fieldName;
				init();
// 				System.out.println("Create text area: " + editObj);
				textField.putClientProperty("Field", fieldName);
				textField.putClientProperty("getMethod", null);
				textField.putClientProperty("setMethod", null);
				textField.addFocusListener((FocusListener)editObj);
// 				textField.addInputMethodListener((DefaultInputMethodListener)editObj);
				//				setData(editObj);
// 				System.out.println("Created text area: " + editObj);
		}

		private void init() {
				
				resizeHeight = false;
				GridBagLayout g = new GridBagLayout();
				GridBagConstraints c = new GridBagConstraints();
				setLayout(g);
				g.columnWeights = new double[]{1.0f};
				g.rowWeights = new double[]{0.0f, 1.0f};
				c.gridwidth = GridBagConstraints.REMAINDER;
				c.anchor = GridBagConstraints.SOUTHWEST;
				c.fill = GridBagConstraints.HORIZONTAL;
				c.insets = new Insets(10,8,1,8); //t,l,b,r

				if ( showLabel ) 
						add(new JLabel(fieldName),c);
				c.gridwidth = GridBagConstraints.RELATIVE; 
				c.anchor = GridBagConstraints.NORTHWEST;
				c.fill = GridBagConstraints.HORIZONTAL;

				textField = new JTextField();
				textField.setPreferredSize(new Dimension(200,50));
				textField.getActionMap().put(nextFocusAction.getValue(Action.NAME), 
																		nextFocusAction);
				textField.getActionMap().put(prevFocusAction.getValue(Action.NAME), 
																		prevFocusAction);
				
				c.insets = new Insets(5,8,1,8); //t,l,b,r
				add(textField, c);

		}
		
		public void setData(Collection items) {
				Vector data = new Vector(items);
				//				list.setListData(data);
		}

		public Object getValue(){
				return new Double(textField.getText());
		}

		public void setValue(Object obj){
				if ( obj instanceof String ) 
						textField.setText((String)obj);
				else
						textField.setText(obj.toString());
		}

// 		public void resize(Container container){
// 				// This object can grow in width only
// 				// Get current size
// 				Dimension preferredSize = getSize();
// 				// Determine what percentage of the container the ui 
// 				// object normally occupies
// 				double width = container.getPreferredSize().getWidth();
// 				double height = container.getPreferredSize().getHeight();
// 				double percWidth = preferredSize.getWidth()/width;
// 				double percHeight = preferredSize.getHeight()/height;
// 				double newWidth = container.getSize().getWidth() * percWidth;
// 				double newHeight = container.getSize().getHeight() * percHeight;
// 				//setBounds(newX, newY, (int)newWidth, (int)newHeight);
// 		}

		public String toString() {
				if (label != null ) 
						return label.getText();
				return getClass().toString();
		}

		public static void main(String[] args) {
		
				try {
						UIManager.setLookAndFeel(WINDOWS);
				}
				catch (Exception ex) {
						System.out.println("Failed loading L&F: ");
				}
				JFrame f = new JFrame();
				 df = new DefaultFocusListener();
// 				System.out.println("theText " + df.theText);
				OncDoubleTextField p = 
						new OncDoubleTextField(df, "MyVariable", true);
				f.getContentPane().add(p);
				f.addWindowListener
						(	new WindowAdapter() {
										public void windowClosing(WindowEvent e) {
// 												System.out.println("theText " + df.theText);					
												System.exit(0);
										}
								});
				f.setSize(500,500);
				f.setVisible(true);
		}		
}
