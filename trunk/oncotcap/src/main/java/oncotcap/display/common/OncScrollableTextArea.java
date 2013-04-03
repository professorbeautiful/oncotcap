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

public class OncScrollableTextArea extends OncUiObject  {
		private final static String WINDOWS =
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		static DefaultFocusListener df = null;
		Icon EDIT_ICON = 
				oncotcap.util.OncoTcapIcons.getImageIcon("edit.jpg");
		Icon CREATE_ICON = 
				oncotcap.util.OncoTcapIcons.getImageIcon("create.jpg");
		Icon DELETE_ICON = 
				oncotcap.util.OncoTcapIcons.getImageIcon("delete.jpg");

		JTextArea textArea = null;
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

		public OncScrollableTextArea(boolean showButtons, boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				init();
		}

		public OncScrollableTextArea(Object editObj, 
												String fieldName,
												boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.fieldName = fieldName;
				init();
// 				System.out.println("Create text area: " + editObj);
				textArea.putClientProperty("Field", fieldName);
				textArea.putClientProperty("getMethod", null);
				textArea.putClientProperty("setMethod", null);
		

				if ( editObj instanceof FocusListener) 
						textArea.addFocusListener((FocusListener)editObj);
// 				textArea.addInputMethodListener((DefaultInputMethodListener)editObj);
				//				setData(editObj);
// 				System.out.println("Created text area: " + editObj);
		}

		private void init() {
				setLayout(new BorderLayout());	
				JPanel topPanel = new JPanel(new BorderLayout());
				GridBagLayout g = new GridBagLayout();
				GridBagConstraints c = new GridBagConstraints();
				//topPanel.setLayout(g);
			// 	g.columnWeights = new double[]{1.0f};
// 				g.rowWeights = new double[]{0.0f, 1.0f};
				c.fill = GridBagConstraints.BOTH;
				c.gridwidth = GridBagConstraints.REMAINDER;
				c.anchor = GridBagConstraints.SOUTHEAST;
				JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
				labelPanel.setPreferredSize(new Dimension(200,20));
				label = new JLabel(fieldName);
				label.setFont(new Font("Helvetica", Font.BOLD, 11));
				labelPanel.add(label);
				if ( showLabel ) 
						topPanel.add(labelPanel,BorderLayout.WEST);

				c.gridwidth = GridBagConstraints.RELATIVE; 
				c.anchor = GridBagConstraints.NORTHWEST;
				textArea = new JTextArea();
				// 				textArea.setPreferredSize(new Dimension(200,50));
				// Enable line-wrapping
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(false);
				textArea.setRows(40);
				
				// USe the tab to go to next field instead of inserting a tab in the 
				// text 
				// 	System.out.println("textArea.getActionMap() " 
				// 													 + textArea.getActionMap());
				// textArea.getActionMap().put(nextFocusAction.getValue(Action.NAME), 
				// 											nextFocusAction);
				// 				textArea.getActionMap().put(prevFocusAction.getValue(Action.NAME), 
				// 																		prevFocusAction);
				
				// 	System.out.println("textArea.getActionMap() " 
				// 													 + textArea.getActionMap().allKeys());
				
				JScrollPane scrollPane = new JScrollPane(textArea);
				// int hpolicy = pane.getHorizontalScrollBarPolicy();
				// JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
				
	// 			System.out.println(scrollPane.getVerticalScrollBarPolicy());
// 				System.out.println(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				
 				scrollPane.setPreferredSize(new Dimension(200,50));
				if ( showButtons || showLabel ) 
						add(topPanel, BorderLayout.NORTH);
				add(scrollPane, BorderLayout.CENTER);
				//add(scrollPane, c);

		}
		
		public void setLabel(String labelString) {
				label.setText(labelString);
		}
		public void setData(Collection items) {
				Vector data = new Vector(items);
				//				list.setListData(data);
		}

		public Object getValue(){
				return textArea.getText();
		}

		public void setValue(Object obj){
				if ( obj == null ) 
						textArea.setText(null);
				else if ( obj instanceof String ) 
						textArea.setText((String)obj);
				else
						textArea.setText(obj.toString());
		}
		
		public void setFont(Font f) {
				if ( textArea != null)
						textArea.setFont(f);
		}				
		public void setFont(Color c) {
				if ( textArea != null)
						textArea.setForeground(c);
		}

		public void addDocumentListener(DocumentListener l) {
				textArea.getDocument().addDocumentListener(l);
		}

// 		public void resize(Container container){
// 				// This object can grow in width and height
// 				// Get current size
// 				Dimension preferredSize = new Dimension(initialWidth, initialHeight);
// 				//getSize();
				
// 				// Determine what percentage of the container the ui 
// 				// object normally occupies
// 				double width = container.getPreferredSize().getWidth();
// 				double height = container.getPreferredSize().getHeight();
// 				double percWidth = initialWidth/width;
// 				double percHeight = initialHeight/height;
// 				double newWidth = width * percWidth;
// 				double newHeight =height * percHeight;
// 				System.out.println("initial-- " + initialWidth  + " " +initialHeight); 

// 				System.out.println("container-- " + width  + " " +height); 
// 				System.out.println("obj-- " + getWidth() + " " +getHeight() + " bounds " + getBounds()); 
// 				System.out.println("perc " + percWidth + " " +percHeight); 
// 				System.out.println("new " + newWidth + " " + newHeight); 
// 			// 	if ( newWidth != 0.0 &&  newHeight != 0.0 )
// // 						setBounds(getX(), getY(), (int)newWidth, (int)newHeight);
// // 				setPreferredSize(new Dimension((int)newWidth, (int)newHeight));
// 		}
// 		public void setBounds(int x,
// 													int y,
// 													int width,
// 													int height) {
		
// 				if ( initialX == -1 && initialY == -1 
// 						 && initialWidth == -1 && initialHeight == -1 ) {
									
	
// 						// initialize original positions
// 						this.initialX = x;
// 						this.initialY = y;
// 						this.initialWidth = width;
// 						this.initialHeight = height;
// 				}
// 			System.out.println("Setting bounds OSTA " + x + " " + y + " " + width + " " 
// 													 + height);
// 				super.setBounds(x,y,width,height);
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
				OncScrollableTextArea p = 
						new OncScrollableTextArea(df, "MyVariable", true);
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
