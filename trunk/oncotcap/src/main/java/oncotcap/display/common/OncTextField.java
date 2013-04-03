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
import oncotcap.util.StringHelper;

public class OncTextField extends OncUiObject  {
		static DefaultFocusListener df = null;
		public JTextField textField = null;
		public JLabel label = null;

		public boolean showButtons = false;
		public boolean showLabel = false;
		public String fieldName = null;

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
		public OncTextField() {
				init();
		}
		public OncTextField(boolean showButtons, boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				init();
		}

		public OncTextField(Object editObj, 
												String fieldName,
												boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.fieldName = fieldName;
				init();
		}

		public void init() {
				GridBagLayout g = new GridBagLayout();
				GridBagConstraints c = new GridBagConstraints();
				setLayout(g);
				c.gridwidth = GridBagConstraints.REMAINDER;
				c.insets = new Insets(0,0,0,0); //t,l,b,r
				c.fill = GridBagConstraints.HORIZONTAL;
				c.anchor = GridBagConstraints.SOUTHWEST;
				 JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
				labelPanel.setPreferredSize(new Dimension(200,20));
				JLabel l = new JLabel(fieldName);
				l.setFont(new Font("Helvetica", Font.BOLD, 11));
				labelPanel.add(l);
				if ( showLabel ) 
						add(labelPanel,c);
				c.insets = new Insets(0,0,0,0);
				c.weightx = 1;
				c.weighty = 1;
				c.gridwidth = GridBagConstraints.REMAINDER; 
				c.gridheight = GridBagConstraints.REMAINDER; 
				c.anchor = GridBagConstraints.NORTHWEST;
				textField = new JTextField();
				add(textField,c);

		}
		
		public void setData(Collection items) {
				Vector data = new Vector(items);
				//				list.setListData(data);
		}

		public Object getValue(){
				String fieldVal = textField.getText();
				return fieldVal;
	}

		public void setValue(Object obj){
				if ( obj == null ) 
						textField.setText((String)null);
				else if ( obj instanceof String ) 
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
// 				//setSize(new Dimension((int)newWidth, (int)newHeight));
// 		}

		public String toString() {
				if (label != null ) 
						return label.getText();
				return getClass().toString();
		}

		public static void main(String[] args) {
		
			// 	try {
// 						UIManager.setLookAndFeel(WINDOWS);
// 				}
// 				catch (Exception ex) {
// 						System.out.println("Failed loading L&F: ");
// 				}
				JFrame f = new JFrame();
				 df = new DefaultFocusListener();
// 				System.out.println("theText " + df.theText);
				OncTextField p = 
						new OncTextField(df, "MyVariable", true);
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
