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
import oncotcap.util.CollectionHelper;

public class OncComboBox extends OncUiObject  {
		private final static String WINDOWS =
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		static DefaultFocusListener df = null;

		JComboBox comboBox = null;
		JLabel label = null;
		Object [] items = null;

		boolean showLabel = false;
		String fieldName = null;

		boolean isCollection = false;
		
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

		public OncComboBox(boolean showLabel) {
				this.showLabel = showLabel;
				init();
		}

		public OncComboBox(Object editObj, 
											 String fieldName,
											 boolean showLabel, 
											 Vector itemVector) {
				Object [] items = itemVector.toArray();
				init0(editObj, fieldName, showLabel, items);
		}
		public OncComboBox(Object editObj, 
											 String fieldName,
											 boolean showLabel, 
											 Object[] items) {
				init0(editObj, fieldName, showLabel, items);
		}
		
		private void init0(Object editObj,
											 String fieldName,
											 boolean showLabel, 
											 Object[] items) {
				this.showLabel = showLabel;
				this.fieldName = fieldName;
				this.items = items;
				init();
// 				System.out.println("Create text area: " + editObj);
				comboBox.putClientProperty("Field", fieldName);
				comboBox.putClientProperty("getMethod", null);
				comboBox.putClientProperty("setMethod", null);

				if ( editObj instanceof FocusListener) 
						comboBox.addFocusListener((FocusListener)editObj);
				// 	comboBox.addInputMethodListener((DefaultInputMethodListener)editObj);
				//				setData(editObj);
// 				System.out.println("Created text area: " + editObj);
		}

		private void init() {
				JPanel topPanel = new JPanel(new BorderLayout());
				GridBagLayout g = new GridBagLayout();
				GridBagConstraints c = new GridBagConstraints();
				setLayout(g);	
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridwidth = GridBagConstraints.REMAINDER;
				c.anchor = GridBagConstraints.SOUTHWEST;
				JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
				labelPanel.setPreferredSize(new Dimension(200,20));
				JLabel l = new JLabel(fieldName);
				l.setFont(new Font("Helvetica", Font.BOLD, 11));
				labelPanel.add(l);
				if ( showLabel ) 
						add(labelPanel,c);
				c.insets = new Insets(0,0,0,0);
				c.ipadx = 0;
				c.ipady = 0;
				c.weightx = 1;
				c.weighty = 1;
				c.gridwidth = GridBagConstraints.REMAINDER; 
				//c.gridheight = GridBagConstraints.REMAINDER; 
				c.anchor = GridBagConstraints.NORTHWEST;
				comboBox = new JComboBox(items);
				add(comboBox, c);
		}
		
		public void setData(Collection items) {
				Vector data = new Vector(items);
				//				list.setListData(data);
		}

		public Object getValue(){
				if ( isCollection() )
					return CollectionHelper.makeVector(comboBox.getSelectedItem());
				else 
					return comboBox.getSelectedItem();
		}

		public void setValue(Object obj){
				if ( obj instanceof String ) 
						comboBox.setSelectedItem((String)obj);
				else
						comboBox.setSelectedItem(obj);
		}
		
		public void setFont(Font f) {
				if ( comboBox != null)
						comboBox.setFont(f);
		}				
		public void setFont(Color c) {
				if ( comboBox != null)
						comboBox.setForeground(c);
		}

		public String toString() {
				if (label != null ) 
						return label.getText();
				return getClass().toString();
		}
		public void setIsCollection(boolean isCollection){
			this.isCollection = isCollection;
		}
		public boolean isCollection() {
			return this.isCollection;
		}
}
