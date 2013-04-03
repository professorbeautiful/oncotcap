package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;

import oncotcap.util.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.browser.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.*;


public class AssignmentParameterEditor extends SubsetParameterEditorPanel {
	
		public AssignmentParameterEditor() {
				setLayout(new BorderLayout());
				filterEditorPanel = new FilterEditorPanel();
				filterEditorPanel.removeOperator(TcapLogicalOperator.AND);
				filterEditorPanel.removeOperator(TcapLogicalOperator.OR);
				filterEditorPanel.removeOperator(TcapLogicalOperator.NOT);
				booleanTree = (BooleanTree)filterEditorPanel.getTree();
				// Assignment is a one one editor by default
				OneOneSubsetParameterTH oneOneTransferHandler = 
						new OneOneSubsetParameterTH();
				booleanTree.setTransferHandler(oneOneTransferHandler);
				booleanTree.getModel().addTreeModelListener(this);
				((GenericTreeSelectionListener)booleanTree.getSelectionListener()).addDoubleClickListener(this);
				JPanel fPanel = new JPanel(new BorderLayout());
				JLabel fPanelLabel = new JLabel("Define assignment here");
				fPanelLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
				fPanel.add(fPanelLabel,
										BorderLayout.NORTH);
				fPanel.add(filterEditorPanel, BorderLayout.CENTER);
				add(fPanel, BorderLayout.CENTER);
				JPanel nameBox = new JPanel(new BorderLayout());
				String displayText = new String();
				nameField = new OncScrollableTextArea(displayText, "DISPLAY", false);
				nameField.setFont(new Font("Helvetica", Font.PLAIN, 11));
				nameField.setEnabled(true);
				nameField.setMinimumSize(new Dimension(500, 150));
				JLabel nameLabel = new JLabel("Assignment");
				nameLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
				nameBox.add(nameLabel, BorderLayout.NORTH);
				nameBox.add(nameField, BorderLayout.SOUTH);
				add(nameBox, BorderLayout.NORTH);
				setPreferredSize(new Dimension(700,500));
		}
		

}

