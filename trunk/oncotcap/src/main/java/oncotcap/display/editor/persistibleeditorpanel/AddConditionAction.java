package oncotcap.display.editor.persistibleeditorpanel;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import java.lang.reflect.Array;
import javax.swing.event.*;

import oncotcap.display.browser.*;
import oncotcap.util.ComponentHelper;
import oncotcap.display.common.*;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.editor.EditorFrame;

import oncotcap.datalayer.persistible.*;

public class AddConditionAction extends OncAbstractAction {
	
	
	GenericTree tree = null;
	Object source = null;
	
	public AddConditionAction(String actionName) {
		super(actionName);
	}
	public void setTree(GenericTree tree) {
		this.tree = tree;
	}
	public GenericTree getTree() {
		return this.tree;
	}
	public void setSource(Object source) {
		this.source = source;
	}
	public Object getSource() {
		return this.source;
	}
	
	public void actionPerformed(ActionEvent e) {
		//get selected item
		if ( getTree() != null ) {
			Object obj = 
				getTree().getLastSelectedPathComponent();
			Object userObject = null;
			if ( obj != null && obj instanceof DefaultMutableTreeNode &&
					((DefaultMutableTreeNode)obj).getUserObject() 
					instanceof Keyword) {
				System.out.println("add condition " 
						+ ((DefaultMutableTreeNode)obj).getUserObject());
				addConditionToTable(obj);
			}
		}
		else if ( source instanceof Keyword ) {
			System.out.println("add probability  keyword" + source);
		}
		else if ( e.getSource() instanceof ParameterEditor){
			System.out.println("PARAMETER EDITOR" + ((ParameterEditor)e.getSource()).getValue());
		}
		else if ( e.getSource() instanceof OntologyTree){
			System.out.println("ONTOLOGY TREE " );
			Object obj = ((OntologyTree)e.getSource()).getTree().getLastSelectedPathComponent();
			if ( obj != null && obj instanceof DefaultMutableTreeNode &&
					((DefaultMutableTreeNode)obj).getUserObject() 
					instanceof Keyword) {
				setTree(((OntologyTree)e.getSource()).getTree());
				addConditionToTable(obj);
			}
			else {
				JOptionPane.showMessageDialog
				(null, 
				"Please select a Keyword to add to probability table.");
			}
		}
		else {
			System.out.println("AddProbablilityAction else " + e.paramString() + 
					" -- " + e.getSource());
			// silent error
			
		}
	}
	
	private void addConditionToTable(Object obj) {
		Keyword keyword = (Keyword)((DefaultMutableTreeNode)obj).getUserObject();
		// Get the parent ConditionalParameterEditor
		ConditionalParameterEditor paramEditor = 
			(ConditionalParameterEditor)ComponentHelper.getParentOfType((Component)getTree(), ConditionalParameterEditor.class);
		ConditionalParameterEditor.DroppableCanvas canvas = paramEditor.getCanvas();
		
		Component[] probTables = canvas.getProbabilityTablePanels();
		CanvasObjectChangeEvent evt = null;
		if ( probTables != null &&
				probTables.length > 0 ) {
			BooleanExpression bool = 	
				BooleanExpression.dropKeywordCreateEnum(keyword, 
						false);
			evt = 
				new CanvasObjectChangeEvent(CanvasObjectChangeEvent.ADD, 
						bool, ((ProbabilityTablePanel)probTables[0]).getConditionalPanel() );
			canvas.canvasObjectChanged( evt );
			
		}
		
	}
}
