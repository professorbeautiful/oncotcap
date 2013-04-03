package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import oncotcap.util.*;
import oncotcap.datalayer.Persistible;
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

public class UndefinedBooleanExpressionPanel extends JPanel {
		private static String ARROW = (new Character((char) 0x279c)).toString();
		private static Color ARMY_GREEN = new Color(106,107,75);

		public UndefinedBooleanExpressionPanel (UndefinedBooleanExpression expr) {
				setLayout(new FlowLayout(FlowLayout.LEFT));
				setBackground(Color.white);
				//setBorder
				//		 (BorderFactory.createLineBorder(Color.red, 
				//																								2));
				if ( expr.getLeftHandSide() instanceof Keyword 
						 &&  expr.getRightHandSide() instanceof EnumLevelList){
						Keyword keyword = (Keyword)expr.getLeftHandSide();
						EnumLevelList ll = (EnumLevelList)expr.getRightHandSide();
						//System.out.println("keyword " + keyword);
						JLabel keywordLabel = new JLabel(keyword.toString() + ARROW);
						//keywordLabel.setFont(new Font("Arial",10,Font.BOLD));
						add(keywordLabel);
						if ( ll.isNumericList() ) 
								add(getNumericLevels(ll));
						else
								add(getStringLevels(ll));
				}
				else
						add(new JLabel("Improperly defined boolean expression"));
		}

		private JPanel getNumericLevels(EnumLevelList ll) {
				JPanel levelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
				levelPanel.setBackground(Color.white);
				String labelString = "[ " + 
						ll.getMinAsString()
						+ " to " 
						+ ll.getMaxAsString()
						+ " ] Increment by " 
						+ ll.getIncrementAsString();
				JLabel levelLabel = new JLabel(labelString);
				levelLabel.setForeground(ARMY_GREEN);
				add(levelLabel);
				return levelPanel;
		}
		private JPanel getStringLevels(EnumLevelList ll) {
				JPanel levelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
				levelPanel.setBackground(Color.white);
				levelPanel.setForeground(Color.black);
				Iterator i = ll.getLevelIterator();
				JLabel levelLabel = null;
				EnumLevel level = null;
				while (i != null && i.hasNext() ) {
						level = (EnumLevel)i.next();
						levelLabel = new JLabel(level.toString() + "  | ");
						levelLabel.setForeground(ARMY_GREEN);
						levelPanel.add(levelLabel);
						//levelPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
				}
				return levelPanel;
		}


} 
