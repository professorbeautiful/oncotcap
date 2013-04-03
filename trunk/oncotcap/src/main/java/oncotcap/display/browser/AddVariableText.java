package oncotcap.display.browser;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import java.lang.reflect.Array;

import javax.swing.event.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import oncotcap.datalayer.persistible.CodeBundle;

import oncotcap.display.common.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.ModifyVariableAction;
import oncotcap.display.common.ListDialog;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.*;
import java.awt.dnd.*;


/// Inner class
public class AddVariableText extends OncAbstractAction {

		private String actionName = null;
		private boolean isNew = false;
		private boolean isModal = false;
		private String createClassName = null;
		Object source = null;
		OntologyButtonPanel ontologyButtonPanel = null;
	
		public AddVariableText(String actionName) {
				super(actionName, null);
				this.actionName = actionName;
				// Set an accelerator key; this value is used by menu items
				putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("Alt-V"));
		}
	
		public void isModal(boolean m) {
				isModal = m;
				//System.out.println("isModal " + isModal);

		}

		public void actionPerformed(ActionEvent e) {
				Object obj = getBrowserActionSource(e);
				CodeBundle cb = null;
				Component cbe = null;
				if ( obj instanceof CodeBundleEditorPanel)
					cbe = (CodeBundleEditorPanel)obj;
				else if ( obj != null && obj instanceof Container)
					cbe = ComponentHelper.getParentOfType((Container)obj,
							CodeBundleEditorPanel.class);
				
				if ( cbe != null && cbe instanceof CodeBundleEditorPanel)
					cb = ((CodeBundleEditorPanel)cbe).getCodeBundle();
				
				
			
				if ( obj instanceof Container){
					
					Component comp = ComponentHelper.getFirstChildComponent((Container)obj,
							JTextComponent.class);
					
					if ( comp != null && comp instanceof JTextComponent ){
						VariableChooser vc = null;
						if ( cb != null ){
							// modal
							vc = new VariableChooser(cb.getProcessDeclaration(), cb, true, null, true);
							vc.setVisible(true);
						}
						if ( vc != null ) {
							Object selectedVariable = vc.getSelectedVariable();
							if ( selectedVariable == null )
								return;
							String selectedText = selectedVariable.toString();
							String currentText = ((JTextComponent)comp).getText();
							int caretPosition = ((JTextComponent)comp).getCaretPosition();
							StringBuilder newText = new StringBuilder();
							newText.append(currentText.substring(0,caretPosition));
							newText.append(selectedText);
							newText.append(currentText.substring(caretPosition));
							((JTextComponent)comp).setText(newText.toString());
						}
					}
				}
				
		}

	


}
