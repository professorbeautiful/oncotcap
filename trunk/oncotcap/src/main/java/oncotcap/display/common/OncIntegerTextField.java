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

public class OncIntegerTextField extends OncTextField  {
		public OncIntegerTextField(Object editObj, 
															 String fieldName,
															 boolean showLabel) {
				super(editObj, fieldName, showLabel);
		}

		public Object getValue(){
				String fieldVal = textField.getText();
				if ( StringHelper.isNumeric(fieldVal) ) 
						return new Integer(fieldVal);
				else 
						return null;	
	}


// 		public static void main(String[] args) {
		
// 		// 		try {
// // 						UIManager.setLookAndFeel(WINDOWS);
// // 				}
// // 				catch (Exception ex) {
// // 						System.out.println("Failed loading L&F: ");
// // 				}
// 				JFrame f = new JFrame();
// 				 df = new DefaultFocusListener();
// // 				System.out.println("theText " + df.theText);
// 				OncIntegerTextField p = 
// 						new OncIntegerTextField(df, "MyVariable", true);
// 				f.getContentPane().add(p);
// 				f.addWindowListener
// 						(	new WindowAdapter() {
// 										public void windowClosing(WindowEvent e) {
// // 												System.out.println("theText " + df.theText);					
// 												System.exit(0);
// 										}
// 								});
// 				f.setSize(500,500);
// 				f.setVisible(true);
// 		}		
}
