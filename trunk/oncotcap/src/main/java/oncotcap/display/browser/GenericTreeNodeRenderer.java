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
import java.awt.event.*;

import java.awt.datatransfer.*;
import java.awt.dnd.*;

import java.util.*;
import java.lang.reflect.Array;
import javax.swing.*;
import javax.swing.tree.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.DefaultTreeRendererComponent;
import oncotcap.display.common.OncTreeNode;
import oncotcap.display.common.StatementTreeRendererComponent;
import oncotcap.display.common.TreeRendererComponent;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.ProbabilityTable;

import oncotcap.datalayer.SearchText;
import oncotcap.datalayer.persistible.parameter.TcapString;
import oncotcap.display.editor.persistibleeditorpanel.*;
import edu.stanford.smi.protege.model.*;

public class GenericTreeNodeRenderer extends JPanel implements TreeCellRenderer {
		static FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		static ImageIcon checkIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon
				("check.gif");

		static DefaultTreeRendererComponent nodeLabel = new DefaultTreeRendererComponent();
		static StatementTreeRendererComponent statementLabel = new StatementTreeRendererComponent();
		static JLabel testNodeLabel = new JLabel();

		private static boolean nextNoFocus = false;
		private ImageIcon opIcon = null;

		public boolean isShowing(){
				return true;
		}
	
		public Component getTreeCellRendererComponent(JTree tree,
																									Object value,
																									boolean isSelected,
																									boolean expanded,
																									boolean leaf,
																									int row,
																									boolean hasFocus) {
				setLayout(flowLayout);
				removeAll();
				Component nodeComponent = null;

				//Component nodeComponent = this;

				// setSize(300, 23);
				nodeLabel.setEnabled(tree.isEnabled());
				//nodeLabel.setFont(tree.getFont());
				nodeLabel.setOpaque(false);
				nodeLabel.setIcon(null);

				//System.out.println("tree value " + value + " " + value.getClass());
				// TREE NODE
				if ( value instanceof DefaultMutableTreeNode) {
						Object userObj = ((DefaultMutableTreeNode)value).getUserObject();
						 		// System.out.println("tree userObj " //+ userObj 
// 					 	 + " " + userObj.getClass());
						if (  userObj instanceof GenericRootNodeObject ) {
								opIcon = getLargeLabel(userObj.toString());

								// System.out.println("root node is " + userObj 
								// + " for tree " + 
								// 																	 tree);
								//oncotcap.util.ForceStackTrace.showStackTrace();

								if ( opIcon != null) {
										nodeLabel.setIcon(opIcon);
										nodeLabel.setText("");
								}
								else {
										nodeLabel.setIcon(null);
										nodeLabel.setText(userObj.toString());
								}
						}
						else if ( userObj instanceof JLabel) {
								nodeLabel.setText(((JLabel)userObj).getText());
								nodeLabel.setIcon(((JLabel)userObj).getIcon());
						}
						else if (  userObj instanceof OntologyObjectName) {
								nodeLabel.setText("Any " + userObj.toString());
								// See if the user obj has an icon
								// find the icon
								if ( !"null".equals(userObj.toString()) ) 
										opIcon = 
												oncotcap.util.OncoTcapIcons.getImageIcon
												(userObj.toString() + ".jpg");

								if ( opIcon != null)
										nodeLabel.setIcon(opIcon);
						}
						else if (userObj instanceof SearchText) {
								ImageIcon containsIcon = 
										oncotcap.util.OncoTcapIcons.getImageIcon
										("contains.jpg");
								
								if ( containsIcon != null)
										nodeLabel.setIcon(containsIcon);
								nodeLabel.setText(userObj.toString());
						}
						else if (userObj instanceof String) {
								nodeLabel.setText((String)userObj);
								// See if the user obj has an icon
								// find the icon
								if ( !((String)userObj).equals("Root Node") ) {
										ImageIcon opIcon = 
												oncotcap.util.OncoTcapIcons.getImageIcon
												((String)userObj + ".jpg");
										if ( opIcon != null)
												nodeLabel.setIcon(opIcon);
										else 
												nodeLabel.setIcon(null);
								}
						}
					 	else if ( userObj instanceof TcapLogicalOperator ||
											userObj instanceof TcapString) {
								// if the operator has an icon use it
								ImageIcon opIcon = 
										oncotcap.util.OncoTcapIcons.getImageIcon
										(userObj.toString() 
										 + ".jpg");
								if ( opIcon != null) {
										nodeLabel.setIcon(opIcon);
										nodeLabel.setText("");
								}
								else {
										nodeLabel.setText(userObj.toString());
										nodeLabel.setIcon(null);
								}
 						}
						else if (userObj instanceof Class) {
								// Strip the package name 
								String className = StringHelper.className(userObj.toString());
								nodeLabel.setText(className);
								// find the icon
								ImageIcon opIcon = 
										oncotcap.util.OncoTcapIcons.getImageIcon
										(className + ".jpg");
								if ( opIcon != null)
										nodeLabel.setIcon(opIcon);
						}
						else if ( userObj instanceof Persistible ) {
								if ( userObj  instanceof UndefinedBooleanExpression ) {
										UndefinedBooleanExpression expr = 
												(UndefinedBooleanExpression)userObj;
										nodeComponent = new UndefinedBooleanExpressionPanel(expr);

								}
								else if ( userObj  instanceof BooleanExpression ) {
										BooleanExpression expr = 
												(BooleanExpression)userObj;
										JLabel label = new JLabel("Default String ") ;
										//System.out.println("RIGHT HAND IS " + expr.getRightHandSide());
									if (expr.getRightHandSide() instanceof EnumLevel) {
												label = new JLabel(expr.toString());
									 			label.setFont(new Font("Helvetica", Font.BOLD, 12));
												label.setOpaque(false);
										}
 									else if (expr.getRightHandSide() instanceof ProbabilityTable){
											label = new JLabel("<html><body>"
																				 + expr.getLeftHandSide().toString()
																				 + " P( "
																				 + 	expr.getRightHandSide()
																				 + " )</body></html>");
											}
									else {
											label = new JLabel(expr.toString());
											label.setFont(new Font("Helvetica", Font.BOLD, 12));
											label.setOpaque(false);
									}
										nodeComponent = label;
												 
								}
								else if ( value instanceof OncTreeNode 
										 && userObj instanceof 
													oncotcap.datalayer.persistible.EnumLevel) {
										nodeComponent = formatEnumLevel((OncTreeNode)value, 
																										nodeLabel);
								}
								else if (userObj instanceof 
												 oncotcap.datalayer.persistible.EnumLevelList) {
									
								}
								else {
										nodeComponent = formatPersistible((Persistible)userObj);
								}
						}
						// FOR ALL PERSISTIBLES when in merge mode
						if ( userObj instanceof Persistible) {
								Color borderColor = ((Persistible)userObj).getForeground();
								if ( nodeComponent instanceof JLabel && 
										 borderColor != Color.BLACK ) {
										ImageIcon mergeIcon = getMergeIcon(borderColor);
										if (mergeIcon != null)
												add(new JLabel(mergeIcon));
										setBorder
												(BorderFactory.createLineBorder(borderColor, 
																											1));
								}
								else 
										setBorder
										 (BorderFactory.createEmptyBorder());
 						}

				}
				// NOT A TREE NODE
				else if ( value instanceof String ) {
						((JLabel)nodeComponent).setText((String)value);
						// See if the user obj has an icon
						// find the icon
						ImageIcon opIcon = 
								oncotcap.util.OncoTcapIcons.getImageIcon
								((String)value + ".jpg");
						if ( opIcon != null)
								((JLabel)nodeComponent).setIcon(opIcon);
				}
				nodeLabel.setVisible(true);

				if ( nodeComponent != null ) {
						nodeComponent.setVisible(true);
						add(nodeComponent);
				}
				else {
						add(nodeLabel);
				}
				setOpaque(true);
				setVisible(true);
				if (isSelected) {
						setBackground(OncBrowserConstants.highlightBGColor);
				}
				else {
						setBackground(Color.white);
				}
				// if (  merge app 
				
				return this;
		}
		
