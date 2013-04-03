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

import java.awt.*;
import java.awt.event.*;

import java.awt.datatransfer.*;
import java.awt.dnd.*;

import java.util.*;
import java.lang.reflect.Array;
import javax.swing.*;
import javax.swing.table.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.OncTreeNode;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.Keyword;
import oncotcap.datalayer.persistible.EnumDefinition;
import oncotcap.datalayer.persistible.EnumLevel;
import oncotcap.datalayer.persistible.BooleanExpression;
import oncotcap.datalayer.persistible.UndefinedBooleanExpression;
import oncotcap.datalayer.persistible.EnumLevelList;

import oncotcap.datalayer.SearchText;
import oncotcap.datalayer.persistible.OntologyObjectName;
import oncotcap.datalayer.persistible.TcapLogicalOperator;
import oncotcap.datalayer.persistible.parameter.TcapString;
import oncotcap.display.editor.persistibleeditorpanel.*;
import edu.stanford.smi.protege.model.*;

public class OncTableCellRenderer extends DefaultTableCellRenderer {
		private static boolean nextNoFocus = false;

		public Component getTableCellRendererComponent(JTable table,
																									Object value,
																									boolean isSelected,
																									boolean expanded,
																									boolean leaf,
																									int row,
																									boolean hasFocus) {

				Component nodeComponent = this;
				setEnabled(table.isEnabled());
				setOpaque(true);
				setIcon(null);
				setText((value == null) ? "" : value.toString());

// 								else if ( value instanceof OncTreeNode 
// 										 && userObj instanceof 
// 													oncotcap.datalayer.persistible.EnumLevel) {
// 										nodeComponent = formatEnumLevel((OncTreeNode)value);
// 								}
// 								else if (userObj instanceof 
// 												 oncotcap.datalayer.persistible.EnumLevelList) {
									
// 								}

// 				System.out.println("table value " + value + " " + value.getClass());
// 				// TABLE NODE
// 								else if ( userObj  instanceof BooleanExpression ) {
// 										BooleanExpression expr = 
// 												(BooleanExpression)userObj;
// 										//										String testTable = "<html><body><TABLE border=\"1\"><CAPTION><EM>A test table with merged cells</EM></CAPTION><TR><TH rowspan=\"2\"><TH colspan=\"2\">Average <TH rowspan=\"2\">Red<BR>eyes<TR><TH>height<TH>weight<TR><TH>Males<TD>1.9<TD>0.003<TD>40%<TR><TH>Females<TD>1.7<TD>0.002<TD>43%</TABLE></body></html>";
			// 	if (isSelected )
// 						label.setBackground(Color.RED);
// 				nodeComponent = label;
				
				return nodeComponent;
		}

		public Component formatEnumLevel(OncTreeNode treeNode) {
				// Get enum and build a label
				Persistible pers = treeNode.getMetaData();
				Persistible userObj = (Persistible)treeNode.getUserObject();
				if ( pers != null ) {
						setText(pers.toString()+ " = " + userObj.toString());
				}
				else 
						setText(userObj.toString());
				setFont(new Font("Helvetica", Font.BOLD, 12));
				setIcon(null);
				return this;
		}		

		public Component formatPersistible(Persistible userObj)  {

				String text = userObj.toString();
				if ( userObj instanceof oncotcap.datalayer.persistible.StatementTemplate ||
						 userObj instanceof oncotcap.datalayer.persistible.StatementBundle)  {
						text = StringHelper.addHtmlStyle(text);
						text = StringHelper.htmlNoParagraphs(text);
				}
				if ( ((Persistible)userObj).showText() )
						setText(text);
				else 
						setText(null);
				ImageIcon opIcon = ((AbstractPersistible)userObj).getIcon();
				setIcon(opIcon);
				return this;
		}

		private Image findImage(Class cls) {
				Image image = null;
				File imageFile = null;
				// see if the image exists
				String clsNameOnly = StringHelper.className(cls.getName());
				try {
						// probably shouldn't put path in here 
						imageFile = new File
								("oncotcap/utilities/images/" + clsNameOnly 
								 + ".jpg");
				}
				catch (Exception e) {
						e.printStackTrace();
				}

				if ( imageFile == null || !imageFile.canRead()) {
					image = findImage(cls.getSuperclass());
				}
				else {
						try {
								image = ImageIO.read(imageFile);
						}
						catch (Exception io) {
								io.printStackTrace();
						}
				}
				return image;
		}

}


