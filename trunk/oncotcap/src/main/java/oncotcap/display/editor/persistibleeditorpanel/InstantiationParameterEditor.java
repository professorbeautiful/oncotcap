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


public class InstantiationParameterEditor extends SubsetParameterEditorPanel {

	public InstantiationParameterEditor() {
				setLayout(new BorderLayout());
				SubsetParameterTransferHandler transferHandler = 
						new SubsetParameterTransferHandler();
				filterEditorPanel = new FilterEditorPanel();
				filterEditorPanel.removeOperator(TcapLogicalOperator.AND);
				filterEditorPanel.removeOperator(TcapLogicalOperator.OR);
				filterEditorPanel.removeOperator(TcapLogicalOperator.NOT);
				booleanTree = (BooleanTree)filterEditorPanel.getTree();
				booleanTree.setTransferHandler(transferHandler);
				//filterEditorPanel.edit(subsetFilter);
				booleanTree.getModel().addTreeModelListener(this);
				((GenericTreeSelectionListener)booleanTree.getSelectionListener()).addDoubleClickListener(this);
				JPanel fPanel = new JPanel(new BorderLayout());
				JLabel fPanelLabel = 
						new JLabel("Define instantiation parameters here");
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
				nameField.addDocumentListener(this);
				JLabel nameLabel = new JLabel("Instantiation parameters");
				nameLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
				nameBox.add(nameLabel, BorderLayout.NORTH);
				nameBox.add(nameField, BorderLayout.SOUTH);
				add(nameBox, BorderLayout.NORTH);
				setPreferredSize(new Dimension(700,600));
		}

}