		private ImageIcon getMergeIcon(Color borderColor) {
				if ( borderColor.equals(Color.green))
						return oncotcap.util.OncoTcapIcons.getImageIcon
						 ("green.jpg");
				if ( borderColor.equals(Color.red))
						return oncotcap.util.OncoTcapIcons.getImageIcon
						 ("red.jpg");
				if ( borderColor.equals(Color.blue))
						return oncotcap.util.OncoTcapIcons.getImageIcon
						 ("blue.jpg");
				if ( borderColor.equals(Color.gray))
						return oncotcap.util.OncoTcapIcons.getImageIcon
						 ("grey.jpg");
				return null; 
		}

		private ImageIcon getLargeLabel(String ontologyObjectName) {
				return oncotcap.util.OncoTcapIcons.getImageIcon
						(OntologyMap.getAbbreviation(ontologyObjectName) + ".jpg");
		}

		public Component formatEnumLevel(OncTreeNode treeNode, 
																		 JLabel nodeLabel) {
				// Get enum and build a label
				Persistible pers = treeNode.getMetaData();
				Persistible userObj = (Persistible)treeNode.getUserObject();
				if ( pers != null ) {
						nodeLabel.setText(pers.toString()+ " = " + userObj.toString());
				}
				else 
						nodeLabel.setText(userObj.toString());
				nodeLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
				nodeLabel.setIcon(null);
				return nodeLabel;
		}		

		public Component formatPersistible(Persistible userObj)
		{
			TreeRendererComponent comp = nodeLabel;
			
			if ( ((Persistible)userObj).showText() )
			{
				if (userObj instanceof StatementTemplate || userObj instanceof StatementBundle)
					comp = statementLabel;
				
				comp.setText(userObj.toString());
			}
			else 
				comp.setText(null);
			
			comp.setIcon(((AbstractPersistible)userObj).getIcon());
			return ((Component) comp);
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


