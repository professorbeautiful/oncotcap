package oncotcap.display.editor.persistibleeditorpanel;
// File: JComponentCellEditor.java
// Author: Zafir Anjum
import java.awt.*;
import java.awt.Component;
import java.awt.event.*;
import java.awt.AWTEvent;
import java.lang.Boolean;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.EventObject;
import javax.swing.tree.*;
import java.io.Serializable;
import javax.swing.*;


public class JComponentCellEditor implements TableCellEditor, TreeCellEditor,
Serializable {
	
	protected EventListenerList listenerList = new EventListenerList();
	transient protected ChangeEvent changeEvent = null;
	
	protected JComponent editorComponent = null;
	protected JComponent container = null;		// Can be tree or table
	
	
	public Component getComponent() {
		return editorComponent;
	}
	
	
	public Object getCellEditorValue() {
		return editorComponent;
	}
	
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}
	
	public boolean shouldSelectCell(EventObject anEvent) {
		if( editorComponent != null && anEvent instanceof MouseEvent
			&& ((MouseEvent)anEvent).getID() == MouseEvent.MOUSE_PRESSED )
		{
            Component dispatchComponent = SwingUtilities.getDeepestComponentAt(editorComponent, 3, 3 );
			MouseEvent e = (MouseEvent)anEvent;
			MouseEvent e2 = new MouseEvent( dispatchComponent, MouseEvent.MOUSE_RELEASED,
				e.getWhen() + 100000, e.getModifiers(), 3, 3, e.getClickCount(),
				e.isPopupTrigger() );
			dispatchComponent.dispatchEvent(e2); 
			e2 = new MouseEvent( dispatchComponent, MouseEvent.MOUSE_CLICKED,
				e.getWhen() + 100001, e.getModifiers(), 3, 3, 1,
				e.isPopupTrigger() );
			dispatchComponent.dispatchEvent(e2); 
		}
		return false;
	}
	
	public boolean stopCellEditing() {
		fireEditingStopped();
		return true;
	}
	
	public void cancelCellEditing() {
		fireEditingCanceled();
	}
	
	public void addCellEditorListener(CellEditorListener l) {
		listenerList.add(CellEditorListener.class, l);
	}
	
	public void removeCellEditorListener(CellEditorListener l) {
		listenerList.remove(CellEditorListener.class, l);
	}
	
	protected void fireEditingStopped() {
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==CellEditorListener.class) {
				// Lazily create the event:
				if (changeEvent == null)
					changeEvent = new ChangeEvent(this);
				((CellEditorListener)listeners[i+1]).editingStopped(changeEvent);
			}	       
		}
	}
	
	protected void fireEditingCanceled() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==CellEditorListener.class) {
				// Lazily create the event:
				if (changeEvent == null)
					changeEvent = new ChangeEvent(this);
				((CellEditorListener)listeners[i+1]).editingCanceled(changeEvent);
			}	       
		}
	}
	
	// implements javax.swing.tree.TreeCellEditor
	public Component getTreeCellEditorComponent(JTree tree, Object value,
		boolean isSelected, boolean expanded, boolean leaf, int row) {
		String         stringValue = tree.convertValueToText(value, isSelected,
			expanded, leaf, row, false);
		System.out.println("Editor for >> " + value.getClass());
		// editorComponent = (JComponent)value;
		container = tree;
		//return editorComponent;
		return new BooleanExpressionEditorPanel(); 
	// 	if ( value instanceof JComponent)
// 				return (JComponent)value;
// 		else
// 				return new JRadioButton("can't touch this");
	}
	
	// implements javax.swing.table.TableCellEditor
	public Component getTableCellEditorComponent(JTable table, Object value,
		boolean isSelected, int row, int column) {
		
		editorComponent = (JComponent)value;
		container = table;
		// if ( value instanceof JComponent)
// 				return (JComponent)value;
// 		else
		if ( value instanceof DefaultMutableTreeNode) {
				if (((DefaultMutableTreeNode)value).getUserObject() instanceof oncotcap.datalayer.persistible.EnumLevelList) {
				BooleanExpressionEditorPanel be = 
						new BooleanExpressionEditorPanel(); 
				if (isSelected)
						be.setBackground(Color.RED);
				return be;
				}
		}
		return null;
		//return editorComponent;
	}
	
} // End of class JComponentCellEditor
