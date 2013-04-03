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
import oncotcap.datalayer.autogenpersistible.ConditionalTableParameter;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.editor.EditorFrame;

import oncotcap.datalayer.persistible.*;

public class AddProbabilityAction extends OncAbstractAction {
	

		GenericTree tree = null;
		Object source = null;

		public AddProbabilityAction(String actionName) {
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
			System.out.println("add probability " + e.getSource());
			if ( e.getSource() instanceof OntologyTree){
				System.out.println("ONTOLOGY TREE " );
				Object obj = ((OntologyTree)e.getSource()).getTree().getLastSelectedPathComponent();
				if ( obj != null && obj instanceof DefaultMutableTreeNode ) {
					setTree(((OntologyTree)e.getSource()).getTree());
					addProbabilityToTable((DefaultMutableTreeNode)obj);
				}
				else {
					JOptionPane.showMessageDialog
					(null, 
					"Please select a Keyword to add to probability table.");
				}
			}
			else if ( e.getSource() instanceof ParameterEditor){
				System.out.println("PARAMETER EDITOR" + ((ParameterEditor)e.getSource()).getValue());
				Object paramObj = ((ParameterEditor)e.getSource()).getValue();
				if (paramObj instanceof ConditionalTableParameter ){
					System.out.println(" IS THIS A CONDITIONALPATRAMETEREDITOR " );
				}
			}
//			else if ( getTree() != null ) {
//				Object obj = getTree().getLastSelectedPathComponent();
//				if ( obj != null && obj instanceof DefaultMutableTreeNode)
//					addProbabilityToTable((DefaultMutableTreeNode)obj);
//			}
//			else if ( source instanceof Keyword ) {
//				// CanvasObjectChangeEvent(int changeEventType,
////				Object changedObject,
////				Object caller)
//				System.out.println("add probability  keyword" + source);
//			}
//			
			else {
				System.out.println("AddProbablilityAction else " + e.paramString() + 
						" -- " + e.getSource());
				// silent error
//				JOptionPane.showMessageDialog
//				(null, 
//				"Please select a Keyword to add to probability table.");
			}
			
		}
		
		private void addProbabilityToTable(DefaultMutableTreeNode obj) {
			System.out.println("add probability " 
					+ obj.getUserObject());
			Keyword keyword = null;
			if ( obj.getUserObject() instanceof Keyword)
				keyword = (Keyword)obj.getUserObject();
			if ( keyword == null )
				return; // this operation can't be done here
			Vector levels = keyword.getAssociatedLevelLists();
			if ( levels == null || levels.size() <= 0 ) {
				JOptionPane.showMessageDialog
				(null, 
				"This characteristic does not have any associated level list. Please add Level List using the LL+ button.");
			}
			else {
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
					if ( bool.getRightHandSide() == null )
						return;
					evt = 
						new CanvasObjectChangeEvent(CanvasObjectChangeEvent.ADD, 
								bool, probTables[0] );
					canvas.canvasObjectChanged( evt );
				}
				else {
					paramEditor.addKeywordToMainCanvas(keyword);
				}
			}
		}

}
