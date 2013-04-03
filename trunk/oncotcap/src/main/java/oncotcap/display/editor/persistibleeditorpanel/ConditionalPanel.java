package oncotcap.display.editor.persistibleeditorpanel;

import java.awt.*;
import java.io.*;
import java.text.*;
import javax.print.attribute.*;
import java.awt.event.*;
import java.util.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

import oncotcap.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.browser.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.autogenpersistible.*;

public class ConditionalPanel extends CanvasEditorPanel 
		implements DropTargetListener {	
		
		private Object conditionalValue = null;
		ProbabilityTablePanel probPanel = null;
		JLabel label = new JLabel();
		String directions = "<Add condition here>";
		public ConditionalPanel(ProbabilityTablePanel probPanel) {
				//System.out.println("ConditionalPanel PROBPANEL" + probPanel);
				this.probPanel = probPanel;
				setBackground(OncBrowserConstants.MBColorPale);
				setLabelToDirections();
				add(label);
				new DropTarget(this, this);
				setPreferredSize( new Dimension(300,30));

		}
		
		private void setLabelToDirections() {
				label.setText(directions);
				label.setFont(OncBrowserConstants.DIRECTIONS_FONT_SMALL_ITALIC);
				label.setForeground(Color.WHITE);
		}
		public ProbabilityTablePanel getProbabilityTablePanel() {
				return this.probPanel;
		}

		public Object getValue(){ return conditionalValue; }
		public void setValue(Object cv) {
				conditionalValue = cv;
				updateLabel();
		}
		public void updateLabel() {
				if ( conditionalValue == null ) 
						setLabelToDirections();
				else {
						// bold font
						label.setText(conditionalValue.toString());
						label.setFont(OncBrowserConstants.DIRECTIONS_FONT_SMALL);
						revalidate();
				}
		}

		public void save(){}
		public  void edit(Object objectToEdit){}

		public void dragEnter(DropTargetDragEvent evt) {
				// Called when the user is dragging and enters this drop target.
		}
		public void dragOver(DropTargetDragEvent evt) {
				// Called when the user is dragging and moves over this drop target.
		}
		public void dragExit(DropTargetEvent evt) {
				// Called when the user is dragging and leaves this drop target.
		}
		public void dropActionChanged(DropTargetDragEvent evt) {
				// Called when the user changes the drag action between copy or move.
		}
		public void drop(DropTargetDropEvent evt) {
				try {
						Transferable t = evt.getTransferable();
						if ( t.isDataFlavorSupported(Droppable.genericTreeNode) ) {
								evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
								Object transferableData = 
										t.getTransferData(Droppable.genericTreeNode);
								GenericTreeNode transferNode = 
										(GenericTreeNode)transferableData;
								if ( transferNode.getUserObject() instanceof Keyword) {
										BooleanExpression bool = 
												BooleanExpression.dropKeywordCreateEnum
												((Keyword)transferNode.getUserObject(), false);
										//conditionalValue = bool.getLeftHandSide();
										// Let interested parties know that 
										// a boolean expression was dropped
										fireCanvasObjectChanged(new CanvasObjectChangeEvent
																						(CanvasObjectChangeEvent.ADD,
																						 bool,
																						 this));
								}
								evt.getDropTargetContext().dropComplete(true);
						}
						else if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
								evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
								String s = 
										(String)t.getTransferData(DataFlavor.stringFlavor);
								add(new JLabel(s));
								evt.getDropTargetContext().dropComplete(true);
								//process(s);
						} else {
								evt.rejectDrop();
						}
				} catch (IOException e) {
						evt.rejectDrop();
				} catch (UnsupportedFlavorException e) {
						evt.rejectDrop();
				}
		}
		
}
